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
	ReportSelectorPanel(ReportTabbedPane pane,Vector<String> tAccounts)
	{
		reportTabbedPane = pane;

		/*
		 * Create the panel label.
		 */
		JLabel treeTitle = new JLabel("Tree Table");
		
		/*
		 * Create the account combobox.
		 */
		JComboBox tComboBox = new JComboBox(tAccounts);
		
		/*
		 * Create the report tree and its scroll pane.
		 */
		reportTree = new ReportTree();
		reportTree.addTreeSelectionListener(this);

		JScrollPane treePane = new JScrollPane(reportTree);
		
		/*
		 * 
		 */
		JPanel tPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

      // settings for title
      c.gridx = 0;
      c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
//      c.insets = new Insets(5 + tXTop, 5+ tXLeft, 5, 5);
      tPanel.add(treeTitle, c);

      // settings for combobox
      c.gridx = 0;
      c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
//      c.insets = new Insets(5 + tXTop, 5+ tXLeft, 5, 5);
      tPanel.add(tComboBox, c);

      // settings for tree
      c.gridx = 0;
      c.gridy = 2;
		c.fill = GridBagConstraints.VERTICAL;
//      c.insets = new Insets(5 + tXTop, 5+ tXLeft, 5, 5);
      tPanel.add(treePane, c);

      add(tPanel);
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
