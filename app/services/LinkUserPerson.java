package services;

import play.db.jpa.JPA;
import models.*;

/**
 * Permet d'ajouter un utilisateur
 * @author Geoffrey
 *
 */
public class LinkUserPerson {

	/**
	 * Ajoute l'utilisateur s'il n'existe pas
	 * @param email email de l'utilisateur ( clé primaire de la table Utilisateur )
	 */
	public static void linkUserPerson(Person p,String id) {

		//recuperation de l'utilisateur
		Consumer user = JPA.em().find(Consumer.class,id); 
		
		//si il ne sont pas deja lier, on le fait
		if(user.getPeople().contains(p)==false){
			user.setPerson(p);
			p.setUser(user);
		}
	}
}
