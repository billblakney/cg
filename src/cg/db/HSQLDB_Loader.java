package cg.db;

import java.math.BigDecimal;
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

import cg.AccountInfo;
import cg.BuyTrade;
import cg.Lot;
import cg.SellTrade;
import cg.SimpleDate;
import cg.Trade;
import cg.TradeList;

public class HSQLDB_Loader
{
   /** Singleton of this class. */
   static private HSQLDB_Loader _hsqldbLoader = null;
   
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
   
   /**
    * Constructor
    * @param aDbUrl
    */
   private HSQLDB_Loader()
   {
   }
   
   /**
    * Get the singleton instance.
    * @return
    */
   public static HSQLDB_Loader getInstance()
   {
      if (_hsqldbLoader == null)
      {
         _hsqldbLoader = new HSQLDB_Loader();
      }
      return _hsqldbLoader;
   }

   /**
    * Get accounts.
    */
   public Vector<AccountInfo> getAccountInfoVector(Connection aConn)
   {
      
      Vector<AccountInfo> accounts = new Vector<AccountInfo>();

      // run query
      try
      {
         System.out.println("Now executing the command: "
               + "select * from acct");

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

   /**
    * Get trades.
    */
   public TradeList getTrades(Connection aConn,int aAccountId)
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
    * -> int lotId;
    * -> int parentId;
    * -> int triggerTradeId;
	 * -> int buyTradeId;
	 * -> int sellTradeId;
	 * -> int numShares;
	 * -> BigDecimal basis;
	 * -> BigDecimal proceeds;
	 * -> State state;
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
    	  setIntOrNull(pstmt,tIdx++, aLot.parentId);
    	  pstmt.setBoolean(tIdx++,aLot.hasChildren);
    	  pstmt.setInt(tIdx++,aLot.triggerTradeId);
    	  pstmt.setInt(tIdx++,aLot.buyTradeId);
    	  setIntOrNull(pstmt,tIdx++,aLot.sellTradeId);
    	  pstmt.setInt(tIdx++,aLot.numShares);
    	  pstmt.setBigDecimal(tIdx++,aLot.basis);
    	  pstmt.setBigDecimal(tIdx++,aLot.proceeds);
    	  pstmt.setString(tIdx++,aLot.state.toString());

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
    	     aLot.lotId = tId;
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

         pstmt.setBoolean(1,aLot.hasChildren);
         pstmt.setInt(2,aLot.lotId);

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
