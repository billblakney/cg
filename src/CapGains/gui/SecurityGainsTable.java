package CapGains.gui;

import java.util.*;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import javax.swing.RowFilter;
import javax.swing.RowFilter.*;

public class SecurityGainsTable extends JTable {

	private SecurityGainsTableModel model;
	private TableRowSorter<SecurityGainsTableModel> sorter = null;

	RowFilter<AbstractTableModel, Integer> rowFilter;

	TotalGain totalGain;

	public SecurityGainsTable() {
		model = new SecurityGainsTableModel();
		setModel(model);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //zzz


		sorter = new TableRowSorter<SecurityGainsTableModel>(model);
		setRowSorter(sorter);

		totalGain = new TotalGain();
		/*
		 * SecurityGainsTableRenderer r = new SecurityGainsTableRenderer();
		 * setDefaultRenderer(Object.class, r); setDefaultRenderer(Float.class,
		 * r); setDefaultRenderer(Integer.class, r);
		 * setDefaultRenderer(Long.class, r);
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

	public SecurityGainsTable.TotalGain getTotalGain() {
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
	
	/// zzz
	// These next two methods were copied from a web page. They set the column
	// widths to match the header and data. If I want to use them, I should
	// probably add them to a JTable subclass, or create a helper class for
	// JTable. I'm leaving them right here now for future reference (and
	// a future decision on whether or not to use them.
	public void packColumns(int margin) {
	    for (int c=0; c<getColumnCount(); c++) {
	        packColumn(c, 2);
	    }
	}

	// Sets the preferred width of the visible column specified by vColIndex. The column
	// will be just wide enough to show the column head and the widest cell in the column.
	// margin pixels are added to the left and right
	// (resulting in an additional width of 2*margin pixels).
	public void packColumn(int vColIndex, int margin) {
	    TableModel model = getModel();
	    DefaultTableColumnModel colModel = (DefaultTableColumnModel)getColumnModel();
	    TableColumn col = colModel.getColumn(vColIndex);
	    int width = 0;

	    // Get width of column header
	    TableCellRenderer renderer = col.getHeaderRenderer();
	    if (renderer == null) {
	        renderer = getTableHeader().getDefaultRenderer();
	    }
	    Component comp = renderer.getTableCellRendererComponent(
	        this, col.getHeaderValue(), false, false, 0, 0);
	    width = comp.getPreferredSize().width;

	    // Get maximum width of column data
	    for (int r=0; r<getRowCount(); r++) {
	        renderer = getCellRenderer(r, vColIndex);
	        comp = renderer.getTableCellRendererComponent(
	            this, getValueAt(r, vColIndex), false, false, r, vColIndex);
	        width = Math.max(width, comp.getPreferredSize().width);
	    }

	    // Add margin
	    width += 2*margin;

	    // Set the width
	    col.setPreferredWidth(width);
	}
}

