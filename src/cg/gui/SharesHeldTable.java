package cg.gui;

import java.awt.Color;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import javax.swing.table.AbstractTableModel;
import javax.swing.RowFilter;
import javax.swing.RowFilter.*;
import cg.Term;
import cg.gui.render.TwoColorTableRenderer;

public class SharesHeldTable extends JTable {

	private SharesHeldTableModel model;
	private TableRowSorter<SharesHeldTableModel> sorter = null;

	private RowFilter<AbstractTableModel,Integer> _tickerFilter = null;
	private RowFilter<AbstractTableModel,Integer> _termFilter = null;

	RowFilter<AbstractTableModel, Integer> rowFilter;

	SharesHeld sharesHeld;

	public SharesHeldTable() {
		model = new SharesHeldTableModel();
		setModel(model);

		sorter = new TableRowSorter<SharesHeldTableModel>(model);
		setRowSorter(sorter);
		
		sharesHeld = new SharesHeld();
		
		Vector<RenderTableCellInfo> tInfos = new Vector<RenderTableCellInfo>();
		
		RenderInteger tIntRender = new RenderInteger(
		      RenderInteger.COMMA_FORMAT,SharesHeldTableModel.COL_SHARES);
		tInfos.add(tIntRender);

		TableTwoColorScheme tColorScheme = new TableTwoColorScheme();
		tColorScheme.bg_Normal[0]     = new Color(235, 237, 255);
		tColorScheme.bg_Normal[1]     = new Color(217, 251, 209);
		RenderTableCellTest tTest =
		      (JLabel label,JTable table,Object value,boolean isSelected,
		            boolean hasFocus, int row,int column) -> {return (0 == row%2);};
		RenderColoredRows tRowRender = new RenderColoredRows(tColorScheme,tTest);

		CustomTableCellRenderer tRenderer = new CustomTableCellRenderer(tInfos);
		tInfos.add(tRowRender);

		for (int i = 0; i < getColumnCount(); i++)
		{
		    getColumnModel().getColumn(i).setCellRenderer(tRenderer);
		}
	}

	public void setRows(Vector gains) {
		model.setData(gains);
		updateSharesHeld();
	}

	public void filterOnTicker(RowFilter<AbstractTableModel,Integer> aFilter)
	{
	   _tickerFilter = aFilter;
		setRowFilter();
	}

	public void filterOnTerm(RowFilter<AbstractTableModel,Integer> aFilter)
	{
	   _termFilter = aFilter;
		setRowFilter();
	}

	private void setRowFilter()
	{
		List<RowFilter<AbstractTableModel, Integer>> filters =
		      new Vector<RowFilter<AbstractTableModel, Integer>>();

		if (_tickerFilter != null)
		{
		   filters.add(_tickerFilter);
		}

		if (_termFilter != null)
		{
		   filters.add(_termFilter);
		}

		sorter.setRowFilter(null);
		
		rowFilter = RowFilter.andFilter(filters);
		sorter.setRowFilter(rowFilter);
		model.fireTableDataChanged();

		updateSharesHeld();
	}

	private void updateSharesHeld() {
		Integer shares = model.getSharesHeld(rowFilter);
		sharesHeld.setValue(shares);
	}

	public SharesHeld getSharesHeld() {
		return sharesHeld;
	}
}

