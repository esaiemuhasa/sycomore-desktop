package com.sycomore.model;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.LevelRepository;
import com.sycomore.entity.Level;

import java.util.Arrays;

public class LevelTableModel extends AbstractClassifiableTableModel<Level> {

    public LevelTableModel() {
        super(DAOFactory.getInstance(LevelRepository.class));
    }

    public void init () {
        Level [] levels = repository.findAll();
        rows.clear();
        if (levels != null && levels.length != 0) {
            rows.addAll(Arrays.asList(levels));
        }
        fireTableDataChanged();
    }
}
