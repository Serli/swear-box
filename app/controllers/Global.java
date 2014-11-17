package controllers;

import org.pac4j.core.client.Clients;
import org.pac4j.oauth.client.Google2Client;
import org.pac4j.play.Config;

import play.Application;
import play.GlobalSettings;
import play.Play;


public class Global extends GlobalSettings{

    @Override
    public void onStart(final Application app) {
    	super.onStart(app);
        //Config.setErrorPage401(views.html.error401.render().toString());
        //Config.setErrorPage403(views.html.error403.render().toString());

        final String GoogleId = Play.application().configuration().getString("GoogleId");
        final String GoogleSecret = Play.application().configuration().getString("GoogleSecret");
        final String baseUrl = Play.application().configuration().getString("baseUrl");
       
        // OAuth
        final Google2Client google2Client = new Google2Client(GoogleId, GoogleSecret);
        
        final Clients clients = new Clients(baseUrl + "/oauth2callback", google2Client); // , casProxyReceptor);
        Config.setClients(clients);
        //Config.setDefaultSuccessUrl("http://localhost:9000/protected");
        // for test purposes : profile timeout = 60 seconds
        // Config.setProfileTimeout(60);
    }
	
}
