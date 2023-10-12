package com.sycomore.model;

import com.sycomore.dao.Repository;
import com.sycomore.entity.Classifiable;

public abstract class AbstractClassifiableTableModel<T extends Classifiable> extends AbstractPersistableEntityTableModel<T> {
    public AbstractClassifiableTableModel(Repository<T> repository) {
        super(repository);
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T row = getRow(rowIndex);
        if (columnIndex == 0)
            return row.getShortName();
        return row.getFullName();
    }
}
