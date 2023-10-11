package com.sycomore.model;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.Repository;
import com.sycomore.dao.SchoolRepository;
import com.sycomore.entity.School;

import java.util.Arrays;


public class SchoolTableModel extends AbstractPersistableEntityTableModel<School> {

    private final SchoolRepository schoolRepository;

    public SchoolTableModel() {
        super(DAOFactory.getInstance(SchoolRepository.class));
        schoolRepository = DAOFactory.getInstance(SchoolRepository.class);
    }

    public void init () {
        School [] schools = schoolRepository.findAll();
        if (schools == null)
            return;

        rows.addAll(Arrays.asList(schools));
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        School school = getRow(rowIndex);
        return school.getName();
    }
}
