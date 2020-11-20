package cn.suanzi.baomi.base.pojo;

import java.util.List;

/**
 * 首页模块对象
 * @author yanfnag.li
 */
public class HomePage {
	
	/**
	 * 模板号
	 */
	private String template;
	
	/**
	 * 模板类型
	 */
	private int moduleValue;
	
	/**
	 * 模块位置
	 */
	private int modulePosition;
	
	/**
	 * 小模块
	 */
	private List<HomeTemplate> subList;
	
	/**
	 * 商家列表的标题
	 */
	private String moduleTitle;
	
	/**
	 * 商家列表的颜色
	 */
	private String moduleTitleColor;

	public String getModuleTitle() {
		return moduleTitle;
	}

	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
	}

	public String getModuleTitleColor() {
		return moduleTitleColor;
	}

	public void setModuleTitleColor(String moduleTitleColor) {
		this.moduleTitleColor = moduleTitleColor;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public int getModuleValue() {
		return moduleValue;
	}

	public void setModuleValue(int moduleValue) {
		this.moduleValue = moduleValue;
	}

	public int getModulePosition() {
		return modulePosition;
	}

	public void setModulePosition(int modulePosition) {
		this.modulePosition = modulePosition;
	}

	public List<HomeTemplate> getSubList() {
		return subList;
	}

	public void setSubList(List<HomeTemplate> subList) {
		this.subList = subList;
	}

	
}
