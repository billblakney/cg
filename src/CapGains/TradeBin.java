package CapGains;

import java.util.*;

public class TradeBin extends TreeSet<Trade> {

	SecurityTradeList trades;
	
	TradeBin(SecurityTradeList stl){
		this.trades = stl;
	}
	
	 // This routine expects a sell trade as input.
	boolean addTrade(Trade st){
		if( contains(st) )
			return true;
		if( st.isOpenPosition() )
			return false;
		Set<Trade> relatedTrades = st.getRelatedTrades();
		for( Trade rt: relatedTrades )
			if( addTrade(rt) == false )
				return false;
		labelTradeSeries(st);
		return true;
	}
	
	private void labelTradeSeries(Trade st){
		for( Trade t: this )
			t.series = st.buySellId;
	}
}
