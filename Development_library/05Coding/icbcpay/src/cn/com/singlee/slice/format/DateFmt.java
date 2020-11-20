package cn.com.singlee.slice.format;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFmt {
	public static byte[] fmtDate(byte[] bInt, String params) {
		int len = params.length();
		int dot = params.indexOf(",");
		byte[] bt = new byte[len - dot - 1];
		for (int i = dot + 1; i < len; i++) {
			bt[(i - dot - 1)] = ((byte) params.charAt(i));
		}
		String BDateFormat = params.substring(0, dot);
		int bsy = BDateFormat.indexOf("y");
		int bey = BDateFormat.lastIndexOf("y");
		int bsm = BDateFormat.indexOf("M");
		int bem = BDateFormat.lastIndexOf("M");
		int bsd = BDateFormat.indexOf("d");
		int bed = BDateFormat.lastIndexOf("d");

		int bynum = bey - bsy + 1;
		int bmnum = bem - bsm + 1;
		int bdnum = bed - bsd + 1;

		String ADateFormat = params.substring(dot + 1);
		int sy = ADateFormat.indexOf("y");
		int ey = ADateFormat.lastIndexOf("y");
		int sm = ADateFormat.indexOf("M");
		int em = ADateFormat.lastIndexOf("M");
		int sd = ADateFormat.indexOf("d");
		int ed = ADateFormat.lastIndexOf("d");

		int ynum = ey - sy + 1;
		int mnum = em - sm + 1;
		int dnum = ed - sd + 1;
		if ((sy != -1) && (bsy != -1)) {
			if (ynum == bynum) {
				int i = sy;
				int j = bsy;
				do {
					bt[i] = bInt[j];
					i++;
					j++;
					if (i > ey) {
						break;
					}
				} while (j <= bey);
			} else if (ynum == bynum + 2) {
				bt[sy] = 50;
				bt[(sy + 1)] = 48;
				int i = sy + 2;
				int j = bsy;
				do {
					bt[i] = bInt[j];
					i++;
					j++;
					if (i > ey) {
						break;
					}
				} while (j <= bey);
			} else if (ynum == bynum - 2) {
				int i = sy;
				int j = bsy + 2;
				do {
					bt[i] = bInt[j];
					i++;
					j++;
					if (i > ey) {
						break;
					}
				} while (j <= bey);
			} else {
				System.err.println("日期参数不正确！");
			}
		} else if ((sy != -1) && (bsy == -1)) {
			byte[] ybt = new byte[2];
			SimpleDateFormat date = new SimpleDateFormat("yy");
			ybt = date.format(new Date()).getBytes();
			if (ynum == 2) {
				bt[sy] = ybt[0];
				bt[(sy + 1)] = ybt[1];
			} else if (ynum == 4) {
				bt[sy] = 50;
				bt[(sy + 1)] = 48;
				bt[(sy + 2)] = ybt[0];
				bt[(sy + 3)] = ybt[1];
			} else {
				System.err.println("日期参数不正确！");
			}
		}
		if (sm != -1) {
			if (mnum == bmnum) {
				int i = sm;
				int j = bsm;
				do {
					bt[i] = bInt[j];
					i++;
					j++;
					if (i > em) {
						break;
					}
				} while (j <= bem);
			} else if (mnum == bmnum + 1) {
				bt[sm] = 48;
				bt[(sm + 1)] = bInt[bem];
			} else if (mnum == bmnum - 1) {
				bt[sm] = bInt[bem];
			} else {
				System.err.println("日期参数不正确！");
			}
		}
		if (sd != -1) {
			if (dnum == bdnum) {
				int i = sd;
				int j = bsd;
				do {
					bt[i] = bInt[j];
					i++;
					j++;
					if (i > ed) {
						break;
					}
				} while (j <= bed);
			} else if (dnum == bdnum + 1) {
				bt[sd] = 48;
				bt[(sd + 1)] = bInt[bed];
			} else if (dnum == bdnum - 1) {
				bt[sd] = bInt[bed];
			} else {
				System.err.println("日期参数不正确！");
			}
		}
		return bt;
	}

	public static byte[] fmtTime(byte[] bInt, String params) {
		int len = params.length();
		int dot = params.indexOf(",");
		byte[] bt = new byte[len - dot - 1];
		for (int i = dot + 1; i < len; i++) {
			bt[(i - dot - 1)] = ((byte) params.charAt(i));
		}
		String BTimeFormat = params.substring(0, dot);
		int bsh = BTimeFormat.indexOf("h");
		int beh = BTimeFormat.lastIndexOf("h");
		int bsm = BTimeFormat.indexOf("m");
		int bem = BTimeFormat.lastIndexOf("m");
		int bss = BTimeFormat.indexOf("s");
		int bes = BTimeFormat.lastIndexOf("s");
		@SuppressWarnings("unused")
		int bhnum = beh - bsh + 1;
		@SuppressWarnings("unused")
		int bmnum = bem - bsm + 1;
		@SuppressWarnings("unused")
		int bsnum = bes - bss + 1;

		String ATimeFormat = params.substring(dot + 1);
		int ash = ATimeFormat.indexOf("h");
		int aeh = ATimeFormat.lastIndexOf("h");
		int asm = ATimeFormat.indexOf("m");
		int aem = ATimeFormat.lastIndexOf("m");
		int ass = ATimeFormat.indexOf("s");
		int aes = ATimeFormat.lastIndexOf("s");
		@SuppressWarnings("unused")
		int ahnum = aeh - ash + 1;
		@SuppressWarnings("unused")
		int amnum = aem - asm + 1;
		@SuppressWarnings("unused")
		int asnum = aes - ass + 1;
		if (ash != -1) {
			int i = ash;
			for (int j = bsh; (i <= aeh) && (j <= beh); j++) {
				bt[i] = bInt[j];
				i++;
			}
		}
		if (asm != -1) {
			int i = asm;
			for (int j = bsm; (i <= aem) && (j <= bem); j++) {
				bt[i] = bInt[j];
				i++;
			}
		}
		if (ass != -1) {
			int i = ass;
			for (int j = bss; (i <= aes) && (j <= bes); j++) {
				bt[i] = bInt[j];
				i++;
			}
		}
		return bt;
	}

	public static byte[] fmtDateAndTime(byte[] bInt, String params) {
		String s = new String(fmtDate(bInt, params));
		params = params.substring(0, params.indexOf(",") + 1) + s;
		return fmtTime(bInt, params);
	}

	public static byte[] fmtSysDate(byte[] bInt, String params) {
		SimpleDateFormat date = new SimpleDateFormat(params);
		return date.format(new Date()).getBytes();
	}
}
