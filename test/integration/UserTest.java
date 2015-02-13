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
 * Test for LinkUserPerson function
 *
 */
public class UserTest{

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
     * Test if the class LinkUserPerson correctly binds a person to a user
     */
    @Test
    public void linkUserToPerson() {
        Person p =new Person(ObjectId.get().toString(),"Lier-Toto", "Lier-Titi",0,"yolo");
        Consumer u1= new Consumer("Lier-email1@email",100);
        Consumer u2= new Consumer("Lier-email2@email",50);

        //Recording users and get the users from the DB
        consumers.insert(u1);
        Consumer u1bd=consumers.findOneById("Lier-email1@email");
        consumers.insert(u2);
        Consumer u2bd=consumers.findOneById("Lier-email2@email");

        //Add the person to an user
        personDAO.add(p,u1.getEmail());
        consumers.updateById("Lier-email1@email", u1);

        //Get the person
        Person pbd=people.findOne(DBQuery.is("name", "Lier-Toto"));

        //Link the person to the other user
        consumerDAO.linkUserPerson(pbd.getIdPerson(),u2.getEmail());
        consumers.updateById("Lier-email2@email", u2);

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

        //Clean
        consumers.remove(u1bd);
        consumers.remove(u2bd);
        people.remove(pbd);
    }


    @AfterClass
    public static void stopApp() {
        Helpers.stop(app);
    }

}