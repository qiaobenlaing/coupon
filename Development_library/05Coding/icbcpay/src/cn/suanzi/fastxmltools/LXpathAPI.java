package cn.suanzi.fastxmltools;

import java.util.List;

import org.w3c.dom.Node;

public abstract interface LXpathAPI {
	public abstract List<Node> selectNodes(Object paramObject, String paramString)
			throws ElementNameInvalidException, AttrInvalidException;

	public abstract Object selectSingleNode(Object paramObject,
			String paramString) throws ElementNameInvalidException,
			AttrInvalidException;

	public abstract List<Node> parseElements(List<Node> paramList1,
			List<DirInfo> paramList2, int paramInt);

	public abstract List<DirInfo> parseDir(String paramString)
			throws ElementNameInvalidException, AttrInvalidException;
}
