package com.asm.tavern.domain.model

import com.asm.tavern.discord.utilities.DiscordUtils
import com.asm.tavern.domain.model.command.Command
import com.asm.tavern.domain.model.command.CommandArgumentUsage

import java.lang.reflect.Field
import java.util.stream.Collectors

import static com.asm.tavern.domain.model.command.CommandFactory.*

class TavernCommands {

	static class Tags {
		static final GENERAL = 'General'
		static final MUSIC = 'Music'
	}

	static class HelpUsages {
		static final CommandArgumentUsage DEFAULT = usage('default', 'Display the help menu').build()
		static final CommandArgumentUsage COMMAND_HELP = usage('command', 'Get help with a command')
				.add(argument('command', 'The command to get help with').example('roll').build())
				.build()

	}

	static final Command HELP = command('help', 'Help with tavern commands')
			.tag(Tags.GENERAL)
			.add(HelpUsages.DEFAULT)
			.add(HelpUsages.COMMAND_HELP)
			.build()

	static class RollUsages {

		static final CommandArgumentUsage DEFAULT = usage('default', 'Roll a 6 sided die').build()
		static final CommandArgumentUsage SIDES = usage('sides', 'Roll a die with a given amount of sides')
				.add(argument('sides', 'Amount of sides on the die').example('6').build())
				.build()
		static final CommandArgumentUsage AMOUNT_AND_SIDES = usage('amount and sides', 'Roll a number of dice with a given amount of sides')
				.add(argument('dice', 'Amount of dice').example('1').build())
				.add(argument('sides', 'Amount of sides on the die').example('6').build())
				.build()
		static final CommandArgumentUsage AMOUNT_X_SIDES = usage('amount x sides', 'Roll a number or dice with a given amount of sides')
				.add(argument('amount x sides', 'amount of dice and sides per die').example('1x6').build())
				.build()

		static final CommandArgumentUsage ROLL_TIDE = usage('?', '?')
				.add(argument('?', '?').build())
				.build()

	}

	static final Command ROLL = command('roll',  'Roll dice')
			.tag(Tags.GENERAL)
			.add(RollUsages.DEFAULT)
			.add(RollUsages.SIDES)
			.add(RollUsages.AMOUNT_AND_SIDES)
			.add(RollUsages.AMOUNT_X_SIDES)
			.usageRouter({ usages, args ->
				switch (args.size()) {
					case 0:
						return RollUsages.DEFAULT
					case 1:
						if (args.first().toLowerCase().contains('x')) {
							return RollUsages.AMOUNT_X_SIDES
						} else if (args.first().toLowerCase() == 'tide') {
							return RollUsages.ROLL_TIDE
						} else {
							return RollUsages.SIDES
						}
					case 2:
						return RollUsages.AMOUNT_AND_SIDES
				}
			})
			.build()

	static final Command COMRADE = command('comrade', 'COMRADES!')
			.tag(Tags.GENERAL)
			.add(usage('default', 'Comrade mode').build())
			.build()

	static final Command UNCOMRADE = command('uncomrade', '...')
			.tag(Tags.GENERAL)
			.add(usage('default', 'Turn off comrade mode').build())
			.build()

	static final Command PLAY = command('play', 'Play a song')
			.tag(Tags.MUSIC)
			.add(usage('url', 'Play a song given the url')
					.add(argument('url', 'the youtube url').example(DiscordUtils.escapeUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ")).build())
					.build())
			.build()

	static final Command NOW_PLAYING = command('np', 'Get the current playing track')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Default').build())
			.build()

	static final Command QUEUE = command('queue', 'Get the queue of music')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Default').build())
			.build()

	static final Command STOP = command('stop', 'Stop and clear the music queue')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Default').build())
			.build()

	static final Command SKIP = command('skip', 'Skip the current track')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Default').build())
			.build()

	static final Command PAUSE = command('pause', 'Pause the music')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Default').build())
			.build()

	static final Command UNPAUSE = command('unpause', 'Unpause the music')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Default').build())
			.build()

	static final Command JOIN = command('join', 'Join the voice chat lobby')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Default').build())
			.build()

	static final Command LEAVE = command('leave', 'Leave the voice chat lobby')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Default').build())
			.build()

	static List<Command> getCommands() {
		Field[] fields = TavernCommands.class.getDeclaredFields()
		return Arrays.stream(fields)
				.filter(field -> Command.class == field.getType() && field.trySetAccessible())
				.map(field -> field.get(null))
				.collect(Collectors.toList())
	}

}
