package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import models.Consumer;
import models.Person;
import models.Statistics;
import play.db.jpa.JPA;

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
	
	public List<Statistics> listByUser(String emailUser) {
		Consumer user = JPA.em().find(Consumer.class,emailUser);
		List<Statistics> stats = JPA.em().createQuery("SELECT s "
				+ "FROM Statistics s "
				+ "WHERE s.person IN (:members)",Statistics.class).setParameter("members", user.getPeople()).getResultList();
		return stats;
	}
    
    
}
