package dao;

import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.*;

/**
 * Regroupe les opérations sur la table Person
 *
 */
public final class PersonDAO{

    private PersonDAO(){
    }

    /**
     * Ajoute une personne dans la table personne 
     * Lie l'utilisateur et la parsonne
     * @param People : la personne a ajouter
     * @param String : l'identifiant de l'utilisateur qui ajoute la personne a sa liste
     */
    public static void add(Person p,String id){
        //enregistrement de la personne
        JPA.em().persist(p);

        //recuperation de l'utilisateur
        Consumer user = JPA.em().find(Consumer.class,id); 
        user.setPerson(p);
        p.setUser(user);

        //liaison entre les deux
        JPA.em().flush();
    }

    /**
     * Supprime une personne dans la table personne
     * @param String : nom de la personne
     * @param String : prenom de la personne
     * @param String : l'identifiant de l'utilisateur qui supprime la personne
     */
    public static void delete(long id){
        //recuperation de la personne
        Query query = JPA.em().createQuery("Select p from Person p where p.idPerson =" + id);

        Person pbd = (Person) query.getSingleResult();

        //suppression des clé dans U_P
        for (Consumer u: pbd.getUsers()){   
            u.getPeople().remove(pbd);
        }

        //referesh BD
        JPA.em().flush();   

        //suppression de la personne
        JPA.em().remove(pbd);
    }

    /**
     * Liste les personnes liées à l'utilisateur dont l'email est passé en paramètre
     * @param emailUser email de l'utilisateur
     * @return List<Person> la liste des personnes
     */
    public  static List<Person> listByUser(String emailUser){
        Consumer u = JPA.em().find(Consumer.class, emailUser);
        return u.getPeople();
    }

}