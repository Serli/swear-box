package dao;

import play.db.jpa.JPA;
import models.*;

/**
 * Ajoute une personne qui sera liée à l'utilisateur connecté
 *
 */
public class AddPerson{

    private AddPerson(){
    }

    /**
     * Ajoute une personne dans la table personne 
     * Lie l'utilisateur et la parsonne
     * @param People : la personne a ajouter
     * @param String : l'identifiant de l'utilisateur qui ajoute la personne a sa liste
     */
    public static void addPerson(Person p,String id){
        //enregistrement de la personne
        JPA.em().persist(p);
        
        //recuperation de l'utilisateur
        Consumer user = JPA.em().find(Consumer.class,id); 
        user.setPerson(p);
        p.setUser(user);

        //liaison entre les deux
        JPA.em().flush();
    }

}