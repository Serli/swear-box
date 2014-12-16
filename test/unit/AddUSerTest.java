package unit;
import models.Consumer;
import play.db.jpa.JPA;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import org.junit.*;

import dao.ConsumerDAO;


/**
 * Test for AddUser function
 * @author Geoffrey
 *
 */
public class AddUserTest {

    /**
     * test AddUser for an user
     */
    @Test
    public void addUser() {
        running(fakeApplication(inMemoryDatabase()), new Runnable()
        {
            public void run()
            {
                JPA.withTransaction(new play.libs.F.Callback0()
                {
                    public void invoke()
                    {
                        //Ajout d'un utilisateur à partir d'un email
                        String email = "test@gmail.com";
                        ConsumerDAO.add(email);

                        //Recherche de l'utilisateur dans la base de données
                        Consumer u = JPA.em().find(Consumer.class, email);
                        assertThat(u).isNotEqualTo(null);

                        JPA.em().remove(u);
                    }
                });
            }
        });
    }
}


