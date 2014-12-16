package unit;

import javax.persistence.Query;

import org.junit.*;

import dao.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;

/**
 * Test la classe DeletePerson
 *
 */
public class DebtPersonTest{

    /**
     * Test suppression d'une personne
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

                        //enregistrement des utilisateurs
                        JPA.em().persist(u1);

                        //ajoute la personne et lie la personne a un autre utilisateur
                        PersonDAO.add(p,u1.getEmail());
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='debt-Toto'").getSingleResult();


                        JPA.em().flush();

                        //suppression de la personne pour les deux utilisateurs
                        PersonDAO.debt(pbd.getIdPerson(),"debt-email1@email");


                        //test si la personne n'existe plus
                        Query query = JPA.em().createQuery("Select p FROM Person p WHERE p.name='debt-Toto'");
                		Person pdebt = (Person) query.getSingleResult();
                        assertThat(pdebt.getDebt()==0);

                        //clean
                        JPA.em().remove(p);
                        JPA.em().remove(u1);
                    }
                });
            }
        });
    }
}