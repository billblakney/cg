package util;

public class IntegerCounter {

	private static long nextInt = 1;

	public static long getNext() {
		return nextInt++;
	}

}
