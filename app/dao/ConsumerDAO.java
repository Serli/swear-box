package dao;

import play.db.jpa.JPA;
import models.Consumer;

/**
 * Groups the operations on the Consumer table
 *
 */
public final class ConsumerDAO {

    private ConsumerDAO(){
    }

    /**
     * Add a new user if he doesn't exist
     * @param String : User email 
     */
    public static void add(String email) {
        //Get the user
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //If the user doesn't exist he is added
        if (u == null) {
            u = new Consumer(email);
            JPA.em().persist(u);
        }
    }
    
    /**
     * Update the user amount
     * @param String : User email 
     * @param int : new amount 
     */
    public static void updateAmount(String email, int vAmount) {
        //Get the user
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //If the user exists its amount is modified
        if (u != null) {
            u.setAmount(vAmount);
        }
        
        JPA.em().flush();
    }
   
    /**
     * Get the user amount
     * @param String : User email 
     * @return int : current amount 
     */
    public static int getAmount(String email) {
        //Get the user
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //If the user exist its amount is returned
        if (u != null) {
            return u.getAmount();
        }
        
        return -1;
    }
}
