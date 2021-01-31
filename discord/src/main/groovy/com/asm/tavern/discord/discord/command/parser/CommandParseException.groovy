package com.asm.tavern.discord.discord.command.parser

class CommandParseException extends Exception {
	CommandParseException(String message) {
		super(message)
	}

	CommandParseException(Throwable t, String message) {
		super(message, t)
	}
}
