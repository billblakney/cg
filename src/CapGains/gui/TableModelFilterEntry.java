package CapGains.gui;

import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;


public class TableModelFilterEntry extends RowFilter.Entry<AbstractTableModel,Integer>{
	
	private AbstractTableModel model;
	private Integer row;
	
	public TableModelFilterEntry(AbstractTableModel model,int row){
		this.model = model;
		this.row = new Integer(row);
	}
	public AbstractTableModel getModel(){
		return model;
	}
	
	public int getValueCount(){
		return model.getColumnCount();
	}
	
	public Object getValue(int index){
		return model.getValueAt(row, index);
	}
	
	// public String getStringValue(int index) - use inherited implementation
	
	public Integer getIdentifier(){
		return row;
	}
}
