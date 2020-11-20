/**
 *
 * @Author: Jianping Chen
 * @Date: 2015.5.8
 * @Version: 1.0.0
 * @Copyright Suanzi Co.,Ltd. @ 2015
 * 
 */

package cn.suanzi.baomi.base.pojo;

/**
 * 支付的类
 * 
 * @author ad
 * 
 */
public class Pay implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商店编码
	 */
	private String shopCode;

	/**
	 * 商店名称
	 */
	private String shopName;

	/**
	 * 支付金额
	 */
	private double price;

	/**
	 * 需要支付的金额
	 */
	private double newPrice;

	/**
	 * 优惠券的code
	 */
	private String couponCode;
	
	/**
	 * 批次优惠券
	 */
	private String batchCouponCode;
	
	/**
	 * 优惠券的金额
	 */
	private double couponPrice;

	/**
	 * 商家金额
	 */
	private double shopBonusPrice;

	/**
	 * 平台红包
	 */
	private double platBonusPrice;
	
	/**
	 * 最后支付的平台红包
	 */
	private double platBonus;
	
	/**
	 * 最后支付的商家红包
	 */
	private double shopBonus;

	/**
	 * 会员卡折扣金额
	 */
	private double cardInsteadPrice;
	
	/**
	 * 优惠券折扣金额
	 */
	private double couponInsteadPrice;

	/**
	 * 商家Logo
	 */
	private String logoUrl;

	/**
	 * 商家活动
	 */
	private String platAct;

	/**
	 * 是否有惠圈活动
	 */
	private int hasPlatAct;

	/**
	 * 工行卡打折
	 */
	private Icbc icbc;

	/**
	 * 会员卡折扣
	 */
	private Card card;

	/**
	 * 有没有首单立减
	 */
	private int isFirst;

	/**
	 * 首单立减10元
	 */
	private String mealFirstDec;

	/**
	 * 红包
	 */
	private Bonus bonus;

	/**
	 * 是否有商家活动
	 */
	private int hasShopAct;

	/**
	 * 商家活动
	 */
	private String shopAct;

	/**
	 * 最低消费金额
	 */
	private double minRealPay;

	/**
	 * 是否满就送
	 */
	private int hasSendCoupon;

	/**
	 * 满就送的信息
	 */
	private String sendCoupon;

	/**
	 * 优惠券
	 */
	private BatchCoupon coupon;

	/**
	 * 会员卡的折扣
	 */
	private double cardDisCount;
	
	/**
	 * 工行卡的折扣
	 */
	private double icbcDisCount;
	
	/**
	 * 抵扣金额
	 */
	private double insteadPrice;
	
	/**
	 * 可用金额
	 */
	private double availablePrice;
	
	/**
	 * 我拥有多少张优惠券
	 */
	private int userCount;
	
	/**
	 * 限用多少张优惠券
	 */
	private int limitedNbr;
	
	/**
	 * 优惠券的折扣
	 */
	private double couponDisCount;
	
	/**
	 * 优惠券类型
	 */
	private String couponType;
	
	/**
	 * 优惠券支付金额
	 */
	private double couponPayMoney;
	
	/**
	 * 会员卡是否打折
	 */
	private int canDiscount;
	
	/**
	 * 工行卡是否打折
	 */
	private int canIcbcDiscount;
	
	/**
	 * 优惠券使用张数
	 */
	private int couponCount;
	
	/**
	 * 是否使用会员卡
	 */
	private int canUseCard;
	
	/**
	 * 折扣券
	 */
	private double minDiscount;
	
	/**
	 *抵扣券
	 */
	private double minReduction;
	
	public Pay() {
		super();
	}

	public Pay(String shopCode, String shopName, double price, double newPrice, String couponCode, double couponPrice,
			double shopBonusPrice, double platBonusPrice, double cardInsteadPrice, double couponInsteadPrice,
			String logoUrl) {
		super();
		this.shopCode = shopCode;
		this.shopName = shopName;
		this.price = price;
		this.newPrice = newPrice;
		this.couponCode = couponCode;
		this.couponPrice = couponPrice;
		this.shopBonusPrice = shopBonusPrice;
		this.platBonusPrice = platBonusPrice;
		this.cardInsteadPrice = cardInsteadPrice;
		this.couponInsteadPrice = couponInsteadPrice;
		this.logoUrl = logoUrl;
	}
	
	public double getMinDiscount() {
		return minDiscount;
	}

	public void setMinDiscount(double minDiscount) {
		this.minDiscount = minDiscount;
	}

	public double getMinReduction() {
		return minReduction;
	}

	public void setMinReduction(double minReduction) {
		this.minReduction = minReduction;
	}

	public int getCanUseCard() {
		return canUseCard;
	}

	public void setCanUseCard(int canUseCard) {
		this.canUseCard = canUseCard;
	}

	public int getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}

	public double getPlatBonus() {
		return platBonus;
	}

	public void setPlatBonus(double platBonus) {
		this.platBonus = platBonus;
	}

	public double getShopBonus() {
		return shopBonus;
	}

	public void setShopBonus(double shopBonus) {
		this.shopBonus = shopBonus;
	}

	public int getCanIcbcDiscount() {
		return canIcbcDiscount;
	}

	public void setCanIcbcDiscount(int canIcbcDiscount) {
		this.canIcbcDiscount = canIcbcDiscount;
	}

	public int getCanDiscount() {
		return canDiscount;
	}

	public void setCanDiscount(int canDiscount) {
		this.canDiscount = canDiscount;
	}

	public String getBatchCouponCode() {
		return batchCouponCode;
	}

	public void setBatchCouponCode(String batchCouponCode) {
		this.batchCouponCode = batchCouponCode;
	}

	public double getCouponPayMoney() {
		return couponPayMoney;
	}

	public void setCouponPayMoney(double couponPayMoney) {
		this.couponPayMoney = couponPayMoney;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String coupneType) {
		this.couponType = coupneType;
	}

	public double getCouponDisCount() {
		return couponDisCount;
	}

	public void setCouponDisCount(double couponDisCount) {
		this.couponDisCount = couponDisCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public int getLimitedNbr() {
		return limitedNbr;
	}

	public void setLimitedNbr(int limitedNbr) {
		this.limitedNbr = limitedNbr;
	}

	public double getInsteadPrice() {
		return insteadPrice;
	}

	public void setInsteadPrice(double insteadPrice) {
		this.insteadPrice = insteadPrice;
	}

	public double getAvailablePrice() {
		return availablePrice;
	}

	public void setAvailablePrice(double availablePrice) {
		this.availablePrice = availablePrice;
	}

	public double getIcbcDisCount() {
		return icbcDisCount;
	}

	public void setIcbcDisCount(double icbcDisCount) {
		this.icbcDisCount = icbcDisCount;
	}

	public double getCardDisCount() {
		return cardDisCount;
	}

	public void setCardDisCount(double cardDisCount) {
		this.cardDisCount = cardDisCount;
	}

	public String getPlatAct() {
		return platAct;
	}

	public BatchCoupon getCoupon() {

		return coupon;
	}

	public void setCoupon(BatchCoupon coupon) {
		this.coupon = coupon;
	}

	public void setPlatAct(String platAct) {
		this.platAct = platAct;
	}

	public int getHasPlatAct() {
		return hasPlatAct;
	}

	public void setHasPlatAct(int hasPlatAct) {
		this.hasPlatAct = hasPlatAct;
	}

	public Icbc getIcbc() {
		return icbc;
	}

	public void setIcbc(Icbc icbc) {
		this.icbc = icbc;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public int getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(int isFirst) {
		this.isFirst = isFirst;
	}

	public String getMealFirstDec() {
		return mealFirstDec;
	}

	public void setMealFirstDec(String mealFirstDec) {
		this.mealFirstDec = mealFirstDec;
	}

	public Bonus getBonus() {
		return bonus;
	}

	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}

	public int getHasShopAct() {
		return hasShopAct;
	}

	public void setHasShopAct(int hasShopAct) {
		this.hasShopAct = hasShopAct;
	}

	public String getShopAct() {
		return shopAct;
	}

	public void setShopAct(String shopAct) {
		this.shopAct = shopAct;
	}

	public double getMinRealPay() {
		return minRealPay;
	}

	public void setMinRealPay(double minRealPay) {
		this.minRealPay = minRealPay;
	}

	public int getHasSendCoupon() {
		return hasSendCoupon;
	}

	public void setHasSendCoupon(int hasSendCoupon) {
		this.hasSendCoupon = hasSendCoupon;
	}

	public String getSendCoupon() {
		return sendCoupon;
	}

	public void setSendCoupon(String sendCoupon) {
		this.sendCoupon = sendCoupon;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(double newPrice) {
		this.newPrice = newPrice;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public double getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(double couponPrice) {
		this.couponPrice = couponPrice;
	}

	public double getShopBonusPrice() {
		return shopBonusPrice;
	}

	public void setShopBonusPrice(double shopBonusPrice) {
		this.shopBonusPrice = shopBonusPrice;
	}

	public double getPlatBonusPrice() {
		return platBonusPrice;
	}

	public void setPlatBonusPrice(double platBonusPrice) {
		this.platBonusPrice = platBonusPrice;
	}

	public double getCardInsteadPrice() {
		return cardInsteadPrice;
	}

	public void setCardInsteadPrice(double cardInsteadPrice) {
		this.cardInsteadPrice = cardInsteadPrice;
	}

	public double getCouponInsteadPrice() {
		return couponInsteadPrice;
	}

	public void setCouponInsteadPrice(double couponInsteadPrice) {
		this.couponInsteadPrice = couponInsteadPrice;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

}
