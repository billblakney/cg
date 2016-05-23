package CapGains.gui;

import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;

public class TradeIsOpenPositionFilter extends RowFilter<AbstractTableModel,Integer> {

	private boolean apply = true;
	
	public TradeIsOpenPositionFilter() {
	}
	
	public TradeIsOpenPositionFilter(boolean apply) {
		this.apply = apply;
	}
	
	public void setApply(boolean apply){
		this.apply = apply;
	}
	
	   public boolean include(Entry<? extends AbstractTableModel, ? extends Integer> entry) {
		   if( apply == false )
			   return true;
		   if( entry.getStringValue(TradeTableModel.COL_BUYSELL).startsWith("Buy") ){ // if its a buy trade
			   Long sharesHeld = (Long)entry.getValue(TradeTableModel.COL_SHARESHELD);
			   if( sharesHeld > 0 )  // and some shares are held
				   return true;
			   else
				   return false;
		   }
		   else
			   return false;
	   }

}