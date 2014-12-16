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
 * Test UpdateNameFirstname function
 *
 */
public class UpdateNameFirstnamePersonTest{

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

                        //enregistrement des utilisateurs
                        JPA.em().persist(u1);

                        //ajoute la personne et lie la personne a un autre utilisateur
                        PersonDAO.add(p,u1.getEmail());
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='update-Toto'").getSingleResult();


                        JPA.em().flush();

                        //suppression de la personne pour les deux utilisateurs
                        PersonDAO.updateNameFirstname(pbd.getIdPerson(),"update-email1@email","toto","titi");


                        //test si la personne n'existe plus
                        Query query = JPA.em().createQuery("Select p FROM Person p WHERE p.name='toto'");
                		Person pdebt = (Person) query.getSingleResult();
                        assertThat(pdebt.getName().equals("toto"));

                        //clean
                        JPA.em().remove(u1);
                        JPA.em().remove(p);
                    }
                });
            }
        });
    }
}