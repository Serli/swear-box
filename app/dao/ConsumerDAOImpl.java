package dao;


import org.jongo.MongoCollection;

import models.Consumer;
import models.Person;
import uk.co.panaxiom.playjongo.PlayJongo;

import com.google.inject.Singleton;

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
        	consumers.remove("{_id: #}", u.getEmail());
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
}