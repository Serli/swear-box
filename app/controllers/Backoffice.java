package controllers;

import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.mvc.Result;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.ConsumerDAO;
import dao.StatisticsDAO;

/**
 * Managed actions for administration view
 *
 */
@Singleton
public class Backoffice extends JavaController {

    @Inject
    private ConsumerDAO consumerDAO;
    
    @Inject
    private StatisticsDAO statisticsDAO;

    @RequiresAuthentication(clientName = "Google2Client")
    public Result findAll() {
        return ok(consumerDAO.findAll());
    }

    @RequiresAuthentication(clientName = "Google2Client")
    public Result findOne(String email) {
        return ok(consumerDAO.findOne(email));
    }
    
    @RequiresAuthentication(clientName = "Google2Client")
    public Result setAdmin(String email) {
    	String admin = getUserProfile().getEmail();
        if(consumerDAO.setAdmin(admin, email))
        	return ok();
        return badRequest();
    }

    @RequiresAuthentication(clientName = "Google2Client")
    public Result setBlacklisted(String email) {
    	String admin = getUserProfile().getEmail();
        if(consumerDAO.setBlacklisted(admin, email,true))
        	return ok();
        return badRequest();
    }
    
    @RequiresAuthentication(clientName = "Google2Client")
    public Result unsetBlacklisted(String email) {
    	String admin = getUserProfile().getEmail();
        if(consumerDAO.setBlacklisted(admin, email,false))
        	return ok();
        return badRequest();
    }
    
    @RequiresAuthentication(clientName = "Google2Client")
    public Result list() {
        return ok(statisticsDAO.list());
    }
    
    @RequiresAuthentication(clientName = "Google2Client")
    public Result someStats() {
        return ok(statisticsDAO.someStats());
    }

}
