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
 * Test DischargePerson function
 *
 */
public class DischargePersonTest{

    private PersonDAO personDAO = new PersonDAOImpl();
    
    /**
     * Test deleting a person
     */
    @Transactional
    @Test
    public void debtPerson() {
        running(fakeApplication(inMemoryDatabase()), new Runnable()
        {
            public void run()
            {
                JPA.withTransaction(new play.libs.F.Callback0()
                {
                    public void invoke()
                    {
                        Person p =new Person("debt-Toto", "debt-Titi",10,"yolo");
                        Consumer u1= new Consumer("debt-email1@email",100);

                        //Recording users
                        JPA.em().persist(u1);

                        //Add the person and link it to an other user
                        personDAO.add(p,u1.getEmail());
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='debt-Toto'").getSingleResult();


                        JPA.em().flush();

                        //Discharge the person for the two users
                        personDAO.discharge(pbd.getIdPerson(),"debt-email1@email");


                        //Test if the debt is equals to 0
                        Query query = JPA.em().createQuery("Select p FROM Person p WHERE p.name='debt-Toto'");
                		Person pdebt = (Person) query.getSingleResult();
                        assertThat(pdebt.getDebt()==0);

                        //Clean
                        JPA.em().remove(p);
                        JPA.em().remove(u1);
                    }
                });
            }
        });
    }
}