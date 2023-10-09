package com.sycomore.dao;

import com.sycomore.entity.SchoolYear;

public class SchoolYearRepository extends BaseRepositoryJPA<SchoolYear>{
    public SchoolYearRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<SchoolYear> getEntityClass() {
        return SchoolYear.class;
    }

    @Override
    protected SchoolYear[] createArray(int size) {
        return new SchoolYear[size];
    }
}
