package com.asm.tavern.domain.model.audio

import org.json.JSONObject
import org.slf4j.ext.XLogger
import org.slf4j.ext.XLoggerFactory


import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration


class SpotifyService {
    private static final XLogger logger = XLoggerFactory.getXLogger(SpotifyService.class)
    String token
    final String client_secret
    final String client_id
    final String URI_CHECK_STRING = "spotify"
    final String API_PLAYLIST_STRING =  "https://api.spotify.com/v1/playlists/{id}/tracks"
    final String API_TRACK_STRING =  "https://api.spotify.com/v1/tracks/{id}"
    final String API_CLIENT_CREDENTIALS_TOKEN_STRING = "https://accounts.spotify.com/api/token"
    final String TRACK_STRING = "track[/:]"
    final String PLAYLIST_STRING = "playlist/"
    final String HEADER_AUTHORIZATION_KEY = "Authorization"
    final String HEADER_CONTENT_TYPE_KEY = "Content-Type"
    final String HEADER_CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded"
    final String BODY_KEY_GRANT_TYPE = "grant_type"
    final String RESPONSE_KEY_ACCESS_TOKEN = "access_token"
    final String RESPONSE_KEY_TRACK_TITLE = "name"
    final String BODY_VALUE_CLIENT_CREDENTIALS = "client_credentials"
    final String PREFIX_BASIC_TOKEN = "Basic "
    final String PREFIX_BEARER_TOKEN = "Bearer "
    final Integer TIMEOUT_DURATION = 3

    SpotifyService(String client_id, String client_secret) {
        this.client_id = client_id
        this.client_secret = client_secret
        this.token = getAuthorizationToken()
    }

    String getClientId() {
        return client_id
    }

    String getClientSecret() {
        return client_secret
    }

    private void getAuthorizationToken(){
        String encodedClientIdSecret = Base64.encoder.encodeToString((client_id + ':' + client_secret).getBytes())

        try{
            HttpResponse<String> response
            HttpClient httpClient = HttpClient.newHttpClient()
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_CLIENT_CREDENTIALS_TOKEN_STRING))
                    .header(HEADER_AUTHORIZATION_KEY, PREFIX_BASIC_TOKEN + encodedClientIdSecret)
                    .header(HEADER_CONTENT_TYPE_KEY, HEADER_CONTENT_TYPE_VALUE)
                    .timeout(Duration.ofSeconds(TIMEOUT_DURATION))
                    .POST(HttpRequest.BodyPublishers.ofString("${BODY_KEY_GRANT_TYPE}=${BODY_VALUE_CLIENT_CREDENTIALS}"))
                    .build()

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            JSONObject jsonObject = new JSONObject(response.body())
            this.token = jsonObject.getString("access_token")
        }
        catch (Exception e){
            logger.error("There was an error retrieving the access token from spotify.")
        }
    }

    Optional<List<Song>> getListOfSongsFromURL(String spotifyUrl){
        if(spotifyUrl.contains(URI_CHECK_STRING)){
            List<Song> songTitleList = new ArrayList<>()
            String splitSpotifyUrl = ""
            boolean isTrack = false
            boolean isPlaylist = false
            HttpResponse<String> response
            getAuthorizationToken()

            try{
                if(spotifyUrl.contains(TRACK_STRING)){
                    isTrack = true
                    splitSpotifyUrl = spotifyUrl.split(TRACK_STRING)[1]
                }
                else if(spotifyUrl.contains(PLAYLIST_STRING)){
                    isPlaylist = true
                    splitSpotifyUrl = spotifyUrl.split(PLAYLIST_STRING)[1]
                }
                else{
                    logger.debug("SpotifyURL did not contain track or playlist.")
                }
            }
            catch (NullPointerException e){
                logger.error("Unable to split spotifyUrl: ${spotifyUrl}")
            }

            if(!splitSpotifyUrl.isEmpty()){
                HttpClient httpClient = HttpClient.newHttpClient()

                if(isTrack){
                    URI apiURI = new URI(API_TRACK_STRING.replaceFirst("(\\{id})", splitSpotifyUrl))
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(apiURI)
                        .header(HEADER_AUTHORIZATION_KEY, PREFIX_BEARER_TOKEN + token)
                        .timeout(Duration.ofSeconds(TIMEOUT_DURATION))
                        .GET()
                        .build()

                    response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
                    String songTitle = new JSONObject(response.body()).getString(RESPONSE_KEY_TRACK_TITLE)

                    songTitleList.add(new Song(new SongId(songTitle), new URI("")))
                }
                else if(isPlaylist){
                    URI apiURI = new URI(API_PLAYLIST_STRING.replaceFirst("(\\{id})", splitSpotifyUrl))
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(apiURI)
                            .header(HEADER_AUTHORIZATION_KEY, PREFIX_BEARER_TOKEN + token)
                            .timeout(Duration.ofSeconds(TIMEOUT_DURATION))
                            .GET()
                            .build()

                    response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
                    JSONObject responseJSON = new JSONObject(response.body())
                    responseJSON.getJSONArray("items").collect().forEach(track ->{
                        String songTitle = track.getAt("track").getAt("name").toString()
                        songTitleList.add(new Song(new SongId(songTitle), new URI("")))
                    })
                }
            }
            if(!songTitleList.isEmpty())
                return Optional.of(songTitleList)
        }
    }
}
