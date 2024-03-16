package com.sycomore.view.components.swing;

import javax.swing.UIManager;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Esaie Muhasa
 * Model par defaut d'un pogresse bar
 */
public class DefaultProgressBarModel implements ProgressBarModel {
	
	private final List<ProgressBarListener> listeners = new ArrayList<>();
	private double value;
	private double max;
	private String caption="";
	
	private double percent;
	
	private Color background1;
	private Color background2;
	private Color foreground;
	private Color borderColor;

	/**
	 * Constructeur par défaut
	 */
	public DefaultProgressBarModel() {
		max = 100;
		value = 0;
		background1 = Color.BLACK;
		background2 = Color.BLACK.brighter().brighter();
		borderColor = UIManager.getColor("Component.borderColor");
		foreground = UIManager.getColor("Component.foreground");
	}

	public DefaultProgressBarModel(double value, Color background1, Color background2, Color foreground, Color borderColor) {
		super();
		this.value = value;
		max = 100;
		this.background1 = background1;
		this.background2 = background2;
		this.foreground = foreground;
		this.borderColor = borderColor;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public double getMax() {
		return max;
	}

	@Override
	public String getCaption() {
		return caption;
	}

	@Override
	public Color getBackground1() {
		return background1;
	}

	@Override
	public Color getBackground2() {
		return background2;
	}
	
	@Override
	public Color getForeground() {
		return foreground;
	}
	
	@Override
	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		if (Objects.equals(this.caption, caption))
			return;
		
		this.caption = caption;
		fireChange();
	}

	/**
	 * @param background1 the background1 to set
	 */
	public void setBackground1(Color background1) {
		this.background1 = background1;
		fireChange();
	}

	/**
	 * @param background2 the background2 to set
	 */
	public void setBackground2(Color background2) {
		this.background2 = background2;
		fireChange();
	}

	/**
	 * @param foreground the foreground to set
	 */
	public void setForeground(Color foreground) {
		this.foreground = foreground;
		fireChange();
	}

	/**
	 * @param borderColor the borderColor to set
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		fireChange();
	}

	/**
	 * Mutation de la valeur
	 */
	public void setValue(double value) {
		if (value == this.value)
			return;
		
		if (value < 0)
			throw new IllegalArgumentException("Les valeurs negatives ne sont pas pris en charge");
		
		this.value = value;
		refreshPercent();
		fireChange();
	}

	/**
	 * Mutation du maximum
	 */
	public void setMax(double max) {
		if (max == this.max)
			return;
		
		if (max < 0)
			throw new IllegalArgumentException("Les valeurs negatives ne sont pas pris en charge");
		
		this.max = max;
		refreshPercent();
		fireChange();
	}
	
	/**
	 * Mis en jour des values
	 */
	public void update (double value, double max) {
		if (value == this.value && max == this.max)
			return;
		
		this.value = value;
		this.max = max;
		
		refreshPercent();
		fireChange();
	}
	
	/**
	 * Mis en jour de value, max et de la chaine de caractère
	 */
	public void update (double value, double max, String caption) {
		if (value == this.value && max == this.max)
			return;
		
		this.value = value;
		this.max = max;
		this.caption = caption;
		
		refreshPercent();
		fireChange();
	}
	
	@Override
	public double getValueToPercent() {
		return percent;
	}

	@Override
	public void addListener(ProgressBarListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	@Override
	public void removeListener(ProgressBarListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Utilitaire qui recalcule la valeur en pourcentage
	 */
	private void refreshPercent () {
		if (max == 0) {
			percent = 0;
			return;
		}
		percent = (value * 100.0d) / max;
	}
	
	/**
	 * Emission de l'évènement de changement d'etat du model
	 */
	protected synchronized void fireChange () {
		Object [] ls = listeners.toArray();
        for (Object l : ls)
			((ProgressBarListener) l).onChange(this);
	}

}
