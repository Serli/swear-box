package services;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import models.User;

/**
 * Permet d'ajouter un utilisateur
 * @author Geoffrey
 *
 */
public class AjoutUtilisateur {

	/**
	 * Ajoute l'utilisateur s'il n'existe pas
	 * @param email email de l'utilisateur ( clé primaire de la table Utilisateur )
	 */
	public static void ajoutUtilisateur(String email) {
		User u = JPA.em().find(User.class, email);
		if (u == null) {
			u = new User(email);
			JPA.em().persist(u);
		}
	}
}
