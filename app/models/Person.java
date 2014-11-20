package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Person implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idPerson;
	
	@NotEmpty
	@Size(max =50)
	private String name;
	
	@NotEmpty
	@Size(max =50)
	private String firstName;
	
	@NotNull
	private int debt;
	
	@NotEmpty
	private String picture;
	
	@ManyToMany(mappedBy="people")
	private List<User> users;
	
	public Person() {
		this.users=new ArrayList <User>();
	}
	
	public Person(String vName, String vFirstName, int vDebt, String vPicture) {
		this();
		this.name = vName;
		this.firstName = vFirstName;
		this.debt = vDebt;
		this.picture = vPicture;
	}

	public Long getIdPerson() {
		return this.idPerson;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String vName) {
		this.name = vName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String vFirstName) {
		this.firstName = vFirstName;
	}

	public int getDebt() {
		return this.debt;
	}

	public void setDebt(int vDebt) {
		this.debt = vDebt;
	}
	
	public String getPicture() {
		return this.picture;
	}

	public void setAdrImage(String vPicture) {
		this.picture = vPicture;
	}
	
	public List<User> getUsers(){
		return this.users;
	}
	
	public void setUser(User vUser){
		this.users.add(vUser);
	}
}
