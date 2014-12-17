package unit;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import models.Consumer;

import org.junit.Test;

import play.db.jpa.JPA;
import dao.ConsumerDAO;
import dao.ConsumerDAOImpl;


/**
 * Test for AddUser function
 * @author Geoffrey
 *
 */
public class AddUserTest {

	private ConsumerDAO consumerDAO = new ConsumerDAOImpl();
	
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
                        consumerDAO.add(email);

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


