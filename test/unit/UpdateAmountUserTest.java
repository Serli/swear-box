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
                        //Add an user from an email
                        String email = "test@gmail.com";
                        consumerDAO.add(email);

                        consumerDAO.updateAmount(email, 20);
                        
                        //Seek the user in the DB
                        Consumer u = JPA.em().find(Consumer.class, email);
                        //Check if the amount is modified
                        assertThat(u.getAmount()==20);

                        JPA.em().remove(u);
                    }
                });
            }
        });
    }
}


