package com.asm.tavern.discord.app

import com.asm.tavern.discord.discord.Discord
import com.asm.tavern.discord.discord.audio.JoinCommandHandler
import com.asm.tavern.discord.discord.audio.LeaveCommandHandler
import com.asm.tavern.discord.discord.audio.NowPlayingCommandHandler
import com.asm.tavern.discord.discord.audio.PauseCommandHandler
import com.asm.tavern.discord.discord.audio.PlayCommandHandler
import com.asm.tavern.discord.discord.audio.SkipCommandHandler
import com.asm.tavern.discord.discord.audio.StopCommandHandler
import com.asm.tavern.discord.discord.audio.UnpauseCommandHandler
import com.asm.tavern.discord.discord.help.HelpHandler
import com.asm.tavern.domain.model.DomainRegistry
import com.asm.tavern.domain.model.TavernCommands
import com.asm.tavern.domain.model.command.CommandHandlerRegistry
import com.asm.tavern.discord.discord.roll.AmountAndSidesRollHandler
import com.asm.tavern.discord.discord.roll.AmountXSidesHandler
import com.asm.tavern.discord.discord.roll.DefaultRollHandler
import com.asm.tavern.domain.model.roll.RollService
import com.asm.tavern.discord.discord.roll.RollTideHandler
import com.asm.tavern.discord.discord.roll.SidesRollHandler
import org.slf4j.ext.XLogger
import org.slf4j.ext.XLoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

class App {
	private static final XLogger logger = XLoggerFactory.getXLogger(App.class)
	// TODO: switch back after testing
	// private static final String PREFIX = '$'
	private static final String PREFIX = '#'

    static void main(String[] args) {
		String token = "NzQ2NDk1NzE1ODc3ODQ3MTIw.X0BKTw.TqlKz-i590w3meP0JtnzFe8H2UM"// TODO: revert to -> args[0]

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml")
		logger.error("Rolled a {}", applicationContext.getBean(RollService.class).roll(1, 6))
		new DomainRegistry().setApplicationContext(applicationContext)

        new Discord(
			token,
			PREFIX,
			TavernCommands.getCommands(),
			new CommandHandlerRegistry()
				.add(new DefaultRollHandler())
				.add(new SidesRollHandler())
				.add(new AmountAndSidesRollHandler())
				.add(new AmountXSidesHandler())
				.add(new RollTideHandler())
				.add(new HelpHandler())
				.add(new PlayCommandHandler())
				.add(new NowPlayingCommandHandler())
				.add(new StopCommandHandler())
				.add(new SkipCommandHandler())
				.add(new PauseCommandHandler())
				.add(new UnpauseCommandHandler())
				.add(new JoinCommandHandler())
				.add(new LeaveCommandHandler())
		).start()
    }
}
