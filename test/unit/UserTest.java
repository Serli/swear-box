package unit;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.inMemoryDatabase;

import java.util.HashMap;
import java.util.Map;

import models.Consumer;
import models.Person;
import models.Statistics;
import net.vz.mongodb.jackson.JacksonDBCollection;

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




/**
 * Tests User functions
 * @author Geoffrey
 *
 */
public class UserTest {

	private PersonDAO personDAO = new PersonDAOImpl();
    private ConsumerDAO consumerDAO = new ConsumerDAOImpl();
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
     * Test adding an user
     */
    @Test
    public void updateAmountUser() {
        //Add an user from an email
        String email = "test@gmail.com";
        consumerDAO.add(email);

        consumerDAO.updateAmount(email, 20);

        //Seek the user in the DB
        Consumer u = consumers.findOneById(email);

        //Check if the amount is modified
        assertThat(u.getAmount()==20);

        consumers.remove(u);
    }

    /**
     * Test AddUser for an user
     */
    @Test
    public void addUser() {
        //Add an user from a email
        String email = "test@gmail.com";
        consumerDAO.add(email);

        //Seek the user in the DB
        Consumer u = consumers.findOneById(email);
        assertThat(u).isNotEqualTo(null);

        consumers.remove(u);
    }

    @AfterClass
    public static void stopApp() {
        Helpers.stop(app);
    }

}


