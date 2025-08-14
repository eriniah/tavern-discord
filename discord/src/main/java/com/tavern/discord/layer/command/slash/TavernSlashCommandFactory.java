package com.tavern.discord.layer.command.slash;

import com.tavern.discord.layer.command.slash.annotations.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class TavernSlashCommandFactory {
    private final Map<Class<?>, OptionType> mapToOptionType;

    public TavernSlashCommandFactory() {
        this.mapToOptionType = Map.ofEntries(
            Map.entry(String.class, OptionType.STRING),
            Map.entry(Integer.TYPE, OptionType.INTEGER),
            Map.entry(Integer.class, OptionType.INTEGER),
            Map.entry(Long.TYPE, OptionType.INTEGER),
            Map.entry(Long.class, OptionType.INTEGER),
            Map.entry(Boolean.TYPE, OptionType.BOOLEAN),
            Map.entry(Boolean.class, OptionType.BOOLEAN),
            Map.entry(Double.TYPE, OptionType.NUMBER),
            Map.entry(Double.class, OptionType.NUMBER),
            Map.entry(Float.TYPE, OptionType.NUMBER),
            Map.entry(Float.class, OptionType.NUMBER),
            Map.entry(Short.TYPE, OptionType.INTEGER),
            Map.entry(Short.class, OptionType.INTEGER),
            Map.entry(Message.Attachment.class, OptionType.ATTACHMENT),
            Map.entry(Channel.class, OptionType.CHANNEL),
            Map.entry(IMentionable.class, OptionType.MENTIONABLE),
            Map.entry(Role.class, OptionType.ROLE),
            Map.entry(User.class, OptionType.USER)
        );
    }

    public TavernSlashCommand simpleNoOptions(String name, String description) {
        return new SimpleTavernSlashCommand(Commands.slash(name, description));
    }

    public TavernSlashCommand simple(Class<?> commandClass) {
        return simple(commandClass, __ -> {});
    }

    public TavernSlashCommand simple(Class<?> commandClass, Consumer<SlashCommandData> configure) {
        try {
            SlashCommandData commandData = getCommand(commandClass);
            commandData.addOptions(getCommandOptions(commandClass));
            configure.accept(commandData);
            return new SimpleTavernSlashCommand(commandData, commandClass);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException(String.format("Failed to construct command '%s'", commandClass.getName()), ex);
        }
    }

    private SlashCommandData getCommand(Class<?> commandClass) {
        SlashCommand command = commandClass.getAnnotation(SlashCommand.class);
        if (command == null) {
            throw new IllegalArgumentException("Command class must be annotated with @Command");
        }

        return Commands.slash(command.name(), command.description());
    }

    private SubcommandData getSubCommand(Class<?> commandClass) {
        SlashCommand command = commandClass.getAnnotation(SlashCommand.class);
        if (command == null) {
            throw new IllegalArgumentException("Command class must be annotated with @Command");
        }

        return new SubcommandData(command.name(), command.description());
    }

    private List<OptionData> getCommandOptions(Class<?> commandClass) {
        Optional<Constructor<?>> constructor = Arrays.stream(commandClass.getConstructors())
            .filter(method -> null != method.getAnnotation(SlashCommandCreator.class))
            .findFirst();
        if (constructor.isEmpty()) {
            throw new IllegalArgumentException("Command class must have a constructor annotated with @CommandCreator");
        }

        List<OptionData> commandOptions = new LinkedList<>();
        for (Parameter parameter: constructor.get().getParameters()) {
            SlashCommandOption option = parameter.getAnnotation(SlashCommandOption.class);
            if (option == null) {
                throw new IllegalArgumentException("All Command constructor parameters must be annotated with @CommandOption");
            }

            // Get option-type, if java-type doesn't map to an option type, throw
            OptionType optionType = Optional.ofNullable(mapToOptionType.get(parameter.getType()))
                .orElseThrow(() -> new IllegalArgumentException(
                    String.format(
                        "Unsupported parameter type: '%s'. Must be one of '%s'",
                        parameter.getType(),
                        mapToOptionType.keySet().stream()
                            .map(Class::getName)
                            .collect(Collectors.joining(", ")))
                    )
                );

            commandOptions.add(new OptionData(
                optionType,
                option.name(),
                option.description(),
                option.required(),
                option.autoComplete()
            ));
        }
        return commandOptions;
    }

    public TavernSlashCommandBuilder builder(String name, String description) {
        return new TavernSlashCommandBuilder(name, description);
    }

    public final class TavernSlashCommandBuilder {
        private final SlashCommandData command;
        private final Map<String, StructuredTavernSlashCommand.TavernSubCommand> subCommands = new HashMap<>();
        private final Map<String, StructuredTavernSlashCommand.TavernCommandSubgroup> subgroupCommands = new HashMap<>();

        TavernSlashCommandBuilder(String name, String description) {
            this.command = Commands.slash(name, description);
        }

        public TavernSlashCommandBuilder addSubCommand(Class<?> commandClass) {
            return addSubCommand(commandClass, __ -> {});
        }

        public TavernSlashCommandBuilder addSubCommand(Class<?> commandClass, Consumer<SubcommandData> configure) {
            SubcommandData command = getSubCommand(commandClass);
            command.addOptions(getCommandOptions(commandClass));
            configure.accept(command);
            subCommands.put(command.getName(), new StructuredTavernSlashCommand.TavernSubCommand(command, commandClass));
            return this;
        }

        public TavernSlashCommandBuilder addSubCommandGroup(String name, String description, Consumer<SubcommandGroupBuilder> configure) {
            SubcommandGroupBuilder subcommandGroupBuilder = new SubcommandGroupBuilder(name, description);
            configure.accept(subcommandGroupBuilder);
            subgroupCommands.put(name, subcommandGroupBuilder.build());
            return this;
        }

        public TavernSlashCommand build() {
            return build(__ -> {});
        }

        public TavernSlashCommand build(Consumer<SlashCommandData> configure) {
            configure.accept(command);
            if (!subCommands.isEmpty()) {
                command.addSubcommands(
                    subCommands.values().stream()
                        .map(StructuredTavernSlashCommand.TavernSubCommand::getSubCommandData)
                        .collect(Collectors.toList())
                );
            }
            if (!subgroupCommands.isEmpty()) {
                command.addSubcommandGroups(
                    subgroupCommands.values().stream()
                        .map(StructuredTavernSlashCommand.TavernCommandSubgroup::getSubCommandGroupData)
                        .collect(Collectors.toList())
                );
            }
            return new StructuredTavernSlashCommand(command, subCommands, subgroupCommands);
        }
    }

    public final class SubcommandGroupBuilder {
        private final String name;
        private final String description;
        private final Map<String, StructuredTavernSlashCommand.TavernSubCommand> subCommands = new HashMap<>();

        public SubcommandGroupBuilder(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public SubcommandGroupBuilder addSubCommand(Class<?> commandClass) {
            return addSubCommand(commandClass, __ -> {});
        }

        public SubcommandGroupBuilder addSubCommand(Class<?> commandClass, Consumer<SubcommandData> configure) {
            SubcommandData command = getSubCommand(commandClass);
            command.addOptions(getCommandOptions(commandClass));
            configure.accept(command);
            subCommands.put(command.getName(), new StructuredTavernSlashCommand.TavernSubCommand(command, commandClass));
            return this;
        }

        StructuredTavernSlashCommand.TavernCommandSubgroup build() {
            return build(__ -> {});
        }

        StructuredTavernSlashCommand.TavernCommandSubgroup build(Consumer<SubcommandGroupData> configure) {
            SubcommandGroupData subcommandGroupData = new SubcommandGroupData(name, description);
            configure.accept(subcommandGroupData);
            subcommandGroupData.addSubcommands(
                subCommands.values().stream()
                    .map(StructuredTavernSlashCommand.TavernSubCommand::getSubCommandData)
                    .collect(Collectors.toList())
            );
            return new StructuredTavernSlashCommand.TavernCommandSubgroup(subcommandGroupData, subCommands);
        }

    }

}
