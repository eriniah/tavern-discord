package com.tavern.discord.listeners.drink;

import java.util.HashMap;
import java.util.Map;

public class DrinkPointCache {
    private final Map<String, Integer> points = new HashMap<>();

    public void add(String userId, int count) {
        this.points.put(userId, this.points.getOrDefault(userId, 0) + count);
    }

    public int get(String userId) {
        return this.points.getOrDefault(userId, 0);
    }

    public Map<String, Integer> getPoints() {
        return new HashMap<>(points);
    }

}
