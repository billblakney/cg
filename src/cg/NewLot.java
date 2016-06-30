package cg;

import cg.db.LotRecord;

//public interface OpenPositionAccessor
//{
//   public String     getSymbol();
//   public SimpleDate getBuyDate();
//   public Integer    getNumShares();
//   public Float      getBuyPrice();
//   public Term       getTerm();
//}
public class NewLot implements OpenPositionAccessor
{
   LotRecord lotRecord;
   Trade triggerTrade;
   Trade firstBuyTrade;
   Trade lastBuyTrade;
   Trade sellTrade;

   NewLot(LotRecord aLot)
   {
      lotRecord = aLot;

      DataStore tDataStore = DataStore.getInstance();
      triggerTrade = tDataStore.getTradeById(aLot._triggerTradeId);
      firstBuyTrade = tDataStore.getTradeById(aLot._firstBuyTradeId);
      lastBuyTrade = tDataStore.getTradeById(aLot._lastBuyTradeId);
      sellTrade = tDataStore.getTradeById(aLot._sellTradeId);
   }
   
   NewLot(NewLot aNewLot)
   {
      lotRecord = aNewLot.lotRecord;
      triggerTrade = aNewLot.triggerTrade;
      firstBuyTrade = aNewLot.firstBuyTrade;
      lastBuyTrade = aNewLot.lastBuyTrade;
      sellTrade = aNewLot.sellTrade;
   }

   @Override // OpenPositionAccessor
   public String getSymbol()
   {
      return triggerTrade.getSymbol();
   }

   @Override // OpenPositionAccessor
   public SimpleDate getBuyDate()
   {
      return firstBuyTrade.getDate();
   }

   @Override // OpenPositionAccessor
   public Integer getNumShares()
   {
      return lotRecord._numShares;
   }

   /**
    * TODO comment on meaning of buy price as basis price
    */
   @Override // OpenPositionAccessor
   public Float getBuyPrice()
   {
      return lotRecord._basis.floatValue()/(float)lotRecord._numShares;
   }

   /**
    * TODO
    */
   @Override // OpenPositionAccessor
   public Term getTerm()
   {
      return Term.MIXED;
   }
}
