package com.tavern.discord;

public final class DiscordColoredTextFactory {
    private DiscordColoredTextFactory() {}

    public static final class Ansi {
        private Ansi() {}

        public static String ansiWrap(String text) {
            return "```ansi\n%s\n```".formatted(text);
        }

        public static String darkGrey(String text) {
            return "[2;30m%s[0m".formatted(text);
        }

        public static String red(String text) {
            return "[2;31m%s[0m".formatted(text);
        }

        public static String yellowGreen(String text) {
            return "[2;32m%s[0m".formatted(text);
        }

        public static String gold(String text) {
            return "[2;33m%s[0m".formatted(text);
        }

        public static String lightBlue(String text) {
            return "[2;34m%s[0m".formatted(text);
        }

        public static String pink(String text) {
            return "[2;35m%s[0m".formatted(text);
        }

        public static String teal(String text) {
            return "[2;36m%s[0m".formatted(text);
        }

        public static String white(String text) {
            return "[2;37m%s[0m".formatted(text);
        }

    }


}
