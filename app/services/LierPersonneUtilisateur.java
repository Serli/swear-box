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

		//recuperation de l'utilisateur
		Utilisateur user = JPA.em().find(Utilisateur.class,id); 
		
		//si il ne sont pas deja lier, on le fait
		if(user.getPersonnes().contains(p)==false){
			user.setPersonnes(p);
			p.setUtilisateurs(user);
		}
	}
}
