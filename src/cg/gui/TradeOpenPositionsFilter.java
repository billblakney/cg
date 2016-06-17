package cg.gui;

import javax.swing.RowFilter;

public class TradeOpenPositionsFilter extends RowFilter<TradeTableModel,Integer> {

	   public boolean include(Entry<? extends TradeTableModel, ? extends Integer> entry) {
		   if( entry.getStringValue(2).startsWith("Buy") ){ // if its a buy trade
			   Integer sharesHeld = (Integer)entry.getValue(5);
			   if( sharesHeld > 0 )  // and some shares are held
				   return true;
			   else
				   return false;
		   }
		   else
			   return false;
	   }

}