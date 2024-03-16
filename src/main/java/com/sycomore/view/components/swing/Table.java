
package com.sycomore.view.components.swing;

import com.sycomore.helper.Config;
import com.sycomore.view.components.jna.JnaFileChooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.Date;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

/**
 * @author Esaie MUHASA
 *
 */
public class Table extends JTable {
	private static final long serialVersionUID = 1086643646477646234L;
	
	public static final JnaFileChooser XLSX_FILE_CHOOSER = new JnaFileChooser();
	static {
		XLSX_FILE_CHOOSER.addFilter("Fichier Excel", "xlsx");;
		XLSX_FILE_CHOOSER.setMultiSelectionEnabled(false);
		XLSX_FILE_CHOOSER.setTitle("Exportation des données au format Excel");
		XLSX_FILE_CHOOSER.setDefaultFileName("exportation-"+ Config.DATE_TIME_FORMAT.format(new Date())+"-data.xlsx");
	}
	private final CustomTableHeader header = new CustomTableHeader();
    private final CustomTableCellRender cell = new CustomTableCellRender();

	public Table() {
		init();
	}


	public Table(TableModel model) {
		super(model);
		init();
	}

	private void init() {
		
		getTableHeader().setDefaultRenderer(header);
		
		setShowHorizontalLines(false);
		setShowVerticalLines(false);

        setRowHeight(40);
		setGridColor(UIManager.getColor("Component.borderColor"));
        getTableHeader().setReorderingAllowed(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        setDefaultRenderer(Object.class, cell);
		getTableHeader().setDefaultRenderer(header);
	}
	
	/**
	 * Filtre d'exportation des donnees au format excel
	 * @author Esaie MUHASA
	 */
	public static class FileFilterExcel extends FileFilter {

		@Override
		public boolean accept(File f) {
			if(f.isDirectory())
				return true;
			return f.getName().matches("^(.+)(\\.xlsx)$");
		}

		@Override
		public String getDescription() {
			return "Format excel 2007 ou plus";
		}
		
	}
	
    private static class CustomTableHeader extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            com.setBackground(UIManager.getColor("Component.background2").darker().darker());
            com.setForeground(Color.WHITE);
            com.setFont(com.getFont().deriveFont(Font.BOLD, 13));
            setHorizontalAlignment(JLabel.LEFT);
            return com;
        }
    }
    
    private class CustomTableCellRender extends DefaultTableCellRenderer {
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel com = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (isCellSelected(row, column)) {
				com.setBackground(UIManager.getColor("Table.selectionBackground"));
			} else {
				if (row % 2 == 0) {
					com.setBackground(UIManager.getColor("Table.row1Background"));
				} else {
					com.setBackground(UIManager.getColor("Table.row2Background"));
				}
			}

			if (table.getShowHorizontalLines() && table.getShowVerticalLines())
				com.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Table.gridColor")));
			com.setForeground(UIManager.getColor("Table.foreground"));
			setHorizontalAlignment(JLabel.LEFT);
			return com;
		}
    }

}
