package cg;

import java.math.BigDecimal;

public class Trade implements TradeDataProvider
{
   TradeRecord tradeRecord;
   
   /*
    * Constructor.
    */
   Trade(TradeRecord aRecord)
   {
      tradeRecord = aRecord;
   }
   
   /*
    * Constructor.
    */
   Trade(Trade aTradeData)
   {
      tradeRecord = new TradeRecord(aTradeData.tradeRecord);
   }

   @Override
   public Integer getTradeId()
   {
      return tradeRecord._tradeId;
   }

   @Override
   public SimpleDate getDate()
   {
      return tradeRecord._date;
   }

   @Override
   public OldTrade.Type getTradeType()
   {
      return tradeRecord._tradeType;
   }

   @Override
   public String getSymbol()
   {
      return tradeRecord._symbol;
   }

   @Override
   public Integer getNumShares()
   {
      return tradeRecord._numShares;
   }

   @Override
   public Integer getNumSharesHeld()
   {
      return tradeRecord._numSharesHeld;
   }

   @Override
   public Integer getNumSharesSold()
   {
      return tradeRecord._numSharesSold;
   }

   @Override
   public Float getSharePrice()
   {
      return tradeRecord._sharePrice;
   }

   @Override
   public BigDecimal getCommission()
   {
      return tradeRecord._commission;
   }

   @Override
   public Integer getClaimedTaxYear()
   {
      return tradeRecord._claimedTaxYear;
   }

   @Override
   public String getNote()
   {
      return tradeRecord._note;
   }

//TODO rm after final decision
//   public void set_date(SimpleDate _date)
//   {
//      this._date = _date;
//   }
//
//   public void set_tradeType(OldTrade.Type _tradeType)
//   {
//      this._tradeType = _tradeType;
//   }
//
//   public void set_symbol(String _symbol)
//   {
//      this._symbol = _symbol;
//   }
//
//   public void set_numShares(Integer _numShares)
//   {
//      this._numShares = _numShares;
//   }
//
//   public void set_numSharesHeld(Integer _numSharesHeld)
//   {
//      this._numSharesHeld = _numSharesHeld;
//   }
//
//   public void set_numSharesSold(Integer _numSharesSold)
//   {
//      this._numSharesSold = _numSharesSold;
//   }
//
//   public void set_sharePrice(Float _sharePrice)
//   {
//      this._sharePrice = _sharePrice;
//   }
//
//   public void set_commission(BigDecimal _commission)
//   {
//      this._commission = _commission;
//   }
//
//   public void set_claimedTaxYear(Integer _claimedTaxYear)
//   {
//      this._claimedTaxYear = _claimedTaxYear;
//   }
//
//   public void set_note(String _note)
//   {
//      this._note = _note;
//   }
}