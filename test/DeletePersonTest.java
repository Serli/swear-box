import org.junit.*;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;
import services.*;

/**
 * Test la classe DeletePerson
 *
 */
public class DeletePersonTest{

	/**
	* Test suppression d'une personne pour deux utilisateurs
	* Verification de la liaison entre les deux tables
	* Vérification de la suppression de la personne dans les deux utilisateur et dans la table U_P
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
						Person p =new Person("Suppr-Toto", "Suppr-Titi",0,"yolo");
						Consumer u1= new Consumer("Suppr-email1@email",100);
						Consumer u2= new Consumer("Suppr-email2@email",50);
						
						//enregistrement des utilisateurs
						JPA.em().persist(u1);
						Consumer u1bd=JPA.em().find(Consumer.class,"Suppr-email1@email");
						JPA.em().persist(u2);
						Consumer u2bd=JPA.em().find(Consumer.class,"Suppr-email2@email");
						
						//ajoute la personne et lie la personne a un autre utilisateur
						AddPerson.addPerson(p,u1.getEmail());
						Person pbd= (Person)JPA.em().createQuery("Select p FROM Person p WHERE p.name='Suppr-Toto'").getSingleResult();
						LinkUserPerson.linkUserPerson(pbd.getIdPerson(),u2.getEmail());
												
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
	
						JPA.em().flush();
						
						//suppression de la personne pour les deux utilisateurs
						DeletePerson.deletePerson(pbd.getIdPerson());
						
						//recuperation des utilisateurs en bd apres suppression
						u1bd=JPA.em().find(Consumer.class,"Suppr-email1@email");
						u2bd=JPA.em().find(Consumer.class,"Suppr-email2@email");
						
						//test si la personne n'existe plus
						assertThat(u1bd.getPeople()).isEmpty();
						assertThat(u2bd.getPeople()).isEmpty();
						
						//clean
						JPA.em().remove(u1bd);
						JPA.em().remove(u2bd);
					}
				});
			}
		});
	}
	
	
}