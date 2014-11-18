
import javax.persistence.Query;

import org.junit.*;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;
import services.*;


public class AjoutPersonneTest{

	/**
	* Test Ajout de personne pour un utilisateur
	* Verification de la liaison entre les deux tables 
	*(par interpolation la création de la table personne)
	*/
	@Transactional
	@Test
    public void test() {
		running(fakeApplication(), new Runnable()
    	{
    	    public void run()
    	   {
				JPA.withTransaction(new play.libs.F.Callback0()
    	        {
					public void invoke()
					{
					Personne p =new Personne("Toto", "Titi",0,"yolo");
					Utilisateur u= new Utilisateur("email@email",100);
					//enregistrement de la personne
					JPA.em().persist(u);
					Utilisateur ubd=JPA.em().find(Utilisateur.class,"email@email");
					assertThat(ubd.getEmail()).isEqualTo(u.getEmail());
					
					AjoutPersonne.ajoutPersonne(p,u.getEmail());
					Personne pbd= (Personne)JPA.em().createQuery("Select p FROM Personne p").getSingleResult();
					
					//regarde si l'utilisateur a la personne dans sa list
					assertThat(ubd.getPersonnes().get(0).getNom()).isEqualTo(p.getNom());
					assertThat(ubd.getPersonnes().get(0).getNom()).isEqualTo(pbd.getNom());
					
					//regarde si la personne a l'utilisateur dans sa list
					assertThat(pbd.getUtilisateurs().get(0).getEmail()).isEqualTo(u.getEmail());
					assertThat(pbd.getUtilisateurs().get(0).getEmail()).isEqualTo(ubd.getEmail());
					}
				});
			}
		});
	}
	
	
}