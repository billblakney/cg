package cg.gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * TODO update xjava version to match this
 */
@SuppressWarnings("serial")
public class CustomTableCellRenderer extends JLabel
                                     implements TableCellRenderer {

//TODO these can be removed; they're here as a note, but may not be useful
//   Color _defaultForegroundColor = null;
//   Color _defaultBackgroundColor = null;
//		_defaultBackgroundColor = UIManager.getColor ( "Panel.background" );
//		_defaultForegroundColor = UIManager.getColor ( "Panel.foreground" );
   
   
   Vector<TableCellInfoGetter> _fontSetters = new Vector<TableCellInfoGetter>();
	
	public CustomTableCellRenderer(TableCellInfoGetter aCellInfoGetter) {
		super();
		init();
		_fontSetters.add(aCellInfoGetter);
	}
	
	public CustomTableCellRenderer(Vector<TableCellInfoGetter> aCellInfoGetters) {
		super();
		init();
		_fontSetters = aCellInfoGetters;
	}

	private void init(){
		setOpaque(true);
		setFont(getFont().deriveFont(Font.PLAIN));
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	
	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column)
	{
	   String tText = null;
	   Font tFont = null;
	   Color tBackground = null;
	   Color tForeground = null;

	   for (TableCellInfoGetter tSetter: _fontSetters)
	   {
	      TableCellInfo tInfo = (TableCellInfo)tSetter.getInfo(this, table, value, isSelected, hasFocus, row, column);
	      if (tInfo != null)
	      {
	         if (tInfo._text != null)
	         {
	            tText = tInfo._text;
	         }
	         if (tInfo._font != null)
	         {
	            tFont = tInfo._font;
	         }
	         if (tInfo._bgColor != null)
	         {
	            tBackground = tInfo._bgColor;
	         }
	         if (tInfo._fgColor != null)
	         {
	            tForeground = tInfo._fgColor;
	         }
	      }
	   }
	   
	   if (tText != null)
	   {
	      setText(tText);
	   }
	   else
	   {
	      setText(value.toString());
	   }

	   if (tFont != null)
	   {
	      setFont(tFont);
	   }
	   else
	   {
	      setFont(null);
	   }
	   
	   if (tBackground != null)
	   {
	      setBackground(tBackground);
	   }
	   else
	   {
	      if (isSelected)
	      {
	         setBackground(table.getSelectionBackground());
	      }
	      else
	      {
	         setBackground(table.getBackground());
	      }
	   }
	   
	   if (tForeground != null)
	   {
	      setForeground(tForeground);
	   }
	   else
	   {
	      if (isSelected)
	      {
	         setForeground(table.getSelectionForeground());
	      }
	      else
	      {
	         setForeground(table.getForeground());
	      }
	   }

		return this;
	}
}
