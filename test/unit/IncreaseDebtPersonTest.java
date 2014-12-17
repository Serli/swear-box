package unit;

import javax.persistence.Query;

import org.junit.*;

import com.google.inject.Inject;

import dao.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;

/**
 * Test IncreaseDebt function
 *
 */
public class IncreaseDebtPersonTest{

    @Inject
    private PersonDAO personDAO;
    
    /**
     * Test increasing debt for a person
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
                        Person p =new Person("increase-Toto", "increase-Titi",0,"yolo");
                        Consumer u1= new Consumer("increase-email1@email",100);

                        //enregistrement des utilisateurs
                        JPA.em().persist(u1);

                        //ajoute la personne et lie la personne a un autre utilisateur
                        personDAO.add(p,u1.getEmail());
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='increase-Toto'").getSingleResult();


                        JPA.em().flush();

                        //suppression de la personne pour les deux utilisateurs
                        personDAO.incrementDebt(pbd.getIdPerson(),"increase-email1@email");


                        //test si la personne n'existe plus
                        Query query = JPA.em().createQuery("Select p FROM Person p WHERE p.name='increase-Toto'");
                		Person pdebt = (Person) query.getSingleResult();
                        assertThat(pdebt.getDebt()==50);

                        //clean
                        JPA.em().remove(u1);
                        JPA.em().remove(p);
                    }
                });
            }
        });
    }
}