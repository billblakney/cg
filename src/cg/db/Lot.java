package cg.db;

import java.math.BigDecimal;
//import util.IntegerCounter;

/**
 */
public class Lot
{
	public enum State
	{
		eOpen("Open"), eClosed("Closed");

		private final String name;

		private State(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
		
		/*
		 * TODO Need to throw exception if no match.
		 */
		static public State getEnumValue(String s)
		{
			if( s.equals(eOpen.toString() ))
				return eOpen;
			else
				return eClosed;
		}
	}
	
   public Integer    _lotId;
   public Integer    _parentId;
   public boolean    _hasChildren;
   public Integer    _triggerTradeId;
   public Integer    _buyTradeId;
   public Integer    _sellTradeId;
   public int        _numShares;
   public BigDecimal _basis;
   public BigDecimal _proceeds;
   public State      _state;
	//public int term; //TODO enum
	//public String note;

	Lot()
	{
	   _lotId = null;
	   _parentId = null;
	   _hasChildren = false;
	   _triggerTradeId = null;
	   _buyTradeId = null;
	   _sellTradeId = null;
	   _numShares = 0;
	   _basis = new BigDecimal(0.0);
	   _proceeds = new BigDecimal(0.0);
	   _state = State.eOpen;
	}
	
	Lot(Lot tLot)
	{
	   _lotId = new Integer(tLot._lotId);
	   _parentId = new Integer(tLot._parentId);
	   _hasChildren = tLot._hasChildren;
	   _triggerTradeId = new Integer(tLot._triggerTradeId);
	   _buyTradeId = new Integer(tLot._buyTradeId);
	   _sellTradeId = new Integer(tLot._sellTradeId);
	   _numShares = tLot._numShares;
	   _basis = new BigDecimal(tLot._basis.toString()); //TODO verify works
	   _proceeds = new BigDecimal(tLot._proceeds.toString()); //TODO verify works
	   _state = tLot._state;
	}


//	private IntegerCounter counter = new IntegerCounter();
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
//                 long _numShares,
//                 float _basis,
//                 float _proceeds,
//                 float gain) {
//      this.ticker = ticker;
////	  this.buyTradeIndex = buyTradeIndex;
//      this.buyDate = buyDate;
//      this.sellDate = sellDate;
//      this.buyPrice = buyPrice;
//      this.sellPrice = sellPrice;
//      this.numShares = _numShares;
//      this.basis = _basis;
//      this.proceeds = _proceeds;
//      this.gain = gain;
//      this.term = Term.computeTerm(buyDate,sellDate);
//   }
//
//	public Lot(String ticker, String id, long _numShares,
//			SimpleDate buyDate, SimpleDate sellDate,
//			float buyPrice, float sellPrice, float _basis, float _proceeds) {
//		this.ticker = ticker;
//		this.id = id;
//		this.sequentialID = "Lot" + counter.getNext();
//		this.numShares = _numShares;
//		this.buyDate = buyDate;
//		this.sellDate = sellDate;
//		this.buyPrice = buyPrice;
//		this.sellPrice = sellPrice;
//		this.basis = _basis;
//		this.proceeds = _proceeds;
//		this.gain = _proceeds - gain;
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
//		return new String("   lot: [" + id + "]" + ",shares=" + _numShares
//				+ ",buyDate=" + buyDate + ",sellDate=" + sellDate
//				+ ",buyPrice=" + buyPrice + ",sellPrice=" + sellPrice
//				+ ",_basis=" + _basis + ",_proceeds=" + _proceeds + ",term=" + term);
//	}
}
