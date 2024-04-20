package com.tavern.domain.model.drinks

import com.tavern.domain.model.discord.UserId

interface DrinkRepository {

	int getDrinks(UserId id)

	void addDrinks(UserId id, int drinks)

}