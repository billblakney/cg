package cg;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Observable;
import java.util.TreeSet;
import java.util.Vector;
import cg.db.AccountDataProxy;
import cg.db.AccountRecord;
import cg.db.ConnectionManager;
import cg.db.DatabaseInterface;
import cg.db.LotRecord;

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

   Vector<LotRecord> _lots = new Vector<LotRecord>();

   /**
    * Print accounts.
    * TODO move, header?, etc
    */
   public static void printAccountInfoVector(Vector<AccountRecord> aAccounts)
   {
	   for (AccountRecord tInfo: aAccounts)
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
//	public Vector getGains(String ticker, String year)
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


   public Vector<AccountRecord> getAccountInfoVector()
   {
      Vector<AccountRecord> tAcctInfo = null;

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
	public Vector<LotDataProvider>
	getOpenPositions(int aAccountId,String ticker, String year)
	{
		Vector<LotDataProvider> tOpenPositions = new Vector<LotDataProvider>();

      if (_cm != null)
      {
         Connection tConn = _cm.getConnection();
         if (tConn != null)
         {
            tOpenPositions = _dbi.getOpenPositions(tConn, aAccountId);
            _cm.closeConnection(tConn);
         }
      }
      else
      {
         //TODO
      }

      return tOpenPositions;
//		Iterator<Map.Entry<String,SecurityTradeList>> i = mgrs.entrySet().iterator();
//		while( i.hasNext() ){
//			Map.Entry<String,SecurityTradeList> e = i.next();
//			SecurityTradeList mgr = e.getValue();
//			if (ticker != null && (ticker.equals(mgr.ticker)) == false) {
//				continue;
//			}
//			heldLots.addAll(mgr.getOpenPositions(year));
//		}
//		return heldLots;
	}

   //TODO don't really want this public, its only for AccountDataProxy use
	public Vector<GainProvider>
	getGains(int aAccountId,String ticker, String year)
	{
		Vector<GainProvider> tGains = new Vector<GainProvider>();

      if (_cm != null)
      {
         Connection tConn = _cm.getConnection();
         if (tConn != null)
         {
            tGains = _dbi.getGains(tConn, aAccountId);
            _cm.closeConnection(tConn);
         }
      }
      else
      {
         //TODO
      }

      return tGains;
//		Iterator<Map.Entry<String,SecurityTradeList>> i = mgrs.entrySet().iterator();
//		while( i.hasNext() ){
//			Map.Entry<String,SecurityTradeList> e = i.next();
//			SecurityTradeList mgr = e.getValue();
//			if (ticker != null && (ticker.equals(mgr.ticker)) == false) {
//				continue;
//			}
//			heldLots.addAll(mgr.getOpenPositions(year));
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
      LotRecord tLot = new LotRecord();
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
      tLot._state = LotRecord.State.eOpen;
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

      Vector<LotRecord> tLots = getActiveOpenLots(aTrade.ticker,aTrade.numShares);
      Iterator<LotRecord> tIterator = tLots.iterator();

      Vector<LotRecord> tNewLots = new Vector<LotRecord>();
      
      while (tIterator.hasNext() && tNumToDistribute > 0)
      {
         LotRecord tLot = tIterator.next();
         /*
          * This is the case where the lot is bigger than the number of
          * shares left to be processed. This will produce two new lots:
          * an open one that has the remaining shares available to sell,
          * and a closed one representing the shares sold.
          */
         if (tNumToDistribute < tLot._numShares)
         {
            // two new lots and update old.hasChildren
            
            LotRecord tNewClosedLot = new LotRecord();
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
            tNewClosedLot._state = LotRecord.State.eClosed;
            }

            tNewLots.add(tNewClosedLot);

            LotRecord tNewOpenLot = new LotRecord();
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
            tNewOpenLot._state = LotRecord.State.eOpen;
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

            LotRecord tNewClosedLot = new LotRecord();
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
            tNewClosedLot._state = LotRecord.State.eClosed;
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
      
      for (LotRecord tLot: tNewLots)
      {
         if (aConn != null)
         {
            _dbi.insertLot(aConn,tLot);
         }
         
         _lots.add(tLot);
      }
   }

   /**
    * Get the list of active open lots for a symbol ordered by FIFO.
    * <p>
    * The FIFO ordering is determined by the sequnce number of the lot's
    * associted buy trade.
    * <p>
    * @param aSymbol The symbol of the lots being requested.
    * @param aQuantity The minimum number of shares to be represented by the
    *    returned lots.
    * @return The list of lots ordered by FIFO and representing at least the
    *    requested number of shares.
    * 
    * TODO get from db
    */
static int passes = 0;
   protected Vector<LotRecord> getActiveOpenLots(String aSymbol,int aQuantity)
   {
      int tQuantityFound = 0;
      
      Vector<LotRecord> tLots = new Vector<LotRecord>();
      
      _lots.sort((LotRecord lot1,LotRecord lot2)-> (lot1._buyTradeId - lot2._buyTradeId));

      for (LotRecord tLot: _lots)
      {
         if (tLot._hasChildren || tLot._state != LotRecord.State.eOpen){
            continue;
         }

         Trade tTrade = getTradeById(tLot._buyTradeId);
         if (tTrade == null){
            System.out.println("ERROR: Need more open lots to sell!");
            System.exit(0);
         }

         if (!tTrade.ticker.equals(aSymbol)) {
            continue;
         }
         
         tLots.add(tLot);

         tQuantityFound += tLot._numShares;
         if (tQuantityFound >= aQuantity) {
            break;
         }
      }
      
if(passes<2)
{
System.out.println("PASS: " + passes + " -------------------------");
   for(int i = 0; i < tLots.size(); i++)
      System.out.println("lotId: " + tLots.elementAt(i)._lotId + "\n"
            + "buyTradeId: " + tLots.elementAt(i)._buyTradeId + "\n"
            + "numShares: " + tLots.elementAt(i)._numShares
            );
}
passes++;
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
