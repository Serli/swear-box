package unit;
import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import models.Consumer;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.modules.mongodb.jackson.MongoDB;
import play.test.FakeApplication;
import play.test.Helpers;
import dao.ConsumerDAO;
import dao.ConsumerDAOImpl;




/**
 * Tests User functions
 * @author Geoffrey
 *
 */
public class UserTest {

    private ConsumerDAO consumerDAO = new ConsumerDAOImpl();
    private static FakeApplication app;
    private static JacksonDBCollection<Consumer, String> consumers;

    private String email;
    
    @BeforeClass
    public static void startApp() {
    	Map<String, String> config = new HashMap<String, String>();
        config.put("ehcacheplugin", "disabled");
        config.put("mongodbJacksonMapperCloseOnStop", "disabled");
        app = Helpers.fakeApplication(config);
        Helpers.start(app);
        consumers = MongoDB.getCollection("Consumer", Consumer.class, String.class);
    }

    @Before
    public void setUp() {
        //Add an user from an email
        email = "testupdateuser@gmail.com";
        consumerDAO.add(email);
        consumers = MongoDB.getCollection("Consumer", Consumer.class, String.class);
    }
    
    /**
     * Test add a user
     */
    @Test
    public void addUser() {
        //Seek the user in the DB
        Consumer u = consumers.findOneById(email);
        assertThat(u).isNotEqualTo(null);
    }
    
    /**
     * Test update amount of an user
     */
    @Test
    public void updateAmountUser() {
        consumerDAO.updateAmount(email, 20);
        
        //Seek the user in the DB
        Consumer u = consumers.findOneById(email);
        
        //Check if the amount is modified
        assertThat(u.getAmount()).isEqualTo(20);
    }
    
    /**
     * Test deleting a person
     */
    @Test
    public void deleteUser() {
    	Consumer u = consumers.findOneById(email);
    	
        //Delete the person for the two users
        consumerDAO.delete(u.getEmail());
        
        //Test if the person doesn't exist anymore
        Consumer udel = consumers.findOneById(email);
        assertThat(udel).isNull();
    }
    
    @After
    public void tearDown() {
    	consumerDAO.delete(email);
    }
    
    @AfterClass
    public static void stopApp() {
        Helpers.stop(app);
    }

}


