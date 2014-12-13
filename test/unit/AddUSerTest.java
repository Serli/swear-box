package unit;
import models.Consumer;
import play.db.jpa.JPA;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import org.junit.*;

import dao.AddUser;


/**
 * Test la classe AddUser
 * @author Geoffrey
 *
 */
public class AddUserTest {

	/**
	 * Test l'ajout d'un utilisateur dans la base de données 
	 */
    @Test
    public void ajoutUtilisateur() {
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
    	        	  AddUser.addUser(email);
    	        	  
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


