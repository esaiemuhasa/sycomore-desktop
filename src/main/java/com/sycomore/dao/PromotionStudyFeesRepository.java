package com.sycomore.dao;

import com.sycomore.entity.Promotion;
import com.sycomore.entity.PromotionStudyFees;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

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

    /**
     * Selection de la configuration des frais scolaire d'une promotion
     */
    public PromotionStudyFees[] findAllByPromotion (Promotion promotion) {
        EntityManager manager = getManager();
        TypedQuery<PromotionStudyFees> query = manager.createQuery("SELECT p FROM "+getEntityClass().getSimpleName()+" p WHERE p.promotion = :promotion", getEntityClass());
        query.setParameter("promotion", promotion);
        List<PromotionStudyFees> list = query.getResultList();
        if (list == null || list.isEmpty())
            return null;
        return list.toArray(createArray(list.size()));
    }
}
