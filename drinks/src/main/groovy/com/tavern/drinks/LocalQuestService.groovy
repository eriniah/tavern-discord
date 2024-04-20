package com.tavern.drinks

import com.tavern.domain.model.drinks.MemberDrinkResult
import com.tavern.domain.model.drinks.Quest
import com.tavern.domain.model.drinks.QuestService

class LocalQuestService implements QuestService {

	@Override
	Quest getCurrentQuest() {
		return new Quest() {
			@Override
			String getName() {
				"REEE"
			}

			@Override
			boolean isComplete() {
				false
			}
		}
	}

	@Override
	void contribute(MemberDrinkResult contribution) {
		// TODO
	}

	@Override
	void contribute(List<MemberDrinkResult> contributions) {
		// TODO
	}
}
