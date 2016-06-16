package cg;

import cg.db.HSQLDB_Loader;

/*
 * Singleton class that manages the storage of data for this app.
 * To use the database, a call must be made to setDbUrl.
 */
public class DataStore
{
   static DataStore _dataStore = null;
   
   HSQLDB_Loader _db = null;
   
   TradeList _tradeList = new TradeList();
   
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

      _tradeList.addAll(aTradeList);
      
      if (_db != null)
      {
         _db.insertTrades(aAccountId,aTradeList);
      }

      for (Trade tTrade: aTradeList)
      {
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

      /*
       * Save the buy lot to the db.
       */
   }
   
   /*
    * TODO is only for ntx accts now
    */
   protected void processSellTrade(SellTrade aTrade)
   {
   }
}
