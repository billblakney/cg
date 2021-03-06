package cg.gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import cg.*;
import ExcelAdapter.*;

/**
 * observer of a account.
 */
public class LotsHeldPanel extends AccountReportPanel implements ActionListener {

	// GUI components
	LotsHeldTable table;

	/*
	 * Selection box for filtering by ticker. The list of selectable tickers is
	 * provided by the displayed account.
	 */
	ColumnFilterComboBox tickerFilterBox;

	/*
	 * Selection box for filtering by year. The list of selectable years is
	 * provided by the displayed account.
	 */
	ColumnFilterComboBox termFilterBox;

	/*
	 * Label to display the total number of shares held.
	 */
	SharesLabel sharesLabel; // label at bottom of panel, with some info

	/**
	 * Constructor.
	 * 
	 * Algorithm Overview: The panel contains three areas: north, center, south.
	 * The north area is a panel, with title and sorting options. The center
	 * area is the table of trades. The south area is a panel with some summary
	 * info. The panels are build in the order listed above. First the
	 * components are built, then a panel is created and the components are
	 * added to it.
	 */
	public LotsHeldPanel() {
		init();
	}
	
	public LotsHeldPanel(AbstractAccountData acct) {
		init();
		setAccount(acct);
	}
	
	protected void init() {

		// Build the north panel.

		// create title
		final JLabel tradeTitle = new JLabel("LotsHeld Table");

		// Build the center panel.
		table = new LotsHeldTable();
		JScrollPane tradeTablePane = new JScrollPane(table);

		// create the ticker filter combo box
		tickerFilterBox = new ColumnFilterComboBox(
		      table,LotsHeldTableModel.COL_TICKER,"-All Symbols-");
		tickerFilterBox.addActionListener(this);

		// create term filter combo box
		termFilterBox = new ColumnFilterComboBox(
		      table,LotsHeldTableModel.COL_TERM,"-All Terms-");
		termFilterBox.addActionListener(this);
		
		// create the top panel, add items
		JPanel topPanel = new JPanel();
		topPanel.add(tradeTitle);
		topPanel.add(tickerFilterBox);
		topPanel.add(termFilterBox);

		// create the widgets for the bottom panel
		sharesLabel = new SharesLabel();
		SharesHeld sharesHeld = table.getSharesHeld();
		sharesHeld.addObserver(this);

		// create the bottom panel, add items
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(sharesLabel);

		// setup and populate the layout mgr
		setLayout(new BorderLayout());
		add(topPanel, "North");
		add(tradeTablePane, "Center");
		add(bottomPanel, "South");

		ExcelAdapter ea = new ExcelAdapter(table);
	}

	/**
	 * updateReplace the currently displayed account with a new one.
	 */
	protected void updatePanel(AbstractAccountData acct) {
		Vector<? extends OpenPositionAccessor> lots = acct.getOpenPositions(null,null); //TODO null,null?
		table.setRows(lots);
		tickerFilterBox.update();
		termFilterBox.update();
	}

	/**
	 * Handle the update notification from AbstractAccountData.
	 */
	public void update(Observable ov, Object obj) {

		if( ov instanceof AbstractAccountData ){
			updatePanel((AbstractAccountData)ov);
		}
		else if( ov instanceof SharesHeld){
			sharesLabel.update((Integer)obj);
		}
	}

	/**
	 * ActionEvent handler. Currently handles the filter selection for the
	 * trades table.
	 */
	public void actionPerformed(ActionEvent e) {
		JComboBox tComboBox = (JComboBox) e.getSource();

		// handle filter by ticker requests
		if (tComboBox == tickerFilterBox) {
			ColumnFilterComboBox tBox = (ColumnFilterComboBox)tComboBox;
			table.filterOnTicker(tBox.getFilter());
		}
		// handle filter by year requests
		else if (tComboBox == termFilterBox) {
			ColumnFilterComboBox tBox = (ColumnFilterComboBox)tComboBox;
			table.filterOnTerm(tBox.getFilter());
		}
	}
}
