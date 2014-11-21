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
	 * @param String : nom de la personne
	 * @param String : prenom de la personne
	 * @param String : l'identifiant de l'utilisateur qui supprime la personne
	 */
	public static void deletePerson(long id){
		//recuperation de la personne
		Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);

		Person pbd = (Person) query.getSingleResult();
		
		//suppression des clé dans U_P
		for (Consumer u: pbd.getUsers()){	
			u.getPeople().remove(pbd);
		}
	
		//referesh BD
		JPA.em().flush();	
		
		//suppression de la personne
		JPA.em().remove(pbd);
	}
	
}