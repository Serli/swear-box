package dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Consumer;
import models.Person;
import models.ResultAggregation;
import models.Statistics;

import org.jongo.MongoCollection;

import play.libs.Json;
import uk.co.panaxiom.playjongo.PlayJongo;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;

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
	private static final String MONTH2[] = {"Jan", "Feb", "Mar", "Apr","May", "Jun", "Jul", "Aug","Sep", "Oct", "Nov", "Dec"};

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
	public JsonNode list(String emailUser, ArrayList<String> ids, int nb, int granularity) {

		/*** declaration ***/
		Calendar calFin = Calendar.getInstance();
		String granu = "";
		ArrayList<BasicDBObject> li = new ArrayList<>();
		ArrayList<BasicDBObject> res = new ArrayList<>();
		
		/*** instanciation ***/
		if(granularity == 1) {
			calFin.add(Calendar.DATE, -nb*7);
			granu = "week";
			for(int i=1; i<=52; i++){
				li.add(new BasicDBObject().append("Date", "S"+i));
			}
		}
		if(granularity == 2) {
			calFin.add(Calendar.MONTH, -nb);
			granu = "month";
			li.add(new BasicDBObject().append("Date", "OSEF"));
			for(int i=1; i<=12; i++){
				li.add(new BasicDBObject().append("Date", MONTH[i-1]));
			}
		}
		
		/*** requete ***/
		Date d = new Date(calFin.getTimeInMillis());
		List<ResultAggregation> a = statistics.aggregate("{ $match: { person.idPerson : {$in : #}, date : {$gt : # }}}",ids,d)	
				.and("{ $group: { _id: { perid : '$person.firstname' , vdate : { $#: '$date' }} ,click: { $sum: 1 }}}",granu)
				.as(ResultAggregation.class);
		
		
		/*** mise en forme JSON ***/
		for(ResultAggregation ra : a) {
			li.get(ra.getPersonId().getInt("vdate")).append(ra.getPersonId().getString("perid"), ra.getClick()+"");
		}
		
		if(granularity == 2) {
			li.remove(0);
			int debut = calFin.get(Calendar.MONTH);
			for(int u =0 ;u<nb;u++){
				debut++;
				res.add(li.get(debut % 12));
			}
		}
		if(granularity == 1) {
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
		List<BasicDBObject> a = statistics.aggregate("{ $group: { _id: { month : { $month: '$date' }, year : { $year: '$date' } }, nb: { $sum: 1 } }}")
				.as(BasicDBObject.class);
		ArrayList<BasicDBObject> res = new ArrayList<>();
		for(BasicDBObject bo : a) {	
			JsonNode j = Json.toJson(bo);
			res.add(new BasicDBObject().append("date", MONTH2[j.findValue("month").asInt()-1]+" "
					+j.findValue("year").toString()).append("nb", bo.getString("nb")));
		}
		return Json.toJson(res);
	}
	
	public JsonNode someStats() {
		ArrayList<BasicDBObject> res = new ArrayList<>();
		Long nbConsumers = consumers.count();
		res.add(new BasicDBObject().append("nbConsumers", nbConsumers));
		Long nbBlacklisted = consumers.count("{blackListed: true}");
		res.add(new BasicDBObject().append("nbBlacklisted", nbBlacklisted));
		return Json.toJson(res);
	}
}


