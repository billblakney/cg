package cg.db;

import java.util.Vector;

import cg.AccountInfo;
import cg.TradeList;

public interface CapGainsDB {

	public Vector<AccountInfo> getAccountInfoVector();
	public TradeList getTrades(int aAccountId);
}
