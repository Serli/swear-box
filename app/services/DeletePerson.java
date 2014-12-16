package services;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.*;

/**
 * Supprime une personne
 *
 */
public class DeletePerson{
	
	private DeletePerson(){
	}

	/**
	 * Supprime une personne dans la table personne
	 * @param long : l'identifiant de la personne a supprimer
	 * @param String : l'identifiant de l'utilisateur qui supprime la personne
	 */
	public static void deletePerson(long id,String email){
		//recuperation de la personne
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);
		Person pbd = (Person) query.getSingleResult();
		Consumer user = JPA.em().find(Consumer.class,id);
		
		//suppression des clé dans U_P
		for (Consumer u: pbd.getUsers()){
			if (user.getPeople().contains(u))
				u.getPeople().remove(pbd);
		}
	
		//referesh BD
		JPA.em().flush();	
		
		//suppression de la personne
		JPA.em().remove(pbd);
	}
	
}