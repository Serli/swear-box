package integration;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.inMemoryDatabase;
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

/**
 * Test for LinkUserPerson function
 *
 */
public class UserTest{

    private PersonDAO personDAO = new PersonDAOImpl();
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
     * Test if the class LinkUserPerson correctly binds a person to a user
     */
    @Test
    public void linkUserToPerson() {
        Person p =new Person("Lier-Toto", "Lier-Titi",0,"yolo");
        Consumer u1= new Consumer("Lier-email1@email",100);
        Consumer u2= new Consumer("Lier-email2@email",50);

        //Recording users and get the users from the DB
        JPA.em().persist(u1);
        Consumer u1bd=JPA.em().find(Consumer.class,"Lier-email1@email");
        JPA.em().persist(u2);
        Consumer u2bd=JPA.em().find(Consumer.class,"Lier-email2@email");

        //Add the person to an user
        personDAO.add(p,u1.getEmail());

        //Get the person
        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Lier-Toto'").getSingleResult();

        //Link the person to the other user
        consumerDAO.linkUserPerson(pbd.getIdPerson(),u2.getEmail());

        //Check if the users have the person in their list
        assertThat(u1bd.getPeople().get(0).getName()).isEqualTo(p.getName());
        assertThat(u1bd.getPeople().get(0).getName()).isEqualTo(pbd.getName());

        assertThat(u2bd.getPeople().get(0).getName()).isEqualTo(p.getName());
        assertThat(u2bd.getPeople().get(0).getName()).isEqualTo(pbd.getName());

        //Check if the person has the users in his list
        assertThat(pbd.getUsers().get(0).getEmail()).isEqualTo(u1.getEmail());
        assertThat(pbd.getUsers().get(0).getEmail()).isEqualTo(u1bd.getEmail());
        assertThat(pbd.getUsers().get(1).getEmail()).isEqualTo(u2.getEmail());
        assertThat(pbd.getUsers().get(1).getEmail()).isEqualTo(u2bd.getEmail());

        //Clean
        JPA.em().remove(u1bd);
        JPA.em().remove(u2bd);
        JPA.em().remove(pbd);
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