import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Test les actions du Controleur Accueil.java
*
*/
public class AccueilTest {


	/**
	 * Test si le message passé en paramètre lors de l'appel de la vue connexion est bien présent 
	 */
    @Test
    public void renderTemplateIndex() {
        Content html = views.html.index.render("Message test");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Message test");
    }

    /**
     * Test si le message passé en paramètre lors de l'appel de la vue user est bien présent
     */
    @Test
    public void renderTemplateUser() {
        Content html = views.html.user.render("Message test");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Message test");
    }

}
