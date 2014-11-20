package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;



import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Consumer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String email;
	
	@NotNull
	private int amount;
	
	@JoinTable(name="U_P",
		    joinColumns = @JoinColumn(name = "idUser", 
		                              referencedColumnName = "email"), 
		    inverseJoinColumns = @JoinColumn(name = "idPerson", 
		                              referencedColumnName = "idPerson"))
	@ManyToMany
	List <Person> people;
	
	public Consumer() {
		this.people=new ArrayList <Person>();
	}
	
	public Consumer(String vEmail) {
		this();
		this.email = vEmail;
		this.amount = 50;
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
