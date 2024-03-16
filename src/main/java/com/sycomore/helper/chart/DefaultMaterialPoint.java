package com.sycomore.helper.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Esaie MUHASA
 *
 */
public class DefaultMaterialPoint extends AbstractChartData implements MaterialPoint {
	
	protected final List<PointListener> listeners = new ArrayList<>();
	
	private double x;
	private double y;
	private float size = 1;
	private String labelX;
	private String labelY;

	public DefaultMaterialPoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructeur de copie.
	 * On recopie seulement les valeurs des cordonnees sur les axes
	 * et les donnees qui cadre avec le rendu du point (couleur, taille, ...)
	 */
	public DefaultMaterialPoint (MaterialPoint point) {
		super();

		borderWidth = point.getBorderWidth();
		backgroundColor = point.getBackgroundColor();
		foregroundColor = point.getForegroundColor();
		borderColor = point.getBorderColor();

		x = point.getX();
		y = point.getY();
		size = point.getSize();
		data = point.getData();
		labelX = point.getLabelX();
		labelY = point.getLabelY();
	}

	/**
	 */
	public DefaultMaterialPoint(double x, double y, float size) {
		super();
		this.x = x;
		this.y = y;
		this.size = size;
	}

	/**
	 * @param x the x to set
	 */
	public synchronized void setX(double x) {
		if(this.x == x)
			return;
		
		this.x = x;
		fireOnChange();
	}

	/**
	 * @param y the y to set
	 */
	public synchronized void setY(double y) {
		if(this.x == y)
			return;
		
		this.y = y;
		fireOnChange();
	}

	@Override
	public void translate(double x, double y, double z) {
		if(this.x == x && this.y == y)
			return;
		
		this.x = x;
		this.y = y;
		fireOnChange();
	}

	/**
	 * @param size the size to set
	 */
	public synchronized void setSize(float size) {
		if(this.size == size)
			return;
		
		this.size = size;
		fireOnChange();
	}

	@Override
	public double getX () {
		return x;
	}

	@Override
	public double getY () {
		return y;
	}

	@Override
	public double getZ() {
		return 0;
	}

	@Override
	public float getSize() {
		return size;
	}

	@Override
	public String getLabelX() {
		if(labelX == null)
			return PieRender.DECIMAL_FORMAT.format(x);
		return labelX;
	}
	
	@Override
	public String getLabelY() {
		if(labelY == null)
			return PieRender.DECIMAL_FORMAT.format(y);
		return labelY;
	}
	
	@Override
	public String getLabelZ() {
		throw new RuntimeException("La 3D n'est pas pris en charge");
	}

	/**
	 * @param labelX the labelX to set
	 */
	public void setLabelX (String labelX) {
		if(Objects.equals(labelX, this.labelX))
			return;
		
		this.labelX = labelX;
		fireOnChange();
	}
	
	public void setLabelY(String labelY) {
		if(Objects.equals(this.labelY, labelY))
			return;
		
		this.labelY = labelY;
		fireOnChange();
	}

	@Override
	public void addPointListener(PointListener listener) {
		if(!listeners.contains(listener) && listener != null)
			listeners.add(listener);
	}

	@Override
	public void removePointListener(PointListener listener) {
		listeners.remove(listener);
	}
	
	protected synchronized void fireOnChange() {
		for (PointListener ls : listeners)
			ls.onChange(this);
	}

	@Override
	public String toString() {
		return "DefaultMaterialPoint [x=" + x + ", y=" + y + ", labelX="+labelX+", labelY="+labelY+ "]";
	}

	@Override
	public boolean equals (Object obj) {
		if(obj instanceof MaterialPoint) {
			MaterialPoint point = (MaterialPoint) obj;
			return point.getX() == x && point.getY() == y;
		}
		
		return super.equals(obj);
	}

}
