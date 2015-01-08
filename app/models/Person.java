package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Represent a family member person
 * It is linked to one or more users
 *
 */
@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPerson;

    @Size(max = 50)
    private String name;

    @NotEmpty
    @Size(max = 50)
    private String firstname;

    @NotNull
    private int debt;

    @NotEmpty
    private String picture;

    @ManyToMany(mappedBy = "people")
    @JsonBackReference
    private List<Consumer> users;

    public Person() {
        this.users = new ArrayList<Consumer>();
    }

    public Person(String vName, String vFirstName, int vDebt, String vPicture) {
        this();
        this.name = vName;
        this.firstname = vFirstName;
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

    public List<Consumer> getUsers() {
        return this.users;
    }

    public void setUser(Consumer vUser) {
        this.users.add(vUser);
    }
}
