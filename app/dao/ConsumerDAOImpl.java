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
	private static JacksonDBCollection<Consumer, String> coll = MongoDB.getCollection("Consumer", Consumer.class, String.class);
	private static JacksonDBCollection<Person, String> coll1 = MongoDB.getCollection("Person", Person.class, String.class);
    /**
     * Add a new user if he doesn't exist
     * @param String : User email 
     */
    public boolean add(String email) {
        //Get the user
    	Consumer u = coll.findOneById(email);
        //If the user doesn't exist he is added
        if (u == null) {
            u = new Consumer(email);
            coll.insert(u);
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
        Consumer u = coll.findOneById(email);
        
        //If the user exists the amount is modified
        if (u != null) {
            u.setAmount(vAmount);
        }
        
        coll.updateById(email, u);
    }
   
    public int getAmount(String email) {
        //Get the user
    	 Consumer u = coll.findOneById(email);
        
        //If the user exists the amount is returned
        if (u != null) {
            return u.getAmount();
        }
        
        return -1;
    }
    
    /**
     * Link a person to an user
     * @param long : person id
     * @param String : user id
     */
    public void linkUserPerson(String idPerson,String idUser) {
        boolean test= true;

        //Get the person
        Person pbd = coll1.findOneById(idPerson);
        Consumer user = coll.findOneById(idUser);

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
        	coll.updateById(idUser, user);      
        	coll1.updateById(pbd.getIdPerson(), pbd);
        }
    }
}