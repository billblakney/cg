package cg.gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.RowFilter;

import cg.LotDataProvider;

/*
 * Note: These three methods must be implemented to extend AbstractTableModel
 *   public int getRowCount();
 *   public int getColumnCount();
 *   public Object getValueAt(int row, int column);
 */
public class LotsHeldTableModel extends AbstractTableModel implements SymbolColumnFinder {

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

	@Override
	public int getSymbolColumn(){
		return COL_TICKER;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
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

	@Override
	public Object getValueAt(int arg0, int arg1) {
		if (arg0 > data.size() || arg1 > columnNames.length)
			throw new ArrayIndexOutOfBoundsException();
		else
			return ((Vector) (data.elementAt(arg0))).elementAt(arg1);
	}

	public void setData(Vector<LotDataProvider> stats){
		data = new Vector();
		for (int i = 0; i < stats.size(); i++)
			data.addElement(getDataRow(stats.elementAt(i)));
		fireTableDataChanged();
	}

	/**
	 * Get vector data for a gain.
	 */
	private Vector getDataRow(Object row) {
		
		LotDataProvider lot = (LotDataProvider) row;
		Vector v = new Vector();
		v.addElement(lot.getSymbol()); //0
		v.addElement(lot.getBuyDate()); //1
		v.addElement(lot.getNumShares()); //2
		v.addElement(lot.getBuyPrice()); //3
		v.addElement(lot.getTerm()); //4
		return v;
	}
	
	public Integer getSharesHeld(RowFilter<AbstractTableModel, Integer> filter) {
		Integer totalShares = new Integer(0);
		for (int i = 0; i < data.size(); i++) {
			RowFilter.Entry<AbstractTableModel, Integer> entry = new TableModelFilterEntry(
					(AbstractTableModel)this, i);
			// pass over entry if filter.include() returns false
			if ((filter != null) && (filter.include(entry) == false))
				continue;
			Integer shares = (Integer) entry.getValue(LotsHeldTableModel.COL_SHARES);
			totalShares += shares;
		}
		return totalShares;
	}
}

