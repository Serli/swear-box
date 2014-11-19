package models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Statistique implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id_Statistique;
	
	@NotNull
	@Column (name="Date_St")
	private Date date;
	
	@ManyToOne
	@JoinColumn(name="id_Personne")
	private Personne personne;
	
	public Statistique(Date date, Personne personne) {
		super();
		this.date = date;
		this.personne = personne;
	}
	public Long getId_Statistique() {
		return id_Statistique;
	}
	public void setId_Statistique(Long id_Statistique) {
		this.id_Statistique = id_Statistique;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Personne getPersonne() {
		return personne;
	}
	public void setPersonne(Personne personne) {
		this.personne = personne;
	}


}
