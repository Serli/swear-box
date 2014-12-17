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
     * Add a user for an authentification  (OAuth2)
     */
    @Override
    public void onStart(final Application app) {
        super.onStart(app);

        //Get datas needed in the file application.conf
        final String googleId = Play.application().configuration().getString("GoogleId");
        final String googleSecret = Play.application().configuration().getString("GoogleSecret");
        final String baseUrl = Play.application().configuration().getString("baseUrl");

        //Create a client Google2Client for the Google connection with OAuth2
        final Google2Client google2Client = new Google2Client(googleId, googleSecret);

        //Add clients to Config
        final Clients clients = new Clients(baseUrl + "/oauth2callback", google2Client);
        Config.setClients(clients);
    }

}
