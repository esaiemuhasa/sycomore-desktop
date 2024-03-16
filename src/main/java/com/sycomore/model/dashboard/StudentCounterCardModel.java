package com.sycomore.model.dashboard;

import com.sycomore.helper.Config;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.view.components.swing.DefaultCardModel;


/**
 * Model des données du card compteur du nombre d'élèves inscrit pour une année scolaire.
 * Cette classe implémente le model singleton.
 */
public class StudentCounterCardModel extends DefaultCardModel<Integer> {

    private static StudentCounterCardModel instance;
    private final YearDataModel dataModel = YearDataModel.getInstance();

    private StudentCounterCardModel() {}

    /**
     * Initialisation du model des données
     */
    private void init () {
        icon = Config.getIcon("dashboard/toge");
        info = "Total élèves inscrits";
        title = "Elèves";
        value = 0;

        dataModel.addYearDataListener(new YearDataModelAdapter() {
            @Override
            public void onLoadFinish() {
                setValue(dataModel.getInscriptionsCount());
            }
        });
    }

    /**
     * Utilitaire de recuperation de l'instance unique du model des données
     */
    public static StudentCounterCardModel getInstance() {
        if (instance == null) {
            instance = new StudentCounterCardModel();
            instance.init();
        }
        return instance;
    }
}
