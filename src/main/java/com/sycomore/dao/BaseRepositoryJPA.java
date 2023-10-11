package com.sycomore.dao;

import com.sycomore.entity.PersistableEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BaseRepositoryJPA <T extends PersistableEntity> implements Repository<T> {
    protected final DAOFactoryJPA factory;
    protected final List<RepositoryListener<T>> listeners = new ArrayList<>();

    protected static BaseRepositoryJPA<?> instance;

    public BaseRepositoryJPA (DAOFactoryJPA factory) {
        this.factory = factory;
        if (instance != null)
            throw new RuntimeException("Vous n'avez pas le droit d'instancier 2 fois la class "+instance.getClass().getName());
        instance = this;
    }

    protected EntityManager getManager () {
        return factory.getManager();
    }

    @Override
    public DAOFactory getFactory() {
        return factory;
    }

    @Override
    public T find(int id) throws RuntimeException {
        return factory.getManager().find(getEntityClass(), id);
    }

    @Override
    public T findOneLatest() throws RuntimeException {
        EntityManager manager = getManager();
        Class<T> cl = getEntityClass();

        TypedQuery<T> query = manager.createQuery("SELECT u FROM "+cl.getSimpleName()+" u ORDER BY recordingDate DESC", cl);
        query.setMaxResults(1).setFirstResult(0);

        return query.getSingleResult();
    }

    @Override
    public T[] findAll() throws RuntimeException {
        EntityManager manager = getManager();
        Class<T> cl = getEntityClass();

        TypedQuery<T> query = manager.createQuery("SELECT u FROM "+cl.getSimpleName()+" u", cl);
        List<T> list = query.getResultList();

        if (list.isEmpty())
            return null;

        return list.toArray(createArray(list.size()));
    }

    @Override
    public T[] findAll(int limit, int offset) throws RuntimeException {
        EntityManager manager = getManager();
        Class<T> cl = getEntityClass();

        TypedQuery<T> query = manager.createQuery("SELECT u FROM "+cl.getSimpleName()+" u", cl);
        query.setMaxResults(limit).setFirstResult(offset);

        List<T> list = query.getResultList();

        return list.toArray(createArray(list.size()));
    }

    @Override
    public int countAll() throws RuntimeException {
        EntityManager manager = getManager();
        Class<T> cl = getEntityClass();
        Query query = manager.createQuery("SELECT COUNT(u.id) FROM "+cl.getSimpleName()+" u");
        return Integer.parseInt(query.getSingleResult().toString());
    }

    @Override
    public void persist(T t) throws RuntimeException {
        EntityManager manager = getManager();
        EntityTransaction transaction = manager.getTransaction();

        boolean commit = !transaction.isActive();
        if (commit)
            transaction.begin();

        boolean hasId = t.getId() != null && t.getId() > 0;

        if (!hasId && t.getRecordingDate() == null)
            t.setRecordingDate(new Date());

        T old = hasId ? find(t.getId()) : null;

        manager.persist(t);
        manager.flush();

        if (commit)
            transaction.commit();

        if (hasId)
            fireOnUpdate(old, t);
        else
            fireOnCreate(t);
    }

    @Override
    public void persist(T t, boolean flush) throws RuntimeException {
        if (flush)
            persist(t);
        else
            getManager().persist(t);
    }

    @Override
    public void remove(T t) throws RuntimeException {
        EntityManager manager = getManager();
        EntityTransaction transaction = manager.getTransaction();
        boolean commit = !transaction.isActive();

        if (commit)
            transaction.begin();

        manager.remove(t);
        manager.flush();

        if (commit)
            transaction.commit();

        fireOnDelete(t);
    }

    public void removeAll() throws RuntimeException {
        T [] data = findAll();
        EntityManager manager = getManager();
        EntityTransaction transaction = manager.getTransaction();
        boolean commit = !transaction.isActive();

        if (commit)
            transaction.begin();

        for (T t : data)
            manager.remove(t);

        manager.flush();
        if (commit)
            transaction.commit();

        for (T t : data)
            fireOnDelete(t);
    }

    @Override
    public void addRepositoryListener(RepositoryListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeRepositoryListener(RepositoryListener<T> listener) {
        listeners.remove(listener);
    }

    protected void fireOnCreate(T t) {
        Object [] ls = listeners.toArray();
        for (Object l: ls)  {
            //noinspection unchecked
            ((RepositoryListener<T>)l).onCreate(t);
        }

        fireOnAlter(null, t);
    }

    protected void fireOnAlter (T oldState, T newState) {
        Object [] ls = listeners.toArray();
        for (Object l: ls) {
            //noinspection unchecked
            ((RepositoryListener<T>)l).onAlter(oldState, newState);
        }
    }

    protected void fireOnUpdate(T oldState, T newState) {
        Object [] ls = listeners.toArray();
        for (Object l: ls) {
            //noinspection unchecked
            ((RepositoryListener<T>)l).onUpdate(oldState, newState);
        }

        fireOnAlter(oldState, newState);
    }

    protected void fireOnDelete (T t) {
        Object [] ls = listeners.toArray();
        for (Object l: ls) {
            //noinspection unchecked
            ((RepositoryListener<T>)l).onDelete(t);
        }

        fireOnAlter(t, null);
    }

    /**
     * Utilitaire qui renvoie la classe de l'entité prise en charge par le repository
     */
    protected abstract Class<T> getEntityClass ();

    /**
     * Utilitaire de creation d'un tableau de taille en paramètre pour l'entité pris en charge par le repository
     */
    protected abstract T [] createArray (int size);
}
