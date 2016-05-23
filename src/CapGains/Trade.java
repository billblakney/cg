package CapGains;

import java.util.*;
import java.math.BigDecimal;

/**
 * This base class encapsulates data common to buy and sell trades. BuyTrade and
 * SellTrade are derived from this class.
 */
public class Trade implements Comparable {

	public enum Type {
		BUY("Buy"), SELL("Sell");

		private final String name;

		private Type(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
		
		/*
		 * TODO Need to through exception if no match
		 */
		static public Type getEnumValue(String s){
			if( s.equals(BUY.toString() ))
				return BUY;
			else
				return SELL;
		}
	}

	public enum SpecialInstruction {
		NO_INSTRUCTION(""), NO_WASH("NO_WASH");

		private final String name;

		private SpecialInstruction(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
		
		/*
		 * TODO Need to through exception if no match
		 */
		static public SpecialInstruction getEnumValue(String s){
			if( s == null )
				return NO_INSTRUCTION;
			if( s.equals("NO_WASH") ) //zzz
				return NO_WASH;
			else
				return NO_INSTRUCTION;
		}
	}

	public int uID; // user ID - provided by user
	public String buySellId; // SecurityTrade List ID - should order trades in
	// security trade list
	public Long portID; // Account ID - should order trades in the account
	public SimpleDate date;
	public Type tradeType;
	public String ticker;
	public Long numShares;
	public Float sharePrice;
	public BigDecimal comm;
	public SpecialInstruction instruction;
	public String note;
	public String history = new String();

	public Long numSharesHeld;
	public Long numSharesSold;
	public String series;

	/**
	 * Share lots used to manage computations of cap gains.
	 */
	public LotSet lotSet = new LotSet();

	/**
	 * Default constructor.
	 */
	public Trade() {
	}

	/**
	 * Constructor that initializes all Trade data members. </p>
	 * 
	 * @param id
	 *            This parameter is reserved for future use.
	 * @param date
	 *            The date of this trade.
	 * @param tradeType
	 *            Either Trade.BUY or Trade.SELL.
	 * @param ticker
	 *            The ticker symbol, e.g. CSCO.
	 * @param numShares
	 *            The number of shares.
	 * @param sharePrice
	 *            Price per share.
	 * @param comm
	 *            Commision.
	 * @param note
	 *            A note to be entered by the user.
	 * TODO Need to better hide this constructor so only BuyTrade and SellTrade can use it.
	 */
	protected Trade(int id, CapGains.SimpleDate date, Trade.Type tradeType,
			String ticker, long numShares, float sharePrice, BigDecimal comm,
			Trade.SpecialInstruction instruction, String note) {

		this.uID = id;
		this.date = date;
		this.tradeType = tradeType;
		this.ticker = ticker;
		this.numShares = numShares;
		this.sharePrice = sharePrice;
		this.comm = comm;
		this.instruction = instruction;
		this.note = note;

		portID = new Long(0);
	}

	/**
	 * Comparison function for "natural order" of trades.
	 * 
	 * @param o
	 *            Trade to be compared to.
	 * @return Result of comparison.
	 */
	public int compareTo(Object o) {

		Trade otherTrade = (Trade) o;

		// First comparison is trade date.
		if (date.before(otherTrade.date) == true) {
			return -1;
		} else if (date.after(otherTrade.date) == true) {
			return 1;
		}

		// First tiebreaker is trade ID.
		// Note: Convert to integer for later integration with
		// the compareTo() functions of the trade vectors.
		if (uID < otherTrade.uID) {
			return -1;
		} else if (uID > otherTrade.uID) {
			return 1;
		}

		// Second tiebreaker is trade ticker.
		return ticker.compareTo(otherTrade.ticker);
	}

	public void appendToHistory(String txt) {
		history += txt;
	}

	public boolean isBuyTrade() {
		if (tradeType == Trade.Type.BUY)
			return true;
		else
			return false;
	}

	public boolean isSellTrade() {
		if (tradeType == Trade.Type.SELL)
			return true;
		else
			return false;
	}

	public boolean isOpenPosition() {
		return false;
	}

	TreeSet<Trade> getRelatedTrades() {
		return new TreeSet<Trade>();
	}

	/**
	 * Prints out the list of stockMgrIds.
	 */
	public void z_print() {
		System.out.println(toString());
	}

	@Override
	public String toString() {

		String buf = tradeType + "," + buySellId + "," + uID + "," + date + ","
				+ ticker + "," + numShares + "," + sharePrice + "," + comm
				+ "," + instruction + "," + note;
		return buf.toString();
	}
}
