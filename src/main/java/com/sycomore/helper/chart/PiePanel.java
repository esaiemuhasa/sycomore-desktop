package com.sycomore.helper.chart;

import com.sycomore.helper.Config;
import com.sycomore.view.components.swing.Card;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author Esaie MUHASA
 */
public class PiePanel extends JPanel implements Printable{

	public enum Alignment {
		VERTICAL,
		HORIZONTAL
	}
	
	private Frame owner;
	
	private PieRender render;
	private PieCaptionRender caption;
	private final JLabel title = new JLabel("");
	protected JScrollPane scroll;
	
	private final GridLayout layout = new GridLayout(1, 2);
	private Alignment alignment = Alignment.HORIZONTAL;
    private final JPanel center = new JPanel(layout);
	private PieModel model;
	
	private final PieModelListener modelListener = new PieModelListener() {
		@Override
		public void repaintPart(PieModel model, int partIndex) {}
		@Override
		public void refresh(PieModel model) {
			if(model.getTitle() != null)
				title.setText(model.getTitle());
			else
				title.setText("");
		}
		
		@Override
		public void onSelectedIndex(PieModel model, int oldIndex, int newIndex) {}
		
		@Override
		public void onTitleChange(PieModel  model, String text) {
			title.setText(text);
		}
	};
	
	private final JMenuItem itemPrint = new JMenuItem("Imprimer le graphique", new ImageIcon(Config.getIcon("print")));
	private final JMenuItem itemExport = new JMenuItem("Exporter la photo", new ImageIcon(Config.getIcon("saveimg")));
	private final JPopupMenu popupMenu = new JPopupMenu();
	
	private final ActionListener btnPrintListener = event -> {
		PrinterJob job = PrinterJob.getPrinterJob();
		
		if(job.printDialog() && job.pageDialog(job.defaultPage()) != null) {

			job.setPrintable(this);
			
			try {
				job.print();
			} catch (PrinterException e) {
				e.printStackTrace(System.err);
				JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur d'impression", JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	
	private final ActionListener btnImgListener = event -> {
		
		boolean status = ChartPanel.FILE_CHOOSER.showSaveDialog(getOwner());
		if(status) {
			String fileName = ChartPanel.FILE_CHOOSER.getSelectedFile().getAbsolutePath();
			if(!fileName.matches("^(.+)(\\.)(png|jpeg|jpg)$"))
				fileName += ".png";
			
			String type = fileName.substring(fileName.lastIndexOf(".")+1);
			
			Color old = getBackground();
			Toolkit tool = Toolkit.getDefaultToolkit();
			int width = (int)(tool.getScreenSize().getWidth() * 1.2f);
			int height = (int)(tool.getScreenSize().getHeight() * 1.2f);
			
			if (height < caption.getHeight())
				height = caption.getHeight() + 200;
			
			boolean isPng = type.equals("png");
			BufferedImage buffer = new BufferedImage(width, height, isPng? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) buffer.getGraphics();
			Rectangle2D rect = new Rectangle2D.Double(0, 0, width-2, height-2);
			
			render.setBackground(Color.WHITE);
			caption.setBackground(Color.WHITE);
			
			if(!isPng) {
				g.setColor(Color.WHITE);
				g.fill(rect);
			}
			
			render.paint(g, width/2d, height);
			caption.paint(g, width/2d, 0, width/2, height);
			
			g.setColor(Color.BLACK);
			g.drawString(title.getText(), 10f, height - 20f);
			g.draw(rect);
			
			render.setBackground(old);
			caption.setBackground(old);
			
			File file = new File(fileName);
			try {
				ImageIO.write(buffer, type, file);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur d'exportation", JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	

	public PiePanel() {
		super ();
		this.setOpaque(true);
		render = new PieRender();
		caption = new  PieCaptionRender();
		init();
	}

	public PiePanel (PieModel model) {
		super ();
		this.model = model;
		render = new PieRender(model);
		caption = new PieCaptionRender(model);
		title.setText(model.getTitle());
		init();

		model.addListener(modelListener);
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
		if(this.model != null)
			this.model.removeListener(modelListener);
		this.model = model;
		
		caption.setModel(model);
		render.setModel(model);
		
		if (model != null) {
			model.addListener(modelListener);
			title.setText(model.getTitle());
		} else 
			title.setText("");
	}

	public void setAlignment(Alignment alignment) {
		if (this.alignment == alignment)
			return;

		this.alignment = alignment;

		if (alignment == Alignment.VERTICAL) {
			layout.setRows(2);
			layout.setColumns(1);
			center.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		} else {
			layout.setRows(1);
			layout.setColumns(2);
			center.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		}
		center.revalidate();
	}

	/**
	 * @return the owner
	 */
	public Frame getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Frame owner) {
		this.owner = owner;
	}
	
	@Override
	public int print (Graphics graphics, PageFormat page, int pageIndex) throws PrinterException {
		if(pageIndex > 0)
			return NO_SUCH_PAGE;
		
		Color old = getBackground();
		render.setBackground(Color.WHITE);
		caption.setBackground(Color.WHITE);
		
		Graphics2D g2 = (Graphics2D) graphics;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.translate(page.getImageableX(), page.getImageableY());
		
		if (page.getOrientation() == PageFormat.LANDSCAPE) {
			render.paint(g2, page.getImageableWidth()/2, page.getImageableHeight());
			caption.paint(g2, page.getImageableWidth()/2, 0, (int)page.getImageableWidth()/2, (int)page.getImageableHeight());
		} else {
			render.paint(g2, page.getImageableWidth(), page.getImageableHeight()/3);
			caption.paint(g2, 5, page.getImageableHeight()/3, (int)page.getImageableWidth(), (int) (page.getImageableHeight() * (2f/3f)));
		}
		
		g2.setColor(UIManager.getColor("Component.borderColor"));
		Rectangle2D b = new Rectangle2D.Double(0, 0, page.getImageableWidth(), page.getImageableHeight());
		g2.draw(b);
		g2.setFont(title.getFont());
		g2.drawString(title.getText(), 10f, g2.getFontMetrics().getAscent());
		
		render.setBackground(old);
		caption.setBackground(old);
		
		return PAGE_EXISTS;
	}

	private void init() {
		setLayout(new BorderLayout());
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(caption, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
		scroll = Config.createVerticalScrollPane(panel);

		itemPrint.addActionListener(btnPrintListener);
		itemExport.addActionListener(btnImgListener);
		title.setFont(Card.FONT_INFO);
		
		center.add(render);
		center.add(scroll);
		center.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

		add(center, BorderLayout.CENTER);
		add(title, BorderLayout.NORTH);
		setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));

		popupMenu.add(itemPrint);
		popupMenu.add(itemExport);

		addMouseListener(mouseAdapter);
		render.addMouseListener(mouseAdapter);
		caption.addMouseListener(mouseAdapter);
	}
	
	/**
	 * change visibility of graphic render
	 * this method has importance when you need update all pie part of model.
	 */
	public void setRenderVisible (boolean visible) {
		render.setVisible(visible);
		if(caption != null)
			caption.setVisible(visible);
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if(render != null)
			render.setBackground(bg);
		
		if(caption != null)
			caption.setBackground(bg);
	}

	private final MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if (!e.isPopupTrigger())
				return;

			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	};
	
}
