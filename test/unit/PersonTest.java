package unit;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Consumer;
import models.Person;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.bson.types.ObjectId;
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
import dao.PersonDAO;
import dao.PersonDAOImpl;

/**
 * Tests for Person functions
 *
 */
public class PersonTest{

    private PersonDAO personDAO = new PersonDAOImpl();
    private ConsumerDAO consumerDAO = new ConsumerDAOImpl();
    
	private static JacksonDBCollection<Person, String> people;
    
    private static FakeApplication app;
    
    private Person p;
    private Consumer u;

    @BeforeClass
    public static void startApp() {
    	Map<String, String> config = new HashMap<String, String>();
        config.put("ehcacheplugin", "disabled");
        config.put("mongodbJacksonMapperCloseOnStop", "disabled");
        app = Helpers.fakeApplication(config);
        Helpers.start(app);
        
        people = MongoDB.getCollection("Person", Person.class, String.class);
    }

    @Before
    public void setUp() {
    	p =new Person(ObjectId.get().toString(),"pName", "pFirstname",0,"phht://url");
        u= new Consumer("email@email",100);
        consumerDAO.add(u.getEmail());
        personDAO.add(p,u.getEmail());
    }
    
    /**
     * Test AddPerson function for a user
     */
    @Test
    public void addPerson() {
        //Get the person from the DB
        Person pbd = people.findOneById(p.getIdPerson());

        //Check if the user has the person in his list
        assertThat(pbd).isNotEqualTo(null);
    }

    /**
     * Test deleting a person
     */
    @Test
    public void deletePerson() {
        Person pbd = people.findOneById(p.getIdPerson());

        //Delete the person for the two users
        personDAO.delete(pbd.getIdPerson(),u.getEmail());

        //Test if the person doesn't exist anymore
        Person psupp = people.findOneById(p.getIdPerson());
        assertThat(psupp).isNull();
    }

    /**
     * Test debt a person
     */
    @Test
    public void debtPerson() {
        Person pbd = people.findOneById(p.getIdPerson());

        //Discharge the person for the two users
        personDAO.discharge(pbd.getIdPerson(),u.getEmail());

        //Test if the debt is equals to 0
        Person pdebt = people.findOneById(p.getIdPerson());
        assertThat(pdebt.getDebt()).isEqualTo(0);
    }

    /**
     * Test increasing debt for a person
     */
    @Test
    public void increaseDebtPerson() {
        Person pbd = people.findOneById(p.getIdPerson());

        //Increase the debt for the person
        personDAO.incrementDebt(pbd.getIdPerson(),u.getEmail());

        //Test if the debt is increased by the amount
        Person pdebt = people.findOneById(p.getIdPerson());
        assertThat(pdebt.getDebt()).isEqualTo(50);
    }

    /**
     * Test the recovery of the list of the user related members in the database
     */
    @Test
    public void listPeopleOfAUser() {
    	Person p2 =new Person(ObjectId.get().toString(),"pName", "pFirstname",0,"phht://url");
    	Person p3 =new Person(ObjectId.get().toString(),"pName", "pFirstname",0,"phht://url");
    	
    	personDAO.add(p2, u.getEmail());
    	personDAO.add(p3, u.getEmail());
    	
    	List<Person> l = personDAO.listByUser(u.getEmail());
    	
    	assertThat(l.size()).isEqualTo(3);
    }

    /**
     * Test updating a person
     */
    @Test
    public void updateNameFirstnamePerson() {
    	Person pbd = people.findOneById(p.getIdPerson());

        //Update the name and the firstname of the person
        personDAO.updateNameFirstname(pbd.getIdPerson(),u.getEmail(),"toto","titi");

        //Test if the update worked
        Person pupdate = people.findOneById(p.getIdPerson());
        assertThat(pupdate.getName()).isEqualTo("toto");
        assertThat(pupdate.getFirstname()).isEqualTo("titi");
    }

    /**
     * Test updating the picture path for a person
     */
    @Test
    public void updatePicturePerson() {
    	Person pbd = people.findOneById(p.getIdPerson());

        //Update the picture the person
        personDAO.updatePicture(pbd.getIdPerson(),u.getEmail(),"toto");

        //Test the picture is updated
        Person pupdate = people.findOneById(p.getIdPerson());
        assertThat(pupdate.getPicture()).isEqualTo("toto");
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