package unit;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;

import org.junit.Test;

import play.twirl.api.Content;
/**
 *
 * Test actions of Controler Welcome.java
 *
 */
public class WelcomeTest {


    /**
     * Test if the message passed as a parameter in the call connection is present views
     */
    @Test
    public void renderTemplateIndex() {
        Content html = views.html.index.render("Message test",0,0);
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Message test");
    }

    /**
     * Test if the message passed as a parameter when calling the user view is present
     */
    @Test
    public void renderTemplateUser() {
        Content html = views.html.user.render("Message test",1,0);
        assertThat(contentType(html)).isEqualTo("text/html");
    }

}
