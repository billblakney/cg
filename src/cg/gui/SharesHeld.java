package cg.gui;

import java.util.Observable;

public class SharesHeld extends Observable {

	private Integer value;

	public SharesHeld() {
		setValue(new Integer(0));
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
		setChanged();
		notifyObservers(getValue());
		// clearChanged() automatically called by notifyObservers()
	}
}
