package com.asm.tavern.discord.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.interactions.components.*
import net.dv8tion.jda.api.interactions.components.buttons.Button

import java.time.Duration
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.function.Function

class TrackScheduler extends AudioEventAdapter {
	private final AudioPlayer player
	private BlockingQueue<AudioTrack> queue
	private TextChannel textChannel
	private final int maxRetryCount = 3
	private int retryCount = 0

	/**
	 * @param player The audio player this scheduler uses
	 */
	TrackScheduler(AudioPlayer player) {
		this.player = player
		this.queue = new LinkedBlockingQueue<>()
	}

	/**
	 * Add the next track to queue or play right away if nothing is in the queue.
	 *
	 * @param track The track to play or add to queue.
	 */
	void queue(AudioTrack track) {
		// Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
		// something is playing, it returns false and does nothing. In that case the player was already playing so this
		// track goes to the queue instead.
		if (!player.startTrack(track, true)) {
			queue.offer(track)
		}
	}

	void playNext(AudioTrack track) {
		// possibly this should just be an error that nothing is currently in the queue and play command should be used instead,
		// but if nothing is in the queue and you attempt to playNext you probably just want the song to play right?
		if (!player.startTrack(track, true)) {
			// add new song to queue position 0 ie: next
			BlockingQueue<AudioTrack> modifiedQueue = new LinkedBlockingQueue<>()
			List<AudioTrack> songList = queue.toList()
			songList.add(0, track)
			songList.forEach(modifiedQueue::add)

			this.queue = modifiedQueue
		}
	}

	/**
	 * Start the next track, stopping the current one if it is playing.
	 */
	void nextTrack() {
		// Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
		// giving null to startTrack, which is a valid argument and will simply stop the player.
		player.startTrack(queue.poll(), false)
	}

	void skip(int amount) {
		(1..<amount).forEach({ _ -> queue.poll()})
		nextTrack()
	}

	void stopAndClear() {
		clear()
		player.stopTrack()
	}

	void clear() {
		queue.clear()
	}

	void pause() {
		player.setPaused(true)
	}

	void unpause() {
		player.setPaused(false)
	}

    boolean getIsPaused() {
        player.paused
    }

	void shuffle() {
		// Shuffle and reconstruct the Queue
		BlockingQueue<AudioTrack> shuffledQueue = new LinkedBlockingQueue<>()
		List<AudioTrack> songList = queue.toList()
		Collections.shuffle(songList)
		songList.forEach(shuffledQueue::add)

		this.queue = shuffledQueue
	}

	void setChannelId(TextChannel textChannel) {
		this.textChannel = textChannel
	}

	BlockingQueue<AudioTrack> getQueue() {
		queue
	}

	AudioTrack getNowPlaying() {
		player.getPlayingTrack()
	}

	@Override
	void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		// Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
		if (endReason.mayStartNext) {
			nextTrack()
		}
	}

	@Override
	void onTrackStart(AudioPlayer player, AudioTrack track){
		Function<Duration, String> formatTime = (Duration duration) -> {
			String.format("%d:%2d", (duration.getSeconds()/60).intValue(), (duration.getSeconds()%60).intValue()).replace(" ", "0")
		}

		Function<String, String> getVideoImageID = (String videoUrl) -> {
			String videoImageId = videoUrl.split("(?<=watch\\?v=)")[1]
			videoUrl = String.format("https://img.youtube.com/vi/%s/sddefault.jpg", videoImageId)
		}

		String videoImgUrl = getVideoImageID(track.info.uri.toString())
		EmbedBuilder eb = new EmbedBuilder()
		//eb.setTitle(track.info.title, track.info.uri) // large hyperlink
		eb.setAuthor(track.info.author, track.info.uri) // , videoImgUrl) image for author top left
		//eb.setImage(videoImgUrl) // Bottom large image
		eb.setThumbnail(videoImgUrl) // Top right corner image
		eb.setDescription("Now Playing: ${track.info.title}")
		eb.addField("Duration:", "${formatTime(Duration.ofMillis(player.playingTrack.position))}/${formatTime(Duration.ofMillis(track.info.length))}", false)
		eb.setColor(0x5865F2) // blurple

		textChannel.sendMessageEmbeds(eb.build())
                .setActionRow(
                        Button.primary("skip", "Skip"),
						Button.primary("shuffle", "Shuffle"),
						Button.primary("pause", "Play/Pause"),
                )
                .queue()
	}

	@Override
	void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		// Retry X times.
		if(retryCount < maxRetryCount)
		{
			retryCount++
			textChannel.sendMessage("Failure playing track: " + track.info.title + " attempting to retry. Attempt Number: " + retryCount).queue()
			player.startTrack(track.makeClone(), false)
		}
		else{
			textChannel.sendMessage("Failed to play track: " + track.info.title + " " + retryCount + " times, track will be skipped. Video may be unavailable."  + retryCount).queue()
			retryCount = 0
		}
	}

}
