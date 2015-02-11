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
	
	private static final String MONTH[] = {"JAN", "FEV", "MAR", "AVR","MAI", "JUN", "JUL", "AOU","SEP", "OCT", "NOV", "DEC"};

	
    /**
     * Add a statistic on the Statistics table
     * @param idPerson : person who swore
     */
	public void add(Long idPerson) {
		Calendar cal = Calendar.getInstance();
		Query query = JPA.em().createQuery(QUERY_PERSON + idPerson);
		Person person = (Person) query.getSingleResult();
		Statistics stats = new Statistics(new Date(cal.getTimeInMillis()),person);
		JPA.em().persist(stats);
	}

	
	/**
	 * List the data to display statistics in the view
     * @param emailUser : user id
     * @param ids : members id
     * @param nb : number of data
     * @param granularity : 1 = Week, 2 = Month
	 */
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

		if(granularity == 1) {
			return result(members,stats,nb,Calendar.WEEK_OF_YEAR,Calendar.DATE,7);
		}		
		else { //(granularity == 2) 
			return result(members,stats,nb,Calendar.MONTH,Calendar.MONTH,1);
		}
	}

	
	/**
	 * List the data statistics with the Month granularity
     * @param members : list of members concerned
     * @param stats : statistics that must extract data
     * @param nb : number of data
	 */
	private ObjectNode result(ArrayList<Person> members, ArrayList<Statistics> stats, int nb, int calendarRef, int calendar, int nbCalendar) {
		
		//Get the actual date
		Date date = new Date();
		Calendar calRef = Calendar.getInstance();
		calRef.setTime(date);

		//Create the Json
		ObjectNode result = Json.newObject();

		//Get the last possible date (actual - nb*Month)
		Calendar calFin = Calendar.getInstance();
		calFin.setTime(date);
		
		calFin.add(calendar, -nb*nbCalendar);

		//Variables loop initialization
		Calendar calTmp = Calendar.getInstance();
		int cpt = 0;
		int cptmember = 0;
		boolean end = false;
		int index = 0;
		int valRef;
		if(calendar == Calendar.MONTH) {
			valRef = calRef.get(calendarRef);;
		}
		else {
			valRef = calRef.get(calendarRef);
		}
		int year = calRef.get(Calendar.YEAR);

		//Create the list of data statistics for the view (one number per month)
		for(Person m : members) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			while(!end && list.size() < nb) {
				for(int i = 0; i<stats.size(); i++) {
					calTmp.setTime(stats.get(i).getDate());
					if(stats.get(i).getPerson().getIdPerson() == m.getIdPerson()) {
						if(calTmp.get(calendarRef) == valRef && calTmp.get(Calendar.YEAR) == year) {
							cpt ++;
						}
						else if(calTmp.before(calFin)) {
							end = true;
						}
						else if(calTmp.get(calendarRef) < valRef && calTmp.get(Calendar.YEAR) == year 
								||  calTmp.get(Calendar.YEAR) < year)
							break;
					}
					else if(calTmp.get(calendarRef) < valRef && calTmp.get(Calendar.YEAR) == year 
							||  calTmp.get(Calendar.YEAR) < year)
						break;
					else {
						cptmember++;
					}
				}	
				index=index+cpt+cptmember;
				calRef.add(calendar, -nbCalendar);
				valRef = calRef.get(calendarRef);
				year = calRef.get(Calendar.YEAR);
				list.add(cpt);
				cpt = 0;
				cptmember = 0;
			}
			while(list.size() < nb) {
				list.add(0);
			}
			Collections.reverse(list);
			result.put("p"+m.getIdPerson().toString(),Json.toJson(list));
			index = 0;
			cptmember = 0;
			calRef.setTime(date);
			valRef = calRef.get(calendarRef);
			year = calRef.get(Calendar.YEAR);
			end = false;
		}
		String ticks[] = new String[nb];
		if(calendar == Calendar.MONTH) {
			for(int i=nb-1; i>=0; i--){
				ticks[i] = MONTH[calRef.get(Calendar.MONTH)];
				calRef.add(calendar, -nbCalendar);
			}
		}
		else {
			for(int i=nb-1; i>=0; i--){
				ticks[i] = "S"+String.valueOf((calRef.get(Calendar.WEEK_OF_YEAR)));
				calRef.add(calendar, -nbCalendar);
			}
		}
		result.put("ticks", Json.toJson(ticks));
		return result;
	}
}
