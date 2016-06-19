package cg.gui;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import cg.*;

public class ReportSelectorPanel extends JPanel implements
		TreeSelectionListener {

	private ReportTabbedPane reportTabbedPane;

	private Account acct;

	private JTree reportTree;

	/**
	 * Constructor that specifies the tabbed pane to be used to display
	 * requested reports.
	 * 
	 * @param pane
	 */
	ReportSelectorPanel(ReportTabbedPane pane,Vector<String> tAccounts) {
		reportTabbedPane = pane;
		reportTree = new ReportTree();
		reportTree.addTreeSelectionListener(this);

		JScrollPane treePane = new JScrollPane(reportTree);

		JLabel treeTitle = new JLabel("Tree Table");
		
		JComboBox tComboBox = new JComboBox(tAccounts);
		JPanel tWorkAroundPanel = new JPanel();
		tWorkAroundPanel.add(tComboBox);

		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		add(treeTitle);
		add(tWorkAroundPanel);
		add(treePane);
//		add(Box.createVerticalGlue());
////		add(treeTitle, "North");
////		add(treePane, "Center");
	}

	/**
	 * This method is used to specify the account to be used for subsequent
	 * report requests (via the valueChanged method).
	 * 
	 * @param acct
	 */
	public void setAccount(Account acct) {
		this.acct = acct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
	 * .TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent event) {
		// Get the string value of the selected node.
		String value = reportTree.getLastSelectedPathComponent().toString();
		System.out.println("selected tree node: " + value);
		// If the user selected a report node, then the string value should
		// match a report type.
		// In that case, add the report to the tabbed pane.
		ReportType type = ReportType.matchString(value);
		if (type != ReportType.NO_REPORT)
			reportTabbedPane.addReport("TODO",type, acct);
	}
}
