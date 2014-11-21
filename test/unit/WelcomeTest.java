package unit;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;

import org.junit.Test;

import play.twirl.api.Content;
/**
*
* Test les actions du Contrôleur Welcome.java
*
*/
public class WelcomeTest {


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
