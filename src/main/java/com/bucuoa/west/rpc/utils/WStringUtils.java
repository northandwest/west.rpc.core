package com.bucuoa.west.rpc.utils;

public class WStringUtils {
	public static String join(String[] array) {
		if (array.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String s : array)
			sb.append(s);
		return sb.toString();
	}

	public static boolean isNotBlank(CharSequence cs) {
		return !isBlank(cs);
	}

	public static boolean isEmpty(CharSequence cs) {
		return isBlank(cs);
	}

	public static boolean isNotEmpty(CharSequence cs) {
		return !isBlank(cs);
	}

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static String toString(Object o) {
		return o != null ? o.toString() : null;
	}
}
