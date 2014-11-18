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
import services.AjoutUtilisateur;
import views.html.*;
/**
 * Gere les actions des vues connexion et user
 * @author Geoffrey
 *
 */
public class Accueil extends JavaController {

	/**
	 * Action appelée lors du lancement de l'appli
	 * @return la vue connexion si l'utilisateur n'est pas connecté, appelle l'action user() sinon
	 */
    public static Result index() {
    	//Récupération du profil google de l'utilisateur
    	Google2Profile googleProfile = (Google2Profile) getUserProfile();
    	if(googleProfile == null) {
    		final String urlGoogle = getRedirectAction("Google2Client").getLocation();
        	return ok(index.render(urlGoogle));
    	}
    	else {
    		return redirect(routes.Accueil.user());
    	}
    }

    /**
     * Action appelée pour l'affichage de la vue user
     * @return la vue user avec en paramètre le nom de la personne connectée
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result user() {
    	//Récupération du nom dans le profil google de l'utilisateur
		Google2Profile googleProfile = (Google2Profile) getUserProfile();
		
		//Ajout de l'utilisateur dans la base de données si besoin
		String nom = googleProfile.getFirstName();
		String email = googleProfile.getEmail();
		AjoutUtilisateur.ajoutUtilisateur(email);
		
        return ok(views.html.user.render(nom));
    }

    /**
    * Methode s'utilisant dans conf\routes et permettant d'afficher la page admin
    */
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result admin() {
        return ok(views.html.admin.render());
    }

    /**
    * Methode s'utilisant dans conf\routes et permettant d'afficher la page d'aide
    */
    public static Result aide() {
        return ok(views.html.aide.render());
    }
    
}
