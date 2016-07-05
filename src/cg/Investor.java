package cg;

import java.util.Vector;
import cg.db.InvestorRecord;

public class Investor
{
   InvestorRecord investorRecord;
   
   public static Vector<Investor> getInvestors(Vector<InvestorRecord> aRecords)
   {
      Vector<Investor> tInvestors = new Vector<Investor>();
      
      for (InvestorRecord tRecord: aRecords)
      {
         tInvestors.add(new Investor(tRecord));
      }
      return tInvestors;
   }
   
   public Investor(InvestorRecord aRecord)
   {
      investorRecord = aRecord;
   }

   public Integer getInvestorId()
   {
      return investorRecord._investorId;
   }
   
   public String getName()
   {
      return investorRecord._name;
   }
}
