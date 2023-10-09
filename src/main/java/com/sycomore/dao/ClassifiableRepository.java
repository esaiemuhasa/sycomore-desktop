package com.sycomore.dao;

import com.sycomore.entity.Classifiable;

public abstract class ClassifiableRepository <T extends Classifiable> extends BaseRepositoryJPA<T> {
    public ClassifiableRepository(DAOFactoryJPA factory) {
        super(factory);
    }
}
