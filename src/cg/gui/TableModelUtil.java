package cg.gui;

import java.util.TreeSet;
import javax.swing.table.AbstractTableModel;

public class TableModelUtil
{
   static TreeSet<String> getColumnValues(AbstractTableModel aModel,int aCol)
   {
      TreeSet<String> tValues = new TreeSet<String>();
      
      for (int i = 0; i < aModel.getRowCount(); i++)
      {
         tValues.add(aModel.getValueAt(i, aCol).toString());
      }
      return tValues;
   }
}
