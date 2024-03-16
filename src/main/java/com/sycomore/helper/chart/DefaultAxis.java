package com.sycomore.helper.chart;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @author Esaie MUHASA
 */
public class DefaultAxis extends AbstractChartData implements Axis {
	
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.##");
	protected String name;
	protected String shortName;
	protected String measureUnit = "";

	/**
	 * 
	 */
	public DefaultAxis() {
		super();
	}

	public DefaultAxis(String name, String shortName, String measureUnit) {
		super();
		this.name = name;
		this.shortName = shortName;
		this.measureUnit = measureUnit;
	}

	public DefaultAxis(Color backgroundColor) {
		super();
		this.backgroundColor = backgroundColor;
	}

	public DefaultAxis(Color backgroundColor, Color foregroundColor, Color borderColor) {
		this(backgroundColor);
		this.foregroundColor = foregroundColor;
		this.borderColor = borderColor;
	}
	
	/**
	 * @return the measureUnit
	 */
	@Override
	public String getMeasureUnit() {
		return measureUnit;
	}

	/**
	 * @param measureUnit the measureUnit to set
	 */
	public void setMeasureUnit(String measureUnit) {
		if(Objects.equals(measureUnit, this.measureUnit))
			return;
		
		this.measureUnit = measureUnit;
		fireOnChange();
	}
	
	@Override
	public String getLabelOf (final double value) {
		if(Double.isNaN(value))
			return "";
		return DECIMAL_FORMAT.format(value)+" "+getMeasureUnit();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getShortName() {
		return shortName;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		if(Objects.equals(this.name, name))
			return;
		
		this.name = name;
		fireOnChange();
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		if(Objects.equals(this.shortName, shortName))
			return;
		
		this.shortName = shortName;
		fireOnChange();
	}

}
