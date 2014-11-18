package models;

import java.io.Serializable;
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
@Table (name ="Utilisateur")
public class Utilisateur implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String Email;
	
	@NotNull
	private int Montant;
	
	@JoinTable(name="U_P",
		    joinColumns = @JoinColumn(name = "id_Utilisateur", 
		                              referencedColumnName = "Email"), 
		    inverseJoinColumns = @JoinColumn(name = "id_Personne", 
		                              referencedColumnName = "id_Personne"))
	@ManyToMany
	List <Personne> personnes;
	
	public Utilisateur() {
	}
	
	public Utilisateur(String email) {
		Email = email;
		Montant = 50;
	}
	
	public Utilisateur(String email, int montant) {
		Email = email;
		Montant = montant;
	}
	
	public String getEmail() {
		return Email;
	}
	
	public void setEmail(String email) {
		Email = email;
	}
	
	public int getMontant() {
		return Montant;
	}
	
	public void setMontant(int montant) {
		Montant = montant;
	}
	
	public List <Personne> getPersonnes() {
		return personnes;
	}
	public void setPersonnes(Personne p) {
		personnes.add(p);
	}

}
