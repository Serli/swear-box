package dao;


import models.Consumer;
import models.Person;
import play.db.jpa.JPA;

import com.google.inject.Singleton;

/**
 * Groups the operations on the Consumer table
 *
 */
@Singleton
public final class ConsumerDAOImpl implements ConsumerDAO {

    /**
     * Add a new user if he doesn't exist
     * @param String : User email 
     */
    public boolean add(String email) {
        //Get the user
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //If the user doesn't exist he is added
        if (u == null) {
            u = new Consumer(email);
            JPA.em().persist(u);
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
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //If the user exists the amount is modified
        if (u != null) {
            u.setAmount(vAmount);
        }
        
        JPA.em().flush();
    }
   
    public int getAmount(String email) {
        //Get the user
        Consumer u = JPA.em().find(Consumer.class, email);
        
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
    public void linkUserPerson(long idPerson,String idUser) {
        boolean test= true;

        //Get the user
        Consumer user = JPA.em().find(Consumer.class,idUser); 

        //Check if they are linked
        for (Person p :user.getPeople()){
            if(p.getIdPerson()==idPerson){
                test = false;
            }
        }

        //Link the person to the user
        if(test){
            Person pbd=JPA.em().find(Person.class,idPerson);
            user.setPerson(pbd);
            pbd.setUser(user);
        }
    }
}
