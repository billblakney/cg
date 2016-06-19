package cg.gui;

import java.util.TreeSet;
import javax.swing.table.TableModel;

public class TableModelUtil
{
   static TreeSet<String> getColumnValues(TableModel aModel,int aCol)
   {
      TreeSet<String> tValues = new TreeSet<String>();
      
      for (int i = 0; i < aModel.getRowCount(); i++)
      {
System.out.println(aModel.getValueAt(i, aCol).toString());
         tValues.add(aModel.getValueAt(i, aCol).toString());
      }
      return tValues;
   }
}
