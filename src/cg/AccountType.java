package cg;

import java.util.Vector;
import cg.db.AccountTypeRecord;

public class AccountType
{
   AccountTypeRecord accountTypeRecord;
   
   public static Vector<AccountType> getAccountTypes(Vector<AccountTypeRecord> aRecords)
   {
      Vector<AccountType> tAccountTypes = new Vector<AccountType>();
      
      for (AccountTypeRecord tRecord: aRecords)
      {
         tAccountTypes.add(new AccountType(tRecord));
      }
      return tAccountTypes;
   }
   
   public AccountType(AccountTypeRecord aRecord)
   {
      accountTypeRecord = aRecord;
   }
   
   public Integer getAccountTypeId()
   {
      return accountTypeRecord._accountTypeId;
   }
   
   public boolean getIsTaxable()
   {
      return accountTypeRecord._isTaxable;
   }
   
   public String getLabel()
   {
      return accountTypeRecord._label;
   }
}
