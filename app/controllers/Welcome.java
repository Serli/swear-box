package controllers;


import models.Person;

import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.Play;
import play.db.jpa.Transactional;
import play.mvc.Result;
import views.html.index;

import com.cloudinary.Cloudinary;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.ConsumerDAO;
import dao.PersonDAO;

/**
 * Managed actions for welcome view
 * @author Geoffrey
 *
 */
@Singleton
public class Welcome extends JavaController {

    private static final Integer USER_PAGE = 0;
    private static final Integer STATISTICS_PAGE = 1;
    private static final Integer ADMIN_PAGE = 2;
    private static final Integer HELP_PAGE = 3;

    @Inject
    private ConsumerDAO consumerDAO;

    @Inject
    private PersonDAO personDAO;

    private Cloudinary cloudinary = com.cloudinary.Singleton.getCloudinary();

    /**
     * Action called when launching the app
     * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
     */
    public static Result index() {
        if(!isConnected()) {
            final String urlGoogle = getRedirectAction("Google2Client").getLocation();
            return ok(index.render(urlGoogle, new Boolean(false), USER_PAGE));
        }
        return redirect(routes.Welcome.user());
    }

    /**
     * Action called for displaying the user view
     * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public Result user() {
        //Get the name in the user google profile
        Google2Profile googleProfile = (Google2Profile) getUserProfile();

        //Add the user to the data if it is needed
        String firstname = googleProfile.getFirstName();
        String email = googleProfile.getEmail();
        if(consumerDAO.add(email)){
            //Add a member with the name and the firstname of the user
            String url = cloudinary.url().format("png")
                    .generate(Play.application().configuration().getString("AvatarDefault"));
            String picture = url.replace("http", "https");
            String name = googleProfile.getFamilyName();
            Person person = new Person(name,firstname,0,picture);
            personDAO.add(person,email);
        }
        return ok(views.html.user.render(firstname, new Boolean(true), USER_PAGE));
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
     * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result admin() {
        return ok(views.html.admin.render(new Boolean(true), ADMIN_PAGE));
    }

    /**
     * Method's using in conf\routes and to display the help page
     * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
     */
    public static Result help() {
        return ok(views.html.help.render(isConnected(), HELP_PAGE));
    }

    /**
     * Method's using in conf\routes and to display the statistics page
     * @return Result : fonction result, help html view with 2 arguments (Boolean : the connection of not of a google user, Integer : the id of the html view)
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result statistics() {
        return ok(views.html.statistics.render(new Boolean(true), STATISTICS_PAGE));
    }

    /**
     * Method which get the user google profile and return if the user is connected
     * @return Boolean : true = User connected
     */
    private static Boolean isConnected() {
        Google2Profile googleProfile = (Google2Profile) getUserProfile();
        return !(googleProfile == null);
    }


}
