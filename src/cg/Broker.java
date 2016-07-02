package cg;

import cg.db.BrokerRecord;

public class Broker
{
   BrokerRecord brokerRecord;
   
   public Broker(BrokerRecord aRecord)
   {
      brokerRecord = aRecord;
   }

   public Integer getBrokerId()
   {
      return brokerRecord._brokerId;
   }
   
   public String getName()
   {
      return brokerRecord._name;
   }
}
