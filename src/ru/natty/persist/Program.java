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

        Query q = em.createNamedQuery ("Artist.findByName");
        q.setParameter ("name", "miclucha maklay");
        Artist mik = (Artist)q.getSingleResult();
        System.out.print ("\n\n" +  mik.getName () + "\n\n");

        Track t = new Track ("Artivasto winter");
        mik.addTrack (t);
        t.setArtist (mik);//It's a thing to improve,
                          //because I have to do such thing, but I shouldn't

        em.persist (mik);

        em.getTransaction().commit();

        em.close();
        emf.close();
    }

}