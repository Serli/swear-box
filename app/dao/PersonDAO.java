package dao;

import java.util.List;
import models.*;

/**
 * Groups the operations on the Person table
 *
 */
public interface PersonDAO{

    /**
     * Add a person on the Person table
     * Link the user with it
     * @param Person : person to add
     * @param String : user id
     */
    public void add(Person p,String id);


    /**
     * Delete a person on the Person table
     * @param Person : person to add
     * @param String : user id
     */

    public void delete(String id,String email);


    /**
     * List all persons for an user
     * @param String : user id
     * @return List<Person> : list of persons
     */
    public List<Person> listByUser(String emailUser);

    /**
     * Discharge a person on the Person table
     * @param long : person id
     * @param String : user id
     */
    public void discharge(String id,String email);



    /**
     * Update a person on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new name
     * @param String : new firstname
     */
    public void updateNameFirstname(String id,String email,String vName, String vFirstname);

    /**
     * Update a person picture on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new picture path
     */
    public void updatePicture(String id,String email,String vPicture);

    /**
     * Increase a person debt
     * @param long : person id
     * @param String : user id
     */
    public void incrementDebt(String id,String email);

}