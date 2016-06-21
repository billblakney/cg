package cg.gui.render;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import cg.gui.GainColumnFinder;
import cg.gui.GainTableModel;

/**
 * This class provides rendering for the cells in the gain table.
 */
public class DefaultGainTableRenderer extends TwoColorTableRenderer {

	TableRowSorter<GainTableModel> sorter;

	public DefaultGainTableRenderer(TableRowSorter<GainTableModel> s) {
		super();
		sorter = s;
		setColorScheme();
	}
	
	private void setColorScheme(){
		scheme.bg_Normal[0]     = new Color(175, 255, 175);
		scheme.bg_Normal[1]     = new Color(255, 204, 204);
		scheme.bg_IsSelected[0] = new Color(175, 225, 175);
		scheme.bg_IsSelected[1] = new Color(225, 150, 150);
	}

	public int getColorIndex(JTable table, int row){
		GainTableModel model = (GainTableModel) table.getModel();
		GainColumnFinder gcf = (GainColumnFinder) model;
		int actual_row = sorter.convertRowIndexToModel(row);
		Integer gain = (Integer) (model.getValueAt(actual_row, gcf.getGainColumn()/*2*/));
		if (gain.intValue() >= 0 )
			return 0; //gain
		else
			return 1; // loss
	}
}
