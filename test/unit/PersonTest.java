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

/**
 * Tests for Person functions
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
     * Test AddPerson function for a user
     */
    @Test
    public void addPerson() {
        Person p =new Person("Toto", "Titi",0,"yolo");
        Consumer u= new Consumer("email@email",100);

        JPA.em().persist(u);

        personDAO.add(p,u.getEmail());

        //Get the person from the DB
        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Toto'").getSingleResult();

        //Check if the user has the person in his list
        assertThat(pbd).isNotEqualTo(null);

        JPA.em().remove(u);
        JPA.em().remove(pbd);
    }

    /**
     * Test deleting a person
     */
    @Test
    public void deletePerson() {
        Person p =new Person("Suppr-Toto", "Suppr-Titi",0,"yolo");
        Consumer u1= new Consumer("Suppr-email1@email",100);

        //Recording the users
        JPA.em().persist(u1);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Suppr-Toto'").getSingleResult();

        JPA.em().flush();

        //Delete the person for the two users
        personDAO.delete(pbd.getIdPerson(),"Suppr-email1@email");

        //Test if the person doesn't exist anymore
        List<Person> lp= JPA.em().createQuery("Select p FROM Person p WHERE p.name='Suppr-Toto'",Person.class).getResultList();
        assertThat(lp).isEmpty();

        //Clean
        JPA.em().remove(u1);
    }

    /**
     * Test debt a person
     */
    @Test
    public void debtPerson() {
        Person p =new Person("debt-Toto", "debt-Titi",10,"yolo");
        Consumer u1= new Consumer("debt-email1@email",100);

        //Recording users
        JPA.em().persist(u1);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='debt-Toto'").getSingleResult();

        JPA.em().flush();

        //Discharge the person for the two users
        personDAO.discharge(pbd.getIdPerson(),"debt-email1@email");

        //Test if the debt is equals to 0
        Query query = JPA.em().createQuery("Select p FROM Person p WHERE p.name='debt-Toto'");
        Person pdebt = (Person) query.getSingleResult();
        assertThat(pdebt.getDebt()==0);

        //Clean
        JPA.em().remove(p);
        JPA.em().remove(u1);
    }

    /**
     * Test increasing debt for a person
     */
    @Test
    public void increaseDebtPerson() {
        Person p =new Person("increase-Toto", "increase-Titi",0,"yolo");
        Consumer u1= new Consumer("increase-email1@email",100);

        //Recording users
        JPA.em().persist(u1);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='increase-Toto'").getSingleResult();

        JPA.em().flush();

        //Increase the debt for the person
        personDAO.incrementDebt(pbd.getIdPerson(),"increase-email1@email");

        //Test if the debt is increased by the amount
        Query query = JPA.em().createQuery("Select p FROM Person p WHERE p.name='increase-Toto'");
        Person pdebt = (Person) query.getSingleResult();
        assertThat(pdebt.getDebt()==50);

        //Clean
        JPA.em().remove(u1);
        JPA.em().remove(p);
    }

    /**
     * Test the recovery of the list of the user related members in the database
     */
    @Test
    public void listPeopleOfAUser() {
        // Add 2 users
        String emailU1 = "test1@email.com";
        Consumer u1 = new Consumer(emailU1, 100);
        JPA.em().persist(u1);
        Consumer u2 = new Consumer("test2@email.com", 100);
        JPA.em().persist(u2);

        // Add 3 persons
        Person p1 = new Person("nom1", "prenom1", 1, "adr1");
        Person p2 = new Person("nom2", "prenom2", 2, "adr2");
        Person p3 = new Person("nom3", "prenom3", 3, "adr3");
        JPA.em().persist(p1);
        JPA.em().persist(p2);
        JPA.em().persist(p3);

        // Link persons to users
        u1.setPerson(p1);
        p1.setUser(u1);
        u1.setPerson(p2);
        p2.setUser(u1);
        u2.setPerson(p3);
        p3.setUser(u2);

        //Synchronisation with the DB
        JPA.em().flush();

        //Get the list of the person link to u1
        List<Person> l = personDAO.listByUser(emailU1);

        assertThat(l.size()).isEqualTo(2);
        assertThat(l.get(0).getFirstname()).isEqualTo(p1.getFirstname());
        assertThat(l.get(1).getFirstname()).isEqualTo(p2.getFirstname());

        JPA.em().remove(u1);
        JPA.em().remove(u2);
        JPA.em().remove(p1);
        JPA.em().remove(p2);
        JPA.em().remove(p3);
    }

    /**
     * Test updating a person
     */
    @Test
    public void updateNameFirstnamePerson() {
        Person p =new Person("update-Toto", "update-Titi",0,"yolo");
        Consumer u1= new Consumer("update-email1@email",100);

        //Recording users
        JPA.em().persist(u1);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='update-Toto'").getSingleResult();

        JPA.em().flush();

        //Update the name and the firstname of the person
        personDAO.updateNameFirstname(pbd.getIdPerson(),"update-email1@email","toto","titi");

        //Test if the update worked
        Query query = JPA.em().createQuery("Select p FROM Person p WHERE p.name='toto'");
        Person pdebt = (Person) query.getSingleResult();
        assertThat(pdebt.getName().equals("toto"));

        //Clean
        JPA.em().remove(u1);
        JPA.em().remove(p);
    }

    /**
     * Test updating the picture path for a person
     */
    @Test
    public void updatePicturePerson() {
        Person p =new Person("updateP-Toto", "updateP-Titi",0,"yolo");
        Consumer u1= new Consumer("updateP-email1@email",100);

        //Recording users
        JPA.em().persist(u1);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='updateP-Toto'").getSingleResult();

        JPA.em().flush();

        //Update the picture the person
        personDAO.updatePicture(pbd.getIdPerson(),"updateP-email1@email","toto");

        //Test the picture is updated
        Query query = JPA.em().createQuery("Select p FROM Person p WHERE p.name='updateP-Toto'");
        Person pdebt = (Person) query.getSingleResult();
        assertThat(pdebt.getPicture().equals("toto"));

        //Clean
        JPA.em().remove(u1);
        JPA.em().remove(p);
    }

    /**
     * Test a link between two users and a person
     */
    @Test
    public void linkUserToPerson() {
        //Creation of entities
        Person p =new Person("Lier-Toto", "Lier-Titi",0,"yolo");
        Consumer u1= new Consumer("Lier-email1@email",100);
        Consumer u2= new Consumer("Lier-email2@email",50);

        //Recording users
        JPA.em().persist(u1);
        JPA.em().persist(u2);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());

        //Add the person and link it to an other user
        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Lier-Toto'").getSingleResult();
        consumerDAO.linkUserPerson(pbd.getIdPerson(),u2.getEmail());

        //Check is both users have the same name
        assertThat(u2.getPeople().get(0).getName()).isEqualTo(pbd.getName());

        //Clean
        JPA.em().remove(u1);
        JPA.em().remove(u2);
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