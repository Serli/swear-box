package services;

import play.db.jpa.JPA;
import models.*;

public class AddPerson{

	/**
	 * Ajoute une personne dans la table personne 
	 * Lie l'utilisateur et la parsonne
	 * @param People : la personne a ajouter
	 * @param String : l'identifiant de l'utilisateur qui ajoute la personne a sa liste
	 */
	public static void addPerson(Person p,String id){
		//enregistrement de la personne
		JPA.em().persist(p);
		//recuperation de l'utilisateur
		User user = JPA.em().find(User.class,id); 
		user.setPerson(p);
		p.setUser(user);
		
		//liaison entre les deux
		JPA.em().flush();
	}
	
}