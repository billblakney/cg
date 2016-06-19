package cg;

import java.util.*;

/**
 * The Account class is used to track a set of stock trades, and to compute
 * the capital gains for those trades. The public interface provides methods for
 * adding trades, computing cap gains, and retrieving various reports on the set
 * of trades.
 * 
 * </p>Internally, this class maintains a list of StockMgr objects. Each
 * StockMgr maintains all of the trades for a particular stock (e.g. CSCO).
 */

public class Account extends AbstractAccountData implements AccountDataProvider {

	public final String DEFAULT_ACCT_NAME = "Default Account";
	String name;
	
	TradeList allTrades = new TradeList();

	// values for notify
	public final static int UPDATE_TABLES = 0x01;
	public final static int UPDATE_TICKER_LIST = 0x02;
	public final static int UPDATE_YEAR_LIST = 0x04;

	private long tradeCount = 0; // used to set trade.pID
	
	/**
	 * HashMap of the StockMgr objects. It is populated by adding trades to
	 * this account.
	 */
	HashMap<String,SecurityTradeList> mgrs = new HashMap<String,SecurityTradeList>();

	/**
	 * Default constructor, which creates an empty account with default name.
	 */
	public Account(String aName) {
		name = aName;
	}

	/**
	 * Constructor that creates a account and initializes it with a set of
	 * trades and default name.
	 */
	public Account(String aName,TradeList trades) {
		name = aName;
		addTrades(trades);
	}
	
	/**
	 * Get the account name.
	 * @return
	 */
	public String getName()
	{
	   return name;
	}

	/**
	 * Add trades to this account, and recomputes the capital
	 * gains of the entire account, and finally notifies the account
	 * observers.
	 * 
	 * @param trades
	 *            List of trades to be added.
	 */
	public void addTrades(TradeList trades) {
		allTrades.addTrades(trades);
		allTrades.sortByDate();  // maybe not really needed; ...
		addTradesToStockMgrs(trades); // will add trades to existing trades
		computeCapGains(); // will recompute gains for all trades
		updateObservers(); // notify observers
	}

	/**
	 * Remove all trades from this account, and update related data
	 * structures in this account.
	 */
	public void removeAllTrades() {
		mgrs.clear();
		allTrades.clear();
		updateObservers(); // notify observers
	}

	/**
	 * Get the list of tickers for all stocks in this account.
	 */
	@Override
	public TreeSet<String> getTickers() {
		return allTrades.getTickers();
	}

	/**
	 * Get the list of years for all stocks in this account.
	 */
	@Override
	public TreeSet<Integer> getYears(Boolean includeIdleYears) {
		return allTrades.getYears(includeIdleYears);
	}

	/**
	 * Get a list of all trades.
	 */
	@Override
	public TradeList getTradeList() {
		return allTrades;
	}

	/**
	 * Get a list of all trades for a specific year.
	 */
	public TradeList getTradeList(int year) {
		TradeList trades = new TradeList();
		
		Iterator<Trade> i = allTrades.iterator();
		while( i.hasNext() ){
			Trade t = i.next();
			if( t.date.getYear() == year ){
				trades.add(t);
			}
		}
		return trades;
	}

	/**
	 * Get all capital tax gains for the specified ticker and year. A value of
	 * 'null' for 'ticker' gets gains for all tickers. A value of 'null' for
	 * 'year' gets gains for all years.
	 */
	@Override
	public Vector getSharesHeldStats() {
	
		Vector stats = new Vector();

		Iterator<Map.Entry<String,SecurityTradeList>> i = mgrs.entrySet().iterator();
		while( i.hasNext() ){
			Map.Entry<String,SecurityTradeList> e = i.next();
			SecurityTradeList mgr = e.getValue();
			stats.addAll(mgr.getSharesHeldStats());
		}
		return stats;
	}

	/**
	 * Get all capital tax gains for the specified ticker and year. A value of
	 * 'null' for 'ticker' gets gains for all tickers. A value of 'null' for
	 * 'year' gets gains for all years.
	 */
	@Override
	public Vector getTaxGains(String ticker, String year) {
	
		Vector gains = new Vector();

		Iterator<Map.Entry<String,SecurityTradeList>> i = mgrs.entrySet().iterator();
		while( i.hasNext() ){
			Map.Entry<String,SecurityTradeList> e = i.next();
			SecurityTradeList mgr = e.getValue();
			if (ticker != null && (ticker.equals(mgr.ticker)) == false) {
				continue;
			}
			gains.addAll(mgr.getGains(year));
		}
		return gains;
	}

	/**
	 * Get all capital tax gain lots for the specified ticker and year. A value of
	 * 'null' for 'ticker' gets gain lots for all tickers. A value of 'null' for
	 * 'year' gets gain lots for all years.
	 */
	@Override
	public Vector getTaxGainLots(String ticker, String year) {
	
		Vector gainLots = new Vector();

		Iterator<Map.Entry<String,SecurityTradeList>> i = mgrs.entrySet().iterator();
		while( i.hasNext() ){
			Map.Entry<String,SecurityTradeList> e = i.next();
			SecurityTradeList mgr = e.getValue();
			if (ticker != null && (ticker.equals(mgr.ticker)) == false) {
				continue;
			}
			gainLots.addAll(mgr.getGainLots(year));
		}
		return gainLots;
	}

	@Override
	public Vector<LotDataProvider>
	getHeldLots(String ticker, String year) {
	
		Vector<LotDataProvider> heldLots = new Vector<LotDataProvider>();

		Iterator<Map.Entry<String,SecurityTradeList>> i = mgrs.entrySet().iterator();
		while( i.hasNext() ){
			Map.Entry<String,SecurityTradeList> e = i.next();
			SecurityTradeList mgr = e.getValue();
			if (ticker != null && (ticker.equals(mgr.ticker)) == false) {
				continue;
			}
			heldLots.addAll(mgr.getHeldLots(year));
		}
		return heldLots;
	}

	/**
	 * Get a summary of capital tax gains for each year.
	 * Begin with the year that corresponds to the first trade.
	 */
	@Override
	public Vector getYearlyGains() {

		Vector gains = new Vector();
		
		TreeSet<Integer> years = allTrades.getYears(false);
		Iterator<Integer> i = years.iterator();
		while( i.hasNext()){
			Integer year = i.next();

			YearlyGain yg = new YearlyGain();
			yg.year = year.intValue();
			TradeList trade_list = getTradeList(year.intValue());
			yg.gain = trade_list.getTotalGain();
			gains.add(yg);
		}
		return gains;
	}

	/**
	 * Get a summary of capital tax gains for each security.
	 */
	@Override
	public Vector getSecurityGains() {
		
		Vector gains = new Vector();

		Iterator<Map.Entry<String,SecurityTradeList>> i = mgrs.entrySet().iterator();
		while( i.hasNext() ){
		    Map.Entry<String,SecurityTradeList> entry = i.next();
		    String year =  entry.getKey();
		    SecurityTradeList mgr = entry.getValue();
		    
		    SecurityGain sg = new SecurityGain();
		    sg.ticker = mgr.ticker;
		    sg.gain = mgr.getTotalGain();
		    gains.add(sg);
		}
		return gains;
	}

	/**
	 * Update observers that this account has changed.
	 */
	private void updateObservers() {
		setChanged();
		notifyObservers(new Integer(UPDATE_TABLES | UPDATE_TICKER_LIST
				| UPDATE_YEAR_LIST));
	}
	
	/**
	 * Load a set of trades from a TradeList. Basically, this routine just
	 * adds each trade to its corresponding StockMgr. (No cap gains computations
	 * are done.)
	 */
	private void addTradesToStockMgrs(TradeList trades) {
		for (int i = 0; i < trades.size(); i++) {
			// Get the trade's ticker.
			Trade trade = trades.elementAt(i);
			String ticker = trade.ticker;

			// If we already have a manager for this stock, add the trade to it.
			if (mgrs.containsKey(ticker)) {
				System.out.println("Adding trade to StockMgr " + ticker);
				SecurityTradeList stockMgr = (SecurityTradeList) mgrs.get(ticker);
					stockMgr.addTrade(trade);
			}
			// Else create a new StockMgr and add the trade to it.
			else {
				System.out.println("Adding trade to NEW StockMgr " + ticker);
				SecurityTradeList stockMgr = new SecurityTradeList(ticker);
					stockMgr.addTrade(trade);
				mgrs.put(ticker, stockMgr);
			}
		}
	}

	/**
	 * Compute cap gains for the entire account.
	 */
	private void computeCapGains() {
		Iterator<Map.Entry<String,SecurityTradeList>> i = mgrs.entrySet().iterator();
		while( i.hasNext() ){
		    Map.Entry<String,SecurityTradeList> entry = i.next();
		    String year =  entry.getKey();
		    SecurityTradeList mgr = entry.getValue();
			
			mgr.computeCapGains();
		}
	}
}
