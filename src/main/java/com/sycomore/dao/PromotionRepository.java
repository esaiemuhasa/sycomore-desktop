package com.sycomore.dao;

import com.sycomore.entity.Promotion;
import com.sycomore.entity.SchoolYear;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

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

    /**
     * Selection de la liste des promotions d'une année scolaire.
     */
    public Promotion [] findAllByYear (SchoolYear year) {
        EntityManager manager = getManager();
        TypedQuery<Promotion> query = manager.createQuery("SELECT p FROM "+getEntityClass().getSimpleName()+" p WHERE p.year = :year", getEntityClass());
        query.setParameter("year", year);
        List<Promotion> list = query.getResultList();
        if (list == null || list.isEmpty())
            return null;
        return list.toArray(createArray(list.size()));
    }
}
