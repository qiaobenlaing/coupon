package cn.suanzi.baomi.base.pojo;

import java.util.List;

/**
 * 商店顶部排序的子类容
 * 例如  商圈下的排序内容
 * @author yingchen
 *
 */
public class SubShopSortType {
	private String queryName;
	private List<CircleItem>  subList;
	private String moduleValue;
	private String value;
	private String focusedUrl;
	private String notFocusedUrl;
	private boolean isCheck;
	
	public SubShopSortType() {
		super();
	}

	public SubShopSortType(String queryName, List<CircleItem> subList, String moduleValue, String value, String focusedUrl, String notFocusedUrl, boolean isCheck) {
		super();
		this.queryName = queryName;
		this.subList = subList;
		this.moduleValue = moduleValue;
		this.value = value;
		this.focusedUrl = focusedUrl;
		this.notFocusedUrl = notFocusedUrl;
		this.isCheck = isCheck;
	}

	
	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public List<CircleItem> getSubList() {
		return subList;
	}

	public void setSubList(List<CircleItem> subList) {
		this.subList = subList;
	}

	public String getModuleValue() {
		return moduleValue;
	}

	public void setModuleValue(String moduleValue) {
		this.moduleValue = moduleValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFocusedUrl() {
		return focusedUrl;
	}

	public void setFocusedUrl(String focusedUrl) {
		this.focusedUrl = focusedUrl;
	}

	public String getNotFocusedUrl() {
		return notFocusedUrl;
	}

	public void setNotFocusedUrl(String notFocusedUrl) {
		this.notFocusedUrl = notFocusedUrl;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	@Override
	public String toString() {
		return "SubShopSortType [queryName=" + queryName + ", subList=" + subList + ", moduleValue=" + moduleValue + ", value=" + value + ", focusedUrl=" + focusedUrl + ", notFocusedUrl=" + notFocusedUrl + ", isCheck=" + isCheck + "]";
	}
	
	
}
