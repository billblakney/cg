package cg.db;

import java.util.TreeSet;
import java.util.Vector;
import cg.AbstractAccountData;
import cg.DataStore;
import cg.GainProvider;
import cg.LotDataProvider;
import cg.TradeDataProvider;
import cg.TradeList;

public class AccountDataProxy extends AbstractAccountData
{
   private DataStore _ds = null;
   
   private int _accountId;
   
   private String _accountName = null;

	private Vector<LotDataProvider> _lotData = null;
   
   public AccountDataProxy(DataStore aDataStore, int aAccountId)
   {
      _accountId = aAccountId;
      _ds = aDataStore;
   }

//   @Override
//   public Vector<LotDataProvider> getHeldLots(String ticker, String year)
//   {
//      // TODO Auto-generated method stub
//      return null;
//   }

	@Override
	public String getName()
	{
	   if (_accountName == null)
	   {
	      _accountName = _ds.getAccountName(_accountId);
	   }
	   return _accountName;
	}

	public Vector<TradeDataProvider> getTrades()
	{
      return _ds.getTrades(_accountId);
	}

	/**
	 * TODO may want to save ticker and year along with _lotData, and only
	 * fetch when the ticker/year is in the request is different from the
	 * last one. or may want to do the filtering here?
	 */
	@Override
	public Vector<LotDataProvider>
	getHeldLots(String ticker, String year)
	{
	   return _ds.getHeldLots(_accountId,ticker,year);
	}

   @Override
   public Vector<GainProvider> getGains(String ticker, String year)
   {
      // TODO Auto-generated method stub
      return null;
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
