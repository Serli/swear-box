package controllers;

import java.util.List;

import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.BodyParser;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;
import org.pac4j.play.Config;
import play.GlobalSettings;
import play.Play;

import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;

import services.*;
import models.*;

public class Administration extends JavaController {

	/**
	 * Ajoute une personne à son utilisateur
	 * Récupere les champs pour créer la personne
	 * Puis ajoute la personne dans la base de donné
	 * @return Result : résultat de la fonction, Ok|pb
	 */
	@Transactional
    public static Result AjouterPersonne() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} else {
			String nom = json.findPath("nom").textValue();
			String prenom = json.findPath("prenom").textValue();
			if(nom == null || prenom==null) {
				return badRequest("Missing parameter [nom] or [prenom]");
			} else {
				final String image = Play.application().configuration().getString("AvatarDefault");
				Personne person = new Personne(nom,prenom,0,image);
				Google2Profile googleProfile = (Google2Profile) getUserProfile();
				String id = googleProfile.getEmail();
				AjoutPersonne.ajoutPersonne(person,id);
				return ok();
			}
		}
    }
    
}
