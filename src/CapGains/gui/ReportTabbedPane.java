package CapGains.gui;

import javax.swing.JTabbedPane;
import javax.swing.*;
import CapGains.*;

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
	public void addReport(ReportType type, Account acct) {
		JPanel newPanel = getReportPanel(type, acct);
		addTab(type.toString(), newPanel);
		int index = indexOfComponent(newPanel);
		setTabComponentAt(index, new ButtonTabComponent(this));
	}
	
	/**
	 * Change the account displayed by all the report tabs.
	 * @param acct
	 */
	public void changeAccount(Account acct){
		int count = getTabCount();
		for( int i = 0; i < count; i++ ){
			AccountReportPanel panel = (AccountReportPanel)getComponentAt(i);
			panel.setAccount(acct);
		}
	}

	private JPanel getReportPanel(ReportType type,Account acct){
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
