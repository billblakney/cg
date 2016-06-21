package cg.gui.render;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import cg.Trade;
import cg.gui.TradeTableModel;

/**
 * This class provides rendering for the cells in the trade table.
 */
public class DefaultTradeTableRenderer extends TwoColorTableRenderer {

	TableRowSorter<TradeTableModel> sorter;

	public DefaultTradeTableRenderer(TableRowSorter<TradeTableModel> s) {
		super();
		sorter = s;
		setColorScheme();
	}
	
	private void setColorScheme(){
		scheme.bg_Normal[0]     = new Color(235, 237, 255);
		scheme.bg_Normal[1]     = new Color(217, 251, 209);
	}

	public int getColorIndex(JTable table, int row){
		TradeTableModel model = (TradeTableModel) table.getModel();
		int actual_row = sorter.convertRowIndexToModel(row);
		cg.Trade.Type tradeType = (cg.Trade.Type) model.getValueAt(actual_row, 2);
		if (tradeType == cg.Trade.Type.BUY)
			return 0;
		else
			return 1;
	}
}
