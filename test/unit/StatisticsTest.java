package unit;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.inMemoryDatabase;

import java.util.List;

import javax.persistence.Query;

import models.Consumer;
import models.Person;

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
     * Test AddStatistic function for a member
     */
    @Test
    public void add() {
        
    }
    
    /**
     * Test ListByUser function for a user
     */
    @Test
    public void listByUser() {
        
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