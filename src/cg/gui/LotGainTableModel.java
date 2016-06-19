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
 *   TODO remove computed and tax year?
 */
public class LotGainTableModel extends AbstractTableModel implements SymbolColumnFinder, GainColumnFinder {

	private String[] columnNames = { "Shares", "Ticker", "Buy Date", "Buy Price",
			"Sell Date", "Sell Price", "Gross", "Basis", "Gain", "Term"/*zzz,
			"Computed Tax Year", "Reported Tax Year"*/ };

	final static int COL_SHARES = 0;
	final static int COL_TICKER = 1;
	final static int COL_BUYDATE = 2;
	final static int COL_BUYPRICE = 3;
	final static int COL_SELLDATE = 4;
	final static int COL_SELLPRICE = 5;
	final static int COL_GROSS = 6;
	final static int COL_BASIS = 7;
	final static int COL_GAIN = 8;
	final static int COL_TERM = 9;
//	final static int COL_CTAXYEAR = 10;
//	final static int COL_RTAXYEAR = 11;

	private Vector data;
	
	LotGainTableModel() {
		data = new Vector(0);
	}

	@Override
	public int getSymbolColumn(){
		return COL_TICKER;
	}

	@Override
	public int getGainColumn(){
		return COL_GAIN;
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

	public void setData(Vector lots){
		data = new Vector();
		for (int i = 0; i < lots.size(); i++)
			data.addElement(getDataRow(lots.elementAt(i)));
		fireTableDataChanged();
	}

	/**
	 * Get vector data for a gain.
	 */
	private Vector getDataRow(Object row) {
		OldLot lot = (OldLot) row;

		Vector v = new Vector();
		v.addElement(new Integer(lot.numShares)); //0
		v.addElement(lot.ticker); //1
		v.addElement(lot.buyDate); //2
		v.addElement(lot.buyPrice); //3
		v.addElement(lot.sellDate); //4
		v.addElement(lot.sellPrice); //5
		v.addElement(new Integer(Math.round(lot.proceeds))); //6
		v.addElement(new Integer(Math.round(lot.basis))); //7
		v.addElement(new Integer(Math.round(lot.gain))); //8
		v.addElement(lot.term); //9
//		v.addElement(new Integer(lot.computedTaxYear.intValue())); //10
//		v.addElement(new Integer(lot.claimedTaxYear.intValue())); //11
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
			Integer gain = (Integer) entry.getValue(LotGainTableModel.COL_GAIN);
			totalGain += gain;
		}
		return totalGain;
	}
}
