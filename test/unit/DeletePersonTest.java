package unit;
import java.util.List;

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
public class DeletePersonTest{

    /**
     * Test suppression d'une personne
     */
    @Transactional
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

                        //enregistrement des utilisateurs
                        JPA.em().persist(u1);

                        //ajoute la personne et lie la personne a un autre utilisateur
                        PersonDAO.add(p,u1.getEmail());
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Suppr-Toto'").getSingleResult();


                        JPA.em().flush();

                        //suppression de la personne pour les deux utilisateurs
                        PersonDAO.delete(pbd.getIdPerson(),"Suppr-email1@email");


                        //test si la personne n'existe plus
                        List<Person> lp= JPA.em().createQuery("Select p FROM Person p WHERE p.name='Suppr-Toto'",Person.class).getResultList();
                        assertThat(lp).isEmpty();

                        //clean
                        JPA.em().remove(u1);
                    }
                });
            }
        });
    }
}