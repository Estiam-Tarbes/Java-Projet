package fr.nicolas.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.nicolas.database.MySQL;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

// Crée par François

public class Races implements SlashCommand{
    private BasicDataSource connectionPool;
    private MySQL mysql;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "races";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) throws SQLException {
        // Création d'une liste qui va contenir les races de notre jeu.
        ArrayList<String> liste = new ArrayList<String>();
        // Création de la connection à la base de donnée
        initConnection();

        // Requête à la base de donnée
        mysql.query("SELECT * FROM races", rs -> {
            try {
                // boucle pour récupérer les résultats
                while (rs.next()){
                    // sauvegarde dans la liste
                    liste.add(rs.getString("nom"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Fermeture de connexion à la base de données
        connectionPool.close();

        // Envoie de la réponse à l'utilisateur
        return event.reply()

                .withEphemeral(false)
                .withEmbeds(

                        EmbedCreateSpec.builder()
                                .color(Color.TAHITI_GOLD)
                                //on concatene du texte pour afficher la données contenue dans la variable
                                .description("Vous pouvez choisir parmi tous ses classes :\n\n- " +liste.toString().replace("[", "").replace("]", "").replaceAll(",", "\n- "))
                                .build()
                );
    }

    private void initConnection() {
        connectionPool = new BasicDataSource();
        connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
        connectionPool.setUsername("estiam");
        connectionPool.setPassword("m8Fd*6MbNRCKxiu4");
        connectionPool.setUrl("jdbc:mysql://5.196.224.14:3306/bot_discord_estiam?autoReconnect=true");
        connectionPool.setInitialSize(1);
        connectionPool.setMaxTotal(10);
        mysql = new MySQL(connectionPool);
    }
}
