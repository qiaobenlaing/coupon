package cn.suanzi.baomi.base.pojo;

import java.util.List;

/**
 * 手机充值
 * 
 * @author liyanfang
 * 
 */
public class PhoneRecharge {

	/**
	 * 错误代码
	 */
	private String code;
	/**
	 * 充值手机号信息
	 */
	private Mobile mobileInfo;

	/**
	 * 价格表
	 */
	private List<Mobile> priceList;

	/**
	 * 商家信息
	 */
	private Shop shopInfo;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Mobile getMobileInfo() {
		return mobileInfo;
	}

	public void setMobileInfo(Mobile mobileInfo) {
		this.mobileInfo = mobileInfo;
	}
	
	public List<Mobile> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<Mobile> priceList) {
		this.priceList = priceList;
	}

	public Shop getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(Shop shopInfo) {
		this.shopInfo = shopInfo;
	}
	
	
}
