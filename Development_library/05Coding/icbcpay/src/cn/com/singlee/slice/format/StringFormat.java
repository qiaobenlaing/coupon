package cn.com.singlee.slice.format;

import sun.security.provider.MD5;

/**
 * 字符串格式化
 * 
 * @author 刘卫平
 * @version 1.0.0	2014-1-13
 */
public class StringFormat {
	
	public static byte[] convertUpperAndLowerCase(byte[] bInt, String params) {
		if (params.equals("UpperCase")) {
			for (int i = 0; i < bInt.length; i++) {
				if (bInt[i] >= 97) {
					bInt[i] = ((byte) (bInt[i] - 32));
				}
			}
			return bInt;
		}
		if (params.equals("LowerCase")) {
			for (int i = 0; i < bInt.length; i++) {
				if (bInt[i] <= 90) {
					bInt[i] = ((byte) (bInt[i] + 32));
				}
			}
			return bInt;
		}
		return bInt;
	}

	public static byte[] fmtFill(byte[] bInt, String params) {
		return IntFormat.fmtFill(bInt, params);
	}

	public static byte[] fmtTrim(byte[] bInt, String params) {
		int fdot = params.indexOf(",");
		int bdot = params.lastIndexOf(",");
		if (params.charAt(fdot + 1) == ',') {
			fdot++;
		}
		byte s = IntFormat.convertStringToChar(params.substring(0, fdot));

		int num = 0;

		num = Integer.parseInt(params.substring(fdot + 1, bdot));
		String direct = params.substring(bdot + 1);
		int len = bInt.length;
		if (len == 0) {
			return bInt;
		}
		if (direct.equals("LEFT")) {
			int i = 0;
			int j = 0;
			while (bInt[i] == s) {
				if (len - i <= num) {
					break;
				}
				i++;
				if (i == len) {
					return new byte[0];
				}
				j++;
			}
			byte[] bt = new byte[len - j];
			System.arraycopy(bInt, j, bt, 0, len - j);
			return bt;
		}
		if (direct.equals("RIGHT")) {
			int i = len - 1;
			while (bInt[i] == s) {
				if (i == num - 1) {
					break;
				}
				i--;
				if (i < 0) {
					return new byte[0];
				}
			}
			byte[] bt = new byte[i + 1];
			System.arraycopy(bInt, 0, bt, 0, i + 1);
			return bt;
		}
		return bInt;
	}

	public static byte[] replaceString(byte[] bInt, String params) {
		int fdot = params.indexOf(",");
		int bdot = params.lastIndexOf(",");
		int len1 = bInt.length;
		int start = Integer.parseInt(params.substring(0, fdot));
		int end = Integer.parseInt(params.substring(fdot + 1, bdot));
		if (end > len1) {
			end = len1;
		}
		if ((start == 0) && (end == 0)) {
			start = 1;
			end = len1;
		}
		int len2 = params.length() - bdot - 1;
		byte[] bt = new byte[len2];
		for (int i = 0; i < len2; i++) {
			bt[i] = ((byte) params.charAt(bdot + i + 1));
		}
		byte[] rpbt = new byte[len1 + len2 - end + start - 1];
		System.arraycopy(bInt, 0, rpbt, 0, start - 1);
		System.arraycopy(bt, 0, rpbt, start - 1, len2);
		System.arraycopy(bInt, end, rpbt, start + len2 - 1, len1 - end);
		return rpbt;
	}

	public static byte[] slectSubString(byte[] bInt, String params)
			throws Exception {
		int len = bInt.length;
		int dot = params.indexOf(",");
		int start, end;
		try {
			start = Integer.parseInt(params.substring(0, dot));
			end = Integer.parseInt(params.substring(dot + 1));
		} catch (NumberFormatException e) {
			throw new Exception("报文配置有错，请检查");
		}
		if ((start == 0) || (end == 0)) {
			throw new Exception("报文配置有错，请检查");
		}
		if (start < 0) {
			start = len + start + 1;
		}
		if (end < 0) {
			end = len + end + 1;
		}
		if (end > len) {
			end = len;
		}
		if ((start < 0) || (start > len) || (end < 0)) {
			throw new Exception("报文配置有错，请检查");
		}
		byte[] bt;
		if (start < end) {
			int newlen = end - start + 1;
			bt = new byte[newlen];
			System.arraycopy(bInt, start - 1, bt, 0, newlen);
		} else {
			int newlen = start - end + 1;
			bt = new byte[newlen];
			for (int i = 0; i < newlen; i++) {
				bt[i] = bInt[(start - i - 1)];
			}
		}
		return bt;
	}

	public static byte[] fmtTrimAll(byte[] bInt, String params) {
		int dot = params.indexOf(',');
		if (params.charAt(dot + 1) == ',') {
			dot++;
		}
		byte s = IntFormat.convertStringToChar(params.substring(0, dot));

		String direct = params.substring(dot + 1);
		byte[] retBt = (byte[]) null;
		if (direct.equals("LEFT")) {
			for (int i = 0; i < bInt.length; i++) {
				if (bInt[i] == s) {
					retBt = new byte[i];
					System.arraycopy(bInt, 0, retBt, 0, i);
					break;
				}
			}
		} else {
			for (int i = bInt.length - 1; i >= 0; i--) {
				if (bInt[i] == s) {
					retBt = new byte[bInt.length - i - 1];
					System.arraycopy(bInt, i + 1, retBt, 0, bInt.length - i - 1);
					break;
				}
			}
		}
		if (retBt == null) {
			retBt = bInt;
		}
		return retBt;
	}

	public static byte[] countNumOfChar(byte[] bInt, String params) {
		byte s = IntFormat.convertStringToChar(params);
		int count = 0;
		for (int i = 0; i < bInt.length; i++) {
			if (bInt[i] == s) {
				count++;
			}
		}
		return String.valueOf(count).getBytes();
	}

	public static byte[] binToHex(byte[] src, String params)
			throws Exception {
		int len = src.length;
		int left = len % 4;
		int tar = 0;
		if (left != 0) {
			tar = len / 4 + 1;
		} else {
			tar = len / 4;
		}
		byte[] bytes = new byte[tar];
		int i = tar - 1;
		int pos = len - 1;
		while (pos >= 0) {
			int t = 3;
			int n = 0;
			int q = 1;
			while ((t >= 0) && (pos >= 0)) {
				if ((src[pos] < 48) || (src[pos] > 49)) {
					throw new Exception("非法二进制字串");
				}
				n += (src[pos] - 48) * q;
				q *= 2;
				t--;
				pos--;
			}
			if (n < 10) {
				bytes[i] = ((byte) (48 + n));
			} else {
				bytes[i] = ((byte) (55 + n));
			}
			i--;
		}
		return bytes;
	}

	public static byte[] hexToBin(byte[] src, String params)
			throws Exception {
		int len = src.length;
		int tar = len * 4;
		byte[] bytes = new byte[tar];
		for (int i = 0; i < len; i++) {
			int ii = i * 4;
			int n = 0;
			if ((src[i] > 47) && (src[i] < 58)) {
				n = src[i] - 48;
			} else if ((src[i] > 64) && (src[i] < 71)) {
				n = src[i] - 55;
			} else if ((src[i] > 96) && (src[i] < 103)) {
				n = src[i] - 87;
			} else {
				throw new Exception("非法16进制字串");
			}
			bytes[ii] = ((byte) (48 + n / 8));
			n %= 8;
			bytes[(ii + 1)] = ((byte) (48 + n / 4));
			n %= 4;
			bytes[(ii + 2)] = ((byte) (48 + n / 2));
			bytes[(ii + 3)] = ((byte) (48 + n % 2));
		}
		return bytes;
	}

	public static byte[] GetBin(byte[] byteSrc, String params)
			throws Exception {
		System.out.println("aaa:" + byteSrc.length);
		String asciistring = new String(byteSrc);
		if (asciistring.length() % 2 != 0) {
			throw new Exception("getBin方法异常");
		}
		byte[] coverdata = new byte[asciistring.length() / 2];
		for (int i = 0; i < coverdata.length; i++) {
			coverdata[i] = ((byte) Integer.parseInt(
					asciistring.substring(i * 2, (i + 1) * 2), 16));
		}
		return coverdata;
	}

	public static byte[] SetBin(byte[] byAsc, String params)
			throws Exception {
		String buf = new String(byAsc);
		String result = "";

		result = to_hex(buf, byAsc.length / 2);

		return result.toString().getBytes();
	}

	private static String to_hex(String asc_str, int len) {
		String result = "";
		char[] bcdbuf = new char[len];
		if (asc_str.length() % 2 != 0) {
			asc_str = '0' + asc_str;
		}
		char[] asc_buf = asc_str.toCharArray();
		for (int cnt = 0; cnt < len; cnt++) {
			char ch1;
			if (asc_buf[(2 * cnt)] >= 'a') {
				ch1 = (char) (asc_buf[(2 * cnt)] - 'a' + 10);
			} else {
				if (asc_buf[(2 * cnt)] >= 'A') {
					ch1 = (char) (asc_buf[(2 * cnt)] - 'A' + 10);
				} else {
					if (asc_buf[(2 * cnt)] >= '0') {
						ch1 = (char) (asc_buf[(2 * cnt)] - '0');
					} else {
						ch1 = '\000';
					}
				}
			}
			char ch2;
			if (asc_buf[(2 * cnt + 1)] >= 'a') {
				ch2 = (char) (asc_buf[(2 * cnt + 1)] - 'a' + 10);
			} else {
				if (asc_buf[(2 * cnt + 1)] >= 'A') {
					ch2 = (char) (asc_buf[(2 * cnt + 1)] - 'A' + 10);
				} else {
					if (asc_buf[(2 * cnt + 1)] >= '0') {
						ch2 = (char) (asc_buf[(2 * cnt + 1)] - '0');
					} else {
						ch2 = '\000';
					}
				}
			}
			bcdbuf[cnt] = ((char) (ch1 << '\004' | ch2));
		}
		for (int i = 0; i < len; i++) {
			result = result + bcdbuf[i];
		}
		return result;
	}

	public static byte[] HextoASCII(byte[] byteSrc, String params) {
		int start = 0;
		int length = byteSrc.length;
		if ((start < 0) || (length < 0)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (start + length > byteSrc.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		StringBuffer hexTableBuffer = new StringBuffer(2 * length);
		for (int i = start; i < start + length; i++) {
			String num = Integer.toHexString(byteSrc[i] & 0xFF).toUpperCase();

			hexTableBuffer.append(num);
		}
		return hexTableBuffer.toString().getBytes();
	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	public static byte[] HexString2Bytes(byte[] tmp) {
		byte[] ret = new byte[8];
		for (int i = 0; i < 8; i++) {
			ret[i] = uniteBytes(tmp[(i * 2)], tmp[(i * 2 + 1)]);
		}
		return ret;
	}

	public static byte[] Bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret = ret + hex.toUpperCase();
		}
		return ret.getBytes();
	}

	public static byte[] ToHexString(byte[] bytes) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		char[] chars = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];
			chars[(i * 2)] = hexDigits[(b >> 4)];
			chars[(i * 2 + 1)] = hexDigits[(b & 0xF)];
		}
		return new String(chars).getBytes();
	}

	
	public static void main(String[] args) throws Exception {
		byte[] a = { 3 };
		@SuppressWarnings("unused")
		String b = "03";

		byte[] tmp = ToHexString(a);
		System.out.println("bin to hex" + new String(tmp));
		byte[] tmp2 = HexString2Bytes(tmp);
		System.out.println("hex to bin" + new String(tmp2));
	}
}
