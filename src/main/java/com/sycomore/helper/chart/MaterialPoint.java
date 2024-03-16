/**
 * 
 */
package com.sycomore.helper.chart;

/**
 * @author Esaie MUHASA
 *
 */
public interface MaterialPoint extends ChartData{
	
	/**
	 * Renvoi la valeur sur l'axe des abs
	 */
	double getX();
	
	/**
	 * Valeur sur l'axe des ord
	 */
	double getY();
	
	
	/**
	 * Valeur sur l'axe Z
	 */
	double getZ();
	
	/**
	 * Translation du point
	 */
	void translate (double x, double y, double z);
	
	/**
	 * Translation dans le plan XY
	 */
	default void translateXY (double x, double y) {
		translate(x, y, getZ());
	}
	
	/**
	 * Taille du point
	 */
	float getSize();
	
	/**
	 * Renvoie le label du point materiel sur l'axe des Xs
	 */
	String getLabelX ();
	
	/**
	 * Renvoie le label du point materiel sur l'axe des Ys
	 */
	String getLabelY ();
	
	/**
	 * Renvoie le label du point materiel sur l'axe des Zs
	 */
	String getLabelZ();
	
	/**
	 * Ajout d'un écouteur
	 */
	void addPointListener(PointListener listener);
	
	/**
	 * Désabonnement d'un listener
	 */
	void removePointListener(PointListener listener);
}
