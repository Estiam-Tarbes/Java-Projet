package fr.nicolas.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Roles implements SlashCommand{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "role";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        // Role disponible via la commande
        String[] Role = {"Mage", "Sorcier", "Princesse", "Brigant", "Chevalier", "Ménestrel", "Hobbit", "Nain", "Roi", "Gotaga"};



        int Number = getRandomNumberInRange(1, Role.length);
        System.out.println(Role[Number]);
        return event.reply()
                .withEphemeral(false)
                .withEmbeds(
                        EmbedCreateSpec.builder()
                                .color(Color.TAHITI_GOLD)
                                //on concatene du texte pour afficher la données contenue dans la variable
                                .description("Voici la date du jour et l'heure : ")
                                .build()
                );

    }

    private static int getRandomNumberInRange(int min, int max) {

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
