package cg;

import java.util.*;
/**
 * <p>Title: LotSet</p>
 *
 * <p>Description: Models a set of lots that "belong" to a trade. A lot
 * set is used by the algorithm that computes capital gains for a set of
 * trades.</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author Bill Blakney
 * @version 1.0
 */
public class LotSet{

   /**
    * The total number of shares that belong to the lot set.
    */
   private long numShares;
   /**
    * The set of lots that belong to this lot set.
    */
   public Vector<OldLot> lots = new Vector<OldLot>();

   public LotSet() {
   }

   void addLot(OldLot lot){
      lots.addElement(lot);
      numShares += lot.numShares;
   }

   void clearLots(){
      lots.clear();
   }

   OldLot getFirstLot(){
      if( lots.isEmpty() == true )
         return null;
      else
         return lots.firstElement();
   }

   OldLot getFirstLossLot(){
      for( int i = 0; i < lots.size(); i++ ){
         OldLot lot = (OldLot)(lots.elementAt(i));
         if( lot.gain < 0.0 )
            return lot;
      }
      return null;
   }

   OldLot getLotAt(int i){
      return (OldLot)lots.elementAt(i);
   }

   int getNumLots(){
      return lots.size();
   }

   boolean hasLots(){
      if( lots == null )
         return false;
      else if( lots.isEmpty() == true )
         return false;
      else
         return true;
   }

   void removeFirstShares(long numShares){
      while( numShares > 0 ){
         OldLot lot = lots.firstElement();

         // On this pass, process no more shares than are in the 1st lot
         long n = numShares;
         if( numShares >= lot.numShares )
            n = lot.numShares;

         this.numShares -= n;
         lot.numShares -= n;
         if( lot.numShares == 0 )
            lots.removeElementAt(0);

         numShares -= n;
      } // end while
   } // end method

   void z_print(String header){
      if( header != null )
         System.out.println(header);
      for( int i = 0; i < lots.size(); i++ )
         System.out.println(lots.elementAt(i).toString());
   } // end method

} // end class
