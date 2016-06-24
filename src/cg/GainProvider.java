package cg;

public interface GainProvider
{
   public String     getSymbol();
   public Integer    getNumShares();
   public SimpleDate getBuyDate();
   public Float      getBuyPrice();
   public SimpleDate getSellDate();
   public Float      getSellPrice();
   public Float      getProceeds();
   public Float      getBasis();
   public Float      getGain();
   public Term       getTerm();
}
