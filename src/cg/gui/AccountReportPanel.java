package cg.gui;

import javax.swing.JPanel;

import cg.Account;

import java.awt.Dimension;
import java.util.*;

public abstract class AccountReportPanel extends JPanel implements Observer {

	/**
	 * Replace the currently displayed account with a new one and register for account changes.
	 */
	public void setAccount(Account acct) {
		acct.addObserver(this);
		updatePanel(acct);
	}
	
	/**
	 * Update the panel to display the account.
	 */
	abstract protected void updatePanel(Account acct);

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(600, 350);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 450);
	}
}
