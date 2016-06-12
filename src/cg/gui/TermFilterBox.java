package cg.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import cg.Term;

public class TermFilterBox extends JComboBox {


	private final String ALL_TRADES_STRING = "All Trades";
	private final String SHORT_TERM_STRING = "Held Short Term";
	private final String LONG_TERM_STRING = "Held Long Term";
	private final String options[] = { ALL_TRADES_STRING, SHORT_TERM_STRING, LONG_TERM_STRING};

	public TermFilterBox(){
		DefaultComboBoxModel model = new DefaultComboBoxModel(options);
		setModel(model);
	}
	
	/**
	 * @return Returns true if "Open Term Only" is selected, or false for "All Trades". 
	 */
	public Term getTerm(){
		String item = (String)getSelectedItem();
		if( item.equals(SHORT_TERM_STRING) )
			return Term.SHORT;
		else if( item.equals(LONG_TERM_STRING) )
			return Term.LONG;
		else
			return Term.BOTH;
	}
}
