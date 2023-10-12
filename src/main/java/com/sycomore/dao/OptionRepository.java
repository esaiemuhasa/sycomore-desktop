package com.sycomore.dao;

import com.sycomore.entity.Option;
import com.sycomore.entity.Section;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class OptionRepository extends BaseRepositoryJPA<Option>{

    public OptionRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Option> getEntityClass() {
        return Option.class;
    }

    @Override
    protected Option[] createArray(int size) {
        return new Option[size];
    }

    /**
     * Selection de la liste des options d'une section
     */
    public Option [] findAllBySection (Section section) {
        EntityManager manager = getManager();
        TypedQuery<Option> query = manager.createQuery("SELECT o FROM Option o WHERE o.section = :section", Option.class);
        query.setParameter("section", section);
        List<Option> list = query.getResultList();
        if (list.isEmpty())
            return null;
        return list.toArray(createArray(list.size()));
    }
}
