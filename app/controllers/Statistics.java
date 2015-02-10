package controllers;

import java.util.ArrayList;

import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.db.jpa.Transactional;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
    @Transactional(readOnly=true)
    @RequiresAuthentication(clientName = "Google2Client")
    public Result listStatistics(String ids, int nb, int granularity) {
    	String[] idsString = ids.split(",");
    	ArrayList<Long> idsLong = new ArrayList<Long>();
    	for(int i=0;i<idsString.length;i++) {
    		idsLong.add(Long.parseLong(idsString[i]));
    	}
        String emailUser = getUserProfile().getEmail();
        ObjectNode statistics = statisticsDAO.list(emailUser,idsLong,nb,granularity);
        return ok(statistics);
    }

}
