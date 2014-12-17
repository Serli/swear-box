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
 * Test UpadteAmountUser function
 * @author Geoffrey
 *
 */
public class UpdateAmountUserTest {

    private ConsumerDAO consumerDAO = new ConsumerDAOImpl();
    
    /**
     * Test adding an user
     */
    @Test
    public void updateAmountUser() {
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

                        consumerDAO.updateAmount(email, 20);
                        
                        //Recherche de l'utilisateur dans la base de données
                        Consumer u = JPA.em().find(Consumer.class, email);
                        assertThat(u.getAmount()==20);

                        JPA.em().remove(u);
                    }
                });
            }
        });
    }
}


