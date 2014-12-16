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
     * link the user with it
     * @param Person : person to add
     * @param String : user id
     */
    public static void add(Person p,String id){
        //enregistrement de la personne
        JPA.em().persist(p);

        //recuperation de l'utilisateur
        Consumer user = JPA.em().find(Consumer.class,id); 
        user.setPerson(p);
        p.setUser(user);

        //liaison entre les deux
        JPA.em().flush();
    }

    /**
     * Delete a person on the Person table
     * @param Person : person to add
     * @param String : user id
     */
	public static void delete(long id,String email){
		//recuperation de la personne
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,email);
		
		//suppression des clé dans U_P
		if (user.getPeople().contains(pbd)){
			for (Consumer u: pbd.getUsers()){	
				u.getPeople().remove(pbd);
			}
		}
	
		//referesh BD
		JPA.em().flush();	
		
		//suppression de la personne
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
     * discharge a person on the Person table
     * @param long : person id
     * @param String : user id
     */
	public static void discharge(long id,String email){
		//recuperation de la personne
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,email);
		
		//suppression des clé dans U_P
		
		//si l'utilisateur a les droits
		if (user.getPeople().contains(pbd)){
			pbd.setDebt(0);
		}
	
		//referesh BD
		JPA.em().flush();	
		
	}
	
	
	/**
     * update a person on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new name
     * @param String : new firstname
     */
	public static void updateNameFirstname(long id,String email,String vName, String vFirstname){
		//recuperation de la personne
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,email);
		
		//suppression des clé dans U_P
		
		//si l'utilisateur a les droits
		if (user.getPeople().contains(pbd)){
			pbd.setName(vName);
			pbd.setFirstname(vFirstname);
		}
	
		//referesh BD
		JPA.em().flush();	
		
	}
	
	/**
     * update a person picture on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new picture path
     */
	public static void updatePicture(long id,String email,String vPicture){
		//recuperation de la personne
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,email);
		
		//suppression des clé dans U_P
		
		//si l'utilisateur a les droits
		if (user.getPeople().contains(pbd)){
			pbd.setAdrImage(vPicture);
		}
	
		//referesh BD
		JPA.em().flush();	
		
	}
	 /**
     * Increase a person debt
     * @param long : person id
     * @param String : user id
     */
    public static void incrementDebt(long id,String email){
		//recuperation de la personne
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,email);
		
		//si l'utilisateur a les droits
		if (user.getPeople().contains(pbd)){
			pbd.setDebt(pbd.getDebt()+user.getAmount());
		}
	
		//referesh BD
		JPA.em().flush();	
    }
	

}