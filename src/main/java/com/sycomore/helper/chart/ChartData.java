package com.sycomore.helper.chart;

import java.awt.Color;

/**
 * @author Esaie MUHASA
 *
 */
public interface ChartData extends DisposableItem {
	
	/**
	 * Conservation d'une reference d'un object dans un ChartData
	 */
	void setData(Object data);
	
	/**
	 * Modification de l'épaisseur de la bordure
	 */
	void setBorderWidth (float borderWidth);
	
	/**
	 * Modification de la coulteur de bordure
	 */
	void setBorderColor(Color borderColor);
	
	/**
	 * Modification de la couleur de premier plan (couleur de texte)
	 */
	void setForegroundColor (Color foregroundColor);
	
	/**
	 * Modification de la couleur d'arrière-plan
	 */
	void setBackgroundColor (Color backgroundColor);
	
	/**
	 * Renvoie la couleur du premier plan (couleur de texte)
	 */
	Color getForegroundColor ();
	
	/**
	 * Renvoie la couleur d'arrière-plan
	 */
	Color getBackgroundColor ();
	
	/**
	 * Renvoie la couleur de bordure
	 */
	Color getBorderColor ();
	
	/**
	 * Renvoie l'épaisseur de la bordure
	 */
	float getBorderWidth();

	/**
	 * Est-ce que le part est visible ?
	 */
	boolean isVisible ();
	
	/**
	 * Modification de la visibilité du ChartData
	 */
	void setVisible(boolean visible);
	
	/**
	 * Renvoie la reference d'un object conserver dans le ChartData
	 */
	Object getData ();
	
	/**
	 * Ajout d'un listener
	 */
	void addRenderedListener (ChartDataRenderedListener listener);
	
	/**
	 * Suppression d'un listener
	 */
	boolean removeRenderedListener (ChartDataRenderedListener listener);
}
