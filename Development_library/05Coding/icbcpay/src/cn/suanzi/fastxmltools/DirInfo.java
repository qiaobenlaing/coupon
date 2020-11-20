package cn.suanzi.fastxmltools;

import java.util.HashMap;
import java.util.Map;

public class DirInfo {

	int nPos = -1;
	boolean bPosAhead = true;
	String sDir = null;
	String sDirName = null;
	String sDirAttr = null;
	Map<String, String> mapAttr = new HashMap<>();

	public DirInfo() {
	}

	public void init(String sDir) throws ElementNameInvalidException,
			AttrInvalidException {
		this.sDir = sDir;
		int nL = sDir.indexOf("[");
		int n = sDir.indexOf("'");
		int nR = sDir.indexOf("]");
		if ((nL < n) && (n < nR)) {
			n = sDir.indexOf("'", n + 1);
			if (n > nR) {
				nR = sDir.indexOf("]", n);
			}
		}
		int nL1 = sDir.indexOf("[", nR);
		int nR1 = sDir.indexOf("]", nL1);
		try {
			if ((nL > nR) || (nL1 > nR1)) {
				throw new ElementNameInvalidException("\"... " + sDir
						+ " ...\" 路径名非法");
			}
			if (nL < 0) {
				this.sDirName = sDir;
			} else if (nL > 0) {
				this.sDirName = sDir.substring(0, nL);
				String sTmp = sDir.substring(nL + 1, nR);
				if (sTmp.indexOf("@") >= 0) {
					this.sDirAttr = getDirAttr(sDir);
					this.mapAttr = getAttrMap(sTmp);
					if (nL < nL1) {
						this.bPosAhead = false;
						this.nPos = Integer.parseInt(sDir.substring(nL1 + 1,
								nR1));
					}
				} else {
					this.bPosAhead = true;
					this.nPos = Integer.parseInt(sTmp);
					if (nL < nL1) {
						this.mapAttr = getAttrMap(sDir.substring(nL1 + 1, nR1));
					}
				}
			} else {
				throw new ElementNameInvalidException("\"... " + sDir
						+ " ...\" 路径名非法");
			}
			if (this.nPos == 0) {
				this.nPos = -1;
			}
		} catch (AttrInvalidException e) {
			throw new AttrInvalidException("\"... " + sDir + " ...\", "
					+ e.getMessage());
		} catch (NumberFormatException e) {
			throw new ElementNameInvalidException("\"... " + sDir
					+ " ...\" 路径名非法:" + e);
		}
	}

	public DirInfo(String sDir) throws ElementNameInvalidException,
			AttrInvalidException {
		init(sDir);
	}

	public static String getDirAttr(String sDir) {
		int nL = sDir.indexOf("[");
		int nR = sDir.indexOf("]");
		if ((nL < 0) || (nR < 0)) {
			return null;
		}
		String sTmp = sDir.substring(nL + 1, nR).trim();
		if (!sTmp.startsWith("@")) {
			int nL1 = sDir.lastIndexOf("[");
			int nR1 = sDir.lastIndexOf("]");
			if ((nL1 < 0) || (nR1 < 0)) {
				return null;
			}
			sTmp = sDir.substring(nL1 + 1, nR1).trim();
			if (!sTmp.startsWith("@")) {
				return null;
			}
			return sTmp;
		}
		return null;
	}

	public Map<String, String> getAttrMap(String sAttrs)
			throws AttrInvalidException {
		sAttrs = sAttrs.trim();
		this.mapAttr.clear();
		int nAT = sAttrs.indexOf("@");
		int nEqual = sAttrs.indexOf("=");
		if ((nAT != 0) || ((nEqual >= 0) && (nEqual < nAT))) {
			throw new AttrInvalidException("'" + sAttrs + "'属性名非法");
		}
		if (nEqual < 0) {
			String sTmp = sAttrs.substring(nAT + 1, sAttrs.length());
			sTmp = sTmp.trim();
			if (sTmp.equals("")) {
				throw new AttrInvalidException("'" + sAttrs + "'属性名非法");
			}
			this.mapAttr.put(sTmp, "");
		} else {
			String sTmp = sAttrs.substring(nAT + 1, nEqual);
			String sValue = sAttrs.substring(nEqual + 1, sAttrs.length());
			sTmp = sTmp.trim();
			sValue = sValue.trim();
			sValue = getAttrValue(sValue);
			if ((sTmp.equals("")) || (sValue.equals(""))) {
				throw new AttrInvalidException("'" + sAttrs + "'属性名非法");
			}
			this.mapAttr.put(sTmp, sValue);
		}
		return this.mapAttr;
	}

	public static String getAttrValue(String sAttrValueExpr)
			throws AttrInvalidException {
		int nL = sAttrValueExpr.indexOf("\"");
		int nR = sAttrValueExpr.lastIndexOf("\"");
		if (nR <= nL) {
			nL = sAttrValueExpr.indexOf("'");
			nR = sAttrValueExpr.lastIndexOf("'");
			if (nR <= nL) {
				throw new AttrInvalidException("'" + sAttrValueExpr
						+ "'属性值非法, 缺少引号");
			}
			return sAttrValueExpr.substring(nL + 1, nR);
		}
		return sAttrValueExpr.substring(nL + 1, nR);
	}

	public String getDirName() {
		return this.sDirName;
	}

	public int getDirPos() {
		return this.nPos;
	}

	public String getDirAttr() {
		return this.sDirAttr;
	}

	public Map<String, String> getDirAttrs() {
		return this.mapAttr;
	}

	public boolean isPosAhead() {
		return this.bPosAhead;
	}

	public String toString() {
		if (this.bPosAhead) {
			return this.sDirName + "[" + this.nPos + "]" + this.mapAttr;
		}
		return this.sDirName + this.mapAttr + "[" + this.nPos + "]";
	}
}
