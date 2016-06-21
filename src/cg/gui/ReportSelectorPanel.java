package cg.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import cg.AbstractAccountData;
import cg.AccountDataProvider;
import cg.DataStore;

public class ReportSelectorPanel extends JPanel implements
      TreeSelectionListener, ActionListener
{

   private ReportTabbedPane reportTabbedPane;

   private int _accountId;

   private JTree reportTree;

   private IdComboBox _accountComboBox;

   /**
    * TODO move
    */
	public void actionPerformed(ActionEvent e)
	{
		IdComboBox tComboBox = (IdComboBox) e.getSource();

		if (tComboBox == _accountComboBox)
		{
		   _accountId = _accountComboBox.getSelectedId();
		}
	}

   /**
    * Constructor that specifies the tabbed pane to be used to display requested
    * reports.
    * 
    * @param pane
    */
   ReportSelectorPanel(ReportTabbedPane pane, Map<Integer,String> tAccountNames)
   {
      reportTabbedPane = pane;

      /*
       * Create the panel label.
       */
      JLabel treeTitle = new JLabel("Tree Table");

      /*
       * Create the account combobox.
       */
      _accountComboBox = new IdComboBox(tAccountNames);
      _accountId = _accountComboBox.getSelectedId();
      _accountComboBox.addActionListener(this);

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
      // c.insets = new Insets(5 + tXTop, 5+ tXLeft, 5, 5);
      tPanel.add(treeTitle, c);

      // settings for combobox
      c.gridx = 0;
      c.gridy = 1;
      c.fill = GridBagConstraints.HORIZONTAL;
      // c.insets = new Insets(5 + tXTop, 5+ tXLeft, 5, 5);
      tPanel.add(_accountComboBox, c);

      // settings for tree
      c.gridx = 0;
      c.gridy = 2;
      c.fill = GridBagConstraints.VERTICAL;
      // c.insets = new Insets(5 + tXTop, 5+ tXLeft, 5, 5);
      tPanel.add(treePane, c);

      add(tPanel);
   }

   /*
    * Respond to the selection of a report tree item by creating and
    * displaying the report.
    */
   public void valueChanged(TreeSelectionEvent event)
   {
      /*
       * Get the selected report type.
       */
      String value = reportTree.getLastSelectedPathComponent().toString();
      System.out.println("selected tree node: " + value);

      ReportType type = ReportType.matchString(value);
      
      /*
       * Get the account.
       */
      DataStore tDataStore = DataStore.getInstance();
      AbstractAccountData tAccount =
            tDataStore.getAccountDataProvider(_accountId);
System.out.println("getting data for acct: " + _accountId + ", " + tAccount.getName());

      /*
       * Add the report to the report tabbed pane.
       */
      if (type != ReportType.NO_REPORT)
      {
         reportTabbedPane.addReport(type, tAccount);
      }
   }
}
