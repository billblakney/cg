package CapGains.gui;

import java.util.Vector;

import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;

import CapGains.SellTrade;
import CapGains.TaxGain;
import CapGains.Trade;

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
	
	private Vector<Trade> trades;
	
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

	public void setData(Vector<Trade> trades){
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
		Trade trade = (Trade) row;

		Vector v = new Vector();
		v.addElement(trade.portID); //0
		v.addElement(trade.date); //1
		v.addElement(trade.tradeType); //2
		v.addElement(trade.ticker); //3
		v.addElement(trade.numShares); //4
		v.addElement(trade.numSharesHeld); //5
		v.addElement(trade.numSharesSold); //6
		v.addElement(trade.sharePrice); //7
		v.addElement(new Float(trade.comm.floatValue())); //8
		if (trade.isSellTrade()) {
			SellTrade sellTrade = (SellTrade) trade;
			v.addElement(sellTrade.getTaxGain().claimedTaxYear); //9
		} else {
			v.addElement(new Integer(0)); //9
		}
		if( trade.note == null )
			v.addElement(""); //10
		else
			v.addElement(trade.note); //10
		return v;
	}
	
	public Long getSharesHeld(RowFilter<AbstractTableModel, Integer> filter) {
		Long ttlShares = new Long(0);
		for (int i = 0; i < data.size(); i++) {
			RowFilter.Entry<AbstractTableModel, Integer> entry = new TableModelFilterEntry(
					(AbstractTableModel)this, i);
			// pass over entry if filter.include() returns false
			if ((filter != null) && (filter.include(entry) == false))
				continue;
			Long n = (Long) entry.getValue(TradeTableModel.COL_SHARESHELD);
			ttlShares += n;
		}
		return ttlShares;
	}
}
