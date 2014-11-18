package models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;



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
	@NotEmpty
	private int Montant;
	
	@JoinTable(name="U_P",
		    joinColumns = @JoinColumn(name = "id_Utilisateur", 
		                              referencedColumnName = "Email"), 
		    inverseJoinColumns = @JoinColumn(name = "id_Personne", 
		                              referencedColumnName = "id_Personne"))
	@ManyToMany
	List <Personne> personnes;
	
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

}
