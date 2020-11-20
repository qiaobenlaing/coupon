

package cn.suanzi.tools;

import java.util.*;



public class AsopUpTool {

	public AsopUpTool() {
		lstData = new ArrayList<String>();
	}

	public boolean analyze(byte byIn[], String sDeli) {
		init();
		boolean bUp = false;
		for (int i = 0; i < PACK_DOWN.length; i++) {
			if (byIn[i] == PACK_DOWN[i])
				continue;
			bUp = true;
			break;
		}

		setTranferDirection(bUp);
		if (bUp) {
			int nData = 0;
			int i = 0;
			for (int n = byIn.length; i < n; i++) {
				byte b = byIn[i];
				if (b == 3)
					if (byIn[i + 1] == 4 && byIn[i + 2] == 5
							&& byIn[i + 3] == 6)
						nData = i + 3;
					else
						return false;
			}

			sHead = new String(byIn, 0, nData - 3);
			String sHeads[] = StringTool.getTokens(sHead, " ");
			sTradeCode = sHeads[0];
			sBranch = sHeads[1];
			sTeller = sHeads[2];
			sData = new String(byIn, nData + 1, byIn.length - nData - 1);
			tokens = StringTool.getTokens(sData, sDeli);
			i = 0;
			for (int n = tokens.length; i < n; i++)
				lstData.add(tokens[i]);

			nSize = tokens.length;
			return true;
		}
		byte crc = byIn[17];
		if (crc != 128)
			return false;
		byte correct = byIn[16];
		@SuppressWarnings("unused")
		byte multypack = byIn[18];
		if (correct == 0)
			setPackType(true);
		else if (correct == 255)
			setPackType(false);
		else
			return false;
		sData = new String(byIn, 19, byIn.length - 19);
		tokens = StringTool.getTokens(sData, sDeli);
		int i = 0;
		for (int n = tokens.length; i < n; i++)
			lstData.add(tokens[i]);

		nSize = tokens.length;
		return true;
	}

	public void setPackType(boolean bCorrect) {
		m_bCorrect = bCorrect;
	}

	public boolean getPackType() {
		return m_bCorrect;
	}

	public boolean getTranferDirection() {
		return m_bUp;
	}

	public void setTranferDirection(boolean bUp) {
		m_bUp = bUp;
	}

	public String getTradeCode() {
		return sTradeCode;
	}

	public String getBranch() {
		return sBranch;
	}

	public String getTeller() {
		return sTeller;
	}

	public String getDataOf(int nIndex) {
		return (String) lstData.get(nIndex);
	}

	public int size() {
		return lstData.size();
	}

	protected void init() {
		sHead = null;
		sData = null;
		sTradeCode = null;
		sTeller = null;
		sBranch = null;
		tokens = null;
		nSize = 0;
		lstData.clear();
	}

	public void setHead(String sTradeCode, String sBranch, String sTeller) {
		init();
		this.sTradeCode = sTradeCode;
		this.sBranch = sBranch;
		this.sTeller = sTeller;
	}

	public void addPack(String sData) {
		lstData.add(sData);
	}

	public byte[] pack(String sDeli) {
		if (sTradeCode == null || sTeller == null || sBranch == null
				|| lstData.size() == 0)
			return null;
		byte byHead[] = (sTradeCode + " " + sTeller + " " + sBranch + " 0 0 0")
				.getBytes();
		byHead = StringTool.addBytes(byHead, PACK_SEPERATOR);
		Iterator<String> iter = lstData.iterator();
		StringBuffer buff = new StringBuffer();
		for (; iter.hasNext(); buff.append(sDeli))
			buff.append((String) iter.next());

		byte byData[] = buff.toString().getBytes();
		byHead = StringTool.addBytes(byHead, byData);
		return byHead;
	}

	public byte[] getAnswer(int nType) {
		byte byHead[] = (byte[]) null;
		if (sTradeCode == null || sTeller == null || sBranch == null)
			byHead = "8888 0000 0000 0 0 0".getBytes();
		else
			byHead = (sTradeCode + " " + sTeller + " " + sBranch + " 0 0 0")
					.getBytes();
		if (nType == ERROR_ANSWER)
			byHead = StringTool.addBytes(byHead, PACK_CORRECT);
		else
			byHead = StringTool.addBytes(byHead, PACK_CORRECT);
		return byHead;
	}

	public static int CORRECT_ANSWER = 0;
	public static int ERROR_ANSWER = 1;
	public static byte PACK_SEPERATOR[] = { 3, 4, 5, 6 };
	public static byte PACK_ERROR[] = { 7, 8, 9, 10 };
	public static byte PACK_CORRECT[] = { 11, 12, 13, 14 };
	public static byte PACK_DOWN[] = new byte[14];
	private String sHead;
	private String sData;
	private String sTradeCode;
	private String sTeller;
	private String sBranch;
	private String tokens[];
	@SuppressWarnings("unused")
	private int nSize;
	private List<String> lstData;
	private boolean m_bUp;
	private boolean m_bCorrect;

}
