package com.asm.tavern.domain.model.command


class CommandResultBuilder {
	private boolean success

	CommandResultBuilder success() {
		success = true
		this
	}

	CommandResultBuilder error() {
		success = false
		this
	}

	CommandResult build() {
		return new CommandResult() {
			@Override
			boolean success() {
				return success
			}
		}
	}

}
