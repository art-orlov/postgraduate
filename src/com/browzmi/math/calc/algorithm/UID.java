package com.browzmi.math.calc.algorithm;

/**
 * Date: 09.11.2008
 * Time: 14:48:32
 */
public final class UID implements Comparable<UID> {
	private final String uid;
	private final int hash;

	private UID(String uid) {
		if (uid.length() != 22) {
			throw new IllegalArgumentException("Wrong uid length");
		}
		this.uid = uid;
		this.hash = uid.hashCode();
	}

	@Override
	public String toString() {
		return uid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final UID that = (UID) o;

		return hash == that.hash && uid.equals(that.uid);
	}

	@Override
	public int hashCode() {
		return hash;
	}

	public int compareTo(UID o) {
		return uid.compareTo(o.uid);
	}

	public static UID of(String uid) {
		return new UID(uid);
	}

	public static UID of(int uid) {
		return new UID(toUid(uid));
	}
	
	private static String toUid(int uid) {
		String res = String.valueOf(uid);
		while (res.length() < 22) {
			res = res + '0';
		}
		return res;
	}
}
