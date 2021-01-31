package com.asm.tavern.discord.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class TrackScheduler extends AudioEventAdapter {
	private final AudioPlayer player
	private final BlockingQueue<AudioTrack> queue

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

	/**
	 * Start the next track, stopping the current one if it is playing.
	 */
	void nextTrack() {
		// Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
		// giving null to startTrack, which is a valid argument and will simply stop the player.
		player.startTrack(queue.poll(), false)
	}

	void stopAndClear() {
		queue.clear()
		player.stopTrack()
	}

	void pause() {
		player.setPaused(true)
	}

	void unpause() {
		player.setPaused(false)
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

}
