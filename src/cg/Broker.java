package cg;

import java.util.Vector;
import cg.db.BrokerRecord;

public class Broker
{
   BrokerRecord brokerRecord;
   
   public static Vector<Broker> getBrokers(Vector<BrokerRecord> aRecords)
   {
      Vector<Broker> tBrokers = new Vector<Broker>();
      
      for (BrokerRecord tRecord: aRecords)
      {
         tBrokers.add(new Broker(tRecord));
      }
      return tBrokers;
   }
   
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
