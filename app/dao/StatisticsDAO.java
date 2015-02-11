package dao;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Groups the operations on the Statistics table
 * @author Geoffrey
 *
 */
public interface StatisticsDAO {
    
    public void add(Long idPerson, String email);
	
    public ObjectNode list(String emailUser,ArrayList<Long> ids, int nb, int granularity);
}
