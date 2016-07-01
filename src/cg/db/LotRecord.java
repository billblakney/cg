package cg.db;

import java.math.BigDecimal;
import cg.SimpleDate;
//import util.IntegerCounter;

/**
 */
public class LotRecord
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
   public Integer    _firstBuyTradeId;
   public Integer    _lastBuyTradeId;
   public Integer    _sellTradeId;
   public int        _numShares;
   public BigDecimal _basis;
   public BigDecimal _proceeds;
   public State      _state;
   public SimpleDate _closeDate;
	//public int term; //TODO enum
	//public String note;

	public LotRecord()
	{
	   _lotId = null;
	   _parentId = null;
	   _hasChildren = false;
	   _triggerTradeId = null;
	   _firstBuyTradeId = null;
	   _lastBuyTradeId = null;
	   _sellTradeId = null;
	   _numShares = 0;
	   _basis = new BigDecimal(0.0);
	   _proceeds = new BigDecimal(0.0);
	   _state = State.eOpen;
	   _closeDate = null;
	}
	
	public LotRecord(LotRecord tLot)
	{
	   _lotId = new Integer(tLot._lotId);
	   _parentId = new Integer(tLot._parentId);
	   _hasChildren = tLot._hasChildren;
	   _triggerTradeId = new Integer(tLot._triggerTradeId);
	   _firstBuyTradeId = new Integer(tLot._firstBuyTradeId);
	   _lastBuyTradeId = new Integer(tLot._lastBuyTradeId);
	   _sellTradeId = new Integer(tLot._sellTradeId);
	   _numShares = tLot._numShares;
	   _basis = new BigDecimal(tLot._basis.toString()); //TODO verify works
	   _proceeds = new BigDecimal(tLot._proceeds.toString()); //TODO verify works
	   _state = tLot._state;
	   _closeDate = tLot._closeDate;
	}
}
