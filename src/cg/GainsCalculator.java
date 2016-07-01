package cg;

import java.util.Vector;
import java.util.Calendar;

/**
 * <p>Title: GainsCalculator</p>
 *
 * <p>Description: Computes capital gains for a series of trades of a
 * particular stock.</p>
 */
class GainsCalculator {

   private SecurityTradeList trades = null;

   public GainsCalculator(SecurityTradeList trades) {
      this.trades = trades;
   }

   /**
    * Compute cap gains for all SellTrades in this StockMgr. The basic
    * algorithm is to call computeTradeCapGains() for each sell trade.
    */
    void computeGains() {

      // Perform necessary initializations.
      trades.resetBuySellIds();
      trades.resetNumSharesHeld();
      trades.resetGains();
      trades.resetLots();

      // Perform calculations.
      setLots(true);
      setGains();
   }

   /**
    * Makes a pass through all trades to set lots.
    */
   private void setLots(boolean debug) {

	   // Loop over all trades, which are expected to be in order from oldest
	   // to newest.
       for (int i = 0; i < trades.size(); i++) {
          OldTrade t = (OldTrade)(trades.elementAt(i));

          //trades.clearLotMessages();          
          if ( t.isBuyTrade()) {
             BuyTrade bt = (BuyTrade)t;
             try {
                setLotsForBuyTrade(bt);
             }
             catch (Exception e) {
                System.out.println(
                "FATAL_ERROR: new_computeCapGains() -> setupBuyTradeLots()");
                System.exit(1);
             }
          }
          // for sell trades, compute the cap gains
          else {
             SellTrade st = (SellTrade)t;
             try {
                //setupSellTradeLots(st); should be obsolete
                setLotsForSellTrade(st);
             }
             catch (Exception e) {
                System.out.println(
                "FATAL_ERROR: new_computeCapGains() -> setupSellTradeLots()");
                System.exit(1);
             }
          }
      }
   }

   /**
    * Sets lots for a sell trade.
    */
   private void setLotsForSellTrade(SellTrade st){

      LotSet sLotSet = st.lotSet;
      LotSet bLotSet = null;

      // Pull buy lots forward using first-in first-out rule.
      //
      // Note that st.numSharesSold is initially zero, and is incremented
      // by the amount of each lot brought forward each iteration through
      // this while loop.
      
      int sharesToProcess = st.numShares; // process all shares of this sell
      while ( sharesToProcess > 0 ){

         // Get the first buy trade that has remaining buy lots.
         OldTrade t = trades.getFirstTradeWithLots(OldTrade.Type.BUY);
         BuyTrade bt = (BuyTrade)t;

         // Get the first buy lot available.
         bLotSet = bt.lotSet;
         OldLot bLot = bLotSet.getFirstLot();


         // Figure out how many shares and how much _basis will be transferred
         // from the buy lot to the new sell lot.
         int n;  // num shares to be transferred
         float b; // _basis to be transferred
         float p; // _proceeds to be transferred

         if ( sharesToProcess >= bLot.numShares){ // can transfer the entire buy lot
             n = bLot.numShares;
             b = bLot.basis;
             p = bLot.proceeds;
        }
         else{ // can transfer some of the buy lot
             n = sharesToProcess;
             float frac = ((float)sharesToProcess) / ((float)bLot.numShares);
             b = frac * bLot.basis;
             p = frac * bLot.proceeds;
         }

         // Create a new sell lot to hold the shares from the buy lot,
         // and add it to the sell lot.
         //
         String sLotID = OldLot.createSellLotID(bLot.id,st);
         OldLot sLot = new OldLot(
            st.ticker,             // ticker
            sLotID,                // id
            n           ,          // _numShares
            bLot.buyDate,          // buyDate
            st.date,               // sellDate
            bt.sharePrice,         // buyPrice
            st.sharePrice,         // sellPrice
            b,                     // _basis
            st.getProceeds(n) + p  // _proceeds
            );
sLot.message = "new sell lot while processing sell trade";
         sLotSet.addLot(sLot);

         // Remove the shares and corresponding _basis from the buy lot.
         // The call to removeFirstShares(n) will also remove the entire
         // first lot if it was emptied.
         //--System.out.println("going to remove first shares");
         bLot.basis -= b;
         bLot.proceeds -= p;
bLot.message = "removed shares from buy lot while processing sell trade"; // will not show for removed lots
         bLotSet.removeFirstShares(n);

         bt.numSharesHeld -= n;
         st.numSharesSold += n;
         bt.appendToHistory("   " + n + " shares moved to " + st.buySellId + "\n");

         sharesToProcess -= n;
         
      } // end while
   } // end routine
/*
   /**
    * Sets lots for a buy trade.
    *
    * @todo Thoroughly test the loss lot logic.
    */
   private void setLotsForBuyTrade(BuyTrade bt){

      // Set sharesToProcess to the total shares in this buy trade.
      int buySharesToProcess = bt.numShares;

      // Get a reference to the buy lot set we will be loading.
      LotSet bLotSet = bt.lotSet;

      /***********************************************************************
       * Buy lots built from wash sell lots.
       * (This code is NOT yet in working order wrt _basis/_proceeds.)
       * (2/16 - Looks like it is working now. Need to test. zzz)
       **********************************************************************/
      // Get all sell trades within 30 days of this buy trade.
      Vector<SellTrade> washableTrades = getPotentialWashSellTrades(bt);
//System.out.println("WASHABLE TRADES: " + washableTrades.size());
      for( int i = 0; i < washableTrades.size(); i++ ){
         SellTrade st = washableTrades.elementAt(i);
         LotSet sLotSet = st.lotSet;

         // Continue processing while there are still more shares to process
         // and the sell trade has some lots left.
         OldLot sLot = sLotSet.getFirstLossLot();

         while( buySharesToProcess > 0 && (sLot != null) )  {
            // Figure out how many shares and how much _basis and _proceeds
            // will be transferred from the sell lot to the buy lot.

            // First assume that we'll be able to use the whole lot.
            int n = sLot.numShares;
            float b = sLot.basis;
            float p = sLot.proceeds;

            // Then adjust if we can't use the whole sell lot.
            if( buySharesToProcess < n){
               n = buySharesToProcess;
               b = ((float)n) / ((float)sLot.numShares) * b;
               p = ((float)n) / ((float)sLot.numShares) * p;
            }
            // Create a new buy lot to hold the shares from the sell lot,
            // and add it to the buy lot.
            String washLotID = OldLot.createWashBuyLotID(sLot.id,bt);
            OldLot bLot = new OldLot(
               bt.ticker,          // ticker
               washLotID,          // id
               n,                  // _numShares
               sLot.buyDate,       // buyDate
               null,               // sellDate
               bt.sharePrice,      // buyPrice
               (float)0.0,         // sellPrice
               bt.getBasis(n) + b, // carry forward _basis
               p                   // carry forward _proceeds
               );
bLot.message = "new sell lot from wash sale while processing buy trade";
            bLotSet.addLot(bLot);

            // Remove the shares and corresponding _basis and _proceeds from the
            // sell lot.
            //--System.out.println("going to remove first shares");
            sLot.basis -= b;
            sLot.proceeds -= p;
            sLotSet.removeFirstShares(n);
sLot.message = "removed shares from wash sale lot while processing buy trade";

            //st.numSharesSold += n; they're not sold, just moved forward
            bt.numSharesHeld += n;
            st.appendToHistory("   " + n + " shares moved to " + bt.buySellId + "\n");

            buySharesToProcess -= n;

            sLot = sLotSet.getFirstLossLot();
         } // end while - no more lots left

         // No need to look to the next sell trade if all buy shares are
         // already processed.
         if( buySharesToProcess < 1 )
            break;

      } // end for washableTrades

      /***********************************************************************
       * Brand new buy lots.
       * (This code is in working order.)
       **********************************************************************/
      // Put any remaining (non-wash) shares in a new lot.
      if( buySharesToProcess > 0 ){
         // Create one lot to hold the remaining shares.
          String freshBuyLotID = OldLot.createFreshBuyLotID(bt);
         OldLot bLot = new OldLot(
            bt.ticker,          // ticker
        	freshBuyLotID,           // id
            buySharesToProcess,      // _numShares
            bt.date,                 // buyDate
            null,                    // sellDate
            bt.sharePrice,           // buyPrice
            (float)0.0,              // sellPrice
            bt.getBasis(buySharesToProcess),  // _basis
            (float)0.0                        // _proceeds
            );
bLot.message = "new buy lot while processing buy trade";

         // Add the lot to the lot set.
         bLotSet.addLot(bLot);
         bt.numSharesHeld += buySharesToProcess;
      } // end if
   } // end method

   /**
    * Iterates through all trades to compute tax gains for all sell trades.
    */
   private void setGains(){

      for (int i = 0; i < trades.size(); i++) {

         OldTrade t = (OldTrade)(trades.elementAt(i));

         // No need to process buy trades.
         if ( t.isBuyTrade() )
            continue;

         // Need to process the sell trade.
         SellTrade st = (SellTrade)t;
         st.computeTaxGain();
     } // end for
   } // end method

   /**
    * Returns all sell trades within 30 days of the specified date,
    * excepting those that have the NO_WASH special instruction.
    *
    * @param date TradeDate
    * @return Vector
    */
   private Vector<SellTrade> getPotentialWashSellTrades(BuyTrade buyTrade){

      Vector<SellTrade> recentSellTrades = new Vector<SellTrade>();

// this wash sale code in general is not yet working, so normally disable it with 'false'
boolean develop_wash_sell_code = false;
if( develop_wash_sell_code == false )
return recentSellTrades;
      // Set firstWashDate to 30 days before the buyTrade.
      SimpleDate firstWashDate = (SimpleDate)(buyTrade.date.clone());
      firstWashDate.add(Calendar.DATE, -30);

      // Set lastWashDate to 30 days before the buyTrade.
      SimpleDate lastWashDate = (SimpleDate)(buyTrade.date.clone());
      lastWashDate.add(Calendar.DATE, 30);

      // Loop through all trades up to buyTrade, and put any sell
      // trades with a negative capital gain and a date later than washDate
      //  into recentSellTrades.
      for (int i = 0; i < trades.size(); i++) {

         OldTrade t = trades.elementAt(i);

         // Pass over trades outside of the 30 day window.
         if( t.date.before(firstWashDate) == true )
            continue;

         // Pass over trades with the NO_WASH instruction..
         if( t.instruction == OldTrade.SpecialInstruction.NO_WASH ){
            continue;
         }

         // We're done if we're into trades beyond the 30 day window.
         if( t.date.after(lastWashDate) == true )
            break;

         // We're only looking for sell trades with negative gains.
         if ( ( (OldTrade) (trades.elementAt(i))).isSellTrade() ){
            SellTrade sellTrade = (SellTrade)trades.elementAt(i);
            recentSellTrades.add(sellTrade);
         } // end if
      } // end for
      return recentSellTrades;
   } // end method
}
