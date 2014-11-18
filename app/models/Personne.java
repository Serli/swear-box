package models;

import java.io.Serializable;
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
@Table (name ="Personne")
public class Personne implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_Personne")
	private Long id_personne;
	
	@Column(name = "Nom")
	@NotEmpty
	@Size(max =50)
	private String Nom;
	
	@NotEmpty
	@Size(max =50)
	@Column (name = "Prenom")
	private String Prenom;
	
	@Column (name ="Dette")
	@NotNull
	private int Dette;
	
	@Column (name ="adr_Image")
	@NotEmpty
	private String adrImage;
	
	@ManyToMany(mappedBy="personnes")
	private List<Utilisateur> utilisateurs;
	
	public Personne(String nom, String prenom, int dette) {
		super();
		Nom = nom;
		Prenom = prenom;
		Dette = dette;
	}

	public Long getId_personne() {
		return id_personne;
	}

	public void setId_personne(Long id_personne) {
		this.id_personne = id_personne;
	}

	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}

	public String getPrenom() {
		return Prenom;
	}

	public void setPrenom(String prenom) {
		Prenom = prenom;
	}

	public int getDette() {
		return Dette;
	}

	public void setDette(int dette) {
		Dette = dette;
	}
	
}
