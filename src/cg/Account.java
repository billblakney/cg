package cg;

import java.util.Vector;
import java.util.LinkedHashMap;
import cg.db.AccountRecord;

public class Account
{

   AccountRecord accountRecord;
   AccountType accountType;
   Investor investor;
   Broker broker;
   
   static public Vector<Account> getAccounts(Vector<AccountRecord> aRecords)
   {
      Vector<Account> tAccounts = new Vector<Account>();

      for (AccountRecord tRecord: aRecords)
      {
         tAccounts.add(new Account(tRecord));
      }

      return tAccounts;
   }
   
   static public LinkedHashMap<Integer,String> getAccountNameMap(Vector<Account> aAccounts)
   {
      LinkedHashMap<Integer,String> tMap = new LinkedHashMap<Integer,String>();

      for (Account tAccount: aAccounts)
      {
         Integer tAcctId = tAccount.accountRecord.acct_id;
         String tName = tAccount.accountRecord.name;
         tMap.put(tAcctId,tName);
      }

      return tMap;
   }
   
   public Account(AccountRecord aAccount)
   {
      accountRecord = aAccount;

      DataStore tDataStore = DataStore.getInstance();
      accountType = tDataStore.getAccountTypeById( accountRecord.acct_type_id);
      investor = tDataStore.getInvestorById(accountRecord.investor_id);
      broker = tDataStore.getBrokerById(accountRecord.broker_id);
   }
}
