package dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;

import models.Consumer;
import models.Person;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;

import com.cloudinary.Cloudinary;
import com.google.inject.Singleton;

/**
 * Groups the operations on the Person table
 *
 */
@Singleton
public final class PersonDAOImpl implements PersonDAO {
    private static final String QUERY_PERSON = "Select p from Person p where p.idPerson =";

    private Cloudinary cloudinary = com.cloudinary.Singleton.getCloudinary();

    /**
     * Add a person on the Person table
     * link the user with it
     * @param Person : person to add
     * @param String : user id
     */
    public void add(Person p,String id){
        //Recording the person
        JPA.em().persist(p);

        //Get the user
        Consumer user = JPA.em().find(Consumer.class,id); 
        user.setPerson(p);
        p.setUser(user);

        //Link the user with the person
        JPA.em().flush();
    }

    /**
     * Delete a person on the Person table
     * @param Person : person to add
     * @param String : user id
     */
    public void delete(long id,String email){
        //Get the person
        Query query = JPA.em().createQuery(QUERY_PERSON + id);
        Person pbd = (Person) query.getSingleResult();
        Consumer user = JPA.em().find(Consumer.class,email);

        //Delete keys in U_P
        if (user.getPeople().contains(pbd)){
            if(pbd.getPicture().startsWith("https")) {
                try { 
                    String url = pbd.getPicture().substring(pbd.getPicture().lastIndexOf("/"));
                    url = url.substring(1,url.lastIndexOf("."));
                    if(!url.equals(Play.application().configuration().getString("AvatarDefault"))) {
                        cloudinary.api().deleteResources(Arrays.asList(url),null);
                    }
                } catch (Exception e) {
                    Logger.info("Delete image on Cloudinary", e);
                }
            }
            for (Consumer u: pbd.getUsers()){	
                u.getPeople().remove(pbd);
            }
        }

        //Refresh DB
        JPA.em().flush();	

        //Delete the person
        JPA.em().remove(pbd);
    }

    /**
     * List all persons for an user
     * @param String : user id
     * @return List<Person> : list of persons
     */
    public List<Person> listByUser(String emailUser){
        Consumer u = JPA.em().find(Consumer.class, emailUser);

        //Sort of the members list by Firstname
        Collections.sort(u.getPeople(), new Comparator<Person>() {
            public int compare(Person o1, Person o2) {
                return o1.getFirstname().toLowerCase().compareTo(o2.getFirstname().toLowerCase());
            }
        });
        return u.getPeople();
    }

    /**
     * Discharge a person on the Person table
     * @param long : person id
     * @param String : user id
     */
    public void discharge(long id,String email){
        //Get the person
        Query query = JPA.em().createQuery(QUERY_PERSON + id);
        Person pbd = (Person) query.getSingleResult();
        Consumer user = JPA.em().find(Consumer.class,email);

        //If the user has rights
        if (user.getPeople().contains(pbd)){
            pbd.setDebt(0);
        }

        //Refresh DB
        JPA.em().flush();	

    }


    /**
     * Update a person on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new name
     * @param String : new firstname
     */
    public void updateNameFirstname(long id,String email,String vName, String vFirstname){
        //Get the person
        Query query = JPA.em().createQuery(QUERY_PERSON + id);
        Person pbd = (Person) query.getSingleResult();
        Consumer user = JPA.em().find(Consumer.class,email);

        //If the user has rights
        if (user.getPeople().contains(pbd)){
            pbd.setName(vName);
            pbd.setFirstname(vFirstname);
        }

        //Refresh DB
        JPA.em().flush();	

    }

    /**
     * Update a person picture on the Person table
     * @param long : person id
     * @param String : user id
     * @param String : new picture path
     */
    public void updatePicture(long id,String email,String vPicture){
        //Get the person
        Query query = JPA.em().createQuery(QUERY_PERSON + id);
        Person pbd = (Person) query.getSingleResult();
        Consumer user = JPA.em().find(Consumer.class,email);
        //If the user has rights
        if (user.getPeople().contains(pbd)){	
            if(pbd.getPicture().startsWith("https")) {
                try { 
                    String url = pbd.getPicture().substring(pbd.getPicture().lastIndexOf("/"));
                    url = url.substring(1,url.lastIndexOf("."));
                    if(!url.equals(Play.application().configuration().getString("AvatarDefault"))) {
                        cloudinary.api().deleteResources(Arrays.asList(url),null);
                    }
                } catch (Exception e) {
                    Logger.info("Delete image on Cloudinary", e);
                }
            }
            pbd.setAdrImage(vPicture);
        }
        //Refresh DB
        JPA.em().flush();	
    }
    /**
     * Increase a person debt
     * @param long : person id
     * @param String : user id
     */
    public void incrementDebt(long id,String email){
        //Get the person
        Query query = JPA.em().createQuery(QUERY_PERSON + id);
        Person pbd = (Person) query.getSingleResult();
        Consumer user = JPA.em().find(Consumer.class,email);

        //If the user has rights
        if (user.getPeople().contains(pbd)){
            if(Integer.MAX_VALUE-pbd.getDebt()>user.getAmount()) {
                pbd.setDebt(pbd.getDebt()+user.getAmount());
            }
            else {
                pbd.setDebt(Integer.MAX_VALUE);
            }
        }

        //Refresh DB
        JPA.em().flush();	
    }


}