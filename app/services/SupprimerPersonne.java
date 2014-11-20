package services;

import java.util.List;

import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;
import models.*;

public class SupprimerPersonne{

	/**
	 * Supprime une personne dans la table personne
	 * @param String : nom de la personne
	 * @param String : prenom de la personne
	 * @param String : l'identifiant de l'utilisateur qui supprime la personne
	 */
	public static void supprimerPersonne(long id){
		//recuperation de la personne
		Query query = JPA.em().createQuery("Select p from Personne p where p.id_personne =" + id);

		Personne pbd = (Personne) query.getSingleResult();
		
		
		//suppression des clé dans U_P
		for (Utilisateur u: pbd.getUtilisateurs()){	
			u.getPersonnes().remove(pbd);
		}

		
		//referesh BD
		JPA.em().flush();	
		
		
		//suppression de la personne
		JPA.em().remove(pbd);
	}
	
}