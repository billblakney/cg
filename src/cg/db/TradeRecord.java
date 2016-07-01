package cg.db;

import java.math.BigDecimal;
import cg.OldTrade;
import cg.SimpleDate;
import cg.OldTrade.Type;

public class TradeRecord
{
   public Integer    _tradeId;
   public SimpleDate _date;
   public OldTrade.Type _tradeType;
   public String     _symbol;
   public Integer    _numShares;
   public Integer    _numSharesHeld;
   public Integer    _numSharesSold;
   public Float      _sharePrice;
   public BigDecimal _commission;
   public Integer    _claimedTaxYear;
   public String     _note;
   
   public TradeRecord()
   {
   }
   
   public TradeRecord(TradeRecord aTrade)
   {
   _tradeId = aTrade._tradeId;
   _date = aTrade._date;
   _tradeType = aTrade._tradeType;
   _symbol = aTrade._symbol;
   _numShares = aTrade._numShares;
   _numSharesHeld = aTrade._numSharesHeld;
   _numSharesSold = aTrade._numSharesSold;
   _sharePrice = aTrade._sharePrice;
   _commission = aTrade._commission;
   _claimedTaxYear = aTrade._claimedTaxYear;
   _note = aTrade._note;
   }
}