package dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jongo.MongoCollection;

import models.Consumer;
import models.Person;
import models.Statistics;
import play.Logger;
import play.Play;
import uk.co.panaxiom.playjongo.PlayJongo;

import com.cloudinary.Cloudinary;
import com.google.inject.Singleton;


/**
 * Groups the operations on the People collection
 *
 */
@Singleton
public final class PersonDAOImpl implements PersonDAO {
	
	private static MongoCollection consumers = PlayJongo.getCollection("Consumer");
	private static MongoCollection statistics = PlayJongo.getCollection("Statistics");
	
	private static final String ID = "{_id: #}";
	
    private Cloudinary cloudinary = com.cloudinary.Singleton.getCloudinary();

    /**
     * Add a person on the People collection
     * link the user with it
     * @param Person : person to add
     * @param String : user id
     */
    public void add(Person p,String id){
        //Recording the person

        //Get the user
        Consumer user = consumers.findOne(ID, id).as(Consumer.class);

        //Link the user with the person
        user.setPerson(p);
        consumers.update(ID, id).with(user);
    }

    /**
     * Delete a person on the People collection
     * @param Person : person to add
     * @param String : user id
     */
    public void delete(String id,String email){
        //Get the person and the user
        Consumer user = consumers.findOne(ID, email).as(Consumer.class);
        
        for(Person p : user.getPeople()) {
        	if(p.getIdPerson().equals(id)) {
        		
        		//Delete picture
                if(p.getPicture().startsWith("https")) {
                    try { 
                        String url = p.getPicture().substring(p.getPicture().lastIndexOf("/"));
                        url = url.substring(1,url.lastIndexOf("."));
                        if(!url.equals(Play.application().configuration().getString("AvatarDefault"))) {
                            cloudinary.api().deleteResources(Arrays.asList(url),null);
                        }
                    } catch (Exception e) {
                        Logger.info("Delete image on Cloudinary", e);
                    }
                }
                
                //Update Statistics collection
                Iterable<Statistics> cursor = statistics.find("{person.idPerson: #}}",p.getIdPerson()).as(Statistics.class);
        		for(Statistics s : cursor) {
        			statistics.remove(ID, s.get_id());
        		}
        		
        		//Update consumers collection
        		user.getPeople().remove(p);
                consumers.update(ID, email).with(user);
                
        		break;
        	}
        }        
    }

    /**
     * List all persons for an user
     * @param String : user id
     * @return List<Person> : list of persons
     */
   public List<Person> listByUser(String emailUser){
	   //Get the person and the user
    	Consumer u = consumers.findOne(ID, emailUser).as(Consumer.class);
    	List<Person> l = u.getPeople();
    	
        //Sort of the members list by Firstname
        Collections.sort(l, new Comparator<Person>() {
            public int compare(Person o1, Person o2) {
                return o1.getFirstname().toLowerCase().compareTo(o2.getFirstname().toLowerCase());
            }
        });
        return l;
    }

    /**
     * Discharge a person on the Person table
     * @param long : person id
     * @param String : user id
     */
    public void discharge(String id,String email){
        //Get the person
    	Consumer user = consumers.findOne(ID, email).as(Consumer.class);

        for(Person p : user.getPeople()) {
        	if(p.getIdPerson().equals(id)) {
        		p.setDebt(0);
        		break;
        	}
        }
        
        //Refresh DB
        consumers.update(ID, email).with(user);
    }


    /**
     * Update a person on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new name
     * @param String : new firstname
     */
    public void updateNameFirstname(String id,String email,String vName, String vFirstname){
 	   //Get the person and the user
    	Consumer user = consumers.findOne(ID, email).as(Consumer.class);

    	//If the user has rights
        for(Person p : user.getPeople()) {
        	if(p.getIdPerson().equals(id)) {
                p.setName(vName);
                p.setFirstname(vFirstname);
        		break;
        	}
        }
        
        //Refresh DB
        consumers.update(ID, email).with(user);
    }

    /**
     * Update a person picture on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new picture path
     */
    public void updatePicture(String id,String email,String vPicture){
  	   //Get the person and the user
     	Consumer user = consumers.findOne(ID, email).as(Consumer.class);
     	
    	//If the user has rights
        for(Person p : user.getPeople()) {
        	if(p.getIdPerson().equals(id)) {
        		if(p.getPicture().startsWith("https")) {
                    try { 
                        String url = p.getPicture().substring(p.getPicture().lastIndexOf("/"));
                        url = url.substring(1,url.lastIndexOf("."));
                        if(!url.equals(Play.application().configuration().getString("AvatarDefault"))) {
                            cloudinary.api().deleteResources(Arrays.asList(url),null);
                        }
                    } catch (Exception e) {
                        Logger.info("Delete image on Cloudinary", e);
                    }
                }
                p.setAdrImage(vPicture);
        		break;
        	}
        }
     	
        //Refresh DB
        consumers.update(ID, email).with(user);
    }
    /**
     * Increase a person debt
     * @param long : person id
     * @param String : user id
     */
    public void incrementDebt(String id,String email){
    	Consumer user = consumers.findOne(ID, email).as(Consumer.class);
        consumers.update("{people.idPerson: #}", id).with("{$inc: {people.$.debt: #}}", user.getAmount());
    }


}