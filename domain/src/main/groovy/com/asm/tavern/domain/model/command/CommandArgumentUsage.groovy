package com.asm.tavern.domain.model.command


/**
 * Possible arg combinations to use with a command
 * Ex:
 * $roll
 * $roll 6
 * $roll 1 6
 */
class CommandArgumentUsage {
	final String name
	final String description
	final List<CommandArgument> args

	CommandArgumentUsage(String name, String description, List<CommandArgument> args) {
		this.name = name
		this.description = description
		this.args = args
	}

	static Builder builder(String name, String description) {
		new Builder(name, description)
	}

	static class Builder {
		final String name
		final String description
		List<CommandArgument> args = []

		Builder(String name, String description) {
			this.name = name
			this.description = description
		}

		Builder add(CommandArgument argument) {
			this.args += argument
			this
		}

		CommandArgumentUsage build() {
			new CommandArgumentUsage(name, description, args)
		}
	}
}
