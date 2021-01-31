package com.asm.tavern.discord.audio

import com.asm.tavern.domain.model.audio.AudioTrackInfo

class AudioTrackInfoAdapter implements AudioTrackInfo {
	private final com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo audioTrack

	AudioTrackInfoAdapter(com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo audioTrack) {
		this.audioTrack = audioTrack
	}

	@Override
	String getTitle() {
		audioTrack.title
	}

	@Override
	URL getUrl() {
		new URL(audioTrack.uri)
	}
}
