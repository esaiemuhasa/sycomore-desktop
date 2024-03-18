package com.sycomore.entity.helper;

import com.sycomore.entity.PersistableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * Paramètre pour faciliter la sauvegarde des états qui ne font pas partie des exigences fonctionnelles du logiciel.
 * Il peut s'agir de la dernière position de la fenêtre sur l'écran de l'utilisateur, la dernière vue en cours de consultation avant fermeture du soft, etc.
 * Le present entité est mise en place pour prendre en charge facilement certaine (ou la plus par) des exigences techniques.
 */
@Entity
@Table(name = "parameter")
public class Parameter extends PersistableEntity {

    public static final String PARAM_MAIN_WINDOW_INSET = "MAIN_WINDOW_INSET";
    public static final String PARAM_MAIN_WINDOW_TYPE = "MAIN_WINDOW_TYPE";

    @Column(name = "parameter_name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "parameter_value")
    @Lob
    private String value;

    @Column()
    private String caption;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    /**
     * Utilitaire de conversion de la valeur sauvegardé sous forme d'une chaine de caractère, en un double
     */
    public double getValueAsDouble () {
        if (value == null)
            return 0;
        return Double.parseDouble(value);
    }

    /**
     * Utilitaire de conversion de la valeur sauvegardé sous forme d'une chaine de caractère, en un int
     */
    public int getValueAsInt () {
        if (value == null)
            return 0;
        return Integer.parseInt(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
