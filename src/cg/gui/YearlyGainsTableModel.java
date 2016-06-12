package cg.gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.RowFilter;

import cg.YearlyGain;

/*
 * Note: These three methods must be implemented to extend AbstractTableModel
 *   public int getRowCount();
 *   public int getColumnCount();
 *   public Object getValueAt(int row, int column);
 */
public class YearlyGainsTableModel extends AbstractTableModel implements GainColumnFinder {

	private String[] columnNames = { "Year", "Gain" };

	final static int COL_YEAR = 0;
	final static int COL_GAIN = 1;

	private Vector data;

	public int getGainColumn(){
		return COL_GAIN;
	}

	YearlyGainsTableModel() {
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
		
		YearlyGain gain = (YearlyGain) row;
		Vector v = new Vector();
		v.addElement(new Integer(gain.year)); //0
		v.addElement(new Integer(Math.round(gain.gain))); //1
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
			Integer gain = (Integer) entry.getValue(YearlyGainsTableModel.COL_GAIN);
			totalGain += gain;
		}
		
		return totalGain;
	}
}

