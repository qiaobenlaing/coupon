package cn.suanzi.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;

public class SocketTool {
	public static byte[] readStream(InputStream stream) throws IOException {
		byte[] byRet = new byte[0];
		InputStream in;
		if ((stream instanceof BufferedInputStream)) {
			in = (BufferedInputStream) stream;
		} else {
			in = new BufferedInputStream(stream);
		}
		byte[] byRead = new byte[4056];
		int nRead = 0;
		try {
			while ((nRead = in.read(byRead)) > 0) {
				if (nRead < 4056) {
					byte[] byNew = new byte[nRead];
					for (int i = 0; i < nRead; i++) {
						byNew[i] = byRead[i];
					}
					byRet = addBytes(byRet, byNew);
					break;
				}
				byRet = addBytes(byRet, byRead);
			}
		} catch (EOFException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw e;
		}
		if ((nRead < 0) && (byRet.length == 0)) {
			throw new IOException("对方连接已关闭");
		}
		return byRet;
	}

	public static byte[] read(InputStream in, int len) throws IOException {
		byte[] byRead = new byte[len];
		int i = 0;
		int ch;
		while ((ch = in.read()) > 0) {
			byRead[(i++)] = ((byte) ch);
		}
		if (i < len) {
			byte[] ret = new byte[i];
			System.arraycopy(byRead, 0, ret, 0, i);
			return ret;
		}
		return byRead;
	}

	/**
	 * 将2个字节码数组合并为1个字节码数组
	 * @param bySrc 源字节码数据
	 * @param byAdd 附加字节码数据
	 * @return 源字节码数据+附加字节码数据
	 */
	public static byte[] addBytes(byte[] bySrc, byte[] byAdd) {
		int nSrc = bySrc.length;
		byte[] byRet = new byte[bySrc.length + byAdd.length];
//		int i = 0;
//		for (int n = bySrc.length; i < n; i++) {
//			byRet[i] = bySrc[i];
//		}
//		i = 0;
//		for (int n = byAdd.length; i < n; i++) {
//			byRet[(i + nSrc)] = byAdd[i];
//		}
		for (int i = 0; i < nSrc; ++i)
			byRet[i] = bySrc[i];
		for (int i = 0; i < byAdd.length; ++i)
			byRet[i + nSrc] = byAdd[i];
		return byRet;
	}

	public static byte[] exchange(String sIp, int nPort, byte[] byData,
			int nTimeOut) throws IOException {
		Socket sock = new Socket(sIp, nPort);
		sock.setSoTimeout(nTimeOut);
		BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				sock.getOutputStream()));
		out.write(byData, 0, byData.length);
		out.flush();
		byte[] byRecv = readStream(in);
		in.close();
		out.close();
		sock.close();
		return byRecv;
	}

	public static byte[] exchangeFix(String sIp, int nPort, byte[] byData,
			int nTimeOut) throws IOException {
		Socket sock = new Socket(sIp, nPort);
		sock.setSoTimeout(nTimeOut);
		DataInputStream in = new DataInputStream(new BufferedInputStream(
				sock.getInputStream()));
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				sock.getOutputStream()));
		out.writeInt(byData.length);
		out.write(byData, 0, byData.length);
		out.flush();
		int nRecv = in.readInt();
		byte[] byRecv = new byte[nRecv];

		in.readFully(byRecv, 0, nRecv);
		in.close();
		out.close();
		sock.close();
		return byRecv;
	}

	public static byte[] exchangeFix(Socket sock, byte[] byData, int nTimeOut)
			throws IOException {
		sock.setSoTimeout(nTimeOut);
		DataInputStream in = new DataInputStream(new BufferedInputStream(
				sock.getInputStream()));
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				sock.getOutputStream()));
		out.writeInt(byData.length);
		out.write(byData, 0, byData.length);
		out.flush();
		int nRecv = in.readInt();
		byte[] byRecv = new byte[nRecv];

		in.readFully(byRecv, 0, nRecv);
		return byRecv;
	}

	public static byte[] exchange(Socket sock, byte[] byData, int nTimeOut)
			throws IOException {
		sock.setSoTimeout(nTimeOut);
		BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				sock.getOutputStream()));
		out.write(byData, 0, byData.length);
		out.flush();
		byte[] byRecv = readStream(in);
		return byRecv;
	}

	/**
	 * 读取流直至给定结尾符
	 * 
	 * @param in
	 * @param endTags
	 * @return
	 * @throws IOException
	 */
	public static byte[] readStream(InputStream in, byte[] endTags)
			throws IOException {
		if (endTags == null || endTags.length == 0) {
			return readStream(in);
		}
		byte[] out = new byte[0];
		byte[] ret = (byte[]) null;
		//String endTag = new String(endTags);
		int subStrFlag = 0;
		do {
			ret = readStream(in);
			//String retStr = new String(ret);
			//subStrFlag = retStr.indexOf(endTag);
			subStrFlag = findEndPos(ret, endTags);
			if (subStrFlag == -1) {
				out = addBytes(out, ret);
			} else {
				//String temp = retStr.substring(0, subStrFlag + endTag.length());
				//out = addBytes(out, temp.getBytes()); // here is the problem
				out = addBytes(out, Arrays.copyOf(ret, subStrFlag + endTags.length));
			}
		} while (subStrFlag < 0);
		
		return out;
	}
	
	/**
	 * 类似于String.indexOf()，查找给定子字符串needle在目标字符串target中的位置。
	 * 
	 * @param target 目标字符串
	 * @param needle 子字符串
	 * @return 如找到，返回匹配的第一个字符的位置；否则返回-1。<br/>
	 * 例1：findEndPos("ABC".getBytes(), "BC".getBytes())返回1。<br/>
	 * 例2：findEndPos("ABC".getBytes(), "BV".getBytes())返回-1。
	 */
	public static int findEndPos(byte[] target, byte[] needle) {
		int subfixLen = needle.length;
		boolean found = false;
		int pos = 0;
		for (; !found  && (pos < target.length - subfixLen + 1); ++pos) {
			boolean match = true;
			for (int j = 0; j < subfixLen; ++j) {
				if (target[pos + j] != needle[j]) {
					match = false;
					break;
				}
			}
			if (match)
				return pos;
		}
		return -1;
	}
	

	public static byte[] newReadFully(InputStream in) throws IOException {
		byte[] temp = new byte[10240];
		int n = 0;
		for (;;) {
			byte[] readByte = new byte[10240];
			int count = in.read(readByte);
			if (count < 0) {
				break;
			}
			System.arraycopy(readByte, 0, temp, n, count);
			n += count;
		}
		byte[] returnByte = new byte[n];
		System.arraycopy(temp, 0, returnByte, 0, n);
		return returnByte;
	}
}
