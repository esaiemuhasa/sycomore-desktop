package com.sycomore.model;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.RepositoryAdapter;
import com.sycomore.dao.SchoolYearRepository;
import com.sycomore.entity.Inscription;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.SchoolYear;
import com.sycomore.helper.event.ProgressEmitter;
import com.sycomore.helper.event.ProgressListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Utilitaire de mis memoire des touts les données relatifs a une année scolaire
 */
public class YearDataModel implements ProgressEmitter {

    private static YearDataModel instance;

    private boolean started = false;

    private final List<ProgressListener> progressListeners = new ArrayList<>();
    private final List<YearDataModelListener> yearDataModelListeners = new ArrayList<>();

    private SchoolYear year;

    private Thread thread;

    //donnees mis en cache
    private final List<SchoolYear> years = new ArrayList<>();
    private final List<Promotion> promotions = new ArrayList<>();
    private final List<Inscription> inscriptions = new ArrayList<>();
    //==================

    private final SchoolYearRepository yearRepository;

    private YearDataModel () {
        yearRepository = DAOFactory.getInstance(SchoolYearRepository.class);

        yearRepository.addRepositoryListener(new RepositoryAdapter<SchoolYear>() {
            @Override
            public void onCreate(SchoolYear year) {
                //lors de la creation d'une nouvelle année scolaire, on charge directement l'année en question
                instance.year = year;
                reload();
            }

            @Override
            public void onUpdate(SchoolYear oldState, SchoolYear newState) {
                if (year == null)
                    return;

                if (Objects.equals(newState.getId(), year.getId()))
                    instance.year = newState;
            }


            @Override
            public void onDelete(SchoolYear year) {
                if (instance.year == null)
                    return;

                //lors de la suppression si l'année actuel vient d'étre supprimé, alors on recharge la dernière année qui existe en BDD
                if (Objects.equals(year.getId(), instance.year.getId())) {
                    SchoolYear current = yearRepository.findOneLatest();
                    if (current != null) {
                        setYear(current);
                    }
                }
            }
        });
    }

    public static YearDataModel getInstance() {
        if (instance == null)
            instance = new YearDataModel();

        return instance;
    }

    public SchoolYear getYear() {
        return year;
    }

    /**
     * Mutation de l'année scolaire actuel
     */
    public synchronized void setYear(SchoolYear year) {
        if (!started) {
            doSetup();
            return;
        }

        if (Objects.equals(year, this.year))
            return;

        this.year = year;
        reload();
    }

    /**
     * Action de demande d'initialisation du model des données annuel
     */
    public synchronized void setup () {
        doSetup();
    }

    /**
     * Initialisation du model des données annuel
     */
    protected void doSetup () {
        if (started)
            return;

        started = true;

        //chargement de la liste des années scolaires
        SchoolYear [] years = yearRepository.findAll();
        if (years != null) {
            this.years.addAll(Arrays.asList(years));

            for (SchoolYear y : years) {
                if (!y.isArchived()) {
                    year = y;
                    break;
                }
            }

            if (year == null){
                year = years[0];
            }
        }

        fireSetup();
        reload();
    }

    /**
     * Renvoie la liste des années scolaires
     */
    public SchoolYear [] getYears () {
        if (years.isEmpty())
            return null;

        int size = years.size();
        return years.toArray(new SchoolYear[size]);
    }

    /**
     * Action de rechargement de donnee du model
     */
    private synchronized void doReload () {
        fireLoadStart();

        fireLoadFinish();
    }

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

    /**
     * Emission de l'évènement de debut de chargement des donnees du model
     */
    protected void fireLoadStart() {
        Object [] listeners = yearDataModelListeners.toArray();
        for (Object ls : listeners)
            ((YearDataModelListener) ls).onLoadStart();
    }

    /**
     * Emission de l'évènement de fin de chargement des données du model
     */
    protected void fireLoadFinish() {
        Object [] listeners = yearDataModelListeners.toArray();
        for (Object ls : listeners)
            ((YearDataModelListener) ls).onLoadFinish();
    }

    protected void fireSetup () {
        Object [] listeners = yearDataModelListeners.toArray();
        for (Object ls : listeners)
            ((YearDataModelListener) ls).onSetup();
    }

    public void addYearDataListener (YearDataModelListener listener) {
        if (!yearDataModelListeners.contains(listener))
            yearDataModelListeners.add(listener);
    }

    public void removeYearDataListener (YearDataModelListener listener) {
        yearDataModelListeners.remove(listener);
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
