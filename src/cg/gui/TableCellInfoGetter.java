package cg.gui;
import javax.swing.JTable;
import javax.swing.JLabel;

public interface TableCellInfoGetter
{
	TableCellInfo getInfo(JLabel label,JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column);
}