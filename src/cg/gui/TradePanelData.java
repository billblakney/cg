package cg.gui;

import java.text.DecimalFormat;
import java.util.Vector;
import java.util.Collections;
import java.util.Comparator;
import java.text.DecimalFormat;

import cg.*;

//TODO this class is obsolete?
public class TradePanelData {

	public TradePanelData() {
	}

	/**
	 * This class provides an ordering function for comparing trade vectors by
	 * date.
	 */
	class OrderTradeVectorsByDate implements Comparator {

		// compare date, then ticker, then id
		public int compare(Object p1, Object p2) {
			Vector v1 = (Vector) p1;
			Vector v2 = (Vector) p2;

			// First comparison is trade date.
			SimpleDate date1 = (SimpleDate) v1.elementAt(1);
			SimpleDate date2 = (SimpleDate) v2.elementAt(1);
			if (date1.before(date2) == true)
				return -1;
			else if (date2.before(date1) == true)
				return 1;

			// First tiebreaker is trade ID.
			Integer id1 = (Integer) v1.elementAt(0);
			Integer id2 = (Integer) v2.elementAt(0);
			if (id1.intValue() < id2.intValue())
				return -1;
			else if (id2.intValue() < id1.intValue())
				return 1;

			// Final tiebreaker is trade ticker.
			String ticker1 = (String) v1.elementAt(3);
			String ticker2 = (String) v2.elementAt(3);
			return ticker1.compareTo(ticker2);
		}
	}

	/**
	 * This class provides an ordering function for comparing trade vectors by
	 * date. It is used by the Sort.sort() method of the Xanthine library.
	 */
	class OrderTradeVectorsByTicker implements Comparator {

		// compare date, then ticker, then id
		public int compare(Object p1, Object p2) {
			Vector v1 = (Vector) p1;
			Vector v2 = (Vector) p2;

			// First comparison is trade ticker.
			String ticker1 = (String) v1.elementAt(3);
			String ticker2 = (String) v2.elementAt(3);
			int r = ticker1.compareTo(ticker2);
			if (r != 0) {
				return r;
			}

			// First tiebreaker is trade date.
			SimpleDate date1 = (SimpleDate) v1.elementAt(1);
			SimpleDate date2 = (SimpleDate) v2.elementAt(1);
			if (date1.before(date2) == true)
				return -1;
			else if (date2.before(date1) == true)
				return 1;

			// Final tiebreaker is trade ticker.
			Integer id1 = (Integer) v1.elementAt(0);
			Integer id2 = (Integer) v2.elementAt(0);
			if (id1.intValue() < id2.intValue())
				return -1;
			else if (id1.intValue() > id2.intValue())
				return 1;
			else
				return 0;
		}
	}

}
