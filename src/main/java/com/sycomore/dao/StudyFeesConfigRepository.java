package com.sycomore.dao;

import com.sycomore.entity.SchoolYear;
import com.sycomore.entity.StudyFeesConfig;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class StudyFeesConfigRepository extends BaseRepositoryJPA<StudyFeesConfig> {

    public StudyFeesConfigRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<StudyFeesConfig> getEntityClass() {
        return StudyFeesConfig.class;
    }

    @Override
    protected StudyFeesConfig[] createArray(int size) {
        return new StudyFeesConfig[size];
    }

    /**
     * Selection de la liste des configs des frais scolaire pour une année scolaire.
     */
    public StudyFeesConfig [] findAllByYear (SchoolYear year) {
        EntityManager manager = getManager();
        TypedQuery<StudyFeesConfig> query = manager.createQuery("SELECT s FROM "+getEntityClass().getSimpleName()+" s WHERE s.year = :year", getEntityClass());
        query.setParameter("year", year);

        List<StudyFeesConfig> list = query.getResultList();
        if (list.isEmpty())
            return null;

        return list.toArray(createArray(list.size()));
    }
}
