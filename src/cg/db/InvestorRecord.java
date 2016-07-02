package cg.db;

/**
 * Class representing a row of the investor table.
 */
public class InvestorRecord
{
   public Integer _investorId;
	public String  _name;

	public InvestorRecord()
	{
	   _investorId = null;
	   _name = null;
	}
	
	public InvestorRecord(InvestorRecord aRecord)
	{
	   _investorId = new Integer(aRecord._investorId);
	   _name = new String(aRecord._name);
	}
}
