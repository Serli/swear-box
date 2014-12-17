package dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.*;

/**
 * Groups the operations on the Person table
 *
 */
public final class PersonDAO{

    private PersonDAO(){
    }

    /**
     * Add a person on the Person table
     * Link the user with it
     * @param Person : person to add
     * @param String : user id
     */
    public static void add(Person p,String id){
        //Recording a person
        JPA.em().persist(p);

        //Get the user
        Consumer user = JPA.em().find(Consumer.class,id); 
        user.setPerson(p);
        p.setUser(user);

        //Link the user with the person
        JPA.em().flush();
    }

    /**
     * Delete a person on the Person table
     * @param Person : person to add
     * @param String : user id
     */
	public static void delete(long id,String email){
		//Get the person
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,email);
		
		//Delete keys in U_P
		if (user.getPeople().contains(pbd)){
			for (Consumer u: pbd.getUsers()){	
				u.getPeople().remove(pbd);
			}
		}
	
		//Refresh the DB
		JPA.em().flush();	
		
		//Delete the person
		JPA.em().remove(pbd);
	}

    /**
     * List all persons for an user
     * @param String : user id
     * @return List<Person> : list of persons
     */
	public  static List<Person> listByUser(String emailUser){
	    Consumer u = JPA.em().find(Consumer.class, emailUser);
	    
	    //Sort of the members list by Firstname
	    Collections.sort(u.getPeople(), new Comparator<Person>()
	    {
	        public int compare(Person o1, Person o2)
	        {
	            return o1.getFirstname().compareTo(o2.getFirstname());
	        }
	    });
	    
	    return u.getPeople();
	}

    /**
     * Discharge a person on the Person table
     * @param long : person id
     * @param String : user id
     */
	public static void discharge(long id,String email){
		//Get the person
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,email);
		
		//If the user can
		if (user.getPeople().contains(pbd)){
			pbd.setDebt(0);
		}
	
		//Refresh DB
		JPA.em().flush();	
	}
	
	
	/**
     * Update a person on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new name
     * @param String : new firstname
     */
	public static void updateNameFirstname(long id,String email,String vName, String vFirstname){
		//Get the person
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,email);
		
		//If the user can
		if (user.getPeople().contains(pbd)){
			pbd.setName(vName);
			pbd.setFirstname(vFirstname);
		}

		//Refresh BD
		JPA.em().flush();	
	}
	
	/**
     * Update a person picture on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new picture path
     */
	public static void updatePicture(long id,String email,String vPicture){
		//Get the person
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,email);
		
		//If the user can
		if (user.getPeople().contains(pbd)){
			pbd.setAdrImage(vPicture);
		}
	
		//Refresh BD
		JPA.em().flush();	
	}

	 /**
     * Increase a person debt
     * @param long : person id
     * @param String : user id
     */
    public static void incrementDebt(long id,String email){
		//Get the person
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,email);
		
		//If the user can
		if (user.getPeople().contains(pbd)){
			pbd.setDebt(pbd.getDebt()+user.getAmount());
		}
	
		//Refresh BD
		JPA.em().flush();	
    }
	

}