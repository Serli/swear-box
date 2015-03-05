package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jongo.marshall.jackson.oid.Id;

import com.mongodb.BasicDBObject;

/**
 * Represents a service user
 * It has a list of people
 *
 */

public class ResultAgreg implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private BasicDBObject personId;
    
    //private List<String> vdate;
    
    private int click;

    public ResultAgreg() {
    }

	  public BasicDBObject getPersonId() {
		return personId;
	}

	public void setPersonId(BasicDBObject personId) {
		this.personId = personId;
	}

	/*public List<String> getVDate() {
		  return this.vdate;
	  }
	  public void setVDate(List<String> date) {
		  this.vdate = date;
	  }*/
	  public int getClick() {
		  return this.click;
	  }
	  public void setClick(int clk) {
		  this.click = clk;
	  }

}