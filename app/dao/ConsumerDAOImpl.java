package dao;


import models.Consumer;
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
}
