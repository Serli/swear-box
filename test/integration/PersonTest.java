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
 * Test for AddPerson function
 *
 */
public class PersonTest{

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
     * Test for AddPerson function
     * Verification of the link between the two tables
     */
    @Test
    public void addPerson() {
        Person p =new Person("Toto", "Titi",0,"yolo");
        Consumer u= new Consumer("email@email",100);

        //Recording of the user
        JPA.em().persist(u);
        Consumer ubd=JPA.em().find(Consumer.class,"email@email");
        assertThat(ubd.getEmail()).isEqualTo(u.getEmail());

        //Add the person
        personDAO.add(p,u.getEmail());

        //Get the person from the DB
        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Toto'").getSingleResult();

        //Check if the user has the person in his list
        assertThat(ubd.getPeople().get(0).getName()).isEqualTo(p.getName());
        assertThat(ubd.getPeople().get(0).getName()).isEqualTo(pbd.getName());

        //Check if the person has the user in his list
        assertThat(pbd.getUsers().get(0).getEmail()).isEqualTo(u.getEmail());
        assertThat(pbd.getUsers().get(0).getEmail()).isEqualTo(ubd.getEmail());
        JPA.em().remove(ubd);
        JPA.em().remove(pbd);
    }

    /**
     * Test removal of a person for two users
     * Verification of the link between the two tables
     * Verification of the removal of the person in both the user and in the table U_P
     */
    @Test
    public void deletePerson() {
        Person p =new Person("Suppr-Toto", "Suppr-Titi",0,"yolo");
        Consumer u1= new Consumer("Suppr-email1@email",100);
        Consumer u2= new Consumer("Suppr-email2@email",50);

        //Recording users
        JPA.em().persist(u1);
        Consumer u1bd=JPA.em().find(Consumer.class,"Suppr-email1@email");
        JPA.em().persist(u2);
        Consumer u2bd=JPA.em().find(Consumer.class,"Suppr-email2@email");

        //Add the person and link it to a other user
        personDAO.add(p,u1.getEmail());
        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Suppr-Toto'").getSingleResult();
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

        JPA.em().flush();

        //Delete the person for the two users
        personDAO.delete(pbd.getIdPerson(),u1bd.getEmail());

        //Get users from the DB after the delete
        u1bd=JPA.em().find(Consumer.class,"Suppr-email1@email");
        u2bd=JPA.em().find(Consumer.class,"Suppr-email2@email");

        //Test if the person doesn't exist anymore
        assertThat(u1bd.getPeople()).isEmpty();
        assertThat(u2bd.getPeople()).isEmpty();

        //Clean
        JPA.em().remove(u1bd);
        JPA.em().remove(u2bd);
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