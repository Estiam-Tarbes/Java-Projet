package fr.nicolas.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Calendar implements SlashCommand{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "calendar";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

    //On met dans une variable de type chaine de caractere une valeur systeme pour récuperer la date et l'heure avec des attributs spécifiques
        String HeureDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return event.reply()
                .withEphemeral(false)
                .withEmbeds(
                        EmbedCreateSpec.builder()
                                .color(Color.TAHITI_GOLD)
                                //on concatene du texte pour afficher la données contenue dans la variable
                                .description("Voici la date du jour et l'heure : " + HeureDate)
                                .build()
                );

    }
}
