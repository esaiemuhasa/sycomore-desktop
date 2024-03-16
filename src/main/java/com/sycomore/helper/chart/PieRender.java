package com.sycomore.helper.chart;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * @author Esaie MUHASA
 */
public class PieRender extends JComponent {
	
	/**
	 * Type de rendue graphique des tartes
	 * @author Esaie MUHASA
	 */
	public enum PieRenderType {
		DEFAULT,
		DONUT_CHART// avec un centre vide comme un beignet
	}
	
	private static final BasicStroke LINE_STROKE = new BasicStroke(1.2f);
	private static final Font RENDER_FONT = new Font("Arial", Font.PLAIN, 18);
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.####");
	private static final float PADDING = 0.10f;

	private PieModel model;
	private PieRenderType type;
	private DecimalFormat formater = DECIMAL_FORMAT;

	private double diameter;//le rayon d'un cercle
	private final List<PiePartInfo> parts = new ArrayList<>();
	
	private int hoverIndex = -1;
	private final boolean hoverSupport = true;//est-ce que ce diagramme est hovable?
	private boolean percentVisible = false;//les pourcentages sont-elles visible
	private double startAngle = 90;
	private double textSize;//la taille du texte
	private float fontSize;//taille de police préférée
	private final Point center = new Point();
	private final MouseListener ls = new MouseListener();
	
	public PieRender() {
		init();
	}

	/**
	 * Constructeur d'initialisation avec un model de donnée
	 */
	public PieRender(PieModel model) {
		this.model = model;
		init();
		model.addListener(pieModelListener);
		prepareRender();
	}

	private void init () {
		type = PieRenderType.DONUT_CHART;
		setFont(RENDER_FONT);

		addMouseListener(ls);
		addMouseMotionListener(ls);
	}
	
	public DecimalFormat getFormater() {
		return formater;
	}

	public void setFormater(DecimalFormat formater) {
		if(formater == this.formater)
			return;
		
		this.formater = formater == null ? DECIMAL_FORMAT : formater;
		
		if (isPercentVisible())
			repaint();
	}

	@Override
	public void doLayout() {
		super.doLayout();
		prepareRender();
	}

	/**
	 * @return the type
	 */
	public PieRenderType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(PieRenderType type) {
		if (this.type == type)
			return;
		
		this.type = type;
		prepareRender();
		repaint();
	}

	/**
	 * @return the percentVisible
	 */
	public boolean isPercentVisible() {
		return percentVisible;
	}

	/**
	 * @param percentVisible the percentVisible to set
	 */
	public void setPercentVisible(boolean percentVisible) {
		if (percentVisible == this.percentVisible)
			return;
					
		this.percentVisible = percentVisible;
		repaint();
	}

	/**
	 * @return the startAngle
	 */
	public double getStartAngle() {
		return startAngle;
	}

	/**
	 * @param startAngle the startAngle to set
	 */
	public synchronized void setStartAngle(double startAngle) {
		if (this.startAngle == startAngle)
			return;
		
		this.startAngle = startAngle;
		prepareRender();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		doPaint(g2);
		
		if (model == null)
			return;
		
		if (model.getSelectedIndex() != -1) {
			PiePartInfo info = parts.get(model.getSelectedIndex());
			g2.setColor(info.getPart().getBackgroundColor());
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g2.fill(createArc(info, 0.01f));
		}
		
		if (hoverIndex != -1) {
			PiePartInfo info = parts.get(hoverIndex);
			g2.setColor(info.getPart().getBackgroundColor());
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			g2.fill(createArc(info, 0.02f));
			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g2.setFont(getFont().deriveFont(fontSize));
			drawPopupLabel(info, g2);
			
		}
		
	}
	
	/**
	 * Dessine les figures élémentaires du graphique
	 */
	private synchronized void doPaint (Graphics2D g2) {
		
		if (model == null)
			return;
		
		for (PiePartInfo info : parts) {
			if (info.getArea() == null)
				continue;
			
			PiePart part  = info.getPart();
			g2.setColor(part.getBackgroundColor());
			g2.fill(info.getArea());				
			
			g2.setColor(getBackground());
			g2.setStroke(LINE_STROKE);
			g2.draw(info.getArea());
			
			if (percentVisible) {
				String percentTxt = DECIMAL_FORMAT.format(model.getPercentOf(part)) + "%";
				g2.setFont(getFont().deriveFont(fontSize));
				
	            FontMetrics fm = g2.getFontMetrics();
	            Rectangle2D rec = fm.getStringBounds(percentTxt, g2);
	            float textX = (float) (center.x + (info.getCosTextAngle() * textSize) - rec.getWidth() / 2f);
	            float textY = (float) (center.y + (info.getSinTextAngle() * textSize) + fm.getAscent() / 2f);
	            
	            g2.drawString(percentTxt, textX, textY);
			}
		}

	}
	
	/**
	 * Exportation du graphique dans un canvas externe
	 */
	public synchronized void paint (Graphics2D g, double width, double height) {
		
		setVisible(false);
		prepareRender(width, height);
		doPaint(g);
		
		prepareRender();
		setVisible(true);
	}
	
	/**
	 * utilitaire de ceration d'un arc de cercle
	 */
    private Shape createArc (PiePartInfo info, float variation) {
    	final double width = getWidth();
    	final double height = getHeight();
    	
    	final double size = diameter;
    	final double size1 = size + (size * variation) + (PADDING * (size/2));
    	final double size2 = size - (size * variation * 2) + (PADDING * (size/2));
    	
    	double x = (width - size1) / 2;
    	double y = (height - size1) / 2;

        Area area = new Area(new Arc2D.Double(x, y, size1, size1, info.getDrawAngle(), -info.getAngle(), Arc2D.PIE));
        x = (width - size2) / 2;
        y = (height - size2) / 2;
        area.subtract(new Area(new Arc2D.Double(x, y, size2, size2, info.getDrawAngle(), -info.getAngle(), Arc2D.PIE)));
        
        return area;
    }
	
	/**
	 * Rendue du label lors du survol sur une tarte
	 */
    private void drawPopupLabel(PiePartInfo info, Graphics2D g2) {
    	
    	final double size = diameter / 2f;//taille préférée
		float x = (float) (center.x + (info.getCosTextAngle() * size));//x par défaut
		float y = (float) (center.y + (info.getSinTextAngle() * size));//y par défaut
		final int maxW = getWidth(), maxH = getHeight();//surface de du composant entier
		final double pd = Math.min(maxW, maxH) - size; //le padding en px
		
    	final float fontSize = (float) (getFont().getSize() * size * 0.0035f);
        boolean up = !(info.getTextAngle() > 0 && info.getTextAngle() < 180);
        double space = size * 0.03f;
        double spaceV = size * 0.01f;
        double paceH = size * 0.02f;
        
        //h1: titre de niveau 1
        //h2: titre de niveau 2
        String percentTxt = DECIMAL_FORMAT.format(model.getPercentOf(info.getPart())) + "%";
        String h2 = DECIMAL_FORMAT.format(info.getPart().getValue())+ model.getSuffix() + " (" + percentTxt + ")";
        String h1 = info.getPart().getLabel();
       
        FontMetrics fmH1 = g2.getFontMetrics(getFont().deriveFont(Font.PLAIN, fontSize));
        FontMetrics fmH2 = g2.getFontMetrics(getFont().deriveFont(Font.BOLD, fontSize));
        Rectangle2D r1 = fmH1.getStringBounds(h1, g2);
        Rectangle2D r2 = fmH1.getStringBounds(h2, g2);
        
        double widthH2 = r2.getWidth() + (paceH * 2), widthH1 = r1.getWidth() + (paceH * 2);
        double width = Math.max(widthH1, widthH2);
        
        //correction debordement de texte sur l'axe de X
        do {
        	if ((width + x) > maxW && widthH2 < width)
        		h1 = h1.substring(0, h1.length()-1);
			r1 = fmH1.getStringBounds(h1, g2);
			width = Math.max(r1.getWidth() + (paceH * 2), r2.getWidth() + (paceH * 2));
		} while ((width + x) > maxW && h1.length() > 1 && widthH2 < width);
        
        if (h1.length() != info.getPart().getLabel().length()) { 
        	if (h1.length() > 3) {        		
        		h1 = h1.substring(0, h1.length()-2);
        	} else {
        		String [] subs = info.getPart().getLabel().split(" ");
        		h1 = subs[0];
        		if (h1.length() <= 3 )
        			h1 += " "+subs[2];
        	}
        	h1 += "...";
        	r1 = fmH1.getStringBounds(h1, g2);
        }
        //==
        
        double height = r1.getHeight() + r2.getHeight() + spaceV * 2;
        double recY = up ? y - height - space : y + space;
        double recX = x - width / 2;
        
        recX += paceH;
        
        if (recX + width > maxW)
        	recX -= pd;
        
        if (recX <= 0)
        	recX = 10;
        
        if (recY <= 0) {//
        	recY = pd / 4;
        } else {//debordement en hauteur
        	double yText = recY + height;
        	if (yText > maxH) {
        		recY -= height;
        	}        	
        }
        
        Color c = new Color(0x99000000 & getBackground().getRGB(), true);
        g2.setColor(c);
        RoundRectangle2D rec = new RoundRectangle2D.Double(recX, recY, width, height, 5, 5);
        
        g2.fill(rec);
        g2.setColor(Color.WHITE);
        g2.draw(rec);
        g2.setFont(getFont().deriveFont(Font.PLAIN, fontSize));
        g2.drawString(h1, (float) recX + 3, (float) (recY + fmH1.getAscent() + spaceV));
        g2.setFont(getFont().deriveFont(Font.BOLD, fontSize));
        g2.drawString(h2, (float) recX + 3, (float) (recY + height - r2.getHeight() + fmH2.getAscent() - spaceV));

    }

	/**
	 * @return the model
	 */
	public PieModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(PieModel model) {
		if(this.model != model) {
			if(this.model != null )
				this.model.removeListener(pieModelListener);
			this.model = model;
			
			if(model != null)
				this.model.addListener(pieModelListener);
			
			prepareRender();
			repaint();
		}
	}
	
	
	/**
	 * Utilitaire de preparation du rendu du graphique
	 */
	private void prepareRender() {		
		final double width = getWidth();
		final double height = getHeight();
		prepareRender(width, height);
	}
	
	/**
	 * Utilitaire de preparation du rendu graphique
	 */
	private synchronized void prepareRender (double width, double height) {
        for (PiePartInfo piePartInfo : parts)
			piePartInfo.dispose();
		
		parts.clear();
		
		//on prend la plus petite valeur entre la hauteur et la largeur de la vue
		diameter = Math.min(width, height);
		float borderHover = 0.05f;
		diameter -= (diameter * borderHover) + (PADDING * diameter);
		textSize = diameter / 2f * 0.75f;
		fontSize = (float) (getFont().getSize() * diameter * 0.0030f);
		
		center.setLocation(width / 2f, height / 2f);
		double translateX = (width - diameter) / 2f;
		double translateY = (height - diameter) / 2f;
		
		double minRadius = diameter * 0.35f;
		double minMinRadius = diameter * 0.01f;
		
		double translateXx = (width - minRadius) / 2f;
		double translateYy = (height - minRadius) / 2f;
		
		double translateXx_x = (width - minMinRadius) / 2f;
		double translateYy_y = (height - minMinRadius) / 2f;
		
		if (model == null)
			return;
		
		double max = model.isRealMaxPriority()? model.getRealMax() : model.getMax();
		
		if (max == 0)
			return;
		
		double drawAngle = startAngle;
		for (PiePart part : model.getParts()) {
			PiePartInfo info;
			final double angle = (part.getValue() * 360f) / max;
			if (part.getValue() != 0f) {				
				final double textAngle = - (drawAngle - angle / 2);
				
				Area area = new Area(new Arc2D.Double(translateX, translateY, diameter, diameter, drawAngle, -angle, Arc2D.PIE));
				if (type == PieRenderType.DONUT_CHART)
					area.subtract(new Area(new Ellipse2D.Double(translateXx, translateYy, minRadius, minRadius)));
				else
					area.subtract(new Area(new Ellipse2D.Double(translateXx_x, translateYy_y, minMinRadius, minMinRadius)));
				info =new PiePartInfo(part, area, angle, textAngle, drawAngle);
			} else
				info = new PiePartInfo(part, null, angle, 0, drawAngle);
			
			parts.add(info);
			drawAngle -= angle;
		}
	}

	private final PieModelListener pieModelListener = new PieModelAdapter() {
		@Override
		public void refresh(PieModel model) {
			prepareRender();
			repaint();
		}

		@Override
		public void repaintPart(PieModel model, int partIndex) {
			prepareRender();
			repaint();
		}

		@Override
		public void onSelectedIndex(PieModel model, int oldIndex, int newIndex) {
			repaint();
		}
	};
	
	/**
	 * @author Esaie MUHASA
	 */
	private class MouseListener extends MouseAdapter {

		@Override
		public void mouseExited(MouseEvent e) {
			if(!hoverSupport)
				return;
			hoverIndex = -1;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(!hoverSupport)
				return;
			
			Point point = e.getPoint();
			int index = -1;
			for (PiePartInfo info : parts) {
				if(info.match(point)) {
					index = model.indexOf(info.getPart());
					break;
				}
			}
			hoverIndex = index;
			repaint();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!model.isSelectablePart())
				return;

			Point point = e.getPoint();
			int index = -1;
			for (PiePartInfo info : parts) {
				if(info.match(point)) {
					index = model.indexOf(info.getPart());
					break;
				}
			}

			if (index != -1 && index != model.getSelectedIndex())
				model.setSelectedIndex(index);
		}
	}
	
	/**
	 * Enveloppe des calculs/metadonnee du rendu d'un part
	 * @author Esaie MUHASA
	 */
	protected static final class PiePartInfo {

		private PiePart part;
		private Shape area;
		private final double angle;
		private final double textAngle;
		private final double drawAngle;
		
		private final double sinTextAngle;
		private final double cosTextAngle;
		
		/**
		 */
		public PiePartInfo(PiePart part, Shape area, double angle, double textAngle, double drawAngle) {
			super();
			this.part = part;
			this.area = area;
			this.angle = angle;
			this.textAngle = textAngle;
			this.drawAngle = drawAngle;
			
			cosTextAngle = Math.cos(Math.toRadians(textAngle));
			sinTextAngle = Math.sin(Math.toRadians(textAngle));
		}
		
		/**
		 * Vérifie si le point appartient à la forme
		 */
		public boolean match (Point M) {			
			if(area == null || M == null)
				return false;
			return area.contains(M);
		}
		
		/**
		 * Liberation des resources
		 */
		public void dispose () {
			part = null;
			area = null;
		}

		/**
		 * @return the part
		 */
		public PiePart getPart() {
			return part;
		}

		/**
		 * @return the angle
		 */
		public double getAngle() {
			return angle;
		}

		/**
		 * @return the area
		 */
		public Shape getArea() {
			return area;
		}

		/**
		 * @return the textAngle
		 */
		public double getTextAngle() {
			return textAngle;
		}

		/**
		 * @return the sinTextAngle
		 */
		public double getSinTextAngle() {
			return sinTextAngle;
		}

		/**
		 * @return the cosTextAngle
		 */
		public double getCosTextAngle() {
			return cosTextAngle;
		}

		/**
		 * @return the drawAngle
		 */
		public double getDrawAngle() {
			return drawAngle;
		}
		
	}

}
