package com.sycomore.view.components.swing;

import javax.swing.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Esaie MUHASA
 */
public class DefaultCardModel <T extends Number> implements CardModel <T> {
	
	private T value;
	private String title = "";
	private String icon;
	private String info = "";
	private String suffix="";
	
	private final List<CardModelListener> listeners = new ArrayList<>();
	
	private Color backgroundColor = UIManager.getColor("Component.background2");
	private Color foregroundColor = UIManager.getColor("Component.foreground");
	
	public DefaultCardModel() {
		super();
	}

	@Override
	public Color getForegroundColor() {
		return foregroundColor;
	}

	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		if (this.backgroundColor == backgroundColor)
			return;
		
		Color oldColor = this.backgroundColor;
		this.backgroundColor = backgroundColor;
		for (CardModelListener ls : listeners) 
			ls.onColorChange(this, 2, oldColor);
	}

	/**
	 * @param foregroundColor the foregroundColor to set
	 */
	public void setForegroundColor(Color foregroundColor) {
		if (this.foregroundColor == foregroundColor)
			return;
		
		Color old = this.foregroundColor;
		this.foregroundColor = foregroundColor;
		for (CardModelListener ls : listeners) 
			ls.onColorChange(this, 1, old);
	}
	
	/**
	 * Lors du changement de la value
	 */
	protected void fireOnValueChange (T old) {
		for (CardModelListener ls : listeners) 
			ls.onValueChange(this, old);
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(T value) {
		if (Objects.equals(value, this.value))
			return;
		T old = this.value;
		this.value = value;
		fireOnValueChange(old);
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		if(Objects.equals(this.title, title))
			return;
		
		String old = this.title;
		this.title = title;
		for (CardModelListener ls : listeners) 
			ls.onTitleChange(this, 1, old);
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		if(Objects.equals(this.icon, icon))
			return;
		
		String old = this.icon;
		this.icon = icon;
		for (CardModelListener ls : listeners) 
			ls.onIconChange(this, old);
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		if(Objects.equals(this.info, info))
			return;
		
		String old = this.info;
		this.info = info;
		for (CardModelListener ls : listeners) 
			ls.onTitleChange(this, 2, old);
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public String getIcon() {
		return icon;
	}

	/**
	 * @return the suffix
	 */
	@Override
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix (String suffix) {
		this.suffix = suffix;
		for (CardModelListener ls : listeners) 
			ls.onChange(this);
	}

	@Override
	public void removeListener(CardModelListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void addListener(CardModelListener listener) {
		if (listener != null && !listeners.contains(listener))
			listeners.add(listener);
	}

}
