package unit;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import javax.persistence.Query;

import models.Consumer;
import models.Person;

import org.junit.Test;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import dao.PersonDAO;
import dao.PersonDAOImpl;

/**
 * Test UpdateNameFirstname function
 *
 */
public class UpdateNameFirstnamePersonTest{

    
    private PersonDAO personDAO = new PersonDAOImpl();
    
    /**
     * Test updating a person
     */
    @Transactional
    @Test
    public void updateNameFirstnamePerson() {
        running(fakeApplication(inMemoryDatabase()), new Runnable()
        {
            public void run()
            {
                JPA.withTransaction(new play.libs.F.Callback0()
                {
                    public void invoke()
                    {
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
                });
            }
        });
    }
}