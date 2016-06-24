package cg;

public class LotData implements LotDataProvider
{
   protected String     _symbol;
   protected SimpleDate _buyDate;
   protected Integer    _numShares;
   protected float      _buyPrice;
   protected Term       _term;

   public String getSymbol()
   {
      return _symbol;
   }

   public SimpleDate getBuyDate()
   {
      return _buyDate;
   }

   public Integer getNumShares()
   {
      return _numShares;
   }

   public Float getBuyPrice()
   {
      return _buyPrice;
   }

   public Term getTerm()
   {
      return _term;
   }

   public void set_symbol(String _symbol)
   {
      this._symbol = _symbol;
   }

   public void set_buyDate(SimpleDate _buyDate)
   {
      this._buyDate = _buyDate;
   }

   public void set_numShares(Integer _numShares)
   {
      this._numShares = _numShares;
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
