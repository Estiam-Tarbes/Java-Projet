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
		//Création du client et se connecte à la passerelle.
		final GatewayDiscordClient client = DiscordClientBuilder.create("").build()
				.login()
				.block();

		try {
			new GlobalCommandRegistrar(client.getRestClient()).registerCommands();
		} catch (Exception e) {
			LOGGER.error("Erreur lors de l'enregistrement des commandes !", e);
		}

		//Récupération de l'action lors de l'execution d'une commande slash
		client.on(ChatInputInteractionEvent.class, SlashCommandListener::handle)
				.then(client.onDisconnect())
				.block(); // Nous utilisons .block() car il n'y a pas d'autre thread non-daemon et le jvm se fermerait sinon.


	}
}
