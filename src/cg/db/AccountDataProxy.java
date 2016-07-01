package cg.db;

import java.util.TreeSet;
import java.util.Vector;
import cg.AbstractAccountData;
import cg.DataStore;
import cg.GainAccessor;
import cg.Lot;
import cg.OpenPositionAccessor;
import cg.Trade;
import cg.TradeDataProvider;
import cg.TradeList;

public class AccountDataProxy extends AbstractAccountData
{
   private DataStore _ds = null;
   
   private int _accountId;
   
   private String _accountName = null;

	private Vector<OpenPositionAccessor> _lotData = null;
   
   public AccountDataProxy(DataStore aDataStore, int aAccountId)
   {
      _accountId = aAccountId;
      _ds = aDataStore;
   }

   /**
    * Get the account name.
    */
	@Override
	public String getName()
	{
	   if (_accountName == null)
	   {
	      _accountName = _ds.getAccountName(_accountId);
	   }
	   return _accountName;
	}

	/**
	 * Get the trades of this account.
	 */
	public Vector<Trade> getTrades()
	{
      return _ds.getTrades(_accountId);
	}

	/**
	 * Get the open positions for this account.
	 * 
	 * TODO may want to save ticker and year along with _lotData, and only
	 * fetch when the ticker/year is in the request is different from the
	 * last one. or may want to do the filtering here?
	 */
	@Override
	public Vector<Lot>
	getOpenPositions(String ticker, String year)
	{
//	   return _ds.getOpenPositions(_accountId,ticker,year);
	   return _ds.getOpenPositions_new(_accountId,ticker,year);
	}

	/**
	 * Get the capital gain components for this account.
	 */
   @Override
   public Vector<GainAccessor> getGains(String ticker, String year)
   {
	   return _ds.getGains(_accountId,ticker,year);
   }

   @Override
   public TradeList getTradeList()
   {
      return _ds.getTradeList(_accountId);
   }

   @Override
   public Vector getSharesHeldStats()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Vector getSecurityGains()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Vector getYearlyGains()
   {
      // TODO Auto-generated method stub
      return null;
   }

}
