package unit;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.List;

import models.Consumer;
import models.Person;

import org.junit.Test;

import play.db.jpa.JPA;
import dao.PersonDAO;
import dao.PersonDAOImpl;

/**
 * Test ListPeople function
 * @author Geoffrey
 *
 */
public class ListPeopleTest {

    private PersonDAO personDAO = new PersonDAOImpl();
    
    /**
     * Test the recovery of the list of the user related members in the database
     */
    @Test
    public void listPeopleOfAUser() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                JPA.withTransaction(new play.libs.F.Callback0() {
                    public void invoke() {
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
                });
            }
        });
    }
}
