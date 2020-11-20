package cn.suanzi.fastxmltools;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlTool_DOM {
	Document doc = null;
	Element root = null;
	LXpathAPI_DOM parser = new LXpathAPI_DOM();

	public void xmlDocumentCreate(String sSource)
			throws CantCreateXmlDocException {
		boolean validation = false;
		boolean ignoreWhitespace = false;
		boolean ignoreComments = false;
		boolean putCDATAIntoText = false;
		boolean createEntityRefs = false;
		if (this.doc != null) {
			throw new CantCreateXmlDocException("文档对象已经存在，无法创建新对象");
		}
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			dbf.setValidating(validation);
			dbf.setIgnoringComments(ignoreComments);
			dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
			dbf.setCoalescing(putCDATAIntoText);

			dbf.setExpandEntityReferences(!createEntityRefs);

			DocumentBuilder db = dbf.newDocumentBuilder();

			this.doc = db.parse(new InputSource(new StringReader(sSource)));
			this.doc.normalize();
		} catch (FactoryConfigurationError e) {
			throw new CantCreateXmlDocException("无法创建《文档对象构造器工厂》:" + e);
		} catch (ParserConfigurationException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象构造器》" + e);
		} catch (SAXException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象》，SAXException" + e);
		} catch (IOException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象》，IOException" + e);
		} catch (IllegalArgumentException e) {
			throw new CantCreateXmlDocException(
					"无法创建《文档对象》，IllegalArgumentException" + e);
		} catch (Exception e) {
			throw new CantCreateXmlDocException("无法导入XML结构的字符串。" + e);
		}
	}

	public void xmlDocumentCreate(File xmlFile)
			throws CantCreateXmlDocException {
		boolean validation = false;
		boolean ignoreWhitespace = false;
		boolean ignoreComments = false;
		boolean putCDATAIntoText = false;
		boolean createEntityRefs = false;
		if (this.doc != null) {
			throw new CantCreateXmlDocException("文档对象已经存在，无法创建新对象");
		}
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

			this.doc = db.parse(xmlFile);
			this.doc.normalize();
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
	}

	public String toString() {
		if (this.doc == null) {
			return "";
		}
		return this.doc.getDocumentElement().toString();
	}

	public String xmlToStream() throws WriteSourceException,
			DocNotExistException {
		if (this.doc == null) {
			throw new DocNotExistException("导出XML结构");
		}
		StringBuffer sbRet = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sbRet.append(toString());
		return sbRet.toString();
	}

	public void xmlToStream(File FileResult) throws WriteSourceException,
			DocNotExistException {
		if (this.doc == null) {
			throw new DocNotExistException("导出XML结构");
		}
		try {
			TransformerFactory tfactory = TransformerFactory.newInstance();

			Transformer tfac = tfactory.newTransformer();

			DOMSource src = new DOMSource(this.doc);
			StreamResult des = new StreamResult(FileResult);
			tfac.transform(src, des);
		} catch (TransformerFactoryConfigurationError e) {
			throw new WriteSourceException("无法创建《文档对象转换器工厂》" + e);
		} catch (TransformerConfigurationException e) {
			throw new WriteSourceException("无法创建《文档对象转换器》" + e);
		} catch (TransformerException e) {
			throw new WriteSourceException("无法转换数据，TransformerException" + e);
		} catch (Exception e) {
			throw new WriteSourceException("无法导出XML结构的字符串。" + e);
		}
	}

	public void xmlStructDestroy() {
		this.doc = null;
	}

	public void xmlStructClear(boolean HoldRoot) throws DocNotExistException,
			PathNotFoundException, DeleteNodeException {
		if (this.doc == null) {
			throw new DocNotExistException("文档对象不存在");
		}
		try {
			Element root = this.doc.getDocumentElement();
			if (root == null) {
				throw new PathNotFoundException("根节点不存在");
			}
			Node nn;
			while ((nn = root.getFirstChild()) != null) {
				root.removeChild(nn);
			}
			if (!HoldRoot) {
				this.doc.removeChild(root);
			}
		} catch (Exception e) {
			throw new DeleteNodeException("无法清空xml结构" + e);
		}
	}

	public void xmlAddElement(String ElementFullPath)
			throws PathNotFoundException, AttrInvalidException,
			ElementNameInvalidException, ElementAlreadyExistException,
			MultiResultException {
		if (ElementFullPath.lastIndexOf("/") == ElementFullPath.length()) {
			ElementFullPath = ElementFullPath.substring(0,
					ElementFullPath.length() - 1);
		}
		String sPathDelim = "/";
		String path = ElementFullPath.substring(0,
				ElementFullPath.lastIndexOf(sPathDelim));
		String ele = ElementFullPath.substring(
				ElementFullPath.lastIndexOf(sPathDelim) + 1,
				ElementFullPath.length());
		List<DirInfo> lstDir = this.parser.parseDir(ElementFullPath);

		Element root = this.doc.getDocumentElement();
		if ((lstDir.size() == 1) && (root == null)) {
			DirInfo info = (DirInfo) lstDir.get(0);
			if ((info.getDirPos() < 1) && (info.getDirAttrs().size() <= 0)) {
				this.doc.appendChild(this.doc.createElement(info.getDirName()));
			} else {
				throw new ElementNameInvalidException("添加节点时叶子节点不可指定属性");
			}
			return;
		}
		if ((lstDir.size() == 1) && (root != null)) {
			throw new ElementNameInvalidException("根节点已存在，不可添加");
		}
		try {
			List<Node> lstPath = this.parser.selectNodes(this.doc, path);
			if (lstPath.size() == 0) {
				throw new PathNotFoundException("指定路径不存在");
			}
			if (lstPath.size() > 1) {
				throw new MultiResultException("指定路径不唯一");
			}
			Element EleParent = (Element) lstPath.get(0);
			DirInfo info = new DirInfo(ele);
			int nDirPos = info.getDirPos();
			if (info.getDirAttrs().size() > 0) {
				throw new ElementNameInvalidException("添加节点时叶子节点不可指定属性");
			}
			String sDirName = info.getDirName();
			List<Node> lstElement = this.parser.selectNodes(this.doc, path + "/"
					+ sDirName);
			int n = lstElement.size();
			int nAdd = 0;
			if (n == 0) {
				if (nDirPos <= 0) {
					nAdd = 1;
				} else {
					nAdd = nDirPos;
				}
			} else if (n < nDirPos) {
				nAdd = nDirPos - n;
			} else {
				throw new ElementAlreadyExistException("指定节点已存在");
			}
			List<Element> lstAdd = new LinkedList<>();
			for (int i = 0; i < nAdd; i++) {
				lstAdd.add(this.doc.createElement(info.getDirName()));
			}
			Iterator<Element> itrEle = lstAdd.iterator();
			while (itrEle.hasNext()) {
				EleParent.appendChild(itrEle.next());
			}
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
	}

	public void xmlDeleteElement(String ElementFullPath)
			throws PathNotFoundException, ElementNameInvalidException,
			DeleteNodeException, AttrInvalidException {
		if (this.parser.parseDir(ElementFullPath).size() == 1) {
			throw new ElementNameInvalidException("不可删除根节点");
		}
		try {
			List<Node> lstEle = this.parser.selectNodes(this.doc, ElementFullPath);
			if (lstEle.size() <= 0) {
				throw new PathNotFoundException("要删除的节点路径不存在");
			}
			Iterator<Node> itrEle = lstEle.iterator();
			while (itrEle.hasNext()) {
				Element ele = (Element) itrEle.next();
				Node parent = ele.getParentNode();
				parent.removeChild(ele);
			}
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
	}

	public boolean xmlIsChildElement(String ElementFullPath)
			throws DocNotExistException, PathNotFoundException,
			MultiResultException, ElementNameInvalidException,
			AttrInvalidException {
		if (this.doc == null) {
			throw new DocNotExistException("文档对象不存在");
		}
		try {
			List<Node> lstEle = this.parser.selectNodes(this.doc, ElementFullPath);
			int n = lstEle.size();
			if (n == 0) {
				throw new PathNotFoundException("指定路径不存在");
			}
			if (n > 1) {
				throw new MultiResultException("指定路径不唯一");
			}
			Element ele = (Element) lstEle.get(0);
			NodeList lstNode = ele.getElementsByTagName("*");
			return lstNode.getLength() == 0;
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
	}

	public int xmlCountElement(String sElementFullPath)
			throws ElementNameInvalidException, PathNotFoundException,
			AttrInvalidException, MultiResultException {
		if (sElementFullPath.lastIndexOf("/") == sElementFullPath.length()) {
			sElementFullPath = sElementFullPath.substring(0,
					sElementFullPath.length() - 1);
		}
		String sPath = sElementFullPath.substring(0,
				sElementFullPath.lastIndexOf("/"));
//		String sElement = sElementFullPath.substring(
//				sElementFullPath.lastIndexOf("/") + 1,
//				sElementFullPath.length());
		int nRet = 0;
		try {
			List<Node> lstPath = this.parser.selectNodes(this.doc, sPath);
			if (lstPath.size() == 0) {
				throw new PathNotFoundException("指定路径不存在");
			}
			if (lstPath.size() > 1) {
				throw new MultiResultException("指定路径不唯一");
			}
			lstPath = this.parser.selectNodes(this.doc, sElementFullPath);
			nRet = lstPath.size();
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
		return nRet;
	}

	public void xmlSetElementAttr(String ElementFullPath, String AttributeValue)
			throws AttrInvalidException, ElementNameInvalidException,
			PathNotFoundException {
		String sPath = ElementFullPath.substring(0,
				ElementFullPath.lastIndexOf("/"));
		if (ElementFullPath.lastIndexOf("@") < 0) {
			throw new AttrInvalidException("未指定设置的属性名");
		}
		String sAttr = ElementFullPath.substring(
				ElementFullPath.lastIndexOf("@") + 1, ElementFullPath.length());
		sAttr = sAttr.trim();
		if (sAttr.equals("")) {
			throw new AttrInvalidException("未指定设置的属性名");
		}
		try {
			List<Node> lstEle = this.parser.selectNodes(this.doc, sPath);
			if (lstEle.size() == 0) {
				throw new PathNotFoundException("指定路径不存在");
			}
			Iterator<Node> itrEle = lstEle.iterator();
			while (itrEle.hasNext()) {
				((Element) itrEle.next()).setAttribute(sAttr, AttributeValue);
			}
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
	}

	public void xmlDeleteElementAttr(String sElementFullPath)
			throws PathNotFoundException, AttrInvalidException,
			ElementNameInvalidException {
		String sPath = sElementFullPath.substring(0,
				sElementFullPath.lastIndexOf("/"));
		if (sElementFullPath.lastIndexOf("@") < 0) {
			throw new AttrInvalidException("未指定设置的属性名");
		}
		String sAttr = sElementFullPath.substring(
				sElementFullPath.lastIndexOf("@") + 1,
				sElementFullPath.length());
		sAttr = sAttr.trim();
		if (sAttr.equals("")) {
			throw new AttrInvalidException("未指定设置的属性名");
		}
		try {
			List<Node> lstEle = this.parser.selectNodes(this.doc, sPath);
			if (lstEle.size() == 0) {
				throw new PathNotFoundException("指定路径不存在");
			}
			Iterator<Node> itrEle = lstEle.iterator();
			while (itrEle.hasNext()) {
				((Element) itrEle.next()).removeAttribute(sAttr);
			}
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
	}

	public String xmlQueryElementAttr(String ElementFullPath)
			throws PathNotFoundException, MultiResultException,
			ElementNameInvalidException, AttrInvalidException {
		String sPath = ElementFullPath.substring(0,
				ElementFullPath.lastIndexOf("/"));
		if (ElementFullPath.lastIndexOf("@") < 0) {
			throw new AttrInvalidException("未指定设置的属性名");
		}
		String sAttr = ElementFullPath.substring(
				ElementFullPath.lastIndexOf("@") + 1, ElementFullPath.length());
		sAttr = sAttr.trim();
		if (sAttr.equals("")) {
			throw new AttrInvalidException("未指定设置的属性名");
		}
		if ((sPath == null) || (sAttr == null)) {
			throw new PathNotFoundException("指定路径不存在");
		}
		List<Node> lstEle = null;
		try {
			lstEle = this.parser.selectNodes(this.doc, sPath);
			if (lstEle.size() == 0) {
				throw new PathNotFoundException("指定路径不存在");
			}
			if (lstEle.size() > 1) {
				throw new MultiResultException("指定路径不唯一");
			}
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
		return ((Element) lstEle.get(0)).getAttribute(sAttr);
	}
}
