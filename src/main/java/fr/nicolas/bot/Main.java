package fr.nicolas.bot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import fr.nicolas.bot.listeners.SlashCommandListener;
import fr.nicolas.database.MySQL;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

// Crée par Nicolas S.

public class Main {
		private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
		private static BasicDataSource connectionPool;
		private static MySQL mysql;

	public static void main(String[] args) throws SQLException {

				initConnection();


				//Création du client et se connecte à la passerelle.
				final GatewayDiscordClient client = DiscordClientBuilder.create("").build()
						.login()
						.block();

				try {
					new GlobalCommandRegistrar(client.getRestClient()).registerCommands();
				} catch (Exception e) {
					LOGGER.error("Erreur lors de l'enregistrement des commandes !", e);
				}

				//Récupération de l'action lors de l'execution d'une commande slash
				client.on(ChatInputInteractionEvent.class, SlashCommandListener::handle)
						.then(client.onDisconnect())
						.block(); // Nous utilisons .block() car il n'y a pas d'autre thread non-daemon et le jvm se fermerait sinon.

		}

		// Création de la connection à la base de données ainsi que la création des tables
		private static void initConnection() throws SQLException {
				connectionPool = new BasicDataSource();
				connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
				connectionPool.setUsername("<utilisateur>");
				connectionPool.setPassword("<mot de passe>");
				connectionPool.setUrl("jdbc:mysql://<adresse>/<base de donnée>?autoReconnect=true");
				connectionPool.setInitialSize(1);
				connectionPool.setMaxTotal(10);
				mysql = new MySQL(connectionPool);
				mysql.createTables();

				connectionPool.close();
		}
}
