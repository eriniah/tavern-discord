package com.tavern.domain.model.repository;

public record Sort(Direction direction, String field) {

    @Override
    public String toString() {
        return "%s%s".formatted(direction.getSign(), field);
    }

    public enum Direction {
        ASC('+'),
        DESC('-');

        private final char sign;
        Direction(char sign) {
            this.sign = sign;
        }

        private char getSign() {
            return sign;
        }
    }
}
