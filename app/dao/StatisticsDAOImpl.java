package dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.jongo.Aggregate;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.Id;

import models.Consumer;
import models.Person;
import models.Statistics;
import play.libs.Json;
import uk.co.panaxiom.playjongo.PlayJongo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Groups the operations on the Statistics table
 * @author Geoffrey
 *
 */
@Singleton
public final class StatisticsDAOImpl implements StatisticsDAO{

	private static MongoCollection consumers = PlayJongo.getCollection("Consumer");
	private static MongoCollection statistics = PlayJongo.getCollection("Statistics");
	
	private static final String MONTH[] = {"JAN", "FEV", "MAR", "AVR","MAI", "JUN", "JUL", "AOU","SEP", "OCT", "NOV", "DEC"};

	/**
	 * Add a statistic on the Statistics table
	 * @param idPerson : person who swore
	 */
	public void add(String idPerson, String email) {
		//Get the person and the user
		Consumer user = consumers.findOne("{_id: #}", email).as(Consumer.class);

		//Add the statistic if the person is linked with the actual user
		for(Person p : user.getPeople()) {
			if(p.getIdPerson().equals(idPerson)) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE,-30);
				Statistics stats = new Statistics(new Date(cal.getTimeInMillis()),p);
				statistics.insert(stats);
				break;
			}
		}
	}


	/**
	 * List the data to display statistics in the view
	 * @param emailUser : user id
	 * @param ids : members id
	 * @param nb : number of data
	 * @param granularity : 1 = Week, 2 = Month
	 */
	public ObjectNode list(String emailUser, ArrayList<String> ids, int nb, int granularity) {
		//Get the members (ids contains the id members)
		Consumer user = consumers.findOne("{_id: #}", emailUser).as(Consumer.class);
		List<Person> l = user.getPeople();
		
		ArrayList<Person> members = new ArrayList<Person>();
		for (Person p : l) {
			if(ids.contains(p.getIdPerson()) ) {
				members.add(p);
			}
		}

		//Get the members statistics
		Iterable<Statistics> statsTmp = statistics.find().as(Statistics.class);
		ArrayList<Statistics> stats = new ArrayList<Statistics>();
		for(Statistics s :statsTmp){
			if(ids.contains(s.getPerson().getIdPerson()))
				stats.add(s);
		}
		
		//**************************************************************************************//
		/*
		Calendar cal = Calendar.getInstance();
		
		Date d = new Date(cal.getTimeInMillis());
		cal.add(Calendar.DATE,-7);
		Date d2 = new Date(cal.getTimeInMillis());
		
		DBObject key = new BasicDBObject("person._id",1);
		DBObject initial = new BasicDBObject("total",0);//.append("date", "");
		DBObject cond = new BasicDBObject("vdate",new BasicDBObject("$lt",d).append("$gt", d2));
		String reduce = "function( curr, result ) {result.total += 1; }";
		//String reduce = "function( doc, result ) {result.total += 1; var date = new Date(doc.vdate); result.vdate = date.getMonth(); }";
		DBObject result = statistics.group(key, cond, initial,reduce);

		
		System.out.println(cond);
		System.out.println(result);
		*/
		
		/*class ResultObject{
			  @Id
			  String date;
			  int click;
			  public ResultObject() {}
			  public ResultObject(String date) {
				  this.date = date;
			  }
			  public ResultObject(String date, int clk) {
				  this.date = date;
				  this.click = clk;
			  }
			  public String getDate() {
				  return this.date;
			  }
			  public void setDate(String date) {
				  this.date = date;
			  }
			  public int getClick() {
				  return this.click;
			  }
			  public void setClick(int clk) {
				  this.click = clk;
			  }
			}*/
		
		/*List<models.ResultAgreg> a = statistics.aggregate("{ $project: { _id: '$person.idPerson' ,vdate :  { $dayOfMonth: '$date' } ,click: 1 } }")
				.and("{ $group: { _id: '$person.idPerson' , vdate : { $dayOfMonth: '$date' }  } }")
				.as(models.ResultAgreg.class);
		*/
		
		 class ResultAgreg {

		    BasicDBObject personId;
		    
		    int click;

		}
		
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-7);
		Date d = new Date(cal.getTimeInMillis());
		
		List<ResultAgreg> a = statistics.aggregate("{ $match: { person.idPerson : {$in : #}, date : {$gt : # }}}",ids,d)	
				.and("{ $group: { _id: { perid : '$person.firstname' , vdate : { $#: '$date' }} ,click: { $sum: 1 }}}","month")
				.as(ResultAgreg.class);
		for(ResultAgreg ra : a) {
			System.out.println(ra.personId);
			System.out.println(ra.click);
		}

		
		//**************************************************************************************//
		
		
		//Sort by date
		Collections.sort(stats, new Comparator<Statistics>() {
			public int compare(Statistics s1, Statistics s2) {
				return -s1.getDate().compareTo(s2.getDate());
			}
		});

		if(granularity == 1) { //Weeks
			return result(members,stats,nb,Calendar.WEEK_OF_YEAR,Calendar.DATE,7);
		}		
		else { //(granularity == 2)  Months
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

		//Get the last possible date (actual - nb*nbCalendar)
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
		int year = calRef.get(Calendar.YEAR);
		if(calendar == Calendar.MONTH) {
			valRef = calRef.get(calendarRef);;
		}
		else {
			valRef = calRef.get(calendarRef);
		}
		
		//Create the list of data statistics for the view (one number per month)
		for(Person m : members) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			while(!end && list.size() < nb) {
				for(int i = 0; i<stats.size(); i++) {
					calTmp.setTime(stats.get(i).getDate());
					if(stats.get(i).getPerson().getIdPerson().equals(m.getIdPerson())) {
						//Incrementing the counter if the statistic date == reference date
						if(calTmp.get(calendarRef) == valRef && calTmp.get(Calendar.YEAR) == year) {
							cpt ++;
						}
						//Stop the while loop (end = true) if the current date is before the last possible date
						else if(calTmp.before(calFin)) {
							end = true;
						}
						//Stop the foreach loop if the statistic date if before the current reference date
						else if(calTmp.get(calendarRef) < valRef && calTmp.get(Calendar.YEAR) == year 
								||  calTmp.get(Calendar.YEAR) < year)
							break;
					}
					//Stop the foreach loop if the statistic date if before the current reference date
					else if(calTmp.get(calendarRef) < valRef && calTmp.get(Calendar.YEAR) == year 
							||  calTmp.get(Calendar.YEAR) < year)
						break;
					//Incrementing the cptmember if the member statistic != the current member
					else {
						cptmember++;
					}
				}	
				//Update index, calRef and add the cpt value in the list
				index=index+cpt+cptmember;
				calRef.add(calendar, -nbCalendar);
				valRef = calRef.get(calendarRef);
				year = calRef.get(Calendar.YEAR);
				list.add(cpt);
				//Reset the counters
				cpt = 0;
				cptmember = 0;
			}
			//Add zeros if necessary to complete the list up to nb elements
			while(list.size() < nb) {
				list.add(0);
			}
			//Reverse the list and put in the Json result (idPerson : list)
			Collections.reverse(list);
			result.put("p"+m.getIdPerson().toString(),Json.toJson(list));
			//Reset the counters and the references values 
			index = 0;
			cptmember = 0;
			calRef.setTime(date);
			valRef = calRef.get(calendarRef);
			year = calRef.get(Calendar.YEAR);
			end = false;
		}
		//Create the ticks list (for the display in the view)
		String ticks[] = new String[nb];
		if(calendar == Calendar.MONTH) { //Granularity MONTH
			for(int i=nb-1; i>=0; i--){
				ticks[i] = MONTH[calRef.get(Calendar.MONTH)];
				calRef.add(calendar, -nbCalendar);
			}
		}
		else { //Granularity WEEK
			for(int i=nb-1; i>=0; i--){
				ticks[i] = "S"+String.valueOf((calRef.get(Calendar.WEEK_OF_YEAR)));
				calRef.add(calendar, -nbCalendar);
			}
		}
		//Put the ticks list in the Json result and return result
		result.put("ticks", Json.toJson(ticks));
		return result;
	}
	
	
}


