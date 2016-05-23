package CapGains;

import java.util.Calendar;

public enum Term {

	SHORT("S"), LONG("L"), MIXED("mixed"), BOTH("Both");

	static public Term computeTerm(SimpleDate buyDate, SimpleDate sellDate) {
		SimpleDate test_date = (SimpleDate) (buyDate.clone());
		test_date.add(Calendar.YEAR, 1);
		test_date.add(Calendar.DAY_OF_MONTH, -1);
		if (test_date.before(sellDate))
			return Term.LONG;
		else
			return Term.SHORT;
	}

	private final String name;

	private Term(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
