package com.sycomore.helper.chart;

/**
 * 
 * @author Esaie MUHASA
 *
 */
public interface PiePart extends ChartData {
	
	/**
	 * Renvoie la valeur exacte du part
	 */
	double getValue ();

	/**
	 * Modification d'une valeur d'un PIE part
	 */
	void setValue (double value);
	
	/**
	 * Renvoie le label du part
	 */
	String getLabel ();
	void setLabel (String label);
	
	/**
	 * Renvoi le nom du part.
	 * Il est preferable de ce nom sois unique dans la collection des parts dans le model
	 */
	String getName ();

	/**
	 * Modification du nom d'un part
	 */
	void setName (String name);

	/**
	 * Écoute du change de l'état d'un part.
	 */
	void addPartListener(PiePartListener listener);

	/**
	 * Désabonnement aux changes d'un Pie-Part.
	 */
	void removePartListener(PiePartListener listener);

	/**
	 * Action qui compte le nombre d'écouteurs au changement d'un Pie-Part.
	 */
	int countPieListener ();
}
