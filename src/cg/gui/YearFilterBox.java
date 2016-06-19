package cg.gui;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import cg.AbstractAccountData;

public class YearFilterBox extends JComboBox {

	private final String ALL_YEARS_STRING = "All Years";
	
	public YearFilterBox(){
	}
	
	/**
	 * @return Returns selected year, or null if "All Years" is selected. 
	 */
	public String getSelectedYear(){
		String year;
		Object item = getSelectedItem();
		if( item.getClass() == String.class ){ // then it is "All Years"
			year = null;
		} else { // item is Integer
			year = ((Integer)item).toString();
		}
		return year;
	}
	
	public void update(AbstractAccountData acct) {
		Vector list = new Vector();
		list.addElement(ALL_YEARS_STRING);
		list.addAll(acct.getYears(true));
		DefaultComboBoxModel model = new DefaultComboBoxModel(list);
		setModel(model);
	}

}
