package dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import models.Consumer;
import models.Person;
import models.Statistics;

import org.jongo.MongoCollection;

import play.libs.Json;
import uk.co.panaxiom.playjongo.PlayJongo;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;

/**
 * Groups the operations on the Statistics collection
 *
 */
@Singleton
public final class StatisticsDAOImpl implements StatisticsDAO{

	private static MongoCollection consumers = PlayJongo.getCollection("Consumer");
	private static MongoCollection statistics = PlayJongo.getCollection("Statistics");
	
	private static final String[] MONTH = {"JAN", "FEV", "MAR", "AVR","MAI", "JUN", "JUL", "AOU","SEP", "OCT", "NOV", "DEC"};
	private static final String[] MONTH2 = {"Jan", "Feb", "Mar", "Apr","May", "Jun", "Jul", "Aug","Sep", "Oct", "Nov", "Dec"};
	private static final int GRANULARITY_WEEK = 1;
	private static final int GRANULARITY_MONTH = 2;
	

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
	public JsonNode list(List<String> ids, int nb, int granularity) {

		/*** declaration ***/
		Calendar calFin = Calendar.getInstance();
		String granu = "";
		List<BasicDBObject> li = new ArrayList<>();
		List<BasicDBObject> res = new ArrayList<>();
		
		/*** instanciation ***/
		if(granularity == GRANULARITY_WEEK) {
			calFin.add(Calendar.DATE, -nb*7);
			granu = "week";
			for(int i=1; i<=52; i++){
				li.add(new BasicDBObject().append("Date", "S"+i));
			}
		}
		if(granularity == GRANULARITY_MONTH) {
			calFin.add(Calendar.MONTH, -nb);
			granu = "month";
			li.add(new BasicDBObject().append("Date", "OSEF"));
			for(int i=1; i<=12; i++){
				li.add(new BasicDBObject().append("Date", MONTH[i-1]));
			}
		}
		
		/*** requete ***/
		Date d = new Date(calFin.getTimeInMillis());
		List<BasicDBObject> a = statistics.aggregate("{ $match: { person.idPerson : {$in : #}, date : {$gt : # }}}",ids,d)	
				.and("{ $group: { _id: { perid : '$person.firstname' , vdate : { $#: '$date' }} ,click: { $sum: 1 }}}",granu)
				.as(BasicDBObject.class);
		
		
		/*** mise en forme JSON ***/
		for(BasicDBObject ra : a) {
			JsonNode js = Json.toJson(ra);
			li.get(js.findValue("vdate").asInt()).append(js.findValue("perid").asText(),js.findValue("click").asInt()+"");
		}
		
		if(granularity == GRANULARITY_MONTH) {
			li.remove(0);
			int debut = calFin.get(Calendar.MONTH);
			for(int u =0 ;u<nb;u++){
				debut++;
				res.add(li.get(debut % 12));
			}
		}
		if(granularity == GRANULARITY_WEEK) {
			int debut = calFin.get(Calendar.WEEK_OF_YEAR);
			for(int u =0 ;u<nb;u++){
				res.add(li.get(debut % 52));
				debut++;
			}
		}
		return Json.toJson(res);
	}


	/**
	 * List all the statistics
	 */
	@Override
	public JsonNode list() {
		List<BasicDBObject> a = statistics.aggregate("{ $group: { _id: { day : { $dayOfMonth: '$date' }, month : { $month: '$date' }, year : { $year: '$date' } }, nb: { $sum: 1 } }}")
				.as(BasicDBObject.class);
		List<BasicDBObject> res = new ArrayList<>();

        Collections.sort(a, new Comparator<BasicDBObject>() {
            public int compare(BasicDBObject o1, BasicDBObject o2) {
            	JsonNode j = Json.toJson(o1);
            	JsonNode j2 = Json.toJson(o2);
            	Calendar cal = Calendar.getInstance();
            	cal.set(j.findValue("year").asInt(), j.findValue("month").asInt(), j.findValue("day").asInt());
           
            	Calendar cal2 = Calendar.getInstance();
            	cal2.set(j2.findValue("year").asInt(), j2.findValue("month").asInt(), j2.findValue("day").asInt());
                return cal.compareTo(cal2);
            }
        });
		for(BasicDBObject bo : a) {	
			JsonNode j = Json.toJson(bo);
			res.add(new BasicDBObject().append("date",j.findValue("day").toString()+" "+ MONTH2[j.findValue("month").asInt()-1]+" "
					+j.findValue("year").toString()).append("nb", bo.getString("nb")));
		}
		return Json.toJson(res);
	}
	
	
	@Override
	public JsonNode someStats() {
		List<BasicDBObject> res = new ArrayList<>();
		Long nbConsumers = consumers.count();
		res.add(new BasicDBObject("nbConsumers", nbConsumers));
		Long nbBlacklisted = consumers.count("{blackListed: true}");
		res.add(new BasicDBObject("nbBlacklisted", nbBlacklisted));
		Iterable<Consumer> a = consumers.find().as(Consumer.class);
		int size = 0;
		for(Consumer c : a) {
			size+= c.getPeople().size();
		}
		res.add(new BasicDBObject("nbMembers", size));
		Long nbStats = statistics.count();
		res.add(new BasicDBObject("nbStats", nbStats));
		return Json.toJson(res);
	}
}


