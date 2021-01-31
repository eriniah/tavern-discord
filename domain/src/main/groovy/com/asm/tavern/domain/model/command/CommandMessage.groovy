package com.asm.tavern.domain.model.command


class CommandMessage {
	final List<Command> commandList
	final CommandArgumentUsage usage
	final List<String> args
	final String message

	CommandMessage(List<Command> commandList, CommandArgumentUsage usage, List<String> args, String message) {
		this.commandList = commandList
		this.usage = usage
		this.args = args
		this.message = message
	}

}
