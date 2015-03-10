package unit;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Consumer;
import models.Person;

import org.bson.types.ObjectId;
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
import dao.PersonDAO;
import dao.PersonDAOImpl;

/**
 * Tests for Person functions
 *
 */
public class PersonTest{

    private PersonDAO personDAO = new PersonDAOImpl();
    private ConsumerDAO consumerDAO = new ConsumerDAOImpl();
    
	private static MongoCollection consumers;// = PlayJongo.getCollection("Consumer");
    
    private static FakeApplication app;
    
    private Person p;
    private Consumer u;

    @BeforeClass
    public static void startApp() {
    	Map<String, String> config = new HashMap<String, String>();
        app = Helpers.fakeApplication(config);
        Helpers.start(app);
        consumers = PlayJongo.getCollection("Consumer");
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
        Person pbd = consumers.findOne("{people.idPerson: #}", p.getIdPerson()).as(Person.class);

        //Check if the user has the person in his list
        assertThat(pbd).isNotEqualTo(null);
    }

    /**
     * Test deleting a person
     */
    @Test
    public void deletePerson() {
    	Consumer c = consumers.findOne("{_id: #}", u.getEmail()).as(Consumer.class);
    	Person pbd=null;
    	for (Person ptmp : c.getPeople()){
    		if(ptmp.getIdPerson().equals(p.getIdPerson()))
    			pbd=ptmp;
    	}

        //Delete the person for the two users
        personDAO.delete(pbd.getIdPerson(),u.getEmail());

        //Test if the person doesn't exist anymore
        Consumer c2 = consumers.findOne("{_id: #}", u.getEmail()).as(Consumer.class);
    	Person psupp=null;
    	for (Person ptmp : c2.getPeople()){
    		if(ptmp.getIdPerson().equals(p.getIdPerson()))
    			psupp=ptmp;
    	}
        assertThat(psupp).isNull();
    }

    /**
     * Test debt a person
     */
    @Test
    public void debtPerson() {
    	Consumer c = consumers.findOne("{_id: #}", u.getEmail()).as(Consumer.class);
    	Person pbd=null;
    	for (Person ptmp : c.getPeople()){
    		if(ptmp.getIdPerson().equals(p.getIdPerson()))
    			pbd=ptmp;
    	}

        //Discharge the person for the two users
        personDAO.discharge(pbd.getIdPerson(),u.getEmail());

        //Test if the debt is equals to 0
        Consumer c2 = consumers.findOne("{_id: #}", u.getEmail()).as(Consumer.class);
    	Person pdebt=null;
    	for (Person ptmp : c2.getPeople()){
    		if(ptmp.getIdPerson().equals(p.getIdPerson()))
    			pdebt=ptmp;
    	}
        assertThat(pdebt.getDebt()).isEqualTo(0);
    }

    /**
     * Test increasing debt for a person
     */
    @Test
    public void increaseDebtPerson() {
    	Consumer c = consumers.findOne("{_id: #}", u.getEmail()).as(Consumer.class);
    	Person pbd=null;
    	for (Person ptmp : c.getPeople()){
    		if(ptmp.getIdPerson().equals(p.getIdPerson()))
    			pbd=ptmp;
    	}

        //Increase the debt for the person
        personDAO.incrementDebt(pbd.getIdPerson(),u.getEmail());

        //Test if the debt is increased by the amount
        Consumer c2 = consumers.findOne("{_id: #}", u.getEmail()).as(Consumer.class);
    	Person pdebt=null;
    	for (Person ptmp : c2.getPeople()){
    		if(ptmp.getIdPerson().equals(p.getIdPerson()))
    			pdebt=ptmp;
    	}
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
    	
    	personDAO.delete(p2.getIdPerson(), u.getEmail());
    	personDAO.delete(p3.getIdPerson(), u.getEmail());
    }

    /**
     * Test updating a person
     */
    @Test
    public void updateNameFirstnamePerson() {
    	Consumer c = consumers.findOne("{_id: #}", u.getEmail()).as(Consumer.class);
    	Person pbd=null;
    	for (Person ptmp : c.getPeople()){
    		if(ptmp.getIdPerson().equals(p.getIdPerson()))
    			pbd=ptmp;
    	}

        //Update the name and the firstname of the person
        personDAO.updateNameFirstname(pbd.getIdPerson(),u.getEmail(),"toto","titi");
        
        //Test if the update worked
        Consumer c2 = consumers.findOne("{_id: #}", u.getEmail()).as(Consumer.class);
    	Person pupdate=null;
    	for (Person ptmp : c2.getPeople()){
    		if(ptmp.getIdPerson().equals(p.getIdPerson()))
    			pupdate=ptmp;
    	}
        assertThat(pupdate.getName()).isEqualTo("toto");
        assertThat(pupdate.getFirstname()).isEqualTo("titi");
    }

    /**
     * Test updating the picture path for a person
     */
    @Test
    public void updatePicturePerson() {
    	Consumer c = consumers.findOne("{_id: #}", u.getEmail()).as(Consumer.class);
    	Person pbd=null;
    	for (Person ptmp : c.getPeople()){
    		if(ptmp.getIdPerson().equals(p.getIdPerson()))
    			pbd=ptmp;
    	}

        //Update the picture the person
        personDAO.updatePicture(pbd.getIdPerson(),u.getEmail(),"toto");

        //Test the picture is updated
        Consumer c2 = consumers.findOne("{_id: #}", u.getEmail()).as(Consumer.class);
    	Person pupdate=null;
    	for (Person ptmp : c2.getPeople()){
    		if(ptmp.getIdPerson().equals(p.getIdPerson()))
    			pupdate=ptmp;
    	}
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