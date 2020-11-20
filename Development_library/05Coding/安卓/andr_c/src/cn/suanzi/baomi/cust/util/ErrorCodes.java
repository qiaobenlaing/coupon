package cn.suanzi.baomi.cust.util;

public class ErrorCodes {
	
	/** 线上支付错误编码   请输入消费金额*/
	public static final int INPUT_CSM_MONEY = 50400;
	/** 50401 消费金额不正确*/
	public static final int ERROR_CSM_MONEY = 50401;
	/** 50720-红包不可用*/
	public static final int NO_USE_BOUNS = 50720;
	/** 50724-红包已经过期*/
	public static final int BOUNS_EXPIRED = 50724;
	/** 80220-优惠券已经过期*/
	public static final int COUPON_EXPIRED = 80220;
	/** 80227-优惠券不可用*/
	public static final int NO_USE_COUPON = 80227;
	/** 60500-订单编码错误*/
	public static final int ERROR_ORDER_CODE = 60500;
	/** 80400-用户会员卡不可用*/
	public static final int NO_USE_CARD = 80400;
	
	/** 50056-银行账户为空*/
	public static final int BANK_ACCOUNT_EMPTY = 50056;
	/** 50057-银行账户编码错误*/
	public static final int BANK_ACCOUNT_CODE_ERROR = 50057;
	/** 50900-支付订单不存在*/
	public static final int  ORDER_NOT_EXIST= 50900;
	

}
