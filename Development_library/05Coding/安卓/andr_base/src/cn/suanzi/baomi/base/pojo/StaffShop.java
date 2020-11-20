package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;

/**
 * 分店
 * @author wensi.yu
 *
 */
public class StaffShop implements Serializable{

	/**
	 * 商家编码
	 */
	private String shopCode;
	
	/**
	 * 商家名字
	 */
	private String shopName;
	
	/**
	 * 商家头像
	 */
	private String logoUrl;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 店员编码
	 */
	private String staffCode;
	
	/**
	 * 店员名字
	 */
	private String realName;
	
	/**
	 * 店员手机
	 */
	private String mobileNbr;
	
	/**
	 * 店长姓名
	 */
	private String managerRealName;
	
	/**
	 * 店长手机
	 */
	private String managerMobileNbr;
	
	/**
	 *显示编辑的标示
	 */
	private boolean showEdit = false;
	
	/**
	 * 品牌ID
	 */
	private String brandId;

	
	public StaffShop() {
		super();
	}

	
	
	public StaffShop(String shopCode, String shopName, String logoUrl, String status, String staffCode, String realName, String mobileNbr, String managerRealName, String managerMobileNbr, boolean showEdit, String brandId) {
		super();
		this.shopCode = shopCode;
		this.shopName = shopName;
		this.logoUrl = logoUrl;
		this.status = status;
		this.staffCode = staffCode;
		this.realName = realName;
		this.mobileNbr = mobileNbr;
		this.managerRealName = managerRealName;
		this.managerMobileNbr = managerMobileNbr;
		this.showEdit = showEdit;
		this.brandId = brandId;
	}



	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getManagerRealName() {
		return managerRealName;
	}

	public void setManagerRealName(String managerRealName) {
		this.managerRealName = managerRealName;
	}

	public String getManagerMobileNbr() {
		return managerMobileNbr;
	}

	public void setManagerMobileNbr(String managerMobileNbr) {
		this.managerMobileNbr = managerMobileNbr;
	}

	public boolean isShowEdit() {
		return showEdit;
	}

	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}
	
	

	public String getBrandId() {
		return brandId;
	}



	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}



	@Override
	public String toString() {
		return "StaffShop [shopCode=" + shopCode + ", shopName=" + shopName + ", logoUrl=" + logoUrl + ", status=" + status + ", staffCode=" + staffCode + ", realName=" + realName + ", mobileNbr=" + mobileNbr + ", managerRealName=" + managerRealName + ", managerMobileNbr=" + managerMobileNbr + ", showEdit=" + showEdit + "]";
	}	
	
}
