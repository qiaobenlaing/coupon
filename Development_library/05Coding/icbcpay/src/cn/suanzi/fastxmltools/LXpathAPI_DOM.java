package cn.suanzi.fastxmltools;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cn.suanzi.tools.ListPoolManager;
import cn.suanzi.tools.StringTool;

public class LXpathAPI_DOM implements LXpathAPI {
	ListPoolManager<Node> lm = new ListPoolManager<>(20);

	public static Document getDoc(String sSource)
			throws CantCreateXmlDocException {
		boolean validation = false;
		boolean ignoreWhitespace = false;
		boolean ignoreComments = false;
		boolean putCDATAIntoText = false;
		boolean createEntityRefs = false;
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			dbf.setValidating(validation);
			dbf.setIgnoringComments(ignoreComments);
			dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
			dbf.setCoalescing(putCDATAIntoText);

			dbf.setExpandEntityReferences(!createEntityRefs);

			DocumentBuilder db = dbf.newDocumentBuilder();

			doc = db.parse(new InputSource(new StringReader(sSource)));
			doc.normalize();
		} catch (FactoryConfigurationError e) {
			throw new CantCreateXmlDocException("无法创建《文档对象构造器工厂》:" + e);
		} catch (ParserConfigurationException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象构造器》" + e);
		} catch (SAXException e) {
			e.printStackTrace(System.out);
			throw new CantCreateXmlDocException("无法创建《文档对象》，SAXException" + e);
		} catch (IOException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象》，IOException" + e);
		} catch (IllegalArgumentException e) {
			throw new CantCreateXmlDocException(
					"无法创建《文档对象》，IllegalArgumentException" + e);
		} catch (Exception e) {
			throw new CantCreateXmlDocException("无法导入XML结构的字符串。" + e);
		}
		return doc;
	}

	/**
	 * 读取文档
	 * 
	 * @param xmlFile
	 *            要读取的目标文档
	 * @return
	 * @throws CantCreateXmlDocException
	 */
	public static Document getDoc(File xmlFile)
			throws CantCreateXmlDocException {
		boolean validation = false;
		boolean ignoreWhitespace = false;
		boolean ignoreComments = false;
		boolean putCDATAIntoText = false;
		boolean createEntityRefs = false;
		Document doc = null;
		if (xmlFile == null) {
			throw new CantCreateXmlDocException("XML文件不存在，无法创建文档对象");
		}
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			dbf.setValidating(validation);
			dbf.setIgnoringComments(ignoreComments);
			dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
			dbf.setCoalescing(putCDATAIntoText);

			dbf.setExpandEntityReferences(!createEntityRefs);

			DocumentBuilder db = dbf.newDocumentBuilder();

			doc = db.parse(xmlFile);
			doc.normalize();
		} catch (FactoryConfigurationError e) {
			throw new CantCreateXmlDocException("无法创建《文档对象构造器工厂》:" + e);
		} catch (ParserConfigurationException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象构造器》:" + e);
		} catch (SAXException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象》，SAXException:" + e);
		} catch (IOException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象》，IOException:" + e);
		} catch (IllegalArgumentException e) {
			throw new CantCreateXmlDocException(
					"无法创建《文档对象》，IllegalArgumentException" + e);
		} catch (Exception e) {
			throw new CantCreateXmlDocException("无法导入XML结构的字符串。" + e);
		}
		return doc;
	}

	/**
	 * 选择符合要求的第一个元素
	 * 
	 * @param doc
	 *            目标文档
	 * @param sDir
	 *            查询路径
	 */
	public Object selectSingleNode(Object doc, String sDir)
			throws ElementNameInvalidException, AttrInvalidException {
		List<Node> lstElement = selectNodes(doc, sDir);
		if (lstElement.size() <= 0) {
			return null;
		}
		return lstElement.get(0);
	}

	/**
	 * 选择符合要求的全部元素
	 * 
	 * @param doc
	 *            目标文档
	 * @param sDir
	 *            查询路径
	 */
	public List<Node> selectNodes(Object doc, String sDir)
			throws ElementNameInvalidException, AttrInvalidException {
		List<Node> lstElement = null;
		try {
			Document document = (Document) doc;
			Element root = document.getDocumentElement();
			List<DirInfo> lstPath = parseDir(sDir);
			List<Node> lstStart = new ArrayList<>();
			lstStart.add(root);

			lstElement = parseElements(lstStart, lstPath, 1);
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
		return lstElement;
	}

	public List<Node> parseElements(List<Node> lstStart, List<DirInfo> lstPath,
			int nPos) {
		List<Node> lstRet = new ArrayList<>();
		if (lstPath.size() == nPos) {
			lstRet = lstStart;
		} else {
			Iterator<Node> itrEle = lstStart.iterator();
			while (itrEle.hasNext()) {
				Element ele = (Element) itrEle.next();
				DirInfo info = (DirInfo) lstPath.get(nPos);
				String sDirName = info.getDirName();
				int nDirPos = info.getDirPos();
				boolean bPosHead = info.isPosAhead();

				List<Node> lstSecond = this.lm.getList();

				NodeList NodeSelect = ele.getChildNodes();
				int nNodeNum = NodeSelect.getLength();
				if (sDirName.equals("*")) {
					for (int i = 0; i < nNodeNum; i++) {
						Node n = NodeSelect.item(i);
						if (n.getNodeType() == 1) {
							lstSecond.add(n);
						}
					}
				} else {
					for (int i = 0; i < nNodeNum; i++) {
						Node n = NodeSelect.item(i);
						if ((n.getNodeType() == 1)
								&& (n.getNodeName().equals(sDirName))) {
							lstSecond.add(n);
						}
					}
				}
				nNodeNum = lstSecond.size();
				if ((nDirPos < 0) || (!bPosHead)) {
					List<Node> lstAdd = this.lm.getList();
					Iterator<Node> itrChild = lstSecond.iterator();
					while (itrChild.hasNext()) {
						Map<String, String> mapAttrs = info.getDirAttrs();
						Iterator<Entry<String, String>> iter = mapAttrs
								.entrySet().iterator();

						String sEleAttrVal = null;

						Element eleInPos = (Element) itrChild.next();
						boolean bHas = true;
						while (iter.hasNext()) {
							String sAttrName = (String) ((Map.Entry<String, String>) iter
									.next()).getKey();
							String sAttrVal = (String) mapAttrs.get(sAttrName);
							Attr attr = eleInPos.getAttributeNode(sAttrName);
							if (attr != null) {
								sEleAttrVal = attr.getValue();
							}
							if ((attr == null)
									|| ((!sAttrVal.equals("")) && (!sEleAttrVal
											.equals(sAttrVal)))) {
								bHas = false;
								break;
							}
						}
						if (bHas) {
							lstAdd.add(eleInPos);
						}
					}
					if ((bPosHead) && (lstAdd.size() > 0)) {
						lstRet.addAll(parseElements(lstAdd, lstPath, nPos + 1));
					} else if ((!bPosHead) && (nNodeNum > 0)
							&& (nDirPos <= nNodeNum)) {
						List<Node> lstNew = this.lm.getList();
						lstNew.add(lstSecond.get(nDirPos - 1));
						lstRet.addAll(parseElements(lstNew, lstPath, nPos + 1));
						this.lm.returnList(lstNew);
					}
					this.lm.returnList(lstAdd);
				} else if (nDirPos <= nNodeNum) {
					boolean bHas = true;
					Element eleInPos = (Element) lstSecond.get(nDirPos - 1);

					Map<String, String> mapAttrs = info.getDirAttrs();
					Iterator<Entry<String, String>> iter = mapAttrs.entrySet()
							.iterator();

					String sEleAttrVal = null;
					while (iter.hasNext()) {
						String sAttrName = (String) ((Map.Entry<String, String>) iter
								.next()).getKey();
						String sAttrVal = (String) mapAttrs.get(sAttrName);
						Attr attr = eleInPos.getAttributeNode(sAttrName);
						if (attr != null) {
							sEleAttrVal = attr.getValue();
						}
						if ((attr == null)
								|| ((!sAttrVal.equals("")) && (!sEleAttrVal
										.equals(sAttrVal)))) {
							bHas = false;
							break;
						}
					}
					if (bHas) {
						List<Node> lstNew = this.lm.getList();
						lstNew.add(eleInPos);
						lstRet.addAll(parseElements(lstNew, lstPath, nPos + 1));
						this.lm.returnList(lstNew);
					}
				}
				this.lm.returnList(lstSecond);
			}
		}
		return lstRet;
	}

	public List<DirInfo> parseDir(String sDir)
			throws ElementNameInvalidException, AttrInvalidException {
		List<DirInfo> lstPath = new ArrayList<>();
		String[] sDirs = StringTool.getTokens(sDir, "/");
		try {
			int i = 0;
			for (int n = sDirs.length; i < n; i++) {
				lstPath.add(new DirInfo(sDirs[i]));
			}
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
		return lstPath;
	}
}
