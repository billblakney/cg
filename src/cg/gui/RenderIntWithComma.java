package cg.gui;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JTable;

public class RenderIntWithComma implements TableCellInfoGetter
{
	protected DecimalFormat _format = new DecimalFormat(
	"###,###,###,##0");
	
	Integer _column = null;

   RenderIntWithComma()
   {
   }

   RenderIntWithComma(Integer aColumn)
   {
	   _column = aColumn;
   }

   @Override
	public TableCellInfo getInfo(JLabel label,JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
	{
	   TableCellInfo tInfo = null;

	   /*
	    * If _column is null, then apply format to all Integer columns.
	    * If _column is not null, then apply format to the specified column.
	    */
	   if ((_column == null && value.getClass() == Integer.class)
	    || (_column != null && column == _column.intValue()))
	   {
	      tInfo = new TableCellInfo();
	      tInfo._text = _format.format(value);
	   }
	   
	   return tInfo;
	}

}
