package com.tavern.app;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.tavern.app.config.TavernConfig;
import com.tavern.discord.layer.*;
import com.tavern.discord.listeners.dice.RollSlashCommandListener;
import com.tavern.discord.listeners.dice.roll.DiceFactory;
import com.tavern.discord.listeners.drink.*;
import com.tavern.discord.listeners.drink.quest.CampaignConfigListener;
import com.tavern.domain.model.*;
import com.tavern.domain.model.guild.GuildService;
import com.tavern.domain.model.guild.GuildServiceCache;
import com.tavern.domain.model.guild.config.GuildConfigRepository;
import com.tavern.domain.model.repository.GetOptions;
import com.tavern.repository.mem.GuildConfigMemRepository;
import joptsimple.*;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.context.support.GenericApplicationContext;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class App {
	private static final XLogger logger = XLoggerFactory.getXLogger(App.class);

	public static void main(String[] pArgs) {
		OptionParser parser = new OptionParser();
		OptionSpec<String> configSpec = parser.accepts("config", "Path to tavern-discord.properties file")
			.withRequiredArg()
			.defaultsTo("tavern-discord.properties");
		OptionSpec<?> helpSpec = parser.acceptsAll(Arrays.asList("help", "h", "?"), "Prints this help message").forHelp();

		OptionSet args = parser.parse(pArgs);
		if (args.has(helpSpec)) {
			try {
				parser.printHelpOn(System.out);
				System.exit(0);
			} catch (IOException ex) {
				throw new IllegalStateException("Failed to print help menu", ex);
			}
		}

		logger.info("Initializing Tavern v{}", System.getProperty("tavern.version"));
        logger.debug("Running with arguments: {}", Arrays.toString(pArgs));

		TavernConfig config;
		try {
			logger.debug("Running in: {}", new File(".").getAbsoluteFile().getCanonicalFile());
			config = readTavernConfig(configSpec, args);
		} catch (IOException ex) {
			throw new IllegalStateException(String.format("Failed to read configuration file at '%s'", configSpec.value(args)), ex);
		}

        logger.info("Initializing application context");
		DiscordInterfaceImpl discordInterface = new DiscordInterfaceImpl();
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.registerBean(TavernMetadata.class, () -> new TavernMetadata(System.getProperty("tavern.version")));
        applicationContext.registerBean(TavernConfig.class, () -> config);
        applicationContext.registerBean(DiceFactory.class, DiceFactory::new);
		GuildConfigRepository guildConfigRepository = new GuildConfigMemRepository();
        applicationContext.registerBean(GuildConfigRepository.class, () -> guildConfigRepository);
		// TODO: EMM Set guild audio cache factory
		GuildService guildService = new GuildService(discordInterface, guildConfigRepository, new GuildServiceCache<>((_0, _1) -> null));
		applicationContext.registerBean(GuildConfigRepository.class, () -> guildConfigRepository);

        DrinkPointCache cache = new DrinkPointCache();
        applicationContext.registerBean(DrinkPointCache.class, () -> cache);

        logger.debug("Refreshing and starting application context");
        applicationContext.refresh();
        applicationContext.start();

		logger.info("Initializing Discord API");
		TavernDiscordClient discord = TavernDiscordClient.builder(discordInterface, config.getDiscord().getToken())
			.commandPrefix(config.getDiscord().getCommandPrefix())
			.injectables(new Injectables() {
				@Override
				public <T> T get(Class<T> type) {
					return applicationContext.getBean(type);
				}
			})
			.listeners(Arrays.asList(
				RollSlashCommandListener.class,
				DrinkCommandListener.class,
				PopPopCommandListener.class,
				DrinkPointCommandListener.class,
				CampaignConfigListener.class
			))
			.build();
		discord.initializeGuilds(
			guildConfigRepository,
			guildService.getInitializedGuilds()
		);

		try {
			if (!discord.awaitReady()) {
				logger.error("Failed to connect to discord. Exiting...");
				System.exit(1);
			}
		} catch (InterruptedException ex) {
			throw new IllegalStateException("Interrupted while connecting to Discord", ex);
		}

		logger.info("Tavern Initialized");
    }

	private static TavernConfig readTavernConfig(OptionSpec<String> configSpec, OptionSet args) throws IOException {
		logger.debug("Reading configuration file at {}", configSpec.value(args));

		JavaPropsMapper mapper = new JavaPropsMapper();
		try (Reader tavernPropertiesReader = new FileReader(configSpec.value(args), Constants.CHARSET)) {
			TavernConfig config = mapper.readValue(tavernPropertiesReader, TavernConfig.class);
			logger.info("Successfully loaded configuration: {}", config);
			return config;
		}
	}

}
