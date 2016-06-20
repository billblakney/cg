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

	private RowFilter<AbstractTableModel,Integer> _tickerFilter = null;
	private RowFilter<AbstractTableModel,Integer> _yearFilter = null;

	private RowFilter<AbstractTableModel, Integer> rowFilter;

	private TotalGain totalGain;

	public LotGainTable() {
		model = new LotGainTableModel();
		setModel(model);

		sorter = new TableRowSorter<LotGainTableModel>(model);
		setRowSorter(sorter);

		DefaultLotTableRenderer r = new DefaultLotTableRenderer(sorter);
		IntegerLotTableRenderer lr = new IntegerLotTableRenderer(sorter);
		setDefaultRenderer(Object.class, r);
		setDefaultRenderer(Float.class, r);
		setDefaultRenderer(Integer.class, lr);

		totalGain = new TotalGain();
	}

	public void setRows(Vector gains) {

		model.setData(gains);
		updateTotalGain();
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

	private void setRowFilter()
	{
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
