package fr.nicolas.bot.listeners;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import fr.nicolas.bot.commands.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SlashCommandListener {

	// Liste contenant les commandes actives
	private final static List<SlashCommand> commands = new ArrayList<>();

	static {
		//Nous enregistrons nos commandes ici lorsque la classe est initialisée
		commands.add(new PingCommand());
		commands.add(new Dice());
		commands.add(new covidStats());
		commands.add(new Calendar());
		commands.add(new Roles());
	}

	public static Mono<Void> handle(ChatInputInteractionEvent event) {
		// Convertir notre liste de tableaux en un flux que l'on peut itérer
		return Flux.fromIterable(commands)
				//Filtre toutes les commandes qui ne correspondent pas au nom de la commande pour laquelle cet événement a été créé.
				.filter(command -> command.getName().equals(event.getCommandName()))
				// Obtention du premier (et seul) élément du flux qui correspond à notre filtre.
				.next()
				//que notre classe de commande gère toute la logique liée à sa commande spécifique.
				.flatMap(command -> {
						try {
								return command.handle(event);
						} catch (SQLException e) {
								e.printStackTrace();
						}
						return null;
				});
	}

}
