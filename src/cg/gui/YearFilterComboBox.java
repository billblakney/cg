package cg.gui;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import cg.SimpleDate;

public class YearFilterComboBox extends ColumnFilterComboBox
{
   /**
    * Constructor.
    */
	public YearFilterComboBox(JTable aTable,int aColumn)
	{
	   super(aTable,aColumn);
	}

   /**
    * Constructor.
    */
	public YearFilterComboBox(JTable aTable,int aColumn,String aAllItem)
	{
	   super(aTable,aColumn,aAllItem);
	}

	/**
	 * Gets a value to be placed in the combobox selection list corresponding
	 * to a specific table model cell entry.
	 * @param aModel
	 * @param aRow
	 * @param aCol
	 * @return
	 */
   @Override
	public String getListItem(TableModel aModel,int aRow,int aCol)
	{
	   SimpleDate tDate = (SimpleDate)aModel.getValueAt(aRow,aCol);
	   return tDate.getYearString();
	}

	/**
	 * Get the row filter corresponding to the current combobox selection.
	 * @return
	 */
   @Override
	public RowFilter<AbstractTableModel,Integer> getFilter()
	{
		String tItem = (String)getSelectedItem();

		if (tItem.equals(_allItem))
		{
			return RowFilter.regexFilter(".*",_column);
		}
		else
		{
		   return new CGDateFilter(tItem,_column);
		}
	}
}
