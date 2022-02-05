package fr.nicolas.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Crée par Nicolas S.

public class Dice implements SlashCommand {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Override
  public String getName() {
	return "dice";
  }

  @Override
  public Mono<Void> handle(ChatInputInteractionEvent event) {

	  // Récupération de l'option face, définie par l'utilisateur lors de l'execution de la commende
	int face = event.getOption("face")
			.flatMap(ApplicationCommandInteractionOption::getValue)
					.map(ApplicationCommandInteractionOptionValue::asLong)
					.get().intValue();

	  // Récupération de l'option jet, définie par l'utilisateur lors de l'execution de la commende
	int jet = event.getOption("jet")
			.flatMap(ApplicationCommandInteractionOption::getValue)
			.map(ApplicationCommandInteractionOptionValue::asLong)
			.get().intValue();

	String roll_txt = "";

	// Boucle qui correspont au nombre de jet effectué
	for (int i = 0; i < jet; i++) {
		// Génération du nombre aléatoire avec comme min 1 et max face
		roll_txt += "**Jet n°"+(i+1)+" :** "+ getRandomNumberInRange(1, face) +"\n";
	}

	// Texte destiné à l'utilisateur
	String desc = "**Lancée de " + jet + " dés de "+ face + " faces.**\n\n" +roll_txt;

	// Envoie de la réponse à l'utilisateur
	return event.reply()
			.withEphemeral(false)
			.withEmbeds(
				EmbedCreateSpec.builder()
					.color(Color.TAHITI_GOLD)
					.description(desc)
					.build()
			);

  }

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}