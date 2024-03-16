package com.sycomore.helper.chart;

import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Esaie MUHASA
 *
 */
public abstract class AbstractChartData implements ChartData {

	private final List<DisposableItemListener> disposableItemListeners = new ArrayList<>();
	protected final List<ChartDataRenderedListener> listListeners = new ArrayList<>();

	protected Color backgroundColor;
	protected Color foregroundColor;
	protected Color borderColor;
	protected boolean visible;
	protected Object data;
	protected float borderWidth;

	protected boolean disposed = false;


	public AbstractChartData() {
		this.backgroundColor = UIManager.getColor("Component.background");
		this.foregroundColor = UIManager.getColor("Component.foreground");
		this.borderColor = UIManager.getColor("Component.borderColor");
		this.visible = true;
		this.borderWidth = 2;
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void setBorderWidth(float borderWidth) {
		float old = this.borderWidth;
		if(old  == borderWidth)
			return;
		
		this.borderWidth = borderWidth;
		fireBorderWithChange(old);
	}

	@Override
	public void setBorderColor(Color borderColor) {
		Color old = this.borderColor;
		if(this.borderColor.equals(borderColor))
			return;
		
		this.borderColor = borderColor;
		fireBorderColorChange(old);
	}

	@Override
	public void setForegroundColor(Color foregroundColor) {
		Color old = this.foregroundColor;
		if(old != null && this.foregroundColor.equals(foregroundColor))
			return;
		
		this.foregroundColor = foregroundColor;
		fireForegroundColorChange(old);
	}

	@Override
	public void setBackgroundColor(Color backgroundColor) {
		Color old = this.backgroundColor;
		if(old != null && this.backgroundColor.equals(backgroundColor))
			return;
		
		this.backgroundColor = backgroundColor;
		fireBackgroundColorChange(old);
	}

	@Override
	public Color getForegroundColor() {
		return foregroundColor;
	}

	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public Color getBorderColor() {
		return borderColor;
	}

	@Override
	public float getBorderWidth() {
		return borderWidth;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		if(this.visible == visible)
			return;
		this.visible = visible;
		fireOnChange();
	}

	@Override
	public Object getData() {
		return data;
	}


	@Override
	public void dispose() {
		fireOnDispose();
	}

	@Override
	public void resume() {
		fireOnResume();
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}

	@Override
	public void addRenderedListener (ChartDataRenderedListener listener) {
		if(listListeners.contains(listener))
			return;
		
		listListeners.add(listener);
	}

	@Override
	public boolean removeRenderedListener(ChartDataRenderedListener listener) {
		return listListeners.remove(listener);
	}

	@Override
	public void addDisposableItemListener(DisposableItemListener listener) {
		if (!disposableItemListeners.contains(listener))
			disposableItemListeners.add(listener);
	}

	@Override
	public void removeDisposableItemListener(DisposableItemListener listener) {
		disposableItemListeners.remove(listener);
	}

	/**
	 * Emission du changement de la couleur de bordure
	 */
	protected synchronized void fireBorderColorChange(Color oldColor) {
		Object[] ls = listListeners.toArray();
		for (Object o : ls)
			((ChartDataRenderedListener)o).onBorderColorChagne(this, oldColor);
	}
	
	protected synchronized void fireBorderWithChange(float oldWidth) {
		Object[] ls = listListeners.toArray();
		for (Object o : ls)
			((ChartDataRenderedListener)o).onBorderWidthChange(this, oldWidth);
	}
	
	/**
	 * Emission du changement de la couleur du premier plan
	 */
	protected synchronized void fireForegroundColorChange(Color oldColor) {
		Object[] ls = listListeners.toArray();
		for (Object o : ls)
			((ChartDataRenderedListener)o).onForegroundColorChagne(this, oldColor);
	}
	
	/**
	 * Lors du changement de la couleur du back
	 */
	protected synchronized void fireBackgroundColorChange(Color oldColor) {
		Object[] ls = listListeners.toArray();
		for (Object o : ls)
			((ChartDataRenderedListener)o).onBackgroundColorChagne(this, oldColor);
	}
	
	/**
	 * Emission de changement d'etat
	 */
	protected synchronized void fireOnChange() {
		Object[] ls = listListeners.toArray();
		for (Object o : ls)
			((ChartDataRenderedListener)o).onChange(this);
	}

	protected synchronized void fireOnDispose() {
		disposed = true;
		Object[] ls = disposableItemListeners.toArray();
		for (Object l : ls)
			((DisposableItemListener)l).onDispose(this);
	}

	protected synchronized void fireOnResume () {
		disposed = false;
		Object[] ls = disposableItemListeners.toArray();
		for (Object l : ls)
			((DisposableItemListener)l).onResume(this);
	}

}
