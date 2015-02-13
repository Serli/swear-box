package integration;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.inMemoryDatabase;

import java.util.HashMap;
import java.util.Map;

import models.Consumer;
import models.Person;
import models.Statistics;
import net.vz.mongodb.jackson.DBQuery;
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
 * Test for AddPerson function
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
        consumers = MongoDB.getCollection("Consumer", Consumer.class, String.class);
    	people = MongoDB.getCollection("Person", Person.class, String.class);
    	statistics = MongoDB.getCollection("Statistics", Statistics.class, String.class);
        Helpers.start(app);
    }


    /**
     * Test for AddPerson function
     * Verification of the link between the two tables
     */
    @Test
    public void addPerson() {
        Person p =new Person(ObjectId.get().toString(),"Toto", "Titi",0,"yolo");
        Consumer u= new Consumer("email@email",100);

        //Recording of the user
        consumers.insert(u);
        Consumer ubd=consumers.findOneById("email@email");
        assertThat(ubd.getEmail()).isEqualTo(u.getEmail());

        //Add the person
        personDAO.add(p,u.getEmail());
        consumers.updateById("email@email", u);

        //Get the person from the DB
        
        Person pbd= people.findOne(DBQuery.is("name", "Toto"));

        //Check if the user has the person in his list
        assertThat(ubd.getPeople().get(0).fetch().getName()).isEqualTo(p.getName());
        assertThat(ubd.getPeople().get(0).fetch().getName()).isEqualTo(pbd.getName());

        //Check if the person has the user in his list
        assertThat(pbd.getUsers().get(0).fetch().getEmail()).isEqualTo(u.getEmail());
        assertThat(pbd.getUsers().get(0).fetch().getEmail()).isEqualTo(ubd.getEmail());
        consumers.remove(ubd);
        people.remove(pbd);
    }

    /**
     * Test removal of a person for two users
     * Verification of the link between the two tables
     * Verification of the removal of the person in both the user and in the table U_P
     */
    @Test
    public void deletePerson() {
        Person p =new Person(ObjectId.get().toString(),"Suppr-Toto", "Suppr-Titi",0,"yolo");
        Consumer u1= new Consumer("Suppr-email1@email",100);
        Consumer u2= new Consumer("Suppr-email2@email",50);

        //Recording users
        consumers.insert(u1);
        Consumer u1bd=consumers.findOneById("Suppr-email1@email");
        consumers.insert(u2);
        Consumer u2bd=consumers.findOneById("Suppr-email2@email");

        //Add the person and link it to a other user
        personDAO.add(p,u1.getEmail());
        Person pbd=people.findOne(DBQuery.is("name", "Suppr-Toto"));
        consumers.updateById("Suppr-email1@email", u1);
        
        
        consumerDAO.linkUserPerson(pbd.getIdPerson(),u2.getEmail());
        consumers.updateById("Suppr-email2@email", u2);

        //Check if the users have the person in their list
        assertThat(u1bd.getPeople().get(0).fetch().getName()).isEqualTo(p.getName());
        assertThat(u1bd.getPeople().get(0).fetch().getName()).isEqualTo(pbd.getName());

        assertThat(u2bd.getPeople().get(0).fetch().getName()).isEqualTo(p.getName());
        assertThat(u2bd.getPeople().get(0).fetch().getName()).isEqualTo(pbd.getName());

        //Check if the person has the users in his list
        assertThat(pbd.getUsers().get(0).fetch().getEmail()).isEqualTo(u1.getEmail());
        assertThat(pbd.getUsers().get(0).fetch().getEmail()).isEqualTo(u1bd.getEmail());
        assertThat(pbd.getUsers().get(1).fetch().getEmail()).isEqualTo(u2.getEmail());
        assertThat(pbd.getUsers().get(1).fetch().getEmail()).isEqualTo(u2bd.getEmail());        

        //Delete the person for the two users
        personDAO.delete(pbd.getIdPerson(),u1bd.getEmail());

        //Get users from the DB after the delete
        u1bd=consumers.findOneById("Suppr-email1@email");
        u2bd=consumers.findOneById("Suppr-email2@email");

        //Test if the person doesn't exist anymore
        assertThat(u1bd.getPeople()).isEmpty();
        assertThat(u2bd.getPeople()).isEmpty();

        //Clean
        consumers.remove(u1bd);
        consumers.remove(u2bd);
    }

    @AfterClass
    public static void stopApp() {
        Helpers.stop(app);
    }

}