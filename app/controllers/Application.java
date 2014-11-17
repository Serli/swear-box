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

    @RequiresAuthentication(clientName = "Google2Client")
    public static Result user() {
        return ok(views.html.user.render());
    }
    
}
