package unit;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.inMemoryDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Consumer;
import models.Person;
import models.Statistics;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.db.jpa.JPA;
import play.modules.mongodb.jackson.MongoDB;
import play.test.FakeApplication;
import play.test.Helpers;
import dao.ConsumerDAO;
import dao.ConsumerDAOImpl;
import dao.PersonDAO;
import dao.PersonDAOImpl;
import dao.StatisticsDAO;
import dao.StatisticsDAOImpl;

/**
 * Tests for Person functions
 *
 */
public class StatisticsTest{

	private PersonDAO personDAO = new PersonDAOImpl();
    private ConsumerDAO consumerDAO = new ConsumerDAOImpl();
    private StatisticsDAO statisticsDAO = new StatisticsDAOImpl();
    private static FakeApplication app;
    private static JacksonDBCollection<Consumer, String> consumers ;
    private static JacksonDBCollection<Person, String> people;
	private static JacksonDBCollection<Statistics, String> statistics;
	
    @BeforeClass
    public static void startApp() {
    	Map<String, String> config = new HashMap<String, String>();
        config.put("ehcacheplugin", "disabled");
        config.put("mongodbJacksonMapperCloseOnStop", "disabled");
        app = Helpers.fakeApplication(config);
        Helpers.start(app);
        consumers = MongoDB.getCollection("Consumer", Consumer.class, String.class);
    	people = MongoDB.getCollection("Person", Person.class, String.class);
    	statistics = MongoDB.getCollection("Statistics", Statistics.class, String.class);
        
    }

    
    /**
     * Test AddStatistic function for a member
     */
    @Test
    public void add() {
    	Person p =new Person(ObjectId.get().toString(),"pName", "pFirstname",0,"https://url");
        Consumer u= new Consumer("email@email",100);
        consumers.insert(u);
        personDAO.add(p,u.getEmail());
        
        statisticsDAO.add(p.getIdPerson(), u.getEmail());
        statisticsDAO.add(p.getIdPerson(), u.getEmail());
        
        List<Statistics> lp= statistics.find(DBQuery.in("person.$id", p.getIdPerson())).toArray();
        assertThat(lp.size()==2);
    }
    @AfterClass
    public static void stopApp() {
        Helpers.stop(app);
    }
}