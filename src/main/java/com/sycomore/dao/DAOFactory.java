package com.sycomore.dao;

import javax.persistence.EntityManager;

public interface DAOFactory {

    /**
     * Methode utilitaire qui renvoie l'une instance du factory du DAO
     */
    static DAOFactory getInstance() {
        return DAOLoader.getFactory();
    }

    /**
     * Utilitaire d'instanciation d'une interface du DAO
     */
    static <E, T extends Repository<E>> T getInstance  (Class<T> reposClass) {
        return getInstance().getRepository(reposClass);
    }

    /**
     * Utilitaire de recuperation d'une interface du DAO.
     */
    <E, T extends Repository<E>> T getRepository  (Class<T> reposClass);

    /**
     * Démarrage d'une nouvelle transaction, s'il n'y a aucune.
     */
    void begin();

    /**
     * Demande de commit des operations en attente
     */
    void commit();

    /**
     * Annulation des operations en attente de commit
     */
    void rollback();

    EntityManager getManager();
}
