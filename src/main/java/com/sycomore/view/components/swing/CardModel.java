package com.sycomore.view.components.swing;

import java.awt.Color;

/**
 * @author Esaie MUHASA
 * Model des donnees d'un carde
 */
public interface CardModel <T extends Number> {
	
	/**
	 * Renvoie la valeur du card
	 */
	T getValue ();
	
	String getSuffix ();
	
	/**
	 * Renvoie le titre principal du card
	 */
	String getTitle ();
	
	/**
	 * Renvoie le sous-titre du card
	 */
	String getInfo ();
	
	/**
	 * Renvoie l'adresse vers l'icône du card
	 */
	String getIcon ();
	
	/**
	 * Renvoie la couleur des textes
	 */
	Color getForegroundColor ();
	
	/**
	 * Modification de la couleur de text
	 */
	void setForegroundColor (Color color);
	
	/**
	 * Renvoie la couleur d'arrière-plan du card
	 */
	Color getBackgroundColor ();
	
	/**
	 * Modification la couleur d'arrière-plan
	 */
	void setBackgroundColor (Color color);
	
	/**
	 * Désabonnement d'un écouteur
	 */
	void removeListener (CardModelListener listener);
	
	/**
	 * Branchement d'un écouteur.
	 */
	void addListener (CardModelListener listener);

}
