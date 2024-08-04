package com.tavern.discord.roll;

import com.tavern.domain.model.TavernCommands;
import com.tavern.domain.model.command.*;
import com.tavern.domain.model.roll.RollService;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import javax.annotation.Nonnull;

public class SidesRollHandler implements CommandHandler {
	private final RollService rollService;

	public SidesRollHandler(RollService rollService) {
		this.rollService = rollService;
	}

	@Override
	public Command getCommand() {
		return TavernCommands.getROLL();
	}

	@Override
	public boolean supportsUsage(CommandArgumentUsage usage) {
		return TavernCommands.RollUsages.getSIDES().equals(usage);
	}

	@Override
	public CommandResult handle(@Nonnull MessageReceivedEvent event, CommandMessage message) {
		try {
			int sides = Integer.parseInt(DefaultGroovyMethods.first(message.getArgs()));
			int roll = rollService.rollSingle(sides);
			event.getChannel().sendMessage("You rolled a " + roll).queue();
			return new CommandResultBuilder().success().build();
		} catch (NumberFormatException ex) {
			event.getChannel().sendMessage("Arguments must be numbers");
			return new CommandResultBuilder().error().build();
		} catch (IllegalArgumentException ex) {
			event.getChannel().sendMessage(ex.getMessage());
			return new CommandResultBuilder().error().build();
		}
	}
}
