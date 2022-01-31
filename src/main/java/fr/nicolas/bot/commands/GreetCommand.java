package fr.nicolas.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class GreetCommand implements SlashCommand {
  	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public String getName() {
		return "greet";
	}

	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {

		Mono<User> user = event.getOption("utilisateur")
				.flatMap(ApplicationCommandInteractionOption::getValue)
				.map(ApplicationCommandInteractionOptionValue::asUser)
				.get();




		//Reply to the slash command, with the name the user supplied
		return  event.reply()
				.withEphemeral(true)
				.withContent("Salut , " + user);
	}
}
