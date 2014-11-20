package services;

import play.db.jpa.JPA;
import models.Consumer;

/**
 * Permet d'ajouter un utilisateur
 * @author Geoffrey
 *
 */
public class AddUser {
	
	private AddUser(){
	}

	/**
	 * Ajoute l'utilisateur s'il n'existe pas
	 * @param email email de l'utilisateur ( clé primaire de la table Utilisateur )
	 */
	public static void addUser(String email) {
		Consumer u = JPA.em().find(Consumer.class, email);
		if (u == null) {
			u = new Consumer(email);
			JPA.em().persist(u);
		}
	}
}
