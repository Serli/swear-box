package integration;
import org.junit.*;

import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

/**
 * Test the functioning of launching the app on a server
 * @author Geoffrey
 *
 */
public class IntegrationTest {

    /**
     * Test if the index starts and is displayed in a browser
     * 
     */
    @Test
    public void applicationLaunch() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                assertThat(browser.pageSource()).contains("Connexion");
            }
        });
    }

}
