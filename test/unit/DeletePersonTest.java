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
                        personDAO.add(p,u1.getEmail());
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Suppr-Toto'").getSingleResult();


                        JPA.em().flush();

                        //suppression de la personne pour les deux utilisateurs
                        personDAO.delete(pbd.getIdPerson(),"Suppr-email1@email");


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