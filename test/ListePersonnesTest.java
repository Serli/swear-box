import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.List;

import org.junit.Test;

import play.db.jpa.JPA;
import models.Personne;
import models.Utilisateur;
import services.ListePersonnes;

/**
 * Test la classe ListePersonne
 * 
 * @author Geoffrey
 *
 */
public class ListePersonnesTest {

	/**
	 * Test la récupération de la liste des membres liés à l'utilisateur dans la
	 * base de données
	 */
	@Test
	public void listePersonnes() {
		running(fakeApplication(inMemoryDatabase()), new Runnable() {
			public void run() {
				JPA.withTransaction(new play.libs.F.Callback0() {
					public void invoke() {
						// Ajout d'un utilisateur
						String emailU1 = "test1@email.com";
						Utilisateur u1 = new Utilisateur(emailU1, 100);
						JPA.em().persist(u1);
						Utilisateur u2 = new Utilisateur("test2@email.com", 100);
						JPA.em().persist(u2);

						// Ajout de deux personnes
						Personne p1 = new Personne("nom1", "prenom1", 1, "adr1");
						Personne p2 = new Personne("nom2", "prenom2", 2, "adr2");
						Personne p3 = new Personne("nom3", "prenom3", 3, "adr3");
						JPA.em().persist(p1);
						JPA.em().persist(p2);
						JPA.em().persist(p3);

						// Liaision des personnes avec les utilisateurs (table
						// u_p)
						u1.setPersonnes(p1);
						p1.setUtilisateurs(u1);
						u1.setPersonnes(p2);
						p2.setUtilisateurs(u1);
						u2.setPersonnes(p3);
						p3.setUtilisateurs(u2);

						// Synchronisation avec la BD
						JPA.em().flush();

						// Récupération de la liste des personnes liées à u1
						List<Personne> l = ListePersonnes
								.listePersonnes(emailU1);

						assertThat(l.size()).isEqualTo(2);
						assertThat(l.get(0).getPrenom()).isEqualTo(
								p1.getPrenom());
						assertThat(l.get(1).getPrenom()).isEqualTo(
								p2.getPrenom());

						JPA.em().remove(u1);
						JPA.em().remove(u2);
						JPA.em().remove(p1);
						JPA.em().remove(p2);
						JPA.em().remove(p3);
					}
				});
			}
		});
	}
}
