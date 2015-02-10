package dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import models.Consumer;
import models.Person;
import models.Statistics;
import play.db.jpa.JPA;
import play.libs.Json;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
		Calendar cal = Calendar.getInstance();
		Query query = JPA.em().createQuery(QUERY_PERSON + idPerson);
		Person person = (Person) query.getSingleResult();
		Statistics stats = new Statistics(new Date(cal.getTimeInMillis()),person);
		JPA.em().persist(stats);
	}

	public ObjectNode list(String emailUser, ArrayList<Long> ids, int nb, int granularity) {
		//Get the members (ids contains the id member)
		Consumer user = JPA.em().find(Consumer.class,emailUser);
		ArrayList<Person> members = new ArrayList<Person>();
		for (Person p : user.getPeople()) {
			if(ids.contains(p.getIdPerson()) ) {
				members.add(p);
			}
		}
		
		//Get the members statistics
		List<Statistics> statistics = JPA.em().createQuery("SELECT s "
				+ "FROM Statistics s "
				+ "WHERE s.person IN (:members)",Statistics.class).setParameter("members", members).getResultList();

		ArrayList<Statistics> stats = new ArrayList<Statistics>(statistics);
		
		//Sort by date
		Collections.sort(stats, new Comparator<Statistics>() {
			public int compare(Statistics s1, Statistics s2) {
				return -s1.getDate().compareTo(s2.getDate());
			}
		});
		
		//Get the actual date
		Date date = new Date();
		Calendar calRef = Calendar.getInstance();
		calRef.setTime(date);

		//Create the Json
		ObjectNode result = Json.newObject();

		//Granularity : Month
		if(granularity == 2) {
			//Get actuals month and year
			int monthRef = calRef.get(Calendar.MONTH);
			int yearRef = calRef.get(Calendar.YEAR);

			//Get the last possible date (actual - nb*Month)
			Calendar calFin = Calendar.getInstance();
			calFin.setTime(date);
			calFin.add(Calendar.MONTH, -nb);

			//Variables loop initialization
			Calendar calTmp = Calendar.getInstance();
			int cpt = 0;
			int cptmember = 0;
			boolean end = false;
			int index = 0;
			int month = monthRef;
			int year = yearRef;

			//Create the list of data statistics for the view (one number per month)
			for(Person m : members) {
				ArrayList<Integer> list = new ArrayList<Integer>();
				month = monthRef;
				year = yearRef;
				while(!end && list.size() < nb) {
					for(int i = 0; i<stats.size(); i++) {
						if(stats.get(i).getPerson().getIdPerson() == m.getIdPerson()) {
							calTmp.setTime(stats.get(i).getDate());
							if(calTmp.get(Calendar.MONTH) == month && calTmp.get(Calendar.YEAR) == year) {
								cpt ++;
							}
							else if(calTmp.before(calFin)) {
								end = true;
							}
						}
						else {
							cptmember++;
						}
					}
					index=index+cpt+cptmember-1;
					calRef.add(Calendar.MONTH, -1);
					month = calRef.get(Calendar.MONTH);
					year = calRef.get(Calendar.YEAR);
					System.out.println(month+" "+calRef.get(Calendar.YEAR));
					list.add(cpt);
					cpt = 0;		
				}
				while(list.size() < nb) {
					list.add(0);
				}
				Collections.reverse(list);
				result.put(m.getIdPerson().toString(),Json.toJson(list));
				index = 0;
				cptmember = 0;
				calRef.setTime(date);
			}
		}
		return result;
	}
}
