package models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Statistics implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idStatistics;
	
	@NotNull
	@Column (name="dateSt")
	private Date date;
	
	@ManyToOne
	@JoinColumn(name="idPerson")
	private Person person;
	
	public Statistics(Date vDate, Person vPerson) {
		super();
		this.date = vDate;
		this.person = vPerson;
	}
	public Long getIdStatistics() {
		return idStatistics;
	}
	public void setIdStatistics(Long vIdStatistics) {
		this.idStatistics = vIdStatistics;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date vDate) {
		this.date = vDate;
	}
	public Person getPerson() {
		return person;
	}
	public void setIdPerson(Person vPersonn) {
		this.person = vPersonn;
	}


}