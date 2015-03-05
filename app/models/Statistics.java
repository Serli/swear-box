package models;

import java.io.Serializable;
import java.util.Date;



import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.jongo.marshall.jackson.oid.Id;


/**
 * Represents the pronunciation of a dirty word
 * Who uttered and when
 *
 */
public class Statistics implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id 
    private String _id;
    
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private Date date;

    private Person person;

    public Statistics() {
    }
    
    public Statistics(Date vDate, Person vPerson) {
        super();
        this.date = vDate;
        this.person = vPerson;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date vDate) {
        this.date = vDate;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person vPerson) {
        this.person = vPerson;
    }
}
