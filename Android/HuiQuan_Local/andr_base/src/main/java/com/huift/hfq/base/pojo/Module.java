package com.huift.hfq.base.pojo;

/**
 * 体验馆 和 主题的数据
 * 
 * @author ad
 */
public class Module {
	/**
	 * 食券十美 的跳转地址
	 */
	private String webAddress;

	/**
	 * 洗车惠 的跳转地址
	 */
	private String webAddress2;

	/**
	 * 食券十美 的名字
	 */
	private String moduleName;

	/**
	 * 洗车惠 的名字
	 */
	private String moduleName2;

	/**
	 * 食券十美 的名字
	 */
	private String imgUrl;
	
	/**
	 * 洗车惠 的名字
	 */
	private String imgUrl2;
	
	/**
	 * 食券十美 的名字
	 */
	private String moduleCode;
	
	/**
	 * 洗车惠 的名字
	 */
	private String moduleCode2;
	
	/**
	 * 类型
	 */
	private int mark;
	
	/**
	 * 五个图标对应商家的说明文字
	 */
	private String typeZh;
	
	/**
	 * 五个图标对应的商家图片URL
	 */
	private String shopTypeImg;
	
	/**
	 * 图片的位置
	 */
	private int sign;
	
	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public String getTypeZh() {
		return typeZh;
	}

	public void setTypeZh(String typeZh) {
		this.typeZh = typeZh;
	}

	public String getShopTypeImg() {
		return shopTypeImg;
	}

	public void setShopTypeImg(String shopTypeImg) {
		this.shopTypeImg = shopTypeImg;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public String getWebAddress2() {
		return webAddress2;
	}

	public void setWebAddress2(String webAddress2) {
		this.webAddress2 = webAddress2;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleName2() {
		return moduleName2;
	}

	public void setModuleName2(String moduleName2) {
		this.moduleName2 = moduleName2;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgUrl2() {
		return imgUrl2;
	}

	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleCode2() {
		return moduleCode2;
	}

	public void setModuleCode2(String moduleCode2) {
		this.moduleCode2 = moduleCode2;
	}

	
	
}
