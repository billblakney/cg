package cg;

public class OpenPosition implements OpenPositionAccessor
{
   protected String     _symbol;
   protected Integer    _numShares;
   protected SimpleDate _buyDate;
   protected Float      _buyPrice;
   protected Term       _term;

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
   public SimpleDate getBuyDate()
   {
      return _buyDate;
   }

   @Override
   public Float getBuyPrice()
   {
      return _buyPrice;
   }

   @Override
   public Term getTerm()
   {
      return _term;
   }

   public void set_symbol(String _symbol)
   {
      this._symbol = _symbol;
   }

   public void set_numShares(Integer _numShares)
   {
      this._numShares = _numShares;
   }

   public void set_buyDate(SimpleDate _buyDate)
   {
      this._buyDate = _buyDate;
   }

   public void set_buyPrice(float _buyPrice)
   {
      this._buyPrice = _buyPrice;
   }

   public void set_term(Term _term)
   {
      this._term = _term;
   }
}
