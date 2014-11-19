
import javax.persistence.Query;

import org.junit.*;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;
import services.*;


public class LierPersonneUtilisateurTest{

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
						
					Personne p =new Personne("Lier-Toto", "Lier-Titi",0,"yolo");
					Utilisateur u1= new Utilisateur("Lier-email1@email",100);
					Utilisateur u2= new Utilisateur("Lier-email2@email",50);
					
					//enregistrement des utilisateurs et recuperation de ceux en bd
					JPA.em().persist(u1);
					Utilisateur u1bd=JPA.em().find(Utilisateur.class,"Lier-email1@email");
					JPA.em().persist(u2);
					Utilisateur u2bd=JPA.em().find(Utilisateur.class,"Lier-email2@email");
					
					//ajout de la personne a un utilisateur
					AjoutPersonne.ajoutPersonne(p,u1.getEmail());
					
					//lie la personne a l'autre utilisateur
					LierPersonneUtilisateur .lierPersonneUtilisateur(p,u2.getEmail());
					
					//recuperation de la personne
					Personne pbd= (Personne)JPA.em().createQuery("Select p FROM Personne p WHERE p.Nom='Lier-Toto'").getSingleResult();
					
					//regarde si les utilisateurs ont la personne dans sa list
					assertThat(u1bd.getPersonnes().get(0).getNom()).isEqualTo(p.getNom());
					assertThat(u1bd.getPersonnes().get(0).getNom()).isEqualTo(pbd.getNom());
					
					assertThat(u2bd.getPersonnes().get(0).getNom()).isEqualTo(p.getNom());
					assertThat(u2bd.getPersonnes().get(0).getNom()).isEqualTo(pbd.getNom());
					
					//regarde si la personne a les utilisateurs dans sa list
					assertThat(pbd.getUtilisateurs().get(0).getEmail()).isEqualTo(u1.getEmail());
					assertThat(pbd.getUtilisateurs().get(0).getEmail()).isEqualTo(u1bd.getEmail());
					assertThat(pbd.getUtilisateurs().get(1).getEmail()).isEqualTo(u2.getEmail());
					assertThat(pbd.getUtilisateurs().get(1).getEmail()).isEqualTo(u2bd.getEmail());
					
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