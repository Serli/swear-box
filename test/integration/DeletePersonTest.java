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
import dao.LinkUserPerson;
import dao.PersonDAO;
import dao.PersonDAOImpl;

/**
 * Test for DeletePerson function
 *
 */
public class DeletePersonTest{

    private PersonDAO personDAO = new PersonDAOImpl();
	
    /**
     * Test removal of a person for two users
     * Verification of the link between the two tables
     * Verification of the removal of the person in both the user and in the table U_P
     */
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
                        Consumer u2= new Consumer("Suppr-email2@email",50);

                        //Recording users
                        JPA.em().persist(u1);
                        Consumer u1bd=JPA.em().find(Consumer.class,"Suppr-email1@email");
                        JPA.em().persist(u2);
                        Consumer u2bd=JPA.em().find(Consumer.class,"Suppr-email2@email");

                        //Add the person and link it to a other user
                        personDAO.add(p,u1.getEmail());
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Suppr-Toto'").getSingleResult();
                        LinkUserPerson.linkUserPerson(pbd.getIdPerson(),u2.getEmail());

                        //Check if the users have the person in their list
                        assertThat(u1bd.getPeople().get(0).getName()).isEqualTo(p.getName());
                        assertThat(u1bd.getPeople().get(0).getName()).isEqualTo(pbd.getName());

                        assertThat(u2bd.getPeople().get(0).getName()).isEqualTo(p.getName());
                        assertThat(u2bd.getPeople().get(0).getName()).isEqualTo(pbd.getName());

                        //Check if the person has the users in his list
                        assertThat(pbd.getUsers().get(0).getEmail()).isEqualTo(u1.getEmail());
                        assertThat(pbd.getUsers().get(0).getEmail()).isEqualTo(u1bd.getEmail());
                        assertThat(pbd.getUsers().get(1).getEmail()).isEqualTo(u2.getEmail());
                        assertThat(pbd.getUsers().get(1).getEmail()).isEqualTo(u2bd.getEmail());		

                        JPA.em().flush();

                        //Delete the person for the two users
                        personDAO.delete(pbd.getIdPerson(),u1bd.getEmail());

                        //Get users from the DB after the delete
                        u1bd=JPA.em().find(Consumer.class,"Suppr-email1@email");
                        u2bd=JPA.em().find(Consumer.class,"Suppr-email2@email");

                        //Test if the person doesn't exist anymore
                        assertThat(u1bd.getPeople()).isEmpty();
                        assertThat(u2bd.getPeople()).isEmpty();

                        //Clean
                        JPA.em().remove(u1bd);
                        JPA.em().remove(u2bd);
                    }
                });
            }
        });
    }
}