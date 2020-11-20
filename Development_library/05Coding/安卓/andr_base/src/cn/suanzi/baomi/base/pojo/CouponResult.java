package cn.suanzi.baomi.base.pojo;

public class CouponResult {
	private String action;
	private String content;
	private String couponCode;
	/** 推送的连接*/
	private String webUrl;
	
	private String batchCouponCode;
	
	private String couponType;
	
	
	public CouponResult() {
		super();
	}
	
	public CouponResult(String action, String content, String couponCode, String webUrl, String batchCouponCode, String couponType) {
		super();
		this.action = action;
		this.content = content;
		this.couponCode = couponCode;
		this.webUrl = webUrl;
		this.batchCouponCode = batchCouponCode;
		this.couponType = couponType;
	}

	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	
	public String getBatchCouponCode() {
		return batchCouponCode;
	}

	public void setBatchCouponCode(String batchCouponCode) {
		this.batchCouponCode = batchCouponCode;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	@Override
	public String toString() {
		return "CouponResult [action=" + action + ", content=" + content + ", couponCode=" + couponCode + ", webUrl=" + webUrl + ", batchCouponCode=" + batchCouponCode + ", couponType=" + couponType + "]";
	}

	
	
	
	
	
}
