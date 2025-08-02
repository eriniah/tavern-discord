package com.tavern.app;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.tavern.app.config.TavernConfig;
import com.tavern.domain.model.Constants;
import joptsimple.*;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.*;
import java.util.Arrays;

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
			} catch (IOException ex) {
				throw new IllegalStateException("Failed to print help menu", ex);
			}
			System.exit(0);
		}

		logger.info("Initializing Tavern v{}", System.getProperty("tavern.version"));

		TavernConfig config;
		try {
			logger.debug("Running in: {}", new File(".").getAbsoluteFile().getCanonicalFile());
			config = readTavernConfig(configSpec, args);
		} catch (IOException ex) {
			throw new IllegalStateException("Failed to read configuration file at '" + configSpec.value(args) + "'", ex);
		}

		logger.info("Initializing Discord API");
		// TOOD: EMM Discord

		logger.info("Initializing application context");
		// TODO: EMM Spring/AppContext/DomainRegistry

//		GenericApplicationContext applicationContext = new GenericApplicationContext();
//		applicationContext.registerBean(RollService.class, RollService::new);

		logger.info("Starting application context");
		// TODO: EMM Start it all up
//		applicationContext.refresh();
//		applicationContext.start();
//		new DomainRegistry().setApplicationContext(applicationContext);
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
