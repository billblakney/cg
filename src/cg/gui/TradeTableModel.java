package cg.gui;

import java.util.Vector;

import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;

import cg.SellTrade;
import cg.TaxGain;
import cg.TradeDataProvider;

/*
 * Note: These three methods must be implemented to extend AbstractTableModel
 *   public int getRowCount();
 *   public int getColumnCount();
 *   public Object getValueAt(int row, int column);
 */
public class TradeTableModel extends AbstractTableModel {

	private String[] columnNames = { "ID", "Date", "Buy/Sell", "Ticker",
			"Shares", "Shares Held", "Shares Sold", "Share Price", "Commission", "Tax Year",
			"Note" };

	final static int COL_ID = 0;
	final static int COL_DATE = 1;
	final static int COL_BUYSELL = 2;
	final static int COL_TICKER = 3;
	final static int COL_SHARES = 4;
	final static int COL_SHARESHELD = 5;
	final static int COL_SHARESSOLD = 6;
	final static int COL_SHAREPRICE = 7;
	final static int COL_COMMISSION = 8;
	final static int COL_TAXYEAR = 9;
	final static int COL_NOTE = 10;

	private Vector data;
	
	private Vector<TradeDataProvider> trades;
	
	TradeTableModel() {
		data = new Vector(0);
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}
    
	public String getColumnName(int col) {
        return columnNames[col];
    }
    
	public Class getColumnClass(int c) {
		if( data.size() > 0 ){
			Object value = getValueAt(0,c);  
			return (value==null ? Object.class:value.getClass());
		}
		else
			return Object.class;
    }

	public Object getValueAt(int arg0, int arg1) {
		if( arg0 > data.size() ||  arg1 > columnNames.length )
			throw new ArrayIndexOutOfBoundsException();
		else
			return ((Vector)(data.elementAt(arg0))).elementAt(arg1);
	}

	public void setData(Vector<TradeDataProvider> trades){
		this.trades = trades;
		data = new Vector();
		for (int i = 0; i < trades.size(); i++)
			data.addElement(getDataRow(trades.elementAt(i)));
		fireTableDataChanged();
	}

	/**
	 * Get vector data for a gain.
	 */
	private Vector getDataRow(Object row) {
		TradeDataProvider trade = (TradeDataProvider) row;

		Vector v = new Vector();
		v.addElement(trade.getTradeId()); //0
		v.addElement(trade.getDate()); //1
		v.addElement(trade.getTradeType()); //2
		v.addElement(trade.getSymbol()); //3
		v.addElement(trade.getNumShares()); //4
		v.addElement(trade.getNumSharesHeld()); //5
		v.addElement(trade.getNumSharesSold()); //6
		v.addElement(trade.getSharePrice()); //7
		v.addElement(trade.getCommission()); //8
		v.addElement(trade.getClaimedTaxYear()); //9
		v.addElement(trade.getNote()); //10
		return v;
	}
	
	public Integer getSharesHeld(RowFilter<AbstractTableModel, Integer> filter) {
		Integer ttlShares = new Integer(0);
		for (int i = 0; i < data.size(); i++) {
			RowFilter.Entry<AbstractTableModel, Integer> entry = new TableModelFilterEntry(
					(AbstractTableModel)this, i);
			// pass over entry if filter.include() returns false
			if ((filter != null) && (filter.include(entry) == false))
				continue;
			Integer n = (Integer) entry.getValue(TradeTableModel.COL_SHARESHELD);
			ttlShares += n;
		}
		return ttlShares;
	}
}
