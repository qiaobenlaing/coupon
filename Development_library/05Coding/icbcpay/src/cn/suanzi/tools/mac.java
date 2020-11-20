

package cn.suanzi.tools;

import java.lang.reflect.Array;

public class mac {

	public mac() {
	}

	public static void main(String arg[]) {
		@SuppressWarnings("unused")
		byte b[] = addMac((new String(arg[0])).getBytes());
	}

	public static boolean macCheck(byte toCheck[]) {
		int len = Array.getLength(toCheck);
		byte org[] = new byte[len - 8];
		byte oldmac[] = new byte[8];
		for (int i = 0; i < Array.getLength(org); i++)
			org[i] = toCheck[i];

		for (int i = 0; i < 8; i++)
			oldmac[i] = toCheck[(len - 8) + i];

		byte newmac[] = getMac(org);
		for (int i = 0; i < Array.getLength(oldmac); i++)
			System.out.print((new Byte(oldmac[i])).intValue());

		for (int i = 0; i < Array.getLength(newmac); i++)
			System.out.print((new Byte(newmac[i])).intValue());

		for (int i = 0; i < Array.getLength(newmac); i++)
			if (oldmac[i] != newmac[i])
				return false;

		return true;
	}

	public static byte[] addMac(byte toAddMac[]) {
		int o = Array.getLength(toAddMac);
		byte mac[] = getMac(toAddMac);
		int t = Array.getLength(mac);
		byte macAdded[] = new byte[o + t];
		for (int i = 0; i < o; i++)
			macAdded[i] = toAddMac[i];

		for (int i = 0; i < t; i++)
			macAdded[i + o] = mac[i];

		return macAdded;
	}

	public static native byte[] getMac(byte abyte0[]);

	static {
		System.loadLibrary("mac");
	}
}
