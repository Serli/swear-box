package dao;

import play.db.jpa.JPA;
import models.*;

/**
 * Link an user and a person
 *
 */
public final class LinkUserPerson {

    private LinkUserPerson(){
    }

    /**
     * Link a person to an user
     * @param long : person id
     * @param String : user id
     */
    public static void linkUserPerson(long idPerson,String idUser) {
        boolean test= true;

        //Get the user
        Consumer user = JPA.em().find(Consumer.class,idUser); 

        //Check if they are linked
        for (Person p :user.getPeople()){
            if(p.getIdPerson()==idPerson){
                test = false;
            }
        }

        //Link the person to the user
        if(test){
            Person pbd=JPA.em().find(Person.class,idPerson);
            user.setPerson(pbd);
            pbd.setUser(user);
        }
    }
}
