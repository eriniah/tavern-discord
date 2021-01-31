package com.asm.tavern.domain.model.comrade

import com.asm.tavern.domain.model.DomainRegistry
import com.asm.tavern.domain.model.discord.GuildId
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.managers.AudioManager

import java.time.OffsetDateTime

class ComradeService {
	private Map<GuildId, Boolean> commradeModeState = new HashMap<>()

	void enableComradeMode(TextChannel textChannel, Member tavernMember, GuildVoiceState posterVoiceState, AudioManager audioManager) {
		commradeModeState.put(new GuildId(textChannel.getGuild().getId()), true)
		MessageEmbed embeddedImage = new MessageEmbed(
				"https://media3.giphy.com/media/befaYZCgtZfZm/giphy.gif",
				"Comrades",
				"Comrades",
				EmbedType.IMAGE,
				OffsetDateTime.now(),
				0xFF0000,
				null,
				new MessageEmbed.Provider("Giphy", "https://giphy.com/"),
				null,
				null,
				null,
				new MessageEmbed.ImageInfo("https://media3.giphy.com/media/befaYZCgtZfZm/giphy.gif", "https://media3.giphy.com/media/befaYZCgtZfZm/giphy.gif", 400, 300),
				null)
		textChannel.sendMessage(embeddedImage).queue()
		tavernMember.modifyNickname("Comrade Bot").submit()
		DomainRegistry.audioService().join(posterVoiceState, audioManager)
		DomainRegistry.audioService().play(textChannel, new URL("https://www.youtube.com/watch?v=bCBJ-MaUTC4&ab_channel=ChornyyBaron"))
	}

	void disableComradeMode(GuildId id) {
		commradeModeState.put(id, false)
	}

}
