package cn.suanzi.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CrcNatp {
	public static short crc_NATP(byte[] byBuff, int nLen) {
		short MTT = 4129;

		short crctt = 0;
		for (int i = 0; i < nLen; i++) {
			int ch = byBuff[i];
			crctt = updcrc(crctt, ch, MTT);
		}
		return crctt;
	}

	protected static short updcrc(short crc, int c, short mask) {
		c <<= 8;
		for (int i = 0; i < 8; i++) {
			if (((crc ^ c) & 0x8000) != 0) {
				crc = (short) (crc << 1 ^ mask);
			} else {
				crc = (short) (crc << 1);
			}
			c <<= 1;
		}
		return crc;
	}

	public static boolean crcCheck(byte[] byIn) {
		try {
			short crc = crc_NATP(byIn, byIn.length - 2);
			DataInputStream din = new DataInputStream(new ByteArrayInputStream(
					byIn));
			din.skip(byIn.length - 2);
			short crcReal = din.readShort();
			if (crcReal != crc) {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static byte[] crcAppend(byte[] byIn) {
		try {
			short crc = crc_NATP(byIn, byIn.length);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			DataOutputStream dout = new DataOutputStream(bo);
			dout.write(byIn, 0, byIn.length);
			dout.writeShort(crc);
			return bo.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}
}