package com.sycomore.dao;

import com.sycomore.entity.PromotionStudyFees;

public class PromotionStudyFeesRepository extends BaseRepositoryJPA<PromotionStudyFees> {

    public PromotionStudyFeesRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<PromotionStudyFees> getEntityClass() {
        return PromotionStudyFees.class;
    }

    @Override
    protected PromotionStudyFees[] createArray(int size) {
        return new PromotionStudyFees[size];
    }
}
