package controllers;

import org.pac4j.core.client.Clients;
import org.pac4j.oauth.client.Google2Client;
import org.pac4j.play.Config;
import play.Application;
import play.GlobalSettings;
import play.Play;

/**
 * Performs tasks in launching the application
 * @author Geoffrey
 *
 */
public class Global extends GlobalSettings{

    /**
     * add a user for an authentification  (OAuth2)
     */
    @Override
    public void onStart(final Application app) {
        super.onStart(app);

        //Récupération des données necessaires dans le fichier application.conf
        final String googleId = Play.application().configuration().getString("GoogleId");
        final String googleSecret = Play.application().configuration().getString("GoogleSecret");
        final String baseUrl = Play.application().configuration().getString("baseUrl");

        //Création du cient Google2Client pour la connexion Google via OAuth2
        final Google2Client google2Client = new Google2Client(googleId, googleSecret);

        //Ajout des clients à Config
        final Clients clients = new Clients(baseUrl + "/oauth2callback", google2Client);
        Config.setClients(clients);
    }

}
