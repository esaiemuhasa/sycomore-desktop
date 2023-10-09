package com.sycomore.dao;

import com.sycomore.entity.Option;

public class OptionRepository extends BaseRepositoryJPA<Option>{

    public OptionRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Option> getEntityClass() {
        return Option.class;
    }

    @Override
    protected Option[] createArray(int size) {
        return new Option[size];
    }
}
