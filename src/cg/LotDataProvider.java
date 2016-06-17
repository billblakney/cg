package cg;

import java.math.BigDecimal;

public interface LotDataProvider
{
   public String getSymbol();
   public SimpleDate getBuyDate();
   public Integer getNumShares();
   public float getBuyPrice();
   public Term getTerm();
//TODO rm
//   v.addElement(lot.ticker); //0
//   v.addElement(lot.buyDate); //1
//   v.addElement(lot.numShares); //2
//   v.addElement(lot.buyPrice); //3
//   v.addElement(lot.term); //4
}
