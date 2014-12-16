package dao;

import play.db.jpa.JPA;
import models.Consumer;

/**
 * Regroupe les opérations sur la table Consumer
 *
 */
public final class ConsumerDAO {

    private ConsumerDAO(){
    }

    /**
     * Ajoute l'utilisateur s'il n'existe pas
     * @param email email de l'utilisateur ( clé primaire de la table Utilisateur )
     */
    public static void add(String email) {
        //Recuperation de l'utilisateur
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //Si l'utilisateur n'existe pas il est ajouté
        if (u == null) {
            u = new Consumer(email);
            JPA.em().persist(u);
        }
    }
    
    /**
     * Modifie le montant de la penalité.
     * @param email email de l'utilisateur ( clé primaire de la table Utilisateur )
     */
    public static void updateAmount(String email, int vAmount) {
        //Recuperation de l'utilisateur
        Consumer u = JPA.em().find(Consumer.class, email);
        
        //Si l'utilisateur n'existe pas il est ajouté
        if (u != null) {
            u.setAmount(vAmount);
        }
        
        JPA.em().flush();
    }
}
