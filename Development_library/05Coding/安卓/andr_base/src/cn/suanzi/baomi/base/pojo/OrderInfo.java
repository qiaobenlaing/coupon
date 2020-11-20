package cn.suanzi.baomi.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情
 * @author ad
 *
 */
public class OrderInfo implements Serializable{
	
	/**
	 * 接单时间
	 */
	private String receiveTime;

	/**
	 * 订单编码
	 */
	private String orderCode;
	
	/**
	 * 订单号
	 */
	private String orderNbr;
	
	/**
	 * 用户编码
	 */
	private String clientCode;
	
	/**
	 * 订单时间
	 */
	private String orderTime;
	
	/**
	 * 订单支付状态
	 */
	private String status;
	
	/**
	 * 消费金额
	 */
	private String orderAmount;
	
	/**
	 * 订单类型
	 */
	private String orderType;
	
	/**
	 * 订单状态
	 */
	private String orderStatus;
	
	/**
	 * 订单编码
	 */
	private String consumeCode;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 接收人姓名
	 */
	private String receiver;
	
	/**
	 * 接收人电话
	 */
	private String receiverMobileNbr;
	
	/**
	 * 配送地址
	 */
	private String deliveryAddress;
	
	/**
	 * 餐号
	 */
	private String mealNbr;
	
	/**
	 * 商品的总数量
	 */
	private String orderProductAmount;
	
	/**
	 * 旧菜单数量
	 */
	 private String oldAmount;
	 
	 /**
	  * 新添数量
	  */
	 private String newAmount;
	
	/**
	 * 产品列表
	 */
	private List<ProductList> productList;
	
	/**
	 * 实际支付
	 */
	private String realPay;
	/**
	 * 抵扣金额
	 */
	private String deduction;
	/**
	 * 商家红包
	 */
	private String shopBonus;
	/**
	 * 平台红包
	 */
	private String platBonus;
	/**
	 * 银行卡抵扣
	 */
	private String bankCardDeduction;
	
	/**
	 * 工行卡折扣
	 */
	private String bankCardDiscount;
	
	/**
	 * 支付类型
	 */
	private String payType;
	/**
	 * 会员卡抵扣
	 */
	private String cardDeduction;
	/**
	 * 优惠券抵扣
	 */
	private String couponDeduction;
	/**
	 * 满多少元可以使用
	 */
	private String availablePrice;
	/**
	 * 折扣
	 */
	private String discountPercent;
	
	/**
	 * 抵扣金额
	 */
	private String insteadPrice;
	
	/**
	 * 是否使用优惠劵
	 */
	private String couponUsed;
	
	/**
	 * 优惠劵类型
	 */
	private String couponType;
	
	/**
	 * 退款申请时间
	 */
	private String refundApplyTime;
	
	/**
	 * 退款理由
	 */
	private String refundReason;
	
	/**
	 * 退款备注
	 */
	private String refundRemark;
	
	/**
	 * 外卖订单数量
	 */
	private int takeOut;
	
	/**
	 * 门店订单数量
	 */
	private int eatIn;
	
	/**
	 * 
	 * 判断时候开启桌号管理
	 */
	private String tableNbrSwitch;
	
	/**
	 * 首单立减
	 * @return
	 */
	private String firstDeduction;
	
	/**
	 * 桌号管理
	 * @return
	 */
	private String tableNbr;
	 
	 /**
	  * 用户使用商家红包
	  * @return
	  */
	private String userShopBonus;
	
	 /**
	  * 用户使用平台红包
	  * @return
	  */
	private String userPlatBonus;
	
	/**
	 * 用户头像
	 * @return
	 */
	private String avatarUrl;
	
	/**
	 * 用户名称
	 * @return
	 */
	private String nickName;
	
	/**
	 * 商家名称
	 * @return
	 */
	private String shopName;
	
	/**
	 * 支付时间
	 * @return
	 */
	private String payedTime;
	
	/**
	 * 银行卡编码
	 * @return
	 */
	private String bankAccountCode;
	
	/**
	 * 用户编码
	 * @return
	 */
	private String userCode;
	
	/**
	 * 后四位
	 */
	private String mobileNbr;
	
	public String getMobileNbr() {
		return mobileNbr;
	}
	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderNbr() {
		return orderNbr;
	}
	public void setOrderNbr(String orderNbr) {
		this.orderNbr = orderNbr;
	}
	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverMobileNbr() {
		return receiverMobileNbr;
	}

	public void setReceiverMobileNbr(String receiverMobileNbr) {
		this.receiverMobileNbr = receiverMobileNbr;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getMealNbr() {
		return mealNbr;
	}

	public void setMealNbr(String mealNbr) {
		this.mealNbr = mealNbr;
	}

	public List<ProductList> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductList> productList) {
		this.productList = productList;
	}


	public String getOrderProductAmount() {
		return orderProductAmount;
	}


	public void setOrderProductAmount(String orderProductAmount) {
		this.orderProductAmount = orderProductAmount;
	}
	public String getOldAmount() {
		return oldAmount;
	}
	public void setOldAmount(String oldAmount) {
		this.oldAmount = oldAmount;
	}
	public String getNewAmount() {
		return newAmount;
	}
	public void setNewAmount(String newAmount) {
		this.newAmount = newAmount;
	}
	public String getRealPay() {
		return realPay;
	}
	public void setRealPay(String realPay) {
		this.realPay = realPay;
	}
	public String getDeduction() {
		return deduction;
	}
	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}
	public String getShopBonus() {
		return shopBonus;
	}
	public void setShopBonus(String shopBonus) {
		this.shopBonus = shopBonus;
	}
	public String getPlatBonus() {
		return platBonus;
	}
	public void setPlatBonus(String platBonus) {
		this.platBonus = platBonus;
	}
	public String getBankCardDeduction() {
		return bankCardDeduction;
	}
	public void setBankCardDeduction(String bankCardDeduction) {
		this.bankCardDeduction = bankCardDeduction;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getCardDeduction() {
		return cardDeduction;
	}
	public void setCardDeduction(String cardDeduction) {
		this.cardDeduction = cardDeduction;
	}
	public String getCouponDeduction() {
		return couponDeduction;
	}
	public void setCouponDeduction(String couponDeduction) {
		this.couponDeduction = couponDeduction;
	}
	public String getAvailablePrice() {
		return availablePrice;
	}
	public void setAvailablePrice(String availablePrice) {
		this.availablePrice = availablePrice;
	}
	public String getDiscountPercent() {
		return discountPercent;
	}
	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}
	public String getInsteadPrice() {
		return insteadPrice;
	}
	public void setInsteadPrice(String insteadPrice) {
		this.insteadPrice = insteadPrice;
	}
	public String getCouponUsed() {
		return couponUsed;
	}
	public void setCouponUsed(String couponUsed) {
		this.couponUsed = couponUsed;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getRefundApplyTime() {
		return refundApplyTime;
	}
	public void setRefundApplyTime(String refundApplyTime) {
		this.refundApplyTime = refundApplyTime;
	}
	public String getRefundReason() {
		return refundReason;
	}
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}
	public String getRefundRemark() {
		return refundRemark;
	}
	public void setRefundRemark(String refundRemark) {
		this.refundRemark = refundRemark;
	}
	
	public int getTakeOut() {
		return takeOut;
	}
	public void setTakeOut(int takeOut) {
		this.takeOut = takeOut;
	}
	public int getEatIn() {
		return eatIn;
	}
	public void setEatIn(int eatIn) {
		this.eatIn = eatIn;
	}
	public String getTableNbrSwitch() {
		return tableNbrSwitch;
	}

	public void setTableNbrSwitch(String tableNbrSwitch) {
		this.tableNbrSwitch = tableNbrSwitch;
	}
	public String getFirstDeduction() {
		return firstDeduction;
	}
	public void setFirstDeduction(String firstDeduction) {
		this.firstDeduction = firstDeduction;
	}
	public String getTableNbr() {
		return tableNbr;
	}
	public void setTableNbr(String tableNbr) {
		this.tableNbr = tableNbr;
	}
	public String getUserShopBonus() {
		return userShopBonus;
	}
	public void setUserShopBonus(String userShopBonus) {
		this.userShopBonus = userShopBonus;
	}
	public String getUserPlatBonus() {
		return userPlatBonus;
	}
	public void setUserPlatBonus(String userPlatBonus) {
		this.userPlatBonus = userPlatBonus;
	}
	public String getBankCardDiscount() {
		return bankCardDiscount;
	}
	public void setBankCardDiscount(String bankCardDiscount) {
		this.bankCardDiscount = bankCardDiscount;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getPayedTime() {
		return payedTime;
	}
	public void setPayedTime(String payedTime) {
		this.payedTime = payedTime;
	}
	public String getBankAccountCode() {
		return bankAccountCode;
	}
	public void setBankAccountCode(String bankAccountCode) {
		this.bankAccountCode = bankAccountCode;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getConsumeCode() {
		return consumeCode;
	}
	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}
	
}
