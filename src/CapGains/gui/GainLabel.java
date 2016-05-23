package CapGains.gui;

import java.text.DecimalFormat;

import javax.swing.JLabel;

public class GainLabel extends JLabel {

	private DecimalFormat gainFormat = new DecimalFormat(
	"\u00A4###,###,##0;(\u00A4###,###,##0)");

	public void update(Integer value) {
		setText("Gain: " + gainFormat.format(value));
	}
}
