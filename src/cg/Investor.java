package cg;

import cg.db.InvestorRecord;

public class Investor
{
   InvestorRecord investorRecord;
   
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
