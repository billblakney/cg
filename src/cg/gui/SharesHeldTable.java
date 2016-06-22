package cg.gui;

import java.util.*;

import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import javax.swing.table.AbstractTableModel;
import javax.swing.RowFilter;
import javax.swing.RowFilter.*;

import cg.Term;

public class SharesHeldTable extends JTable {

	private SharesHeldTableModel model;
	private TableRowSorter<SharesHeldTableModel> sorter = null;

	private RowFilter<AbstractTableModel,Integer> _tickerFilter = null;
	private RowFilter<AbstractTableModel,Integer> _termFilter = null;

	RowFilter<AbstractTableModel, Integer> rowFilter;

	SharesHeld sharesHeld;

	public SharesHeldTable() {
		model = new SharesHeldTableModel();
		setModel(model);

		sorter = new TableRowSorter<SharesHeldTableModel>(model);
		setRowSorter(sorter);
		
		sharesHeld = new SharesHeld();
		
//		RenderIntWithComma tIntRender = new RenderIntWithComma(SharesHeldTableModel.COL_SHARES);
//		CustomTableCellRenderer tRenderer = new CustomTableCellRenderer(tIntRender);
//		
//		setDefaultRenderer(Integer.class,tRenderer);

		/*
		 * SharesHeldTableRenderer r = new SharesHeldTableRenderer();
		 * setDefaultRenderer(Object.class, r); setDefaultRenderer(Float.class,
		 * r); setDefaultRenderer(Integer.class, r);
		 * setDefaultRenderer(Integer.class, r);
		 */
	}

	public void setRows(Vector gains) {
		model.setData(gains);
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

