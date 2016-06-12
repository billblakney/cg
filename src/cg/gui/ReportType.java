package cg.gui;

public enum ReportType {
	NO_REPORT(""),
	ALL_TRADES("All Trades"),
	SHARES_HELD("Shares Held"),
	LOTS_HELD("Lots Held"),
	TAX_GAINS("Tax Gains"),
	TAX_GAIN_LOTS("Tax Gain Lots"),
	GAINS_BY_YEAR("Gains by Year"),
	GAINS_BY_SECURITY("Gains by Security");

	private final String name;

	private ReportType(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
	
	static public ReportType matchString(String reportType){
		if( reportType.equals(ALL_TRADES.toString()) )
			return ALL_TRADES;
		else if( reportType.equals(SHARES_HELD.toString()) )
			return SHARES_HELD;
		else if( reportType.equals(LOTS_HELD.toString()) )
			return LOTS_HELD;
		else if( reportType.equals(TAX_GAINS.toString()) )
			return TAX_GAINS;
		else if( reportType.equals(TAX_GAIN_LOTS.toString()) )
			return TAX_GAIN_LOTS;
		else if( reportType.equals(GAINS_BY_YEAR.toString()) )
			return GAINS_BY_YEAR;
		else if( reportType.equals(GAINS_BY_SECURITY.toString()) )
			return GAINS_BY_SECURITY;
		else
			return NO_REPORT;
	}
}
