package com.sycomore.model;

import com.sycomore.dao.CategoryRepository;
import com.sycomore.dao.DAOFactory;
import com.sycomore.entity.Category;

import java.util.Arrays;

public class CategoryTableModel extends AbstractPersistableEntityTableModel <Category> {

    public CategoryTableModel() {
        super(DAOFactory.getInstance(CategoryRepository.class));
    }

    public void  init () {
        Category [] categories = repository.findAll();
        rows.clear();
        if (categories != null)
            rows.addAll(Arrays.asList(categories));

        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).getLabel();
    }
}
