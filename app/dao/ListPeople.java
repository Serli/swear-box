package dao;

import java.util.List;

import models.Consumer;
import models.Person;
import play.db.jpa.JPA;

/**
 * Liste les personnes liées à l'utilisateur connecté
 * @author Geoffrey
 *
 */
public class ListPeople {

    private ListPeople(){
    }

    /**
     * Liste les personnes liées à l'utilisateur dont l'email est passé en paramètre
     * @param emailUser email de l'utilisateur
     * @return List<Person> la liste des personnes
     */
    public  static List<Person> listPeople(String emailUser){
        Consumer u = JPA.em().find(Consumer.class, emailUser);
        return u.getPeople();
    }

}

