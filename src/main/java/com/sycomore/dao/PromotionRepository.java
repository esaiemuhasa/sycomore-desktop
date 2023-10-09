package com.sycomore.dao;

import com.sycomore.entity.Promotion;

public class PromotionRepository extends BaseRepositoryJPA<Promotion> {

    public PromotionRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Promotion> getEntityClass() {
        return Promotion.class;
    }

    @Override
    protected Promotion[] createArray(int size) {
        return new Promotion[size];
    }
}
