package cg.gui;

import java.awt.Color;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import bbj.swing.table.render.CustomTableCellRenderer;
import bbj.swing.table.render.RenderColoredRows;
import bbj.swing.table.render.RenderInteger;
import bbj.swing.table.render.RenderTableCellInfo;
import bbj.swing.table.render.TableCellColorSet;
import bbj.swing.table.render.TableCellColorIndexChooser;
import cg.Trade;
import cg.TradeDataProvider;

@SuppressWarnings("serial")
public class TradeTable extends JTable {
	
   final static public TableCellColorSet kBuyColors = new TableCellColorSet(
         new Color (235,237,255),
         Color.lightGray,
         Color.white,
         Color.black,
         Color.black,
         Color.black);
	
   final static public TableCellColorSet kSellColors = new TableCellColorSet(
         new Color (217,251,209),
         Color.lightGray,
         Color.white,
         Color.black,
         Color.black,
         Color.black);

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
	
	TradeTable()
	{
		model = new TradeTableModel();
		setModel(model);
		
		sorter = new TableRowSorter<TradeTableModel>(model);
		setRowSorter(sorter);
		
		sharesHeld = new SharesHeld();
		
		setRenderers();
	}

	private void setRenderers()
	{
		Vector<RenderTableCellInfo> tInfos = new Vector<RenderTableCellInfo>();
		
		/*
		 * Set renders for comma separated integer columns.
		 */
		RenderInteger tIntRenderShares = new RenderInteger(
		      RenderInteger.COMMA_FORMAT,TradeTableModel.COL_SHARES);
		tInfos.add(tIntRenderShares);
		
		RenderInteger tIntRenderSharesHeld = new RenderInteger(
		      RenderInteger.COMMA_FORMAT,TradeTableModel.COL_SHARESHELD);
		tInfos.add(tIntRenderSharesHeld);
		
		RenderInteger tIntRenderSharesSold = new RenderInteger(
		      RenderInteger.COMMA_FORMAT,TradeTableModel.COL_SHARESSOLD);
		tInfos.add(tIntRenderSharesSold);

		/*
		 * Set the render for row coloring.
		 */
		Vector<TableCellColorSet> tColors = new Vector<TableCellColorSet>();
		tColors.add(kBuyColors);
		tColors.add(kSellColors);
		
		TableCellColorIndexChooser tColorChooser =
		      (JLabel label,JTable table,Object value,boolean isSelected,
		            boolean hasFocus, int row,int column) -> 
		{
		   TableModel tModel = table.getModel();
		   int tModelRow = sorter.convertRowIndexToModel(row);

		   Trade.Type tTradeType = (Trade.Type)tModel.getValueAt(
		         tModelRow,TradeTableModel.COL_BUYSELL);

		   return ((tTradeType==Trade.Type.BUY)?0:1);
		};

		RenderColoredRows tRowRender = new RenderColoredRows(
		     tColors,tColorChooser);
//		tInfos.add(tRowRender);

		/*
		 * Create the custom table cell renderer and apply it to all columns.
		 */
		CustomTableCellRenderer tRenderer = new CustomTableCellRenderer(tInfos);

		for (int i = 0; i < getColumnCount(); i++)
		{
		    getColumnModel().getColumn(i).setCellRenderer(tRenderer);
		}
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
