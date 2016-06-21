package cg;

import java.math.BigDecimal;

public interface TradeDataProvider
{
   public Integer getTradeId();
   public SimpleDate getDate();
   public Trade.Type getTradeType();
   public String getSymbol();
   public Integer getNumShares();
   public Integer getNumSharesHeld();
   public Integer getNumSharesSold();
   public Float getSharePrice();
   public BigDecimal getCommission();
   public Integer getClaimedTaxYear();
   public String getNote();
}
