package cg;

import java.util.Collections;
import java.util.Vector;
import java.util.TreeSet;

public class TradeList extends Vector<Trade> {

	public TradeList() {
	}

	public TradeList(TradeList tl) {
		addTrades(tl);
	}

	public TradeList(Vector<Trade> trades) {
		addTrades(trades);
	}

	public void addTrades(Vector<Trade> trades){
		for (int i = 0; i < trades.size(); i++)
			add(trades.elementAt(i));
		sortByDate();
	}

	public void addTrades(TradeList tl){
		for (int i = 0; i < tl.size(); i++)
			add(tl.elementAt(i));
		sortByDate();
	}

	public void addTrade(Trade t){
		add(t);
		sortByDate();
	}
	
	public TreeSet<String> getTickers() {
		TreeSet<String> tickers = new TreeSet<String>();
		for( int i = 0; i < size(); i++ ){
			Trade t = (Trade)elementAt(i);
			tickers.add(t.ticker);
		}
		return tickers;
	}
	
	public TreeSet<Integer> getYears(Boolean includeIdleYears) {
		TreeSet<Integer> years = new TreeSet<Integer>();
		for( int i = 0; i < size(); i++ ){
			Trade t = (Trade)elementAt(i);
			years.add(t.date.getYear());
		}
		if( years.size() > 0 && includeIdleYears == true ){
			Integer min_year = Collections.min(years);
			Integer max_year = Collections.max(years); 
			for( int i = min_year+1; i < max_year; i++ ){
				years.add(new Integer(i));
			}
		}
		return years;
	}

	/**
	 * Get the number of shares still held (i.e. not yet sold)
	 */
	public long getNumSharesHeld() {
		long numHeld = 0; // total number of shares still held
		for (int i = 0; i < size(); i++) {
			// If this is a buy trade, add number shares still held to the
			// total.
			if (elementAt(i).isBuyTrade()) {
				BuyTrade buyTrade = (BuyTrade) elementAt(i);
				numHeld += buyTrade.numSharesHeld.longValue();
			}
		}
		return numHeld;
	}

	/**
	 * Get the tax gains for the specified year. Use "null" for year to get tax
	 * gains for all years.
	 */
	public Vector<TaxGain> getGains(String year) {

		Vector<TaxGain> taxGains = new Vector<TaxGain>();

		for (int i = 0; i < size(); i++) {
			if (((Trade) elementAt(i)).tradeType != Trade.Type.SELL) {
				continue;
			}
			SellTrade sellTrade = (SellTrade) elementAt(i);
			TaxGain gain = sellTrade.taxGain;
			SimpleDate date = sellTrade.date;
			// System.out.println("year: " + date.getYearString());
			if (year != null && (year.equals(date.getYearString())) == false) {
				continue;
			}
			taxGains.addElement(gain);
		}
		return taxGains;
	}

	/**
	 * Get the tax gain lots for the specified year. Use "null" for year to get tax
	 * gain lots for all years.
	 */
	public Vector<Lot> getGainLots(String year) {

		Vector<Lot> taxGainLots = new Vector<Lot>();

		for (int i = 0; i < size(); i++) {
			if (((Trade) elementAt(i)).tradeType != Trade.Type.SELL) {
				continue;
			}
			SellTrade sellTrade = (SellTrade) elementAt(i);
			TaxGain gain = sellTrade.taxGain;
			SimpleDate date = sellTrade.date;
			// System.out.println("year: " + date.getYearString());
			if (year != null && (year.equals(date.getYearString())) == false) {
				continue;
			}
			Vector<Lot> lots = gain.gainComps;
			for( int j = 0; j < lots.size(); j++ )
				taxGainLots.addElement(lots.elementAt(j));
		}
		return taxGainLots;
	}

	/**
	 * Get the held lots for the specified year. Use "null" for year to get held
	 * lots for all years.
	 */
	public Vector<Lot> getHeldLots(String year) {

		Vector<Lot> heldLots = new Vector<Lot>();

		for (int i = 0; i < size(); i++) {
			if (((Trade) elementAt(i)).tradeType != Trade.Type.BUY) {
				continue;
			}
			BuyTrade buyTrade = (BuyTrade) elementAt(i);
			SimpleDate date = buyTrade.date;
			// System.out.println("year: " + date.getYearString());
			if (year != null && (year.equals(date.getYearString())) == false) {
				continue;
			}
			Vector<Lot> lots = buyTrade.lotSet.lots;
			for( int j = 0; j < lots.size(); j++ )
				heldLots.addElement(lots.elementAt(j));
		}
		return heldLots;
	}

	/**
	 * Get the tax gains for the specified year. Use "null" for year to get tax
	 * gains for all years.
	 */
	public float getTotalGain() {

		float ttlGain = 0;
		
		for (int i = 0; i < size(); i++) {
			if (((Trade) elementAt(i)).tradeType != Trade.Type.SELL) {
				continue;
			}
			SellTrade sellTrade = (SellTrade) elementAt(i);
			ttlGain += sellTrade.taxGain.gain;
		}
		return ttlGain;
	}

	public void sortByDate() {
		Collections.sort(this);
	}

	/**
	 * Prints out the list of stockMgrIds.
	 */
	void z_printBuySellIds(String header) {

		System.out.println(header);
		for (int i = 0; i < size(); i++) {
			Trade t = elementAt(i);
			System.out.println(t.buySellId);
		}
	}

	/**
	 * Prints out the list of stockMgrIds.
	 */
	void z_printTradeLots(String header) {

		System.out.println(header);
		for (int i = 0; i < size(); i++) {
			Trade t = elementAt(i);
			t.z_print();
			if (t.history.length() > 0)
				System.out.println(t.history);
			t.lotSet.z_print(null);
		}
	}

	/**
	 * Debug routine for getting a Vector of strings, one per trade.
	 */
	protected Vector<String> d_getTradeStrings() {
		Vector v = new Vector<String>();
		for (int i = 0; i < size(); i++) {
			v.addElement(elementAt(i).toString());
		}
		return v;
	}

	/**
	 * Debug routine for getting a Vector of strings, one per gain.
	 */
	protected Vector<String> d_getGainStrings() {
		Vector v = new Vector<String>();
		for (int i = 0; i < size(); i++) {
			if ( elementAt(i).isSellTrade() ) {
				v.addElement(((SellTrade) elementAt(i)).taxGain.toString());
			}
		}
		return v;
	}

}
