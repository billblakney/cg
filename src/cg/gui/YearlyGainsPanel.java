package cg.gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import cg.*;
import ExcelAdapter.*;

/**
 * <p>
 * Title: YearlyGainsPanel
 * </p>
 */
public class YearlyGainsPanel extends AccountReportPanel implements ActionListener {

	// GUI components
	YearlyGainsTable table;

	/*
	 * Label to display total gain of displayed yearly gains.
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
	public YearlyGainsPanel() {
		init();
	}

	public YearlyGainsPanel(AbstractAccountData acct) {
		init();
		setAccount(acct);
	}

	private void init() {
		// create title
		JLabel gainTitle = new JLabel("Yearly Gains Table");

		// create the top panel, add items
		JPanel topPanel = new JPanel();
		topPanel.add(gainTitle);

		// create the center panel
		table = new YearlyGainsTable();
		JScrollPane gainTablePane = new JScrollPane(table);

		// create the widgets for the bottom panel
		gainLabel = new GainLabel();
		YearlyGainsTable.TotalGain totalGain = table.getTotalGain();
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
		Vector gains = acct.getYearlyGains(); //zzz
		table.setRows(gains);
	}

	/**
	 * Handle the update notification from AbstractAccountData.
	 */
	public void update(Observable ov, Object obj) {

		if( ov instanceof AbstractAccountData){
			updatePanel((AbstractAccountData)ov);
		}
		else if( ov instanceof YearlyGainsTable.TotalGain){
			gainLabel.update((Integer)obj);
		}
	}

	/**
	 * ActionEvent handler.
	 */
	public void actionPerformed(ActionEvent e) {
		JComboBox box = (JComboBox) e.getSource();
	}
}