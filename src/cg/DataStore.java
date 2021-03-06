package cg;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.TreeSet;
import java.util.Vector;
import cg.db.AccountDataProxy;
import cg.db.AccountRecord;
import cg.db.BrokerRecord;
import cg.db.ConnectionManager;
import cg.db.DatabaseInterface;
import cg.db.InvestorRecord;
import cg.db.LotRecord;
import cg.db.TradeRecord;
import cg.db.LotRecord.State;

/*
 * Singleton class that manages the storage of data for this app.
 * To use the database, a call must be made to setDbUrl.
 */
public class DataStore
{
   static DataStore _dataStore = null;
   
   ConnectionManager _cm = null;
   
   DatabaseInterface _dbi = null;
   
   //TODO load
   Vector<Investor> _investors = new Vector<Investor>();
   
   //TODO load
   Vector<Broker> _brokers = new Vector<Broker>();
   
   //TODO load
   Vector<AccountType> _accountTypes = new Vector<AccountType>();
   
   //TODO load
   Vector<Account> _accounts = new Vector<Account>();

   //TODO load
   Vector<OldTrade> _trades = new Vector<OldTrade>();

   Vector<Lot> _lots = new Vector<Lot>();

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
      
      Connection tConn = _cm.getConnection();
      
      _investors = Investor.getInvestors(_dbi.selectInvestors(tConn));
      _brokers = Broker.getBrokers(_dbi.selectBrokers(tConn));
      _accountTypes = AccountType.getAccountTypes(_dbi.selectAccountTypes(tConn));
      _accounts = Account.getAccounts(_dbi.selectAccounts(tConn));
   }
   
   public void clearAllTradesAndLots()
   {

      if (_cm != null)
      {
         Connection tConn = _cm.getConnection();
         if (tConn != null)
         {
            _dbi.deleteAllTradesAndLots(tConn);
            _cm.closeConnection(tConn);
         }
      }
      else
      {
         //TODO
      }
      _trades = new Vector<OldTrade>();
      _lots = new Vector<Lot>();
   }

   public AbstractAccountData getAccountDataProvider(int aAccountId)
   {
      return new AccountDataProxy(this,aAccountId);
   }
   
   //TODO from cache
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
   
   public LinkedHashMap<Integer,String> getAccountNameMap()
   {
      return Account.getAccountNameMap(_accounts);
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

   public Vector<Trade> getTrades(int aAccountId)
   {
      Vector<TradeRecord> tTradeRecords = null;

      if (_cm != null)
      {
         Connection tConn = _cm.getConnection();
         if (tConn != null)
         {
            tTradeRecords = _dbi.getTrades(tConn,aAccountId);
            _cm.closeConnection(tConn);
         }
      }
      else
      {
         //TODO
      }
      
//      Vector<OldTrade> tTrades = null;
      Vector<Trade> tTrades = new Vector<Trade>();

      for (TradeRecord tRecord: tTradeRecords)
      {
         tTrades.add(new Trade(tRecord));
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


   public Vector<AccountRecord> getAccounts()
   {
      Vector<AccountRecord> tAcctInfo = null;

      if (_cm != null)
      {
         Connection tConn = _cm.getConnection();
         if (tConn != null)
         {
            tAcctInfo = _dbi.selectAccounts(tConn);
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
	public Vector<OpenPosition>
	getOpenPositions(int aAccountId,String ticker, String year)
	{
		Vector<OpenPosition> tOpenPositions = new Vector<OpenPosition>();

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
      
      if (ticker != null)
      {
         tOpenPositions.removeIf(p -> p.getSymbol().equals(ticker));
      }

      return tOpenPositions;
	}

   //TODO don't really want this public, its only for AccountDataProxy use
	public Vector<Lot>
	getOpenPositions_new(int aAccountId,String ticker, String year)
	{
System.out.println("_lots.size(): " + _lots.size());
		Vector<Lot> tOpenPositions = new Vector<Lot>(_lots);
System.out.println("tOpenPositions.size(): " + tOpenPositions.size());

		/*
		 * Remove any parent lots and lots that are not open.
		 */
		tOpenPositions.removeIf( p ->
		      (p.lotRecord._hasChildren || !(p.lotRecord._state == State.eOpen)));
System.out.println("tOpenPositions.size(): " + tOpenPositions.size());
      
		/*
		 * Filter by ticker if ticker is present.
		 */
      if (ticker != null)
      {
         tOpenPositions.removeIf(p -> !p.getSymbol().equals(ticker));
System.out.println("tOpenPositions.size(): " + tOpenPositions.size());
      }

      return tOpenPositions;
	}

   //TODO don't really want this public, its only for AccountDataProxy use
	public Vector<GainAccessor>
	getGains(int aAccountId,String ticker, String year)
	{
		Vector<GainAccessor> tGains = new Vector<GainAccessor>();

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

      for (OldTrade tTrade: aTradeList)
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
      tLot._firstBuyTradeId = aTrade.tradeId;
      tLot._lastBuyTradeId = aTrade.tradeId;
      tLot._sellTradeId = null;
      tLot._numShares = aTrade.numShares;
		float tBasis = ((float)aTrade.numShares) * aTrade.sharePrice + aTrade.comm.floatValue(); //TODO use method?
      tLot._basis = new BigDecimal(tBasis); //TODO
      tLot._proceeds = new BigDecimal(0.0);
      tLot._state = LotRecord.State.eOpen;
      tLot._closeDate = null;

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
      _lots.add(new Lot(tLot));
   }
   
   /*
    * TODO is only for ntx accts now
    */
   private void processSellTrade(Connection aConn,SellTrade aSellTrade)
   {
      int tNumToDistribute = aSellTrade.numShares;

      Vector<LotRecord> tLots =
            getActiveOpenLots(aSellTrade.ticker,aSellTrade.numShares);

      Iterator<LotRecord> tIterator = tLots.iterator();

      Vector<LotRecord> tNewLots = new Vector<LotRecord>();
      
      while (tIterator.hasNext() && tNumToDistribute > 0)
      {
         LotRecord tLot = tIterator.next();
         /*
          * Case: Shares to sell only consume a portion of this lot.
          * This will produce two new lots:
          * 1. An open lot that has the remaining shares available to sell.
          * 2. A closed lot representing the shares sold.
          */
         if (tNumToDistribute < tLot._numShares)
         {
            // two new lots and update old.hasChildren
            
            /*
             * Create a new closed lot that represents the shares sold, and 
             * represents a capital gain.
             */
            LotRecord tNewClosedLot = new LotRecord();
            {
            tNewClosedLot._parentId = tLot._lotId;
            tNewClosedLot._hasChildren = false;
            tNewClosedLot._triggerTradeId = aSellTrade.tradeId;
            tNewClosedLot._firstBuyTradeId = tLot._firstBuyTradeId;
            tNewClosedLot._lastBuyTradeId = tLot._lastBuyTradeId;
            tNewClosedLot._sellTradeId = aSellTrade.tradeId;
            tNewClosedLot._numShares = tNumToDistribute;
            float tLotFactor =
                  (float)tNewClosedLot._numShares/(float)tLot._numShares;
            tNewClosedLot._basis =
                  new BigDecimal(tLotFactor*tLot._basis.floatValue());
            float tTradeFactor =
                  (float)tNumToDistribute/(float)aSellTrade.numShares;
            float tProceeds =
                  ((float)tNumToDistribute) * aSellTrade.sharePrice.floatValue()
                     - tTradeFactor * aSellTrade.comm.floatValue();
            tNewClosedLot._proceeds = new BigDecimal(tProceeds);
            tNewClosedLot._state = LotRecord.State.eClosed;
            tNewClosedLot._closeDate = aSellTrade.date; //TODO ntx only
            }

            tNewLots.add(tNewClosedLot);

            /*
             * Create a new open lot to represent the remaining open position.
             */
            LotRecord tNewOpenLot = new LotRecord();
            {
            tNewOpenLot._parentId = tLot._lotId;
            tNewOpenLot._hasChildren = false;
            tNewOpenLot._triggerTradeId = aSellTrade.tradeId;
            tNewOpenLot._firstBuyTradeId = tLot._firstBuyTradeId;
            tNewOpenLot._lastBuyTradeId = tLot._lastBuyTradeId;
            tNewOpenLot._sellTradeId = null;
            tNewOpenLot._numShares = tLot._numShares - tNumToDistribute;
            float tFactor = (float)tNewOpenLot._numShares/(float)tLot._numShares;
            tNewOpenLot._basis = new BigDecimal(tFactor*tLot._basis.floatValue());
            tNewOpenLot._proceeds = new BigDecimal(0.0);
            tNewOpenLot._state = LotRecord.State.eOpen;
            tNewOpenLot._closeDate = null;
            }

            tNewLots.add(tNewOpenLot);

            tNumToDistribute = 0;
         }
         /*
          * Case: Shares to sell consumes the entire lot.
          * This will produce one new lot:
          * 2. A closed lot representing the shares sold.
          */
         else
         {
            // new lot and update old.hasChildren

            LotRecord tNewClosedLot = new LotRecord();
            {
//   public Integer    _lotId;
//   public Integer    _parentId;
//   public boolean    _hasChildren;
//   public Integer    _triggerTradeId;
//   public Integer    _firstBuyTradeId;
//   public Integer    _lastBuyTradeId;
//   public Integer    _sellTradeId;
//   public int        _numShares;
//   public BigDecimal _basis;
//   public BigDecimal _proceeds;
//   public State      _state;
//   public SimpleDate _closeDate;
//	//public int term; //TODO enum
//	//public String note;
            tNewClosedLot._parentId = tLot._lotId;
            tNewClosedLot._hasChildren = false;
            tNewClosedLot._triggerTradeId = aSellTrade.tradeId;
            tNewClosedLot._firstBuyTradeId = tLot._firstBuyTradeId;
            tNewClosedLot._lastBuyTradeId = tLot._lastBuyTradeId;
            tNewClosedLot._sellTradeId = aSellTrade.tradeId;
            tNewClosedLot._numShares = tLot._numShares;
            tNewClosedLot._basis = new BigDecimal(tLot._basis.floatValue());
            float tTradeFactor =
                  (float)tLot._numShares/(float)aSellTrade.numShares;
            float tProceeds =
                  ((float)tLot._numShares) * aSellTrade.sharePrice.floatValue()
                     - tTradeFactor * aSellTrade.comm.floatValue();
            tNewClosedLot._proceeds = new BigDecimal(tProceeds);
            tNewClosedLot._state = LotRecord.State.eClosed;
            tNewClosedLot._closeDate = aSellTrade.date;
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
         
         _lots.add(new Lot(tLot));
      }
   }

   /**
    * Get the list of active open lots for a symbol ordered by FIFO.
    * <p>
    * The FIFO ordering is determined by the sequence number of the lot's
    * associated buy trade.
    * <p>
    * @param aSymbol The symbol of the lots being requested.
    * @param aQuantity The minimum number of shares to be represented by the
    *    returned lots.
    * @return The list of lots ordered by FIFO and representing at least the
    *    requested number of shares.
    * 
    * TODO get from db
    */
   protected Vector<LotRecord> getActiveOpenLots(String aSymbol,int aQuantity)
   {
      int tQuantityFound = 0;
      
      Vector<LotRecord> tLots = new Vector<LotRecord>();
      
      //TODO need to sort on acquire date; need to add acquire date to LotRecord, or use view
      Vector<LotRecord> tLotRecords = Lot.getLotRecords(_lots);
      tLotRecords.sort((LotRecord lot1,LotRecord lot2)-> (lot1._lastBuyTradeId - lot2._lastBuyTradeId));

      for (LotRecord tLot: tLotRecords)
      {
         if (tLot._hasChildren || tLot._state != LotRecord.State.eOpen){
            continue;
         }

         OldTrade tTrade = getTradeById(tLot._lastBuyTradeId); //TODO acquire
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
      
      return tLots;
   }
   
   protected Investor getInvestorById(Integer aInvestorId)
   {
      if (aInvestorId != null)
      {
         for (Investor tInvestor: _investors)
         {
            if (tInvestor.getInvestorId() == aInvestorId)
            {
               return tInvestor;
            }
         }
      }
      return null;
   }
   
   protected Broker getBrokerById(Integer aBrokerId)
   {
      if (aBrokerId != null)
      {
         for (Broker tBroker: _brokers)
         {
            if (tBroker.getBrokerId() == aBrokerId)
            {
               return tBroker;
            }
         }
      }
      return null;
   }
   
   protected AccountType getAccountTypeById(Integer aAccountTypeId)
   {
      if (aAccountTypeId != null)
      {
         for (AccountType tAccountType: _accountTypes)
         {
            if (tAccountType.getAccountTypeId() == aAccountTypeId)
            {
               return tAccountType;
            }
         }
      }
      return null;
   }
   
   protected OldTrade getTradeById(Integer aTradeId)
   {
      if (aTradeId != null)
      {
         for (OldTrade tTrade: _trades)
         {
            if (tTrade.tradeId == aTradeId)
            {
               return tTrade;
            }
         }
      }
      return null;
   }
}
