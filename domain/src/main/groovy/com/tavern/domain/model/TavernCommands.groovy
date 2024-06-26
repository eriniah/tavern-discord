package com.tavern.domain.model


import com.tavern.domain.model.command.Command
import com.tavern.domain.model.command.CommandArgumentUsage

import java.lang.reflect.Field
import java.util.stream.Collectors

import static com.tavern.domain.model.command.CommandFactory.*

class TavernCommands {

	static class Roles {
		static final String DJ = "DJ"
	}

	static class Tags {
		static final String GENERAL = 'General'
		static final String MUSIC = 'Music'
		static final String DRINKING = 'Drinking'
	}

	static class HelpUsages {
		static final CommandArgumentUsage DEFAULT = usage('default', 'Display the help menu').build()
		static final CommandArgumentUsage COMMAND_HELP = usage('command', 'Get help with a command or sub command')
				.varArgs()
				.add(argument('command', 'The command to get help with').example('songs'))
				.add(argument('subCommand', 'One or more sub commands').example('add'))
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
				.add(argument('sides', 'Amount of sides on the die').example('6'))
				.build()
		static final CommandArgumentUsage AMOUNT_AND_SIDES = usage('amount and sides', 'Roll a number of dice with a given amount of sides')
				.add(argument('dice', 'Amount of dice').example('1'))
				.add(argument('sides', 'Amount of sides on the die').example('6'))
				.build()
		static final CommandArgumentUsage AMOUNT_X_SIDES = usage('amount x sides', 'Roll a number or dice with a given amount of sides')
				.add(argument('amount x sides', 'amount of dice and sides per die').example('1x6'))
				.build()

		static final CommandArgumentUsage ROLL_TIDE = usage('?', '?')
				.add(argument('?', '?'))
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

	static final Command PLAY = command('play', 'Play a song')
			.tag(Tags.MUSIC)
			.add(usage('song', 'Play a song')
			.varArgs()
				.add(argument('song', 'the youtube url or song id').example("https://www.youtube.com/watch?v=dQw4w9WgXcQ")))
			.build()

	static final Command WEAVE = command('weave', 'Sets tavern to weave mode for a given song')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Turns off Weave Mode'))
			.add(usage('song', 'Song to be weaved into the queue')
					.varArgs()
					.add(argument('song', 'the youtube url or song id').example("https://www.youtube.com/watch?v=dQw4w9WgXcQ")))
			.build()

	static final Command PLAY_MODE = command('pm', 'Sets tavern to play mode for given category')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Turns off modes'))
			.add(usage('category', 'Category to be shuffled at the end of the queue')
					.add(argument('category', 'the category string').example('elevator')))
			.build()

	static final Command NOW_PLAYING = command('np', 'Get the current playing track')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Get the current playing track'))
			.build()

	static final Command QUEUE = command('queue', 'Get the queue of music')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Get the queue of music'))
			.build()

	static final Command STOP = command('stop', 'Stop and clear the music queue')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Stop and clear the music queue'))
			.build()

	static final Command CLEAR = command('clear', 'Clear the music queue')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Clear the music queue'))
			.build()

	static final Command SKIP = command('skip', 'Skip the current track')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Skip the current track'))
			.add(usage('skip amount', 'Skip an amount of songs')
					.add(argument('amount', 'The amount of songs to skip').example('5')))
			.build()

	static final Command SKIP_TIME = command('st', 'Skip seconds from playing track')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Skip 60 seconds'))
			.add(usage('skip amount', 'Skip X seconds').add(argument('amount', 'The amount of seconds to skip').example('60')))
			.build()

	static final Command SHUFFLE = command('shuffle', 'Shuffle the current song queue')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Shuffle the current song queue'))
			.build()

	static final Command PLAY_NEXT = command('pn', 'Play this song or playlist after the currently playing song')
			.tag(Tags.MUSIC)
			.add(usage('song', 'Play this song or playlist after the currently playing song')
			.varArgs()
					.add(argument('song', 'the youtube url or song id').example("https://www.youtube.com/watch?v=dQw4w9WgXcQ")))
			.build()

	static final Command PAUSE = command('pause', 'Pause the music')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Pause the music'))
			.build()

	static final Command UNPAUSE = command('unpause', 'Resume the music')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Resume the music'))
			.build()

	static final Command JOIN = command('join', 'Join the voice chat lobby')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Join the voice chat lobby'))
			.build()

	static final Command LEAVE = command('leave', 'Leave the voice chat lobby')
			.tag(Tags.MUSIC)
			.add(usage('default', 'Leave the voice chat lobby'))
			.build()

	static class SongsUsages {
		static final CommandArgumentUsage DEFAULT = usage('default', 'List songs').build()
		static final CommandArgumentUsage ADD_WITH_CATEGORY = usage('add ', 'Register a new song with category')
				.add(argument('add', 'required').example('add'))
				.add(argument('id', 'The id to register the song as').example('chuchu'))
				.add(argument('url', 'The url of the song to register').example('https://www.youtube.com/watch?v=5d32-RnUlAA'))
				.add(argument('category', 'The category of the song to register').example('powerhour'))
				.requireRole(Roles.DJ)
				.build()

		static final CommandArgumentUsage ADD = usage('add', 'Register a new song')
				.add(argument('add', 'required').example('add'))
				.add(argument('id', 'The id to register the song as').example('chuchu'))
				.add(argument('url', 'The url of the song to register').example('https://www.youtube.com/watch?v=5d32-RnUlAA'))
				.requireRole(Roles.DJ)
				.build()

		static final CommandArgumentUsage REMOVE = usage('remove', 'Remove a registered song')
				.add(argument('remove', 'required').example('remove'))
				.add(argument('id', 'The id of the song to remove').example('chchu'))
				.requireRole(Roles.DJ)
				.build()
	}

	static final Command SONGS = command('songs', 'Commands for registered songs')
			.tag(Tags.MUSIC)
			.add(SongsUsages.DEFAULT)
			.add(SongsUsages.ADD)
			.add(SongsUsages.REMOVE)
			.add(SongsUsages.ADD_WITH_CATEGORY)
			.usageRouter({ usages, args ->
				switch(args.size()){
					case 0:
						return SongsUsages.DEFAULT
					case 2:
						if(args.first().toLowerCase().contains('remove'))
							return SongsUsages.REMOVE
						else
							return SongsUsages.DEFAULT
					case 3:
						if(args.first().toLowerCase().contains('add'))
							return SongsUsages.ADD
						else
							return SongsUsages.DEFAULT
					case 4:
						if(args.first().toLowerCase().contains('add'))
							return SongsUsages.ADD_WITH_CATEGORY
						else
							return SongsUsages.DEFAULT
					default:
						return SongsUsages.DEFAULT
				}
			})
			.build()

	static final Command COMRADE = command('comrade', 'COMRADES!')
			.tag(Tags.GENERAL)
			.add(usage('default', 'Comrade mode'))
			.build()

	static final Command UNCOMRADE = command('uncomrade', '...')
			.tag(Tags.GENERAL)
			.add(usage('default', 'Turn off comrade mode'))
			.build()

	static final Command DRINK = command('drink', 'Take some drinks')
			.tag(Tags.DRINKING)
			.add(usage('drink', 'Take 1-5 drinks'))
			.build()

	static final Command POPPOP = command('poppop', 'Pop pop!')
			.tag(Tags.DRINKING)
			.add(usage('poppop', 'Everyone takes 0-4 drinks'))
			.build()

	static final Command DRINKS = command('drinks', 'Drink totals')
			.tag(Tags.DRINKING)
			.add(usage('drinks', 'List total drinks for users in the voice channel'))
			.build()

	static List<Command> getCommands() {
		Field[] fields = TavernCommands.class.getDeclaredFields()
		return Arrays.stream(fields)
				.filter(field -> Command.class == field.getType() && field.trySetAccessible())
				.map(field -> field.get(null))
				.collect(Collectors.toList())
	}

}
