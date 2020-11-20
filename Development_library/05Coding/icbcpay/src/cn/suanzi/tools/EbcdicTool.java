

package cn.suanzi.tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EbcdicTool {

	public EbcdicTool() {
		lsAscEbc = new ArrayList<Object>();
	}

	public static int hiByte(int nWord) {
		return nWord >> 8;
	}

	public static int lowByte(int nWord) {
		return nWord & -1;
	}

	public static int ebcToAsc(int nChar) {
		return EbcdToAsciiBuf[nChar < 176 || nChar > 185 ? nChar : nChar + 64];
	}

	public static byte[] ebcdToAscii(byte byS[]) {
		int pS[] = byteToInt(byS);
		return intToByte(ebcdToAscii(pS));
	}

	public static int[] ebcdToAscii(int byS[]) {
		int nOut[] = new int[byS.length];
		int i = 0;
		for (int n = byS.length; i < n; i++) {
			if (byS[i] == 0)
				byS[i] = 64;
			int c = byS[i];
			nOut[i] = EbcdToAsciiBuf[c];
		}

		return nOut;
	}

	public static byte[] asciiToEbcd(byte byS[]) {
		int pS[] = byteToInt(byS);
		int n = pS.length;
		int pD[] = new int[n];
		for (int i = 0; i < n; i++)
			pD[i] = AsciiToEbcdBuf[pS[i]];

		return intToByte(pD);
	}

	public static int[] asciiToEbcd(int byS[]) {
		int pS[] = byS;
		int n = pS.length;
		int pD[] = new int[n];
		for (int i = 0; i < n; i++)
			pD[i] = AsciiToEbcdBuf[pS[i]];

		return pD;
	}

	public byte[] asciiToEbcdic1(byte byS[]) {
		int convert_flag = 1;
		int pS[] = byteToInt(byS);
		int n = pS.length;
		int pD[] = new int[n + 5];
		int pTmp[] = new int[2];
		int i = 0;
		int j = 0;
		int mode = 1;
		while (i < n)
			if (pS[i] == 3) {
				convert_flag = 0;
				i++;
			} else if (pS[i] == 6) {
				convert_flag = 1;
				i++;
			} else if (convert_flag == 0) {
				if (mode == 0) {
					pD[j++] = 15;
					mode = 1;
				}
				pD[j++] = pS[i++];
			} else if (pS[i] >= 32 && pS[i] < 128) {
				if (mode == 0) {
					pD[j] = 15;
					j++;
					mode = 1;
				}
				pD[j] = AsciiToEbcdBuf[pS[i]];
				i++;
				j++;
			} else if (pS[i] >= 161 && pS[i] <= 247 && pS[i + 1] >= 161
					&& pS[i + 1] <= 254) {
				if (mode == 1) {
					pD[j] = 14;
					j++;
					mode = 0;
				}
				pD[j] = pS[i];
				i++;
				j++;
				pD[j] = pS[i];
				i++;
				j--;
				pTmp[0] = pD[j];
				pTmp[1] = pD[j + 1];
				pTmp = encodeFromQuWei(pTmp);
				pD[j] = pTmp[0];
				pD[j + 1] = pTmp[1];
				j += 2;
			} else {
				pD[j] = 64;
				i++;
				j++;
			}
		if (mode == 0) {
			pD[j] = 15;
			j++;
		}
		pD[j] = 0;
		return intToByte(pD);
	}

	public static byte[] ebcdicToAscii5(byte bsrc_ebcd[], int nLen) {
		int current_row = 1;
		int current_col = 1;
		int mode = 0;
		int j = 0;
		@SuppressWarnings("unused")
		int dest_len = 1;
		int nIndex = 0;
		int src_ebcd[] = byteToInt(bsrc_ebcd);
		int dest_asc[] = new int[nLen];
		while (nIndex < nLen)
			label0: switch (src_ebcd[nIndex]) {
			case 52: // '4'
				if ((nIndex += 3) > nLen)
					break;
				switch (src_ebcd[nIndex - 2] & 0xc) {
				default:
					break;

				case 0: // '\0'
					if (current_col >= src_ebcd[nIndex - 1])
						break label0;
					for (int i = current_col; i < src_ebcd[nIndex - 1]; i++)
						dest_asc[j++] = 32;

					current_col = src_ebcd[nIndex - 1];
					break label0;

				case 4: // '\004'
					for (int i = current_row; i < src_ebcd[nIndex - 1]; i++)
						dest_asc[j++] = 10;

					current_row = src_ebcd[nIndex - 1];
					current_col = 1;
					break label0;

				case 8: // '\b'
					for (int i = 0; i < src_ebcd[nIndex - 1]; i++)
						dest_asc[j++] = 32;

					current_col += src_ebcd[nIndex - 1];
					break label0;

				case 12: // '\f'
					for (int i = 0; i < src_ebcd[nIndex - 1]; i++)
						dest_asc[j++] = 10;

					current_row += src_ebcd[nIndex - 1];
					current_col = 1;
					break;
				}
				break;

			case 12: // '\f'
				dest_asc[j++] = 12;
				nIndex++;
				current_row = 1;
				current_col = 1;
				break;

			case 21: // '\025'
				dest_asc[j++] = 10;
				nIndex++;
				current_row++;
				current_col = 1;
				break;

			case 17: // '\021'
				dest_asc[j++] = 17;
				nIndex++;
				break;

			case 18: // '\022'
				dest_asc[j++] = 18;
				nIndex++;
				break;

			case 14: // '\016'
				if (src_ebcd[nIndex + 1] == 40) {
					nIndex += 4;
					dest_asc[j++] = 32;
					current_col++;
				} else {
					nIndex++;
				}
				mode = 1;
				break;

			case 15: // '\017'
				if (src_ebcd[nIndex + 1] == 40) {
					nIndex += 4;
					dest_asc[j++] = 32;
					current_col++;
				} else {
					nIndex++;
				}
				mode = 0;
				break;

			default:
				if (mode == 0) {
					dest_asc[j++] = ebcToAsc(src_ebcd[nIndex]);
					nIndex++;
					current_col++;
					break;
				}
				int nTmp = nIndex;
				while (src_ebcd[nIndex] != 15 && src_ebcd[nIndex] != 18
						&& src_ebcd[nIndex] != 52)
					if (++nIndex > nLen) {
						dest_asc = byteSet(nLen, 32);
						return intToByte(dest_asc);
					}
				byte byTmp[] = new byte[nIndex - nTmp];
				System.arraycopy(src_ebcd, nTmp, byTmp, 0, nIndex - nTmp);
				byTmp = hzToAscii(byTmp);
				System.arraycopy(dest_asc, j, byTmp, 0, byTmp.length);
				j += nLen;
				current_col += nLen;
				break;
			}
		return intToByte(dest_asc);
	}

	public static int[] byteSet(int nLength, int ch) {
		int byIn[] = new int[nLength];
		for (int i = 0; i < nLength; i++)
			byIn[i] = ch;

		return byIn;
	}

	public static byte[] asciiToEbcdic(byte byS[]) {
		int pS[] = byteToInt(byS);
		return intToByte(asciiToEbcdic(pS));
	}

	public static int[] asciiToEbcdic(int byS[]) {
		int convert_flag = 1;
		int pS[] = byS;
		int n = pS.length;
		int pD[] = new int[n];
		int pTmp[] = new int[2];
		int i = 0;
		int j = 0;
		int mode = 1;
		while (i < n)
			if (pS[i] == 3) {
				convert_flag = 0;
				i++;
			} else if (pS[i] == 6) {
				convert_flag = 1;
				i++;
			} else if (convert_flag == 0) {
				if (mode == 0)
					mode = 1;
				pD[j++] = pS[i++];
			} else if (pS[i] >= 32 && pS[i] < 128) {
				if (mode == 0)
					mode = 1;
				pD[j] = AsciiToEbcdBuf[pS[i]];
				i++;
				j++;
			} else if (pS[i] >= 161 && pS[i] <= 247 && pS[i + 1] >= 161
					&& pS[i + 1] <= 254) {
				if (mode == 1)
					mode = 0;
				pTmp[0] = pS[i++];
				pTmp[1] = pS[i++];
				pTmp = encodeFromQuWei(pTmp);
				pD[j++] = pTmp[0];
				pD[j++] = pTmp[1];
			} else {
				pD[j] = 64;
				i++;
				j++;
			}
		return pD;
	}

	public static byte[] hzToAscii(byte byS[]) {
		int s[] = byteToInt(byS);
		int len = s.length;
		int pWret[] = new int[2];
		int d[] = new int[len];
		int i = 0;
		int j = 0;
		@SuppressWarnings("unused")
		int ratio = 17;
		while (i < len) {
			int L = s[i];
			int H = i + 1 >= len ? 0 : s[i + 1];
			if (L == 64 && H == 64) {
				d[j] = 32;
				d[j + 1] = 32;
				j += 2;
				i += 2;
			} else if (L >= 65 && L < 108 && H >= 65 && H <= 253 || L == 108
					&& H >= 65 && H <= 159) {
				pWret[0] = s[i];
				pWret[1] = s[i + 1];
				pWret = decodeToQuWei(pWret);
				int k;
				if (i < 2)
					k = i + 2;
				else
					k = i - 2;
				if (s[k] == 127 && s[k + 1] >= 65 && s[i - 1] <= 253
						|| s[i + 2] == 127 && s[i + 3] >= 65 && s[i + 3] <= 253) {
					ratio = 18;
					d[j] = (pWret[1] - 161) + 32 + 1;
					j++;
				} else {
					d[j] = pWret[0];
					d[j + 1] = pWret[1];
					j += 2;
				}
				i += 2;
			} else if (L == 127 && H >= 65 && H <= 253) {
				ratio = 18;
				s[i] = 66;
				pWret[0] = s[i];
				pWret[1] = s[i + 1];
				pWret = decodeToQuWei(pWret);
				d[j] = pWret[0];
				d[j + 1] = pWret[1];
				j += 2;
				i += 2;
			} else {
				int ch = s[i];
				if (ch >= 176 && ch <= 185)
					ch += 64;
				d[j++] = EbcdToAsciiBuf[ch];
				i++;
			}
		}
		return intToByte(d);
	}

	protected static int[] decodeToQuWei(int pW[]) {
		int pWret[] = new int[2];
		int j = 0;
		int Temp1;
		int Temp2;
		if (pW[0] == 64 && pW[1] == 64) {
			Temp1 = 32;
			Temp2 = 32;
		} else if (pW[0] < 72) {
			int word = pW[0] * 256 + pW[1];
			int i;
			for (i = 0; i < 9; i++) {
				for (j = 0; j < 94; j++)
					if (word == HanZi01To09Buf[i][j])
						break;

				if (j != 94)
					break;
			}

			if (i != 9) {
				Temp1 = i + 161;
				Temp2 = j + 161;
			} else {
				Temp1 = 32;
				Temp2 = 32;
			}
		} else {
			int M = (pW[0] - 64) * 2;
			if (pW[1] > 159) {
				Temp1 = M;
				Temp2 = pW[1] - 159;
			} else {
				Temp1 = M - 1;
				Temp2 = pW[1] - 64;
				if (pW[1] > 127)
					Temp2--;
			}
			Temp1 = (Temp1 + 161) - 1;
			Temp2 = (Temp2 + 161) - 1;
		}
		pWret[0] = Temp1;
		pWret[1] = Temp2;
		return pWret;
	}

	protected static int[] encodeFromQuWei(int pW[]) {
		int M = (pW[0] - 161) + 1;
		int Temp1;
		int Temp2;
		if (M > 15) {
			M = ((pW[0] - 161) + 1) / 2;
			if (((pW[0] - 161) + 1) % 2 == 0) {
				Temp1 = M + 64;
				Temp2 = (pW[1] - 161) + 1 + 159;
			} else {
				Temp1 = M + 65;
				if ((pW[1] - 161) + 1 > 63)
					Temp2 = (pW[1] - 161) + 1 + 65;
				else
					Temp2 = (pW[1] - 161) + 1 + 64;
			}
		} else if (M < 10) {
			int word = HanZi01To09Buf[M - 1][pW[1] - 161];
			Temp1 = hiByte(word);
			Temp2 = lowByte(word);
		} else {
			Temp1 = 64;
			Temp2 = 64;
		}
		pW[0] = Temp1;
		pW[1] = Temp2;
		return pW;
	}

	public static byte[] intToByte(int nIn[]) {
		byte byRet[] = new byte[nIn.length];
		int i = 0;
		for (int n = nIn.length; i < n; i++)
			byRet[i] = (byte) nIn[i];

		return byRet;
	}

	public static int[] byteToInt(byte byIn[]) {
		int nRet[] = new int[byIn.length];
		int i = 0;
		for (int n = byIn.length; i < n; i++)
			nRet[i] = byIn[i] & 0xff;

		return nRet;
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

	public static String fillChar(String sSource, char ch, int nLen,
			boolean bLeft) {
		int nSrcLen = sSource.length();
		if (nSrcLen <= nLen)
			if (bLeft) {
				for (int i = 0; i < nLen - nSrcLen; i++)
					sSource = ch + sSource;

			} else {
				for (int i = 0; i < nLen - nSrcLen; i++)
					sSource = sSource + ch;

			}
		return sSource;
	}

	@SuppressWarnings("unused")
	public static void main(String args[]) throws IOException {
		long lStart = System.currentTimeMillis();
		EbcdicTool ebt = new EbcdicTool();
		String sIn = "He$OPQllo,1234567890 中国, ，that is the end";
		try (FileInputStream fin = new FileInputStream(new File(
				"./icbcsz/AI12001.hpt"))) {
			byte byIn[] = new byte[395];
			fin.read(byIn);
			byte byRet[] = ebcdToAscii(byIn);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static final int Chinese = 0;
	static final int Ascii = 1;
	static final int IC_LL = 161;
	static final int IC_LH = 247;
	static final int IC_UL = 161;
	static final int IC_UH = 254;
	static final int EC_LL = 65;
	static final int EC_LH = 108;
	static final int EC_UL = 65;
	static final int EC_UH = 253;
	static final int EC_Max = 27807;
	static final int ASC_L = 32;
	static final int ASC_H = 128;
	static final int Chi_Begin = 14;
	static final int Chi_End = 15;
	static final int HZ_Begin = 14;
	static final int HZ_End = 15;
	static final int HZ_Begin_21 = 14;
	static final int HZ_Begin_22 = 17;
	static final int HZ_End_21 = 18;
	static final int HZ_End_22 = 15;
	static final int HZ_Begin_41 = 14;
	static final int HZ_Begin_42 = 40;
	static final int HZ_Begin_43 = 193;
	static final int HZ_Begin_44 = 64;
	static final int HZ_End_41 = 15;
	static final int HZ_End_42 = 40;
	static final int HZ_End_43 = 193;
	static final int HZ_End_44 = 64;
	static final int CHAR_BIT = 8;
	@SuppressWarnings("unused")
	private List<Object> lsAscEbc;
	static final int HanZi01To09Buf[][] = {
			{ 16448, 17220, 17217, 17221, 17733, 17734, 17504, 17499, 17501,
					17482, 17313, 17532, 17535, 17505, 17521, 17506, 17522,
					17507, 17523, 17508, 17524, 17509, 17525, 17218, 17219,
					17474, 17475, 17755, 17756, 17510, 17526, 17483, 17530,
					17531, 17762, 17763, 17764, 17765, 17766, 17767, 17768,
					17769, 17770, 17771, 17772, 17773, 17774, 17775, 17776,
					17777, 17778, 17779, 17780, 17781, 17782, 17783, 17484,
					17785, 17786, 17511, 17527, 17485, 17528, 17512, 17513,
					17529, 17645, 17646, 17647, 17486, 17120, 17800, 17226,
					16970, 17803, 17514, 17518, 17637, 17638, 17632, 17633,
					17636, 17639, 17640, 17641, 17642, 17634, 17635, 17515,
					17648, 17649, 17650, 17651, 17533 },
			{ 16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 17841,
					17842, 17843, 17844, 17845, 17846, 17847, 17848, 17849,
					17850, 17851, 17852, 17853, 17854, 17855, 17856, 17857,
					17858, 17859, 17860, 17861, 17862, 17863, 17864, 17865,
					17866, 17867, 17868, 17869, 17870, 17871, 17872, 17873,
					17874, 17875, 17876, 17877, 17878, 17879, 17880, 17889,
					17890, 17891, 17892, 17893, 17894, 17895, 17896, 17897,
					17898, 16448, 16448, 17905, 17906, 17907, 17908, 17909,
					17910, 17911, 17912, 17913, 17914, 16448, 16448, 16881,
					16882, 16883, 16884, 16885, 16886, 16887, 16888, 16889,
					16890, 16891, 16892, 16448, 16448 },
			{ 16986, 17023, 17019, 17243, 17004, 16976, 17488, 16973, 16989,
					16988, 16974, 17003, 16992, 16971, 16993, 17136, 17137,
					17138, 17139, 17140, 17141, 17142, 17143, 17144, 17145,
					17018, 16990, 16972, 17022, 17006, 17007, 17020, 17089,
					17090, 17091, 17092, 17093, 17094, 17095, 17096, 17097,
					17105, 17106, 17107, 17108, 17109, 17110, 17111, 17112,
					17113, 17122, 17123, 17124, 17125, 17126, 17127, 17128,
					17129, 17476, 17376, 17477, 17520, 17005, 17017, 17025,
					17026, 17027, 17028, 17029, 17030, 17031, 17032, 17033,
					17041, 17042, 17043, 17044, 17045, 17046, 17047, 17048,
					17049, 17058, 17059, 17060, 17061, 17062, 17063, 17064,
					17065, 17088, 16975, 17104, 17057 },
			{ 17479, 17537, 17480, 17538, 17481, 17539, 17489, 17540, 17490,
					17541, 17542, 17600, 17543, 17601, 17544, 17602, 17545,
					17603, 17546, 17604, 17548, 17605, 17549, 17606, 17550,
					17607, 17551, 17608, 17552, 17609, 17553, 17610, 17554,
					17611, 17494, 17555, 17612, 17556, 17613, 17557, 17614,
					17558, 17559, 17560, 17561, 17562, 17565, 17615, 17621,
					17566, 17616, 17622, 17567, 17617, 17623, 17570, 17618,
					17624, 17571, 17619, 17625, 17572, 17573, 17574, 17575,
					17576, 17491, 17577, 17492, 17578, 17493, 17580, 17581,
					17582, 17583, 17594, 17595, 17495, 17596, 17626, 17627,
					17478, 17597, 16448, 16817, 16818, 16819, 16820, 16821,
					16822, 16823, 16824, 16825, 16826 },
			{ 17223, 17281, 17224, 17282, 17225, 17283, 17233, 17284, 17234,
					17285, 17286, 17344, 17287, 17345, 17288, 17346, 17289,
					17347, 17290, 17348, 17292, 17349, 17293, 17350, 17294,
					17351, 17295, 17352, 17296, 17353, 17297, 17354, 17298,
					17355, 17238, 17299, 17356, 17300, 17357, 17301, 17358,
					17302, 17303, 17304, 17305, 17306, 17309, 17359, 17365,
					17310, 17360, 17366, 17311, 17361, 17367, 17314, 17362,
					17368, 17315, 17363, 17369, 17316, 17317, 17318, 17319,
					17320, 17235, 17321, 17236, 17322, 17237, 17324, 17325,
					17326, 17327, 17338, 17339, 17239, 17340, 17370, 17371,
					17222, 17341, 17364, 17241, 17242, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448 },
			{ 16737, 16738, 16739, 16740, 16741, 16742, 16743, 16744, 16745,
					16746, 16747, 16748, 16749, 16750, 16751, 16752, 16753,
					16754, 16755, 16756, 16757, 16758, 16759, 16760, 16448,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16705,
					16706, 16707, 16708, 16709, 16710, 16711, 16712, 16713,
					16714, 16715, 16716, 16717, 16718, 16719, 16720, 16721,
					16722, 16723, 16724, 16725, 16726, 16727, 16728, 16448,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448 },
			{ 16832, 16833, 16834, 16835, 16836, 16837, 16838, 16839, 16840,
					16841, 16842, 16843, 16844, 16845, 16846, 16847, 16848,
					16849, 16850, 16851, 16852, 16853, 16854, 16855, 16856,
					16857, 16858, 16859, 16860, 16861, 16862, 16863, 16864,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16768,
					16769, 16770, 16771, 16772, 16773, 16774, 16775, 16776,
					16777, 16778, 16779, 16780, 16781, 16782, 16783, 16784,
					16785, 16786, 16787, 16788, 16789, 16790, 16791, 16792,
					16793, 16794, 16795, 16796, 16797, 16798, 16799, 16800,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448 },
			{ 17985, 17986, 17987, 17988, 17989, 17990, 17991, 17992, 17993,
					17994, 17995, 17996, 17997, 17998, 17999, 18000, 18001,
					18002, 18003, 18004, 18005, 18006, 18007, 18008, 18009,
					18010, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 18021, 18022, 18023, 18024, 18025,
					18026, 18027, 18028, 18029, 18030, 18031, 18032, 18033,
					18034, 18035, 18036, 18037, 18038, 18039, 18040, 18041,
					18042, 18043, 18044, 18045, 18046, 18047, 18048, 18049,
					18050, 18051, 18052, 18053, 18054, 18055, 18056, 18057,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448 },
			{ 16448, 16448, 16448, 18084, 18085, 18086, 18087, 18088, 18089,
					18090, 18091, 18092, 18093, 18094, 18095, 18096, 18097,
					18098, 18099, 18100, 18101, 18102, 18103, 18104, 18105,
					18106, 18107, 18108, 18109, 18110, 18111, 18112, 18113,
					18114, 18115, 18116, 18117, 18118, 18119, 18120, 18121,
					18122, 18123, 18124, 18125, 18126, 18127, 18128, 18129,
					18130, 18131, 18132, 18133, 18134, 18135, 18136, 18137,
					18138, 18139, 18140, 18141, 18142, 18143, 18144, 18145,
					18146, 18147, 18148, 18149, 18150, 18151, 18152, 18153,
					18154, 18155, 18156, 18157, 18158, 18159, 16448, 16448,
					16448, 16448, 16448, 16448, 16448, 16448, 16448, 16448,
					16448, 16448, 16448, 16448, 16448 } };
	static final int AsciiToEbcdBuf[] = { 0, 1, 2, 3, 55, 45, 46, 47, 22, 5,
			37, 11, 12, 13, 14, 15, 16, 17, 18, 19, 60, 61, 50, 38, 24, 25, 63,
			39, 28, 29, 30, 31, 64, 79, 127, 123, 91, 108, 80, 125, 77, 93, 92,
			78, 107, 96, 75, 97, 240, 241, 242, 243, 244, 245, 246, 247, 248,
			249, 122, 94, 76, 126, 110, 111, 124, 193, 194, 195, 196, 197, 198,
			199, 200, 201, 209, 210, 211, 212, 213, 214, 215, 216, 217, 226,
			227, 228, 229, 230, 231, 232, 233, 74, 224, 90, 95, 109, 121, 129,
			130, 131, 132, 133, 134, 135, 136, 137, 145, 146, 147, 148, 149,
			150, 151, 152, 153, 162, 163, 164, 165, 166, 167, 168, 169, 192,
			106, 208, 161, 7, 32, 33, 34, 35, 36, 21, 6, 23, 40, 41, 42, 43,
			44, 9, 10, 27, 48, 49, 26, 51, 52, 53, 54, 8, 56, 57, 58, 59, 4,
			20, 62, 225, 65, 66, 67, 68, 69, 70, 71, 72, 73, 81, 82, 83, 84,
			85, 86, 87, 88, 89, 98, 99, 100, 101, 102, 103, 104, 105, 112, 113,
			114, 115, 116, 117, 118, 119, 120, 128, 138, 139, 140, 141, 142,
			143, 144, 154, 155, 156, 157, 158, 159, 160, 170, 171, 172, 173,
			174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186,
			187, 188, 189, 190, 191, 202, 203, 204, 205, 206, 207, 218, 219,
			220, 221, 222, 223, 234, 235, 236, 237, 238, 239, 250, 251, 252,
			253, 254, 255 };
	static int EbcdToAsciiBuf[];

	static {
		EbcdToAsciiBuf = new int[256];
		for (int i = 0; i < 256; i++)
			EbcdToAsciiBuf[AsciiToEbcdBuf[i]] = i;

		EbcdToAsciiBuf[0] = 32;
	}
}
