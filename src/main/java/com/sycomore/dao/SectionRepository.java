package com.sycomore.dao;

import com.sycomore.entity.Section;

public class SectionRepository extends ClassifiableRepository<Section>{

    public SectionRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Section> getEntityClass() {
        return Section.class;
    }

    @Override
    protected Section[] createArray(int size) {
        return new Section[size];
    }
}
