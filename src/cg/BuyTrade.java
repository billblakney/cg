package cg;

import java.math.BigDecimal;

/**
 * This class represents a buy trade. It extends Trade. The only functionality
 * added by this class relates to the number of unsold shares. That value is
 * initialized by the constructor to match the number of shares in the trade,
 * and can be decremented via decrementUnsoldShares().
 */
public class BuyTrade extends Trade {

	public BuyTrade(int tradeId, cg.SimpleDate date, Trade.Type tradeType,
			String ticker, int numShares, float sharePrice, BigDecimal comm,
			Trade.SpecialInstruction instruction, String note) {

		super(tradeId, date, tradeType, ticker, numShares, sharePrice, comm,
				instruction, note);
	}

	/**
	 * This is a test.
	 * 
	 * @param t
	 *            Trade object
	 */

	public float getBasis() {
		return ((float) numShares) * ((float) sharePrice) + comm.floatValue();
	}

	public float getBasis(int numShares) {
		return ((float) numShares) / ((float) this.numShares) * getBasis();
	}

	public boolean isOpenPosition() {
		if (numSharesHeld > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return super.toString() + ",numdSharesHeld = " + numSharesHeld;
	}
}
