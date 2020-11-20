// ---------------------------------------------------------
// @author    Weiping Liu
// @version   1.0.0
// @copyright 版权所有 (c) 2014 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------

package com.huift.hfq.base;
/**
 * 常量
 * 
 * @author Weiping Liu
 * @version 1.0.0
 */
public class Const {
	/** 是否debug模式 */
	public static final boolean IS_DEBUG = true;

	/**
	 * 系统用来存储文件的地方。 注意：不同的应用还用在各自APP下定义自己的默认文件夹。
	 */
	public static final String DEFAULT_DIR = "/huiquan";
	/** 数据库名称 */
	public static final String DB_NAME = "huiquan.db";

	/** 保存首页对象*/
	public final static String MPOS_CSMCODE = "mPosCsmCode";
	
	public static final String HQ_CODE = "00000000-0000-0000-0000-000000000000";
	/** 本地的数据库 */
//	public static final String HTTP_URL = "https://hfq.huift.com.cn";//本地版本
	public static final String HTTP_URL = "https://hq.hkctsbus.com";//生产环境
//	public static final String HTTP_URL = "https://cloud.hfq.huift.com.cn";

	/** 公共API地址 */
	public interface ApiAddr {
		/** 顾客端分享微信地址 开发版 */
		String SHARE_URL = HTTP_URL + "/Wechat/";
		/** h5 开发版*/
		String H5_URL = HTTP_URL + "/Api/";

		String COMM = HTTP_URL + "/Api/Comm";
		/** 商店端API地址 */
		String SHOP = HTTP_URL + "/Api/Shop";
		/** 顾客端API地址 */
		String CUST = HTTP_URL + "/Api/Client";
	}

	/**
	 * 判断是否首页是否有新人注册送豪礼的活动
	 */
	public static final String IS_OPENREGACT = "isOpenRegAct";

	/** 保存JpushId*/
	public static final String JPUSH_REGID = "jpushregid";
	
	/**
	 * 保存登陆的对象
	 * @author ad
	 */
	public interface Login {
		/** 登陆前的对象 店铺详情*/
		public static final String SHOP_DETAIL = "shopDetailFragment";
		/** 退出登录*/
		public static final String EXIT_LOGIN = "exitLogin";
		/** 外卖订单登录*/
		public static final String ORDER_LOGIN = "orderLogin";
		/** 新人注册*/
		public static final String NEW_REGISTER = "newRegister";
		/** 活动主题*/
		public static final String ACT_THEME = "actThemeDetail";
		/** h5*/
		public static final String H5_ACT = "h5act";
		/** 商家端执行登录*/
		public final static String ANDR_B_HOMEFRAGMENT = "andr_b_homeFragment";
	}
	
	/**
	 * App类型
	 * 
	 * @author Weiping
	 */
	public interface AppType {
		/** 商家端 */
		public static final int SHOP = 0;
		/** 顾客端 */
		public static final int CUST = 1;
	}

	/** JSON-RPC API请求出错时最多尝试次数（包括第一次）。 */
	public static final int API_MAX_TRY = 3;

	/**
	 * 记载每个页面下拉的时间
	 * @author ad
	 *  // B 端的  110001;  会员卡  110001  优惠券 120001 消息 130001 活动 140001  店铺 150001 订单 160001  员工管理170001 其他180001
	 *  // C 端的  210001;  会员卡  210001  优惠券 220001 消息 230001 活动 240001  店铺 250001 其他 260001 
	 * */  
	public interface PullRefresh {
		/** 活动列表*/
		public static final int CUST_ACT_LIST_PULL = 240001;
		/** 历史的优惠券下拉  已过期*/
		public static final int CUST_COUPON_UNUSED = 220001;
		/** 历史的优惠券下拉  已使用*/
		public static final int CUST_COUPON_USED = 220002;
		/** 有效的优惠券*/
		public static final int CUST_EFFECT_COUPON = 220003;
		/** homeFragment首页*/
		public static final int CUST_HOME = 260001;
		/** 我的消息 会员卡*/
		public static final int CUST_CARD_MSG = 230001;
		/** 我的消息 优惠券*/
		public static final int CUST_COUPON_MSG = 230002;
		/** 我的消息 异业广播*/
		public static final int CUST_SHOP_MSG = 230003;
		/** 我的消息 会员聊天*/
		public static final int CUST_VIP_MSG = 230004;
		/** 我的消息界面*/
		public static final int CUST_VIP_CHAT = 230005;
		/** 店铺列表*/
		public static final int CUST_SHOP_LIST = 250003;
		/** 关注商家*/
	    public static final int SHOP_FOCUS_LIST_PULL = 250004;
	    /** 订单完成*/
	    public static final int SHOP_ORDER_FAIL_LIST_PULL = 250005;
	    /** 未支付订单*/
	    public static final int SHOP_ORDER_SUCCESS_LIST_PULL = 250006;
		/**足迹*/
		public static final int CUST_COMMON_LIST_PULL = 250001;
		/**附近*/
		public static final int CUST_NEARLY_LIST_PULL = 250002;
		
		/** 我的消息 会员卡*/
		public static final int SHOP_CARD_MSG = 130001;
		/** 我的消息 优惠券*/
		public static final int SHOP_COUPON_MSG = 130002;
		/** 我的消息 异业广播*/
		public static final int SHOP_SHOP_MSG = 130003;
		/** 我的消息 会员聊天*/
		public static final int SHOP_VIP_MSG = 130004;
		/** 我的消息界面*/
		public static final int SHOP_VIP_CHAT = 130005;
		
		/** 餐后支付 --结算*/
		public static final int SHOP_AFTER_PAY = 160001;
		
		/** 员工管理*/
		public static final int STAFF_SHOP_LIST = 170001;
		/** 员工列表管理*/
		public static final int STAFF_SHOP_MANAGER = 170002;
	}
}
