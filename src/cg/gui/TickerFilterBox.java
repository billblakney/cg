package cg.gui;

import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import cg.AbstractAccountData;

public class TickerFilterBox extends JComboBox {

	private final String ALL_STOCKS_STRING = "All Stocks";
	
	private JTable _table = null;
	
	private int _column = -1;

	public TickerFilterBox(JTable aTable,int aColumn)
	{
	   _table = aTable;
	   _column = aColumn;
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
	
	public void update()
	{
	   Vector list = new Vector();
		list.addElement(ALL_STOCKS_STRING);
		list.addAll(TableModelUtil.getColumnValues(_table.getModel(),_column));
		DefaultComboBoxModel model = new DefaultComboBoxModel(list);
		setModel(model);
	}

}
