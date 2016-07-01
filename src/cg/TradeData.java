package cg;

import java.math.BigDecimal;

public class TradeData implements TradeDataProvider
{
   protected Integer    _tradeId;
   protected SimpleDate _date;
   protected OldTrade.Type _tradeType;
   protected String     _symbol;
   protected Integer    _numShares;
   protected Integer    _numSharesHeld;
   protected Integer    _numSharesSold;
   protected Float      _sharePrice;
   protected BigDecimal _commission;
   protected Integer    _claimedTaxYear;
   protected String     _note;

   @Override
   public Integer getTradeId()
   {
      return _tradeId;
   }

   @Override
   public SimpleDate getDate()
   {
      return _date;
   }

   @Override
   public OldTrade.Type getTradeType()
   {
      return _tradeType;
   }

   @Override
   public String getSymbol()
   {
      return _symbol;
   }

   @Override
   public Integer getNumShares()
   {
      return _numShares;
   }

   @Override
   public Integer getNumSharesHeld()
   {
      return _numSharesHeld;
   }

   @Override
   public Integer getNumSharesSold()
   {
      return _numSharesSold;
   }

   @Override
   public Float getSharePrice()
   {
      return _sharePrice;
   }

   @Override
   public BigDecimal getCommission()
   {
      return _commission;
   }

   @Override
   public Integer getClaimedTaxYear()
   {
      return _claimedTaxYear;
   }

   @Override
   public String getNote()
   {
      return _note;
   }

   public void set_tradeId(Integer _tradeId)
   {
      this._tradeId = _tradeId;
   }

   public void set_date(SimpleDate _date)
   {
      this._date = _date;
   }

   public void set_tradeType(OldTrade.Type _tradeType)
   {
      this._tradeType = _tradeType;
   }

   public void set_symbol(String _symbol)
   {
      this._symbol = _symbol;
   }

   public void set_numShares(Integer _numShares)
   {
      this._numShares = _numShares;
   }

   public void set_numSharesHeld(Integer _numSharesHeld)
   {
      this._numSharesHeld = _numSharesHeld;
   }

   public void set_numSharesSold(Integer _numSharesSold)
   {
      this._numSharesSold = _numSharesSold;
   }

   public void set_sharePrice(Float _sharePrice)
   {
      this._sharePrice = _sharePrice;
   }

   public void set_commission(BigDecimal _commission)
   {
      this._commission = _commission;
   }

   public void set_claimedTaxYear(Integer _claimedTaxYear)
   {
      this._claimedTaxYear = _claimedTaxYear;
   }

   public void set_note(String _note)
   {
      this._note = _note;
   }
}