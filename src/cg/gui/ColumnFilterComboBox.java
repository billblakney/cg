package cg.gui;

import java.util.TreeSet;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Class used to filter rows in a table per the values of specific column.
 * TODO document
 */
public class ColumnFilterComboBox extends JComboBox {

	protected String _allItem = "-All-";
	
	protected JTable _table = null;
	
	protected int _column = -1;

	/**
	 * Constructor.
	 */
	public ColumnFilterComboBox(JTable aTable,int aColumn)
	{
	   _table = aTable;
	   _column = aColumn;
	}

	/**
	 * Constructor.
	 */
	public ColumnFilterComboBox(JTable aTable,int aColumn,String aAllItem)
	{
	   _table = aTable;
	   _column = aColumn;
	   _allItem = aAllItem;
	}
	
	/**
	 * Update the selection list based on current values in the table model.
	 * This method should be called when the table contents changes.
	 */
	public void update()
	{
	   Vector list = new Vector();
		list.addElement(_allItem);
		list.addAll(getModelListItems(_table.getModel(),_column));
		DefaultComboBoxModel model = new DefaultComboBoxModel(list);
		setModel(model);
	}
	
	/**
	 * Gets a value to be placed in the combobox selection list corresponding
	 * to a specific table model cell entry.
	 * @param aModel
	 * @param aRow
	 * @param aCol
	 * @return
	 */
	public String getListItem(TableModel aModel,int aRow,int aCol)
	{
	   return aModel.getValueAt(aRow,aCol).toString();
	}

	/**
	 * Get the row filter corresponding to the current combobox selection.
	 * @return
	 */
	public RowFilter<AbstractTableModel,Integer> getFilter()
	{
		String tItem = (String)getSelectedItem();

		if (tItem.equals(_allItem))
		{
			tItem = ".*";
		}
		return RowFilter.regexFilter(tItem,_column);
	}

	/**
	 * Get the set of items to be placed in the combobox list.
	 * This method uses the getListItem method to convert the value of
	 * a table model cell to a value to be placed in the combobox list.
	 * @param aModel
	 * @param aCol
	 * @return
	 */
   private TreeSet<String> getModelListItems(TableModel aModel,int aCol)
   {
      TreeSet<String> tValues = new TreeSet<String>();
      
      for (int i = 0; i < aModel.getRowCount(); i++)
      {
         tValues.add(getListItem(aModel,i,aCol));
      }
      return tValues;
   }

}
