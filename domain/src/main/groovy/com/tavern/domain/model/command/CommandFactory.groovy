package com.tavern.domain.model.command

class CommandFactory {

	static Command.Builder command(String name, String description) {
		Command.builder(name, description)
	}

	static CommandArgument.Builder argument(String name, String description) {
		CommandArgument.builder(name, description)
	}

	static CommandArgumentUsage.Builder usage(String name, String description) {
		CommandArgumentUsage.builder(name, description)
	}
}
