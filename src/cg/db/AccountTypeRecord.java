package cg.db;

/**
 * Class representing a row of the accountType table.
 */
public class AccountTypeRecord
{
   public Integer _accountTypeId;
   public boolean _isTaxable;
	public String  _label;

	public AccountTypeRecord()
	{
	   _accountTypeId = null;
	   _isTaxable = true;
	   _label = null;
	}
	
	public AccountTypeRecord(AccountTypeRecord aRecord)
	{
	   _accountTypeId = new Integer(aRecord._accountTypeId);
	   _isTaxable = aRecord._isTaxable;
	   _label = new String(aRecord._label);
	}
}
