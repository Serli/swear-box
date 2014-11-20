package services;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import models.*;

/**
 * Permet d'ajouter un utilisateur
 * @author Geoffrey
 *
 */
public class LierPersonneUtilisateur {

	/**
	 * Ajoute l'utilisateur s'il n'existe pas
	 * @param email email de l'utilisateur ( clé primaire de la table Utilisateur )
	 */
	public static void lierPersonneUtilisateur(Person p,String id) {

		//recuperation de l'utilisateur
		User user = JPA.em().find(User.class,id); 
		
		//si il ne sont pas deja lier, on le fait
		if(user.getPeople().contains(p)==false){
			user.setPerson(p);
			p.setUser(user);
		}
	}
}
