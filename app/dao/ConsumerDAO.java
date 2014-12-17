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
        //Recuperation de l'utilisateur
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //Si l'utilisateur n'existe pas il est ajouté
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
        //Recuperation de l'utilisateur
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //Si l'utilisateur n'existe pas il est ajouté
        if (u != null) {
            u.setAmount(vAmount);
        }
        
        JPA.em().flush();
    }
   
    public static int getAmount(String email) {
        //Recuperation de l'utilisateur
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //Si l'utilisateur n'existe pas il est ajouté
        if (u != null) {
            return u.getAmount();
        }
        
        return -1;
    }
}
