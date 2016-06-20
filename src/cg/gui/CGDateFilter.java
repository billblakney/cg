package cg.gui;

import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;

import cg.SimpleDate;

//TODO rename to DateRowFilter
public class CGDateFilter extends RowFilter<AbstractTableModel, Integer> {

	private String year;
	private int column;

	public CGDateFilter(String year,int col) {
		column = col;
		if( null == year)
			this.year = null;
		else
			this.year = new String(year);
	}

	public boolean include(
			Entry<? extends AbstractTableModel, ? extends Integer> entry) {
		if( null == year){
			return true;
		}
		SimpleDate date = (SimpleDate) entry.getValue(column);
		if ((null != date) && (date.getYearString().equals(year)))
			return true;
		else
			return false;
	}
}