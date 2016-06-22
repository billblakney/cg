package cg.gui;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JTable;

public class RenderInteger implements RenderTableCellInfo
{
   /** Comma separated format. */
	static public final String COMMA_FORMAT = "###,###,###,##0";
   
	/** Format to be used. */
	protected DecimalFormat _format;
	
	/** Column to be formatted. */
	protected Integer _column = null;

	/**
	 * Constructor to format all columns.
	 * 
	 * @param aBigDecimalFormat
	 */
   RenderInteger(String aBigDecimalFormat)
   {
      _format = new DecimalFormat(aBigDecimalFormat);
   }

   /**
    * Constructor to format a specified column.
    * 
    * @param aBigDecimalFormat
    * @param aColumn
    */
   RenderInteger(String aBigDecimalFormat,Integer aColumn)
   {
      _format = new DecimalFormat(aBigDecimalFormat);
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
