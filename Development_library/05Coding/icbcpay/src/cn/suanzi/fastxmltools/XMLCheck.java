package cn.suanzi.fastxmltools;

public class XMLCheck {
	public byte[] xmlPretreatmentCheck(byte[] xmlByte) {
		XmlTool xmlTool = new XmlTool();
		try {
			xmlTool.xmlDocumentCreate(new String(xmlByte));
		} catch (CantCreateXmlDocException e) {
			xmlTool.xmlStructDestroy();
			return xmlConvertProcess(xmlByte);
		}
		xmlTool.xmlStructDestroy();
		return xmlByte;
	}

	private byte[] xmlConvertProcess(byte[] xmlByte) {
		int length = xmlByte.length;
		int count = 0;
		for (int i = 42; i < length - 3; i++) {
			int tmp = xmlByte[i] & 0xFF;
			int tmp2 = xmlByte[(i + 1)] & 0xFF;
			if (tmp < 32) {
				xmlByte[i] = 65;
			} else if (tmp > 128) {
				count++;
				if ((tmp2 < 128) && (tmp2 > 32)) {
					if (count % 2 == 0) {
						count = 0;
					} else {
						xmlByte[i] = 65;
						count = 0;
					}
				}
			}
		}
		return xmlByte;
	}

}
