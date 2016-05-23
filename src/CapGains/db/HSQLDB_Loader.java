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

public class HSQLDB_Loader implements CapGainsDB {

	Connection db = null; // A connection to the database
	Statement sql; // Our statement to run queries with

	public Vector<AccountInfo> getAccountInfoVector() {

		Vector<AccountInfo> accounts = new Vector<AccountInfo>();

		if( connectdb() == false )
			return accounts;

		// run query
		try {
			System.out.println("Now executing the command: "
					+ "select * from acct");
			ResultSet results = sql.executeQuery("select * from acct");
			if (results != null) {

				while (results.next()) {
					AccountInfo acct = new AccountInfo();
					acct.shortname = results.getString("shortname");
					acct.longname = results.getString("longname");
					acct.investor = results.getString("investor");
					acct.broker = results.getString("broker");
					System.out.println(acct.toString());

					accounts.add(acct);
				}
			}
			results.close();
		} catch (Exception ex) {
			System.out.println("***Exception:\n" + ex);
			ex.printStackTrace();
		}

		closedb();
		
		return accounts;
	}

	public TradeList getTrades(String acct_shortname) {

		TradeList tlist = new TradeList();
		
		if( connectdb() == false )
			return tlist;
		
		// run query
		try {
			System.out.println("Now executing the command: "
					+ "SELECT DISTINCT * FROM trade WHERE trade.acct = acct.shortname = acct_shortname ORDER BY trade.seqnum");
			ResultSet results = sql.executeQuery("SELECT DISTINCT * FROM trade WHERE trade.acct='" + acct_shortname
					                    + "' ORDER BY trade.seqnum");
			if (results != null) {

				while (results.next()) {
					// convert all fields from the db record
					String acct = results.getString("acct");
					int seqnum = results.getInt("seqnum");
/*					
GregorianCalendar refdate = new GregorianCalendar();
					Date date = results.getDate("date",refdate);
*/
					Date date = results.getDate("date");
					String buysell = results.getString("buysell");
					String ticker = results.getString("ticker");
					long shares = results.getLong("shares");
					float price = results.getFloat("price");
					BigDecimal commission = results.getBigDecimal("commission");
					String special_rule = results.getString("special_rule");

					// Convert date to format needed by the Trade constructor
					SimpleDate tdate = new SimpleDate(date);

					// Convert buysell to format needed by the Trade constructor
					Trade.Type tradeType = Trade.Type.getEnumValue(buysell);

					// Convert special_rule to format needed by the Trade constructor
					Trade.SpecialInstruction instr = Trade.SpecialInstruction.getEnumValue(special_rule);

					
					// protected Trade(int id, CapGains.TradeDate date, Trade.Type tradeType,
					//        String ticker, long numShares, float sharePrice, BigDecimal comm,
					//        Trade.SpecialInstruction instruction, String note)
					
					if( tradeType == Trade.Type.BUY ){
						BuyTrade bt = new BuyTrade(seqnum,tdate,tradeType,ticker,shares,price,commission,instr,"");
						tlist.add(bt);
					}
					else {
						SellTrade st = new SellTrade(seqnum,tdate,tradeType,ticker,shares,price,commission,instr,"");
						tlist.add(st);
					}
				}
			}
			results.close();
		} catch (Exception ex) {
			System.out.println("***Exception:\n" + ex);
			ex.printStackTrace();
		}

		closedb();
		return tlist;
	}

	private boolean connectdb(){
		try {
			connectToDB();
		}
		catch (Exception ex) {
			System.out.println("Failed to connect to db:\n" + ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean closedb(){
		try {
			db.close();
		}
		catch (Exception ex) {
			System.out.println("Failed to close db:\n" + ex);
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void connectToDB() {
		System.out
				.println("Checking if Driver is registered with DriverManager.");

		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Couldn't find the driver!");
			System.out.println("Let's print a stack trace, and exit.");
			cnfe.printStackTrace();
			System.exit(1);
		}

		System.out
				.println("Registered the driver ok, so let's make a connection.");

		try {
			// The second and third arguments are the username and password,
			// respectively. They should be whatever is necessary to connect
			// to the database.
			db = DriverManager
					.getConnection(
							"jdbc:hsqldb:hsql://localhost/E/home/CapGains/CapGains.db/dbz/dbz_text",
							"SA", "");
		} catch (SQLException se) {
			System.out
					.println("Couldn't connect: print out a stack trace and exit.");
			se.printStackTrace();
			System.exit(1);
		}

		if (db != null)
			System.out.println("Hooray! We connected to the database!");
		else
			System.out.println("We should never get here.");

			try {
				sql = db.createStatement(); // create sql statement for later use 
			} catch (SQLException se) {
				System.out
						.println("Couldn't create sql statement: print out a stack trace and exit.");
				se.printStackTrace();
				System.exit(1);
			}
	}
}
