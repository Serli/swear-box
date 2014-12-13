package dao;

import play.db.jpa.JPA;
import models.Consumer;

/**
 * Ajoute un utilisateur
 * @author Geoffrey
 *
 */
public final class AddUser {

    private AddUser(){
    }

    /**
     * Ajoute l'utilisateur s'il n'existe pas
     * @param email email de l'utilisateur ( clé primaire de la table Utilisateur )
     */
    public static void addUser(String email) {
        //Recuperation de l'utilisateur
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //Si l'utilisateur n'existe pas il est ajouté
        if (u == null) {
            u = new Consumer(email);
            JPA.em().persist(u);
        }
    }
}
