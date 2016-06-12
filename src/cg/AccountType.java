package cg;

public enum AccountType {
	NORMAL("Normal (taxable)"),
	ROLLOVER_IRA("Rollover IRA"),
	ROTH_IRA("Roth IRA"),
	A401K("401k");

	private final String name;

	private AccountType(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
