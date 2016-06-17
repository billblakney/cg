package cg;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;
import cg.db.HSQLDB_Loader;

/*
 * Singleton class that manages the storage of data for this app.
 * To use the database, a call must be made to setDbUrl.
 */
public class DataStore
{
   static DataStore _dataStore = null;
   
   HSQLDB_Loader _db = null;
   
   Vector<Trade> _trades = new Vector<Trade>();

   Vector<Lot> _lots = new Vector<Lot>();
   
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
   
   public void setDbUrl(String dbUrl)
   {
	   _db = new HSQLDB_Loader(dbUrl);
	   if (!_db.canConnectToDb())
	   {
	      System.out.println("ERROR: Can't connect to db with url " + dbUrl);
	      System.exit(0);
	   }
   }
   
   public void addTrades(
         int aAccountId,TradeList aTradeList,boolean aProcessThem)
   {
      if (_db != null)
      {
         _db.insertTrades(aAccountId,aTradeList);
      }

      for (Trade tTrade: aTradeList)
      {
         _trades.add(tTrade);

         if (tTrade.isBuyTrade())
         {
            processBuyTrade((BuyTrade)tTrade);
         }
         else
         {
            processSellTrade((SellTrade)tTrade);
         }
      }
   }
   
   /*
    * TODO is only for ntx accts now
    */
   protected void processBuyTrade(BuyTrade aTrade)
   {
      /*
       * Create a new buy lot.
       */
      Lot tLot = new Lot();
      tLot.lotId = null;
      tLot.parentId = null;
      tLot.hasChildren = false;
      tLot.triggerTradeId = aTrade.tradeId;
      tLot.buyTradeId = aTrade.tradeId;
      tLot.sellTradeId = null;
      tLot.numShares = aTrade.numShares;
		float tBasis = ((float)aTrade.numShares) * aTrade.sharePrice + aTrade.comm.floatValue(); //TODO use method?
      tLot.basis = new BigDecimal(tBasis); //TODO
      tLot.proceeds = new BigDecimal(0.0);
      tLot.state = Lot.State.eOpen;
      tLot.hasChildren = false;

      /*
       * Save the lot to the db.
       */
      _db.insertLot(tLot);

      /*
       * Save the lot to the cache.
       */
      _lots.add(tLot);
   }
   
   /*
    * TODO is only for ntx accts now
    */
   protected void processSellTrade(SellTrade aTrade)
   {
      int tNumToDistribute = aTrade.numShares;
System.out.println("number to distrubute: " + tNumToDistribute);

      Vector<Lot> tLots = getActiveOpenLots(aTrade.ticker,aTrade.numShares);
      Iterator<Lot> tIterator = tLots.iterator();
System.out.println("tLots.size: " + tLots.size());

      while (tIterator.hasNext() && tNumToDistribute > 0)
      {
         Lot tLot = tIterator.next();
         /*
          * If the number left to distribute is less than the number
          * of lot shares, then only part of the lot is consumed. This will
          * produce two new lots: one sold and one open.
          */
         if (tNumToDistribute < tLot.numShares)
         {
            // two new lots and update old.hasChildren
System.out.println("creating two new lots from old");
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
System.out.println("creating one new lot from old");
            tNumToDistribute -= tLot.numShares;
         }
         
      }
   }

   protected Vector<Lot> getActiveOpenLots(String aTicker,int aQuantity)
   {
      int tQuantityFound = 0;
      
      Vector<Lot> tLots = new Vector<Lot>();
      
      for (Lot tLot: _lots)
      {
         if (tLot.hasChildren || tLot.state != Lot.State.eOpen){
System.out.println("has children or not open!!!!!!!!!");
            continue;
         }

         Trade tTrade = getTradeById(tLot.buyTradeId);
         if (tTrade == null){
            System.out.println("ERROR: Need more open lots to sell!");
            System.exit(0);
         }

         if (!tTrade.ticker.equals(aTicker)) {
System.out.println("wrong ticker!!!!!!!!!");
            continue;
         }
         
System.out.println("=======> adding lot");
         tLots.add(tLot);

         tQuantityFound += tLot.numShares;
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
