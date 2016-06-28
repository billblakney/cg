package cg;

public class OpenPosition implements OpenPositionAccessor
{
   protected String     _symbol;
   protected Integer    _numShares;
   protected Integer    _firstBuySeqNum;
   protected SimpleDate _firstBuyDate;
   protected SimpleDate _lastBuyDate;
   protected Float      _basis;
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
      return _lastBuyDate;
   }

   @Override
   public Float getBuyPrice()
   {
      return _basis/(float)_numShares;
   }

   @Override
   public Term getTerm()
   {
      return _term;
   }

   public SimpleDate get_firstBuyDate()
   {
      return _firstBuyDate;
   }

   public int get_firstBuySeqNum()
   {
      return _firstBuySeqNum;
   }

   public void set_symbol(String _symbol)
   {
      this._symbol = _symbol;
   }

   public void set_numShares(Integer _numShares)
   {
      this._numShares = _numShares;
   }

   public void set_firstBuySeqNum(Integer _firstBuySeqNum)
   {
      this._firstBuySeqNum = _firstBuySeqNum;
   }

   public void set_firstBuyDate(SimpleDate _firstBuyDate)
   {
      this._firstBuyDate = _firstBuyDate;
   }

   public void set_lastBuyDate(SimpleDate _lastBuyDate)
   {
      this._lastBuyDate = _lastBuyDate;
   }

   public void set_basis(float _basis)
   {
      this._basis = _basis;
   }

   public void set_term(Term _term)
   {
      this._term = _term;
   }
}
