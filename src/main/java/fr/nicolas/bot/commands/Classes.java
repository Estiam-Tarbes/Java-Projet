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
import java.util.ArrayList;

public class Classes implements SlashCommand{
    private BasicDataSource connectionPool;
    private MySQL mysql;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return "roles";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) throws SQLException {

        initConnection();
        ArrayList<String> liste = new ArrayList<String>();

        mysql.query("SELECT * FROM classes", rs -> {
            try {
                while (rs.next()){
                    liste.add(rs.getString("nom"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        connectionPool.close();

        return event.reply()

                .withEphemeral(false)
                .withEmbeds(

                        EmbedCreateSpec.builder()
                                .color(Color.TAHITI_GOLD)
                                //on concatene du texte pour afficher la données contenue dans la variable
                                .description(liste.toString().replace("[", "").replace("]", "").replaceAll(",", "\n"))
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
