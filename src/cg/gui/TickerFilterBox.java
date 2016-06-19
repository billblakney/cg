package cg.gui;

import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import cg.AbstractAccountData;

public class TickerFilterBox extends JComboBox {

	private final String ALL_STOCKS_STRING = "All Stocks";
	
	public TickerFilterBox()
	{
	}
	
	/**
	 * @return Returns selected year, or null if "All Tickers" is selected. 
	 */
	public String getSelectedTicker(){
		String item = (String)getSelectedItem();
		if (item.equals(ALL_STOCKS_STRING))
			return null;
		else
			return item;
	}
//	
//	public void update(AbstractAccountData acct) {
//		Vector list = new Vector();
//		list.addElement(ALL_STOCKS_STRING);
//		list.addAll(acct.getTickers());
//		DefaultComboBoxModel model = new DefaultComboBoxModel(list);
//		setModel(model);
//	}
	
	public void update(TableModel aModel) {
		Vector list = new Vector();
		list.addElement(ALL_STOCKS_STRING);
		if (aModel instanceof SymbolColumnFinder)
		{
		   SymbolColumnFinder tFinder = (SymbolColumnFinder)aModel;
		   int tColumn = tFinder.getSymbolColumn();
		   list.addAll(TableModelUtil.getColumnValues(aModel, tColumn));
		}
		DefaultComboBoxModel model = new DefaultComboBoxModel(list);
		setModel(model);
	}

}
