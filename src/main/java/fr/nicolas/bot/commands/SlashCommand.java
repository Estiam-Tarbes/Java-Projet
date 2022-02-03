package fr.nicolas.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

import java.sql.SQLException;

public interface SlashCommand {
	String getName();

	Mono<Void> handle(ChatInputInteractionEvent event) throws SQLException;

}
