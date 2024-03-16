package com.sycomore.dao;

import com.sycomore.entity.Promotion;
import com.sycomore.entity.PromotionStudyFees;
import com.sycomore.entity.SchoolYear;
import com.sycomore.entity.StudyFeesConfig;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    /**
     * Somme des frais que doit payer une promotion.
     */
    public double sumAllByPromotion (Promotion promotion) {
        EntityManager manager = getManager();
        Query query = manager.createQuery("SELECT SUM(c.amount) AS cash FROM PromotionStudyFees p INNER JOIN p.config c WHERE p.promotion = :promotion");
        query.setParameter("promotion", promotion);

        Object sum = query.getSingleResult();
        if (sum == null)
            return 0;

        return Double.parseDouble(sum.toString());
    }

    /**
     * Selection des configurations des frais scolaires pour une année scolaire.
     */
    public PromotionStudyFees [] findAllByYear (SchoolYear year) {
        EntityManager manager = getManager();
        TypedQuery<PromotionStudyFees> query = manager.createQuery("SELECT s FROM "+getEntityClass().getSimpleName()
                +" s INNER JOIN s.promotion p WHERE p.year = :year", getEntityClass());
        query.setParameter("year", year);

        List<PromotionStudyFees> list = query.getResultList();
        if (list.isEmpty())
            return null;

        return list.toArray(createArray(list.size()));
    }
}
