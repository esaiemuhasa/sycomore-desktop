package com.sycomore.dao;

/**
 * Utilitaire qui se charge d'instancier le factory du DAO, et de genre son unique instance durant
 * execution de l'app.
 */
class DAOLoader {
    private static DAOFactoryJPA factory;

    private DAOLoader() {
    }

    public static DAOFactoryJPA getFactory () {
        if (factory == null) {
            factory = new DAOFactoryJPA();
        }

        return factory;
    }
}
