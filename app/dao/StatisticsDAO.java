package dao;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Groups the operations on the Statistics table
 * @author Geoffrey
 *
 */
public interface StatisticsDAO {
    
    public void add(String idPerson, String email);
	
    public ObjectNode list(String emailUser,ArrayList<String> ids, int nb, int granularity);
}
