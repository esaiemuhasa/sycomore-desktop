/**
 * 
 */
package com.sycomore.view.components.swing;

import java.awt.Color;

/**
 * @author Esaie MUHASA
 */
public interface CardModelListener {
	
	/**
	 * Lorsqu'il y a des changements majeurs
	 */
	void onChange (CardModel<?> model);
	
	/**
	 * Lors du changement de l'état de la valeur
	 */
	void onValueChange (CardModel<?> model, Object oldValue);
	
	/**
	 * Lors du changement du titre du card
	 * @param index (1 pour le titre principal et 2 pour le titre secondaire)
	 */
	void onTitleChange (CardModel<?> model, int index, String oldTitle);

	/**
	 * Lors du changement de la couleur du card
	 * @param index (1 pour foreground et 2 pour background)
	 */
	void onColorChange (CardModel<?> model, int index, Color oldColor);
	
	/**
	 * Lorsque l'URI de l'icône change
	 */
	void onIconChange (CardModel<?> model, String oldIcon);
}
