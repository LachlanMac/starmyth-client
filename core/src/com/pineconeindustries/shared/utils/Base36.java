package com.pineconeindustries.shared.utils;

import java.util.HashMap;

/*****************************************************
 * Class : Base36
 * 
 * @version 0.2.1
 * @author Lachlan R McCallum
 * 
 *         The Base36 class serves as general class with all static methods that
 *         perform several types of conversions involving Base36 and Base10.
 *****************************************************/

public class Base36 {

	private static HashMap<Character, Integer> key;
	public static final char keyIndex[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	static {
		key = new HashMap<Character, Integer>();
		for (int i = 0; i < keyIndex.length; i++) {
			key.put(keyIndex[i], i);
		}
	}

	public static int parse(char c) {
		return key.get(c);
	}

	public static char parse(int n) {
		return keyIndex[n];
	}

	public static char subtract(char a, char b) {

		int subtract = parse(b);
		int start = parse(a);
		int index = start;

		for (int i = subtract; i > 0; i--) {
			index--;
			if (index == -1) {
				index = 35;
			}
		}
		return parse(index);

	}

	public static char add(char a, char b) {

		int add = parse(b);
		int start = parse(a);

		int index = start;

		int stop = add + start;

		for (int i = start; i < stop; i++) {

			index++;
			if (index == 36) {
				index = 0;
			}
		}

		return parse(index);

	}

	public static long parseBigValue(String value) {

		long val = 0;
		long multiple = 1;

		for (int i = value.length() - 1; i >= 0; i--) {
			val += key.get(value.charAt(i)) * multiple;
			multiple *= 36;
		}
		return val;
	}

	public static int parseValue(String value) {
		int val = 0;
		int multiple = 1;

		for (int i = value.length() - 1; i >= 0; i--) {
			val += key.get(value.charAt(i)) * multiple;
			multiple *= 36;
		}

		return val;
	}

	public static String parseValue(int value) {

		if (value == 0) {
			return "0";
		}

		StringBuffer b = new StringBuffer();
		int v = value;

		while (v != 0) {
			int remainder = v % 36;
			v = v / 36;
			b.append(parse(remainder));

		}
		b.reverse();

		return b.toString();

	}

}