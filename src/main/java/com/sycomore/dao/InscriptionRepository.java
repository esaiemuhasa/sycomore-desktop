package com.sycomore.dao;

import com.sycomore.entity.Inscription;

public class InscriptionRepository extends BaseRepositoryJPA<Inscription> {

    public InscriptionRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Inscription> getEntityClass() {
        return Inscription.class;
    }

    @Override
    protected Inscription[] createArray(int size) {
        return new Inscription[size];
    }
}
