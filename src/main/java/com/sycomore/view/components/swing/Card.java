package com.sycomore.view.components.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * @author Esaie MUHASA
 *
 */
public class Card extends JComponent implements CardModelListener {
	private static final long serialVersionUID = 2035862107414961620L;
	
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("# ##0.##");
	
	private static final Dimension 
			DEFAULT_SIZE = new Dimension(300, 110),
			MIN_SIZE = new Dimension(150, 100),
			MAX_SIZE = new Dimension(1000, 120);
	
	public static final Font 
			FONT_TITLE = new Font("Arial", Font.PLAIN, 14),
			FONT_VALUE = new Font("Arial", Font.BOLD, 24),
			FONT_INFO = new Font("Arial", Font.BOLD, 11);
	
	private CardModel<?> model;
	private Image icon;

	public Card () {//default constructor
		super();
		model = new DefaultCardModel<>();
		model.addListener(this);
		init();
	}
	
	/**
	 * Constructor d'initialisation du model du card
	 */
	public Card (CardModel<?> model) {
		super();
		this.model = model;
		model.addListener(this);
		if (model.getIcon() != null) {			
			try {
				icon = ImageIO.read(new File(model.getIcon()));
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
		init();
	}
	
	/**
	 * @return the model
	 */
	public CardModel<?> getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(CardModel<?> model) {
		if (this.model == model)
			return;
		
		if(this.model != null)
			this.model.removeListener(this);
		
		this.model = model;
		
		if (model != null) {			
			model.addListener(this);
			try {
				icon = ImageIO.read(new File(model.getIcon()));
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
		repaint();
	}

	/**
	 * Initialisation des dimensions du card
	 */
	private void init() {
		setPreferredSize(DEFAULT_SIZE);
		setMinimumSize(MIN_SIZE);
		setMaximumSize(MAX_SIZE);
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		model.setBackgroundColor(bg);
	}
	
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		model.setForegroundColor(fg);
	}
	
	@Override
	public void onValueChange(CardModel<?> model, Object oldValue) {
		repaint();
	}
	
	@Override
	public void onChange(CardModel<?> model) {
		repaint();
	}

	@Override
	public void onTitleChange(CardModel<?> model, int index, String oldTitle) {
		repaint();
	}

	@Override
	public void onColorChange(CardModel<?> model, int index, Color oldColor) {
		repaint();
	}

	@Override
	public void onIconChange(CardModel<?> model, String oldIcon) {
		try {
			icon = ImageIO.read(new File(model.getIcon()));
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int width = this.getWidth(), height = this.getHeight();
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		g2.setColor(model.getBackgroundColor());
		g2.fillRect(0, 0, width, height);
		
		//icon
		if (icon != null)
			g2.drawImage(icon, 10, 10, 65, 65, null);
		//==icon
		g2.setColor(model.getBackgroundColor());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		g2.fillRect(0, 0, width, height);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
		FontMetrics metricsInfo = g2.getFontMetrics(FONT_INFO);
		FontMetrics  metricsValue = g2.getFontMetrics(FONT_VALUE);
		FontMetrics metricsTitle = g2.getFontMetrics(FONT_TITLE);
		
		String value = model.getValue()+model.getSuffix();
		if(model.getValue() instanceof Double)
			value = DECIMAL_FORMAT.format(model.getValue())+" "+model.getSuffix();
		
		int hI =  height-(metricsInfo.getHeight()+10);
        int hV = (height- (metricsInfo.getHeight()+10)) - metricsValue.getHeight() - 5, xV = width - metricsValue.stringWidth(value) - 5;
		int hT = hV + metricsValue.getHeight() - 10, xT = width - metricsTitle.stringWidth(model.getTitle()) - 5;
		
		Color darker = model.getBackgroundColor().darker();
		g2.setColor(darker);
		g2.fillRect(0, hI, width, 50);
		
		g2.setColor(model.getForegroundColor());
		
		//value 
		g2.setFont(FONT_VALUE);
		g2.drawString(value, xV, hV);
		//==value
		
		//title
		g2.setFont(FONT_TITLE);
		g2.drawString(model.getTitle(), xT, hT);
		//==title
		
		g2.setFont(FONT_INFO);
		g2.drawString(model.getInfo(), 8, hI+16);
	}

}
