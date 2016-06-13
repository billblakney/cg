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
import cg.SellTrade;
import cg.SimpleDate;
import cg.Trade;
import cg.TradeList;

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
               acct.id = tResults.getInt("acct_id");
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
   public void insertTrades(int aAccountId,Vector<Trade> aTrades)
   {
      if (connectdb() == false){
    	  System.out.println("ERROR: failed to connect to db");
    	  return;
      }

      // run query
      try
      {
    	  _db.setAutoCommit(false);
    	  
//    	   trade_id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY,
//    	   acct_id INT NOT NULL,
//    	   seqnum INT NOT NULL,
//    	   date DATE NOT NULL,
//    	   buysell varchar(4) NOT NULL,
//    	   ticker varchar(6) NOT NULL,
//    	   shares INT NOT NULL,
//    	   price REAL NOT NULL,
//    	   commission REAL NOT NULL,
//    	   special_rule varchar(10),
//    	   FOREIGN KEY (acct_id) REFERENCES acct(acct_id));

    	  //INSERT INTO trade VALUES ('Main(ET)',1,'2000-04-10','Buy','NEOP',3000,1.0,19.95,null);
    	  PreparedStatement pstmt = _db.prepareStatement(
"INSERT INTO trade (acct_id,seqnum,date,buysell,ticker,shares,price,commission,special_rule) VALUES (?,?,?,?,?,?,?,?,?)");
//"INSERT INTO trade (acct,seqnum,date,buysell,ticker,shares,price,commission,special_rule) VALUES (?,?,?,?,?,?,?,?,?)");

//    	  int count = 4000;
    	  for (Trade tTrade: aTrades)
    	  {
    		  pstmt.setInt(1,aAccountId); //acct_id

//Statement tSql = _db.createStatement();
//ResultSet tResults = tSql.executeQuery("CALL NEXT VALUE FOR myseq");
//if (tResults != null){
//	tResults.next();
//System.out.println("NEXTVALUE: " + tResults.getInt(1));
//}

    		  pstmt.setInt(2,tTrade.uID);
//    		  pstmt.setInt(2,count++); //seqnum

    		  pstmt.setDate(3,new Date(tTrade.date.getDate().getTime()));
    		  
    		  String tBuySell = (tTrade.isBuyTrade() ? "Buy":"Sell");
    		  pstmt.setString(4,tBuySell);

    		  pstmt.setString(5,tTrade.ticker);

    		  pstmt.setInt(6,(int)tTrade.numShares.intValue());

    		  pstmt.setBigDecimal(7,new BigDecimal(tTrade.sharePrice));

    		  pstmt.setBigDecimal(8,tTrade.comm);

    		  pstmt.setString(9,"test");

    		  pstmt.addBatch();
    	  }

    	  int[] updateCount = pstmt.executeBatch();
    	  _db.setAutoCommit(true); 

         pstmt.close();
      }
      catch (Exception ex)
      {
         System.out.println("Exception writing trades to db:\n" + ex);
         ex.printStackTrace();
      }

System.out.println("closing db...");
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
