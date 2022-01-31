package fr.nicolas.bot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import fr.nicolas.bot.listeners.SlashCommandListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		//Creates the gateway client and connects to the gateway
		final GatewayDiscordClient client = DiscordClientBuilder.create("").build()
				.login()
				.block();

		try {
			new GlobalCommandRegistrar(client.getRestClient()).registerCommands();
		} catch (Exception e) {
			LOGGER.error("Error trying to register global slash commands", e);
		}

		//Register our slash command listener
		client.on(ChatInputInteractionEvent.class, SlashCommandListener::handle)
				.then(client.onDisconnect())
				.block(); // We use .block() as there is not another non-daemon thread and the jvm would close otherwise.


	}
}
