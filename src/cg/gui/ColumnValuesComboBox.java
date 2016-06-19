package cg.gui;

import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import cg.AbstractAccountData;

public class ColumnValuesComboBox extends JComboBox {

	private String _allItem = "-All-";
	
	private JTable _table = null;
	
	private int _column = -1;

	public ColumnValuesComboBox(JTable aTable,int aColumn)
	{
	   _table = aTable;
	   _column = aColumn;
	}

	public ColumnValuesComboBox(JTable aTable,int aColumn,String aAllItem)
	{
	   _table = aTable;
	   _column = aColumn;
	   _allItem = aAllItem;
	}
	
	/**
	 * @return Returns selected year, or null if "All Tickers" is selected. 
	 */
	public String getSelectedTicker(){
		String item = (String)getSelectedItem();
		if (item.equals(_allItem))
			return null;
		else
			return item;
	}
	
	public void update()
	{
	   Vector list = new Vector();
		list.addElement(_allItem);
		list.addAll(TableModelUtil.getColumnValues(_table.getModel(),_column));
		DefaultComboBoxModel model = new DefaultComboBoxModel(list);
		setModel(model);
	}

}
