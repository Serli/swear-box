package dao;

import java.util.List;

import models.Statistics;

/**
 * Groups the operations on the Statistics table
 * @author Geoffrey
 *
 */
public interface StatisticsDAO {
    
    public void add(Long idPerson);
	
    public List<Statistics> listByUser(String emailUser);
}
