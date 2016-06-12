package cg.gui;

import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;

import cg.Term;

public class CGTermFilter extends RowFilter<AbstractTableModel, Integer> {

	private Term term;
	private int column;

	public CGTermFilter(Term term,int col) {
		column = col;
		this.term = term;
	}

	public boolean include(
			Entry<? extends AbstractTableModel, ? extends Integer> entry) {
		if( term == Term.BOTH)
			return true;

		Term t = (Term) entry.getValue(column);
		if( t.toString().equals(term.toString()) )
			return true;
		else
			return false;
	}
}