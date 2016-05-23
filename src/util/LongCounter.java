package util;

public class LongCounter {

	private static long nextLong = 1;

	public static long getNext() {
		return nextLong++;
	}

}
