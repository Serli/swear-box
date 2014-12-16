package controllers;

import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import dao.ConsumerDAO;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

/**
 * maneged actions for welcome view
 * @author Geoffrey
 *
 */
public class Welcome extends JavaController {

    /**
     * Action called when launching the app
     * @return connection view for an unconnected user or welcome view
     */
    public static Result index() {
        //Récupération du profil google de l'utilisateur
        Google2Profile googleProfile = (Google2Profile) getUserProfile();
        if(googleProfile == null) {
            final String urlGoogle = getRedirectAction("Google2Client").getLocation();
            return ok(index.render(urlGoogle));
        }
        return redirect(routes.Welcome.user());
    }

    /**
     * Action called for displaying the user view
     * @return user view
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result user() {
        //Récupération du nom dans le profil google de l'utilisateur
        Google2Profile googleProfile = (Google2Profile) getUserProfile();

        //Ajout de l'utilisateur dans la base de données si besoin
        String nom = googleProfile.getFirstName();
        String email = googleProfile.getEmail();
        ConsumerDAO.add(email);

        return ok(views.html.user.render(nom));
    }
    
   
    /**
     * Method's using in conf\routes and to display the admin page
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result admin() {
        return ok(views.html.admin.render());
    }

    /**
     * Method's using in conf\routes and to display the help page
     */
    public static Result help() {
        return ok(views.html.help.render());
    }

    /**
     * Method's using in conf\routes and to display the statistics page
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result statistics() {
        return ok(views.html.statistics.render());
    }


}
