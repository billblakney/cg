package cg.gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.RowFilter;

import cg.SharesHeldStat;

/*
 * Note: These three methods must be implemented to extend AbstractTableModel
 *   public int getRowCount();
 *   public int getColumnCount();
 *   public Object getValueAt(int row, int column);
 */
public class SharesHeldTableModel extends AbstractTableModel {

	private String[] columnNames = { "Ticker", "Term", "Shares", "Cost", "Lo Price", "Hi Price", "Av Price" };

	final static int COL_TICKER = 0;
	final static int COL_TERM = 1;
	final static int COL_SHARES = 2;
	final static int COL_COST = 3;
	final static int COL_LO_PRICE = 4;
	final static int COL_HI_PRICE = 5;
	final static int COL_AV_PRICE = 6;

	private Vector data;

	SharesHeldTableModel() {
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
		
		SharesHeldStat stat = (SharesHeldStat) row;
		Vector v = new Vector();
		v.addElement(stat.ticker); //0
		v.addElement(stat.term); //1
		v.addElement(stat.num_shares); //2
		v.addElement(stat.total_cost); //3
		v.addElement(stat.lo_share_price); //4
		v.addElement(stat.hi_share_price); //5
		v.addElement(stat.av_share_price); //6
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
			Long shares = (Long) entry.getValue(SharesHeldTableModel.COL_SHARES);
			totalShares += shares;
		}
		return totalShares;
	}
}

