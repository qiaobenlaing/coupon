

package cn.suanzi.tools;



public class BcdTool {

	public BcdTool() {
	}

	public static byte[] ascToBcd(byte byAsc[]) throws BcdToolException {
		int nLen = byAsc.length / 2 + byAsc.length % 2;
		int nOdd = byAsc.length % 2;
		byte byBcd[] = new byte[nLen];
		for (int i = 0; i < nLen; i++)
			if (i == nLen - 1 && nOdd == 1)
				byBcd[i] = (byte) ((byAsc[i * 2] - 48) * 16 + 15);
			else
				byBcd[i] = (byte) (((byAsc[i * 2] - 48) * 16 + byAsc[i * 2 + 1]) - 48);

		return byBcd;
	}

	public static byte[] ascToBcd(byte byAsc[], int nFill, boolean bLeft)
			throws BcdToolException {
		if (byAsc.length % 2 == 1) {
			byte byAscNew[] = new byte[byAsc.length + 1];
			byte byFill[] = new byte[1];
			byFill[0] = (byte) (nFill + 48);
			if (bLeft) {
				byAscNew[0] = byFill[0];
				System.arraycopy(byAsc, 0, byAscNew, 1, byAsc.length);
			} else {
				System.arraycopy(byAsc, 0, byAscNew, 0, byAsc.length);
				System.arraycopy(byFill, 0, byAscNew, byAscNew.length - 1, 1);
			}
			byAsc = byAscNew;
		}
		return ascToBcd(byAsc);
	}

	public static byte[] ascToBcd(byte byAsc[], int nFill, boolean bLeft,
			int nLength) throws BcdToolException {
		byte byBcd_Local[] = ascToBcd(byAsc, nFill, bLeft);
		int nBcd_Local = byBcd_Local.length;
		if (nBcd_Local * 2 > nLength + 1)
			throw new BcdToolException(
					"数据长度超长 : " + nBcd_Local
							+ ">" + nLength);
		int nFillLen = nLength - nBcd_Local * 2;
		nFillLen = nFillLen / 2 + nFillLen % 2;
		if (nFillLen <= 0)
			return byBcd_Local;
		byte byFill[] = new byte[nFillLen];
		byte byDest[] = new byte[nFillLen + nBcd_Local];
		for (int i = 0; i < nFillLen; i++)
			byFill[i] = (byte) (nFill + nFill * 16);

		if (bLeft) {
			System.arraycopy(byFill, 0, byDest, 0, byFill.length);
			System.arraycopy(byBcd_Local, 0, byDest, byFill.length,
					byBcd_Local.length);
		} else {
			System.arraycopy(byBcd_Local, 0, byDest, 0, byBcd_Local.length);
			System.arraycopy(byFill, 0, byDest, byBcd_Local.length,
					byFill.length);
		}
		return byDest;
	}

	public static byte[] bcdToAsc(byte byBcd[]) throws BcdToolException {
		int nLen = byBcd.length;
		byte byAsc[] = new byte[nLen * 2];
		for (int i = 0; i < nLen; i++) {
			int nBcd = byBcd[i];
			if (byBcd[i] < 0)
				nBcd += 256;
			byAsc[2 * i] = (byte) (nBcd / 16 + 48);
			byAsc[2 * i + 1] = (byte) (nBcd % 16 + 48);
		}

		return byAsc;
	}

	public static byte[] bcdToAsc(byte byBcd[], int nFill, boolean bLeft)
			throws BcdToolException {
		byte byAsc[] = bcdToAsc(byBcd);
		@SuppressWarnings("unused")
		int nLen = byAsc.length;
		byAsc = StringTool.trim(byAsc, (char) (nFill + 48), bLeft ? 0 : 1);
		return (new String(byAsc)).trim().getBytes();
	}

	public static byte[] bcdToAsc(byte byBcd[], int nFill, boolean bLeft,
			int nLen2) throws BcdToolException {
		byte byTmp[] = bcdToAsc(byBcd);
		@SuppressWarnings("unused")
		int nLen = byTmp.length;
		int iDirection = 0;
		if (bLeft)
			iDirection = 0;
		else
			iDirection = 1;
		byte byAsc[] = StringTool.trim(byTmp, (char) (nFill + 48), iDirection);
		String sTmp;
		if (byAsc != null) {
			if (byAsc.length > nLen2) {
				byte byFilter[] = new byte[nLen2];
				if (bLeft)
					System.arraycopy(byAsc, byAsc.length - nLen2, byFilter, 0,
							nLen2);
				else
					System.arraycopy(byAsc, 0, byFilter, 0, nLen2);
				byAsc = byFilter;
			}
			sTmp = new String(byAsc);
		} else {
			sTmp = "";
		}
		byAsc = new byte[sTmp.length()];
		byAsc = StringTool.fill(sTmp, (char) (nFill + 48), nLen2, bLeft)
				.getBytes();
		return (new String(byAsc)).getBytes();
	}
}
