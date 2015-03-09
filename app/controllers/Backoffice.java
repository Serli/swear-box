package controllers;

import java.util.ArrayList;

import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.ConsumerDAO;

/**
 * Managed actions for administration view
 *
 */
@Singleton
public class Backoffice extends JavaController {

    @Inject
    private ConsumerDAO consumerDAO;

    @RequiresAuthentication(clientName = "Google2Client")
    public Result findAll() {
        return ok(consumerDAO.findAll());
    }

    @RequiresAuthentication(clientName = "Google2Client")
    public Result findOne(String email) {
        return ok(consumerDAO.findOne(email));
    }

}
