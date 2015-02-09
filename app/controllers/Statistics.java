package controllers;

import java.util.List;

import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.StatisticsDAO;

/**
 * Managed actions for administration view
 *
 */
@Singleton
public class Statistics extends JavaController {

    private static final String JSON_MESSG = "Expecting Json data";

    @Inject
    private StatisticsDAO statisticsDAO;

    /**
     * List all persons for a connected user
     * @return Result(JSON) : list of membres
     */
    @Transactional(readOnly=true)
    @RequiresAuthentication(clientName = "Google2Client")
    public Result listStatistics() {
        String emailUser = getUserProfile().getEmail();
        List<models.Statistics> statistics = statisticsDAO.listByUser(emailUser);
        return ok(Json.toJson(statistics));
    }

}
