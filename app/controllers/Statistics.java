package controllers;

import java.util.ArrayList;

import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.StatisticsDAO;

/**
 * Managed actions for administration view
 *
 */
@Singleton
public class Statistics extends JavaController {

    @Inject
    private StatisticsDAO statisticsDAO;

    /**
     * List data stats for a connected user and a members list 
     * @return Result(JSON) : list of data stats
     */
    @RequiresAuthentication(clientName = "Google2Client")
    public Result listStatistics(String ids, int nb, int granularity) {
    	String[] idsString = ids.split(",");
    	ArrayList<String> idsLong = new ArrayList<String>();
    	for(int i=0;i<idsString.length;i++) {
    		idsLong.add(idsString[i]);
    	}
        String emailUser = getUserProfile().getEmail();
        JsonNode statistics = statisticsDAO.list(emailUser,idsLong,nb,granularity);
        return ok(statistics);
    }
    
    @RequiresAuthentication(clientName = "Google2Client")
    public Result list() {
        return ok(statisticsDAO.list());
    }

}
