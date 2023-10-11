package com.sycomore.model;

import com.sycomore.dao.Repository;
import com.sycomore.dao.RepositoryAdapter;
import com.sycomore.dao.RepositoryListener;
import com.sycomore.entity.PersistableEntity;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPersistableEntityTableModel <T extends PersistableEntity> extends AbstractTableModel {
    private final List<T> rows = new ArrayList<>();

    private final Repository<T> repository;

    public AbstractPersistableEntityTableModel(Repository<T> repository) {
        this.repository = repository;
        repository.addRepositoryListener(repositoryListener);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Recuperation de l'objet a l'index en paramètre.
     */
    public T getRow (int index) {
        return rows.get(index);
    }

    public Object [] toArray () {
        if (rows.isEmpty()) {
            return null;
        }

        Object [] data = new Object[rows.size()];
        return rows.toArray(data);
    }

    public void dispose () {
        repository.removeRepositoryListener(repositoryListener);
    }

    private final RepositoryListener<T> repositoryListener = new RepositoryAdapter<T>() {
        @Override
        public void onCreate(T t) {
            rows.add(t);
            fireTableRowsInserted(rows.size()-1, rows.size()-1);
        }

        @Override
        public void onUpdate(T oldState, T newState) {
            for (int i = 0; i < rows.size(); i++) {
                if (Objects.equals(rows.get(i).getId(), newState.getId())) {
                    rows.set(i, newState);
                    fireTableRowsUpdated(i, i);
                    break;
                }
            }
        }

        @Override
        public void onDelete(T t) {
            for (int i = 0; i < rows.size(); i++) {
                if (Objects.equals(rows.get(i).getId(), t.getId())) {
                    rows.remove(i);
                    fireTableRowsDeleted(i, i);
                    break;
                }
            }
        }
    };
}
