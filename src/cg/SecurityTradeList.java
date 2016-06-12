package cg;

import java.util.Vector;
import java.util.Calendar;

/**
 * The StockMgr is used to record a set of trades for a particular stock, and to
 * compute cap gains for that set of trades.
 */
public class SecurityTradeList extends TradeList {
	
	/**
	 * Stores the ticker symbol for the stock being managed.
	 */
	public String ticker;

	/**
	 * Constructor.
	 */
	public SecurityTradeList(String ticker) {
		this.ticker = ticker;
	}
	
	public SecurityTradeList(String ticker, Vector<Trade> trades) {
		this.ticker = ticker;
		addTrades(trades);
	}

	public void addTrades(Vector<Trade> trades){
		for (int i = 0; i < trades.size(); i++){
			Trade t = trades.elementAt(i);
			if( (null != ticker) && (t.ticker.equals(ticker) == false) )
				continue;
			else
				add(t);
		}
		sortByDate();
	}

	public void addTrade(Trade t){
		if( t.ticker.equals(ticker) == false )
			return; // later, throw exception again, like used to
		add(t);
		sortByDate();
	}

	/**
	 * Compute cap gains for all SellTrades in this StockMgr.
	 */
	public void computeCapGains() {
		System.out.println("Computing gains for " + ticker + " ...");
		GainsCalculator gc = new GainsCalculator(this);
		gc.computeGains();
	}

	/**
	 * Compute cap gains for all SellTrades in this StockMgr.
	 */
	public Vector<SharesHeldStat> getSharesHeldStats() {
		Vector<SharesHeldStat> stats = new Vector<SharesHeldStat>();
		if( getNumSharesHeld() == 0 )
			return stats;
		
		SimpleDate year_ago_today = new SimpleDate();
		year_ago_today.add(Calendar.YEAR,-1);
		
		// Create short term, long term, and "combined" stats.
		// Note: initialize the lo and hi prices to work with algorithm used below 
		SharesHeldStat sStats = new SharesHeldStat(ticker,Term.SHORT,new Float(1000000.0),new Float(0.0));
		SharesHeldStat lStats = new SharesHeldStat(ticker,Term.LONG,new Float(1000000.0),new Float(0.0));
		
		for (int i = 0; i < size(); i++) {
			if( elementAt(i).isBuyTrade() ) {
				BuyTrade bt = (BuyTrade) elementAt(i);
				if( bt.numSharesHeld < 1 ) // only interested in buy trades shares currently held
					continue;
				
				if( bt.date.before(year_ago_today) )
					updateStats(lStats,bt);
				else
					updateStats(sStats,bt);
			}
		}

		if( sStats.num_shares > 0 ){
			sStats.av_share_price = sStats.total_cost/sStats.num_shares;
			stats.add(sStats);
		}
		if( lStats.num_shares > 0 ){
			lStats.av_share_price = lStats.total_cost/lStats.num_shares;
			stats.add(lStats);
		}

		return stats;
	}
	
	private void updateStats(SharesHeldStat stat, BuyTrade bt){
		stat.num_shares += bt.numSharesHeld;
		stat.total_cost += bt.sharePrice * bt.numSharesHeld;
		if( bt.sharePrice < stat.lo_share_price )
			stat.lo_share_price = bt.sharePrice;
		if( bt.sharePrice > stat.hi_share_price )
			stat.hi_share_price = bt.sharePrice;
	}

	/**
	 * Get the number of shares still held (i.e. not yet sold)
	 */
	@Override
	public long getNumSharesHeld() {
		long numHeld = 0; // total number of shares still held
		for (int i = 0; i < size(); i++) {
			// If this is a buy trade, add number shares still held to the
			// total.
			if( elementAt(i).isBuyTrade() ) {
				BuyTrade buyTrade = (BuyTrade) elementAt(i);
				numHeld += buyTrade.numSharesHeld.longValue();
			}
		}
		return numHeld;
	}

	Trade getFirstTradeWithLots(Trade.Type tradeType) {
		for (int i = 0; i < size(); i++) {
			Trade t = elementAt(i);
			if (t.tradeType != tradeType)
				continue;
			Lot lot = t.lotSet.getFirstLot();
			if (lot != null)
				return t;
		} // end for
		return null;
	}

	/**
	 * Reinitializes all gains for the sell trades in trades.
	 */
	void resetGains() {
		for (int i = 0; i < size(); i++) {
			Trade t = elementAt(i);
			if (t.isSellTrade()) {
				SellTrade st = (SellTrade) t;
				st.resetTaxGain();
			} // end if
		} // end for
	} // end method

	/**
	 * Remove all of the lots for a set of trades.
	 */
	void resetLots() {
		for (int i = 0; i < size(); i++) {
			Trade t = elementAt(i);
			t.lotSet.clearLots();
		}
	}

	/**
	 * Takes a set of sequential buy and sell trades and sets their buySellIds.
	 * The IDs are assigned using the format <ticker>-<n>-<Buy|Sell>, where the
	 * prefix indicates whether the trade is a buy or sell, and the suffix is an
	 * index that starts with a value of 1, and is incremented for each trade.
	 * NEOP-1-Buy
	 * NEOP-2-Buy
	 * NEOP-3-Buy
	 * NEOP-4-Sell
	 * NEOP-5-Sell
	 * NEOP-6-Buy 
	 */
	void resetBuySellIds() {
		/**
		 * Counter for constructing the trade id.
		 */
		final String BUY = "Buy";
		final String SELL = "Sell";
		int counter = 1;

		for (int i = 0; i < size(); i++) {

			// If its a buy trade, format is "<ticker>-<counter>-Buy".
			if (elementAt(i).isBuyTrade()) {
				BuyTrade bt = (BuyTrade) elementAt(i);
				bt.buySellId = new String(bt.ticker + "-" + (counter++) + "-"
						+ BUY);
			}

			// Else its a buy trade, with format "<ticker>-<counter>-Sell".
			else {
				SellTrade st = (SellTrade) elementAt(i);
				st.buySellId = new String(st.ticker + "-" + (counter++) + "-"
						+ SELL);
			}
		}
	}

	/**
	 * Resets the number of shares held for all trades. For buy trades, the
	 * number of shares held is set to the number of shares originally assigned
	 * to the trade. For sell trades, the number of shares held is set to zero.
	 * (Number of shares held for a sell trade represents the number of shares
	 * for which a tax gain is relevant (i.e. the number of non-washed shares).
	 */
	void resetNumSharesHeld() {

		for (int i = 0; i < size(); i++) {
			Trade t = elementAt(i);
			t.numSharesHeld = new Long(0);
			t.numSharesSold = new Long(0);
		}
	}
}
