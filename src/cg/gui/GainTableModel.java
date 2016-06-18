package cg.gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.RowFilter;

import cg.TaxGain;

/*
 * Note: These three methods must be implemented to extend AbstractTableModel
 *   public int getRowCount();
 *   public int getColumnCount();
 *   public Object getValueAt(int row, int column);
 */
public class GainTableModel extends AbstractTableModel implements GainColumnFinder {

	private String[] columnNames = { "Shares", "Ticker", "Buy Date",
			"Sell Date", "Gross", "Basis", "Gain", "Term", "Computed Tax Year",
			"Reported Tax Year" };

	final static int COL_SHARES = 0;
	final static int COL_TICKER = 1;
	final static int COL_BUYDATE = 2;
	final static int COL_SELLDATE = 3;
	final static int COL_GROSS = 4;
	final static int COL_BASIS = 5;
	final static int COL_GAIN = 6;
	final static int COL_TERM = 7;
	final static int COL_CTAXYEAR = 8;
	final static int COL_RTAXYEAR = 9;

	private Vector data;

	public int getGainColumn(){
		return COL_GAIN;
	}
	
	GainTableModel() {
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

	public void setData(Vector gains){
		data = new Vector();
		for (int i = 0; i < gains.size(); i++)
			data.addElement(getDataRow(gains.elementAt(i)));
		fireTableDataChanged();
	}

	/**
	 * Get vector data for a gain.
	 */
	private Vector getDataRow(Object row) {
		TaxGain taxGain = (TaxGain) row;

		Vector v = new Vector();
		v.addElement(new Integer(taxGain.numShares)); //0
		v.addElement(taxGain.ticker); //1
		v.addElement(taxGain.buyDateRange); //2
		v.addElement(taxGain.sellDate); //3
		v.addElement(new Integer(Math.round(taxGain.proceeds))); //4
		v.addElement(new Integer(Math.round(taxGain.basis))); //5
		v.addElement(new Integer(Math.round(taxGain.gain))); //6
		v.addElement(taxGain.term); //7
		v.addElement(new Integer(taxGain.computedTaxYear.intValue())); //8
		v.addElement(new Integer(taxGain.claimedTaxYear.intValue())); //9
		return v;
	}
	
	public Integer getTotalGain(RowFilter<AbstractTableModel, Integer> filter) {
		Integer totalGain = new Integer(0);
		for (int i = 0; i < data.size(); i++) {
			RowFilter.Entry<AbstractTableModel, Integer> entry = new TableModelFilterEntry(
					(AbstractTableModel)this, i);
			// pass over entry if filter.include() returns false
			if ((filter != null) && (filter.include(entry) == false))
				continue;
			Integer gain = (Integer) entry.getValue(GainTableModel.COL_GAIN);
			totalGain += gain;
		}
		return totalGain;
	}
}
