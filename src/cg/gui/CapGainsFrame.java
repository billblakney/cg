package cg.gui;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import cg.*;
import cg.db.*;
//import CapGains.db.PostgreSQL_Loader;

/**
 * The main frame of the CapGains application. The main() method is the entry
 * point for the CapGains application. </br> The main method takes zero, one, or
 * two command-line arguments:
 * <ul>
 * <li>arg[0] - Name of the default directory for the "load trades" file chooser
 * <li>arg[1] - Name of trade file to be loaded at startup
 * </ul>
 * 
 * </br><b>GUI Components</b></br> The main frame contains the following three
 * areas:
 * <ul>
 * <li>report selector panel - control for creating reports
 * <li>report tabbed pane - tabbed pain for displaying reports
 * <li>tree panel - not currently used
 * </ul>
 * 
 * </br><b>Methods Overview</b></br>
 * <ul>
 * <li>CapGainsFrame() constructor. Processes command line arguments, makes a
 * call to initFrame() to setup the GUI, and initializes the application with a
 * new account, or the account specified via a command line argument.
 * <li>initFrame(). Sets up the GUI components for this frame. It is called by
 * the constructor.
 * </ul>
 * 
 * </br><b>Inner Classes Overview</b></br> The CapsGainsMenuBar is an inner
 * class. Its sole purpose is to create all of the menus and menu items for the
 * application. It also processes the action events for these menu items by
 * making appropriate calls to the CapGainsFrame methods.
 */
public class CapGainsFrame extends JFrame {

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

	private DataStore _dataStore = null;

	/**
	 * The account that is currently displayed.
	 */
	private Account legacyAccount = null;

	/**
	 * The tabbed pane that displays reports.
	 */
	ReportTabbedPane reportTabbedPane;

	/**
	 * The panel that displays the account in a tree format.
	 */
	private ReportSelectorPanel reportSelectorPanel;

	/**
	 * The default reports that are displayed with the selection of an account.
	 */
	private ReportType[] defaultReportTypes = {
			ReportType.ALL_TRADES,
			ReportType.TAX_GAINS };

	/**
	 * The main routine for the CapGains application.
	 */
	public static void main(String[] args) {
		CapGainsFrame frame = new CapGainsFrame(args);
		frame.setVisible(true);
	}

	/**
	 * The default constructor, which does the following:
	 * <ul>
	 * <li>Processes the command line arguments.
	 * <li>Sets up all of the GUI components of the frame with a call to
	 * initFrame().
	 * <li>Initializes the frame with account. By default the initial account is
	 * empty. This default is overridden if a trade file was specified as a
	 * command line argument.
	 * <li>The actionLoadTrades() and actionClearTrades() methods handles the
	 * user actions to load trades and clear trades, respectively.
	 * <li>The setAccount() method calls the setAccount() methods on all of the
	 * main GUI components (obsolete?)
	 */
	public CapGainsFrame(String[] args) {
		super("CapGains");
		
		processEnvironmentVars();
		
		processCommandLine(args);
		
		if (dbUrl != null)
		{
		   _dataStore = DataStore.getInstance();
		   _dataStore.setDbUrl(dbUrl);
		}

		// construct the frame and set its properties
		initFrame();

if( tradeFile != null)
{
TradeList trades = TradeFileReader.loadTradeFile(tradeFile);
Account acct = new Account("TODO",trades);
showAccount(acct);
}
	}

	/**
	 * Process any relevant environment variables.
	 */
	private void processEnvironmentVars()
	{
	   dataDir = System.getenv("CG_DATADIR");
	   tradeFile = System.getenv("CG_TRADEFILE");
	   dbUrl = System.getenv("HSQLDB_URL");
	}

	/**
	 * Process the command line arguments.
	 */
	private void processCommandLine(String[] args)
	{
	   for (int i = 0; i < args.length; i++)
	   {
	      if (args[i].equals("-h"))
	      {
	         printUsage();
	      }
	      if (args[i].equals("-datadir"))
	      {
	         if (i+1 < args.length)
	         {
	            dataDir = args[i+1];
	         }
	         else
	         {
	            System.out.println("missing -datadir arg");
	         }
	      }
	      else if (args[i].equals("-tradefile"))
	      {
	         if (i+1 < args.length)
	         {
	            String tradeFile = args[i+1];
	         }
	         else
	         {
	            System.out.println("missing -tradefile arg");
	         }
	      }
	      else if (args[i].equals("-dburl"))
	      {
	         if (i+1 < args.length)
	         {
	            dbUrl = args[i+1];
	         }
	         else
	         {
	            System.out.println("missing -dburl arg");
	         }
	      }
	   }
System.out.println("dataDir: " + dataDir);
System.out.println("tradeFile: " + tradeFile);
System.out.println("dbUrl: " + dbUrl);
	}

	/**
	 * Print usage.
	 */
	private void printUsage()
	{
	   System.out.println("args: -datadir <datadir> [-file <tradefile>] [-db <local_db_path]");
	}
	
	/**
	 * Method to update the display with data for a specified account.
	 * 
	 * @param acct Account to be displayed.
	 */
	@Deprecated
	public void showAccount(Account acct)
	{
	   legacyAccount = acct;
//		reportTabbedPane.removeAll();

		reportTabbedPane.addReport(ReportType.ALL_TRADES,acct);
//		reportTabbedPane.addReport(ReportType.SHARES_HELD,acct);
//		reportTabbedPane.addReport(ReportType.LOTS_HELD,acct);
//
//		reportTabbedPane.addReport(ReportType.TAX_GAINS,acct);
		reportTabbedPane.addReport(ReportType.TAX_GAIN_LOTS,acct);
//		reportTabbedPane.addReport(ReportType.GAINS_BY_SECURITY,acct);
//		reportTabbedPane.addReport(ReportType.GAINS_BY_YEAR,acct);
	}

	/**
	 * Sets up the GUI components of the frame.
	 */
	private void initFrame() {

		// add window listener
		addWindowListener(new BasicWindowMonitor());

		// set background color
		setBackground(Color.cyan);

		// create the tabbed pane for reports
		reportTabbedPane = new ReportTabbedPane();

		// create the report tree panel
		Map<Integer,String> tAccountNames = new LinkedHashMap<Integer,String>();
		if (_dataStore != null)
		{
		   // TODO kind of ugly; add method to get names directly
		   Vector<AccountRecord> tAccounts = _dataStore.getAccountInfoVector();
//		   tAccountNames = AccountRecord.getNames(tAccounts); //TODO maybe deprecate getNames
		   for (AccountRecord tInfo: tAccounts)
		   {
		      tAccountNames.put(tInfo.acct_id, tInfo.name);
		   }
		}
		else
		{
		   tAccountNames.put(0,"Legacy");
		}
		reportSelectorPanel = new ReportSelectorPanel(reportTabbedPane,tAccountNames);

		// add panels to split panes
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				reportSelectorPanel, reportTabbedPane);

		// activate the mainSplitPane
		getContentPane().removeAll();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		setJMenuBar(new CapGainsMenuBar());

		// Pack and set frame size.
		// Note that setSize() won't work if moved above pack().
		pack();
		setSize(1200, 875);
	}

	/**
	 * Handles the main frame close event.
	 */
	public class BasicWindowMonitor extends WindowAdapter {

		public void windowClosing(WindowEvent e) {
			Window w = e.getWindow();
			w.setVisible(false);
			w.dispose();
			System.exit(0);
		}
	}

	/**
	 * Prompts the user to select an account
	 */
	@Deprecated
	private void actionSelectAccount() {

		Vector<AccountRecord> accts = _dataStore.getAccountInfoVector();
		Object[] choices = accts.toArray();

		AccountRecord selected_acct = (AccountRecord) JOptionPane.showInputDialog(
				this, "Select an account", "Select Account",
				JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);

		System.out.println("Selected account: " + selected_acct.name);

		TradeList trades = _dataStore.getTradeList(selected_acct.acct_id);
		Account acct = new Account(selected_acct.name,trades);
		showAccount(acct);
	}

	/**
	 * Creates a new account and loads new trades into it.
	 */
	private void actionLegacyLoadAccountTradeFile(String tradeFile) {
		TradeList trades = TradeFileReader.loadTradeFile(tradeFile);
		Account acct = new Account("LEGACY",trades);
		showAccount(acct);
	}

	/**
	 * Clears existing trades. Does this by replacing the existing account with
	 * a new empty one.
	 */
	private void actionLegacyClearTrades() {
		legacyAccount.removeAllTrades();
	}

	/**
	 * TODO Using this routine to develop the DB capability.
	 */
	private void actionLoadAccountTradeFile()
	{
		// Get all accounts for dialog selection.
		Vector<AccountRecord> tAccountInfo = _dataStore.getAccountInfoVector();
		Vector<String> tAccountNames = AccountRecord.getNames(tAccountInfo);
//		DataStore.printAccountInfoVector(tAccountInfo);

		// Launch the load trade file dialog.
		LoadTradeFileDialog tDialog = new LoadTradeFileDialog(
				(JFrame)this,true,tAccountNames,dataDir,"Test Message");

		if (tDialog.pressedOk())
		{
			System.out.println("user pressed ok");

			// Get info selected by user in the dialog.
			String tAccountName = tDialog.getAccountName();
			int tAccountId = AccountRecord.getAccountId(tAccountInfo, tAccountName);
			String tTradeFileName = tDialog.getTradeFileName();

			System.out.println("user selected account " + tAccountName + ", id " + tAccountId);
			System.out.println("user selected trade file " + tTradeFileName);

			// Load the trades from the trade file and write them to the DB.
			TradeList trades = TradeFileReader.loadTradeFile(tTradeFileName);
			_dataStore.addTrades(tAccountId,trades,true);

//			// For now, show reports for the loaded trade file.
//			// TODO later, the reports will take data from the DB, but not yet.
//			Account acct = new Account(tAccountName,trades);
//			showAccount(acct);
//
////			actionCreateReport(ReportType.LOTS_HELD);
//			reportTabbedPane.addReport("FROMDB",ReportType.LOTS_HELD,
//			      _dataStore.getAccountDataProvider(tAccountId)); //TODO individual accounts
		}
	}

	/**
	 * Clears existing trades. Does this by replacing the existing account with
	 * a new empty one.
	 */
	private void actionClearTrades() {
		_dataStore.clearAllTradesAndLots();
	}

	/**
	 * Loads new trades into the displayed account.
	 * 
	 * @param tradeFile
	 *            File that contains the new trades to be loaded.
	 */
	private void actionLoadTrades(String tradeFile) {
		TradeList trades = TradeFileReader.loadTradeFile(tradeFile);
		legacyAccount.addTrades(trades);
	}

	/**
	 * Creates a report of the specified type for the specified account.
	 */
	private void actionCreateReport(ReportType type) {
		reportTabbedPane.addReport(type,legacyAccount);
	}

	/**
	 * Clears existing trades. Does this by replacing the existing account with
	 * a new empty one.
	 */
	private void actionRemoveAllReports() {
		reportTabbedPane.removeAll();
	}

	/**
	 * This class implements the main application menu bar.
	 */
	public class CapGainsMenuBar extends JMenuBar implements ActionListener {

		// File menu strings
		final String ADD_INVESTOR = "Add Investor";
		final String SELECT_INVESTOR = "Select Investor";
		final String SELECT_DB_ACCOUNT = "Select DB Account";
		final String LEGACY_LOAD_TRADE_FILE = "LEGACY Load Account Trade File";
		final String LEGACY_CLEAR_TRADES = "LEGACY Clear Trades";
		final String LOAD_TRADE_FILE = "Load Account Trade File";
		final String CLEAR_TRADES = "Clear Trades";
		final String REMOVE_ALL_REPORTS = "Remove All Reports";

		final String YEARLY_GAINS_SUMMARY_REPORT = "Yearly Gains Summary Report";
		final String SECURITY_GAINS_SUMMARY_REPORT = "Security Gains Summary Report";

		/**
		 * The default constructor. This constructor sets up the menu items for
		 * CapsGainsFrame.
		 */
		CapGainsMenuBar() {
			createFileMenu();
			createTestMenu();
			createWindowMenu();
		}

		/**
		 * Creates the "File" main menu and its items.
		 */
		private void createFileMenu() {

			// Create a "File" menu.
			JMenu fileMenu = new JMenu("File");
			
			JMenuItem tItem;

			// Add "Add Investor" menu item to the "File" menu.
			tItem = new JMenuItem(ADD_INVESTOR);
			tItem.addActionListener(this);
			fileMenu.add(tItem);

			// Add "Select Investor" menu item to the "File" menu.
			tItem = new JMenuItem(SELECT_INVESTOR);
			tItem.addActionListener(this);
			fileMenu.add(tItem);

			// Add "Select DB Account" menu item to the "File" menu.
			tItem = new JMenuItem(SELECT_DB_ACCOUNT);
			tItem.addActionListener(this);
			if (dbUrl != null){
			   tItem.setEnabled(false);
			}
			fileMenu.add(tItem);

			// Add "LEGACY Load Trades From File" menu item to the "File" menu.
			tItem = new JMenuItem(LEGACY_LOAD_TRADE_FILE);
			tItem.addActionListener(this);
			fileMenu.add(tItem);

			// Add "LEGACY Clear Trades" menu item to the "File" menu.
			tItem = new JMenuItem(LEGACY_CLEAR_TRADES);
			tItem.addActionListener(this);
			fileMenu.add(tItem);

			// Add "Load Trades From File" menu item to the "File" menu.
			tItem = new JMenuItem(LOAD_TRADE_FILE);
			tItem.addActionListener(this);
			fileMenu.add(tItem);

			// Add "Clear Trades" menu item to the "File" menu.
			tItem = new JMenuItem(CLEAR_TRADES);
			tItem.addActionListener(this);
			fileMenu.add(tItem);

			// Add "Remove All Reports" menu item to the "File" menu.
			tItem = new JMenuItem(REMOVE_ALL_REPORTS);
			tItem.addActionListener(this);
			fileMenu.add(tItem);

			// Add the "File" menu to the menu bar.
			add(fileMenu);
		} // end method

		/**
		 * Creates the "Test" main menu and its items.
		 */
		private void createTestMenu() {

			// Create a "" menu.
			JMenu gainPanelMenu = new JMenu("Test");

			// Add "" menu item to the "" menu.
			JMenuItem itemYearlyGainsSummary = new JMenuItem(
					YEARLY_GAINS_SUMMARY_REPORT);
			itemYearlyGainsSummary.addActionListener(this);
			gainPanelMenu.add(itemYearlyGainsSummary);

			// Add "" menu item to the "" menu.
			JMenuItem itemSecurityGainsSummary = new JMenuItem(
					SECURITY_GAINS_SUMMARY_REPORT);
			itemSecurityGainsSummary.addActionListener(this);
			gainPanelMenu.add(itemSecurityGainsSummary);

			// Add the "Gain Panel" menu to the menu bar.
			add(gainPanelMenu);
		} // end method

		/**
		 * Creates the "Gain Panel" main menu and its items.
		 */
		private void createWindowMenu() {

			// Create a "" menu.
			JMenu gainPanelMenu = new JMenu("Window");

			// Add "" menu item to the "" menu.
			JMenuItem itemChooseColors = new JMenuItem("Choose Colors");
			itemChooseColors.addActionListener(this);
			gainPanelMenu.add(itemChooseColors);

			// Add the "Gain Panel" menu to the menu bar.
			add(gainPanelMenu);
		} // end method

		/**
		 * Handles ActionEvents for the CapGainsMenuBar. Currently the only
		 * action events handled are "Clear Trades" and "Load Trades".
		 */
		public void actionPerformed(ActionEvent e) {

			// Handle "Load Trades".
			if (e.getActionCommand().equals(LOAD_TRADE_FILE)) {
				System.out.println("User selected \"Select Load Trades\"");
				actionLoadAccountTradeFile();
			}
			// Handle "Clear Trades".
			else if (e.getActionCommand().equals(CLEAR_TRADES)) {
				System.out.println("User selected \"Clear Trades\"");
				// clear the trades
				actionClearTrades();
			}
			// Handle "Remove All Reports".
			else if (e.getActionCommand().equals(REMOVE_ALL_REPORTS)) {
				System.out.println("User selected \"Remove All Reports\"");
				// clear the trades
				actionRemoveAllReports();
			}
			// Handle "Add Investor"
			else if (e.getActionCommand().equals(ADD_INVESTOR)) {
				System.out.println("User selected \"Add Investor\"");
			}
			// Handle "Select Investor"
			else if (e.getActionCommand().equals(SELECT_INVESTOR)) {
				System.out.println("User selected \"Select Investor\"");
			}
			// Handle "Select DB Account"
			else if (e.getActionCommand().equals(SELECT_DB_ACCOUNT)) {
				System.out.println("User selected \"Select DB Account\"");
//				actionSelectAccount();
			}
			// Handle "LEGACY Select File Account"
			else if (e.getActionCommand().equals(LEGACY_LOAD_TRADE_FILE)) {
				System.out.println("User selected " + LEGACY_LOAD_TRADE_FILE);

				// Use file choser to select trade file.
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(dataDir));

				int option = chooser.showOpenDialog(this);
				if (option == JFileChooser.APPROVE_OPTION)
				{
					String tradeFile = chooser.getSelectedFile().getPath();
					System.out.println("File selected: " + tradeFile);

					// load the trades
					actionLegacyLoadAccountTradeFile(tradeFile);
				}
				else
				{
					System.out.println("File selection canceled");
				}
			}
			// Handle "LEGACY Clear Trades".
			else if (e.getActionCommand().equals(LEGACY_CLEAR_TRADES)) {
				System.out.println("User selected \"LEGACY Clear Trades\"");
				// clear the trades
				actionLegacyClearTrades();
			}
			// Handle "Yearly Gains Summary Report"
			else if (e.getActionCommand().equals(YEARLY_GAINS_SUMMARY_REPORT)) {
				System.out
						.println("User selected \"Yearly Gains Summary Report\"");
				actionCreateReport(ReportType.GAINS_BY_YEAR);
			}
			// Handle "Security Gains Summary Report"
			else if (e.getActionCommand().equals(SECURITY_GAINS_SUMMARY_REPORT)) {
				System.out
						.println("User selected \"Security Gains Summary Report\"");
				actionCreateReport(ReportType.GAINS_BY_SECURITY);
			}
			// Handle "Choose Colors"
			else if (e.getActionCommand().equals("Choose Colors")) {
				System.out.println("User selected \"Choose Colors\"");
				Color newColor = JColorChooser.showDialog(this, "Choose Color",
						Color.yellow);

			}

		}
	}
}