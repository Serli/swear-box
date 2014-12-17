package integration;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import models.Consumer;
import models.Person;

import org.junit.Test;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import dao.PersonDAO;
import dao.PersonDAOImpl;

/**
 * Test for AddPerson function
 *
 */
public class AddPersonTest{

    private PersonDAO personDAO = new PersonDAOImpl();
	
    /**
     * Test for AddPerson function
     * Verification of the link between the two tables
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
                        Consumer ubd=JPA.em().find(Consumer.class,"email@email");
                        assertThat(ubd.getEmail()).isEqualTo(u.getEmail());

                        //ajout de la personne
                        personDAO.add(p,u.getEmail());

                        //recuperation de la personne en bd
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Toto'").getSingleResult();

                        //regarde si l'utilisateur a la personne dans sa list
                        assertThat(ubd.getPeople().get(0).getName()).isEqualTo(p.getName());
                        assertThat(ubd.getPeople().get(0).getName()).isEqualTo(pbd.getName());

                        //regarde si la personne a l'utilisateur dans sa list
                        assertThat(pbd.getUsers().get(0).getEmail()).isEqualTo(u.getEmail());
                        assertThat(pbd.getUsers().get(0).getEmail()).isEqualTo(ubd.getEmail());
                        JPA.em().remove(ubd);
                        JPA.em().remove(pbd);
                    }
                });
            }
        });
    }


}