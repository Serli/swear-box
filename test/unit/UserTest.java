package unit;
import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import models.Consumer;

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




/**
 * Tests User functions
 * @author Geoffrey
 *
 */
public class UserTest {

    private ConsumerDAO consumerDAO = new ConsumerDAOImpl();
    private static FakeApplication app;
	private static MongoCollection consumers;//= PlayJongo.getCollection("Consumer");
	
    private String email;
    
    @BeforeClass
    public static void startApp() {
    	Map<String, String> config = new HashMap<String, String>();
        app = Helpers.fakeApplication(config);
        Helpers.start(app);
        consumers= PlayJongo.getCollection("Consumer");
    }

    @Before
    public void setUp() {
        //Add an user from an email
        email = "testupdateuser@gmail.com";
        consumerDAO.add(email);
    }
    
    /**
     * Test add a user
     */
    @Test
    public void addUser() {
        //Seek the user in the DB
        Consumer u = consumers.findOne("{_id: #}", email).as(Consumer.class);
        assertThat(u).isNotEqualTo(null);
    }
    
    /**
     * Test update amount of an user
     */
    @Test
    public void updateAmountUser() {
        consumerDAO.updateAmount(email, 20);
        
        //Seek the user in the DB
        Consumer u = consumers.findOne("{_id: #}", email).as(Consumer.class);
        
        //Check if the amount is modified
        assertThat(u.getAmount()).isEqualTo(20);
    }
    
    /**
     * Test deleting a person
     */
    @Test
    public void deleteUser() {
    	Consumer u = consumers.findOne("{_id: #}", email).as(Consumer.class);
    	
        //Delete the person for the two users
        consumerDAO.delete(u.getEmail());
        
        //Test if the person doesn't exist anymore
        Consumer udel = consumers.findOne("{_id: #}", email).as(Consumer.class);
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


