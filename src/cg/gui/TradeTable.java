package cg.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import cg.TradeDataProvider;
import cg.gui.render.DefaultTradeTableRenderer;
import cg.gui.render.IntegerTradeTableRenderer;

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
		
		/*
		 * TODO cleanup/document this renderer stuff
		 * TODO use new render classes for all tables, rm old classes
		 */
		
		Vector<RenderTableCellInfo> tInfos = new Vector<RenderTableCellInfo>();
		
		RenderInteger tIntRenderShares = new RenderInteger(
		      RenderInteger.COMMA_FORMAT,TradeTableModel.COL_SHARES);
		tInfos.add(tIntRenderShares);
		
		RenderInteger tIntRenderSharesHeld = new RenderInteger(
		      RenderInteger.COMMA_FORMAT,TradeTableModel.COL_SHARESHELD);
		tInfos.add(tIntRenderSharesHeld);
		
		RenderInteger tIntRenderSharesSold = new RenderInteger(
		      RenderInteger.COMMA_FORMAT,TradeTableModel.COL_SHARESSOLD);
		tInfos.add(tIntRenderSharesSold);

		TableTwoColorScheme tColorScheme = new TableTwoColorScheme();
		tColorScheme.bg_Normal[0]     = new Color(235, 237, 255);
		tColorScheme.bg_Normal[1]     = new Color(217, 251, 209);
		RenderTableCellTest tTest =
		      (JLabel label,JTable table,Object value,boolean isSelected,
		            boolean hasFocus, int row,int column) -> 
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
		      return false;
		   else
		      return true;
		};

		RenderColoredRows tRowRender = new RenderColoredRows(tColorScheme,tTest);

		CustomTableCellRenderer tRenderer = new CustomTableCellRenderer(tInfos);
		tInfos.add(tRowRender);

		for (int i = 0; i < getColumnCount(); i++)
		{
		    getColumnModel().getColumn(i).setCellRenderer(tRenderer);
		}

//		DefaultTradeTableRenderer r = new DefaultTradeTableRenderer(sorter);
//		IntegerTradeTableRenderer lr = new IntegerTradeTableRenderer(sorter);
//		setDefaultRenderer(Object.class, r);
//		setDefaultRenderer(Float.class, r);
//		setDefaultRenderer(Integer.class, r);
//		setDefaultRenderer(cg.Trade.Type.class,r);
//		setDefaultRenderer(cg.Trade.SpecialInstruction.class,r);
	}

	public void setRows(Vector<TradeDataProvider> trades) {

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
