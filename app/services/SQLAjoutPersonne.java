package services;

import play.db.jpa.JPA;
import models.*;

public class SQLAjoutPersonne{

	public static void AjoutPersonne(Personne p,String id){
		//enregistrement de la personne
		JPA.em().persist(p);
		//recuperation de l'utilisateur
		Utilisateur user = JPA.em().find(Utilisateur.class,id); 
		user.setPersonnes(p);
		//liaison entre les deux
		JPA.em().merge(user);
	}
}