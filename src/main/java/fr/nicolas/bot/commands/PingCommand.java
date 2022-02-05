package fr.nicolas.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

import java.time.Instant;

// Crée par Fabien

public class PingCommand implements SlashCommand {

	@Override
	public String getName() {
		return "ping";
	}

	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {

		// Envoie de la réponse à l'utilisateur
		return event.reply()
				.withEphemeral(false)
				.withEmbeds(
						EmbedCreateSpec.builder()
								.color(Color.BLUE)
								.description("Pong")
								.timestamp(Instant.now())
								.build()
				);
	}
}
