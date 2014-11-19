package services;

import play.db.jpa.JPA;
import models.*;

public class AjoutPersonne{

	/**
	 * Ajoute une personne dans la table personne 
	 * Lie l'utilisateur et la parsonne
	 * @param Personne : la personne a ajouter
	 * @param String : l'identifiant de l'utilisateur qui ajoute la personne a sa liste
	 */
	public static void ajoutPersonne(Personne p,String id){
		//enregistrement de la personne
		JPA.em().persist(p);
		//recuperation de l'utilisateur
		Utilisateur user = JPA.em().find(Utilisateur.class,id); 
		user.setPersonnes(p);
		p.setUtilisateurs(user);
		
		//liaison entre les deux
		JPA.em().flush();
	}
	
}