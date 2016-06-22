package cg.gui;

import java.util.*;

import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import javax.swing.table.AbstractTableModel;
import javax.swing.RowFilter;
import javax.swing.RowFilter.*;

import cg.LotDataProvider;
import cg.Term;

public class LotsHeldTable extends JTable {

	private LotsHeldTableModel model;
	private TableRowSorter<LotsHeldTableModel> sorter = null;

	private RowFilter<AbstractTableModel,Integer> _tickerFilter = null;
	private RowFilter<AbstractTableModel,Integer> _termFilter = null;

	RowFilter<AbstractTableModel, Integer> rowFilter;

	SharesHeld sharesHeld;

	public LotsHeldTable() {
		model = new LotsHeldTableModel();
		setModel(model);

		sorter = new TableRowSorter<LotsHeldTableModel>(model);
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
		      RenderInteger.COMMA_FORMAT,LotsHeldTableModel.COL_SHARES);
		tInfos.add(tIntRenderShares);
	   
		/*
		 * Create the custom table cell renderer and apply it to all columns.
		 */
		CustomTableCellRenderer tRenderer = new CustomTableCellRenderer(tInfos);

		for (int i = 0; i < getColumnCount(); i++)
		{
		    getColumnModel().getColumn(i).setCellRenderer(tRenderer);
		}
	}

	public void setRows(Vector<LotDataProvider> lots) {
		model.setData(lots);
		updateSharesHeld();
	}

	public void filterOnTicker(RowFilter<AbstractTableModel,Integer> aFilter)
	{
	   _tickerFilter = aFilter;
		setRowFilter();
	}

	public void filterOnTerm(RowFilter<AbstractTableModel,Integer> aFilter)
	{
	   _termFilter = aFilter;
		setRowFilter();
	}

	private void setRowFilter()
	{
		List<RowFilter<AbstractTableModel, Integer>> filters =
		      new Vector<RowFilter<AbstractTableModel, Integer>>();

		if (_tickerFilter != null)
		{
		   filters.add(_tickerFilter);
		}

		if (_termFilter != null)
		{
		   filters.add(_termFilter);
		}

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

