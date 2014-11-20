package services;

import play.db.jpa.JPA;
import models.*;

/**
 * Permet d'ajouter un utilisateur
 * @author Geoffrey
 *
 */
public class LinkUserPerson {
	
	private LinkUserPerson(){
	}

	/**
	 * Ajoute l'utilisateur s'il n'existe pas
	 * @param email email de l'utilisateur ( clé primaire de la table Utilisateur )
	 */
	public static void linkUserPerson(long idPerson,String idUser) {
		boolean test= true;

		//recuperation de l'utilisateur
		Consumer user = JPA.em().find(Consumer.class,idUser); 
		
		//regarde si il sont deja lier
		for (Person p :user.getPeople()){
			if(p.getIdPerson()==idPerson){
				test = false;
			}
		}
		
		//lie les deux
		if(test){
			Person pbd=JPA.em().find(Person.class,idPerson);
			user.setPerson(pbd);
			pbd.setUser(user);
		}
	}
}
