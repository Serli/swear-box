package dao;

import models.Person;

/**
 * Groups the operations on the Consumer table
 *
 */
public interface ConsumerDAO {

    /**
     * Add a new user if he doesn't exist
     * @param String : User email 
     */
    public boolean add(String email);
    
    /**
     * Delete user if he exists
     * @param String : User email 
     */
    public boolean delete(String email);
    
    /**
     * Update the user amount
     * @param String : User email 
     * @param int : new amount 
     */
    public void updateAmount(String email, int vAmount);
   
    /**
     * Get the user amount
     * @param String : User email 
     * @return int : current amount 
     */
    public int getAmount(String email);

    /**
     * Link a person to an user
     * @param String : person id
     * @param String : user id
     */
    public void linkUserPerson(Person pe,String idUser);

}