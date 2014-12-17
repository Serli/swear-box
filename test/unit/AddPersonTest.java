package unit;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import models.Consumer;
import models.Person;

import org.junit.Test;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import dao.PersonDAO;
import dao.PersonDAOImpl;

/**
 * Test for AddPerson function
 *
 */
public class AddPersonTest{

    private PersonDAO personDAO = new PersonDAOImpl();
	
    /**
     * Test AddPerson function for a user
     */
    @Transactional
    @Test
    public void addPerson() {
        running(fakeApplication(inMemoryDatabase()), new Runnable()
        {
            public void run()
            {
                JPA.withTransaction(new play.libs.F.Callback0()
                {
                    public void invoke()
                    {
                        Person p =new Person("Toto", "Titi",0,"yolo");
                        Consumer u= new Consumer("email@email",100);

                        //Recording the user
                        JPA.em().persist(u);

                        //Add the person
                        personDAO.add(p,u.getEmail());

                        //Get the person from the DB
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Toto'").getSingleResult();

                        //Check if the user has the person in his list
                        assertThat(pbd).isNotEqualTo(null);

                        JPA.em().remove(u);
                        JPA.em().remove(pbd);
                    }
                });
            }
        });
    }

}