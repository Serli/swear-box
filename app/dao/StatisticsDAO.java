package dao;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Groups the operations on the Statistics table
 * @author Geoffrey
 *
 */
public interface StatisticsDAO {
    
	/**
	 * Add a statistic on the Statistics collection
	 * @param idPerson : person who swore
	 */
    public void add(String idPerson, String email);
	
	/**
	 * List the data to display statistics in the view
	 * @param emailUser : user id
	 * @param ids : members id
	 * @param nb : number of data
	 * @param granularity : 1 = Week, 2 = Month
	 */
    public JsonNode list(String emailUser,ArrayList<String> ids, int nb, int granularity);
}
