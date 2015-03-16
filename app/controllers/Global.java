package controllers;
import org.pac4j.core.client.Clients;
import org.pac4j.oauth.client.Google2Client;
import org.pac4j.play.Config;

import play.Application;
import play.GlobalSettings;
import play.Play;

import com.cloudinary.Cloudinary;
import com.cloudinary.SingletonManager;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import dao.ConsumerDAO;
import dao.ConsumerDAOImpl;
import dao.PersonDAO;
import dao.PersonDAOImpl;
import dao.StatisticsDAO;
import dao.StatisticsDAOImpl;

/**
 * Performs tasks in launching the application
 *
 */
public class Global extends GlobalSettings{

    private Injector injector;
    
    /**
     * Add a user for an authentification  (OAuth2)
     */
    @Override
    public void onStart(final Application app) {
        super.onStart(app);

        //Get datas needed in the file application.conf
        final String googleId = Play.application().configuration().getString("GoogleId");
        final String googleSecret = Play.application().configuration().getString("GoogleSecret");
        final String baseUrl = Play.application().configuration().getString("baseUrl");

        //Create a client Google2Client for the Google connection with OAuth2
        final Google2Client google2Client = new Google2Client(googleId, googleSecret);

        //Add clients to Config
        final Clients clients = new Clients(baseUrl + "/oauth2callback", google2Client);
        Config.setClients(clients);
        
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ConsumerDAO.class).to(ConsumerDAOImpl.class);
                bind(PersonDAO.class).to(PersonDAOImpl.class);
                bind(StatisticsDAO.class).to(StatisticsDAOImpl.class);
            }
        });
        
        final String cloudUrl = System.getenv("CLOUDINARY_URL");
        
        SingletonManager manager = new SingletonManager();
        manager.setCloudinary(new Cloudinary(cloudUrl));
        manager.init();
    }

    @Override
    public <T> T getControllerInstance(Class<T> aClass) throws Exception {
        return injector.getInstance(aClass);
    }

}
