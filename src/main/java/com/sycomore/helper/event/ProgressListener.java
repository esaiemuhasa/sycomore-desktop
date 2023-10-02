package com.sycomore.helper.event;

/**
 * Interface d'émission de l'évolution d'une tâche en arrière-plan
 */
public interface ProgressListener {
    int MAX_INDETERMINATE = -1;

    /**
     * Action de démarrage d'une nouvelle tâche
     * @param progressKey la clée qui indentifi le processuce encours execution
     */
    void onStart (int progressKey);

    /**
     * Pour chaque progression de traitement
     * @param progressKey indentifient du procéssuce
     * @param value etat de progression du processuce
     * @param max maximum des opérations qui seront faites
     * @param message message a afficher, ou null
     */
    void onProgress (int progressKey, int value, int max, String message);

    /**
     * Événement de fins de progression des taches en arriere plan
     * @param progressKey clé de la tache en cours de progression
     */
    void onFinish (int progressKey);

    /**
     * Lors qu'une erreur survient dans le processuce de traitement.
     */
    void onError (Throwable error);
}
