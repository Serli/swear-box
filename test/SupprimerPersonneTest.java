
import javax.persistence.Query;

import org.junit.*;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;
import services.*;


public class SupprimerPersonneTest{

	/**
	* Test Ajout de personne pour un utilisateur
	* Verification de la liaison entre les deux tables 
	*(par interpolation la création de la table personne)
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
						Personne p =new Personne("Suppr-Toto", "Suppr-Titi",0,"yolo");
						Utilisateur u1= new Utilisateur("Suppr-email1@email",100);
						Utilisateur u2= new Utilisateur("Suppr-email2@email",50);
						//enregistrement de la personne
						JPA.em().persist(u1);
						Utilisateur u1bd=JPA.em().find(Utilisateur.class,"Suppr-email1@email");
						JPA.em().persist(u2);
						Utilisateur u2bd=JPA.em().find(Utilisateur.class,"Suppr-email2@email");
						
						AjoutPersonne.ajoutPersonne(p,u1.getEmail());
						LierPersonneUtilisateur .lierPersonneUtilisateur(p,u2.getEmail());
						Personne pbd= (Personne)JPA.em().createQuery("Select p FROM Personne p WHERE p.Nom='Suppr-Toto'").getSingleResult();
						
						//regarde si l'utilisateur a la personne dans sa list
						assertThat(u1bd.getPersonnes().get(0).getNom()).isEqualTo(p.getNom());
						assertThat(u1bd.getPersonnes().get(0).getNom()).isEqualTo(pbd.getNom());
						
						assertThat(u2bd.getPersonnes().get(0).getNom()).isEqualTo(p.getNom());
						assertThat(u2bd.getPersonnes().get(0).getNom()).isEqualTo(pbd.getNom());
						
						//regarde si la personne a l'utilisateur dans sa list
						assertThat(pbd.getUtilisateurs().get(0).getEmail()).isEqualTo(u1.getEmail());
						assertThat(pbd.getUtilisateurs().get(0).getEmail()).isEqualTo(u1bd.getEmail());
						assertThat(pbd.getUtilisateurs().get(1).getEmail()).isEqualTo(u2.getEmail());
						assertThat(pbd.getUtilisateurs().get(1).getEmail()).isEqualTo(u2bd.getEmail());		
	
						JPA.em().flush();
						
						SupprimerPersonne.supprimerPersonne("Suppr-Toto", "Suppr-Titi", "Suppr-email1@email");
						
						u1bd=JPA.em().find(Utilisateur.class,"Suppr-email1@email");
						u2bd=JPA.em().find(Utilisateur.class,"Suppr-email2@email");
						
						assertThat(u1bd.getPersonnes()).isEmpty();
						assertThat(u2bd.getPersonnes()).isEmpty();
						
						JPA.em().remove(u1bd);
						JPA.em().remove(u2bd);
					}
				});
			}
		});
	}
	
	
}