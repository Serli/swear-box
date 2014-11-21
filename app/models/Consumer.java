package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Représente un utilisateur du service
 * Il possède une liste de personnes
 *
 */
@Entity
public class Consumer implements Serializable{

    
	private static final long serialVersionUID = 1L;
	private static final int AMOUNTDEFAULT = 50;
	
	@Id
	private String email;
	
	@NotNull
	private int amount;
	
	@ManyToMany
	@JoinTable(name="U_P",
		    joinColumns = @JoinColumn(name = "idUser", 
		                              referencedColumnName = "email"), 
		    inverseJoinColumns = @JoinColumn(name = "idPerson", 
		                              referencedColumnName = "idPerson"))
	@JsonManagedReference
	List <Person> people;
	
	public Consumer() {
		this.people=new ArrayList <Person>();
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
