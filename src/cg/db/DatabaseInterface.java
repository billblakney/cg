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
import cg.LotData;
import cg.LotDataProvider;
import cg.SellTrade;
import cg.SimpleDate;
import cg.Term;
import cg.Trade;
import cg.TradeData;
import cg.TradeDataProvider;
import cg.TradeList;

public class DatabaseInterface
{
   /** Singleton of this class. */
   static private DatabaseInterface _hsqldbLoader = null;
   
   private String _insertTradeSql =
         "INSERT INTO trade "
         + "(acct_id,seqnum,date,buysell,ticker,shares,price,commission,special_rule)"
         + " VALUES (?,?,?,?,?,?,?,?,?)";
   
   private String _insertLotSql =
         "INSERT INTO lot "
         + "(parent_id,has_children,trigger_trade_id,buy_trade_id,sell_trade_id,num_shares,basis,proceeds,state)"
         + " VALUES (?,?,?,?,?,?,?,?,?)";
   
   private String _updateLotHasChildrenSql =
         "UPDATE lot SET has_children = ? WHERE lot_id = ?";
   
   private String _selectLotDataSql =
         "SELECT symbol, buy_date, shares, buy_price FROM lotreport WHERE acct_id = ?";
   
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
    * Get accounts.
    */
   public Vector<AccountInfo> getAccountInfoVector(Connection aConn)
   {
      
      Vector<AccountInfo> accounts = new Vector<AccountInfo>();

      try
      {
//// begin
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
               AccountInfo acct = new AccountInfo();
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
   public Vector<LotDataProvider> getLotData(Connection aConn,int aAccountId)
   {
      Vector<LotDataProvider> tLotDatas = new Vector<LotDataProvider>();

      try
      {
         PreparedStatement tSql = aConn.prepareStatement(_selectLotDataSql);
         
         tSql.setInt(1,aAccountId);

         ResultSet tResults = tSql.executeQuery();

         if (tResults != null)
         {
            while (tResults.next())
            {
               LotData tLotData = new LotData();
               tLotData.set_symbol(tResults.getString(1));
               tLotData.set_buyDate(new SimpleDate(tResults.getDate(2)));
               tLotData.set_numShares(tResults.getInt(3));
               tLotData.set_buyPrice(tResults.getFloat(4));
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
               Trade.Type tradeType = Trade.Type.getEnumValue(buysell);

               // Convert special_rule to format needed by the Trade constructor
               Trade.SpecialInstruction instr = Trade.SpecialInstruction
                     .getEnumValue(special_rule);

               if (tradeType == Trade.Type.BUY)
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
   public Vector<TradeDataProvider> getTrades(Connection aConn,int aAccountId)
   {
      Vector<TradeDataProvider> tTrades = new Vector<TradeDataProvider>();

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
               Trade.Type tradeType = Trade.Type.getEnumValue(buysell);

               // Convert special_rule to format needed by the Trade constructor
               Trade.SpecialInstruction instr = Trade.SpecialInstruction
                     .getEnumValue(special_rule);

               TradeData tTrade = new TradeData();
               tTrade.set_tradeId(trade_id);
               tTrade.set_date(new SimpleDate(date));
               tTrade.set_tradeType(tradeType);
               tTrade.set_symbol(ticker);
               tTrade.set_numShares(shares);
               tTrade.set_numSharesHeld(0);
               tTrade.set_numSharesSold(0);
               tTrade.set_sharePrice(price);
               tTrade.set_commission(commission);
               tTrade.set_claimedTaxYear(0);
               tTrade.set_note("");
               
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
   public void insertTrades(Connection aConn,int aAccountId,Vector<Trade> aTrades)
   {
      insertTrades_Batch(aConn,aAccountId,aTrades);
//      insertTrades_OneByOne(aConn,aAccountId,aTrades);
   }

   /**
    * Insert trades one-by-one.
    */
   private void insertTrades_OneByOne(Connection aConn,int aAccountId,Vector<Trade> aTrades)
   {
      // run query
      try
      {
    	  PreparedStatement pstmt = aConn.prepareStatement(_insertTradeSql,Statement.RETURN_GENERATED_KEYS);

    	  int tTradeIndex = 0;
    	  
    	  for (Trade tTrade: aTrades)
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
   private void insertTrades_Batch(Connection aConn,int aAccountId,Vector<Trade> aTrades)
   {
      // run query
      try
      {
    	  aConn.setAutoCommit(false);
    	  
    	  PreparedStatement pstmt = aConn.prepareStatement(_insertTradeSql,Statement.RETURN_GENERATED_KEYS);

    	  for (Trade tTrade: aTrades)
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
	 * -> int _buyTradeId;
	 * -> int _sellTradeId;
	 * -> int _numShares;
	 * -> BigDecimal _basis;
	 * -> BigDecimal _proceeds;
	 * -> State _state;
    */
   public void insertLot(Connection aConn,Lot aLot)
   {
      try
      {
    	  PreparedStatement pstmt = aConn.prepareStatement(_insertLotSql,Statement.RETURN_GENERATED_KEYS);

    	  /*
    	   * Build the prepared insert statement.
    	   */
    	  int tIdx = 1;
    	  setIntOrNull(pstmt,tIdx++, aLot._parentId);
    	  pstmt.setBoolean(tIdx++,aLot._hasChildren);
    	  pstmt.setInt(tIdx++,aLot._triggerTradeId);
    	  pstmt.setInt(tIdx++,aLot._buyTradeId);
    	  setIntOrNull(pstmt,tIdx++,aLot._sellTradeId);
    	  pstmt.setInt(tIdx++,aLot._numShares);
    	  pstmt.setBigDecimal(tIdx++,aLot._basis);
    	  pstmt.setBigDecimal(tIdx++,aLot._proceeds);
    	  pstmt.setString(tIdx++,aLot._state.toString());

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
         System.out.println("Exception writing lot to db:\n" + ex);
      }
   }

   /**
    */
   public void updateLotHasChildren(Connection aConn,Lot aLot)
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
         System.out.println("Exception writing lot to db:\n" + ex);
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
}
