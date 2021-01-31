package com.asm.tavern.discord.app

import com.asm.tavern.discord.audio.LavaPlayerAudioService
import com.asm.tavern.domain.model.audio.AudioService
import com.asm.tavern.domain.model.comrade.ComradeService
import com.asm.tavern.domain.model.roll.RollService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TavernContext {

	@Bean
	RollService getRollService() {
		new RollService()
	}

	@Bean
	AudioService getAudioService() {
		new LavaPlayerAudioService()
	}

	@Bean
	ComradeService getComradeService() {
		new ComradeService()
	}

}
