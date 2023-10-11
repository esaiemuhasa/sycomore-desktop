package com.sycomore.dao;

import com.sycomore.entity.Inscription;
import com.sycomore.entity.Payment;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PaymentRepository extends BaseRepositoryJPA<Payment>{

    public PaymentRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Payment> getEntityClass() {
        return Payment.class;
    }

    @Override
    protected Payment[] createArray(int size) {
        return new Payment[size];
    }

    /**
     * Selection des operations de payement d'un inscrit
     */
    public Payment[] findAllByInscription (Inscription inscription) {
        EntityManager manager = getManager();
        TypedQuery<Payment> query = manager.createQuery("SELECT p FROM Payment p WHERE p.inscription = :inscription", Payment.class);
        query.setParameter("inscription", inscription);
        List<Payment> list = query.getResultList();
        return list.toArray(createArray(list.size()));
    }
}
