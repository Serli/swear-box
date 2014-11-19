
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
						Personne p =new Personne("Suppr-Toto", "Suppr-Titi",0,"yolo");
						Utilisateur u1= new Utilisateur("Suppr-email1@email",100);
						Utilisateur u2= new Utilisateur("Suppr-email2@email",50);
						
						//enregistrement des utilisateurs
						JPA.em().persist(u1);
						Utilisateur u1bd=JPA.em().find(Utilisateur.class,"Suppr-email1@email");
						JPA.em().persist(u2);
						Utilisateur u2bd=JPA.em().find(Utilisateur.class,"Suppr-email2@email");
						
						//ajoute la personne et lie la personne a un autre utilisateur
						AjoutPersonne.ajoutPersonne(p,u1.getEmail());
						LierPersonneUtilisateur .lierPersonneUtilisateur(p,u2.getEmail());
						
						
						Personne pbd= (Personne)JPA.em().createQuery("Select p FROM Personne p WHERE p.Nom='Suppr-Toto'").getSingleResult();
						
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
	
						JPA.em().flush();
						
						//suppression de la personne pour les deux utilisateurs
						SupprimerPersonne.supprimerPersonne("Suppr-Toto", "Suppr-Titi", "Suppr-email1@email");
						
						//recuperation des utilisateurs en bd apres suppression
						u1bd=JPA.em().find(Utilisateur.class,"Suppr-email1@email");
						u2bd=JPA.em().find(Utilisateur.class,"Suppr-email2@email");
						
						//test si la personne n'existe plus
						assertThat(u1bd.getPersonnes()).isEmpty();
						assertThat(u2bd.getPersonnes()).isEmpty();
						
						//clean
						JPA.em().remove(u1bd);
						JPA.em().remove(u2bd);
					}
				});
			}
		});
	}
	
	
}