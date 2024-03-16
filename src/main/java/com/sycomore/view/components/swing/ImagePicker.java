package com.sycomore.view.components.swing;

import com.sycomore.helper.Config;
import com.sycomore.view.components.jna.JnaFileChooser;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


/**
 * @author Esaie MUHASA
 */
public class ImagePicker extends JPanel {
	private static final long serialVersionUID = -8188839974929150048L;
	private static final Dimension PREFERRED_SIZE = new Dimension(260, 300);
	private static final String [] EXT = {"png", "jpg", "jpeg"};
	
	private final JLabel title = new JLabel("", JLabel.CENTER);
	
	private final JSlider slider = new JSlider(JSlider.VERTICAL);
	private final ImagePickerRender render = new ImagePickerRender();
	private final JButton btnChoose = new JButton("Choisir", new ImageIcon(Config.getIcon("edit")));
	
	private final static JnaFileChooser FILE_CHOOSER = new JnaFileChooser();
	static {
		FILE_CHOOSER.setMultiSelectionEnabled(false);
		FILE_CHOOSER.addFilter("Image", "png", "jpg", "jpeg");
		FILE_CHOOSER.setTitle("Sélectionné la photo de passeport");
	}
	private Frame mainFrame;

    private final MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				btnChoose.doClick();
			}
		}
	};

	public ImagePicker() {
		super(new BorderLayout(5, 5));
		init();
	}
	
	public ImagePicker(String label) {
		this();
		this.title.setText(label);
	}
	
	public void setMaintFrame (Frame frame) {
		this.mainFrame = frame;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btnChoose.setEnabled(enabled);
		render.setEnabled(enabled);
		
		if(render.fileName != null)
			slider.setEnabled(enabled);
		else {
			slider.setEnabled(false);
		}
	}
	
	/**
	 * Demande d'affichage d'une image
	 */
	public void show (String imageFileName) {
        //pour afficher une image x dans l'image picker
        this.render.setFileName(imageFileName, slider.getValue());
	}
	
	private void init() {
		final JPanel center = new JPanel(new BorderLayout());
		final Box box = Box.createVerticalBox();
		setPreferredSize(PREFERRED_SIZE);
		setMaximumSize(PREFERRED_SIZE);
		setMinimumSize(PREFERRED_SIZE);
		setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
		
		slider.setMaximum(200);
		slider.setMinimum(1);
		
		box.add(slider);
		box.add(Box.createVerticalStrut(20));
		
		this.add(box, BorderLayout.EAST);
		center.add(render, BorderLayout.CENTER);
		center.setBorder(new EmptyBorder(0, 5, 0, 0));

		final JPanel bottom = new JPanel(new BorderLayout());

		bottom.add(btnChoose, BorderLayout.CENTER);
		bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		title.setForeground(Color.LIGHT_GRAY);
		add(title, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		
		slider.setEnabled(false);

		btnChoose.addActionListener(event -> {
			boolean result = FILE_CHOOSER.showOpenDialog(mainFrame);
			
			if(result) {
				File file = FILE_CHOOSER.getSelectedFile();
				render.setFileName(file.getAbsolutePath(), slider.getValue());
				slider.setEnabled(true);
			} else {
				render.setFileName(null, slider.getValue());
				slider.setEnabled(false);
			}
		});
		
		render.addMouseListener(mouseListener);
		slider.addChangeListener(event -> {
			if(!render.setRation(slider.getValue())) {
				slider.setEnabled(false);
				slider.setValue(render.getCurrentRation());
				slider.setEnabled(true);
			}
		});
	}
	
	/**
	 * Renvoie le chemin absolut vers l'image sur le HDD
	 */
	public String getSelectedFileName () {
		return render.getFileName();
	}
	
	/**
	 * Renvoie le type de l'image.
	 * Typiquement une petite chaine de caractère comme png, jpg ou jpeg.
	 */
	public String getImageType () {
		if(getSelectedFileName() == null)
			return null;
		for (String e : EXT)
			if(getSelectedFileName().toLowerCase().matches(".+\\."+e))
				return e;
		
		return null;
	}
	
	/**
	 * Renvoie l'image deja redimensionner
	 */
	public BufferedImage getImage () {
		return render.cropImage();
	}
	
	/**
	 * Est-ce que l'image afficher dans le rendue est redimensionnable??
	 */
	public boolean isReadyToCrop() {
		return render.isReadyToCrop();
	}
	
	/**
	 * 
	 * @author Esaie MUHASA
	 * Rendu de l'image choisie
	 */
	private static class ImagePickerRender extends JComponent {
		private static final long serialVersionUID = 7268721008490974405L;
		private static final BasicStroke BORDER_STROKE = new BasicStroke(2f);
		private static final Color BK_COLOR = new Color(0x88000000, true);
		private static final int IMAGE_RECT_CROP_SIZE = 150;
		private static final String DEFAULT_FILE_NAME = Config.getIcon("personne");
		private static BufferedImage defaultImage;
		
		private String fileName;
		private BufferedImage image;
		
		//coordonnee de l'outil pour crop l'image
		private int xRect = 0;
		private int yRect = 0;
		
		//dimension de l'image
		private int xImg = 0;
		private int yImg = 0;
		private int wImg;//largeur de l'image (apres alcul du ration)
		private int hImg;//hauteur de l'image (apres calcul du ration)
		
		private int currentRation = 100;//la ration actuelement prise en compte
		private static final int MIN_RATION = 1;
		private static final int MAX_RATION = 200;
		
		protected final MouseAdapter listener = new MouseAdapter() {
			
			private Point start;
			
			public void mouseDragged(MouseEvent e) {
				if(!imageReady())
					return;
				
				Point mouse = e.getPoint();
				
				if(start == null) {
					start = mouse;
					return;
				}
				
				int distX = (int) (mouse.getX() - start.getX()), distY = (int) (mouse.getY() - start.getY());
				move (xImg+distX, yImg+distY);
				
				start = mouse;
			};
			
			public void mousePressed(MouseEvent e) {	
				if(!imageReady())
					return;
				
				start = e.getPoint();
				ImagePickerRender.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			};
			
			public void mouseReleased(MouseEvent e) {
				if(!imageReady())
					return;
				
				ImagePickerRender.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				start = null;
			};
			
			/**
			 * Déplacement du cadre de crop
			 */
			private void move (int x, int y) {
				int xP = x + wImg, yP = y + hImg;//position attendue du coin doit de l'image
				xImg = x >= xRect ? (xRect) : (xP <= (xRect + IMAGE_RECT_CROP_SIZE)? xImg : x);
				yImg = y >= yRect ? (yRect) : (yP <= (yRect + IMAGE_RECT_CROP_SIZE)? yImg : y) ;
				ImagePickerRender.this.repaint();
			}
			
			/**
			 * Verification si l'image est déjà sélectionné
			 */
			private boolean imageReady () {
				return fileName != null && image != null;
			}
			
		};
		
		public ImagePickerRender() {
			super();
			addMouseListener(listener);
			addMouseMotionListener(listener);
		}
		
		@Override
		public void doLayout() {
			super.doLayout();
			if(image == null) {
				initCropLook();
			}
			
			if(defaultImage == null) {
				try {
					 defaultImage = ImageIO.read(new File(DEFAULT_FILE_NAME));
				} catch (IOException ignored) {}
			}
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			
			
			if (fileName != null) {				
				g2.drawImage(image, xImg, yImg, wImg, hImg, null);
			} else {
				initCropLook();
				g2.drawImage(defaultImage, xRect, yRect, IMAGE_RECT_CROP_SIZE, IMAGE_RECT_CROP_SIZE, null);
			}
			
			g2.setColor(BK_COLOR);
			
			g2.fillRect(0, 0, getWidth(), yRect);
			g2.fillRect(0, getHeight()-yRect, getWidth(), yRect);
			
			g2.fillRect(0, yRect, xRect, getHeight()-yRect*2);
			g2.fillRect((xRect+ IMAGE_RECT_CROP_SIZE), yRect, xRect, getHeight() - yRect*2);

			g2.setColor(UIManager.getColor("Component.borderColor"));
			g2.setStroke(BORDER_STROKE);
			g2.drawRect(1, 1, getWidth()-2, getHeight()-2);

			g2.setColor(Color.ORANGE);
			g2.drawRect(xRect, yRect, IMAGE_RECT_CROP_SIZE, IMAGE_RECT_CROP_SIZE);
		}

		/**
		 * @return the fileName
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * @param fileName the fileName to set
		 */
		public void setFileName (String fileName, int ration) {
			this.fileName = fileName;
			if(fileName != null) {
				try {
					image = ImageIO.read(new File(fileName));
					if(image.getWidth() <= IMAGE_RECT_CROP_SIZE || image.getHeight() <= IMAGE_RECT_CROP_SIZE) {
						xImg = xRect;
						yImg = yRect;
						setRation(100);
					} else {
						int w = (int)(image.getWidth() * (ration/100.0)),
							h = (int)(image.getHeight() * (ration/100.0));
						xImg = (w - getWidth()) / -2;
						yImg = (h - getHeight()) / -2;
						setRation(ration);
					}
				} catch (IOException ignored) {}
			} else {
				image = null;
			}
			repaint();
		}
		
		private void initCropLook () {
			xRect = this.getWidth()/2 - IMAGE_RECT_CROP_SIZE /2;
			yRect = this.getHeight()/2 - IMAGE_RECT_CROP_SIZE /2;
		}
		
		/**
		 * Pour manipuler la ration d'affichage de l'image
		 * @param ration une valeur entre 1 et 100
		 * @return {@link Boolean} true if ration successfully applied, other ways false
		 */
		public boolean setRation (int ration) {
			boolean accept = ration >= MIN_RATION && ration <= MAX_RATION;
			if (accept)	{				
				BigDecimal bigW = BigDecimal.valueOf(image.getWidth() * (ration / 100.0)).setScale(0, RoundingMode.HALF_UP),
						bigH = BigDecimal.valueOf(image.getHeight() * (ration / 100.0)).setScale(0, RoundingMode.HALF_UP);
				
				int wProposed = bigW.intValue(), hProposed = bigH.intValue();
				if (wProposed >= IMAGE_RECT_CROP_SIZE && hProposed >= IMAGE_RECT_CROP_SIZE) {
					
					int xP = wProposed + xImg, yP = hProposed + yImg;
					
					xImg = xP < (xRect + IMAGE_RECT_CROP_SIZE)? xRect : xImg;
					yImg = yP < (yRect + IMAGE_RECT_CROP_SIZE)? yRect : yImg;
					
					wImg = wProposed;
					hImg = hProposed;
					
					repaint();
					currentRation = ration;
				} else 
					accept = false;
			}		
			
			return accept;
		}
		
		/**
		 * @return the currentRation
		 */
		public int getCurrentRation() {
			return currentRation;
		}
		
		/**
		 * y a-t-il moyen de crooper l'image??
		 */
		public boolean isReadyToCrop() {
            return image != null && (image.getWidth() >= IMAGE_RECT_CROP_SIZE && image.getHeight() >= IMAGE_RECT_CROP_SIZE);
        }

		/**
		 * Renvoie l'image cropper
		 */
		public BufferedImage cropImage () {
			
			if(!isReadyToCrop())
				return image;
			
			double diffX = Math.abs(xImg - xRect),
					diffY = Math.abs(yImg - yRect);
			
			Image resize = image.getScaledInstance(wImg, hImg, Image.SCALE_DEFAULT);
			BufferedImage buffer = new BufferedImage(wImg, hImg, BufferedImage.TYPE_INT_BGR);
			buffer.createGraphics().drawImage(resize, 0, 0, null);
			
			BigDecimal bigX = new BigDecimal(diffX).setScale(0, RoundingMode.HALF_UP),
					bigY = new BigDecimal(diffY).setScale(0, RoundingMode.HALF_UP);
			
			int x = bigX.intValue(), y = bigY.intValue(), size = IMAGE_RECT_CROP_SIZE;
            return buffer.getSubimage(x, y, size, size);
		}
	}

}
