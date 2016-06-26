package cg.db;

import java.util.Vector;
import cg.TradeList;

//TODO not being used now
public interface CapGainsDB {

	public Vector<AccountRecord> getAccountInfoVector();
	public TradeList getTrades(int aAccountId);
}
