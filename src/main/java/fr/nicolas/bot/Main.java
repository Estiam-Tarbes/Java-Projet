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

public class Main {
		private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
		public static Main instance;
		private static BasicDataSource connectionPool;
		private static MySQL mysql;

		private static final String TABLE = "users";


		public static void main(String[] args) throws SQLException {

				initConnection();


				//Création du client et se connecte à la passerelle.
				final GatewayDiscordClient client = DiscordClientBuilder.create("ODk0MjY4MjkwMTM2OTM2NDU4.YVniPw.Gpy_VBqm48zsFvmBdVMaGZUMH9k").build()
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

		private static void initConnection() throws SQLException {
				connectionPool = new BasicDataSource();
				connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
				connectionPool.setUsername("estiam");
				connectionPool.setPassword("m8Fd*6MbNRCKxiu4");
				connectionPool.setUrl("jdbc:mysql://5.196.224.14:3306/bot_discord_estiam?autoReconnect=true");
				connectionPool.setInitialSize(1);
				connectionPool.setMaxTotal(10);
				mysql = new MySQL(connectionPool);
				mysql.createTables();

				connectionPool.close();
		}

		public MySQL getMySQL() {
				return mysql;
		}

		public static Main getInstance() {
				return instance;
		}
}
