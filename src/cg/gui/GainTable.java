package cg.gui;

import java.awt.Color;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import bbj.swing.table.render.CustomTableCellRenderer;
import bbj.swing.table.render.RenderColorsRule;
import bbj.swing.table.render.RenderIntegerRule;
import bbj.swing.table.render.RenderRule;
import bbj.swing.table.render.TableCellColorSet;
import bbj.swing.table.render.TableCellColorIndexChooser;

@SuppressWarnings("serial")
public class GainTable extends JTable {
	
   final static public TableCellColorSet kGainColors = new TableCellColorSet(
         new Color (175, 255, 175),
         new Color (175, 225, 175),
         Color.white,
         Color.black,
         Color.black,
         Color.black);
	
   final static public TableCellColorSet kLossColors = new TableCellColorSet(
         new Color (255, 204, 204),
         new Color (225, 150, 150),
         Color.white,
         Color.black,
         Color.black,
         Color.black);

	private GainTableModel model;
	private TableRowSorter<GainTableModel> sorter = null;

	private RowFilter<AbstractTableModel,Integer> _tickerFilter = null;
	private RowFilter<AbstractTableModel,Integer> _yearFilter = null;

	private RowFilter<AbstractTableModel, Integer> rowFilter;

	private TotalGain totalGain;

	public GainTable() {
		model = new GainTableModel();
		setModel(model);

		sorter = new TableRowSorter<GainTableModel>(model);
		setRowSorter(sorter);

		totalGain = new TotalGain();

		setRenderers();
	}

	private void setRenderers()
	{
		Vector<RenderRule> tInfos = new Vector<RenderRule>();
		
		/*
		 * Set renders for comma separated integer columns.
		 */
		RenderIntegerRule tIntRenderShares = new RenderIntegerRule(
		      RenderIntegerRule.COMMA_FORMAT,GainTableModel.COL_SHARES);
		tInfos.add(tIntRenderShares);
		
		RenderIntegerRule tIntRenderGross = new RenderIntegerRule(
		      RenderIntegerRule.COMMA_FORMAT,GainTableModel.COL_GROSS);
		tInfos.add(tIntRenderGross);
		
		RenderIntegerRule tIntRenderBasis = new RenderIntegerRule(
		      RenderIntegerRule.COMMA_FORMAT,GainTableModel.COL_BASIS);
		tInfos.add(tIntRenderBasis);
		
		RenderIntegerRule tIntRenderGain = new RenderIntegerRule(
		      RenderIntegerRule.COMMA_FORMAT,GainTableModel.COL_GAIN);
		tInfos.add(tIntRenderGain);

		/*
		 * Set the render for row coloring.
		 */
		Vector<TableCellColorSet> tColors = new Vector<TableCellColorSet>();
		tColors.add(kGainColors);
		tColors.add(kLossColors);
		
		TableCellColorIndexChooser tColorChooser =
		      (JLabel label,JTable table,Object value,boolean isSelected,
		            boolean hasFocus, int row,int column) -> 
		{
		   TableModel tModel = table.getModel();
		   int tModelRow = sorter.convertRowIndexToModel(row);

		   Integer gain = (Integer)tModel.getValueAt(
		         tModelRow,GainTableModel.COL_GAIN);

		   return ((gain > 0) ? 0:1);
		};

		RenderColorsRule tRowRender = new RenderColorsRule(
		     tColors,tColorChooser);
		tInfos.add(tRowRender);

		/*
		 * Create the custom table cell renderer and apply it to all columns.
		 */
		CustomTableCellRenderer tRenderer = new CustomTableCellRenderer(tInfos);

		for (int i = 0; i < getColumnCount(); i++)
		{
		    getColumnModel().getColumn(i).setCellRenderer(tRenderer);
		}
	}

	public void setRows(Vector gains) {

		model.setData(gains);
		updateTotalGain();
	}

	public void filterOnTicker(RowFilter<AbstractTableModel,Integer> aFilter)
	{
	   _tickerFilter = aFilter;
		setRowFilter();
	}

	public void filterOnYear(RowFilter<AbstractTableModel,Integer> aFilter)
	{
	   _yearFilter = aFilter;
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

		if (_yearFilter != null)
		{
		   filters.add(_yearFilter);
		}

		rowFilter = RowFilter.andFilter(filters);
		sorter.setRowFilter(rowFilter);
		model.fireTableDataChanged();

		updateTotalGain();
	}

	private void updateTotalGain() {
		Integer g = model.getTotalGain(rowFilter);
		totalGain.setValue(g);
	}

	public GainTable.TotalGain getTotalGain() {
		return totalGain;
	}

	// The observers
	public class TotalGain extends Observable {

		private Integer value;

		TotalGain() {
			setValue(new Integer(0));
		}

		Integer getValue() {
			return value;
		}

		private void setValue(Integer value) {
			this.value = value;
			setChanged();
			notifyObservers(getValue());
			// clearChanged() automatically called by notifyObservers()
		}
	}
}
