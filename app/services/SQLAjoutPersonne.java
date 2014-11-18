package services;

import play.db.jpa.JPA;
import models.*;

public class SQLAjoutPersonne{

	public static void AjoutPersonne(Personne p){
		JPA.em().persist(p);
	}
}