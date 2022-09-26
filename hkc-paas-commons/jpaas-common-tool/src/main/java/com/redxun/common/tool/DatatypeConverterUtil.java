package com.redxun.common.tool;

/**
 *  数据类型转换工具类
 *
 */
public class DatatypeConverterUtil {

	/**
	 * 把字符串转为16进制数组
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexBinary(String hexStr) {
		final int len = hexStr.length();

		if (len % 2 != 0) {
			throw new IllegalArgumentException("hexBinary needs to be even-length: " + hexStr);
		}

		byte[] out = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			int h = hexToBin(hexStr.charAt(i));
			int l = hexToBin(hexStr.charAt(i + 1));
			if (h == -1 || l == -1) {
				throw new IllegalArgumentException("contains illegal character for hexBinary: " + hexStr);
			}

			out[i / 2] = (byte) (h * 16 + l);
		}

		return out;
	}

	/**
	 * 把字符转成Bin
	 * @param ch
	 * @return
	 */
	private static int hexToBin(char ch) {
		if ('0' <= ch && ch <= '9') {
			return ch - '0';
		}
		if ('A' <= ch && ch <= 'F') {
			return ch - 'A' + 10;
		}
		if ('a' <= ch && ch <= 'f') {
			return ch - 'a' + 10;
		}
		return -1;
	}

}
