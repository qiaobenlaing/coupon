package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 店铺详情的数据
 * @author yingchen
 *
 */
public class NewShopInfoData implements Serializable{
	/**店铺活动数据*/
	private List<Activitys> actList;
	
	/**附件商户数据*/
	private List<Shop> aroundShop;
	
	/**店铺优惠券列表*/
	private Coupons couponList;
	
	/**店铺最近访问用户*/
	private List<User> recentVisitor;
	
	/**商家环境图片（滚屏）*/
	private List<ShopDecoration> shopDecoration;

	/**教育行业的荣誉墙*/
	private List<ShopHonor> shopHonor;
	
	/**商家主要信息*/
	private Shop shopInfo;
	
	/**商家产品图片*/
	private List<NewShopProduct> shopPhotoList;
	
	/**教育行业的教师*/
	private List<Teacher> shopTeacher;
	
	/**教育行业的每日/周/月之星*/
	private EveryStar studentStar;
	
	/**教育行业的招生启示*/
	private RecruitInfo shopRecruitInfo;
	
	/**课程表*/
	private List<Schedule> shopClass;
	
	/**校长之语*/
	private ShopMaster shopHeader;
	
	
	public NewShopInfoData() {
		super();
	}

	




	public NewShopInfoData(List<Activitys> actList, List<Shop> aroundShop, Coupons couponList, List<User> recentVisitor, List<ShopDecoration> shopDecoration, List<ShopHonor> shopHonor, Shop shopInfo, List<NewShopProduct> shopPhotoList, List<Teacher> shopTeacher, EveryStar studentStar, RecruitInfo shopRecruitInfo, List<Schedule> shopClass, ShopMaster shopHeader) {
		super();
		this.actList = actList;
		this.aroundShop = aroundShop;
		this.couponList = couponList;
		this.recentVisitor = recentVisitor;
		this.shopDecoration = shopDecoration;
		this.shopHonor = shopHonor;
		this.shopInfo = shopInfo;
		this.shopPhotoList = shopPhotoList;
		this.shopTeacher = shopTeacher;
		this.studentStar = studentStar;
		this.shopRecruitInfo = shopRecruitInfo;
		this.shopClass = shopClass;
		this.shopHeader = shopHeader;
	}






	public List<Activitys> getActList() {
		return actList;
	}

	public void setActList(List<Activitys> actList) {
		this.actList = actList;
	}

	public List<Shop> getAroundShop() {
		return aroundShop;
	}

	public void setAroundShop(List<Shop> aroundShop) {
		this.aroundShop = aroundShop;
	}

	public Coupons getCouponList() {
		return couponList;
	}

	public void setCouponList(Coupons couponList) {
		this.couponList = couponList;
	}

	public List<User> getRecentVisitor() {
		return recentVisitor;
	}

	public void setRecentVisitor(List<User> recentVisitor) {
		this.recentVisitor = recentVisitor;
	}

	public List<ShopDecoration> getShopDecoration() {
		return shopDecoration;
	}

	public void setShopDecoration(List<ShopDecoration> shopDecoration) {
		this.shopDecoration = shopDecoration;
	}

	public Shop getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(Shop shopInfo) {
		this.shopInfo = shopInfo;
	}

	public List<NewShopProduct> getShopPhotoList() {
		return shopPhotoList;
	}

	public void setShopPhotoList(List<NewShopProduct> shopPhotoList) {
		this.shopPhotoList = shopPhotoList;
	}



	public List<ShopHonor> getShopHonor() {
		return shopHonor;
	}



	public void setShopHonor(List<ShopHonor> shopHonor) {
		this.shopHonor = shopHonor;
	}



	public List<Teacher> getShopTeacher() {
		return shopTeacher;
	}



	public void setShopTeacher(List<Teacher> shopTeacher) {
		this.shopTeacher = shopTeacher;
	}



	public EveryStar getStudentStar() {
		return studentStar;
	}



	public void setStudentStar(EveryStar studentStar) {
		this.studentStar = studentStar;
	}

	public RecruitInfo getShopRecruitInfo() {
		return shopRecruitInfo;
	}

	public void setShopRecruitInfo(RecruitInfo shopRecruitInfo) {
		this.shopRecruitInfo = shopRecruitInfo;
	}

	public List<Schedule> getShopClass() {
		return shopClass;
	}



	public void setShopClass(List<Schedule> shopClass) {
		this.shopClass = shopClass;
	}






	public ShopMaster getShopHeader() {
		return shopHeader;
	}






	public void setShopHeader(ShopMaster shopHeader) {
		this.shopHeader = shopHeader;
	}
	
	
	
}
