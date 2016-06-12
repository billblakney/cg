package cg.gui;

import java.util.*;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.RowFilter.*;

public class LotGainTable extends JTable {

	private LotGainTableModel model;
	private TableRowSorter<LotGainTableModel> sorter = null;

	private String filterYear;

	final private String ANY_TICKER = ".*";
	private String tickerRegEx = ANY_TICKER;

	private RowFilter<AbstractTableModel, Integer> rowFilter;

	private TotalGain totalGain;

	public LotGainTable() {
		model = new LotGainTableModel();
		setModel(model);

		sorter = new TableRowSorter<LotGainTableModel>(model);
		setRowSorter(sorter);

		DefaultLotTableRenderer r = new DefaultLotTableRenderer(sorter);
		LongLotTableRenderer lr = new LongLotTableRenderer(sorter);
		setDefaultRenderer(Object.class, r);
		setDefaultRenderer(Float.class, r);
		setDefaultRenderer(Integer.class, lr);
		setDefaultRenderer(Long.class, lr);

		totalGain = new TotalGain();
	}

	public void setRows(Vector gains) {

		model.setData(gains);
		updateTotalGain();
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
				2);
		// ticker filter
		RowFilter<AbstractTableModel, Integer> tf = RowFilter.regexFilter(
				tickerRegEx, LotGainTableModel.COL_TICKER);
		filters.add(tf);
		// year filter
		filters.add(new CGDateFilter(filterYear,LotGainTableModel.COL_SELLDATE));

		rowFilter = RowFilter.andFilter(filters);
		sorter.setRowFilter(rowFilter);

		updateTotalGain();
	}

	private void updateTotalGain() {
		Integer g = model.getTotalGain(rowFilter);
		totalGain.setValue(g);
	}

	public LotGainTable.TotalGain getTotalGain() {
		return totalGain;
	}

	// The observers
	public class TotalGain extends Observable {

		private Integer value;

		TotalGain() {
			setValue(new Integer(0));
		}

		Integer getValue() {
			return value;
		}

		private void setValue(Integer value) {
			this.value = value;
			setChanged();
			notifyObservers(getValue());
			// clearChanged() automatically called by notifyObservers()
		}
	}
}
