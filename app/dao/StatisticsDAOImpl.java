package dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.Person;
import models.Statistics;

import com.google.inject.Singleton;

/**
 * Groups the operations on the Statistics table
 * @author Geoffrey
 *
 */
@Singleton
public final class StatisticsDAOImpl implements StatisticsDAO{
	
	private static final String QUERY_PERSON = "Select p from Person p where p.idPerson =";

	public void add(Long idPerson) {
		Date date = new Date();
		Query query = JPA.em().createQuery(QUERY_PERSON + idPerson);
        Person person = (Person) query.getSingleResult();
		Statistics stats = new Statistics(date,person);
		JPA.em().persist(stats);
	}
    
    
}
