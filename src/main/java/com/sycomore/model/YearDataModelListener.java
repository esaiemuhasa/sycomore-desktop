package com.sycomore.model;

import com.sycomore.entity.SchoolYear;

/**
 * Interface d'écoute des changements d'etat du model des donnees annuel
 */
public interface YearDataModelListener {

    /**
     * Debut de chargement du model des donnees
     */
    void onLoadStart();

    /**
     * Fin de chargement du model des données
     */
    void onLoadFinish();

}
