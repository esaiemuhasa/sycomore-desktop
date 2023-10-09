package com.sycomore.dao;

import com.sycomore.entity.PersistableEntity;

/**
 * Interface d'émission des événements par les interfaces du DAO.
 * @param <T>
 */
public interface RepositoryListener <T extends PersistableEntity> {
    void onCreate (T t);

    void onUpdate (T oldState, T newState);

    /**
     * Événement émis lors du changement d'état d'une entité (creation, update or delete)
     */
    void onAlter (T oldState, T newState);

    void onDelete (T t);
}
