package cg.gui;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JTable;

public class RenderColoredRows implements RenderTableCellInfo
{
	TableTwoColorScheme _scheme;
	RenderTableCellTest _test;
	Set<Integer> _columns;

   RenderColoredRows(TableTwoColorScheme aColorScheme,RenderTableCellTest aTest)
   {
      _scheme = aColorScheme;
      _test = aTest;
   }

   RenderColoredRows(TableTwoColorScheme aColorScheme,RenderTableCellTest aTest,Set<Integer> aColumns)
   {
      _scheme = aColorScheme;
      _test = aTest;
      _columns = aColumns;
   }

   @Override
	public TableCellInfo getInfo(JLabel label,JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
	{
      if (_columns != null && !_columns.contains(column))
      {
         return null;
      }

      TableCellInfo tInfo = new TableCellInfo();

      int i = 0;
      if (_test.test(label, table, value, isSelected, hasFocus, row, column))
      {
         i = 1;
      }

		// cell in normal row (isSelected == false)
		if (isSelected == false) {
			tInfo._font = label.getFont().deriveFont(Font.PLAIN);
			tInfo._bgColor =_scheme.bg_Normal[i];
			tInfo._fgColor = _scheme.fg_Normal[i];
		// cell in selected row without focus
		} else if ( isSelected == true && hasFocus == false ){
			tInfo._font = label.getFont().deriveFont(Font.ITALIC);
			tInfo._bgColor = _scheme.bg_IsSelected[i];
			tInfo._fgColor = _scheme.fg_IsSelected[i];
		// cell in selected row with focus
		} else {
			tInfo._font = label.getFont().deriveFont(Font.PLAIN);
			tInfo._bgColor = _scheme.bg_HasFocus[i];
			tInfo._fgColor = _scheme.fg_HasFocus[i];
		}
	   
	   return tInfo;
	}

}
