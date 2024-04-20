package com.tavern.drinks

import com.tavern.domain.model.drinks.ComradeService

class LocalComradeService implements ComradeService {
	private boolean comradeModeEnabled

	@Override
	void enable() {
		comradeModeEnabled = true
	}

	@Override
	void disable() {
		comradeModeEnabled = false
	}

	@Override
	boolean isEnabled() {
		comradeModeEnabled
	}

}
