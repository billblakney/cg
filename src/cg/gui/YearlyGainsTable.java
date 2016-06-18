package cg.gui;

import java.util.*;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import javax.swing.table.AbstractTableModel;
import javax.swing.RowFilter;

public class YearlyGainsTable extends JTable {

	private YearlyGainsTableModel model;
	private TableRowSorter<YearlyGainsTableModel> sorter = null;

	RowFilter<AbstractTableModel, Integer> rowFilter;

	TotalGain totalGain;

	public YearlyGainsTable() {
		model = new YearlyGainsTableModel();
		setModel(model);

		sorter = new TableRowSorter<YearlyGainsTableModel>(model);
		setRowSorter(sorter);

		totalGain = new TotalGain();
		/*
		 * YearlyGainsTableRenderer r = new YearlyGainsTableRenderer();
		 * setDefaultRenderer(Object.class, r); setDefaultRenderer(Float.class,
		 * r); setDefaultRenderer(Integer.class, r);
		 */
	}

	public void setRows(Vector gains) {

		model.setData(gains);
		updateTotalGain();
	}

	private void setRowFilter() {
		List<RowFilter<AbstractTableModel, Integer>> filters = new ArrayList<RowFilter<AbstractTableModel, Integer>>(
				2);
	}

	private void updateTotalGain() {
		Integer g = model.getTotalGain(rowFilter);
		totalGain.setValue(g);
	}

	public YearlyGainsTable.TotalGain getTotalGain() {
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

