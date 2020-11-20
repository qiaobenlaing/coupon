package cn.suanzi.fastxmltools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import cn.suanzi.tools.StringTool;


/**
 * XML处理工具
 * 
 * @author 刘卫平
 * @version 1.0.0 2014-1-7
 */
@SuppressWarnings("deprecation")
public class XmlTool {
	private OutputFormat format = null;
	private XMLSerializer serializer = null;
	/** 配置文件的编码 */
	private String encoding = "utf-8"; // "gb2312";

	Document doc = null;
	LXpathAPI_DOM parser = new LXpathAPI_DOM();
	private LXpathParser lxparser = new LXpathParser();

	
	/**
	 * 构造函数。
	 */
	public XmlTool() {
		super();
	}

	/**
	 * 构造函数。
	 *
	 * @param encoding 配置文件的编码
	 */
	public XmlTool(String encoding) {
		super();
		this.encoding = encoding;
	}

	public void xmlDocumentCreate(String sSource)
			throws CantCreateXmlDocException {
		boolean ignoreWhitespace = false;
		boolean ignoreComments = false;
		boolean validation = false;

		boolean putCDATAIntoText = true;
		boolean createEntityRefs = false;
		String sSrc = getStrFromEncode(sSource, this.encoding);
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

			this.doc = db.parse(new ByteArrayInputStream(sSrc.getBytes()));
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

	public void xmlDocumentCreate(String sSource, String econding)
			throws CantCreateXmlDocException {
		boolean ignoreWhitespace = false;
		boolean ignoreComments = false;
		boolean validation = false;

		boolean putCDATAIntoText = true;
		boolean createEntityRefs = false;

		this.encoding = econding;
		String sSrc = getStrFromEncode(sSource, this.encoding);
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

			this.doc = db.parse(
					new ByteArrayInputStream(sSrc.getBytes(this.encoding)),
					this.encoding);
			this.doc.normalize();
			
		} catch (FactoryConfigurationError e) {
			throw new CantCreateXmlDocException("无法创建《文档对象构造器工厂》:" + e);
		} catch (ParserConfigurationException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象构造器》" + e);
		} catch (SAXException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象》，SAXException。" + e);
		} catch (IOException e) {
			throw new CantCreateXmlDocException("无法创建《文档对象》，IOException。" + e);
		} catch (IllegalArgumentException e) {
			throw new CantCreateXmlDocException(
					"无法创建《文档对象》，IllegalArgumentException" + e);
		} catch (Exception e) {
			throw new CantCreateXmlDocException("无法导入XML结构的字符串。" + e);
		}
	}

	public void xmlDocumentCreate(Document doc) {
		this.doc = doc;
	}

	public void xmlDocumentCreate(InputStream in)
			throws CantCreateXmlDocException {
		boolean validation = false;
		boolean ignoreWhitespace = false;
		boolean ignoreComments = false;
		boolean putCDATAIntoText = false;
		boolean createEntityRefs = false;
		if (this.doc != null) {
			throw new CantCreateXmlDocException("文档对象已经存在，无法创建新对象");
		}
		if (in == null) {
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

			this.doc = db.parse(in);
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
			throw new CantCreateXmlDocException("无法创建《文档对象构造器工厂》" + e);
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
		return getStrFromUnicode(this.doc.getDocumentElement().toString(),
				this.encoding);
	}

	public String xmlToStream() throws WriteSourceException,
			DocNotExistException {
		if (this.doc == null) {
			throw new DocNotExistException("导出XML结构");
		}
		StringWriter write = new StringWriter();
		try {
			this.format = new OutputFormat("xml", this.encoding, false);
			this.serializer = new XMLSerializer(write, this.format);
			this.serializer.serialize(this.doc.getDocumentElement());
			return write.toString();
		} catch (Exception e) {
			throw new WriteSourceException("无法转换数据", e);
		}
	}

	public String xmlToStream(String encoding) throws WriteSourceException,
			DocNotExistException {
		if (this.doc == null) {
			throw new DocNotExistException("导出XML结构");
		}
		StringWriter write = new StringWriter();
		try {
			this.format = new OutputFormat("xml", encoding, false);
			this.serializer = new XMLSerializer(write, this.format);
			this.serializer.serialize(this.doc.getDocumentElement());
			return write.toString();
		} catch (Exception e) {
			throw new WriteSourceException("无法转换数据", e);
		}
	}

	public void xmlToStream(File FileResult) throws WriteSourceException,
			DocNotExistException {
		if (this.doc == null) {
			throw new DocNotExistException("导出XML结构");
		}
		try {
			FileOutputStream fos = new FileOutputStream(FileResult);
			this.format = new OutputFormat("xml", this.encoding, true);
			this.serializer = new XMLSerializer(fos, this.format);
			this.serializer.serialize(this.doc.getDocumentElement());
		} catch (Exception e) {
			throw new WriteSourceException("无法导出XML结构的字符串。", e);
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
			Node nn = null;
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

	public void xmlMoveElement(String sDestElementPath,
			String sSourceElementPath) throws PathNotFoundException,
			AttrInvalidException, ElementNameInvalidException,
			WriteSourceException, MultiResultException {
		String sDestElePath = getStrFromEncode(sDestElementPath, this.encoding);
		String sSrcElePath = getStrFromEncode(sSourceElementPath, this.encoding);
		try {
			List<Node> lstDest = parser.selectNodes(doc, sDestElePath);
			if (lstDest.size() == 0)
				throw new PathNotFoundException("指定目的路径不存在 : '" + sDestElePath + "'");
			if (lstDest.size() > 1)
				throw new MultiResultException("指定目的路径不唯一 : '" + sDestElePath + "'");
			List<Node> lstSource = parser.selectNodes(doc, sSrcElePath);
			if (lstSource.size() == 0)
				return;
			for (Iterator<Node> itrEle = lstDest.iterator(); itrEle.hasNext();) {
				Element eleDest = (Element) itrEle.next();
				Element eleSrc;
				for (Iterator<Node> itrEleSrc = lstSource.iterator(); itrEleSrc.hasNext(); eleDest.appendChild(eleSrc))
					eleSrc = (Element) itrEleSrc.next();
			}

		} catch (DOMException e) {
			throw new WriteSourceException("DOM树添加节点失败 ", e);
		}
	}

	public void xmlCopyElement(String sDestElementPath,
			String sSourceElementPath) throws PathNotFoundException,
			AttrInvalidException, ElementNameInvalidException,
			WriteSourceException, MultiResultException {
		String sDestElePath = getStrFromEncode(sDestElementPath, this.encoding);
		String sSrcElePath = getStrFromEncode(sSourceElementPath, this.encoding);
		try {
			List<Node> lstDest = this.parser.selectNodes(this.doc, sDestElePath);
			if (lstDest.size() == 0) {
				throw new PathNotFoundException("指定目的路径不存在");
			}
			if (lstDest.size() > 1) {
				throw new MultiResultException("指定目的路径不唯一");
			}
			List<Node> lstSource = this.parser.selectNodes(this.doc, sSrcElePath);
			if (lstSource.size() == 0) {
				return;
			}
			for (Iterator<Node> itrEle = lstDest.iterator(); itrEle.hasNext();) {
				Element eleDest = (Element) itrEle.next();
				Element eleSrc;
				for (Iterator<Node> itrEleSrc = lstSource.iterator(); 
						itrEleSrc.hasNext(); 
						eleDest.appendChild(eleSrc.cloneNode(true))) {
					eleSrc = (Element) itrEleSrc.next();
					@SuppressWarnings("unused")
					NodeList nl = eleSrc.getChildNodes();
				}

			}
		} catch (DOMException e) {
			throw new WriteSourceException("DOM树添加节点失败 : ", e);
		}
	}
	   
	public void xmlAddElement(String ElementFullPath)
			throws PathNotFoundException, AttrInvalidException,
			ElementNameInvalidException, ElementAlreadyExistException,
			MultiResultException {
		String sElePath = getStrFromEncode(ElementFullPath, this.encoding);
		if (sElePath.lastIndexOf("/") == sElePath.length()) {
			sElePath = sElePath.substring(0, sElePath.length() - 1);
		}
		String sPathDelim = "/";
		String path = sElePath.substring(0, sElePath.lastIndexOf(sPathDelim));
		String ele = sElePath.substring(sElePath.lastIndexOf(sPathDelim) + 1,
				sElePath.length());
		List<DirInfo> lstDir = this.parser.parseDir(sElePath);
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
			List<Node> lstElement = this.parser.selectNodes(this.doc, path
					+ "/" + sDirName);
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
			for (int i = 0; i < nAdd; i++) {
				EleParent
						.appendChild(this.doc.createElement(info.getDirName()));
			}
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
	}

	public void xmlInsertElement(String sNewElement)
			throws PathNotFoundException, AttrInvalidException,
			ElementNameInvalidException, ElementAlreadyExistException,
			MultiResultException {
		String sElePath = getStrFromEncode(sNewElement, this.encoding);
		if (sElePath.lastIndexOf("/") == sElePath.length()) {
			sElePath = sElePath.substring(0, sElePath.length() - 1);
		}
		String sPathDelim = "/";
		String path = sElePath.substring(0, sElePath.lastIndexOf(sPathDelim));
		String ele = sElePath.substring(sElePath.lastIndexOf(sPathDelim) + 1,
				sElePath.length());
		List<DirInfo> lstDir = this.parser.parseDir(sElePath);
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
			List<Node> lstElement = this.parser.selectNodes(this.doc, path
					+ "/" + sDirName);
			int n = lstElement.size();
			if (n < nDirPos) {
				xmlAddElement(sNewElement);
			} else {
				Element elePref = (Element) lstElement.get(nDirPos - 1);
				EleParent.insertBefore(
						this.doc.createElement(info.getDirName()), elePref);
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
		String sElePath = getStrFromEncode(ElementFullPath, this.encoding);
		if (this.parser.parseDir(sElePath).size() == 1) {
			throw new ElementNameInvalidException("不可删除根节点");
		}
		try {
			List<Node> lstEle = this.parser.selectNodes(this.doc, sElePath);
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

	public List<String> xmlQueryElement(String ElementFullPath)
			throws PathNotFoundException, ElementNameInvalidException,
			AttrInvalidException {
		String sElePath = getStrFromEncode(ElementFullPath, this.encoding);
		try {
			List<Node> lstEle = this.parser.selectNodes(this.doc, sElePath);

			List<String> lstName = new ArrayList<>();
			if (lstEle.size() <= 0) {
				return lstName;
			}
			Iterator<Node> itrEle = lstEle.iterator();
			while (itrEle.hasNext()) {
				Element ele = (Element) itrEle.next();
				lstName.add(getStrFromUnicode(ele.getTagName(),
						this.encoding));
			}
			return lstName;
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
		String sElePath = getStrFromEncode(ElementFullPath, this.encoding);
		if (this.doc == null) {
			throw new DocNotExistException("文档对象不存在");
		}
		try {
			List<Node> lstEle = this.parser.selectNodes(this.doc, sElePath);
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
		String sElePath = getStrFromEncode(sElementFullPath, this.encoding);
		if (sElePath.startsWith("count")) {
			return 1;
		}
		if (sElePath.lastIndexOf("/") == sElePath.length()) {
			sElePath = sElePath.substring(0, sElePath.length() - 1);
		}
		if (sElePath.lastIndexOf("/@") > 0) {
			sElePath = sElePath.substring(0, sElePath.lastIndexOf("/@"));
		}
		String sPath = "";
		if (sElePath.indexOf("/") == sElePath.lastIndexOf("/")) {
			sPath = sElePath;
		} else {
			sPath = sElePath.substring(0, sElePath.lastIndexOf("/"));
		}
		int nRet = 0;
		try {
			List<Node> lstPath = this.parser.selectNodes(this.doc, sPath);
			if (lstPath.size() == 0) {
				return 0;
			}
			if (lstPath.size() > 1) {
				throw new MultiResultException("指定路径不唯一");
			}
			lstPath = this.parser.selectNodes(this.doc, sElePath);
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
		String sElePath = getStrFromEncode(ElementFullPath, this.encoding);
		String sAttrValue = getStrFromEncode(AttributeValue, this.encoding);
		String sPath = sElePath.substring(0, sElePath.lastIndexOf("/"));
		if (sElePath.lastIndexOf("@") < 0) {
			throw new AttrInvalidException("未指定设置的属性名");
		}
		String sAttr = sElePath.substring(sElePath.lastIndexOf("@") + 1,
				sElePath.length());
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
				((Element) itrEle.next()).setAttribute(sAttr, sAttrValue);
			}
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
	}

	public void xmlAppendElement(String ElementFullPath)
			throws XmlToolException {
		String sElePath = ElementFullPath;
		String[] sPaths = StringTool.getTokens(sElePath, "/");
		StringBuffer sbPath = new StringBuffer();
		int i = 0;
		for (int n = sPaths.length; i < n; i++) {
			sbPath.append("/");
			sbPath.append(sPaths[i]);
			String sDir = sbPath.toString();
			int nSize = xmlQueryElement(sDir).size();
			if (nSize <= 0) {
				xmlAddElement(sDir);
			}
			if (nSize > 1) {
				throw new MultiResultException("添加属性的路径不唯一");
			}
		}
	}

	public void xmlAppendElementAttr(String ElementFullPath,
			String AttributeValue) throws AttrInvalidException,
			ElementNameInvalidException, ElementAlreadyExistException,
			PathNotFoundException, MultiResultException {
		
		String sElePath = ElementFullPath;
		String sAttrValue = AttributeValue;
		String[] sPaths = StringTool.getTokens(sElePath, "/");
		StringBuffer sbPath = new StringBuffer();
		try {
			int i = 0;
			for (int n = sPaths.length - 1; i < n; i++) {
				sbPath.append("/");
				sbPath.append(sPaths[i]);
				String sDir = sbPath.toString();
				int nSize = xmlQueryElement(sDir).size();
				if (nSize <= 0) {
					xmlAddElement(sDir);
				}
				if (nSize > 1) {
					throw new MultiResultException("添加属性的路径不唯一");
				}
			}
			xmlSetElementAttr(sElePath, sAttrValue);
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
	}

	public void xmlDeleteElementAttr(String sElementFullPath)
			throws PathNotFoundException, AttrInvalidException,
			ElementNameInvalidException {
		String sElePath = getStrFromEncode(sElementFullPath, this.encoding);
		String sPath = sElePath.substring(0, sElePath.lastIndexOf("/"));
		if (sElePath.lastIndexOf("@") < 0) {
			throw new AttrInvalidException("未指定设置的属性名");
		}
		String sAttr = sElePath.substring(sElePath.lastIndexOf("@") + 1,
				sElePath.length());
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

	public Map<String, String> xmlListElementAttrValue(String ElementPath)
			throws PathNotFoundException, MultiResultException,
			ElementNameInvalidException, AttrInvalidException {
		String sElePath = getStrFromEncode(ElementPath, this.encoding);
		Map<String, String> map = new HashMap<>();
		List<Node> lstEle = null;
		try {
			lstEle = this.parser.selectNodes(this.doc, sElePath);
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
		NamedNodeMap Attrs = ((Element) lstEle.get(0)).getAttributes();
		int nLen = Attrs.getLength();
		for (int i = 0; i < nLen; i++) {
			Node Attribute = Attrs.item(i);
			String sName = Attribute.getNodeName();
			String sValue = Attribute.getNodeValue();
			map.put(getStrFromUnicode(sName, this.encoding),
					getStrFromUnicode(sValue, this.encoding));
		}
		return map;
	}

	public String[] xmlListElementAttr(String ElementPath)
			throws PathNotFoundException, MultiResultException,
			ElementNameInvalidException, AttrInvalidException {
		String sElePath = getStrFromEncode(ElementPath, this.encoding);
		List<Node> lstEle = null;
		try {
			lstEle = this.parser.selectNodes(this.doc, sElePath);
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
		NamedNodeMap Attrs = ((Element) lstEle.get(0)).getAttributes();
		int nLen = Attrs.getLength();
		String[] sRet = new String[nLen];
		for (int i = 0; i < nLen; i++) {
			Node Attribute = Attrs.item(i);
			sRet[i] = getStrFromUnicode(Attribute.getNodeName(),
					this.encoding);
		}
		return sRet;
	}

	public String xmlQueryElementAttr(String ElementFullPath)
			throws PathNotFoundException, MultiResultException,
			ElementNameInvalidException, AttrInvalidException {
		String sElePath = getStrFromEncode(ElementFullPath, this.encoding);

		String sPath = sElePath.substring(0, sElePath.lastIndexOf("/"));
		if (sElePath.lastIndexOf("@") < 0) {
			throw new AttrInvalidException("未指定设置的属性名");
		}
		String sAttr = sElePath.substring(sElePath.lastIndexOf("@") + 1,
				sElePath.length());
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
				throw new PathNotFoundException("指定路径不存在 : " + sPath);
			}
			if (lstEle.size() > 1) {
				throw new MultiResultException("指定路径不唯一");
			}
		} catch (ElementNameInvalidException e) {
			throw e;
		} catch (AttrInvalidException e) {
			throw e;
		}
		String sRet = getStrFromUnicode(
				((Element) lstEle.get(0)).getAttribute(sAttr), this.encoding);
		return sRet;
	}

	public void xmlSetElementValue(String ElementFullPath, String sValue)
			throws XmlToolException {
		String sElePath = getStrFromEncode(ElementFullPath, this.encoding);
		String value = getStrFromEncode(sValue, this.encoding);
		try {
			List<Node> lstPath = this.parser.selectNodes(this.doc, sElePath);
			if (lstPath.size() == 0) {
				throw new PathNotFoundException("指定路径不存在");
			}
			Iterator<Node> itrEle = lstPath.iterator();
			Text txt = this.doc.createTextNode(value);
			while (itrEle.hasNext()) {
				Element Ele = (Element) itrEle.next();
				Ele.appendChild(txt.cloneNode(false));
			}
		} catch (XmlToolException e) {
			throw e;
		}
	}

	public void xmlDeleteElementValue(String ElementFullPath)
			throws XmlToolException {
		try {
			List<Node> lstPath = this.parser.selectNodes(this.doc,
					ElementFullPath);
			if (lstPath.size() == 0) {
				throw new PathNotFoundException("指定路径不存在");
			}
			Iterator<Node> itrEle = lstPath.iterator();
			while (itrEle.hasNext()) {
				Element Ele = (Element) itrEle.next();
				NodeList nl = Ele.getChildNodes();
				int i = 0;
				for (int n = nl.getLength(); i < n; i++) {
					Node node = nl.item(i);
					if (node.getNodeType() == 3) {
						Ele.removeChild(node);
						break;
					}
				}
			}
		} catch (XmlToolException e) {
			throw e;
		}
	}

	public String xmlQueryElementValue(String ElementFullPath)
			throws XmlToolException {
		String sElePath = getStrFromEncode(ElementFullPath, this.encoding);
		try {
			List<Node> lstPath = this.parser.selectNodes(this.doc, sElePath);
			if (lstPath.size() == 0) {
				throw new PathNotFoundException("指定路径不存在");
			}
			if (lstPath.size() > 1) {
				throw new MultiResultException("指定路径不唯一");
			}
			Element Ele = (Element) lstPath.get(0);
			NodeList nl = Ele.getChildNodes();
			int i = 0;
			for (int n = nl.getLength(); i < n; i++) {
				Node node = nl.item(i);
				if (node.getNodeType() == 3) {
					return getStrFromUnicode(node.getNodeValue(),
							this.encoding);
				}
			}
			return "";
		} catch (XmlToolException e) {
			throw e;
		}
	}

	public String xmlQuery(String sXPath) throws XmlToolException {
		this.lxparser.parse(sXPath);

		int nType = this.lxparser.token();
		if (nType == 2) {
			String path = this.lxparser.getToken();
			return xmlQueryElementAttr(path);
		}
		if (nType == 3) {
			String path = this.lxparser.getToken();
			return xmlQueryElementValue(path);
		}
		if (nType == 4) {
			String sFuncName = this.lxparser.getToken();
			return xmlFuncQuery(sFuncName, this.lxparser);
		}
		throw new XmlToolException("XPath非法 : " + sXPath);
	}

	private String xmlFuncQuery(String sFuncName, LXpathParser lxparser)
			throws XmlToolException {
		if (sFuncName.equals("count")) {
			int nType = lxparser.token();
			if (nType == 3) {
				String spath = lxparser.getToken();
				return Integer.toString(xmlCountElement(spath));
			}
			throw new XmlToolException("count()方法只使用于节点");
		}
		throw new XmlToolException("无此方法 ：" + sFuncName);
	}

	public static String getStrFromUnicode(String sIn, String sEncoding) {
		return sIn;
	}

	public static String getStrFromEncode(String sIn, String sEncoding) {
		return sIn;
	}

	public static String xmlCovertStyle(String sXml) {
		int nNodeStart = 0;
		int nNodeEnd = 0;
		String sTmp = sXml;
		StringBuffer sbRet = new StringBuffer();
		while (sTmp.length() > 0) {
			nNodeStart = sTmp.indexOf("<");
			nNodeEnd = sTmp.indexOf(">");
			if (nNodeStart < 0) {
				break;
			}
			String sNode = sTmp.substring(nNodeStart + 1, nNodeEnd);
			String sFlag = sNode.substring(sNode.length() - 1);
			if (sFlag.equals("/")) {
				String sForw = sNode.substring(0, sNode.length() - 1);
				String sElement = sForw;
				if (sNode.indexOf(" ") > 0) {
					sElement = sNode.substring(0, sNode.indexOf(" "));
				}
				sbRet.append("<" + sForw + ">");
				sbRet.append("</" + sElement + ">");
			} else if (sNode.startsWith("/")) {
				sbRet.append("<" + sNode + ">");
			} else {
				sbRet.append("<" + sNode + ">");
			}
			sTmp = sTmp.substring(nNodeEnd + 1);
			if (sTmp.indexOf("<") > 0) {
				String sTxt = sTmp.substring(0, sTmp.indexOf("<"));
				sbRet.append(sTxt);
			}
		}
		return sbRet.toString();
	}

	/**
	 * @return 返回encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding 设定给变量encoding的值
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
}
