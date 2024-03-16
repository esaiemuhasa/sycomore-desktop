package com.sycomore.view.components.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.Objects;

import javax.swing.JComponent;

/**
 * @author Esaie Muhasa
 * 
 * Barre de progression personnalisé
 */
public class ProgressBar extends JComponent {
	final Font TXT_FONT = new Font("Arial", Font.BOLD, 13);
	
	private ProgressBarModel model;
	
	private GradientPaint bkg;
	private double ration;
	
	public ProgressBar() {
		model = new DefaultProgressBarModel();
		init();
		model.addListener(barListener);
	}
	
	public ProgressBar (ProgressBarModel model) {
		Objects.requireNonNull(model);
		init();
		this.model = model;
		model.addListener(barListener);
	}
	
	private void init() {
		setPreferredSize(new Dimension(200, 25));
	}
	
	public void setModel(ProgressBarModel model) {
		if (model == this.model)
			return;
		
		this.model = model;
		doChange(model);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		
		final double max = model.getMax(), value = model.getValue();
		String txt = model.getCaption();
		
		double newRation = (value * getWidth()) / max;
		
		if (ration != newRation) {
			ration = newRation;
			initBkg();
		}
		
		g2.setColor(model.getBorderColor());
		g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
		
		g2.setPaint(bkg);
		g2.fillRect(1, 1, ((int) ration), getHeight()-2);
		
		
		g2.setFont(TXT_FONT);
		g2.setColor(model.getForeground());
		FontMetrics metrics = g2.getFontMetrics();
		float txt_x = 10f, txt_y = getHeight()/2f + metrics.getHeight()/3f;
		g2.drawString(txt, txt_x, txt_y);
		
		super.paintComponent(g);
	}
	
	private void doChange (ProgressBarModel model) {
		if (this.model != model)
			return;
		
		ration = (model.getValue() * getWidth()) / model.getMax();
		initBkg();
		repaint();
	}
	
	/**
	 * Initalisation de la couleur d'ariere plan
	 */
	private void initBkg() {
		
		double ration = this.ration;
		double percent = model.getValueToPercent();
		
		if(percent < 25)
			ration *= 4;
		else if (percent <= 50)
			ration *= 2;
		
		Point2D start = new Point2D.Double(1d, getHeight()/2d),
				end = new Point2D.Double(ration, getHeight()/2d);
		
		bkg = new GradientPaint(start, model.getBackground1(), end, model.getBackground2());
	}

	private final ProgressBarListener barListener = this::doChange;

}
