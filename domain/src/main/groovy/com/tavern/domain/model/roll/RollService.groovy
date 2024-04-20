package com.tavern.domain.model.roll

import java.time.Instant

class RollService {
	private static final Random RANDOM = new Random(Instant.now().nano)

	int rollSingle(int sides) {
		roll(1, sides).first()
	}

	List<Integer> roll(int dice, int sides) throws IllegalArgumentException {
		if (dice <= 0) {
			throw new IllegalArgumentException("Dice must be 1 or greater")
		} else if (sides <= 0) {
			throw new IllegalArgumentException("Sides must be 1 or greater")
		}
		List<Integer> rolls = new ArrayList<>()
		for (int i = 0; i < dice; i++) {
			rolls.add(RANDOM.nextInt(sides) + 1)
		}
		rolls
	}

}
