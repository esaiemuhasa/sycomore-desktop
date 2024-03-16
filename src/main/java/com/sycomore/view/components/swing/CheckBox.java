package com.sycomore.view.components.swing;

import javax.swing.Icon;
import javax.swing.JCheckBox;

/**
 * @author Esaie MUHASA
 */
public class CheckBox <T> extends JCheckBox {
	
	private T data;

	public CheckBox(T data) {
		super(data.toString());
		this.data = data;
	}

	public CheckBox(Icon icon, T data) {
		super(icon);
		this.data = data;
	}

	public CheckBox(String text, T data) {
		super(text);
		this.data = data;
	}


	public CheckBox(Icon icon, boolean selected) {
		super(icon, selected);
	}

	public CheckBox(String text, boolean selected) {
		super(text, selected);
	}

	public CheckBox(String text, Icon icon) {
		super(text, icon);
	}

	public CheckBox(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

}
