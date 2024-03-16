package com.sycomore.helper.chart;

public interface DisposableItem {

    /**
     * Démande de mise hors service d'un part.
     */
    void dispose ();

    /**
     * Remise en activité d'un part
     */
    void resume ();

    /**
     * Utilitaire de verification si un composant est en pause.
     */
    boolean isDisposed ();

    /**
     * Abonnement d'un écouteur de changement d'occupation d'un composant
     */
    void addDisposableItemListener (DisposableItemListener listener);

    /**
     * Désabonnement d'un écouteur des changements d'occupation d'un composant.
     */
    void removeDisposableItemListener (DisposableItemListener listener);
}
