package controllers;

import java.util.ArrayList;
import java.util.List;

import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;

import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.ConsumerDAO;
import dao.StatisticsDAO;

/**
 * Managed actions for administration view
 *
 */
@Singleton
public class Statistics extends JavaController {

	@Inject
	private StatisticsDAO statisticsDAO;

	@Inject
	private ConsumerDAO consumerDAO;
	
	/**
	 * List data stats for a connected user and a members list 
	 * @return Result(JSON) : list of data stats
	 */
	@RequiresAuthentication(clientName = "Google2Client")
	public Result listStatistics(String ids, int nb, int granularity) {
		if(isAuthorized()){
			String[] idsString = ids.split(",");
			List<String> idsLong = new ArrayList<String>();
			for(int i=0;i<idsString.length;i++) {
				idsLong.add(idsString[i]);
			}
			JsonNode statistics = statisticsDAO.list(idsLong,nb,granularity);
			return ok(statistics);
		}
		return badRequest();
	}

	/**
	 * get authorization
	 * @return boolean : true = access; false = denied
	 */
	private boolean isAuthorized(){
		String email = getUserProfile().getEmail();
		return !consumerDAO.inBlackLister(email);
	}

}
