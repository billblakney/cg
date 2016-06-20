package cg.gui;

import javax.swing.*;

import cg.*;

/**
 * 
 *
 */
public class ReportTabbedPane extends JTabbedPane {
	
	public ReportTabbedPane() {
	}

	/**
	 * Adds a report tab for the specified report type for the specified account.
	 * 
	 * @param type Type of report.
	 * @param acct Account being reported.
	 */
	public void addReport(
	      String accountName,ReportType type, AbstractAccountData acct) {
		JPanel newPanel = getReportPanel(type, acct);
		String title = accountName + " - " + type.toString();
		addTab(title, newPanel);
		int index = indexOfComponent(newPanel);
		setTabComponentAt(index, new ButtonTabComponent(this));
	}
	
	/**
	 * Change the account displayed by all the report tabs.
	 * @param acct
	 */
	public void changeAccount(AbstractAccountData acct){
		int count = getTabCount();
		for( int i = 0; i < count; i++ ){
			AccountReportPanel panel = (AccountReportPanel)getComponentAt(i);
			panel.setAccount(acct);
		}
	}

	private JPanel getReportPanel(ReportType type,AbstractAccountData acct){
		if( type == ReportType.ALL_TRADES )
			return new TradePanel(acct);
		else if( type == ReportType.SHARES_HELD )
			return new SharesHeldPanel(acct);
		else if( type == ReportType.LOTS_HELD )
			return new LotsHeldPanel(acct);
		else if( type == ReportType.TAX_GAINS )
			return new GainPanel(acct);
		else if( type == ReportType.TAX_GAIN_LOTS )
			return new LotGainPanel(acct);
		else if( type == ReportType.GAINS_BY_SECURITY)
			return new SecurityGainsPanel(acct);
		else if( type == ReportType.GAINS_BY_YEAR )
			return new YearlyGainsPanel(acct);
		else{
			System.out.println("Error: unrecognized report type");
			return new JPanel();
		}
	}
}
