package com.asm.tavern.discord.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.entities.TextChannel

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class TrackScheduler extends AudioEventAdapter {
	private final AudioPlayer player
	private BlockingQueue<AudioTrack> queue
	private TextChannel textChannel

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
		textChannel.sendMessage("Now Playing: ${track.getInfo().title}").queue()


	}

}
