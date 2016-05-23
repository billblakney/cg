package CapGains.db;

import java.util.Vector;

import CapGains.AccountInfo;
import CapGains.TradeList;

public interface CapGainsDB {

	public Vector<AccountInfo> getAccountInfoVector();
	public TradeList getTrades(String short_account_name);
}
