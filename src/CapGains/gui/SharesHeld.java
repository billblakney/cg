package CapGains.gui;

import java.util.Observable;

public class SharesHeld extends Observable {

	private Long value;

	public SharesHeld() {
		setValue(new Long(0));
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
		setChanged();
		notifyObservers(getValue());
		// clearChanged() automatically called by notifyObservers()
	}
}
