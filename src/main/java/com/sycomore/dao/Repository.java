package com.sycomore.dao;

public interface Repository <T> {
    DAOFactory getFactory ();
    /**
     * Recuperation de l'entité dont l'id est en paramètre
     */
    T find (int id) throws RuntimeException;

    /**
     * Selection de toutes les occurrences d'une table dans la bdd
     */
    T[] findAll () throws RuntimeException;

    /**
     * Selection d'une partie des occurrences d'une table dans la base de donnees.
     */
    T[] findAll (int limit, int offset) throws RuntimeException;

    /**
     * Comptage des toutes les occurrences d'une table
     */
    int countAll () throws RuntimeException;

    /**
     * Sauvegarde l'état d'une entité dans la BDD
     */
    void persist (T t) throws RuntimeException;

    /**
     * Sauvegarde de l'état d'une entité dans la BDD, en prenant le controlle validation de
     * la transaction.
     */
    void persist (T t, boolean flush) throws RuntimeException;

    /**
     * Suppression d'une occurrence dans la BDD.
     */
    void remove (T t) throws RuntimeException;

    /**
     * Abonnement d'un listener au changement d'état des entités managé par le repos
     */
    void addRepositoryListener (RepositoryListener <T>  listener);

    /**
     * Désabonnement d'un écouteur
     */
    void removeRepositoryListener (RepositoryListener<T> listener);

    void removeAll();
}
