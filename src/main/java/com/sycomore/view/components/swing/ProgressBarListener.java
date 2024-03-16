package com.sycomore.view.components.swing;

/**
 * @author Esaie Muhasa
 * Interface d'ecoute des changements d'etat du model du bar de progression
 */
public interface ProgressBarListener {
	
	/**
	 * Lors du changement d'etat du model des donnees
	 */
	void onChange (ProgressBarModel model);

}
