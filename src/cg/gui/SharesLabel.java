package cg.gui;

import java.text.DecimalFormat;

import javax.swing.JLabel;

public class SharesLabel extends JLabel {

	private DecimalFormat numSharesFormat = new DecimalFormat(
	"###,###,###,##0");

	public void update(Long value) {
		setText("Shares: " + numSharesFormat.format(value));
	}
}
