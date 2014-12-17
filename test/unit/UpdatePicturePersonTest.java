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
 * Test UpdatePicturePerson function
 *
 */
public class UpdatePicturePersonTest{

    private PersonDAO personDAO = new PersonDAOImpl();
    
    /**
     * Test updating the picture path for a person
     */
    @Transactional
    @Test
    public void updatePicturePerson() {
        running(fakeApplication(inMemoryDatabase()), new Runnable()
        {
            public void run()
            {
                JPA.withTransaction(new play.libs.F.Callback0()
                {
                    public void invoke()
                    {
                        Person p =new Person("updateP-Toto", "updateP-Titi",0,"yolo");
                        Consumer u1= new Consumer("updateP-email1@email",100);

                        //Recording users
                        JPA.em().persist(u1);

                        //Add the person and link it to an other user
                        personDAO.add(p,u1.getEmail());
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='updateP-Toto'").getSingleResult();


                        JPA.em().flush();

                        //Update the picture the person
                        personDAO.updatePicture(pbd.getIdPerson(),"updateP-email1@email","toto");


                        //Test the picture is updated
                        Query query = JPA.em().createQuery("Select p FROM Person p WHERE p.name='updateP-Toto'");
                		Person pdebt = (Person) query.getSingleResult();
                        assertThat(pdebt.getPicture().equals("toto"));

                        //Clean
                        JPA.em().remove(u1);
                        JPA.em().remove(p);
                    }
                });
            }
        });
    }
}