package cn.suanzi.baomi.base.pojo;

import java.util.List;

public class ShopDetail {
	
	/**
	 * 商店信息
	 */
	private Shop shopInfo;
	
	/**
	 * 会员卡信息
	 */
	private List<Card> shopCard;
	
	/**
	 * 优惠券列表
	 */
	private Coupons couponList;
	
	/**
	 * 店铺装修
	 */
	private List<ShopDecoration> shopDecoration;
	
	/**
	 * 商店图片
	 */
	private List<Decoration> shopPhotoList;
	
	/**
	 * 活动集合
	 */
	private List<Activitys> actList;

	
	public List<Activitys> getActList() {
		return actList;
	}

	public void setActList(List<Activitys> actList) {
		this.actList = actList;
	}

	public Shop getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(Shop shopInfo) {
		this.shopInfo = shopInfo;
	}

	public List<Card> getShopCard() {
		return shopCard;
	}

	public void setShopCard(List<Card> shopCard) {
		this.shopCard = shopCard;
	}

	public Coupons getCouponList() {
		return couponList;
	}

	public void setCouponList(Coupons couponList) {
		this.couponList = couponList;
	}

	public List<ShopDecoration> getShopDecoration() {
		return shopDecoration;
	}

	public void setShopDecoration(List<ShopDecoration> shopDecoration) {
		this.shopDecoration = shopDecoration;
	}

	public List<Decoration> getShopPhotoList() {
		return shopPhotoList;
	}

	public void setShopPhotoList(List<Decoration> shopPhotoList) {
		this.shopPhotoList = shopPhotoList;
	}

}
