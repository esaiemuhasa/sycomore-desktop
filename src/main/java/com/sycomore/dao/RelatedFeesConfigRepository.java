package com.sycomore.dao;

import com.sycomore.entity.RelatedFeesConfig;

public class RelatedFeesConfigRepository extends ConfigurableFeesRepository<RelatedFeesConfig> {

    public RelatedFeesConfigRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<RelatedFeesConfig> getEntityClass() {
        return RelatedFeesConfig.class;
    }

    @Override
    protected RelatedFeesConfig[] createArray(int size) {
        return new RelatedFeesConfig[size];
    }
}
