/**
 * 
 */
package com.sycomore.helper.chart;

/**
 * @author Esaie MUHASA
 *
 */
public interface PieModelListener {
	
	/**
	 * method utilitaire appeler pour refrechier la vue
	 */
	void refresh (PieModel model);
	
	/**
	 * pour demander a la vue de redessiner le part en parametre
	 */
	void repaintPart (PieModel model, int partIndex);
	
	/**
	 * Lors du changement de l'index selectionner
	 */
	void onSelectedIndex (PieModel model, int oldIndex, int newIndex);
	
	/**
	 * lors du changement du titre
	 */
	void onTitleChange (PieModel  model, String title);

}
