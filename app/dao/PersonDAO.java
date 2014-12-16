package dao;

import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.*;

/**
 * Regroupe les opérations sur la table Person
 *
 */
public final class PersonDAO{

    private PersonDAO(){
    }

    /**
     * Ajoute une personne dans la table personne 
     * Lie l'utilisateur et la parsonne
     * @param People : la personne a ajouter
     * @param String : l'identifiant de l'utilisateur qui ajoute la personne a sa liste
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
	 * Supprime une personne dans la table personne
	 * @param long : l'identifiant de la personne a supprimer
	 * @param String : l'identifiant de l'utilisateur qui supprime la personne
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
     * Liste les personnes liées à l'utilisateur dont l'email est passé en paramètre
     * @param emailUser email de l'utilisateur
     * @return List<Person> la liste des personnes
     */
    public  static List<Person> listByUser(String emailUser){
        Consumer u = JPA.em().find(Consumer.class, emailUser);
        return u.getPeople();
    }
    
    /**
	 * Acquite la dette d'une personne
	 * @param long : l'identifiant de la personne a acquitter
	 * @param String : l'identifiant de l'utilisateur qui acquitte la personne
	 */
	public static void debt(long id,String email){
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

}