package com.sycomore.model;

import com.sycomore.dao.Repository;
import com.sycomore.entity.School;


public class SchoolTableModel extends AbstractPersistableEntityTableModel<School> {
    public SchoolTableModel(Repository<School> repository) {
        super(repository);
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
