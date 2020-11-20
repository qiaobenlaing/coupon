package com.huift.hfq.base.pojo;

import java.util.List;

/**
 * 首页选项卡
 * @author liyanfang
 *
 */
public class TabSave implements java.io.Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -58876656379562722L;
	
	/**
	 * 对象对应的id
	 */
	private String id;

	/**
	 * 标题选中的颜色
	 */
	private  String focusedColor;
	
	/**
	 * 标题没有选中的颜色
	 */
	private String notFocusedColor;
	
	/**
	 * 选中的tab图片
	 */
	private String focusedUrl;
	
	/**
	 * 没有选中图片的url
	 */
	private String notFocusedUrl;
	
	/**
	 * 图片的高度
	 */
	private String imgHeight;
	
	/**
	 * 图片占屏幕的宽度比
	 */
	private double screenRate;
	
	/**
	 * 图片的长宽比
	 */
	private double imgRate;
	
	/**
	 * 模块的位置
	 */
	private int subModulePosition;
	
	/**
	 * 模块的标题
	 */
	private String title;
	
	/**
	 * 背景颜色
	 */
	private String bgColor;
	
	/**
	 * 选项卡的列表
	 */
	private String homeTabs;
	
	public TabSave() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TabSave(String id, String homeTabs) {
		super();
		this.id = id;
		this.homeTabs = homeTabs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHomeTabs() {
		return homeTabs;
	}

	public void setHomeTabs(String homeTabs) {
		this.homeTabs = homeTabs;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getFocusedColor() {
		return focusedColor;
	}

	public void setFocusedColor(String focusedColor) {
		this.focusedColor = focusedColor;
	}

	public String getNotFocusedColor() {
		return notFocusedColor;
	}

	public void setNotFocusedColor(String notFocusedColor) {
		this.notFocusedColor = notFocusedColor;
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

	public String getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(String imgHeight) {
		this.imgHeight = imgHeight;
	}

	public double getScreenRate() {
		return screenRate;
	}

	public void setScreenRate(double screenRate) {
		this.screenRate = screenRate;
	}

	public double getImgRate() {
		return imgRate;
	}

	public void setImgRate(double imgRate) {
		this.imgRate = imgRate;
	}

	public int getSubModulePosition() {
		return subModulePosition;
	}

	public void setSubModulePosition(int subModulePosition) {
		this.subModulePosition = subModulePosition;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
