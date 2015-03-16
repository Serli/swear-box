package dao;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Groups the operations on the Statistics collection
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
	 * @return JsonNode : stats
	 */
    public JsonNode list(List<String> ids, int nb, int granularity);
    
	/**
	 * List all the statistics
	 * @return JsonNode : stats
	 */
    public JsonNode list();
    
    /**
     * List different stats used in the back-office
	 * @return JsonNode : specifics stats
     */
    public JsonNode someStats();
}
