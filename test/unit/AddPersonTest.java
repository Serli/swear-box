package unit;

import org.junit.*;

import com.google.inject.Inject;

import dao.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;

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

                        //enregistrement de l'utilisateur
                        JPA.em().persist(u);

                        //ajout de la personne
                        personDAO.add(p,u.getEmail());

                        //recuperation de la personne en bd
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Toto'").getSingleResult();

                        //regarde si l'utilisateur a la personne dans sa list
                        assertThat(pbd).isNotEqualTo(null);

                        JPA.em().remove(u);
                        JPA.em().remove(pbd);
                    }
                });
            }
        });
    }

}