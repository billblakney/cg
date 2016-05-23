package CapGains.gui;

import java.util.Vector;
import java.util.Collections;
import java.util.Comparator;

import CapGains.*;

public class GainPanelData {

	public GainPanelData() {
	}

	/**
	 * Comparator for sorting gain vectors by date.
	 */
	class OrderGainVectorsByDate implements Comparator {

		public int compare(Object p1, Object p2) {
			Vector v1 = (Vector) p1;
			Vector v2 = (Vector) p2;

			// First comparison is date.
			SimpleDate date1 = (SimpleDate) v1.elementAt(3);
			SimpleDate date2 = (SimpleDate) v2.elementAt(3);

			if (date1.before(date2) == true) {
				return -1;
			} else if (date2.before(date1) == true) {
				return 1;
			} else {
				String ticker1 = (String) v1.elementAt(1);
				String ticker2 = (String) v2.elementAt(1);
				/*
				 * !!! Integer id1 = (Integer)v1.elementAt(0); Integer id2 =
				 * (Integer)v2.elementAt(0); if( id1.intValue() < id2.intValue()
				 * ) return -1; else if( id2.intValue() < id1.intValue() )
				 * return 1; else
				 */
				return ticker1.compareTo(ticker2);
			}
		}
	}

	/**
	 * Comparator for sorting gain vectors by ticker.
	 */
	class OrderGainVectorsByTicker implements Comparator {

		// compare date, then ticker, then id
		public int compare(Object p1, Object p2) {
			Vector v1 = (Vector) p1;
			Vector v2 = (Vector) p2;

			String ticker1 = (String) v1.elementAt(1);
			String ticker2 = (String) v2.elementAt(1);

			int r = ticker1.compareTo(ticker2);
			if (r != 0) {
				return r;
			} else {
				SimpleDate date1 = (SimpleDate) v1.elementAt(3);
				SimpleDate date2 = (SimpleDate) v2.elementAt(3);

				if (date1.before(date2) == true) {
					return -1;
				} else if (date2.before(date1) == true) {
					return 1;
				} else {
					return 0;
					/*
					 * Integer id1 = (Integer)v1.elementAt(0); Integer id2 =
					 * (Integer)v2.elementAt(0);
					 * 
					 * if( id1.intValue() < id2.intValue() ) return -1; else
					 * return 1;
					 */
				}
			}
		}
	}
}
