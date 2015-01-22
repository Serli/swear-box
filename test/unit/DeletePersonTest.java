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
import play.db.jpa.Transactional;
import dao.PersonDAO;
import dao.PersonDAOImpl;

/**
 * Test for DeletePerson function
 *
 */
public class DeletePersonTest{

	private PersonDAO personDAO = new PersonDAOImpl();
	
    /**
     * Test deleting a person
     */
    @Test
    public void deletePerson() {
        running(fakeApplication(inMemoryDatabase()), new Runnable()
        {
            public void run()
            {
                JPA.withTransaction(new play.libs.F.Callback0()
                {
                    public void invoke()
                    {
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
                });
            }
        });
    }
}