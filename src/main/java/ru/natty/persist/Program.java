/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.persist;

import javax.persistence.*;

/**
 *
 * @author necto
 */
public class Program {
    public static void main(String[] args) {
        // Create the EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("natty");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Genre gen = new Genre("alternative");
        em.persist(gen);
        Artist art = new Artist("Muse");
        em.persist(art);
        em.flush();

        art.getGenreCollection().add(gen);
        em.getTransaction().commit();



        em.close();
        emf.close();
    }

}