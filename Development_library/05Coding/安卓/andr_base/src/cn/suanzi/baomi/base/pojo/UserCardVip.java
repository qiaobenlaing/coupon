// ---------------------------------------------------------
// @author    yanfang.li
// @version   1.0.0
// @createTime 2015.5.22 
// @copyright 版权所有 (c) 2015 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------
package cn.suanzi.baomi.base.pojo;
/**
 * 会员卡用户信息
 * @author yanfang.li
 *
 */
public class UserCardVip implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 会员卡编号
	 */
	private String realName;
	
	/**
	 * 用户会员卡编码
	 */
	private String userCardCode;
	
	/**
	 * 卡号
	 */
	private String cardNbr;
	
	/**
	 * 用户编码
	 */
	private String userCode;
	
	/**
	 * 消费次数
	 */
	private String consumeTimes;
	
	/**
	 * 总消费金额
	 */
	private String consumePriceAmount;
	
	/**
	 * 使用优惠券张数
	 */
	private String couponUseAmount;
	
	/**
	 * 抵扣总额
	 */
	private String deductionPrice;
	
	/**
	 * 用户头像
	 */
	private String avatarUrl;
	
	/**
	 * 用户昵称
	 */
	private String nickName;
	
	/**
	 * 积分
	 */
	private String point;
	
	/**
	 * 即将过期的积分
	 */
	private String toExpiredPoints;
	
	/**
	 * 申请时间
	 */
	private String applyTime;
	
	/**
	 * 商家名称
	 */
	private String shopName;
	
	/**
	 * 优惠券类型
	 */
	private int couponType;
	
	/**
	 * 面值
	 */
	private String insteadPrice;
	
	/**
	 * 折扣
	 */
	private String discountPercent;
	
	/**
	 * 每张优惠券能做什么
	 */
	private String function;
	
	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public String getInsteadPrice() {
		return insteadPrice;
	}

	public void setInsteadPrice(String insteadPrice) {
		this.insteadPrice = insteadPrice;
	}

	public String getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getToExpiredPoints() {
		return toExpiredPoints;
	}

	public void setToExpiredPoints(String toExpiredPoints) {
		this.toExpiredPoints = toExpiredPoints;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserCardCode() {
		return userCardCode;
	}

	public void setUserCardCode(String userCardCode) {
		this.userCardCode = userCardCode;
	}

	public String getCardNbr() {
		return cardNbr;
	}

	public void setCardNbr(String cardNbr) {
		this.cardNbr = cardNbr;
	}

	public String getConsumeTimes() {
		return consumeTimes;
	}

	public void setConsumeTimes(String consumeTimes) {
		this.consumeTimes = consumeTimes;
	}

	public String getConsumePriceAmount() {
		return consumePriceAmount;
	}

	public void setConsumePriceAmount(String consumePriceAmount) {
		this.consumePriceAmount = consumePriceAmount;
	}

	public String getCouponUseAmount() {
		return couponUseAmount;
	}

	public void setCouponUseAmount(String couponUseAmount) {
		this.couponUseAmount = couponUseAmount;
	}

	public String getDeductionPrice() {
		return deductionPrice;
	}

	public void setDeductionPrice(String deductionPrice) {
		this.deductionPrice = deductionPrice;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
	
}
