package com.sycomore.view.components.swing;

import java.awt.Color;

/**
 * @author Esaie Muhasa
 * 
 * Interface qui decrit les comportements de base d'un model des donnees d'une bar de progression.
 */
public interface ProgressBarModel {
	
	/**
	 * Renvoie la valeur actuelle de la bar de progression
	 */
	double getValue ();
	
	/**
	 * Renvoie le maximum possible de la bar de progression
	 */
	double getMax ();
	
	/**
	 * Renvoie le pourcentage de value sur max
	 */
	double getValueToPercent();
	
	/**
	 * Renvoie le text à afficher dans la bar de progression
	 */
	String getCaption();
	
	/**
	 * Renvoie la couleur d'arrière-plan numéro 1
	 */
	Color getBackground1 ();
	
	/**
	 * Renvoie deuxième couleur d'arrière-plan
	 */
	Color getBackground2 ();
	
	/**
	 * Renvoie la couleur du premier plan
	 */
	Color getForeground ();
	
	/**
	 * Renvoie la couleur de bordure du progress bar
	 */
	Color getBorderColor ();
	
	/**
	 * Ajout d'un écouteur des changements du model du progress model.
	 */
	void addListener (ProgressBarListener listener);
	
	/**
	 * Désabonnement d'un écouteur.
	 */
	void removeListener (ProgressBarListener listener);

}
