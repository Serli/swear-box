import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

import play.db.jpa.JPA;
import models.*;
import services.*;

import java.util.List;

import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.BodyParser;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;
import org.pac4j.play.Config;
import play.GlobalSettings;
import play.Play;

import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;


@Transactional
public class AjoutPersonneTest{

	@Test
    public void test() {
	
		Personne p =new Personne("Toto", "Titi",0,"");
		Utilisateur u= new Utilisateur("email@email",100);
		//enregistrement de la personne
		enregistrement(u);
		
		AjoutPersonne.ajoutPersonne(p,u.getEmail());
		Utilisateur ubd=JPA.em().find(Utilisateur.class,"email@email");
		assertThat(ubd.getEmail()).isEqualTo(u.getEmail());
	}
	
	
	public void enregistrement(Utilisateur u){
		JPA.em().persist(u);
	}
	
}