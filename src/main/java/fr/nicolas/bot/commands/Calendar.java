package fr.nicolas.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.nicolas.bot.Main;
import fr.nicolas.database.MySQL;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Calendar implements SlashCommand{
    private BasicDataSource connectionPool;
    private MySQL mysql;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "calendar";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) throws SQLException {

        initConnection();

        mysql.query("SELECT * FROM users WHERE uuid = '"+ event.getInteraction().getUser().getId().asString() +"'", rs -> {
            try {
                if (rs.next()) {
                    System.out.println(rs.getInt("niveau"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        connectionPool.close();

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
