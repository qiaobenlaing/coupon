

package cn.suanzi.tools;

import java.util.StringTokenizer;

public class StringTool {

	public StringTool() {
	}

	public static String[] getTokens(String sSource, String sDelim) {
		StringTokenizer tokenizer = new StringTokenizer(sSource, sDelim);
		int iCount = tokenizer.countTokens();
		String sTokens[] = new String[iCount];
		for (int i = 0; i < iCount; i++)
			sTokens[i] = tokenizer.nextToken();

		return sTokens;
	}

	public static boolean isNum(String sSource, boolean bInt) {
		boolean bHex = false;
		@SuppressWarnings("unused")
		int nSrc = 0;
		if (bInt) {
			int i = 0;
			for (int n = sSource.length(); i < n; i++) {
				if (!Character.isLetter(sSource.charAt(i)))
					continue;
				nSrc = Integer.parseInt(sSource, 16);
				bHex = true;
				break;
			}

			if (!bHex)
				Integer.parseInt(sSource);
		} else {
			Float.parseFloat(sSource);
		}
		return true;
	}

	public static String convertToHex(byte bySrc[], int offset, int len) {
		byte byNew[] = new byte[len];
		String sTmp = "";
		String sResult = "";
		@SuppressWarnings("unused")
		int nMov = 24;
		int i;
		for (i = 0; i < len; i++)
			byNew[i] = bySrc[offset + i];

		i = 0;
		for (int n = byNew.length; i < n; i++) {
			sTmp = Integer.toHexString(byNew[i] & 0xff);
			sTmp = fillChar(sTmp, '0', 2, true) + " ";
			sResult = sResult + sTmp;
		}

		return sResult;
	}

	/**
	 * byte[]转为HEX，中间不带空格。
	 * @param bySrc 源byte[]
	 * @param offset 读取bySrc的起始位置
	 * @param len 读取bySrc的长度
	 * @return
	 */
	public static String convertToHexNoSpace(byte bySrc[], int offset, int len) {
		byte byNew[] = new byte[len];
		String sTmp = "";
		StringBuilder sResult = new StringBuilder();
		@SuppressWarnings("unused")
		int nMov = 24;
		int i;
		for (i = 0; i < len; i++)
			byNew[i] = bySrc[offset + i];

		i = 0;
		for (int n = byNew.length; i < n; i++) {
			sTmp = Integer.toHexString(byNew[i] & 0xff);
			sTmp = fillChar(sTmp, '0', 2, true);
			sResult.append(sTmp);
		}

		return sResult.toString();
	}

	public static String formatInHex(byte bySrc[], int nLineLen) {
		@SuppressWarnings("unused")
		String sRepalce = "\n\t\r\0";
		int nLength = bySrc.length;
		int nLine = nLength / nLineLen + 1;
		String sLineTmp = "";
		byte byNew[] = new byte[nLine * nLineLen];
		for (int i = 0; i < nLength; i++)
			byNew[i] = bySrc[i];

		String sRet = "";
		for (int i = 0; i < nLine; i++) {
			if (byNew[(i + 1) * nLineLen - 1] < 0) {
				sLineTmp = new String(byNew, i * nLineLen, nLineLen - 1);
				sLineTmp = sLineTmp + "?";
			} else if (byNew[i * nLineLen] < 0) {
				sLineTmp = new String(byNew, i * nLineLen + 1, nLineLen - 1);
				sLineTmp = "?" + sLineTmp;
			} else {
				sLineTmp = new String(byNew, i * nLineLen, nLineLen);
			}
			sLineTmp = "  " + sLineTmp;
			sRet = sRet
					+ "["
					+ fillChar(Integer.toHexString(i * nLineLen + 1), '0', 4,
							true)
					+ "-"
					+ fillChar(Integer.toHexString(i * nLineLen + nLineLen),
							'0', 4, true) + "] ";
			sRet = sRet + convertToHex(byNew, i * nLineLen, nLineLen);
			int j = 0;
			for (int n = "\n\t\r\0".length(); j < n; j++)
				sLineTmp = sLineTmp.replace("\n\t\r\0".charAt(j), '.');

			sRet = sRet + sLineTmp + "\n";
		}

		return sRet;
	}

	public static String formatInHex(String sSource, int nLineLen) {
		String sRepalce = "\n\t\r\0";
		byte bySrc[] = sSource.getBytes();
		int nLength = bySrc.length;
		int nLine = nLength / nLineLen + 1;
		String sLineTmp = "";
		byte byNew[] = new byte[nLine * nLineLen];
		for (int i = 0; i < nLength; i++)
			byNew[i] = bySrc[i];

		String sRet = "";
		for (int i = 0; i < nLine; i++) {
			if (byNew[(i + 1) * nLineLen - 1] < 0) {
				sLineTmp = new String(byNew, i * nLineLen, nLineLen - 1);
				sLineTmp = sLineTmp + "?";
			} else if (byNew[i * nLineLen] < 0) {
				sLineTmp = new String(byNew, i * nLineLen + 1, nLineLen - 1);
				sLineTmp = "?" + sLineTmp;
			} else {
				sLineTmp = new String(byNew, i * nLineLen, nLineLen);
			}
			sLineTmp = "  " + sLineTmp;
			sRet = sRet + convertToHex(byNew, i * nLineLen, nLineLen);
			int j = 0;
			for (int n = sRepalce.length(); j < n; j++)
				sLineTmp = sLineTmp.replace(sRepalce.charAt(j), '.');

			sRet = sRet + sLineTmp + "\n";
		}

		return sRet;
	}

	public static String getChinese(int nChinese) {
		String sInt = Integer.toString(nChinese, 16);
		byte byInt[] = new byte[2];
		int i = 0;
		for (int n = byInt.length; i < n; i++) {
			String sTmp = sInt.substring(i * 2, (i + 1) * 2);
			byInt[i] = (byte) Integer.parseInt(sTmp, 16);
		}

		return new String(byInt);
	}

	public static byte[] addBytes(byte bySrc[], byte byAdd[]) {
		int nSrc = bySrc.length;
		byte byRet[] = new byte[bySrc.length + byAdd.length];
		int i = 0;
		for (int n = bySrc.length; i < n; i++)
			byRet[i] = bySrc[i];

		i = 0;
		for (int n = byAdd.length; i < n; i++)
			byRet[i + nSrc] = byAdd[i];

		return byRet;
	}

	public static byte[] trim(byte bySrc[], char ch, int nPos) {
		int nLen = bySrc.length;
		byte byRet[] = new byte[nLen];
		if (nPos == 0) {
			int i;
			for (i = 0; i < nLen; i++)
				if (bySrc[i] != (byte) ch)
					break;

			for (int j = i; j < nLen; j++)
				byRet[j - i] = bySrc[j];

		} else if (nPos == 1) {
			int i;
			for (i = nLen - 1; i >= 0; i--)
				if (bySrc[i] != (byte) ch)
					break;

			int nRight = i + 1;
			for (int j = 0; j < nRight; j++)
				byRet[j] = bySrc[j];

		} else if (nPos == 2) {
			int i;
			for (i = nLen - 1; i >= 0; i--)
				if (bySrc[i] != (byte) ch)
					break;

			int nRight = i + 1;
			for (i = 0; i < nLen; i++)
				if (bySrc[i] != (byte) ch)
					break;

			int nLeft = i;
			for (int j = nLeft; j < nRight; j++)
				byRet[j - nLeft] = bySrc[j];

		}
		return trimnull(byRet);
	}

	public static byte[] trimnull(byte byRet[]) {
		int startPos = 0;
		int endPos = byRet.length - 1;
		for (int i = 0; i < byRet.length; i++) {
			if (byRet[i] != 0) {
				startPos = i;
				break;
			}
			if (i == byRet.length - 1 && byRet[i] == 0)
				return null;
		}

		for (int i = byRet.length - 1; i >= 0; i--) {
			if (byRet[i] == 0)
				continue;
			endPos = i;
			break;
		}

		byte byTmp[] = new byte[(endPos - startPos) + 1];
		System.arraycopy(byRet, startPos, byTmp, 0, (endPos - startPos) + 1);
		return byTmp;
	}

	public static String trim(String sSrc, char ch, int nPos) {
		if (sSrc == null || sSrc.equals(""))
			return sSrc;
		byte bySrc[] = sSrc.getBytes();
		int nLen = bySrc.length;
		byte byRet[] = new byte[nLen];
		if (nPos == 0) {
			int i;
			for (i = 0; i < nLen; i++)
				if (bySrc[i] != (byte) ch)
					break;

			for (int j = i; j < nLen; j++)
				byRet[j - i] = bySrc[j];

		} else if (nPos == 1) {
			int i;
			for (i = nLen - 1; i >= 0; i--)
				if (bySrc[i] != (byte) ch)
					break;

			int nRight = i + 1;
			for (int j = 0; j < nRight; j++)
				byRet[j] = bySrc[j];

		} else if (nPos == 2) {
			int i;
			for (i = nLen - 1; i >= 0; i--)
				if (bySrc[i] != (byte) ch)
					break;

			int nRight = i + 1;
			for (i = 0; i < nLen; i++)
				if (bySrc[i] != (byte) ch)
					break;

			int nLeft = i;
			for (int j = nLeft; j < nRight; j++)
				byRet[j - nLeft] = bySrc[j];

		}
		return (new String(byRet)).trim();
	}

	public static String fill(String sSrc, char ch, int nLen, boolean bLeft) {
		byte bTmp[] = trimnull(sSrc.getBytes());
		sSrc = new String(bTmp);
		if (sSrc == null || sSrc.equals("")) {
			StringBuffer sbRet = new StringBuffer();
			for (int i = 0; i < nLen; i++)
				sbRet.append(ch);

			return sbRet.toString();
		}
		byte bySrc[] = sSrc.getBytes();
		int nSrcLen = bySrc.length;
		if (nSrcLen >= nLen)
			return sSrc;
		byte byRet[] = new byte[nLen];
		if (bLeft) {
			int i = 0;
			for (int n = nLen - nSrcLen; i < n; i++)
				byRet[i] = (byte) ch;

			i = nLen - nSrcLen;
			for (int n = nLen; i < n; i++)
				byRet[i] = bySrc[(i - nLen) + nSrcLen];

		} else {
			int i = 0;
			for (int n = nSrcLen; i < n; i++)
				byRet[i] = bySrc[i];

			i = nSrcLen;
			for (int n = nLen; i < n; i++)
				byRet[i] = (byte) ch;

		}
		return new String(byRet);
	}

	public static String fillChar(String sSource, char ch, int nLen,
			boolean bLeft) {
		int nSrcLen = sSource.length();
		if (nSrcLen <= nLen) {
			StringBuffer buffer = new StringBuffer();
			if (bLeft) {
				for (int i = 0; i < nLen - nSrcLen; i++)
					buffer.append(ch);

				buffer.append(sSource);
			} else {
				buffer.append(sSource);
				for (int i = 0; i < nLen - nSrcLen; i++)
					buffer.append(ch);

			}
			return buffer.toString();
		} else {
			return sSource;
		}
	}

	public static String toHexTable(byte byteSrc[]) {
		return toHexTable(byteSrc, 16, 7);
	}

	public static String toHexTable(byte byteSrc[], int lengthOfLine) {
		return toHexTable(byteSrc, lengthOfLine, 7);
	}

	public static String toHexTable(byte byteSrc[], int lengthOfLine, int column) {
		StringBuffer hexTableBuffer = new StringBuffer(256);
		int lineCount = byteSrc.length / lengthOfLine;
		int totalLen = byteSrc.length;
		if (byteSrc.length % lengthOfLine != 0)
			lineCount++;
		for (int lineNumber = 0; lineNumber < lineCount; lineNumber++) {
			int startPos = lineNumber * lengthOfLine;
			byte lineByte[] = new byte[Math.min(lengthOfLine, totalLen
					- startPos)];
			System.arraycopy(byteSrc, startPos, lineByte, 0, lineByte.length);
			int columnA = column & 4;
			if (4 == columnA) {
				int count = 10 * lineNumber;
				String addrStr = Integer.toString(count);
				int len = addrStr.length();
				for (int i = 0; i < 8 - len; i++)
					hexTableBuffer.append('0');

				hexTableBuffer.append(addrStr);
				hexTableBuffer.append("h: ");
			}
			int columnB = column & 2;
			if (2 == columnB) {
				StringBuffer byteStrBuf = new StringBuffer();
				for (int i = 0; i < lineByte.length; i++) {
					String num = Integer.toHexString(lineByte[i] & 0xff);
					if (num.length() < 2)
						byteStrBuf.append('0');
					byteStrBuf.append(num);
					byteStrBuf.append(' ');
				}

				hexTableBuffer.append(fillChar(byteStrBuf.toString(), ' ', 48,
						false));
				hexTableBuffer.append("; ");
			}
			int columnC = column & 1;
			if (1 == columnC) {
				for (int i = 0; i < lineByte.length; i++) {
					char c = (char) lineByte[i];
					if (c < '!')
						c = '.';
					try {
						if (c >= '\240' && i < lineByte.length - 1) {
							char c2 = (char) lineByte[i + 1];
							if (c2 >= '\240') {
								String str = new String(lineByte, i, 2);
								hexTableBuffer.append(str);
								i++;
								continue;
							}
						}
					} catch (Exception exception) {
					}
					hexTableBuffer.append("");
					hexTableBuffer.append(c);
				}

			}
			if (lineNumber >= lineCount - 1)
				break;
			hexTableBuffer.append('\n');
		}

		return hexTableBuffer.toString();
	}

	 /** 
     * 二进制数组转二进制字符串 
     * @param b 
     * @return 
     */  
    public static String bytesToBytesStr(byte[] b){  
        StringBuffer sb = new StringBuffer();  
        String s ="";  
        for(byte bs:b){    
            String sj = Integer.toBinaryString(bs);  
            s += sj;  
            int i = sj.length();  
            if (i<8) {   //8位不够，前面补零操作  
                int in = 8-i;  
                s = addZeroHead(s,in);  
                sb.append(s);  
                s="";  
            }  
         }  
        return sb.toString();  
    }  

    /** 
     * 前补零操作 
     * 二进制字符串中，不够八位 
     * @return 
     */  
    public static String addZeroHead(String src,int addZero){  
        String sr = src;  
        String s = "";  
        for(int f=0;f<addZero;f++){  
            s += "0";  
        }  
        return sr = s+sr;  
    }  
	public static void main(String args[]) {
		@SuppressWarnings("unused")
		byte b[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 16, 17, 18, 19, 20, 21, 22, 23,
				24, 25, 32, 33, 34, 35, 36, 37, 73, 74, 75 };
		String strData[] = { "b5", "da", "b5", "00", "da", "01", "88", "41",
				"00", "00", "04", "00", "00", "00", "11", "00", "00", "00",
				"03", "12", "09", "16", "09", "06", "98", "86", "20", "06",
				"98", "86", "20", "36", "39", "30", "30", "30", "33", "38",
				"33", "33", "32", "37", "31", "30", "30", "30", "30", "30",
				"30", "30", "30", "30", "30", "37", "31", "30", "30", "31",
				"00", "20", "32", "32", "36", "3d", "33", "33", "37", "3c",
				"37", "30", "3d", "33", "39", "30", "3b", "31", "3b", "37",
				"31", "3e", "01", "01", "06", "62", "48", "10", "00", "01",
				"54", "54", "54", "54", "54", "54", "54", "54" };
		byte byteData[] = new byte[strData.length];
		for (int i = 0; i < strData.length; i++)
			byteData[i] = (byte) Integer.parseInt(strData[i], 16);

		byte test[] = "12345678ab中文ef12345678ab中文ef12345678ab中文ef12345678ab中文ef12345678ab中文ef12345678ab中文ef12345678ab中文ef12345678ab中文ef"
				.getBytes();
		 String str = toHexTable(test, 16);
		System.out.println(str);

	}

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int ALL = 2;
}
