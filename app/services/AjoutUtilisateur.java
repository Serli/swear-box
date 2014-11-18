package services;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import models.Utilisateur;

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
		Utilisateur u = JPA.em().find(Utilisateur.class, email);
		if (u == null) {
			u = new Utilisateur(email);
			JPA.em().persist(u);
		}
	}
}
