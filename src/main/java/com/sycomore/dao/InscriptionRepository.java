package com.sycomore.dao;

import com.sycomore.entity.Inscription;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.SchoolYear;
import com.sycomore.entity.Student;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class InscriptionRepository extends BaseRepositoryJPA<Inscription> {

    public InscriptionRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Inscription> getEntityClass() {
        return Inscription.class;
    }

    @Override
    protected Inscription[] createArray(int size) {
        return new Inscription[size];
    }

    /**
     * Selection de la liste des inscriptions dans une promotion (liste des élèves inscrit dans une promotion)
     */
    public Inscription[] findAllByPromotion (Promotion promotion) {
        EntityManager manager = getManager();
        TypedQuery<Inscription> query = manager.createQuery("SELECT i FROM Inscription i WHERE i.promotion = :promotion", Inscription.class);
        query.setParameter("promotion", promotion);
        List<Inscription> list = query.getResultList();
        return list.toArray(createArray(list.size()));
    }

    /**
     * Selection de la liste des inscriptions faite pour une année scolaire.
     */
    public Inscription[] findAllByYear (SchoolYear year) {
        EntityManager manager = getManager();
        TypedQuery<Inscription> query = manager.createQuery("SELECT i FROM Inscription i INNER JOIN Promotion p ON p.id = i.promotion  WHERE p.year = :year", Inscription.class);
        query.setParameter("year", year);
        List<Inscription> list = query.getResultList();
        return list.toArray(createArray(list.size()));
    }

    /**
     * Selection de la liste des inscriptions d'un élève.
     */
    public Inscription[] findAllByStudent (Student student) {
        EntityManager manager = getManager();
        TypedQuery<Inscription> query = manager.createQuery("SELECT i FROM Inscription i WHERE i.student = :student", Inscription.class);
        query.setParameter("student", student);
        List<Inscription> list = query.getResultList();
        return list.toArray(createArray(list.size()));
    }

}
