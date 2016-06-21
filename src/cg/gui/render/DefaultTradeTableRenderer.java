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

	/*
	 * Constructor.
	 */
	public DefaultTradeTableRenderer(TableRowSorter<TradeTableModel> s) {
		super();
		sorter = s;
		setColorScheme();
	}
	
	/*
	 * Set the two color scheme.
	 */
	private void setColorScheme(){
		scheme.bg_Normal[0]     = new Color(235, 237, 255);
		scheme.bg_Normal[1]     = new Color(217, 251, 209);
	}

	@Override
	public int getColorIndex(JTable table, int row)
	{
	   /*
	    * Get the actual row.
	    */
		TradeTableModel model = (TradeTableModel) table.getModel();
		int actual_row = sorter.convertRowIndexToModel(row);
		
		/*
		 * Look at the trade type column to determine the color index to be used,
		 * so that buy and sell trades are colored differently.
		 */
		cg.Trade.Type tradeType = (cg.Trade.Type) model.getValueAt(actual_row, 2);

		if (tradeType == cg.Trade.Type.BUY)
			return 0;
		else
			return 1;
	}
}
