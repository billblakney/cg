package cg;

import java.util.Vector;
import cg.db.AccountInfo;

public class Test1
{
	/** Starting directory for the file chooser launched by "Load Trades". */
	private String _dataDir = null;

	/** Starting directory for the file chooser launched by "Load Trades". */
	private String _tradeFile = null;

	/**
	 * HSQLDB URL
	 * Example for local server engine:
	 * "jdbc:hsqldb:hsql://localhost/cg/db/db1/data/db1"
	 * See HSQLDB documentation for more possible setups.
	 */
	private String _dbUrl = null;

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
	   DataStore tDataStore = DataStore.getInstance();
	   tDataStore.setDbUrl(_dbUrl);

	   /*
	    * Get all accounts from the db. //TODO put to DataStore
	    */
	   Vector<AccountInfo> tAccountInfo = tDataStore.getAccountInfoVector();
//	   Vector<String> tAccountNames = AccountInfo.getNames(tAccountInfo);

	   DataStore.printAccountInfoVector(tAccountInfo);

	   /*
	    * Load the trades from the trade file.
	    */
	   TradeList tTradeList = TradeFileReader.loadTradeFile(_tradeFile);
	   int tAccountId = AccountInfo.getAccountId(tAccountInfo, "TestAccount");
	   System.out.println("tAccountId: " + tAccountId);
	   
	   /*
	    * Add the trades to the data store, where they will be processed and
	    * saved.
	    */
	   tDataStore.addTrades(tAccountId,tTradeList,true);
	   
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
	   _dataDir = System.getenv("CG_DATADIR");
      if (_dataDir == null)
      {
         System.out.println("Missing dataDir (CG_DATADIR)");
         System.exit(0);
      }
	   _tradeFile = System.getenv("CG_TRADEFILE");
      if (_tradeFile == null)
      {
         System.out.println("Missing tradeFile (CG_TRADEFILE)");
         System.exit(0);
      }
	   _dbUrl = System.getenv("HSQLDB_URL");
      if (_dbUrl == null)
      {
         System.out.println("Missing dbUrl (HSQLDB_URL)");
         System.exit(0);
      }
      
      _tradeFile = _dataDir + "/" + _tradeFile;
	}
}
