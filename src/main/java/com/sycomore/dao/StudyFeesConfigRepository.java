package com.sycomore.dao;

import com.sycomore.entity.StudyFeesConfig;

public class StudyFeesConfigRepository extends BaseRepositoryJPA<StudyFeesConfig> {

    public StudyFeesConfigRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<StudyFeesConfig> getEntityClass() {
        return StudyFeesConfig.class;
    }

    @Override
    protected StudyFeesConfig[] createArray(int size) {
        return new StudyFeesConfig[size];
    }
}
