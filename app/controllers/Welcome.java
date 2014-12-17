package controllers;


import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;



import com.google.inject.Inject;

import dao.ConsumerDAO;
import dao.PersonDAO;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

/**
 * maneged actions for welcome view
 * @author Geoffrey
 *
 */
public class Welcome extends JavaController {

    @Inject
    private ConsumerDAO consumerDAO;
    
    @Inject
    private PersonDAO personDAO;
    
    /**
     * Action called when launching the app
     * @return connection view for an unconnected user or welcome view
     */
    public static Result index() {
        if(!isConnected()) {
            final String urlGoogle = getRedirectAction("Google2Client").getLocation();
            return ok(index.render(urlGoogle, new Boolean(false), new Integer(0)));
        }
        return redirect(routes.Welcome.user());
    }

    /**
     * Action called for displaying the user view
     * @return user view
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public Result user() {
        //Récupération du nom dans le profil google de l'utilisateur
        Google2Profile googleProfile = (Google2Profile) getUserProfile();

        //Ajout de l'utilisateur dans la base de données si besoin
        String nom = googleProfile.getFirstName();
        String email = googleProfile.getEmail();
        consumerDAO.add(email);

        return ok(views.html.user.render(nom, new Boolean(true), new Integer(0)));
    }
    

    /**
     * Action called for increase a person debt
     * @return Result : fonction result, Ok|pb
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public Result increaseDebt(Long id) {
    	Google2Profile googleProfile = (Google2Profile) getUserProfile();
        String email = googleProfile.getEmail();
        personDAO.incrementDebt(id,email);
        return ok();
    }

    /**
     * Method's using in conf\routes and to display the admin page
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result admin() {
        return ok(views.html.admin.render(new Boolean(true), new Integer(2)));
    }

    /**
     * Method's using in conf\routes and to display the help page
     */
    public static Result help() {
        Boolean isActive = isConnected();
        return ok(views.html.help.render(isActive, new Integer(3)));
    }

    /**
     * Method's using in conf\routes and to display the statistics page
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result statistics() {
        return ok(views.html.statistics.render(new Boolean(true), new Integer(1)));
    }

    /**
    * Method that returns true if the user logged in to the google account
    */
    private static Boolean isConnected() {
        //Récupération du profil google de l'utilisateur
        Google2Profile googleProfile = (Google2Profile) getUserProfile();
        return !(googleProfile == null);
    }


}
