package com.tavern.audio

import com.tavern.domain.model.discord.VoiceChannelId
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager

class GuildMusicManager {
	private final AudioPlayer player
	private final TrackScheduler trackScheduler
	VoiceChannelId voiceChannelId

	GuildMusicManager(AudioPlayerManager manager, VoiceChannelId voiceChannelId) {
		this.player = manager.createPlayer()
		this.trackScheduler = new TrackScheduler(player)
		player.addListener(trackScheduler)
		this.voiceChannelId = voiceChannelId
	}

	AudioPlayerSendHandler getSendHandler() {
		new AudioPlayerSendHandler(player)
	}

	TrackScheduler getScheduler() {
		trackScheduler
	}

}
