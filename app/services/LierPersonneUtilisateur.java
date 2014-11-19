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
	public static void lierPersonneUtilisateur(Personne p,String id) {
		//enregistrement de la personne
		//recuperation de l'utilisateur
		Utilisateur user = JPA.em().find(Utilisateur.class,id); 
		user.setPersonnes(p);
		p.setUtilisateurs(user);
	}
}
