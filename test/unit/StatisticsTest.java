package unit;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.inMemoryDatabase;

import java.util.List;

import models.Consumer;
import models.Person;
import models.Statistics;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.db.jpa.JPA;
import play.test.FakeApplication;
import play.test.Helpers;
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
    	Person p =new Person("pName", "pFirstname",0,"https://url");
        Consumer u= new Consumer("email@email",100);
        JPA.em().persist(u);
        personDAO.add(p,u.getEmail());
        
        statisticsDAO.add(p.getIdPerson(), u.getEmail());
        statisticsDAO.add(p.getIdPerson(), u.getEmail());
        
        List<Statistics> lp= JPA.em().createQuery("Select s FROM Statistics s WHERE s.person = :member",Statistics.class).setParameter("member", p).getResultList();
        assertThat(lp.size()==2);
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