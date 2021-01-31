package com.asm.tavern.discord.discord.command.parser

import com.asm.tavern.domain.model.command.Command
import com.asm.tavern.domain.model.command.CommandFactory
import com.asm.tavern.domain.model.command.CommandMessage
import spock.lang.Specification

class CommandParserSpecification
		extends Specification {

	/**
	 * Valid Usages
	 * $command
	 * $command arg1
	 * $command subCommand arg1
	 * $command subCommand subCommand2
	 */
	Command testCommand = CommandFactory.command('command', 'command 1')
		.add(CommandFactory.usage('usage1', 'no arg usage').build())
		.add(CommandFactory.usage('usage2', 'single arg usage')
			.add(CommandFactory.argument('arg1', 'command 1 - arg 1').build())
			.build())
		.add(CommandFactory.command('subCommand', 'sub command 1')
			.add(CommandFactory.usage('usage1', 'single arg usage')
				.add(CommandFactory.argument('arg1', 'root command arg 1').build())
				.build())
			.add(CommandFactory.command('subCommand2', 'sub command 2')
				.add(CommandFactory.usage('usage1', 'no arg usage').build())
				.build())
			.build())
		.build()

	Command testCommand2 = CommandFactory.command('command2', 'command 2').build()

	List<Command> commands = [testCommand, testCommand2]

	def "Parse empty message"() {
		String message = ''
		CommandParser parser = new CommandParser('$', commands)

		when:
		CommandMessage parseResult = parser.parse(message)

		then:
		!parseResult.commandList
		!parseResult.args
		!parseResult.usage
		parseResult.message == message
	}

	def "Parse missing command message"() {
		String message = '$none'
		CommandParser parser = new CommandParser('$', commands)

		when:
		CommandMessage parseResult = parser.parse(message)

		then:
		!parseResult.commandList
		!parseResult.args
		!parseResult.usage
		parseResult.message == message
	}

	def "Parse wrong prefix command message"() {
		String message = '_command'
		CommandParser parser = new CommandParser('$', commands)

		when:
		CommandMessage parseResult = parser.parse(message)

		then:
		!parseResult.commandList
		!parseResult.args
		!parseResult.usage
		parseResult.message == message
	}

	def "Parse root command message"() {
		String message = '$command'
		CommandParser parser = new CommandParser('$', commands)

		when:
		CommandMessage parseResult = parser.parse(message)

		then:
		parseResult.message == message
		parseResult.commandList.size() == 1
		parseResult.commandList.first() == testCommand
		parseResult.usage == testCommand.argumentUsages.first()
		!parseResult.args
	}

	def "Parse sub command message"() {
		String command = '$command'
		String subCommand = 'subCommand'
		String arg1 = 'arg1'
		String message = "${command} ${subCommand} ${arg1}"
		CommandParser parser = new CommandParser('$', commands)

		when:
		CommandMessage parseResult = parser.parse(message)

		then:
		parseResult.message == message
		parseResult.commandList.size() == 2
		parseResult.commandList.first() == testCommand
		parseResult.commandList.last() == testCommand.subCommands.first()
		parseResult.usage == testCommand.subCommands.first().argumentUsages.first()
		parseResult.args.size() == 1
		parseResult.args[0] == arg1
	}

	def "Parse sub command message with invalid usage"() {
		String command = '$command'
		String subCommand = 'subCommand'
		String message = "${command} ${subCommand}"
		CommandParser parser = new CommandParser('$', commands)

		when:
		CommandMessage parseResult = parser.parse(message)

		then:
		!parseResult.commandList
		!parseResult.args
		!parseResult.usage
		parseResult.message == message
	}

}
