package cg.gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import cg.*;
import ExcelAdapter.*;

/**
 * <p>
 * Title: GainPanel
 * </p>
 */
public class GainPanel extends AccountReportPanel implements ActionListener {

	// GUI components
	GainTable table;

	/*
	 * Selection box for filtering by ticker. The list of selectable tickers is
	 * provided by the displayed account.
	 */
	TickerFilterBox tickerFilterBox;

	/*
	 * Selection box for filtering by year. The list of selectable years is
	 * provided by the displayed account.
	 */
	YearFilterBox yearFilterBox;

	/*
	 * Label to display total gain of displayed gain components.
	 */
	private GainLabel gainLabel;


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
	public GainPanel() {
		init();
	}

	public GainPanel(AbstractAccountData acct) {
		init();
		setAccount(acct);
	}

	private void init(){
		// create title
		JLabel gainTitle = new JLabel("Cap Gains Table");

		// create the ticker filter combo box
		tickerFilterBox = new TickerFilterBox();
		tickerFilterBox.addActionListener(this);

		// create the year filter combo box
		yearFilterBox = new YearFilterBox();
		yearFilterBox.addActionListener(this);

		// create the top panel, add items
		JPanel topPanel = new JPanel();
		topPanel.add(gainTitle);
		topPanel.add(tickerFilterBox);
		topPanel.add(yearFilterBox);

		// create the center panel
		table = new GainTable();
		JScrollPane gainTablePane = new JScrollPane(table);

		// create the widgets for the bottom panel
		gainLabel = new GainLabel();
		GainTable.TotalGain totalGain = table.getTotalGain();
		totalGain.addObserver(this);

		// create the bottom panel, add items
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(gainLabel);

		// setup and populate the layout mgr
		setLayout(new BorderLayout());
		add(topPanel, "North");
		add(gainTablePane, "Center");
		add(bottomPanel, "South");

		ExcelAdapter ea = new ExcelAdapter(table);
	}

	/**
	 * updateReplace the currently displayed account with a new one.
	 */
	protected void updatePanel(AbstractAccountData acct) {
		Vector gains = acct.getTaxGains(null,null); //zzz
		table.setRows(gains);
		tickerFilterBox.update(acct);
		yearFilterBox.update(acct);
	}

	/**
	 * Handle the update notification from AbstractAccountData.
	 */
	public void update(Observable ov, Object obj) {

		if( ov instanceof AbstractAccountData){
			updatePanel((AbstractAccountData)ov);
		}
		else if( ov.getClass() == GainTable.TotalGain.class ){
			gainLabel.update((Integer)obj);
		}
	}

	/**
	 * ActionEvent handler.
	 */
	public void actionPerformed(ActionEvent e) {
		JComboBox box = (JComboBox) e.getSource();

		// handle filter by ticker requests
		if (box == tickerFilterBox) {
			TickerFilterBox tickerbox = (TickerFilterBox)box;
			table.filterOnTicker(tickerbox.getSelectedTicker());
		}
		// handle filter by year requests
		else if (box == yearFilterBox) {
			YearFilterBox yearbox = (YearFilterBox)box;
			table.filterOnYear(yearbox.getSelectedYear());
		}
	}
}
