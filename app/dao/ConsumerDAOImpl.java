package dao;


import java.util.ArrayList;
import java.util.List;

import org.jongo.MongoCollection;

import play.Play;
import play.libs.Json;
import models.Consumer;
import models.Person;
import uk.co.panaxiom.playjongo.PlayJongo;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;

/**
 * Groups the operations on the Consumer table
 *
 */
@Singleton
public final class ConsumerDAOImpl implements ConsumerDAO {
	
	private static MongoCollection consumers = PlayJongo.getCollection("Consumer");
    /**
     * Add a new user if he doesn't exist
     * @param String : User email 
     */
    public boolean add(String email) {
        //Get the user
    	Consumer u = consumers.findOne("{_id: #}", email).as(Consumer.class);
    	
        //If the user doesn't exist he is added
        if (u == null) {
            u = new Consumer(email);
            
            List<String> admin = Play.application().configuration().getStringList("Admin");
            if (admin.contains(email))
            	u.setAdmin(true);
            
            consumers.insert(u);
            return true;
        }
        return false;
    }
    
    /**
     * Delete user if he exists
     * @param String : User email 
     */
    public boolean delete(String email) {
        //Get the user
    	Consumer u = consumers.findOne("{_id: #}", email).as(Consumer.class);
    	
        //If the user doesn't exist he is added
        if (u != null) {
        	consumers.remove("{_id: #}", email);
            return true;
        }
        return false;
    }
    
    /**
     * Update the user amount
     * @param String : User email 
     * @param int : new amount 
     */
    public void updateAmount(String email, int vAmount) {
        //Get the user
        Consumer u = consumers.findOne("{_id: #}", email).as(Consumer.class);
        
        //If the user exists the amount is modified
        if (u != null) {
            u.setAmount(vAmount);
        }
        consumers.update("{_id: #}", email).with(u);
    }
   
    public int getAmount(String email) {
        //Get the user
    	 Consumer u = consumers.findOne("{_id: #}", email).as(Consumer.class);
        
        //If the user exists the amount is returned
        if (u != null) {
            return u.getAmount();
        }
        
        return -1;
    }
    
    /**
     * Link a person to an user
     * @param String : person id
     * @param String : user id
     */
    public void linkUserPerson(Person pe,String idUser) {
        boolean test= true;

        //Get the person and the user
        Consumer user = consumers.findOne("{_id: #}", idUser).as(Consumer.class);

        for(Person p : user.getPeople()) {
        	if(p.getIdPerson().equals(pe.getIdPerson())) {
        		test = false;
        		break;
        	}
        }

        //Link the person to the user
        if(test){
        	user.setPerson(pe);
        	consumers.update("{_id: #}", idUser).with(user);
        }
    }

	public boolean inBlackLister(String email) {
		//Get the user
    	Consumer u = consumers.findOne("{_id: #}", email).as(Consumer.class);
    	
		if(u != null)
			return u.isBlackListed();
		
		return true;
	}

	public JsonNode findOne(String email) {
		
		//Get the person and the user
        BasicDBObject user = consumers.findOne("{_id: #}", email).as(BasicDBObject.class);
        return Json.toJson(user);
	}
    
    /**
     * Get users
     * @return List<Consumer> : list of consumer
     */
    public JsonNode findAll(){
		List<BasicDBObject> a = consumers.aggregate("{ $group: { _id: { email : '$_id', blck: '$blackListed', admin : '$admin' }}}")
				.as(BasicDBObject.class);
		ArrayList<BasicDBObject> res = new ArrayList<>();
		for(BasicDBObject bo : a) {	
			JsonNode j = Json.toJson(bo);
			res.add(new BasicDBObject().append("email", j.findValue("email")).append("blacklisted", j.findValue("blck")).append("admin", j.findValue("admin")));
		}
		return Json.toJson(res);
    	
    }
    
    /**
     * add user(email) to administrator
     * @param String id : admin who add another admin
     * @param String email : user to set admin
     * @return boolean : success
     */
    public boolean setAdmin(String id,String email) {
        //Get the user
    	Consumer u = consumers.findOne("{_id: #}", id).as(Consumer.class);
    	Consumer u2 = consumers.findOne("{_id: #}", email).as(Consumer.class);
    	
        //If the user doesn't exist he is added
        if (u != null && u2 !=null) {
        	if(u.isAdmin())
        		u2.setAdmin(true);
        	consumers.update("{_id: #}", email).with(u2);
            return true;
        }
        return false;
    }
    
    /**
     * add user(email) to the blacklist
     * @param String id : admin who add a user to blacklist
     * @param String email : user to blacklist
     * @param boolean cond : value of blacklist
     * @return boolean : success
     */
    public boolean setBlacklisted(String id,String email, boolean cond) {
        //Get the user
    	Consumer u = consumers.findOne("{_id: #}", id).as(Consumer.class);
    	Consumer u2 = consumers.findOne("{_id: #}", email).as(Consumer.class);
    	
        //If the user doesn't exist he is added
        if (u != null && u2 !=null) {
        	if(u.isAdmin())
        		u2.setBlackLister(cond);
        	consumers.update("{_id: #}", email).with(u2);
            return true;
        }
        return false;
    }
}