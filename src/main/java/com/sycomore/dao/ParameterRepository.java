package com.sycomore.dao;

import com.sycomore.entity.helper.Parameter;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ParameterRepository extends BaseRepositoryJPA<Parameter> {

    public ParameterRepository(DAOFactoryJPA factory) throws RuntimeException {
        super(factory);
    }

    /**
     * Recuperation du paramètre dont le nom est en paramètre
     */
    public Parameter findOneByName (String name) {
        EntityManager manager = getManager();

        TypedQuery<Parameter> query = manager.createQuery("SELECT p FROM  Parameter p WHERE p.name = :name", Parameter.class);
        query.setParameter("name", name);

        return query.getSingleResult();
    }

    @Override
    protected Class<Parameter> getEntityClass() {
        return Parameter.class;
    }

    @Override
    protected Parameter[] createArray(int size) {
        return new Parameter[size];
    }
}
