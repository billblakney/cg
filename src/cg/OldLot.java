package cg;

import util.LongCounter;

/**
 * This class encapsulates information for a lot.
 * A lot is a representation of the capital gains
 * associated with some or all shares of a single sell trade. For example,
 * if the all of the sold shares had been acquired on the same day at the
 * same price, then the total capital gains for that sell would be
 * represented by a single cap gains component. However, if the sold shares
 * had been acquired at different dates and prices, the sell would be
 * represented by a collection of cap gains components, each representing
 * the gain associated corresponding to a set of shares of the same price
 * purchased the same day.  The total cap gains for the sell trade in that
 * case would be the sum of the cap gains of all of the cap gains
 * components.
 * </p>
 * The SellTrade uses a Vector of GainComponents to store a set of cap
 * gain components.
 */
public class OldLot {

	public String id;
	public String sequentialID;

   /**
    * The index of the buy trade associated with this lot.
    * This is a trade index into the StockMgr that manages this security.
    */
//   public int buyTradeIndex;

   /**
    * The ticker associated with this lot.
    */
   public String ticker;

   /**
    * The date of the buy trade associated with this lot.
    */
   public SimpleDate buyDate;

   /**
    * The date of the sell trade associated with this lot.
    */
   public SimpleDate sellDate;

   /**
    * The share price of the buy trade associated with this lot.
    */
   public float buyPrice;

   /**
    * The share price of the sell trade associated with this lot.
    */
   public float sellPrice;

   /**
    * The number of shares associated with this lot.
    */
   public int numShares;

   /**
    * The basis for this lot.
    */
   public float basis;

   /**
    * The proceeds for this lot.
    */
   public float proceeds;

   /**
    * The net cap gain associated with this lot.
    */
   public float gain;

   /**
    * The net cap gain associated with this lot.
    */
   public Term term;

   public String message; // used by GainsCalculator to record/print info during
	// processing

	private LongCounter counter = new LongCounter();
	
	/**
	 * Creates a lot ID for a "fresh" buy lot, i.e. a buy lot that is not being
	 * created from a wash sale lot. In this case, the ID is a copy of the buy
	 * trade ID for which the lot is being created.
	 * Format: [<buy trade ID>]
	 */
	static public String createFreshBuyLotID(BuyTrade bt) {
		return bt.buySellId;
	}

	/**
	 * Creates a lot ID for a "wash" buy lot, i.e. a buy lot that is  being
	 * created from a wash sale lot. In this case, the ID is a copy of the 
	 * "wash sell lot" concatenated with the buy trade ID. Note that the wash
	 * sell lot ID may itself be a concatenation of various trade IDs.
	 * Format: [<wash sell lot ID>,<buy trade ID>]
	 */
	static public String createWashBuyLotID(String washLotID, BuyTrade bt) {
		return washLotID + "," + bt.buySellId;
	}

	/**
	 * Creates a lot ID for a sell lot In this case, the ID is a copy of the 
	 * buy lot ID concatenated with the sell trade ID. Note that the buy
	 * lot ID may itself be a concatenation of various trade IDs.
	 * Format: [<buy lot ID>,<sell trade ID>]
	 */
	static public String createSellLotID(String buyLotID, SellTrade st) {
		return buyLotID + "," + st.buySellId;
	}

   OldLot(){
   }
   
   /**
    * Constructor that specifies the information for this gain component.
    */
   OldLot(
                 String ticker,
                 SimpleDate buyDate,
                 SimpleDate sellDate,
                 float buyPrice,
                 float sellPrice,
                 int numShares,
                 float basis,
                 float proceeds,
                 float gain) {
      this.ticker = ticker;
//	  this.buyTradeIndex = buyTradeIndex;
      this.buyDate = buyDate;
      this.sellDate = sellDate;
      this.buyPrice = buyPrice;
      this.sellPrice = sellPrice;
      this.numShares = numShares;
      this.basis = basis;
      this.proceeds = proceeds;
      this.gain = gain;
      this.term = Term.computeTerm(buyDate,sellDate);
   }

	public OldLot(String ticker, String id, int numShares,
			SimpleDate buyDate, SimpleDate sellDate,
			float buyPrice, float sellPrice, float basis, float proceeds) {
		this.ticker = ticker;
		this.id = id;
		this.sequentialID = "Lot" + counter.getNext();
		this.numShares = numShares;
		this.buyDate = buyDate;
		this.sellDate = sellDate;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
		this.basis = basis;
		this.proceeds = proceeds;
		this.gain = proceeds - gain;
		this.term = Term.computeTerm(buyDate,sellDate);
	}
   
   public float getGain() {
	   return gain;
   }
	/**
	 * Builds a string representation of the Lot.
	 * 
	 * @return String
	 */
	public String toString() {
		return new String("   lot: [" + id + "]" + ",shares=" + numShares
				+ ",buyDate=" + buyDate + ",sellDate=" + sellDate
				+ ",buyPrice=" + buyPrice + ",sellPrice=" + sellPrice
				+ ",basis=" + basis + ",proceeds=" + proceeds + ",term=" + term);
	}
}
