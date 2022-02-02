package fr.nicolas.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.json.JSONException;
import reactor.core.publisher.Mono;

import java.io.IOException;

public interface SlashCommand {
	String getName();

	Mono<Void> handle(ChatInputInteractionEvent event) throws IOException, JSONException;

}
