package services;

import java.util.List;
import javax.persistence.TypedQuery;
import play.db.jpa.JPA;

import models.Personne;

public class ListePersonnes {

public  static List<Personne> listePersonnes(String emailUser){
		
		@SuppressWarnings("unchecked")
		TypedQuery<Personne> req = (TypedQuery<Personne>) JPA.em().createNativeQuery(
				"SELECT Personne.id_personne, Personne.nom, Personne.prenom, Personne.dette, Personne.adr_image "
				+ "FROM Personne "
				+ "INNER JOIN u_p ON u_p.id_personne = Personne.id_personne "
				+ "WHERE u_p.id_utilisateur = :email",
				Personne.class).setParameter("email", emailUser);
		
		List<Personne> personnes = (List<Personne>) req.getResultList();
		return personnes;
	
	}

}

