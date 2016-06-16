package cg;

import java.math.BigDecimal;
//import util.LongCounter;

/**
 */
public class Lot {

	public int lotId;
	public int parentId;
	public int triggerTradeId;
	public int buyTradeId;
	public int sellTradeId;
	public int numShares;
	public BigDecimal basis;
	public BigDecimal proceeds;
	public int createState; //TODO enum
	//public int term; //TODO enum
	//public String note;


//	private LongCounter counter = new LongCounter();
//	
//	/**
//	 * Creates a lot ID for a "fresh" buy lot, i.e. a buy lot that is not being
//	 * created from a wash sale lot. In this case, the ID is a copy of the buy
//	 * trade ID for which the lot is being created.
//	 * Format: [<buy trade ID>]
//	 */
//	static public String createFreshBuyLotID(BuyTrade bt) {
//		return bt.buySellId;
//	}
//
//	/**
//	 * Creates a lot ID for a "wash" buy lot, i.e. a buy lot that is  being
//	 * created from a wash sale lot. In this case, the ID is a copy of the 
//	 * "wash sell lot" concatenated with the buy trade ID. Note that the wash
//	 * sell lot ID may itself be a concatenation of various trade IDs.
//	 * Format: [<wash sell lot ID>,<buy trade ID>]
//	 */
//	static public String createWashBuyLotID(String washLotID, BuyTrade bt) {
//		return washLotID + "," + bt.buySellId;
//	}
//
//	/**
//	 * Creates a lot ID for a sell lot In this case, the ID is a copy of the 
//	 * buy lot ID concatenated with the sell trade ID. Note that the buy
//	 * lot ID may itself be a concatenation of various trade IDs.
//	 * Format: [<buy lot ID>,<sell trade ID>]
//	 */
//	static public String createSellLotID(String buyLotID, SellTrade st) {
//		return buyLotID + "," + st.buySellId;
//	}
//
//   Lot(){
//   }
//   
//   /**
//    * Constructor that specifies the information for this gain component.
//    */
//   Lot(
//                 String ticker,
//                 SimpleDate buyDate,
//                 SimpleDate sellDate,
//                 float buyPrice,
//                 float sellPrice,
//                 long numShares,
//                 float basis,
//                 float proceeds,
//                 float gain) {
//      this.ticker = ticker;
////	  this.buyTradeIndex = buyTradeIndex;
//      this.buyDate = buyDate;
//      this.sellDate = sellDate;
//      this.buyPrice = buyPrice;
//      this.sellPrice = sellPrice;
//      this.numShares = numShares;
//      this.basis = basis;
//      this.proceeds = proceeds;
//      this.gain = gain;
//      this.term = Term.computeTerm(buyDate,sellDate);
//   }
//
//	public Lot(String ticker, String id, long numShares,
//			SimpleDate buyDate, SimpleDate sellDate,
//			float buyPrice, float sellPrice, float basis, float proceeds) {
//		this.ticker = ticker;
//		this.id = id;
//		this.sequentialID = "Lot" + counter.getNext();
//		this.numShares = numShares;
//		this.buyDate = buyDate;
//		this.sellDate = sellDate;
//		this.buyPrice = buyPrice;
//		this.sellPrice = sellPrice;
//		this.basis = basis;
//		this.proceeds = proceeds;
//		this.gain = proceeds - gain;
//		this.term = Term.computeTerm(buyDate,sellDate);
//	}
//   
//   public float getGain() {
//	   return gain;
//   }
//	/**
//	 * Builds a string representation of the Lot.
//	 * 
//	 * @return String
//	 */
//	public String toString() {
//		return new String("   lot: [" + id + "]" + ",shares=" + numShares
//				+ ",buyDate=" + buyDate + ",sellDate=" + sellDate
//				+ ",buyPrice=" + buyPrice + ",sellPrice=" + sellPrice
//				+ ",basis=" + basis + ",proceeds=" + proceeds + ",term=" + term);
//	}
}
