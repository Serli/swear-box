package models;

import java.io.Serializable;



/**
 * Represent a family member person
 * It is linked to one users
 */
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    private String idPerson;

    private String name;
    
    private String firstname;

    private int debt;

    private String picture;
    
    
    public Person() {
    }
 
    public Person(String idPerson, String vName, String vFirstName, int vDebt, String vPicture) {
        this();
        this.idPerson = idPerson;
        this.name = vName;
        this.firstname = vFirstName;
        this.debt = vDebt;
        this.picture = vPicture;
    }
   
    public String getName() {
        return this.name;
    }

    public void setName(String vName) {
        this.name = vName;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String vFirstName) {
        this.firstname = vFirstName;
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
    
	public String getIdPerson() {
		return idPerson;
	}
    
	public void setIdPerson(String id) {
		this.idPerson = id;
	}
    

}