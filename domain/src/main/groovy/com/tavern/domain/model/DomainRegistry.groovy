package com.tavern.domain.model

import com.tavern.domain.model.audio.AudioService
import com.tavern.domain.model.audio.SongService
import com.tavern.domain.model.drinks.DrinkService
import com.tavern.domain.model.roll.RollService
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class DomainRegistry implements ApplicationContextAware {
	private static ApplicationContext context

	static RollService rollService() {
		context.getBean(RollService.class)
	}

	static AudioService audioService() {
		context.getBean(AudioService.class)
	}

	static DrinkService drinkService() {
		context.getBean(DrinkService.class)
	}

	static SongService songService() {
		context.getBean(SongService.class)
	}

	@Override
	synchronized void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if (context == null) {
			context = applicationContext
		}
	}

}
