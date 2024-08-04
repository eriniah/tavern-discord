package com.tavern.drinks;

import com.tavern.domain.model.discord.UserId;
import com.tavern.domain.model.drinks.DrinkRepository;

import java.util.HashMap;
import java.util.Map;

public class LocalDrinkRepository implements DrinkRepository {
	private final Map<UserId, Integer> playerDrinks = new HashMap<UserId, Integer>();

	@Override
	public int getDrinks(UserId id) {
		ensureExistence(id);
		return playerDrinks.get(id);
	}

	@Override
	public void addDrinks(UserId id, int drinks) {
		ensureExistence(id);
		playerDrinks.put(id, playerDrinks.get(id) + drinks);
	}

	private void ensureExistence(UserId id) {
		if (!playerDrinks.containsKey(id)) {
			playerDrinks.put(id, 0);
		}

	}

}
