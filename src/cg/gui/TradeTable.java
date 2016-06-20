package cg.gui;

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

	private RowFilter<AbstractTableModel,Integer> _tickerFilter = null;
	private RowFilter<AbstractTableModel,Integer> _yearFilter = null;
	
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
		IntegerTradeTableRenderer lr = new IntegerTradeTableRenderer(sorter);
		setDefaultRenderer(Object.class, r);
		setDefaultRenderer(Float.class, r);
		setDefaultRenderer(Integer.class, r);
		setDefaultRenderer(cg.Trade.Type.class,r);
		setDefaultRenderer(cg.Trade.SpecialInstruction.class,r);
	}

	public void setRows(Vector trades) {

		model.setData(trades);
		updateSharesHeld();
	}
	
	void filterShowOpenPositions(boolean show){
		openPositionsOnly = show;
		setRowFilter();
	}

	public void filterOnTicker(RowFilter<AbstractTableModel,Integer> aFilter)
	{
	   _tickerFilter = aFilter;
		setRowFilter();
	}

	public void filterOnYear(RowFilter<AbstractTableModel,Integer> aFilter)
	{
	   _yearFilter = aFilter;
		setRowFilter();
	}

	private void setRowFilter() {
		List<RowFilter<AbstractTableModel, Integer>> filters =
		      new Vector<RowFilter<AbstractTableModel, Integer>>();

		if (_tickerFilter != null)
		{
		   filters.add(_tickerFilter);
		}

		if (_yearFilter != null)
		{
		   filters.add(_yearFilter);
		}

		//open positions filter
		filters.add(new TradeIsOpenPositionFilter(openPositionsOnly));
		sorter.setRowFilter(null);

		
		rowFilter = RowFilter.andFilter(filters);
		sorter.setRowFilter(rowFilter);
		model.fireTableDataChanged();

		updateSharesHeld();
	}

	private void updateSharesHeld() {
		Integer shares = model.getSharesHeld(rowFilter);
		sharesHeld.setValue(shares);
	}

	public SharesHeld getSharesHeld() {
		return sharesHeld;
	}
}
