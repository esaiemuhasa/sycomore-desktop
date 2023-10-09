package com.sycomore.dao;

import com.sycomore.entity.InscriptionRelatedFees;

public class InscriptionRelatedFeesRepository extends BaseRepositoryJPA<InscriptionRelatedFees> {

    public InscriptionRelatedFeesRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<InscriptionRelatedFees> getEntityClass() {
        return InscriptionRelatedFees.class;
    }

    @Override
    protected InscriptionRelatedFees[] createArray(int size) {
        return new InscriptionRelatedFees[size];
    }
}
