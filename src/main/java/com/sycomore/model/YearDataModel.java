package com.sycomore.model;

import com.sycomore.entity.SchoolYear;
import com.sycomore.helper.event.ProgressEmitter;
import com.sycomore.helper.event.ProgressListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilitaire de mis memoire des touts les données relatifs a une année scolaire
 */
public class YearDataModel implements ProgressEmitter {

    private static YearDataModel instance;

    private final List<ProgressListener> progressListeners = new ArrayList<>();

    private SchoolYear year;

    private Thread thread;

    private YearDataModel () {}

    public static YearDataModel getInstance() {
        if (instance == null)
            instance = new YearDataModel();

        return instance;
    }

    public SchoolYear getYear() {
        return year;
    }

    public void setYear(SchoolYear year) {
        this.year = year;
        reload();
    }

    /**
     * Action de rechargement de donnee du model
     */
    private synchronized void doReload () {}

    /**
     * Demande de rechargement des donnees du model.
     * L'opération de chargement des donnees est faite dans un nouveau thread.
     */
    public synchronized void reload () {
        if (thread == null || thread.getState() == Thread.State.TERMINATED) {
            thread = new Thread(this::doReload);
            thread.start();
        }
    }

    @Override
    public void addProgressListener(ProgressListener listener) {
        if (!progressListeners.contains(listener))
            progressListeners.add(listener);
    }

    @Override
    public void removeProgressListener(ProgressListener listener) {
        progressListeners.remove(listener);
    }
}
