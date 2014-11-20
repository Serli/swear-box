import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.List;

import org.junit.Test;

import play.db.jpa.JPA;
import models.*;
import models.User;
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
						User u1 = new User(emailU1, 100);
						JPA.em().persist(u1);
						User u2 = new User("test2@email.com", 100);
						JPA.em().persist(u2);

						// Ajout de deux personnes
						Person p1 = new Person("nom1", "prenom1", 1, "adr1");
						Person p2 = new Person("nom2", "prenom2", 2, "adr2");
						Person p3 = new Person("nom3", "prenom3", 3, "adr3");
						JPA.em().persist(p1);
						JPA.em().persist(p2);
						JPA.em().persist(p3);

						// Liaision des personnes avec les utilisateurs (table
						// u_p)
						u1.setPerson(p1);
						p1.setUser(u1);
						u1.setPerson(p2);
						p2.setUser(u1);
						u2.setPerson(p3);
						p3.setUser(u2);

						// Synchronisation avec la BD
						JPA.em().flush();

						// Récupération de la liste des personnes liées à u1
						List<Person> l = ListePersonnes
								.listePersonnes(emailU1);

						assertThat(l.size()).isEqualTo(2);
						assertThat(l.get(0).getFirstName()).isEqualTo(
								p1.getFirstName());
						assertThat(l.get(1).getFirstName()).isEqualTo(
								p2.getFirstName());

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
