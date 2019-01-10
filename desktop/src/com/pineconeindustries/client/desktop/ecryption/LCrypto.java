package com.pineconeindustries.client.desktop.ecryption;

public class LCrypto {

	final static char[] KEY = new char[] { 'd', 'y', '!', 'U', '7', 'e', '0', 'z', 'B', '@', 'm', 'A', 'N', 'D', '^',
			'o', '3', 'g', 'k', 'R', '5', 'z', '(', 'W', 'w', '$', 'Z', 'C', 'M', '2', 'v', '1', 'Z', 'd', '-', '2' };

	public static String decrypt(String encrypted) {

		String[] split = encrypted.split("=");
		String checksum = split[1].trim();
		String crypt = split[0].trim();

		int length = crypt.length();

		int rollingIndex = (length / 4) / 2;

		char[] decrypted = new char[length / 4];

		for (int i = 0; i < length; i += 4) {

			String tmp = new String(
					new char[] { crypt.charAt(i), crypt.charAt(i + 1), crypt.charAt(i + 2), crypt.charAt(i + 3) });

			int code = Integer.parseInt(tmp, 16) / (int) KEY[(i / 4) + rollingIndex];

			decrypted[i / 4] = (char) code;

		}

		return new String(decrypted) + "=" + checksum;

	}

	public static String encrypt(String start) {

		int rollingIndex = start.length() / 2;

		int checksum = 0;
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < start.length(); i++) {

			int ASCII = start.charAt(i);
			int KEYVAL = KEY[i + rollingIndex];

			String hex = Integer.toHexString(ASCII * KEYVAL);

			checksum += (ASCII * KEYVAL);

			switch (hex.length()) {
			case 1:
				sb.append("000" + hex);
				break;
			case 2:
				sb.append("00" + hex);
				break;
			case 3:
				sb.append("0" + hex);
				break;
			case 4:
				sb.append(hex);
				break;
			default:
				System.out.println("ERROR");
				break;
			}

		}

		sb.append("=" + Integer.toHexString(checksum));

		return sb.toString();

	}

}
