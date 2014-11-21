package services;

import play.db.jpa.JPA;
import models.*;

/**
 * Lie une personne à un utilisateur
 *
 */
public class LinkUserPerson {
	
	private LinkUserPerson(){
	}

	/**
	 * Lie la personne à l'utilisateur
	 * @param idPerson id de la personne à lier
	 * @param idUser id de l'utilisateur à lier
	 */
	public static void linkUserPerson(long idPerson,String idUser) {
		boolean test= true;

		//recuperation de l'utilisateur
		Consumer user = JPA.em().find(Consumer.class,idUser); 
		
		//regarde si il sont deja lier
		for (Person p :user.getPeople()){
			if(p.getIdPerson()==idPerson){
				test = false;
			}
		}
		
		//lie les deux
		if(test){
			Person pbd=JPA.em().find(Person.class,idPerson);
			user.setPerson(pbd);
			pbd.setUser(user);
		}
	}
}
