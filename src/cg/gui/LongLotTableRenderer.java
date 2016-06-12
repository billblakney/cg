package cg.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

/**
 * This class provides rendering for the cells in the trade table.
 */
public class LongLotTableRenderer extends DefaultLotTableRenderer {

	private DecimalFormat numSharesFormat = new DecimalFormat(
	"###,###,###,##0");
	
	
	public LongLotTableRenderer(TableRowSorter<LotGainTableModel> s) {
		super(s);
	}

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		
		super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);

/*		if (value == null)
			setText("");
		else
			setText(value.toString());*/
		setText(numSharesFormat.format(value));

		return this;
	}
}
