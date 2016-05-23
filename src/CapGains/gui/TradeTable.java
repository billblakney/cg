package CapGains.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

public class TradeTable extends JTable {

	
	/**
	 * The model for this table.
	 */
	private TradeTableModel model;
	
	/**
	 * The sorter for this table.
	 */
	private TableRowSorter<TradeTableModel> sorter = null;

	private String filterYear;

	final private String ANY_TICKER = ".*";
	private String tickerRegEx = ANY_TICKER;
	
	private boolean openPositionsOnly;

	RowFilter<AbstractTableModel, Integer> rowFilter;

	SharesHeld sharesHeld;
	
	TradeTable(){
		model = new TradeTableModel();
		setModel(model);
		
		sorter = new TableRowSorter<TradeTableModel>(model);
		setRowSorter(sorter);
		
		sharesHeld = new SharesHeld();

		DefaultTradeTableRenderer r = new DefaultTradeTableRenderer(sorter);
		LongTradeTableRenderer lr = new LongTradeTableRenderer(sorter);
		setDefaultRenderer(Object.class, r);
		setDefaultRenderer(Float.class, r);
		setDefaultRenderer(Integer.class, r);
		setDefaultRenderer(Long.class, lr);
		setDefaultRenderer(CapGains.Trade.Type.class,r);
		setDefaultRenderer(CapGains.Trade.SpecialInstruction.class,r);
	}

	public void setRows(Vector trades) {

		model.setData(trades);
		updateSharesHeld();
	}
	
	void filterShowOpenPositions(boolean show){
		openPositionsOnly = show;
		setRowFilter();
	}

	public void filterOnTicker(String ticker) {
		if (null == ticker)
			tickerRegEx = ANY_TICKER;
		else
			tickerRegEx = new String(ticker);
		setRowFilter();
	}

	public void filterOnYear(String year) {
		if (null == year)
			filterYear = null;
		else
			filterYear = new String(year);
		setRowFilter();
	}

	private void setRowFilter() {
		List<RowFilter<AbstractTableModel, Integer>> filters = new ArrayList<RowFilter<AbstractTableModel, Integer>>(
				3);
		// ticker filter
		RowFilter<AbstractTableModel, Integer> tf = RowFilter.regexFilter(
				tickerRegEx, TradeTableModel.COL_TICKER);
		filters.add(tf);

		// year filter
		filters.add(new CGDateFilter(filterYear,TradeTableModel.COL_DATE));

		//open positions filter
		filters.add(new TradeIsOpenPositionFilter(openPositionsOnly));
		sorter.setRowFilter(null);

		
		rowFilter = RowFilter.andFilter(filters);
		sorter.setRowFilter(rowFilter);
		model.fireTableDataChanged();

		updateSharesHeld();
	}

	private void updateSharesHeld() {
		Long shares = model.getSharesHeld(rowFilter);
		sharesHeld.setValue(shares);
	}

	public SharesHeld getSharesHeld() {
		return sharesHeld;
	}
}
