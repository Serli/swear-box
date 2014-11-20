package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.Play;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
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
    @RequiresAuthentication(clientName = "Google2Client")
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
	
	/**
	 * Supprime une personne
	 * utilise le nom le prenom et l'utilisateur avec qui il est lié pour plus de sécurité (doublon nom prenom)
	 * @return Result : résultat de la fonction, Ok|pb
	 */
	@Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result SupprimerPersonne() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} else {
			long id = -1;
			id = json.findPath("id").longValue();
			if(id == -1 ) {
				return badRequest("Missing parameter [id]");
			} else {
				SupprimerPersonne.supprimerPersonne(id);
				return ok();
			}
		}
    }

	/**
     * Action appelée pour récupérer la liste des membres de l'utilisateur
     * @return la liste des membres au format JSON
     */
	@Transactional
	public static Result listePersonnes() {
		Google2Profile googleProfile = (Google2Profile) getUserProfile();
		String emailUser = googleProfile.getEmail();
		List<Personne> personnes = ListePersonnes.listePersonnes(emailUser);
		return ok(Json.toJson(personnes));
	}}
