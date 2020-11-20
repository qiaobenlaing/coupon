/**
 * 
 */
package cn.suanzi.tools;

/**
 * @author liuweiping
 *
 */
public class ExpressTool {

	/**
	 * 将给定字符串转化为byte数组
	 * @param buf 字符串
	 * @return 转化过后的byte数组
	 */
	public static byte[] getBytes(String buf) {
		if (isAscii(buf)) {
			return asciiGetBytes(buf);
		}
		return buf.getBytes();
	}

	public static boolean isAscii(String buf) {
		int ind = 0;
		while (ind < buf.length()) {
			char ch = buf.charAt(ind);
			if ((ch < '') && (ch >= 0)) {
				ind++;
			} else {
				return false;
			}
		}
		return true;
	}

	public static byte[] asciiGetBytes(String buf) {
		int size = buf.length();

		byte[] bytebuf = new byte[size];
		for (int i = 0; i < size; i++) {
			bytebuf[i] = ((byte) buf.charAt(i));
		}
		return bytebuf;
	}
}
