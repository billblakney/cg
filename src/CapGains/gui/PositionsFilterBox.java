package CapGains.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class PositionsFilterBox extends JComboBox {

	private final String ALL_TRADES_STRING = "All Trades";
	private final String OPEN_POSITIONS_ONLY_STRING = "Open Positions Only";
	private final String options[] = { ALL_TRADES_STRING, OPEN_POSITIONS_ONLY_STRING};
	
	public PositionsFilterBox(){
		DefaultComboBoxModel model = new DefaultComboBoxModel(options);
		setModel(model);
	}
	
	/**
	 * @return Returns true if "Open Positions Only" is selected, or false for "All Trades". 
	 */
	public boolean onlyShowOpenPositions(){
		String item = (String)getSelectedItem();
		if( item.equals(OPEN_POSITIONS_ONLY_STRING) )
			return true;
		else
			return false;
	}
}
