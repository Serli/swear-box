package unit;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Consumer;
import models.Person;
import models.Statistics;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.FakeApplication;
import play.test.Helpers;
import uk.co.panaxiom.playjongo.PlayJongo;
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

    private StatisticsDAO statisticsDAO = new StatisticsDAOImpl();
    private PersonDAO personDAO = new PersonDAOImpl();
    private ConsumerDAO consumerDAO = new ConsumerDAOImpl();

	private static MongoCollection statistics;// = PlayJongo.getCollection("Statistics");

    private static FakeApplication app;
    
    private Person p;
    private Consumer u;

    @BeforeClass
    public static void startApp() {
    	Map<String, String> config = new HashMap<String, String>();
        app = Helpers.fakeApplication(config);
        Helpers.start(app);
        statistics = PlayJongo.getCollection("Statistics");
    }
    
    @Before
    public void setUp() {
    	p =new Person(ObjectId.get().toString(),"pName", "pFirstname",0,"phht://url");
        u= new Consumer("email@email",100);
        consumerDAO.add(u.getEmail());
        personDAO.add(p,u.getEmail());
    }
    
    /**
     * Test AddStatistic function for a member
     */
    @Test
    public void add() {
        statisticsDAO.add(p.getIdPerson(), u.getEmail());
        statisticsDAO.add(p.getIdPerson(), u.getEmail());
        
        Iterable<Statistics> ip = statistics.find("{person.idPerson: #}", p.getIdPerson()).as(Statistics.class);
        List<Statistics> lp = new ArrayList<Statistics>();
        for(Statistics s : ip){
        	lp.add(s);
        }
        assertThat(lp.size()).isEqualTo(2);
    }

    @After
    public void tearDown() {
        personDAO.delete(p.getIdPerson(), u.getEmail());
        consumerDAO.delete(u.getEmail());
    }
    
    @AfterClass
    public static void stopApp() {
        Helpers.stop(app);
    }
}