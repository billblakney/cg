package cg;

import java.util.TreeSet;
import java.util.Vector;
import cg.db.ConnectionManager;

public class AccountDataProxy extends AbstractAccountData
{
   private int _accountId;
   
   private DataStore _ds = null;
   
   AccountDataProxy(DataStore aDataStore, int aAccountId)
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
	public Vector<LotDataProvider>
	getHeldLots(String ticker, String year)
	{
	   return _ds.getHeldLots(_accountId,ticker,year);
	}

   @Override
   public TradeList getTradeList()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public TreeSet<Integer> getYears(Boolean includeIdleYears)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Vector getSharesHeldStats()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Vector getTaxGains(String ticker, String year)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Vector getTaxGainLots(String ticker, String year)
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
