package cg.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

/**
 * This class provides rendering for the cells in a table using a two color scheme. 
 */
abstract public class TwoColorTableRenderer extends JLabel implements TableCellRenderer {

//	TableRowSorter<GainTableModel> sorter;
	
	TableTwoColorScheme scheme;
	
	public TwoColorTableRenderer(/*TableRowSorter<GainTableModel> s*/) {
		super();
		init();
		scheme = new TableTwoColorScheme();
	}
	
	public TwoColorTableRenderer(TableTwoColorScheme s) {
		super();
		init();
		scheme = s;
	}

	private void init(){
		setOpaque(true);
		setFont(getFont().deriveFont(Font.PLAIN));
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	public void setColorScheme(TableTwoColorScheme s){
		scheme = s;
	}
	
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		
		setColors(table,isSelected,hasFocus,row);

		if (value == null)
			setText("");
		else
			setText(value.toString());
		return this;
	}

	/**
	 * An implementation of this method tells the renderer which color scheme
	 * indexed to use: 0 or 1.
	 */
	abstract public int getColorIndex(JTable table, int row);
	
	private void setColors(JTable table, boolean isSelected, boolean hasFocus, int row) {

		int i = getColorIndex(table,row);
		
		// cell in normal row (isSelected == false)
		if (isSelected == false) {
			setFont(getFont().deriveFont(Font.PLAIN));
			setBackground(scheme.bg_Normal[i]);
			setForeground(scheme.fg_Normal[i]);
		// cell in selected row without focus
		} else if ( isSelected == true && hasFocus == false ){
			setFont(getFont().deriveFont(Font.ITALIC));
			setBackground(scheme.bg_IsSelected[i]);
			setForeground(scheme.fg_IsSelected[i]);
		// cell in selected row with focus
		} else {
			setFont(getFont().deriveFont(Font.PLAIN));
			setBackground(scheme.bg_HasFocus[i]);
			setForeground(scheme.fg_HasFocus[i]);
		}
	}
}
