package dao;


import net.vz.mongodb.jackson.DBRef;
import net.vz.mongodb.jackson.JacksonDBCollection;
import models.Consumer;
import models.Person;
import play.modules.mongodb.jackson.MongoDB;

import com.google.inject.Singleton;

/**
 * Groups the operations on the Consumer table
 *
 */
@Singleton
public final class ConsumerDAOImpl implements ConsumerDAO {
	
	private static JacksonDBCollection<Consumer, String> consumers = MongoDB.getCollection("Consumer", Consumer.class, String.class);
	private static JacksonDBCollection<Person, String> people = MongoDB.getCollection("Person", Person.class, String.class);
	
    /**
     * Add a new user if he doesn't exist
     * @param String : User email 
     */
    public boolean add(String email) {
        //Get the user
    	Consumer u = consumers.findOneById(email);
    	
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
    	Consumer u = consumers.findOneById(email);
    	
        //If the user doesn't exist he is added
        if (u != null) {
            consumers.remove(u);
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
        Consumer u = consumers.findOneById(email);
        
        //If the user exists the amount is modified
        if (u != null) {
            u.setAmount(vAmount);
        }
        consumers.updateById(email, u);
    }
   
    public int getAmount(String email) {
        //Get the user
    	 Consumer u = consumers.findOneById(email);
        
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
    public void linkUserPerson(String idPerson,String idUser) {
        boolean test= true;

        //Get the person and the user
        Person pbd = people.findOneById(idPerson);
        Consumer user = consumers.findOneById(idUser);

        for(DBRef<Person,String> p : user.getPeople()) {
        	if(p.getId().equals(pbd.getIdPerson())) {
        		test = false;
        		break;
        	}
        }

        //Link the person to the user
        if(test){
        	DBRef<Person,String> pref = new DBRef<Person,String>(pbd.getIdPerson(),Person.class);
        	DBRef<Consumer,String> uref = new DBRef<Consumer,String>(idUser,Consumer.class);
        	user.setPerson(pref);
        	pbd.setUser(uref);
        	consumers.updateById(idUser, user);      
        	people.updateById(pbd.getIdPerson(), pbd);
        }
    }
}