package cn.com.singlee.slice.format;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import cn.suanzi.tools.BcdTool;
import cn.suanzi.tools.BcdToolException;


public class IntFormat {
	public static void main(String[] args) {
		String str = "+3500";
		byte[] bt = str.getBytes();
		byte[] amt = fmtIntToMoney(bt, str);
		System.out.println(new String(amt));
	}

	public static byte[] fmtFill(byte[] bInt, String params) {
		int fdot = params.indexOf(",");
		int bdot = params.lastIndexOf(",");
		if (params.charAt(fdot + 1) == ',') {
			fdot++;
		}
		byte s = convertStringToChar(params.substring(0, fdot));
		int num = 0;

		num = Integer.parseInt(params.substring(fdot + 1, bdot));
		String direct = params.substring(bdot + 1);
		int len = bInt.length;
		int j = num - len;
		if (j <= 0) {
			return bInt;
		}
		byte[] bt = new byte[j];
		byte[] rpbt = new byte[len + j];
		for (int i = 0; i < j; i++) {
			bt[i] = s;
		}
		if (direct.equals("LEFT")) {
			System.arraycopy(bt, 0, rpbt, 0, j);
			System.arraycopy(bInt, 0, rpbt, j, len);
			return rpbt;
		}
		if (direct.equals("RIGHT")) {
			System.arraycopy(bInt, 0, rpbt, 0, len);
			System.arraycopy(bt, 0, rpbt, len, j);
			return rpbt;
		}
		return bInt;
	}

	public static byte[] fmtMoney(byte[] bInt, String params) {
		NumberFormat formatter = new DecimalFormat(params);
		String str = new String(bInt);
		int i = Integer.parseInt(str);
		String ret = formatter.format(i);
		return ret.getBytes();
	}

	public static byte[] fmtTrim(byte[] bInt, String params) {
		int fdot = params.indexOf(",");
		int bdot = params.lastIndexOf(",");
		if (params.charAt(fdot + 1) == ',') {
			fdot++;
		}
		byte s = convertStringToChar(params.substring(0, fdot));
		int num = 0;

		num = Integer.parseInt(params.substring(fdot + 1, bdot));
		String direct = params.substring(bdot + 1);
		int len = bInt.length;
		byte[] tmpbt = new byte[1];
		tmpbt[0] = 48;
		if (direct.equals("LEFT")) {
			int i = 0;
			int j = 0;
			while (bInt[i] == s) {
				if (len - i <= num) {
					break;
				}
				i++;
				if (i == len) {
					return tmpbt;
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
					return tmpbt;
				}
			}
			byte[] bt = new byte[i + 1];
			System.arraycopy(bInt, 0, bt, 0, i + 1);
			return bt;
		}
		return bInt;
	}

	public static byte[] ascToBcd(byte[] byAsc, String params) {
		boolean bLeft = false;
		int bdot = params.lastIndexOf(",");
		String c = params.substring(0, 1);
		int nFill = Integer.parseInt(c, 16);
		String str = params.substring(2, bdot);
		String strlen = params.substring(bdot + 1);
		int len = Integer.parseInt(strlen);
		byte[] ret = (byte[]) null;
		if (str.equals("LEFT")) {
			bLeft = true;
		}
		try {
			ret = BcdTool.ascToBcd(byAsc, nFill, bLeft, len);
		} catch (BcdToolException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static byte[] bcdToAsc(byte[] byBcd, String params) {
		boolean bLeft = false;
		String c = params.substring(0, 1);
		int bdot = params.lastIndexOf(",");
		int nFill = Integer.parseInt(c, 16);
		String str = params.substring(2, bdot);
		String strlen = params.substring(bdot + 1);
		int len = Integer.parseInt(strlen);
		byte[] ret = (byte[]) null;
		if (str.equals("LEFT")) {
			bLeft = true;
		}
		try {
			ret = BcdTool.bcdToAsc(byBcd, nFill, bLeft, len);
		} catch (BcdToolException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static byte[] numCompute(byte[] bInt, String params) {
		int fdot = params.indexOf(",");
		int len = bInt.length;
		String op = params.substring(0, fdot);
		String strNum = params.substring(fdot + 1);
		int iNum = Integer.parseInt(strNum);
		int pos = 0;
		for (pos = 0; pos < len; pos++) {
			if (bInt[pos] != 32) {
				break;
			}
		}
		byte[] tail = new byte[len - pos];
		System.arraycopy(bInt, pos, tail, 0, len - pos);
		int opNum = Integer.parseInt(new String(tail));
		if (op.equals("+")) {
			opNum += iNum;
		} else if (op.equals("-")) {
			opNum -= iNum;
		}
		String strRet = String.valueOf(opNum);
		int strLen = strRet.length();
		byte[] head = new byte[len - strLen];
		for (int i = 0; i < len - strLen; i++) {
			head[i] = 48;
		}
		byte[] btRet = new byte[len];
		System.arraycopy(head, 0, btRet, 0, len - strLen);
		System.arraycopy(strRet.getBytes(), 0, btRet, len - strLen, strLen);
		return btRet;
	}

	public static byte convertStringToChar(String str) {
		if (str.length() == 1) {
			return (byte) str.charAt(0);
		}
		if (str.equals("\\n")) {
			return 10;
		}
		if (str.equals("\\t")) {
			return 9;
		}
		if (str.equals("\\r")) {
			return 13;
		}
		if (str.equals("\\f")) {
			return 12;
		}
		if (str.equals("\\")) {
			return 92;
		}
		if (str.equals("\\,")) {
			return 44;
		}
		if ((str.length() == 4) && (str.substring(0, 2).equalsIgnoreCase("0x"))) {
			return (byte) Integer.parseInt(str.substring(2), 16);
		}
		System.err.println("不支持该填充字符！[" + str + "]");
		return 0;
	}

	public static byte[] fmtMoneyToInt(byte[] bInt, String params) {
		int len = bInt.length;
		StringBuffer amt = new StringBuffer();
		int i = 0;
		int mark = -1;
		int start = 0;
		boolean pure = true;
		for (; i < len; i++) {
			if (bInt[i] == 46) {
				mark = i;
				break;
			}
		}
		if (mark == -1) {
			return bInt;
		}
		if ((bInt[0] == 45) || (bInt[0] == 43)) {
			amt.append((char) bInt[0]);
			start = 1;
		}
		for (i = start; i < mark; i++) {
			if (bInt[i] != 48) {
				pure = false;
				break;
			}
		}
		if (pure) {
			for (i = mark + 1; i < len; i++) {
				if (bInt[i] != 48) {
					start = i;
					break;
				}
			}
		}
		for (i = start; i < len; i++) {
			if (bInt[i] != 46) {
				amt.append((char) bInt[i]);
			}
		}
		return amt.toString().getBytes();
	}

	public static byte[] fmtIntToMoney(byte[] bInt, String params) {
		int len = bInt.length;
		byte[] bt;
		if ((bInt[0] == 45) || (bInt[0] == 43)) {
			if (len < 4) {
				bt = new byte[5];
				bt[0] = bInt[0];
				bt[1] = 48;
				bt[2] = 46;
				bt[3] = 48;
				bt[4] = 48;
				for (int i = 0; i < len - 1; i++) {
					bt[(4 - i)] = bInt[(len - i - 1)];
				}
			} else {
				bt = new byte[len + 1];
				for (int i = 0; i < len - 2; i++) {
					bt[i] = bInt[i];
				}
				bt[(len - 2)] = 46;
				bt[(len - 1)] = bInt[(len - 2)];
				bt[len] = bInt[(len - 1)];
			}
		} else if (len < 3) {
			bt = new byte[4];
			bt[0] = 48;
			bt[1] = 46;
			bt[2] = 48;
			bt[3] = 48;
			for (int i = 0; i < len; i++) {
				bt[(3 - i)] = bInt[(len - i - 1)];
			}
		} else {
			bt = new byte[len + 1];
			for (int i = 0; i < len - 2; i++) {
				bt[i] = bInt[i];
			}
			bt[(len - 2)] = 46;
			bt[(len - 1)] = bInt[(len - 2)];
			bt[len] = bInt[(len - 1)];
		}
		return bt;
	}

	public static byte[] fmtDigitMoneyToInt(byte[] bInt, String params)
			throws Exception {
		int num = Integer.parseInt(params);
		String str = new String(bInt);
		if (str.indexOf(".") == -1) {
			throw new Exception("金额没有小数点");
		}
		if (str.length() - str.indexOf(".") - 1 > num) {
			throw new Exception("金额小数位超出[" + num + "]位");
		}
		if (str.length() - str.indexOf(".") - 1 < num) {
			throw new Exception("金额小数位少于[" + num + "]位");
		}
		int len = bInt.length;
		StringBuffer amt = new StringBuffer();
		int i = 0;
		int mark = -1;
		int start = 0;
		boolean pure = true;
		for (; i < len; i++) {
			if (bInt[i] == 46) {
				mark = i;
				break;
			}
		}
		if (mark == -1) {
			return bInt;
		}
		if ((bInt[0] == 45) || (bInt[0] == 43)) {
			amt.append((char) bInt[0]);
			start = 1;
		}
		for (i = start; i < mark; i++) {
			if (bInt[i] != 48) {
				pure = false;
				break;
			}
		}
		if (pure) {
			for (i = mark + 1; i < len; i++) {
				if (bInt[i] != 48) {
					start = i;
					break;
				}
			}
		}
		for (i = start; i < len; i++) {
			if (bInt[i] != 46) {
				amt.append((char) bInt[i]);
			}
		}
		return amt.toString().getBytes();
	}
}
