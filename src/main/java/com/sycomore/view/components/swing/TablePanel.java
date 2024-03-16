package com.sycomore.view.components.swing;

import com.sycomore.helper.Config;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.table.JTableHeader;


/**
 * @author Esaie MUHASA
 */
public class TablePanel extends JPanel {
	private final JTable table;
	private final JLabel title;
	private final JPanel header = new JPanel(new BorderLayout());
	private JScrollPane scroll;
	
	public TablePanel(JTable table, String title, boolean scrollable) {
		super(new BorderLayout());
		this.table = table;
		this.title = Config.createSubTitle(title);
		this.init(scrollable);
	}

	public TablePanel(Table table, String title) {
		super(new BorderLayout());
		this.table = table;
		this.title = Config.createSubTitle(title);
		this.init(true);
	}
	
	/**
	 * Modification du titre du panel
	 */
	public void setTitle (String title) {
		this.title.setText(title);
		this.title.setVisible(title != null && !title.trim().isEmpty());
	}
	
	/**
	 * @return the header
	 */
	public JPanel getHeader() {
		return header;
	}

	/**
	 * @return the table
	 */
	public JTable getTable() {
		return table;
	}
	
//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
//
//        g2.setColor(FormUtil.BORDER_COLOR);
//        g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
//	}

	private void init (boolean scrollable) {
		JPanel body = new JPanel(new BorderLayout());
		header.setOpaque(true);
		header.setBackground(UIManager.getColor("Component.background2"));
		header.add(title, BorderLayout.CENTER);
		
		if (scrollable) {			
			scroll = Config.createVerticalScrollPane(table);
			body.add(scroll, BorderLayout.CENTER);
		} else {
			JTableHeader tHeader = table.getTableHeader();
			tHeader.setBackground(UIManager.getColor("Component.background1"));
			body.add(tHeader, BorderLayout.NORTH);
			body.add(table, BorderLayout.CENTER);
		}
		
		add(header, BorderLayout.NORTH);
		add(body, BorderLayout.CENTER);
		setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
		
		if (title.getText() == null || title.getText().trim().isEmpty())
			title.setVisible(false);
	}

	/**
	 * @return the scroll
	 */
	public JScrollPane getScroll() {
		return scroll;
	}

}
