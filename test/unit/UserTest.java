package unit;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.inMemoryDatabase;
import models.Consumer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.db.jpa.JPA;
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

    @BeforeClass
    public static void startApp() {
        app = Helpers.fakeApplication(inMemoryDatabase());
        Helpers.start(app);
        JPA.bindForCurrentThread( JPA.em("default"));
    }

    @Before
    public void setUp() {
        JPA.em().getTransaction().begin();
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
        Consumer u = JPA.em().find(Consumer.class, email);

        //Check if the amount is modified
        assertThat(u.getAmount()==20);

        JPA.em().remove(u);
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
        Consumer u = JPA.em().find(Consumer.class, email);
        assertThat(u).isNotEqualTo(null);

        JPA.em().remove(u);
    }

    @After
    public void tearDown() {
        JPA.em().getTransaction().commit();
    }

    @AfterClass
    public static void stopApp() {
        JPA.bindForCurrentThread(null);
        Helpers.stop(app);
    }

}


