package services;

import java.util.List;
import javax.persistence.TypedQuery;
import play.db.jpa.JPA;

import models.Person;

/**
 * Liste les personnes liées à l'utilisateur connecté
 * @author Geoffrey
 *
 */
public class ListPeople {
	
	private ListPeople(){
	}
	
	/**
	 * Liste les personnes liées à l'utilisateur dont l'email est passé en paramètre
	 * @param emailUser email de l'utilisateur
	 * @return List<Person> la liste des personnes
	 */
	public  static List<Person> listPeople(String emailUser){
		
		@SuppressWarnings("unchecked")
		TypedQuery<Person> req = (TypedQuery<Person>) JPA.em().createNativeQuery(
				"SELECT Person.idPerson, Person.name, Person.firstname, Person.debt, Person.picture "
				+ "FROM Person "
				+ "INNER JOIN u_p ON u_p.idPerson = Person.idPerson "
				+ "WHERE u_p.idUser = :email",
				Person.class).setParameter("email", emailUser);
		
		return (List<Person>) req.getResultList();
	}

}

