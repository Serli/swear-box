package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import dao.*;

import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.Play;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import models.*;

/**
 * Gère les actions de la vue admin
 *
 */
public class Administration extends JavaController {

    /**
     * Ajoute une personne à son utilisateur
     * Récupere les champs pour créer la personne
     * Puis ajoute la personne dans la base de donné
     * @return Result : résultat de la fonction, Ok|pb
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result addPerson() {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String name = json.findPath("name").textValue();
            String firstname = json.findPath("firstname").textValue();
            if(name == null || firstname==null) {
                return badRequest("Missing parameter [name] or [firstname]");
            } else {
                final String picture = Play.application().configuration().getString("AvatarDefault");
                Person person = new Person(name,firstname,0,picture);
                Google2Profile googleProfile = (Google2Profile) getUserProfile();
                String id = googleProfile.getEmail();
                PersonDAO.add(person,id);
                return ok();
            }
        }
    }

    /**
     * Supprime une personne
     * utilise l'identifiant l'utilisateur avec qui il est lié pour plus de sécurité
     * @return Result : résultat de la fonction, Ok|pb
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result deletePerson(Long id) {
        Google2Profile googleProfile = (Google2Profile) getUserProfile();
        String email = googleProfile.getEmail();
        PersonDAO.delete(id,email);
        return ok();
    }

    
    /**
     * Acquitte une personne
     * utilise l'identifiant l'utilisateur avec qui il est lié pour plus de sécurité
     * @return Result : résultat de la fonction, Ok|pb
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result debt() {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            long id = -1;
            id = json.findPath("id").longValue();
            if(id == -1 ) {
                return badRequest("Missing parameter [id]");
            } else {
            	Google2Profile googleProfile = (Google2Profile) getUserProfile();
				String email = googleProfile.getEmail();
                PersonDAO.debt(id,email);
                return ok();
            }
        }
    }
    
    /**
     * Action appelée pour récupérer la liste des membres de l'utilisateur
     * @return la liste des membres au format JSON
     */
    @Transactional(readOnly=true)
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result listPerson() {
        Google2Profile googleProfile = (Google2Profile) getUserProfile();
        String emailUser = googleProfile.getEmail();
        List<Person> persons = PersonDAO.listByUser(emailUser);
        return ok(Json.toJson(persons));
    }}
