package com.sycomore.view.components.navigation;

import com.sycomore.entity.SchoolYear;

public interface SidebarMoreOptionListener {

    /**
     * Action de demande de creation d'une nouvelle année scolaire
     */
    void doNewYear ();

    /**
     * Demande de fermeture de l'application
     */
    void doLogout ();

    /**
     * Action de demande de changement de l'année scolaire en cours de consultation.
     */
    void doLoadYear (SchoolYear year);

    /**
     * Action de demande de visualisation de la boite de dialogue de gestion des filières
     * et classe d'étude
     */
    void doOpenOptions();
}
