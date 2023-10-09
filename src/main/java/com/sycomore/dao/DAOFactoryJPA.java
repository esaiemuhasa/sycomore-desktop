package com.sycomore.dao;

import com.sycomore.entity.PersistableEntity;
import com.sycomore.helper.Config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation par default du DAO
 */
public class DAOFactoryJPA implements DAOFactory {
    private final Map<String, Repository<?>> repositories = new HashMap<>();
    private EntityManager entityManager;
    private final EntityManagerFactory managerFactory;

    DAOFactoryJPA() {
        managerFactory = Persistence.createEntityManagerFactory(Config.get("persistence_unit"));
        entityManager = managerFactory.createEntityManager();
    }

    @Override
    public <E extends PersistableEntity, T extends Repository<E>> T getRepository(Class<T> reposClass) {
        if (!repositories.containsKey(reposClass.getName())) {
            try {
                Constructor<T> constructor = reposClass.getConstructor(DAOFactoryJPA.class);
                T o = constructor.newInstance(this);
                repositories.put(reposClass.getName(), o);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked") T t = (T) repositories.get(reposClass.getName());
        return t;
    }

    @Override
    public void commit() {
        EntityManager manager = getManager();
        EntityTransaction transaction = manager.getTransaction();
        manager.flush();
        if (transaction.isActive())
            transaction.commit();
    }

    @Override
    public void begin() {
        EntityTransaction transaction = getManager().getTransaction();
        if (!transaction.isActive()){
            transaction.begin();
        }
    }

    @Override
    public void rollback() {
        EntityTransaction transaction = getManager().getTransaction();
        if (!transaction.isActive()){
            transaction.rollback();
        }
    }

    /**
     * Renvoie le manager crée par default, lors de l'initialisation du DAO
     */
    @Override
    public EntityManager getManager () {
        if (!entityManager.isOpen())
            entityManager = managerFactory.createEntityManager();

        return entityManager;
    }

    /**
     * Utilitaire de creation d'un nouveau manager, depute le factory des managers
     */
    public EntityManager createManager () {
        return managerFactory.createEntityManager();
    }
}
