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
	public static void supprimerPersonne(String nom, String prenom, String id){
		//recuperation de la personne
		TypedQuery<Personne> query =  (TypedQuery<Personne>) JPA.em().createNativeQuery(
		        "Select p.Id_Personne, p.Nom, p.Prenom, p.Dette, p.adr_Image "
		        + "FROM Personne as p INNER JOIN U_P as up "
		        	+ "ON p.id_Personne = up.id_Personne "
		        + "WHERE p.Nom='" + nom + "' "
		        	+ "AND p.Prenom='" + prenom+ "' "
		        	+ "AND up.id_Utilisateur='" + id + "'",
		        Personne.class);

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