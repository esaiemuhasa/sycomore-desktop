package com.sycomore.dao;

import com.sycomore.entity.School;

public class SchoolRepository extends BaseRepositoryJPA<School> {
    public SchoolRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<School> getEntityClass() {
        return School.class;
    }

    @Override
    protected School[] createArray(int size) {
        return new School[size];
    }
}
