package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.vz.mongodb.jackson.DBRef;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.MongoCollection;

import org.codehaus.jackson.map.annotate.JsonSerialize;
/**
 * Represent a family member person
 * It is linked to one or more users
 *
 */

@MongoCollection(name = "Person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String idPerson;

    private String name;
    
    private String firstname;

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private int debt;

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
    private String picture;
    
    public List <DBRef<Consumer,String>> users;
    
    public Person() {
        this.users = new ArrayList<DBRef<Consumer,String>>();
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

    public List <DBRef<Consumer,String>> getUsers() {
        return this.users;
    }

    public void setUser(DBRef<Consumer,String> vUser) {
    	this.users.add(vUser);
    }
    
    //@ObjectId
    //@JsonProperty("_id")
	public String getIdPerson() {
		return idPerson;
	}
    
    //@ObjectId
    //@JsonProperty("_id")
	public void setIdPerson(String id) {
		this.idPerson = id;
	}
    

}