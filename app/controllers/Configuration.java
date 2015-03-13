package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import models.Person;

import org.apache.commons.codec.binary.Base64;
import org.bson.types.ObjectId;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.Logger;
import play.Play;
import play.libs.Json;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.ConsumerDAO;
import dao.PersonDAO;

/**
 * Managed actions for administration view
 *
 */
@Singleton
public class Configuration extends JavaController {

    private static final String JSON_MESSG = "Expecting Json data";

    @Inject
    private ConsumerDAO consumerDAO;

    @Inject
    private PersonDAO personDAO;

    private Cloudinary cloudinary = com.cloudinary.Singleton.getCloudinary();

    /**
     * Add a person to a connected user
     * Use field informations to create a Person
     * Add this person on the database
     * @return Result : fonction result, Ok|pb
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public Result addPerson() {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest(JSON_MESSG);
        } else {
            String name = json.findPath("name").textValue();
            String firstname = json.findPath("firstname").textValue();
            if(name == null || firstname==null) {
                return badRequest("Missing parameter [name] or [firstname]");	
            } else {
                String url = cloudinary.url().format("png")
                        .generate(Play.application().configuration().getString("AvatarDefault"));
                String picture = url.replace("http", "https");
                Person person = new Person(ObjectId.get().toString(),name,firstname,0,picture);
                String id = getUserProfile().getEmail();
                personDAO.add(person,id);
                return ok();
            }
        }
    }

    /**
     * Delete a person for a connected user
     * @param Long : person id to delete
     * @return Result : fonction result, Ok|pb
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public Result deletePerson(String id) {
        String email = getUserProfile().getEmail();
        personDAO.delete(id,email);
        return ok();
    }


    /**
     * Discharge a person for a connected user
     * @param Long : person id to delete
     * @return Result : fonction result, Ok|pb
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public Result discharge(String idt) {
        String email = getUserProfile().getEmail();
        personDAO.discharge(idt,email);
        return ok();
    }

    /**
     * List all persons for a connected user
     * @return Result(JSON) : list of membres
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public Result listPerson() {
        String emailUser = getUserProfile().getEmail();
        List<Person> persons = personDAO.listByUser(emailUser);
        return ok(Json.toJson(persons));
    }

    /**
     * Put the current amount in a Json
     * @return Result(JSON) : amount of the id member
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public Result amount() {
        String emailUser = getUserProfile().getEmail();
        Integer amount = consumerDAO.getAmount(emailUser);
        return ok(Json.toJson(amount));
    }

    /**
     * Update the default amount for a connected user
     * @param Long : user id 
     * @param int : new amount
     * @return Result : fonction result, Ok|pb
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public Result updateAmount() {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest(JSON_MESSG);
        } else {
            String amount = json.findPath("amount").textValue();
            if(amount == null) {
                return badRequest("Missing parameter [amount]");
            } else {
                String email = getUserProfile().getEmail();
                consumerDAO.updateAmount(email, Integer.parseInt(amount));
                return ok();
            }
        }
    }

    /**
     * Update a person for a connected user
     * @param Long : user id 
     * @param String : new name
     * @param String : new firstname
     * @return Result : fonction result, Ok|pb
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public Result updateNameFirstname(String id) {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest(JSON_MESSG);
        } else {
            String name = json.findPath("name").textValue();
            String firstname = json.findPath("firstname").textValue();
            if(name == null || firstname==null) {
                return badRequest("Missing parameter [name] or [firstname]");
            } else {
                String email = getUserProfile().getEmail();
                personDAO.updateNameFirstname(id,email, name,firstname);
                return ok();
            }
        }
    }

    /**
     * Update a person picture for a connected user
     * @param Long : user id 
     * @return Result : fonction result, Ok|pb
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public Result updatePicture(String id) {
        MultipartFormData fd = request().body().asMultipartFormData();
        FilePart fp = fd.getFile("file");
        if (fp!= null){
            try {
                File f = fp.getFile();
				Map<?,?> options = Cloudinary.asMap(
                		  "transformation",
                		  new Transformation().width(200).height(200).crop("scale")
                		);
                Map<?,?> uploadResult = cloudinary.uploader().upload(f,options);
                String email = getUserProfile().getEmail();
                personDAO.updatePicture(id,email, (String)uploadResult.get("secure_url"));
                return ok();
            } catch (IOException e) {
                Logger.info("IOException", e);
                return badRequest(e.toString());
            }
        }
        return badRequest();
    }

    /**
     * Update a person picture for a connected user in the mobile app
     * @param Long : user id 
     * @return Result : fonction result, Ok|pb
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public Result updateImage(String id) {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest(JSON_MESSG);
        } else {
            String image64 = json.findPath("image64").textValue();
            if(image64 == null) {
                return badRequest("Missing parameter [image]");
            } else {
                try {
                    byte[] data = Base64.decodeBase64(image64);
                    Map<?,?> options = Cloudinary.asMap(
                              "transformation",
                              new Transformation().width(200).height(200).crop("scale")
                            );
                    Map<?,?> uploadResult = cloudinary.uploader().uploadLargeRaw(data,options);
                    String email = getUserProfile().getEmail();
                    personDAO.updatePicture(id,email, (String)uploadResult.get("secure_url"));
                } catch (FileNotFoundException e) {
                    Logger.info("FileNotFoundException", e);
                } catch (IOException e) {
                    Logger.info("IOException", e);
                }
                return ok();
            }
        }
    } 
}