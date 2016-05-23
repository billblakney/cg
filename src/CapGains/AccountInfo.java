package CapGains;

public class AccountInfo {
	public String shortname;
	public String longname;
	public String type;
	public String investor;
	public String broker;

	public AccountInfo() {
	}
	
	public String toString() {
		  return
		      "shortname = " + shortname + ";"
			+ "longname = " + longname + ";"
			+ "type = " + type + ";"
			+ "investor = " + investor + ";"
			+ "broker = " + broker + ";"
			+ "\n";
	}
}
