package cg;

import java.math.BigDecimal;

public interface LotDataProvider
{
   public String     getSymbol();
   public SimpleDate getBuyDate();
   public Integer    getNumShares();
   public Float      getBuyPrice();
   public Term       getTerm();
}
