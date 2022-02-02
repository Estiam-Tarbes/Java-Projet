package fr.nicolas.bot.commands;


import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.nicolas.bot.utils.DateValidatorDateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static fr.nicolas.bot.utils.JsonReader.readJsonFromUrl;

public class covidStats implements SlashCommand {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public String getName() {
		return "covid-stats";
	}

	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) throws IOException, JSONException {

		return event.reply()
						.withEphemeral(false)
						.withEmbeds(
										EmbedCreateSpec.builder()
														.color(Color.TAHITI_GOLD)
														.description("Clique pour accèder au dernière données du Covid")
														.build()
						).withComponents(ActionRow.of(Button.link("https://covidtracker.fr/france/", "\uD83E\uDDA0 Clique ici")));

	}

}
