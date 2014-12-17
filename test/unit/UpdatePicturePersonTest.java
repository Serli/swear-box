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
 * Test UpdatePicturePerson function
 *
 */
public class UpdatePicturePersonTest{

    @Inject
    private PersonDAO personDAO;
    
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

                        //enregistrement des utilisateurs
                        JPA.em().persist(u1);

                        //ajoute la personne et lie la personne a un autre utilisateur
                        personDAO.add(p,u1.getEmail());
                        Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='updateP-Toto'").getSingleResult();


                        JPA.em().flush();

                        //suppression de la personne pour les deux utilisateurs
                        personDAO.updatePicture(pbd.getIdPerson(),"updateP-email1@email","toto");


                        //test si la personne n'existe plus
                        Query query = JPA.em().createQuery("Select p FROM Person p WHERE p.name='updateP-Toto'");
                		Person pdebt = (Person) query.getSingleResult();
                        assertThat(pdebt.getPicture().equals("toto"));

                        //clean
                        JPA.em().remove(u1);
                        JPA.em().remove(p);
                    }
                });
            }
        });
    }
}