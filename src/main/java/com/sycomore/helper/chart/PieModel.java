package com.sycomore.helper.chart;

/**
 * @author Esaie MUHASA
 */
public interface PieModel {
	
	void setTitle (String title);
	String getTitle ();	
	
	//adding parts
	void addPart (PiePart part);
	void addPart (int index, PiePart part);
	void addParts (PiePart ...parts);
	// --
	
	/**
	 * Renvoie la valeur max. L'équivalent de 360°
	 */
	double getMax ();
	void setMax (double max);

	/**
	 * Les parts doit-elle être selectable ??
	 */
	void setSelectablePart (boolean selectablePart);

	/**
	 * Les parts sont-elles selectable ??
	 */
	boolean isSelectablePart ();

	/**
	 * La priorité du real max est plus élevé que celui du max fixé par l'utilisateur
	 */
	boolean isRealMaxPriority();
	
	double getRealMax();
	String getSuffix ();
	
	/**
	 * Recuperation du model d'un part a l'index en paramètre
	 */
	PiePart getPartAt (int index);
	
	/**
	 * Renvoie l'index du part en paramètre
	 * renvoie -1 dans le cas où le part n'appartient pas au model de la pie
	 */
	int indexOf (PiePart part);
	
	/**
	 * Modification de l'index sélectionné
	 */
	void setSelectedIndex (int index) throws IndexOutOfBoundsException;
	
	/**
	 * Renvoie l'index sélectionné ou -1 si aucun index n'est sélectionné
	 */
	int getSelectedIndex ();
	
	/**
	 * Renvoi les pourcentages de l'index a l'index
	 */
	double getPercentOf (int index);
	
	/**
	 * Renvoie le pourcentage du part en paramètre, si cel-ci se trouve dans le model
	 */
	double getPercentOf (PiePart part);
	
	/**
	 * Renvoie la somme des pourcents de parts
	 */
	double getSumPercent ();
	
	/**
	 * Renvoie objet le part qui a une reference vers l'objet en paramètre
	 */
	PiePart findByData (Object data);
	
	/**
	 * Renvoie le nombre des parts
	 */
	int getCountPart();
	
	/**
	 * Recuperation du part dont le nom est en paramètre
	 */
	PiePart getPartByName (String name);
	
	/**
	 * Suppression du part à l'index en paramètre
	 */
	void removePartAt (int index);

	/**
	 * Démande de suppression d'un part dont la reference est en paramètre
	 */
	void removePart (PiePart part);
	
	/**
	 * Suppression du part dont la donnee est en paramètre
	 */
	boolean removeByData (Object data);
	
	void removeAll ();
	PiePart [] getParts ();
	
	/**
	 * recuperation du contenue d'un model en un tableau des double
	 */
	static Double [] toValuesArray(PieModel model) {
		Double [] values = new Double[model.getCountPart()];
		for (int i = 0; i < values.length; i++)
			values[i] = model.getPartAt(i).getValue();
		return values;
	}
	
	/**
	 * Renvoie le tableau des labels d'un model
	 */
	static String [] toLabelsArray(PieModel model) {
		String [] labels = new String[model.getCountPart()]; 
		for (int i = 0; i < labels.length; i++)
			labels[i] = model.getPartAt(i).getLabel();
		return labels;
	}
	
	void addListener (PieModelListener listener);
	void removeListener (PieModelListener listener);
}
