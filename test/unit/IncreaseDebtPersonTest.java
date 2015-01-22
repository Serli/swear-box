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
 * Test IncreaseDebt function
 *
 */
public class IncreaseDebtPersonTest{

    private PersonDAO personDAO = new PersonDAOImpl();
    
    /**
     * Test increasing debt for a person
     */
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
                });
            }
        });
    }
}