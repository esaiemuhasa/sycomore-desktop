package com.sycomore.dao;

import com.sycomore.entity.Level;

public class LevelRepository extends ClassifiableRepository<Level> {
    public LevelRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Level> getEntityClass() {
        return Level.class;
    }

    @Override
    protected Level[] createArray(int size) {
        return new Level[size];
    }
}
