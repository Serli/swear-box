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
 * maneged actions for administration view
 *
 */
public class Administration extends JavaController {

    /**
     * Add a person to a conected user
     * Use field informations to create a Person
     * Add this person on the database
     * @return Result : fonction result, Ok|pb
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
                Person person = new Person(name,firstname,10,picture);
                Google2Profile googleProfile = (Google2Profile) getUserProfile();
                String id = googleProfile.getEmail();
                PersonDAO.add(person,id);
                return ok();
            }
        }
    }

    /**
     * Delete a person for a conected user
     * @param Long : person id to delete
     * @return Result : fonction result, Ok|pb
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
     * Discharge a person for a conected user
     * @param Long : person id to delete
     * @return Result : fonction result, Ok|pb
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result discharge(Long idt) {
        Google2Profile googleProfile = (Google2Profile) getUserProfile();
        String email = googleProfile.getEmail();
        PersonDAO.discharge(idt,email);
        return ok();
    }
    
    /**
     * List all persons for a conected user
     * @return Result(JSON) : list of membres
     */
    @Transactional(readOnly=true)
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result listPerson() {
        Google2Profile googleProfile = (Google2Profile) getUserProfile();
        String emailUser = googleProfile.getEmail();
        List<Person> persons = PersonDAO.listByUser(emailUser);
        return ok(Json.toJson(persons));
    }
    
    
    /**
     * Update the default amount for a connected user
     * @param Long : user id 
     * @param int : new amount
     * @return Result : fonction result, Ok|pb
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result updateAmount(Long id, int vAmount) {
        Google2Profile googleProfile = (Google2Profile) getUserProfile();
        String email = googleProfile.getEmail();
        ConsumerDAO.updateAmount(email, vAmount);
        return ok();
    }
    
    /**
     * Update a person for a conected user
     * @param Long : user id 
     * @param String : new name
     * @param String : new firstname
     * @return Result : fonction result, Ok|pb
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result updateNameFirstname(Long id, String vName, String vFirstname ) {
        Google2Profile googleProfile = (Google2Profile) getUserProfile();
        String email = googleProfile.getEmail();
        PersonDAO.updateNameFirstname(id,email, vName,vFirstname);
        return ok();
    }
    
    /**
     * Update a person picture for a conected user
     * @param Long : user id 
     * @param String : new picture path
     * @return Result : fonction result, Ok|pb
     */
    @Transactional
    @RequiresAuthentication(clientName = "Google2Client")
    public static Result updatePicture(Long id, String vPicture) {
        Google2Profile googleProfile = (Google2Profile) getUserProfile();
        String email = googleProfile.getEmail();
        PersonDAO.updatePicture(id,email, vPicture);
        return ok();
    }
}
