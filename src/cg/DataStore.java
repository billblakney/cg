package cg;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Observable;
import java.util.TreeSet;
import java.util.Vector;
import cg.db.AccountInfo;
import cg.db.ConnectionManager;
import cg.db.DatabaseInterface;
import cg.db.Lot;

/*
 * Singleton class that manages the storage of data for this app.
 * To use the database, a call must be made to setDbUrl.
 */
public class DataStore
{
   static DataStore _dataStore = null;
   
   ConnectionManager _cm = null;
   
   DatabaseInterface _dbi = null;
   
   Vector<Trade> _trades = new Vector<Trade>();

   Vector<Lot> _lots = new Vector<Lot>();

   /**
    * Print accounts.
    * TODO move, header?, etc
    */
   public static void printAccountInfoVector(Vector<AccountInfo> aAccounts)
   {
	   for (AccountInfo tInfo: aAccounts)
	   {
		   System.out.println(tInfo.toString());
	   }
   }
   
   private DataStore()
   {
   }
   
   static public DataStore getInstance()
   {
      if (_dataStore == null)
      {
         _dataStore = new DataStore();
      }
      return _dataStore;
   }
   
   public void setDbUrl(String aDbUrl)
   {
      _cm = ConnectionManager.getInstance();
      _cm.setDbUrl(aDbUrl);
      
      _dbi = DatabaseInterface.getInstance();
   }
   
   public AbstractAccountData getAccountDataProvider(int aAccountId)
   {
      return new AccountDataProxy(this,aAccountId);
   }
   
   public String getAccountName(int aAccountId)
   {
      String tName = null;

      if (_cm != null)
      {
         Connection tConn = _cm.getConnection();
         if (tConn != null)
         {
            tName = _dbi.getAccountName(tConn,aAccountId);
            _cm.closeConnection(tConn);
         }
      }
      else
      {
         //TODO
      }

      return tName;
   }

   public TradeList getTradeList(int aAccountId)
   {
      TradeList tTradeList = null;

      if (_cm != null)
      {
         Connection tConn = _cm.getConnection();
         if (tConn != null)
         {
            tTradeList = _dbi.getTradeList(tConn,aAccountId);
            _cm.closeConnection(tConn);
         }
      }
      else
      {
         //TODO
      }

      return tTradeList;
   }

   public Vector<TradeDataProvider> getTrades(int aAccountId)
   {
      Vector<TradeDataProvider> tTrades = null;

      if (_cm != null)
      {
         Connection tConn = _cm.getConnection();
         if (tConn != null)
         {
            tTrades = _dbi.getTrades(tConn,aAccountId);
            _cm.closeConnection(tConn);
         }
      }
      else
      {
         //TODO
      }

      return tTrades;
   }
   
//   @Override
//	public TradeList getTradeList()
//   {
//	   return null;
//   }
//
//	public TreeSet<Integer> getYears(Boolean includeIdleYears)
//   {
//	   return null;
//   }
//
//	public Vector getSharesHeldStats()
//   {
//	   return null;
//   }
//
//	public Vector getTaxGains(String ticker, String year)
//   {
//	   return null;
//   }
//
//	public Vector getTaxGainLots(String ticker, String year)
//   {
//	   return null;
//   }
//
//	public Vector getSecurityGains()
//   {
//	   return null;
//   }
//
//	public Vector getYearlyGains()
//   {
//	   return null;
//   }


   public Vector<AccountInfo> getAccountInfoVector()
   {
      Vector<AccountInfo> tAcctInfo = null;

      if (_cm != null)
      {
         Connection tConn = _cm.getConnection();
         if (tConn != null)
         {
            tAcctInfo = _dbi.getAccountInfoVector(tConn);
            _cm.closeConnection(tConn);
         }
      }
      else
      {
         //TODO
      }

      return tAcctInfo;
   }

   //TODO don't really want this public, its only for AccountDataProxy use
	protected Vector<LotDataProvider>
	getHeldLots(int aAccountId,String ticker, String year)
	{
		Vector<LotDataProvider> tHeldLots = new Vector<LotDataProvider>();

      if (_cm != null)
      {
         Connection tConn = _cm.getConnection();
         if (tConn != null)
         {
            tHeldLots = _dbi.getLotData(tConn, aAccountId);
            _cm.closeConnection(tConn);
         }
      }
      else
      {
         //TODO
      }

      return tHeldLots;
//		Iterator<Map.Entry<String,SecurityTradeList>> i = mgrs.entrySet().iterator();
//		while( i.hasNext() ){
//			Map.Entry<String,SecurityTradeList> e = i.next();
//			SecurityTradeList mgr = e.getValue();
//			if (ticker != null && (ticker.equals(mgr.ticker)) == false) {
//				continue;
//			}
//			heldLots.addAll(mgr.getHeldLots(year));
//		}
//		return heldLots;
	}
   
   public void addTrades(
         int aAccountId,TradeList aTradeList,boolean aProcessThem)
   {
      Connection tConn = null;

      if (_cm != null)
      {
         tConn = _cm.getConnection();
      }
      
      if (tConn != null)
      {
         _dbi.insertTrades(tConn,aAccountId,aTradeList);
      }

      for (Trade tTrade: aTradeList)
      {
         _trades.add(tTrade);

         if (tTrade.isBuyTrade())
         {
            processBuyTrade(tConn,(BuyTrade)tTrade);
         }
         else
         {
            processSellTrade(tConn,(SellTrade)tTrade);
         }
      }

      if (_cm != null)
      {
         _cm.closeConnection(tConn);
      }
   }
   
   /*
    * TODO is only for ntx accts now
    */
   private void processBuyTrade(Connection aConn,BuyTrade aTrade)
   {
      /*
       * Create a new buy lot.
       */
      Lot tLot = new Lot();
      tLot._lotId = null;
      tLot._parentId = null;
      tLot._hasChildren = false;
      tLot._triggerTradeId = aTrade.tradeId;
      tLot._buyTradeId = aTrade.tradeId;
      tLot._sellTradeId = null;
      tLot._numShares = aTrade.numShares;
		float tBasis = ((float)aTrade.numShares) * aTrade.sharePrice + aTrade.comm.floatValue(); //TODO use method?
      tLot._basis = new BigDecimal(tBasis); //TODO
      tLot._proceeds = new BigDecimal(0.0);
      tLot._state = Lot.State.eOpen;
      tLot._hasChildren = false;

      /*
       * Save the lot to the db.
       */
      if (aConn != null)
      {
         _dbi.insertLot(aConn,tLot);
      }

      /*
       * Save the lot to the cache.
       */
      _lots.add(tLot);
   }
   
   /*
    * TODO is only for ntx accts now
    */
   private void processSellTrade(Connection aConn,SellTrade aTrade)
   {
      int tNumToDistribute = aTrade.numShares;

      Vector<Lot> tLots = getActiveOpenLots(aTrade.ticker,aTrade.numShares);
      Iterator<Lot> tIterator = tLots.iterator();

      Vector<Lot> tNewLots = new Vector<Lot>();
      
      while (tIterator.hasNext() && tNumToDistribute > 0)
      {
         Lot tLot = tIterator.next();
         /*
          * This is the case where the lot is bigger than the number of
          * shares left to be processed. This will produce two new lots:
          * an open one that has the remaining shares available to sell,
          * and a closed one representing the shares sold.
          */
         if (tNumToDistribute < tLot._numShares)
         {
            // two new lots and update old.hasChildren
            
            Lot tNewClosedLot = new Lot();
            {
            tNewClosedLot._parentId = tLot._lotId;
            tNewClosedLot._hasChildren = false;
            tNewClosedLot._triggerTradeId = aTrade.tradeId;
            tNewClosedLot._buyTradeId = tLot._buyTradeId;
            tNewClosedLot._sellTradeId = aTrade.tradeId;
            tNewClosedLot._numShares = tNumToDistribute;
            float tFactor = (float)tNewClosedLot._numShares/(float)tLot._numShares;
            tNewClosedLot._basis = new BigDecimal(tFactor*tLot._basis.floatValue());
            float tProceeds = //TODO !!! factor?
                  ((float)tNumToDistribute) * aTrade.sharePrice.floatValue()
                     - aTrade.comm.floatValue();
            tNewClosedLot._proceeds = new BigDecimal(tProceeds);
            tNewClosedLot._state = Lot.State.eClosed;
            }

            tNewLots.add(tNewClosedLot);

            Lot tNewOpenLot = new Lot();
            {
            tNewOpenLot._parentId = tLot._lotId;
            tNewOpenLot._hasChildren = false;
            tNewOpenLot._triggerTradeId = aTrade.tradeId;
            tNewOpenLot._buyTradeId = tLot._buyTradeId;
            tNewOpenLot._sellTradeId = null;
            tNewOpenLot._numShares = tLot._numShares - tNumToDistribute;
            float tFactor = (float)tNewOpenLot._numShares/(float)tLot._numShares;
            tNewOpenLot._basis = new BigDecimal(tFactor*tLot._basis.floatValue());
            tNewOpenLot._proceeds = new BigDecimal(0.0);
            tNewOpenLot._state = Lot.State.eOpen;
            }

            tNewLots.add(tNewOpenLot);

            tNumToDistribute = 0;
         }
         /*
          * If the number left to distribute is greater or equal to the number
          * of lot shares, then the entire lot is consumed. This will result
          * in just one new sold lot.
          */
         else
         {
            // new lot and update old.hasChildren

            Lot tNewClosedLot = new Lot();
            {
            tNewClosedLot._parentId = tLot._lotId;
            tNewClosedLot._hasChildren = false;
            tNewClosedLot._triggerTradeId = aTrade.tradeId;
            tNewClosedLot._buyTradeId = tLot._buyTradeId;
            tNewClosedLot._sellTradeId = aTrade.tradeId;
            tNewClosedLot._numShares = tLot._numShares;
            tNewClosedLot._basis = new BigDecimal(tLot._basis.floatValue());//TOOD check
            float tProceeds = //TODO !!! factor?
                  ((float)tNumToDistribute) * aTrade.sharePrice.floatValue()
                     - aTrade.comm.floatValue();
            tNewClosedLot._proceeds = new BigDecimal(tProceeds);
            tNewClosedLot._state = Lot.State.eClosed;
            }

            tNewLots.add(tNewClosedLot);

            tNumToDistribute -= tLot._numShares;
         }

         tLot._hasChildren = true;

         if (aConn != null)
         {
            _dbi.updateLotHasChildren(aConn,tLot);
         }
      }
      
      for (Lot tLot: tNewLots)
      {
         if (aConn != null)
         {
            _dbi.insertLot(aConn,tLot);
         }
         
         _lots.add(tLot);
      }
   }

   protected Vector<Lot> getActiveOpenLots(String aTicker,int aQuantity)
   {
      int tQuantityFound = 0;
      
      Vector<Lot> tLots = new Vector<Lot>();
      
      for (Lot tLot: _lots)
      {
         if (tLot._hasChildren || tLot._state != Lot.State.eOpen){
            continue;
         }

         Trade tTrade = getTradeById(tLot._buyTradeId);
         if (tTrade == null){
            System.out.println("ERROR: Need more open lots to sell!");
            System.exit(0);
         }

         if (!tTrade.ticker.equals(aTicker)) {
            continue;
         }
         
         tLots.add(tLot);

         tQuantityFound += tLot._numShares;
         if (tQuantityFound >= aQuantity) {
            break;
         }
      }
      return tLots;
   }
   
   protected Trade getTradeById(int aTradeId)
   {
      for (Trade tTrade: _trades)
      {
         if (tTrade.tradeId == aTradeId)
         {
            return tTrade;
         }
      }
      return null;
   }
}
