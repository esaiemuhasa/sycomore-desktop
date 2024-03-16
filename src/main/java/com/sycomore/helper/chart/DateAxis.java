package com.sycomore.helper.chart;

import com.sycomore.helper.Config;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateAxis extends DefaultAxis{
	
	public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private DateFormat formater;

	public DateAxis() {
		super();
		formater = new SimpleDateFormat("dd/MM/yyyy");
	}

	public DateAxis(Color backgroundColor, Color foregroundColor, Color borderColor) {
		super(backgroundColor, foregroundColor, borderColor);
		formater = new SimpleDateFormat("dd/MM/yyyy");
	}

	public DateAxis(Color backgroundColor) {
		super(backgroundColor);
		formater = new SimpleDateFormat("dd/MM/yyyy");
	}

	public DateAxis(DateFormat formater) {
		super();
		this.formater = formater;
	}

	/**
	 * @param name
	 * @param shortName
	 * @param measureUnit
	 */
	public DateAxis(String name, String shortName, String measureUnit) {
		super(name, shortName, measureUnit);
		formater = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public DateAxis(DateFormat formater, String name, String shortName, String measureUnit) {
		super(name, shortName, measureUnit);
		this.formater = formater;
	}

	public void setFormater(DateFormat formater) {
		if(this.formater == formater)
			return;
		
		this.formater = formater;
		fireOnChange();
	}
	
	@Override
	public String getLabelOf (double value) {
		long millis = System.currentTimeMillis() + (((long) value) * 1000L * 60L * 60L * 24L);
		Date date = new Date(millis);
		Date max = Config.toMiddleTimestampOfDay(date);
		return formater.format(max);
	}

	/**
	 * @return the formater
	 */
	public DateFormat getFormater() {
		return formater;
	}
}
