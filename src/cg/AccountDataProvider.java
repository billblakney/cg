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
   public String getName();
	public Vector<TradeDataProvider> getTrades();
	public Vector<LotDataProvider> getOpenPositions(String ticker, String year);
	public Vector<GainProvider> getGains(String ticker, String year);

	public TradeList getTradeList();
	public Vector getSharesHeldStats(); //TODO change raw vector?
//	public Vector getGains(String ticker, String year); //TODO raw vec?
	public Vector getSecurityGains(); //TODO raw vec?
	public Vector getYearlyGains(); //TODO raw vec?
}
