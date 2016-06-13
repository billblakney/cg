package cg;

import java.util.Vector;
import cg.db.HSQLDB_Loader;

public class Test1
{
	/** Starting directory for the file chooser launched by "Load Trades". */
	private String dataDir = null;

	/** Starting directory for the file chooser launched by "Load Trades". */
	private String tradeFile = null;

	/**
	 * HSQLDB URL
	 * Example for local server engine:
	 * "jdbc:hsqldb:hsql://localhost/cg/db/db1/data/db1"
	 * See HSQLDB documentation for more possible setups.
	 */
	private String dbUrl = null;

   public static void main(String[] args)
   {
		Test1 tTest = new Test1(args);
		tTest.processEnvironmentVars();
//		processCommandLine(args);
		tTest.runTest();
   }

	public Test1(String[] args)
	{
	}
	
	private void runTest()
	{
	   HSQLDB_Loader db = new HSQLDB_Loader(dbUrl);

	   // Get all accounts for dialog selection.
	   Vector<AccountInfo> tAccountInfo = db.getAccountInfoVector();
//	   Vector<String> tAccountNames = AccountInfo.getNames(tAccountInfo);

	   HSQLDB_Loader.printAccountInfoVector(tAccountInfo);
	   // Load the trades from the trade file and write them to the DB.
//	   TradeList trades = TradeFileReader.loadTradeFile("/home/bill/workspace/cg/data/EtRoth/allTrades.txt");
	   TradeList trades = TradeFileReader.loadTradeFile(tradeFile);
	   int tAccountId = AccountInfo.getAccountId(tAccountInfo, "TestAccount");
	   System.out.println("tAccountId: " + tAccountId);
	   db.insertTrades(tAccountId,trades);

//	   // For now, show reports for the loaded trade file.
//	   // TODO later, the reports will take data from the DB, but not yet.
//	   String tAccountName = "EtRoth";
//	   Account acct = new Account(tAccountName,trades);
//	   //      showAccount(acct);
//
	}

	/**
	 * Process any relevant environment variables.
	 */
	private void processEnvironmentVars()
	{
	   dataDir = System.getenv("CG_DATADIR");
      if (dataDir == null)
      {
         System.out.println("Missing dataDir (CG_DATADIR)");
         System.exit(0);
      }
	   tradeFile = System.getenv("CG_TRADEFILE");
      if (tradeFile == null)
      {
         System.out.println("Missing tradeFile (CG_TRADEFILE)");
         System.exit(0);
      }
	   dbUrl = System.getenv("HSQLDB_URL");
      if (dbUrl == null)
      {
         System.out.println("Missing dbUrl (HSQLDB_URL)");
         System.exit(0);
      }
      
      tradeFile = dataDir + "/" + tradeFile;
	}
}
