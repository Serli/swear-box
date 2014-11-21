package unit;
import org.junit.*;

import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

/**
 * Test le bon fonctionnement du lancement de l'appli sur un serveur
 * @author Geoffrey
 *
 */
public class IntegrationTest {

    /**
     * Test si l'index se lance et s'affiche dans un navigateur
     * 
     */
    @Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                assertThat(browser.pageSource()).contains("Connexion");
            }
        });
    }

}
