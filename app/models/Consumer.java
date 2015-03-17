package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jongo.marshall.jackson.oid.Id;

/**
 * Represents a service user
 * It has a list of people
 */

public class Consumer implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final int AMOUNTDEFAULT = 50;

    @Id
    private String email;

    private int amount;
    
    private boolean blackListed;
    
    private boolean admin;
    
    private List <Person> people;

    public Consumer() {
        this.people=new ArrayList<Person>();
        this.amount=AMOUNTDEFAULT;
        this.blackListed = false;
        this.admin=false;
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

	public boolean isBlackListed() {
		return this.blackListed;
	}

	public void setBlackLister(boolean blackListed) {
		this.blackListed = blackListed;
	}
    
	public boolean isAdmin() {
		return this.admin;
	}

	public void setAdmin(boolean vAdmin) {
		this.admin = vAdmin;
	}
}