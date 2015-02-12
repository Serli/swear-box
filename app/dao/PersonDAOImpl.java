package dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.DBRef;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.WriteResult;
import models.Consumer;
import models.Person;
import models.Statistics;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.modules.mongodb.jackson.MongoDB;

import com.cloudinary.Cloudinary;
import com.google.inject.Singleton;

/**
 * Groups the operations on the Person table
 *
 */
@Singleton
public final class PersonDAOImpl implements PersonDAO {
    private static JacksonDBCollection<Consumer, String> consumers = MongoDB.getCollection("Consumer", Consumer.class, String.class);
	private static JacksonDBCollection<Person, String> people = MongoDB.getCollection("Person", Person.class, String.class);
	private static JacksonDBCollection<Statistics, String> statistics = MongoDB.getCollection("Statistics", Statistics.class, String.class);

    private Cloudinary cloudinary = com.cloudinary.Singleton.getCloudinary();

    /**
     * Add a person on the Person table
     * link the user with it
     * @param Person : person to add
     * @param String : user id
     */
    public void add(Person p,String id){
        //Recording the person
    	people.insert(p);

        //Get the user
        Consumer user = consumers.findOneById(id); 
        DBRef<Person,String> pref = new DBRef<Person,String>(p.getIdPerson(),Person.class);
        DBRef<Consumer,String> uref = new DBRef<Consumer,String>(id,Consumer.class);

        //Link the user with the person
        user.setPerson(pref);
        p.setUser(uref);
        consumers.updateById(id, user);      
        people.updateById(p.getIdPerson(), p);

 
    }

    /**
     * Delete a person on the Person table
     * @param Person : person to add
     * @param String : user id
     */
    public void delete(String id,String email){
        //Get the person
        Person pbd = people.findOneById(id);
        Consumer user = consumers.findOneById(email);
        
        //Delete picture
        for(DBRef<Person,String> p : user.getPeople()) {
        	if(p.getId().equals(pbd.getIdPerson())) {
                if(pbd.getPicture().startsWith("https")) {
                    try { 
                        String url = pbd.getPicture().substring(pbd.getPicture().lastIndexOf("/"));
                        url = url.substring(1,url.lastIndexOf("."));
                        if(!url.equals(Play.application().configuration().getString("AvatarDefault"))) {
                            cloudinary.api().deleteResources(Arrays.asList(url),null);
                        }
                    } catch (Exception e) {
                        Logger.info("Delete image on Cloudinary", e);
                    }
                }
        		break;
        	}
        }

  
        //Refresh consumer and statistics collection
       for(DBRef<Person,String> p : user.getPeople()) {
        	if(p.getId().equals(pbd.getIdPerson())) {
                DBCursor<Statistics> cursor = statistics.find(DBQuery.in( "person.$id" , pbd.getIdPerson()));
                List<Statistics> stats = cursor.toArray();
        		for(Statistics s : stats) {
        			statistics.remove(s);
        		}
        		user.getPeople().remove(p);
        		break;
        	}
        }
        consumers.updateById(email, user);
        
        //Delete the person
        people.remove(pbd);
    }

    /**
     * List all persons for an user
     * @param String : user id
     * @return List<Person> : list of persons
     */
   public List<Person> listByUser(String emailUser){
	   //Get the person and the user
    	Consumer u = consumers.findOneById(emailUser);
    	List<Person> l = consumers.fetch(u.getPeople());
    	
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
    	Person pbd = people.findOneById(id);
    	Consumer user = consumers.findOneById(email);

        for(DBRef<Person,String> p : user.getPeople()) {
        	if(p.getId().equals(pbd.getIdPerson())) {
        		pbd.setDebt(0);
        		break;
        	}
        }
        
        //Refresh DB
        people.updateById(id,pbd);
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
    	Person pbd = people.findOneById(id);
    	Consumer user = consumers.findOneById(email);

    	//If the user has rights
        for(DBRef<Person,String> p : user.getPeople()) {
        	if(p.getId().equals(pbd.getIdPerson())) {
                pbd.setName(vName);
                pbd.setFirstname(vFirstname);
        		break;
        	}
        }
        
        //Refresh DB
        people.updateById(id,pbd);
    }

    /**
     * Update a person picture on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new picture path
     */
    public void updatePicture(String id,String email,String vPicture){
  	   //Get the person and the user
     	Person pbd = people.findOneById(id);
     	Consumer user = consumers.findOneById(email);
     	
    	//If the user has rights
        for(DBRef<Person,String> p : user.getPeople()) {
        	if(p.getId().equals(pbd.getIdPerson())) {
        		if(pbd.getPicture().startsWith("https")) {
                    try { 
                        String url = pbd.getPicture().substring(pbd.getPicture().lastIndexOf("/"));
                        url = url.substring(1,url.lastIndexOf("."));
                        if(!url.equals(Play.application().configuration().getString("AvatarDefault"))) {
                            cloudinary.api().deleteResources(Arrays.asList(url),null);
                        }
                    } catch (Exception e) {
                        Logger.info("Delete image on Cloudinary", e);
                    }
                }
                pbd.setAdrImage(vPicture);
        		break;
        	}
        }
     	
        //Refresh DB
        people.updateById(id,pbd);	
    }
    /**
     * Increase a person debt
     * @param long : person id
     * @param String : user id
     */
    public void incrementDebt(String id,String email){
        //Get the person
       	Person pbd = people.findOneById(id);
    	Consumer user = consumers.findOneById(email);

        for(DBRef<Person,String> p : user.getPeople()) {
        	if(p.getId().equals(pbd.getIdPerson())) {
                if(Integer.MAX_VALUE-pbd.getDebt()>user.getAmount()) {
                    pbd.setDebt(pbd.getDebt()+user.getAmount());
                }
                else {
                    pbd.setDebt(Integer.MAX_VALUE);
                }
        		break;
        	}
        }

        //Refresh DB
        people.updateById(id,pbd);	
    }


}