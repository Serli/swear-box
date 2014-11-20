package services;

import java.util.List;
import javax.persistence.TypedQuery;
import play.db.jpa.JPA;

import models.Person;

public class ListPeople {
	
	private ListPeople(){
	}
	

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

