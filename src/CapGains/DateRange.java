package CapGains;

public class DateRange {

	public SimpleDate beginDate = null;
	public SimpleDate endDate = null;

	public DateRange() {
	}

	public String toString() {
		if (endDate == null)
			return beginDate.toString();
		else
			return "various (" + beginDate.toString() + ","
					+ endDate.toString() + ")";
	}
}
