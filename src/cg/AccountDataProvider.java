package cg;

import java.util.TreeSet;
import java.util.Vector;

public interface AccountDataProvider
{
	/**
	 * Get lots held for the specified ticker and year. A value of
	 * 'null' for 'ticker' gets held lots for all tickers. A value of 'null' for
	 * 'year' gets held lots for all years.
	 */
	public Vector<LotDataProvider> getHeldLots(String ticker, String year);

	public TradeList getTradeList();
	public TreeSet<Integer> getYears(Boolean includeIdleYears);
	public Vector getSharesHeldStats(); //TODO change raw vector?
	public Vector getTaxGains(String ticker, String year); //TODO raw vec?
	public Vector getTaxGainLots(String ticker, String year); //TODO raw vec?
	public Vector getSecurityGains(); //TODO raw vec?
	public Vector getYearlyGains(); //TODO raw vec?
}
