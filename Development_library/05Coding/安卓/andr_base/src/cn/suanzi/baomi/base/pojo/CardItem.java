package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 会员卡列表实体类
 * @author qian.zhou
 */
public class CardItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String shopName;//商户标志
	private String tel;//联系电话
	private String country;//国家
	private String province;//省份
	private String city;//城市
	private String street;//街道
	private String shopCode;//商店编码
	private String logoUrl;//商家logo
	private String distance;//离用户的距离
	private String popularity;//人气
	private String hasN;//是否有N元购
	private String hasReduce;//是否有抵扣券购
	private String hasDiscount;//是否有折扣券
	private String hasPhysical;//是否有实物券
	private String hasExperice;//是否有体验券
	private List<String> couponType; //优惠券的类型
	private List<Photo> newPhotoList;//上新相册	newPhotoList
	//private Photo newActInfo;//活动
	private String actCode;//活动编码
	private String actName;//活动名字
	private String hasNew;//是否上新
	private int isFirst;//是否有首单立减  
	private int hasIcbcDiscount;//工银优惠
	private int hasCoupon;//是否有优惠
	
	//类型	type	N1	Y	1-美食；2-咖啡；3-健身；4-娱乐；5-服装；6-其他
	private String type;
	
	public CardItem() {
		super();
	}
	


	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getPopularity() {
		return popularity;
	}

	public void setPopularity(String popularity) {
		this.popularity = popularity;
	}

	public String getHasN() {
		return hasN;
	}

	public void setHasN(String hasN) {
		this.hasN = hasN;
	}

	public String getHasReduce() {
		return hasReduce;
	}

	public void setHasReduce(String hasReduce) {
		this.hasReduce = hasReduce;
	}

	public String getHasDiscount() {
		return hasDiscount;
	}

	public void setHasDiscount(String hasDiscount) {
		this.hasDiscount = hasDiscount;
	}

	public String getHasPhysical() {
		return hasPhysical;
	}

	public void setHasPhysical(String hasPhysical) {
		this.hasPhysical = hasPhysical;
	}

	public String getHasExperice() {
		return hasExperice;
	}

	public void setHasExperice(String hasExperice) {
		this.hasExperice = hasExperice;
	}

	public List<Photo> getNewPhotoList() {
		return newPhotoList;
	}

	public void setNewPhotoList(List<Photo> newPhotoList) {
		this.newPhotoList = newPhotoList;
	}
	

	/*public Photo getNewActInfo() {
		return newActInfo;
	}

	public void setNewActInfo(Photo newActInfo) {
		this.newActInfo = newActInfo;
	}*/

	public List<String> getCouponType() {
		return couponType;
	}


	public void setCouponType(List<String> couponType) {
		this.couponType = couponType;
	}



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	public String getActCode() {
		return actCode;
	}



	public void setActCode(String actCode) {
		this.actCode = actCode;
	}



	public String getActName() {
		return actName;
	}



	public void setActName(String actName) {
		this.actName = actName;
	}



	public String getHasNew() {
		return hasNew;
	}



	public void setHasNew(String hasNew) {
		this.hasNew = hasNew;
	}



	public int getIsFirst() {
		return isFirst;
	}



	public void setIsFirst(int isFirst) {
		this.isFirst = isFirst;
	}



	public int getHasIcbcDiscount() {
		return hasIcbcDiscount;
	}



	public void setHasIcbcDiscount(int hasIcbcDiscount) {
		this.hasIcbcDiscount = hasIcbcDiscount;
	}



	public int getHasCoupon() {
		return hasCoupon;
	}



	public void setHasCoupon(int hasCoupon) {
		this.hasCoupon = hasCoupon;
	}
}
