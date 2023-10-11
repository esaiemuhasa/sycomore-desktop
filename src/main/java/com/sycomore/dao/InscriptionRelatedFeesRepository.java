package com.sycomore.dao;

import com.sycomore.entity.Inscription;
import com.sycomore.entity.InscriptionRelatedFees;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class InscriptionRelatedFeesRepository extends BaseRepositoryJPA<InscriptionRelatedFees> {

    public InscriptionRelatedFeesRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<InscriptionRelatedFees> getEntityClass() {
        return InscriptionRelatedFees.class;
    }

    @Override
    protected InscriptionRelatedFees[] createArray(int size) {
        return new InscriptionRelatedFees[size];
    }

    /**
     * Selection la liste des frais supplémentaire que doit payer un élève
     */
    protected InscriptionRelatedFees [] findAllByInscription (Inscription inscription) {
        EntityManager manager = getManager();
        TypedQuery<InscriptionRelatedFees> query = manager.createQuery("SELECT i FROM "+getEntityClass().getSimpleName()+" i WHERE i.inscription = :inscription", getEntityClass());
        query.setParameter("inscription", inscription);
        List<InscriptionRelatedFees> list =  query.getResultList();
        if (list == null || list.isEmpty())
            return null;
        return list.toArray(createArray(list.size()));
    }
}
