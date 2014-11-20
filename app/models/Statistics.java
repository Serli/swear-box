package models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


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
