package dao;

import play.db.jpa.JPA;
import models.*;

/**
 * link an user and a person
 *
 */
public final class LinkUserPerson {

    private LinkUserPerson(){
    }

    /**
     * link a person to an user
     * @param long : person id
     * @param String : user id
     */
    public static void linkUserPerson(long idPerson,String idUser) {
        boolean test= true;

        //recuperation de l'utilisateur
        Consumer user = JPA.em().find(Consumer.class,idUser); 

        //regarde si il sont deja lier
        for (Person p :user.getPeople()){
            if(p.getIdPerson()==idPerson){
                test = false;
            }
        }

        //lie les deux
        if(test){
            Person pbd=JPA.em().find(Person.class,idPerson);
            user.setPerson(pbd);
            pbd.setUser(user);
        }
    }
}
