package CapGains.db;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Collections;
import java.util.Vector;
import java.util.GregorianCalendar;
import CapGains.AccountInfo;
import CapGains.Trade;
import CapGains.BuyTrade;
import CapGains.SellTrade;
import CapGains.SimpleDate;
import CapGains.TradeList;

public class HSQLDB_Loader implements CapGainsDB
{
   /** Full path to the database. */
   private String _dbUrl = null;

   /** Database user. */
   private String _dbUser = "SA";

   /** Database user. */
   private String _dbPwd = "";

   /** Database connection. */
   private Connection _db = null;
   
   /**
    * Constructor
    * @param aDbUrl
    */
   public HSQLDB_Loader(String aDbUrl)
   {
      _dbUrl = aDbUrl;
   }

   /**
    * Get accounts.
    */
   public Vector<AccountInfo> getAccountInfoVector()
   {

      Vector<AccountInfo> accounts = new Vector<AccountInfo>();

      if (connectdb() == false)
      {
         return accounts;
      }

      // run query
      try
      {
         System.out.println("Now executing the command: "
               + "select * from acct");

         Statement tSql = _db.createStatement();

         ResultSet tResults = tSql.executeQuery("select * from acct");

         if (tResults != null)
         {

            while (tResults.next())
            {
               AccountInfo acct = new AccountInfo();
               acct.shortname = tResults.getString("shortname");
               acct.longname = tResults.getString("longname");
               acct.investor = tResults.getString("investor");
               acct.broker = tResults.getString("broker");
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
         ex.printStackTrace();
      }

      closedb();

      return accounts;
   }

   /**
    * Get trades.
    */
   public TradeList getTrades(String acct_shortname)
   {

      TradeList tlist = new TradeList();

      if (connectdb() == false) return tlist;

      // run query
      try
      {
         Statement tSql = _db.createStatement();

         System.out
               .println("Now executing the command: "
                     + "SELECT DISTINCT * FROM trade WHERE trade.acct = acct.shortname = acct_shortname ORDER BY trade.seqnum");

         ResultSet tResults = tSql.executeQuery(
               "SELECT DISTINCT * FROM trade WHERE trade.acct='"
                     + acct_shortname + "' ORDER BY trade.seqnum");
         if (tResults != null)
         {

            while (tResults.next())
            {
               // convert all fields from the db record
               String acct = tResults.getString("acct");
               int seqnum = tResults.getInt("seqnum");
               /*
                * GregorianCalendar refdate = new GregorianCalendar(); Date date
                * = results.getDate("date",refdate);
                */
               Date date = tResults.getDate("date");
               String buysell = tResults.getString("buysell");
               String ticker = tResults.getString("ticker");
               long shares = tResults.getLong("shares");
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

               // protected Trade(int id, CapGains.TradeDate date, Trade.Type
               // tradeType,
               // String ticker, long numShares, float sharePrice, BigDecimal
               // comm,
               // Trade.SpecialInstruction instruction, String note)

               if (tradeType == Trade.Type.BUY)
               {
                  BuyTrade bt = new BuyTrade(seqnum, tdate, tradeType, ticker,
                        shares, price, commission, instr, "");
                  tlist.add(bt);
               }
               else
               {
                  SellTrade st = new SellTrade(seqnum, tdate, tradeType,
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
         ex.printStackTrace();
      }

      closedb();
      return tlist;
   }

   /**
    * Get trades.
    */
   public void addTrades(Vector<Trade> aTrades)
   {
      if (connectdb() == false){
    	  //TODO error msg
    	  System.out.println("ERROR: failed to connect to db");
    	  return;
      }

      // run query
      try
      {
    	  _db.setAutoCommit(false);

    	  //INSERT INTO trade VALUES ('Main(ET)',1,'2000-04-10','Buy','NEOP',3000,1.0,19.95,null);
    	  PreparedStatement pstmt = _db.prepareStatement("INSERT INTO trade VALUES(?,?,?,?,?,?,?,?,?)");

    	  pstmt.setString(1,"Main(ET)");
    	  pstmt.setInt(2, 9);
    	  pstmt.setDate(3, new Date(2016,6,7));
    	  pstmt.setString(4,"Buy");
    	  pstmt.setString(5,"AGEN");
    	  pstmt.setInt(6,1500);
    	  pstmt.setBigDecimal(7,new BigDecimal(3.5));
    	  pstmt.setBigDecimal(8,new BigDecimal(9.95));
    	  pstmt.setString(9,null);
//    	  pstmt.setString(2, "52919-49278");
//    	  pstmt.setFloat(3, 49.99);
//    	  pstmt.setBoolean(4, true);

    	  pstmt.addBatch();

    	  // rinse, lather, repeat

System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    	  int[] updateCount = pstmt.executeBatch();
    	  _db.commit();
    	  _db.setAutoCommit(true); 

         pstmt.close();
//         tSql.close();
//    	  
//    	  
//    	  
//    	  
//         Statement tSql = _db.createStatement();
//
//         System.out
//               .println("Now executing the command: "
//                     + "SELECT DISTINCT * FROM trade WHERE trade.acct = acct.shortname = acct_shortname ORDER BY trade.seqnum");
//
//         ResultSet tResults = tSql.executeQuery(
//               "SELECT DISTINCT * FROM trade WHERE trade.acct='"
//                     + acct_shortname + "' ORDER BY trade.seqnum");
//         if (tResults != null)
//         {
//
//            while (tResults.next())
//            {
//               // convert all fields from the db record
//               String acct = tResults.getString("acct");
//               int seqnum = tResults.getInt("seqnum");
//               /*
//                * GregorianCalendar refdate = new GregorianCalendar(); Date date
//                * = results.getDate("date",refdate);
//                */
//               Date date = tResults.getDate("date");
//               String buysell = tResults.getString("buysell");
//               String ticker = tResults.getString("ticker");
//               long shares = tResults.getLong("shares");
//               float price = tResults.getFloat("price");
//               BigDecimal commission = tResults.getBigDecimal("commission");
//               String special_rule = tResults.getString("special_rule");
//
//               // Convert date to format needed by the Trade constructor
//               SimpleDate tdate = new SimpleDate(date);
//
//               // Convert buysell to format needed by the Trade constructor
//               Trade.Type tradeType = Trade.Type.getEnumValue(buysell);
//
//               // Convert special_rule to format needed by the Trade constructor
//               Trade.SpecialInstruction instr = Trade.SpecialInstruction
//                     .getEnumValue(special_rule);
//
//               // protected Trade(int id, CapGains.TradeDate date, Trade.Type
//               // tradeType,
//               // String ticker, long numShares, float sharePrice, BigDecimal
//               // comm,
//               // Trade.SpecialInstruction instruction, String note)
//
//               if (tradeType == Trade.Type.BUY)
//               {
//                  BuyTrade bt = new BuyTrade(seqnum, tdate, tradeType, ticker,
//                        shares, price, commission, instr, "");
//                  aTrades.add(bt);
//               }
//               else
//               {
//                  SellTrade st = new SellTrade(seqnum, tdate, tradeType,
//                        ticker, shares, price, commission, instr, "");
//                  aTrades.add(st);
//               }
//            }
//         }
//         tResults.close();
//         tSql.close();
      }
      catch (Exception ex)
      {
         System.out.println("***Exception:\n" + ex);
         ex.printStackTrace();
      }

      closedb();
   }

   /**
    * Open connection to the database.
    * @return
    */
   private boolean connectdb()
   {
      try
      {
         connectToDB();
      }
      catch (Exception ex)
      {
         System.out.println("Failed to connect to db:\n" + ex);
         ex.printStackTrace();
         return false;
      }
      return true;
   }

   /**
    * Close connection to the dtabase.
    * @return
    */
   private boolean closedb()
   {
      try
      {
         _db.close();
      }
      catch (Exception ex)
      {
         System.out.println("Failed to close db:\n" + ex);
         ex.printStackTrace();
         return false;
      }
      return true;
   }

   /**
    * Obtain a connection to the database.
    */
   private void connectToDB()
   {
      /*
       * Verify that the driver is registered.
       */
      try
      {
         Class.forName("org.hsqldb.jdbcDriver");
      }
      catch (ClassNotFoundException ex)
      {
         System.out.println("Couldn't find the driver!\n" + ex);
         System.exit(1);
      }

      try
      {
         // The second and third arguments are the username and password,
         // respectively. They should be whatever is necessary to connect
         // to the database.
         _db = DriverManager.getConnection(_dbUrl,_dbUser,_dbPwd);
      }
      catch (SQLException ex)
      {
         System.out.println("Couldn't connect:\n" + ex);
         System.exit(1);
      }

      if (_db != null)
      {
         System.out.println("Connected to the database.");
      }
      else
      {
         System.out.println("ERROR: null database connection");
         System.exit(1);
      }
   }
}
