package cg.db;

/**
 * Class representing a row of the broker table.
 */
public class BrokerRecord
{
   public Integer _brokerId;
	public String  _name;

	public BrokerRecord()
	{
	   _brokerId = null;
	   _name = null;
	}
	
	public BrokerRecord(BrokerRecord aRecord)
	{
	   _brokerId = new Integer(aRecord._brokerId);
	   _name = new String(aRecord._name);
	}
}
