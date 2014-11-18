package controllers;

import java.util.List;

import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.BodyParser;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

import services.*;
import models.*;

public class Administration extends JavaController {

	@Transactional
    public static void AjoutPersonne() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			//badRequest("Expecting Json data");
		} else {
			String nom = json.findPath("nom").textValue();
			String prenom = json.findPath("prenom").textValue();
			if(nom == null || prenom==null) {
				//badRequest("Missing parameter [nom] or [prenom]");
			} else {
				final String image = Play.application().configuration().getString("AvatarDefault");
				Personne person = New Presonne(nom,prenom,0,image);
				SQLAjoutPersonne.AjoutPersonne(person);
			}
		}
    }
    
}
