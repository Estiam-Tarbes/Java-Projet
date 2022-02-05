package fr.nicolas.bot;

import discord4j.common.JacksonResources;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// Crée par Nicolas S.

public class GlobalCommandRegistrar {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private final RestClient restClient;

	public GlobalCommandRegistrar(RestClient restClient) {
		this.restClient = restClient;
	}

	// Enrengistrement des commandes
	protected void registerCommands() throws IOException {
		//Créer un ObjectMapper qui supporte les classes Discord4J
		final JacksonResources d4jMapper = JacksonResources.create();

		// Variables de commodité pour faciliter la lecture du code ci-dessous
		final ApplicationService applicationService = restClient.getApplicationService();
		final long applicationId = restClient.getApplicationId().block();

		//Récupération de nos commandes json depuis les ressources comme données de commande
		List<ApplicationCommandRequest> commands = new ArrayList<>();
		for (String json : getCommandsJson()) {
			ApplicationCommandRequest request = d4jMapper.getObjectMapper()
					.readValue(json, ApplicationCommandRequest.class);

			commands.add(request); //Ajout dans une liste
		}

        /* Envoie au près de discord pour enrengistrer les commandes
        */
		applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, commands)
				.doOnNext(ignore -> LOGGER.debug("Commandes globales enregistrées avec succès"))
				.doOnError(e -> LOGGER.error("Échec de l'enregistrement des commandes globales", e))
				.subscribe();
	}

	// Récupération des fichiers json

	private static List<String> getCommandsJson() throws IOException {
		//Emplacement des commandes
		final String commandsFolderName = "commandes/";

		//Récupération des fichiers json
		URL url = GlobalCommandRegistrar.class.getClassLoader().getResource(commandsFolderName);
		Objects.requireNonNull(url, commandsFolderName + " n'a pas pu être trouvé");

		File folder;
		try {
			folder = new File(url.toURI());
		} catch (URISyntaxException e) {
			folder = new File(url.getPath());
		}

		//Récupère tous les fichiers contenus dans ce dossier et renvoie le contenu des fichiers sous la forme d'une liste de chaînes de caractères.
		List<String> list = new ArrayList<>();
		File[] files = Objects.requireNonNull(folder.listFiles(), folder + " n'est pas un répertoire");

		for (File file : files) {
			String resourceFileAsString = getResourceFileAsString(commandsFolderName + file.getName());
			list.add(resourceFileAsString);
		}
		return list;
	}

	private static String getResourceFileAsString(String fileName) throws IOException {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		try (InputStream resourceAsStream = classLoader.getResourceAsStream(fileName)) {
			if (resourceAsStream == null) return null;
			try (InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
				 BufferedReader reader = new BufferedReader(inputStreamReader)) {
				return reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}
		}
	}

}
