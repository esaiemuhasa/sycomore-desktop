package com.sycomore.helper.event;

/**
 * Comportement d'une classe susceptible d'émettre des événements, lors qu'il réalise de tache en arriere plan.
 */
public interface ProgressEmitter {

    /**
     * Abonnement d'un listener
     */
    void addProgressListener(ProgressListener listener);

    /**
     * Désabonnement d'un listener
     */
    void removeProgressListener(ProgressListener listener);
}
