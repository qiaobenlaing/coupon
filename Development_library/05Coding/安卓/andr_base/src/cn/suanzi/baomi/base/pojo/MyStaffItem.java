package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 店员管理的实体类
 * @author qian.zhou
 */
public class MyStaffItem implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 员工编码
	 */
	private String staffCode;
	/**
	 * 员工头像
	 */
	private String staffLogo;
	/**
	 * 电话号码
	 */
	private String mobileNbr;
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 用户状态
	 */
	private String userLvl;
	
	
	/**
	 * 是否接收支付短信
	 */
	private String isSendPayedMsg;
	/**
	 * 商家编码
	 */
	private String shopCode;
	
	/**
	 * 店长信息集合
	 */
	private List<Item> shopList;
	
	/**
	 * 街道
	 */
	private String street;
	
	/**
	 * 商家名称
	 */
	private String shopName;
	
	/**
	 * 商家位于的省份
	 */
	private String province;
	
	/**
	 * 商家位于的地区
	 */
	private String district;
	
	/**
	 * 该店是否是属于该店长的
	 */
	private String isOwner;
	
	/**
	 * 品牌id
	 */
	private String brandId;
	
	/**
	 * 关系ID
	 */
	private String id;
	
	/**
	 * 店主名字
	 */
	private String managerName;
	
	private boolean Checked = true;

	public MyStaffItem() {
		super();
	}

	
	public MyStaffItem(String staffCode, String staffLogo, String mobileNbr, String realName, String userLvl,
			String isSendPayedMsg, String shopCode, List<Item> shopList, String street, String shopName,
			String province, String district, String isOwner, String brandId, String id, String managerName) {
		super();
		this.staffCode = staffCode;
		this.staffLogo = staffLogo;
		this.mobileNbr = mobileNbr;
		this.realName = realName;
		this.userLvl = userLvl;
		this.isSendPayedMsg = isSendPayedMsg;
		this.shopCode = shopCode;
		this.shopList = shopList;
		this.street = street;
		this.shopName = shopName;
		this.province = province;
		this.district = district;
		this.isOwner = isOwner;
		this.brandId = brandId;
		this.id = id;
		this.managerName = managerName;
	}


	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getStaffLogo() {
		return staffLogo;
	}

	public void setStaffLogo(String staffLogo) {
		this.staffLogo = staffLogo;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserLvl() {
		return userLvl;
	}

	public void setUserLvl(String userLvl) {
		this.userLvl = userLvl;
	}

	public String getIsSendPayedMsg() {
		return isSendPayedMsg;
	}

	public void setIsSendPayedMsg(String isSendPayedMsg) {
		this.isSendPayedMsg = isSendPayedMsg;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public List<Item> getShopList() {
		return shopList;
	}

	public void setShopList(List<Item> shopList) {
		this.shopList = shopList;
	}


	public String getStreet() {
		return street;
	}


	public void setStreet(String street) {
		this.street = street;
	}


	public String getShopName() {
		return shopName;
	}


	public void setShopName(String shopName) {
		this.shopName = shopName;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	public String getDistrict() {
		return district;
	}


	public void setDistrict(String district) {
		this.district = district;
	}


	public String getIsOwner() {
		return isOwner;
	}


	public void setIsOwner(String isOwner) {
		this.isOwner = isOwner;
	}


	public String getBrandId() {
		return brandId;
	}


	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getManagerName() {
		return managerName;
	}


	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
}
