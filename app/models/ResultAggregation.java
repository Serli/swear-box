package models;

import java.io.Serializable;

import org.jongo.marshall.jackson.oid.Id;

import com.mongodb.BasicDBObject;

/**
 * Represents a aggregate result in StatisticDAOImpl
 *
 */
public class ResultAggregation implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	private BasicDBObject personId;

	private int click;

	public ResultAggregation() {
	}

	public BasicDBObject getPersonId() {
		return personId;
	}

	public void setPersonId(BasicDBObject personId) {
		this.personId = personId;
	}

	public int getClick() {
		return this.click;
	}
	public void setClick(int clk) {
		this.click = clk;
	}

}