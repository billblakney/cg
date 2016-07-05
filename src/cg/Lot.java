package cg;

import java.util.Vector;
import cg.db.LotRecord;

//public interface OpenPositionAccessor
//{
//   public String     getSymbol();
//   public SimpleDate getBuyDate();
//   public Integer    getNumShares();
//   public Float      getBuyPrice();
//   public Term       getTerm();
//}
public class Lot implements OpenPositionAccessor
{
   LotRecord lotRecord;
   OldTrade triggerTrade;
   OldTrade firstBuyTrade;
   OldTrade lastBuyTrade;
   OldTrade sellTrade;
   
   public static Vector<LotRecord> getLotRecords(Vector<Lot> aLots)
   {
      Vector<LotRecord> tRecords = new Vector<LotRecord>();
      
      for (Lot tLot: aLots)
      {
         tRecords.add(tLot.lotRecord);
      }
      
      return tRecords;
   }

   public Lot(LotRecord aLot)
   {
      lotRecord = aLot;

      DataStore tDataStore = DataStore.getInstance();
      triggerTrade = tDataStore.getTradeById(aLot._triggerTradeId);
      firstBuyTrade = tDataStore.getTradeById(aLot._firstBuyTradeId);
      lastBuyTrade = tDataStore.getTradeById(aLot._lastBuyTradeId);
      sellTrade = tDataStore.getTradeById(aLot._sellTradeId);
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
