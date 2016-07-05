package cg.db;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Vector;
import java.util.GregorianCalendar;
import cg.BuyTrade;
import cg.Gain;
import cg.GainAccessor;
import cg.OpenPosition;
import cg.OpenPositionAccessor;
import cg.SellTrade;
import cg.SimpleDate;
import cg.Term;
import cg.OldTrade;
import cg.Trade;
import cg.TradeDataProvider;
import cg.TradeList;

public class DatabaseInterface
{
   /** Singleton of this class. */
   static private DatabaseInterface _hsqldbLoader = null;

   private String tSelectInvestorsSql =
         "SELECT * FROM investor";

   private String tSelectBrokersSql =
         "SELECT * FROM broker";

   private String tSelectAccountTypesSql =
         "SELECT * FROM acct_type";

   private String tClearTradesAndLots =
      "DELETE FROM lot;" +
      "DELETE FROM trade;" +
      "ALTER TABLE lot ALTER COLUMN lot_id RESTART WITH 9000;" +
      "ALTER TABLE trade ALTER COLUMN trade_id RESTART WITH 8000;";
   
   private String _insertTradeSql =
      "INSERT INTO trade "
      + "(acct_id,seqnum,date,buysell,ticker,shares,price,commission,"
      + "special_rule)"
      + " VALUES (?,?,?,?,?,?,?,?,?)";
   
   private String _insertLotSql =
      "INSERT INTO lot "
      + "(parent_id,has_children,trigger_trade_id,first_buy_trade_id,"
      + "last_buy_trade_id,sell_trade_id,num_shares,basis,"
      + "proceeds,state,close_date)"
      + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
   
   private String _updateLotHasChildrenSql =
      "UPDATE lot SET has_children = ? WHERE lot_id = ?";
   
   private String _selectOpenPositionsSql =
      "SELECT symbol, shares, first_buy_seqnum, first_buy_date, last_buy_date, basis FROM vwOpenPositions"
      + " WHERE acct_id = ?";
   
   private String _selectGainsSql =
      "SELECT lot_id,symbol,num_shares,buy_date,buy_price,sell_date,"
      + "sell_price,basis,proceeds FROM closedlotreport WHERE acct_id = ?";

   /**
    * Constructor
    * @param aDbUrl
    */
   private DatabaseInterface()
   {
   }
   
   /**
    * Get the singleton instance.
    * @return
    */
   public static DatabaseInterface getInstance()
   {
      if (_hsqldbLoader == null)
      {
         _hsqldbLoader = new DatabaseInterface();
      }
      return _hsqldbLoader;
   }

   /**
    * Get investors.
    */
   public Vector<InvestorRecord> getInvestorRecords(Connection aConn)
   {
      Vector<InvestorRecord> tInvestors = new Vector<InvestorRecord>();

      // run query
      try
      {
         Statement tSql = aConn.createStatement();

         ResultSet tResults = tSql.executeQuery(tSelectInvestorsSql);

         if (tResults != null)
         {
            while (tResults.next())
            {
               // convert all fields from the db record
               int investor_id = tResults.getInt("investor_id");
               String name = tResults.getString("name");

               InvestorRecord tInvestor = new InvestorRecord();

               tInvestor._investorId = investor_id;
               tInvestor._name = name;

               tInvestors.add(tInvestor);
            }
         }
         tResults.close();
         tSql.close();
      }
      catch (Exception ex)
      {
         System.out.println("***Exception:\n" + ex);
      }

      return tInvestors;
   }

   /**
    * Get brokers.
    */
   public Vector<BrokerRecord> getBrokerRecords(Connection aConn)
   {
      Vector<BrokerRecord> tBrokers = new Vector<BrokerRecord>();

      // run query
      try
      {
         Statement tSql = aConn.createStatement();

         ResultSet tResults = tSql.executeQuery(tSelectBrokersSql);

         if (tResults != null)
         {
            while (tResults.next())
            {
               // convert all fields from the db record
               int broker_id = tResults.getInt("broker_id");
               String name = tResults.getString("name");

               BrokerRecord tBroker = new BrokerRecord();

               tBroker._brokerId = broker_id;
               tBroker._name = name;

               tBrokers.add(tBroker);
            }
         }
         tResults.close();
         tSql.close();
      }
      catch (Exception ex)
      {
         System.out.println("***Exception:\n" + ex);
      }

      return tBrokers;
   }

   /**
    * Get accountTypes.
    */
   public Vector<AccountTypeRecord> getAccountTypeRecords(Connection aConn)
   {
      Vector<AccountTypeRecord> tAccountTypes = new Vector<AccountTypeRecord>();

      // run query
      try
      {
         Statement tSql = aConn.createStatement();

         ResultSet tResults = tSql.executeQuery(tSelectAccountTypesSql);

         if (tResults != null)
         {
            while (tResults.next())
            {
               // convert all fields from the db record
               int accountTypeId = tResults.getInt("acct_type_id");
               boolean isTaxable = tResults.getBoolean("is_taxable");
               String label = tResults.getString("label");

               AccountTypeRecord tAccountType = new AccountTypeRecord();

               tAccountType._accountTypeId = accountTypeId;
               tAccountType._isTaxable = isTaxable;
               tAccountType._label = label;

               tAccountTypes.add(tAccountType);
            }
         }
         tResults.close();
         tSql.close();
      }
      catch (Exception ex)
      {
         System.out.println("***Exception:\n" + ex);
      }

      return tAccountTypes;
   }

   /**
    * Get accounts.
    */
   public Vector<AccountRecord> getAccountRecords(Connection aConn)
   {
      
      Vector<AccountRecord> accounts = new Vector<AccountRecord>();

      try
      {
//// begin demonstrate getting a result set from a stored procedure
////CREATE PROCEDURE atest()
////READS SQL DATA DYNAMIC RESULT SETS 1
////BEGIN ATOMIC
////  DECLARE RESULT SCROLL CURSOR WITH HOLD WITH RETURN FOR SELECT * FROM acct;
////  OPEN RESULT;
////END
//         CallableStatement call = aConn.prepareCall("call atest()");
//         call.execute();
//         if (call.getMoreResults())
//         {
//System.out.println("Got result ==============================");
//             ResultSet result = call.getResultSet();
//            while (result.next())
//            {
//System.out.println("##################!!!!!!!!!!!!====================@@@@@@@@@@");
//               System.out.println("acct_id: " + result.getInt("acct_id"));
//               System.out.println("name: " + result.getString("name"));
//               System.out.println("acct_type_id: " + result.getInt("acct_type_id"));
//               System.out.println("investor_id: " + result.getInt("investor_id"));
//               System.out.println("broker_id: " + result.getInt("broker_id"));
//            }
//         }
//         call.close();
//// end

         Statement tSql = aConn.createStatement();

         ResultSet tResults = tSql.executeQuery("select * from acct");

         if (tResults != null)
         {
            while (tResults.next())
            {
               AccountRecord acct = new AccountRecord();
               acct.acct_id = tResults.getInt("acct_id");
               acct.name = tResults.getString("name");
               acct.acct_type_id = tResults.getInt("acct_type_id");
               acct.investor_id = tResults.getInt("investor_id");
               acct.broker_id = tResults.getInt("broker_id");
               System.out.println(acct.toString());

               accounts.add(acct);
            }
         }
         tResults.close();
         tSql.close();
      }
      catch (Exception ex)
      {
         System.out.println("ERROR:\n" + ex);
      }

      return accounts;
   }

   /**
    * Clear all trades and lots.
    */
   public void clearAllTradesAndLots(Connection aConn)
   {
      try
      {
         Statement tStatement = aConn.createStatement();
         tStatement.executeUpdate(tClearTradesAndLots);
         tStatement.close();
      }
      catch (Exception ex)
      {
         System.out.println("ERROR:\n" + ex);
      }
   }

   public String getAccountName(Connection aConn,int aAccountId)
   {
      String tName = null;

      try
      {
         Statement tSql = aConn.createStatement();

         String tQuery = "select * from acct where acct_id = " + aAccountId;

         ResultSet tResults = tSql.executeQuery(tQuery);

         if (tResults != null)
         {
            if (tResults.next())
            {
               tName = tResults.getString("name");
            }
         }
         tResults.close();
         tSql.close();
      }
      catch (Exception ex)
      {
         System.out.println("ERROR:\n" + ex);
      }
System.out.println("queried account name for id " + aAccountId + ": " + tName);
      return tName;
   }

   /**
    * Get lot report rows.
    */
   public Vector<OpenPosition> getOpenPositions(Connection aConn,int aAccountId)
   {
      Vector<OpenPosition> tLotDatas = new Vector<OpenPosition>();

      try
      {
         PreparedStatement tSql = aConn.prepareStatement(_selectOpenPositionsSql);
         
         tSql.setInt(1,aAccountId);

         ResultSet tResults = tSql.executeQuery();

         if (tResults != null)
         {
            while (tResults.next())
            {
               OpenPosition tLotData = new OpenPosition();
               int tIdx = 1;
               tLotData.set_symbol(tResults.getString(tIdx++));
               tLotData.set_numShares(tResults.getInt(tIdx++));
               tLotData.set_firstBuySeqNum(tResults.getInt(tIdx++));
               tLotData.set_firstBuyDate(new SimpleDate(tResults.getDate(tIdx++)));
               tLotData.set_lastBuyDate(new SimpleDate(tResults.getDate(tIdx++)));
               tLotData.set_basis(tResults.getFloat(tIdx++));
               tLotData.set_term(Term.MIXED);//TODO

               tLotDatas.add(tLotData);
            }
         }
         tResults.close();
         tSql.close();
      }
      catch (Exception ex)
      {
         System.out.println("***Exception:\n" + ex);
      }
      
      return tLotDatas;
   }

   /**
    * Get TODO document
    */
   public Vector<GainAccessor> getGains(Connection aConn,int aAccountId)
   {
      Vector<GainAccessor> tGains = new Vector<GainAccessor>();

      try
      {
         PreparedStatement tSql = aConn.prepareStatement(_selectGainsSql);
         
         tSql.setInt(1,aAccountId);

         ResultSet tResults = tSql.executeQuery();

         if (tResults != null)
         {
            while (tResults.next())
            {
//         "SELECT lot_id, symbol, num_shares, buy_date, buy_price, sell_date, sell_price, basis, proceeds FROM closedlotreport WHERE acct_id = ?";
               Gain tGain = new Gain();
               int i = 1;
               tGain.set_lotId(tResults.getInt(i++));
               tGain.set_symbol(tResults.getString(i++));
               tGain.set_numShares(tResults.getInt(i++));
               tGain.set_buyDate(new SimpleDate(tResults.getDate(i++)));
               tGain.set_buyPrice(tResults.getFloat(i++));
               tGain.set_sellDate(new SimpleDate(tResults.getDate(i++)));
               tGain.set_sellPrice(tResults.getFloat(i++));
               tGain.set_basis(tResults.getFloat(i++));
               tGain.set_proceeds(tResults.getFloat(i++));
               tGain.set_term(Term.MIXED);//TODO

               tGains.add(tGain);
            }
         }
         tResults.close();
         tSql.close();
      }
      catch (Exception ex)
      {
         System.out.println("***Exception:\n" + ex);
      }
      
      return tGains;
   }

   /**
    * Get trades.
    */
   public TradeList getTradeList(Connection aConn,int aAccountId)
   {
      TradeList tlist = new TradeList();

      // run query
      try
      {
         Statement tSql = aConn.createStatement();

         ResultSet tResults = tSql.executeQuery(
               "SELECT DISTINCT * FROM trade WHERE trade.acct_id='"
                     + aAccountId + "' ORDER BY trade.trade_id");
         if (tResults != null)
         {

            while (tResults.next())
            {
               // convert all fields from the db record
               int trade_id = tResults.getInt("trade_id");
               int acct_id = tResults.getInt("acct_id");
               int seqnum = tResults.getInt("seqnum");
               /*
                * GregorianCalendar refdate = new GregorianCalendar(); Date date
                * = results.getDate("date",refdate);
                */
               Date date = tResults.getDate("date");
               String buysell = tResults.getString("buysell");
               String ticker = tResults.getString("ticker");
               int shares = tResults.getInt("shares");
               float price = tResults.getFloat("price");
               BigDecimal commission = tResults.getBigDecimal("commission");
               String special_rule = tResults.getString("special_rule");

               // Convert date to format needed by the Trade constructor
               SimpleDate tdate = new SimpleDate(date);

               // Convert buysell to format needed by the Trade constructor
               OldTrade.Type tradeType = OldTrade.Type.getEnumValue(buysell);

               // Convert special_rule to format needed by the Trade constructor
               OldTrade.SpecialInstruction instr = OldTrade.SpecialInstruction
                     .getEnumValue(special_rule);

               if (tradeType == OldTrade.Type.BUY)
               {
                  BuyTrade bt = new BuyTrade(trade_id, tdate, tradeType, ticker,
                        shares, price, commission, instr, "");
                  tlist.add(bt);
               }
               else
               {
                  SellTrade st = new SellTrade(trade_id, tdate, tradeType,
                        ticker, shares, price, commission, instr, "");
                  tlist.add(st);
               }
            }
         }
         tResults.close();
         tSql.close();
      }
      catch (Exception ex)
      {
         System.out.println("***Exception:\n" + ex);
      }

      return tlist;
   }

   /**
    * Get trades.
    */
   public Vector<TradeRecord> getTrades(Connection aConn,int aAccountId)
   {
      Vector<TradeRecord> tTrades = new Vector<TradeRecord>();

      // run query
      try
      {
         Statement tSql = aConn.createStatement();

         ResultSet tResults = tSql.executeQuery(
               "SELECT DISTINCT * FROM trade WHERE trade.acct_id='"
                     + aAccountId + "' ORDER BY trade.trade_id");
         if (tResults != null)
         {

            while (tResults.next())
            {
               // convert all fields from the db record
               int trade_id = tResults.getInt("trade_id");
               int acct_id = tResults.getInt("acct_id");
               int seqnum = tResults.getInt("seqnum");
               /*
                * GregorianCalendar refdate = new GregorianCalendar(); Date date
                * = results.getDate("date",refdate);
                */
               Date date = tResults.getDate("date");
               String buysell = tResults.getString("buysell");
               String ticker = tResults.getString("ticker");
               int shares = tResults.getInt("shares");
               float price = tResults.getFloat("price");
               BigDecimal commission = tResults.getBigDecimal("commission");
               String special_rule = tResults.getString("special_rule");

               // Convert date to format needed by the Trade constructor
               SimpleDate tdate = new SimpleDate(date);

               // Convert buysell to format needed by the Trade constructor
               OldTrade.Type tradeType = OldTrade.Type.getEnumValue(buysell);

               // Convert special_rule to format needed by the Trade constructor
               OldTrade.SpecialInstruction instr = OldTrade.SpecialInstruction
                     .getEnumValue(special_rule);

               TradeRecord tTrade = new TradeRecord();
               tTrade._tradeId = trade_id;
               tTrade._date = new SimpleDate(date);
               tTrade._tradeType = tradeType;
               tTrade._symbol = ticker;
               tTrade._numShares = shares;
               tTrade._numSharesHeld = new Integer(0);
               tTrade._numSharesSold = new Integer(0);
               tTrade._sharePrice = price;
               tTrade._commission = commission;
               tTrade._claimedTaxYear = new Integer(0);
               tTrade._note = "";
               tTrades.add(tTrade);
            }
         }
         tResults.close();
         tSql.close();
      }
      catch (Exception ex)
      {
         System.out.println("***Exception:\n" + ex);
      }

      return tTrades;
   }

   /**
    * Get trades.
    * -> trade_id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY,
    * -> acct_id INT NOT NULL,
    * -> seqnum INT NOT NULL,
    * -> date DATE NOT NULL,
    * -> buysell varchar(4) NOT NULL,
    * -> ticker varchar(6) NOT NULL,
    * -> shares INT NOT NULL,
    * -> price REAL NOT NULL,
    * -> commission REAL NOT NULL,
    * -> special_rule varchar(10),
    */
   public void insertTrades(Connection aConn,int aAccountId,Vector<OldTrade> aTrades)
   {
      insertTrades_Batch(aConn,aAccountId,aTrades);
//      insertTrades_OneByOne(aConn,aAccountId,aTrades);
   }

   /**
    * Insert trades one-by-one.
    */
   private void insertTrades_OneByOne(Connection aConn,int aAccountId,Vector<OldTrade> aTrades)
   {
      // run query
      try
      {
    	  PreparedStatement pstmt = aConn.prepareStatement(_insertTradeSql,Statement.RETURN_GENERATED_KEYS);

    	  int tTradeIndex = 0;
    	  
    	  for (OldTrade tTrade: aTrades)
    	  {
    	     /*
    	      * Build the prepared insert statement.
    	      */
    		  pstmt.setInt(1,aAccountId); //acct_id

    		  pstmt.setInt(2,tTrade.tradeId);

    		  pstmt.setDate(3,new Date(tTrade.date.getDate().getTime()));
    		  
    		  String tBuySell = (tTrade.isBuyTrade() ? "Buy":"Sell");
    		  pstmt.setString(4,tBuySell);

    		  pstmt.setString(5,tTrade.ticker);

    		  pstmt.setInt(6,tTrade.numShares);

    		  pstmt.setBigDecimal(7,new BigDecimal(tTrade.sharePrice));

    		  pstmt.setBigDecimal(8,tTrade.comm);

    		  pstmt.setString(9,"test");

    		  /*
    		   * Execute the batch command to save the trades to database.
    		   */
    		  pstmt.executeUpdate();

    		  /*
    		   * Update the trades with their primary keys.
    		   */
    		  ResultSet genKeys = pstmt.getGeneratedKeys();
    		  if (genKeys.next())
    		  {
    		     int tTradeId = genKeys.getInt(1);
    		     aTrades.elementAt(tTradeIndex).tradeId = tTradeId;
    		     tTradeIndex++;
    		  }
    	  }

         pstmt.close();
      }
      catch (Exception ex)
      {
         System.out.println("Exception writing trades to db:\n" + ex);
      }
   }

   /**
    * Get trades.
    */
   private void insertTrades_Batch(Connection aConn,int aAccountId,Vector<OldTrade> aTrades)
   {
      // run query
      try
      {
    	  aConn.setAutoCommit(false);
    	  
    	  PreparedStatement pstmt = aConn.prepareStatement(_insertTradeSql,Statement.RETURN_GENERATED_KEYS);

    	  for (OldTrade tTrade: aTrades)
    	  {
    	     /*
    	      * Build the prepared insert statement.
    	      */
    		  pstmt.setInt(1,aAccountId);

    		  pstmt.setInt(2,tTrade.tradeId);

    		  pstmt.setDate(3,new Date(tTrade.date.getDate().getTime()));
    		  
    		  String tBuySell = (tTrade.isBuyTrade() ? "Buy":"Sell");
    		  pstmt.setString(4,tBuySell);

    		  pstmt.setString(5,tTrade.ticker);

    		  pstmt.setInt(6,tTrade.numShares);

    		  pstmt.setBigDecimal(7,new BigDecimal(tTrade.sharePrice));

    		  pstmt.setBigDecimal(8,tTrade.comm);

    		  pstmt.setString(9,"test");

    		  pstmt.addBatch();
    	  }

    	  /*
    	   * Execute the batch command to save the trades to database.
    	   */
    	  int[] updateCount = pstmt.executeBatch();
    	  aConn.setAutoCommit(true); 

    	  /*
    	   * Update the trades with their primary keys.
    	   */
    	  ResultSet genKeys = pstmt.getGeneratedKeys();
    	  int tIndex = 0;
    	  while (genKeys.next())
    	  {
    	     int tTradeId = genKeys.getInt(1);
    	     aTrades.elementAt(tIndex).tradeId = tTradeId;
    	     tIndex++;
    	  }

         pstmt.close();
      }
      catch (Exception ex)
      {
         System.out.println("Exception writing trades to db:\n" + ex);
      }
   }

   /**
    * Insert lot.
    * -> int _lotId;
    * -> int _parentId;
    * -> int _triggerTradeId;
	 * -> int _firstBuyTradeId;
	 * -> int _sellTradeId;
	 * -> int _numShares;
	 * -> BigDecimal _basis;
	 * -> BigDecimal _proceeds;
	 * -> State _state;
    */
   public void insertLot(Connection aConn,LotRecord aLot)
   {
      try
      {
    	  PreparedStatement pstmt = aConn.prepareStatement(_insertLotSql,Statement.RETURN_GENERATED_KEYS);

    	  /*
    	   * Build the prepared insert statement.
    	   */
//      + "(parent_id,has_children,trigger_trade_id,first_buy_trade_id,"
//      + "last_buy_trade_id,sell_trade_id,num_shares,basis,"
//      + "proceeds,state,close_date)"
//      + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    	  int tIdx = 1;
    	  setIntOrNull(pstmt,tIdx++, aLot._parentId);
    	  pstmt.setBoolean(tIdx++,aLot._hasChildren);
    	  pstmt.setInt(tIdx++,aLot._triggerTradeId);
    	  pstmt.setInt(tIdx++,aLot._firstBuyTradeId);
    	  pstmt.setInt(tIdx++,aLot._lastBuyTradeId);
    	  setIntOrNull(pstmt,tIdx++,aLot._sellTradeId);
    	  pstmt.setInt(tIdx++,aLot._numShares);
    	  pstmt.setBigDecimal(tIdx++,aLot._basis);
    	  pstmt.setBigDecimal(tIdx++,aLot._proceeds);
    	  pstmt.setString(tIdx++,aLot._state.toString());
        setDateOrNull(pstmt,tIdx++,aLot._closeDate);

    	  /*
    	   * Execute the batch command to save the trades to database.
    	   */
    	  pstmt.executeUpdate();

    	  /*
    	   * Update the trades with their primary keys.
    	   */
    	  ResultSet genKeys = pstmt.getGeneratedKeys();
    	  if (genKeys.next())
    	  {
    	     int tId = genKeys.getInt(1);
    	     aLot._lotId = tId;
    	  }
    	  else
    	  {
    	     //TODO ERROR
    	  }

         pstmt.close();
      }
      catch (Exception ex)
      {
         System.out.println("Exception inserting lot to db:\n" + ex);
      }
   }

   /**
    */
   public void updateLotHasChildren(Connection aConn,LotRecord aLot)
   {
      try
      {
         PreparedStatement pstmt = aConn.prepareStatement(_updateLotHasChildrenSql);

         /*
          * Build the prepared update statement.
          */

         pstmt.setBoolean(1,aLot._hasChildren);
         pstmt.setInt(2,aLot._lotId);

         /*
          * Execute the update.
          */
         pstmt.executeUpdate();

         pstmt.close();
      }
      catch (Exception ex)
      {
         System.out.println("Exception updating lot to db:\n" + ex);
      }
   }

   /**
    * TODO maybe create a class derived from PreparedStatement and add setIntOrNull
    * @param aStatement
    * @param aIndex
    * @param aInteger
    */
   private void setIntOrNull(
         PreparedStatement aStatement,int aIndex,Integer aInteger)
   {
      try
      {
         if (aInteger == null)
         {
            aStatement.setNull(aIndex,java.sql.Types.INTEGER);
         }
         else
         {
            aStatement.setInt(aIndex,aInteger);
         }
      }
      catch (Exception ex)
      {
         System.out.println("Exception in setIntIfPositive:\n" + ex);
      }
   }

   private void setDateOrNull(
         PreparedStatement aStatement,int aIndex,SimpleDate aDate)
   {
      try
      {
         if (aDate == null)
         {
            aStatement.setNull(aIndex,java.sql.Types.DATE);
         }
         else
         {
            aStatement.setDate(aIndex,new Date(aDate.getDate().getTime()));
         }
      }
      catch (Exception ex)
      {
         System.out.println("Exception in setIntIfPositive:\n" + ex);
      }
   }
}
