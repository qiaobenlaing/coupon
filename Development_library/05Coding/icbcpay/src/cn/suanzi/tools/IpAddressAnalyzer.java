

package cn.suanzi.tools;

import java.util.*;



public class IpAddressAnalyzer {

	public IpAddressAnalyzer() {
		m_lstSingleIp = new ArrayList<String>();
		m_lstClusterIp = new ArrayList<String>();
	}

	public void init() {
		m_lstSingleIp.clear();
		m_lstClusterIp.clear();
	}

	public boolean addIpAddress(String sIp) {
		int nType = checkIpType(sIp);
		if (nType == NONE_IPADDRESS)
			return false;
		if (nType == SINGLE_IPADDRESS)
			m_lstSingleIp.add(sIp);
		else if (nType == CLUSTER_IPADDRESS)
			m_lstClusterIp.add(sIp);
		else if (nType == RANGE_IPADDRESS) {
			String sMaxminIP[] = StringTool.getTokens(sIp, "-");
			String sMaxIp = sMaxminIP[0];
			String sMinIp = sMaxminIP[1];
			String sTmp[] = StringTool.getTokens(sMaxIp, ".");
			int iStart = Integer.parseInt(sTmp[3]);
			String sTmp2[] = StringTool.getTokens(sMinIp, ".");
			int iEnd = Integer.parseInt(sTmp2[3]);
			for (int i = iStart; i <= iEnd; i++) {
				sIp = sTmp[0] + "." + sTmp[1] + "." + sTmp[2] + "." + i;
				m_lstClusterIp.add(sIp);
			}

		} else {
			return false;
		}
		return true;
	}

	public int checkIpType(String sIp) {
		if (sIp == null || sIp.equals(""))
			return NONE_IPADDRESS;
		String sTokens[] = StringTool.getTokens(sIp, "-");
		if (sTokens.length <= 1) {
			sTokens = StringTool.getTokens(sIp, ".");
			int nNum = sTokens.length;
			if (nNum == 4 || nNum == 6) {
				if (sIp.indexOf("*") < 0)
					return SINGLE_IPADDRESS;
				else
					return CLUSTER_IPADDRESS;
			} else {
				return NONE_IPADDRESS;
			}
		}
		if (sTokens.length == 2) {
			if (checkIpType(sTokens[0]) == SINGLE_IPADDRESS
					&& checkIpType(sTokens[1]) == SINGLE_IPADDRESS)
				return RANGE_IPADDRESS;
			else
				return NONE_IPADDRESS;
		} else {
			return NONE_IPADDRESS;
		}
	}

	public boolean isIpAddressIn(String sIp) {
		int nType = checkIpType(sIp);
		if (nType != SINGLE_IPADDRESS)
			return false;
		for (Iterator<String> iter = m_lstClusterIp.iterator(); iter.hasNext();) {
			String sIpCluster = (String) iter.next();
			if (isIpAddressInCluster(sIpCluster, sIp))
				return true;
		}

		for (Iterator<String> iter = m_lstSingleIp.iterator(); iter.hasNext();) {
			String sCurIp = (String) iter.next();
			if (sCurIp.equals(sIp))
				return true;
		}

		return false;
	}

	public boolean isIpAddressInCluster(String sIpCluster, String sIp) {
		String sCluster[] = StringTool.getTokens(sIpCluster, ".");
		String sTokens[] = StringTool.getTokens(sIp, ".");
		if (sTokens.length != 4 || sCluster.length != 4)
			return false;
		for (int i = 0; i < 4; i++)
			if (!sCluster[i].equals("*") && !sCluster[i].equals(sTokens[i]))
				return false;

		return true;
	}

	public static void main(String args[]) {
		IpAddressAnalyzer ia = new IpAddressAnalyzer();
		ia.init();
		ia.addIpAddress("10.128.160.1");
		ia.addIpAddress("10.128.160.111");
		ia.addIpAddress("10.128.18.6-10.128.18.9");
	}

	private List<String> m_lstSingleIp;
	private List<String> m_lstClusterIp;
	private static int NONE_IPADDRESS = -1;
	private static int SINGLE_IPADDRESS = 1;
	private static int CLUSTER_IPADDRESS = 2;
	private static int RANGE_IPADDRESS = 3;

}
