import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import models.Utilisateur;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;
import services.AjoutUtilisateur;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;



/**
 * Test la classe AjoutUtilisateur
 * @author Geoffrey
 *
 */
public class AjoutUtilisateurTest {

	/**
	 * Test l'ajout d'un utilisateur dans la base de données 
	 */
    @Test
    public void ajoutUtilisateur() {
    	    running(fakeApplication(), new Runnable()
    	    {
    	      public void run()
    	      {
    	        JPA.withTransaction(new play.libs.F.Callback0()
    	        {
    	          public void invoke()
    	          {
    	        	  String email = "test@gmail.com";
    	        	  AjoutUtilisateur.ajoutUtilisateur(email);
       	           	  Utilisateur u = JPA.em().find(Utilisateur.class, email);
       	           	  assertThat(u).isNotEqualTo(null);
    	          }
    	        });
    	      }
    	    });
    }
}


