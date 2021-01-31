package com.asm.tavern.discord.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler

import java.nio.Buffer
import java.nio.ByteBuffer

class AudioPlayerSendHandler implements AudioSendHandler {
	private final AudioPlayer audioPlayer
	private final ByteBuffer buffer
	private final MutableAudioFrame frame

	AudioPlayerSendHandler(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer
		this.buffer = ByteBuffer.allocate(1024)
		this.frame = new MutableAudioFrame()
		this.frame.setBuffer(buffer)
	}

	@Override
	boolean canProvide() {
		audioPlayer.provide(frame)
	}

	@Override
	ByteBuffer provide20MsAudio() {
		((Buffer) buffer).flip()
		buffer
	}

	@Override
	boolean isOpus() {
		true
	}

}
