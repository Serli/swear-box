package controllers;

import java.util.List;

import play.libs.Json;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

public class Application extends JavaController {

	/**
	 * Action appelée lors du lancement de l'appli
	 * @return la vue connexion si l'utilisateur n'est pas connecté, appelle l'action user() sinon
	 */
    public static Result index() {
    	Google2Profile googleProfile = (Google2Profile) getUserProfile();
    	if(googleProfile == null) {
    		final String urlGoogle = getRedirectAction("Google2Client").getLocation();
        	return ok(index.render(urlGoogle));
    	}
    	else {
    		return redirect(routes.Application.user());
    	}
    }

    /**
     * Action appelé pour l'affichage de la vue user
     * @return la vue user avec en paramètre le nom de la personne connectée
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result user() {
		Google2Profile googleProfile = (Google2Profile) getUserProfile();
		String nom = googleProfile.getFirstName();
        return ok(views.html.user.render(nom));
    }

    /**
    * Methode s'utilisant dans conf\routes et permettant d'afficher la page admin
    */
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result admin() {
        return ok(views.html.admin.render());
    }
    
}
