package cg;

public class GainData implements GainProvider
{
   protected String     _symbol;
   protected Integer    _numShares;
   protected SimpleDate _buyDate;
   protected Float      _buyPrice;
   protected SimpleDate _sellDate;
   protected Float      _sellPrice;
   protected Float       _basis;
   protected Float       _proceeds;
//   protected Float       _gain;
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
   public SimpleDate getSellDate()
   {
      return _sellDate;
   }

   @Override
   public Float getSellPrice()
   {
      return _sellPrice;
   }

   @Override
   public Float getBasis()
   {
      return _basis;
   }

   @Override
   public Float getProceeds()
   {
      return _proceeds;
   }

   @Override
   public Float getGain()
   {
      return _proceeds - _basis;
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

   public void set_sellDate(SimpleDate _sellDate)
   {
      this._sellDate = _sellDate;
   }

   public void set_sellPrice(float _sellPrice)
   {
      this._sellPrice = _sellPrice;
   }

   public void set_basis(float _basis)
   {
      this._basis = _basis;
   }

   public void set_proceeds(float _proceeds)
   {
      this._proceeds = _proceeds;
   }

   public void set_term(Term _term)
   {
      this._term = _term;
   }
}
