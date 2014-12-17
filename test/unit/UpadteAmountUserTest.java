package unit;
import models.Consumer;
import play.db.jpa.JPA;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import org.junit.*;

import com.google.inject.Inject;

import dao.ConsumerDAO;


/**
 * Test UpadteAmountUser function
 * @author Geoffrey
 *
 */
public class UpadteAmountUserTest {

    @Inject
    private ConsumerDAO consumerDAO;

    
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


