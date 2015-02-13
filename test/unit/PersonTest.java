package unit;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.inMemoryDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import models.Consumer;
import models.Person;
import models.Statistics;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.DBRef;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.db.jpa.JPA;
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
    private static FakeApplication app;
    private static JacksonDBCollection<Consumer, String> consumers ;
    private static JacksonDBCollection<Person, String> people;
	private static JacksonDBCollection<Statistics, String> statistics;
	
    @BeforeClass
    public static void startApp() {
    	Map<String, String> config = new HashMap<String, String>();
        config.put("ehcacheplugin", "disabled");
        config.put("mongodbJacksonMapperCloseOnStop", "disabled");
        app = Helpers.fakeApplication(config);
        Helpers.start(app);
        consumers = MongoDB.getCollection("Consumer", Consumer.class, String.class);
    	people = MongoDB.getCollection("Person", Person.class, String.class);
    	statistics = MongoDB.getCollection("Statistics", Statistics.class, String.class);
        
    }

    /**
     * Test AddPerson function for a user
     */
    @Test
    public void addPerson() {
        Person p =new Person(ObjectId.get().toString(),"Toto", "Titi",0,"yolo");
        Consumer u= new Consumer("email@email",100);

        consumers.insert(u);

        personDAO.add(p,u.getEmail());

        //Get the person from the DB
        Person pbd=people.findOne(DBQuery.is("name", "Toto"));
        
        //Check if the user has the person in his list
        assertThat(pbd).isNotEqualTo(null);

        consumers.remove(u);
        people.remove(pbd);
    }

    /**
     * Test deleting a person
     */
    @Test
    public void deletePerson() {
        Person p =new Person(ObjectId.get().toString(),"Suppr-Toto", "Suppr-Titi",0,"yolo");
        Consumer u1= new Consumer("Suppr-email1@email",100);

        //Recording the users
        consumers.insert(u1);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        Person pbd= people.findOne(DBQuery.is("name", "Suppr-Toto"));

        consumers.updateById("Suppr-email1@email", u1);

        //Delete the person for the two users
        personDAO.delete(pbd.getIdPerson(),"Suppr-email1@email");

        //Test if the person doesn't exist anymore
        List<Person> lp= people.find(DBQuery.in("name", "Suppr-Toto")).toArray();
        assertThat(lp).isEmpty();

        //Clean
        consumers.remove(u1);
    }

    /**
     * Test debt a person
     */
    @Test
    public void debtPerson() {
        Person p =new Person(ObjectId.get().toString(),"debt-Toto", "debt-Titi",10,"yolo");
        Consumer u1= new Consumer("debt-email1@email",100);

        //Recording users
        consumers.insert(u1);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        Person pbd=people.findOne(DBQuery.is("name", "debt-Toto"));

        consumers.updateById("debt-email1@email", u1);

        //Discharge the person for the two users
        personDAO.discharge(pbd.getIdPerson(),"debt-email1@email");

        //Test if the debt is equals to 0
        Person pdebt = people.findOne(DBQuery.is("name", "debt-Toto"));
        assertThat(pdebt.getDebt()==0);

        //Clean
        people.remove(p);
        consumers.remove(u1);
    }

    /**
     * Test increasing debt for a person
     */
    @Test
    public void increaseDebtPerson() {
        Person p =new Person(ObjectId.get().toString(),"increase-Toto", "increase-Titi",0,"yolo");
        Consumer u1= new Consumer("increase-email1@email",100);

        //Recording users
        consumers.insert(u1);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        Person pbd= people.findOne(DBQuery.is("name", "increase-Toto"));

        consumers.updateById("increase-email1@email", u1);

        //Increase the debt for the person
        personDAO.incrementDebt(pbd.getIdPerson(),"increase-email1@email");
        
        people.updateById(pbd.getIdPerson(), pbd);

        //Test if the debt is increased by the amount
        Person pdebt = people.findOne(DBQuery.is("name", "increase-Toto"));
        assertThat(pdebt.getDebt()==50);

        //Clean
        consumers.remove(u1);
        people.remove(p);
    }

    /**
     * Test the recovery of the list of the user related members in the database
     */
    @Test
    public void listPeopleOfAUser() {
        // Add 2 users
        String emailU1 = "test1@email.com";
        Consumer u1 = new Consumer(emailU1, 100);
        consumers.insert(u1);
        Consumer u2 = new Consumer("test2@email.com", 100);
        consumers.insert(u2);

        // Add 3 persons
        Person p1 = new Person(ObjectId.get().toString(),"nom1", "prenom1", 1, "adr1");
        Person p2 = new Person(ObjectId.get().toString(),"nom2", "prenom2", 2, "adr2");
        Person p3 = new Person(ObjectId.get().toString(),"nom3", "prenom3", 3, "adr3");
        people.insert(p1);
        people.insert(p2);
        people.insert(p3);

        // Link persons to users
        DBRef<Person,String> pref = new DBRef<Person,String>(p1.getIdPerson(),Person.class);
        DBRef<Consumer,String> uref = new DBRef<Consumer,String>(u1.getEmail(),Consumer.class);
        u1.setPerson(pref);
        p1.setUser(uref);
        pref = new DBRef<Person,String>(p2.getIdPerson(),Person.class);
        u1.setPerson(pref);
        p2.setUser(uref);
        pref = new DBRef<Person,String>(p3.getIdPerson(),Person.class);
        uref = new DBRef<Consumer,String>(u1.getEmail(),Consumer.class);
        u2.setPerson(pref);
        p3.setUser(uref);

        //Synchronisation with the DB
        consumers.updateById("test1@email", u1);
        consumers.updateById("test2@email", u2);
        people.updateById(p1.getIdPerson(), p1);
        people.updateById(p2.getIdPerson(), p2);
        people.updateById(p3.getIdPerson(), p3);

        //Get the list of the person link to u1
        List<Person> l = personDAO.listByUser(emailU1);

        assertThat(l.size()).isEqualTo(2);
        assertThat(l.get(0).getFirstname()).isEqualTo(p1.getFirstname());
        assertThat(l.get(1).getFirstname()).isEqualTo(p2.getFirstname());

        consumers.remove(u1);
        consumers.remove(u2);
        people.remove(p1);
        people.remove(p2);
        people.remove(p3);
    }

    /**
     * Test updating a person
     */
    @Test
    public void updateNameFirstnamePerson() {
        Person p =new Person(ObjectId.get().toString(),"update-Toto", "update-Titi",0,"yolo");
        Consumer u1= new Consumer("update-email1@email",100);

        //Recording users
        consumers.insert(u1);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        Person pbd= people.findOne(DBQuery.is("name", "update-Toto"));

        consumers.updateById("update-email1@email", u1);

        //Update the name and the firstname of the person
        personDAO.updateNameFirstname(pbd.getIdPerson(),"update-email1@email","toto","titi");

        people.updateById(pbd.getIdPerson(), pbd);
        
        //Test if the update worked
        Person pdebt = people.findOne(DBQuery.is("name", "toto"));
        assertThat(pdebt.getName().equals("toto"));

        //Clean
        consumers.remove(u1);
        people.remove(p);
    }

    /**
     * Test updating the picture path for a person
     */
    @Test
    public void updatePicturePerson() {
        Person p =new Person(ObjectId.get().toString(),"updateP-Toto", "updateP-Titi",0,"yolo");
        Consumer u1= new Consumer("updateP-email1@email",100);

        //Recording users
        consumers.insert(u1);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        Person pbd= people.findOne(DBQuery.is("name", "updateP-Toto"));

        consumers.updateById("updateP-email1@email", u1);

        //Update the picture the person
        personDAO.updatePicture(pbd.getIdPerson(),"updateP-email1@email","toto");
        people.updateById(pbd.getIdPerson(), pbd);
        
        //Test the picture is updated
        Person pdebt = people.findOne(DBQuery.is("name", "updateP-Toto"));
        assertThat(pdebt.getPicture().equals("toto"));

        //Clean
        consumers.remove(u1);
        people.remove(p);
    }

    /**
     * Test a link between two users and a person
     */
    @Test
    public void linkUserToPerson() {
        //Creation of entities
        Person p =new Person(ObjectId.get().toString(),"Lier-Toto", "Lier-Titi",0,"yolo");
        Consumer u1= new Consumer("Lier-email1@email",100);
        Consumer u2= new Consumer("Lier-email2@email",50);

        //Recording users
        consumers.insert(u1);
        consumers.insert(u2);

        //Add the person and link it to an other user
        personDAO.add(p,u1.getEmail());
        consumers.updateById("Lier-email1@email", u1);

        //Add the person and link it to an other user
        Person pbd= people.findOne(DBQuery.is("name", "Lier-Toto"));
        consumerDAO.linkUserPerson(pbd.getIdPerson(),u2.getEmail());
        consumers.updateById("Lier-email2@email", u2);
        
        //Check is both users have the same name
        assertThat(u2.getPeople().get(0).fetch().getName()).isEqualTo(pbd.getName());

        //Clean
        consumers.remove(u1);
        consumers.remove(u2);
        people.remove(pbd);
    }
    
    @AfterClass
    public static void stopApp() {
        //Helpers.stop(app);
    }

}