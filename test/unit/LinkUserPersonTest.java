package unit;
import org.junit.*;

import dao.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;

/**
 * Test la classe DeletePerson
 *
 */
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
					
					//enregistrement des utilisateurs
					JPA.em().persist(u1);
					JPA.em().persist(u2);
					
					//ajout de la personne a un utilisateur
					AddPerson.addPerson(p,u1.getEmail());
					
					//recuperation de la personne
					Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Lier-Toto'").getSingleResult();
	
					//lie la personne a l'autre utilisateur
					LinkUserPerson .linkUserPerson(pbd.getIdPerson(),u2.getEmail());
				
					//regarde si les utilisateurs ont la personne dans sa list
					assertThat(u2.getPeople().get(0).getName()).isEqualTo(pbd.getName());
				
					
					//clean
					JPA.em().remove(u1);
					JPA.em().remove(u2);
					JPA.em().remove(pbd);
					}
				});
			}
		});
	}
	
	
}