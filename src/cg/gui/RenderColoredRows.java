package cg.gui;
import java.awt.Font;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JTable;

public class RenderColoredRows implements RenderTableCellInfo
{
	List<TableCellColorSet> _scheme;
	TableRowColorChooser _colorIndexGetter;
	Set<Integer> _columns;

   RenderColoredRows(List<TableCellColorSet> aColorScheme,TableRowColorChooser aTest)
   {
      _scheme = aColorScheme;
      _colorIndexGetter = aTest;
   }

   RenderColoredRows(List<TableCellColorSet> aColorScheme,TableRowColorChooser aTest,Set<Integer> aColumns)
   {
      _scheme = aColorScheme;
      _colorIndexGetter = aTest;
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

      int tIndex = _colorIndexGetter.getIndex(label, table, value, isSelected, hasFocus, row, column);
      
      if (tIndex < 0 || tIndex >= _scheme.size())
      {
         System.out.print("ERROR: invalid color set");
         return null;
      }

      TableCellInfo tInfo = new TableCellInfo();

      TableCellColorSet tColorSet = _scheme.get(tIndex);

		// cell in normal row (isSelected == false)
		if (isSelected == false) {
			tInfo._font = label.getFont().deriveFont(Font.PLAIN);
			tInfo._bgColor = tColorSet._bgNormal;
			tInfo._fgColor = tColorSet._fgNormal;
		// cell in selected row without focus
		} else if ( isSelected == true && hasFocus == false ){
			tInfo._font = label.getFont().deriveFont(Font.ITALIC);
			tInfo._bgColor = tColorSet._bgSelected;
			tInfo._fgColor = tColorSet._fgSelected;
		// cell in selected row with focus
		} else {
			tInfo._font = label.getFont().deriveFont(Font.PLAIN);
			tInfo._bgColor = tColorSet._bgFocus;
			tInfo._fgColor = tColorSet._fgFocus;
		}
	   
	   return tInfo;
	}
}
