package services;

import java.util.List;

import javax.persistence.Query;

import models.Personne;
import play.db.jpa.JPA;

public class ListePersonnes {

public  static List<Personne> listePersonnes(){
		
		Query req = JPA.em().createQuery("SELECT Nom ,Prenom ,Dette ,adr_Image FROM Personne" );
		List<Personne> personnes = req.getResultList();
		return personnes;
	
	}

}

