package com.sycomore.dao;

/**
 * Helper de l'interface d'émission des événements par l'une interface du DAO
 * @param <T>
 */
public abstract class RepositoryAdapter <T> implements RepositoryListener<T> {
    @Override
    public void onCreate(T t) {}

    @Override
    public void onUpdate(T oldState, T newState) {}

    @Override
    public void onAlter(T oldState, T newState) {}

    @Override
    public void onDelete(T t) {}
}
