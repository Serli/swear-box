package unit;
import org.junit.*;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;
import services.*;


public class LinkUserPersonTest{

	/**
	* Test LierPersonneUtilisateur
	*/
	@Transactional
	@Test
    public void test() {
		running(fakeApplication(inMemoryDatabase()), new Runnable()
    	{
    	    public void run()
    	   {
				JPA.withTransaction(new play.libs.F.Callback0()
    	        {
					public void invoke()
					{
						
					Person p =new Person("Lier-Toto", "Lier-Titi",0,"yolo");
					Consumer u1= new Consumer("Lier-email1@email",100);
					Consumer u2= new Consumer("Lier-email2@email",50);
					
					//enregistrement des utilisateurs et recuperation de ceux en bd
					JPA.em().persist(u1);
					Consumer u1bd=JPA.em().find(Consumer.class,"Lier-email1@email");
					JPA.em().persist(u2);
					Consumer u2bd=JPA.em().find(Consumer.class,"Lier-email2@email");
					
					//ajout de la personne a un utilisateur
					AddPerson.addPerson(p,u1.getEmail());
					
					//recuperation de la personne
					Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Lier-Toto'").getSingleResult();
	
					//lie la personne a l'autre utilisateur
					LinkUserPerson .linkUserPerson(pbd.getIdPerson(),u2.getEmail());
				
					//regarde si les utilisateurs ont la personne dans sa list
					assertThat(u1bd.getPeople().get(0).getName()).isEqualTo(p.getName());
					assertThat(u1bd.getPeople().get(0).getName()).isEqualTo(pbd.getName());
					
					assertThat(u2bd.getPeople().get(0).getName()).isEqualTo(p.getName());
					assertThat(u2bd.getPeople().get(0).getName()).isEqualTo(pbd.getName());
					
					//regarde si la personne a les utilisateurs dans sa list
					assertThat(pbd.getUsers().get(0).getEmail()).isEqualTo(u1.getEmail());
					assertThat(pbd.getUsers().get(0).getEmail()).isEqualTo(u1bd.getEmail());
					assertThat(pbd.getUsers().get(1).getEmail()).isEqualTo(u2.getEmail());
					assertThat(pbd.getUsers().get(1).getEmail()).isEqualTo(u2bd.getEmail());
					
					//clean
					JPA.em().remove(u1bd);
					JPA.em().remove(u2bd);
					JPA.em().remove(pbd);
					}
				});
			}
		});
	}
	
	
}