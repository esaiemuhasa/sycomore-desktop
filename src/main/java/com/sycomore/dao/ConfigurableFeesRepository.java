package com.sycomore.dao;

import com.sycomore.entity.ConfigurableFees;
import com.sycomore.entity.SchoolYear;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class ConfigurableFeesRepository<T extends ConfigurableFees> extends BaseRepositoryJPA<T> {

    public ConfigurableFeesRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    /**
     * Selection des configurations d'une année
     */
    public T [] findAllByYear (SchoolYear year) throws RuntimeException {
        EntityManager manager = getManager();
        Class<T> cl = getEntityClass();

        TypedQuery<T> query = manager.createQuery("SELECT u FROM "+cl.getSimpleName()+" u WHERE year = :year", cl);
        query.setParameter("year", year);

        List<T> list = query.getResultList();

        if (list.isEmpty())
            return null;

        return list.toArray(createArray(list.size()));
    }
}
