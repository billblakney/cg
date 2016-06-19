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

	/** Default filter for Term. */
	private Term filterTerm = Term.BOTH;

	final private String ANY_TICKER = ".*";
	/** Default filter for Ticker. */
	private String tickerRegEx = ANY_TICKER;

	RowFilter<AbstractTableModel, Integer> rowFilter;

	SharesHeld sharesHeld;

	public SharesHeldTable() {
		model = new SharesHeldTableModel();
		setModel(model);

		sorter = new TableRowSorter<SharesHeldTableModel>(model);
		setRowSorter(sorter);
		
		sharesHeld = new SharesHeld();

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

	public void filterOnTicker(String ticker) {
		if (null == ticker)
			tickerRegEx = ANY_TICKER;
		else
			tickerRegEx = new String(ticker);
		setRowFilter();
	}

	public void filterOnTerm(Term term) {
		filterTerm = term;
		setRowFilter();
	}

	private void setRowFilter() {
		List<RowFilter<AbstractTableModel, Integer>> filters = new ArrayList<RowFilter<AbstractTableModel, Integer>>(
				2);
		// ticker filter
		RowFilter<AbstractTableModel, Integer> tf = RowFilter.regexFilter(
				tickerRegEx, SharesHeldTableModel.COL_TICKER);
		filters.add(tf);

		// term filter
		filters.add(new CGTermFilter(filterTerm,SharesHeldTableModel.COL_TERM));

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

