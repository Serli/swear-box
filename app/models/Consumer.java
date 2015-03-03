package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.MongoCollection;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Represents a service user
 * It has a list of people
 *
 */

@MongoCollection(name = "Consumer")
public class Consumer implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final int AMOUNTDEFAULT = 50;

    @Id
    private String email;

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private int amount;
    private List <Person> people;

    public Consumer() {
        this.people=new ArrayList<Person>();
        this.amount=AMOUNTDEFAULT;
    }

    public Consumer(String vEmail) {
        this();
        this.email = vEmail;
    }

    public Consumer(String vEmail, int vAmount) {
        this(vEmail);
        this.amount = vAmount;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String vEmail) {
        this.email = vEmail;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int vAmount) {
        this.amount = vAmount;
    }

    public List <Person> getPeople() {
        return this.people;
    }

    public void setPerson(Person vPerson) {
    	this.people.add(vPerson);
        
    }

}