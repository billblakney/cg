package cg.gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.RowFilter;

import cg.OldLot;

/*
 * Note: These three methods must be implemented to extend AbstractTableModel
 *   public int getRowCount();
 *   public int getColumnCount();
 *   public Object getValueAt(int row, int column);
 */
public class LotsHeldTableModel extends AbstractTableModel {

	private String[] columnNames = { "Ticker", "Buy Date", "Shares", "Buy Price", "Term" };

	final static int COL_TICKER = 0;
	final static int COL_BUY_DATE = 1;
	final static int COL_SHARES = 2;
	final static int COL_BUY_PRICE = 3;
	final static int COL_TERM = 4;

	private Vector data;

	LotsHeldTableModel() {
		data = new Vector(0);
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class getColumnClass(int c) {
		if (data.size() > 0) {
			Object value = getValueAt(0, c);
			return (value == null ? Object.class : value.getClass());
		} else
			return Object.class;
	}

	public Object getValueAt(int arg0, int arg1) {
		if (arg0 > data.size() || arg1 > columnNames.length)
			throw new ArrayIndexOutOfBoundsException();
		else
			return ((Vector) (data.elementAt(arg0))).elementAt(arg1);
	}

	public void setData(Vector stats){
		data = new Vector();
		for (int i = 0; i < stats.size(); i++)
			data.addElement(getDataRow(stats.elementAt(i)));
		fireTableDataChanged();
	}

	/**
	 * Get vector data for a gain.
	 */
	private Vector getDataRow(Object row) {
		
		OldLot lot = (OldLot) row;
		Vector v = new Vector();
		v.addElement(lot.ticker); //0
		v.addElement(lot.buyDate); //1
		v.addElement(lot.numShares); //2
		v.addElement(lot.buyPrice); //3
		v.addElement(lot.term); //4
		return v;
	}
	
	public Long getSharesHeld(RowFilter<AbstractTableModel, Integer> filter) {
		Long totalShares = new Long(0);
		for (int i = 0; i < data.size(); i++) {
			RowFilter.Entry<AbstractTableModel, Integer> entry = new TableModelFilterEntry(
					(AbstractTableModel)this, i);
			// pass over entry if filter.include() returns false
			if ((filter != null) && (filter.include(entry) == false))
				continue;
			Long shares = (Long) entry.getValue(LotsHeldTableModel.COL_SHARES);
			totalShares += shares;
		}
		return totalShares;
	}
}

