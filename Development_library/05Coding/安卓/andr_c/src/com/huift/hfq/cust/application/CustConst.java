package com.huift.hfq.cust.application;

public interface CustConst {
	/** 分页显示的条数 */
	public final static int PAGE_NUM = 10;
	/** 判断用户是否注册过 */
	public final static String IS_REGISTER = "register";

	/** 保存userCode */
	public final static String REGISTER_USERCODE = "userCode";

	public final static String ACT_TITLE = "actTitle";

	/** 保存shop */
	public final static String SHOP_OBJ = "shopObj";
	
	/** 保存商家反馈编码*/
	public final static String TO_SHOP_CODE = "toShopCode";
	
	/** 保存得到的商家反馈编码*/
	public final static String Err_SHOP_CODE = "errShopCode";
	
	/** 保存错误信息中的输入信息*/
	public final static String INPUT_ERROR_INFO = "inputErroeInfo";
	
	/** 从h5带过去的标志*/
	public final static String IMAGET_CODE_FLAG = "1";
	

	/** 获取验证码的时间 */
	public static final int INDENCODE_TIME = 9000;
	public final static String ThemeContent = "theme";
	public final static String Experices = "experices";

	/**
	 * 记录在哪需要登陆跳转到哪里的标识符
	 * 
	 * @author ad
	 */
	public interface Login {
		// 在home界面条跳转到登陆界面
		public final static String HOME_LOGIN = "homelogin";
		public final static String CARD_LOGIN = "cardlogin";
		public final static String COUPON_LOGIN = "couponlogin";
		public final static String MY_LOGIN = "mylogin";
	}
	
	/**
	 * 首页
	 * @author ad
	 */
	public interface Home {
		/** 滚屏*/
		public final static int SCROLL_VALUE = 1;
		/** 分类 */
		public final static int SHOPTYPE_VALUE = 2;
		/** 商圈*/
		public final static int SHOPCIRCLE_VALUE = 3;
		/** 品牌*/
		public final static int SHOPBRAND_VALUE = 4;
		/** 活动*/
		public final static int SHOPACT_VALUE = 5;
		/** 推荐商户*/
		public final static int SHOPLIST_VALUE = 6;
		/** 功能*/
		public final static int FUNCTION_VALUE = 7;
		/** 跳转到h5界面*/
		public final static String LINK_H5 = "0"; 
		/** 跳转到商家列表*/
		public final static String LINK_SHOP_LIST = "1";
		/** 优惠券列表*/
		public final static String LINK_COUPON_LIST = "2";
		/** 工银特惠*/
		public final static String LINK_ICBC = "3";
		
	}
	
	/**
	 * 新人注册送豪礼
	 * 
	 * @author ad
	 * 
	 */
	public interface IsOpenAct {
		public final static String OPEN = "1";
		public final static String CLOSE = "0";

	}

	/**
	 * 判断是否有数据
	 * 
	 * @author ad
	 */
	public interface DATA {
		/** 有数据 */
		public final static int HAVE_DATA = 1;
		/** 没数据数据 */
		public final static int NO_DATA = 0;
		/** 正在加载 */
		public final static int LOADIMG = 2;
	}

	/**
	 * 保存登录的用户名和密码
	 * 
	 * @author ad
	 * 
	 */
	public interface LoginSave {
		public final static String LOGIN_KEEP = "loginkeep";

	}

	/**
	 * 登录的key
	 * 
	 * @author ad
	 * 
	 */
	public interface LoginKey {
		/** 保存对象 */
		public final static String LOGITN_MAIN = "loginMain";
		public final static String LOGIN = "allLogin";
		/** 判断是否在登录页 */
		public final static String IS_LOGIN_MAIN = "isLogin";
		/** 登陆的时间 */
		public final static String LOGIN_TIME = "loginTime";
	}

	public interface Key {
		public final static String CITY_OBJ = "citys";
		public final static String CITY_NAME = "citysname";
		public final static String CITY_LAT = "latitude";
		public final static String CITY_LONG = "longitude";
		public final static String SCAN_SAVE = "scansave";
		/** 判断是否有发消息 */
		public final static String MSG_SEND = "msgSend";
		/** 个人信息是否修改 */
		public final static String UPP_USERINFO = "upp_userInfo";
		/** 抢优惠券成功 */
		public final static String GRAB_COUPON = "grabCoupon";
		/** app手动更新 */
		public final static String APP_UPP = "appUpdate";
		/** 关注商家是否有数据 */
		public final static String IS_DATA = "data";
		/** 订单是否取消成功 */
		public final static String CANCEL_ORDER_ISSUCCESS = "issuccess";
		/** 判断用户是否取消或者关注了商家 */
		public final static String UPP_IS_FOCUS = "upp_isfocus";

	}

	/**
	 * 活动详情的key
	 * 
	 * @author ad
	 * 
	 */
	public interface ActDetailKey {
		/** 保存对象 */
		public final static String ACT_MAIN = "actMain";
		public final static String ACT = "allAct";
		/** 判断是否在活动详情页 */
		public final static String IS_ACT_MAIN = "isAct";
		/** 接受到的activityCode */
		public final static String ACT_CODE = "activityCode";
		/** 接受的活动名称 */
		public final static String ACT_NAME = "activityName";
	}

	/**
	 * 商家建议
	 * 
	 * @author ad
	 * 
	 */
	public interface SysSuggest {
		/** 50100-反馈内容为空 */
		public final static int FEEDBACK_CONTENT = 50100;
	}

	/**
	 * 消息
	 * 
	 * @author yanfang.li
	 */
	public interface Massage {
		/** 代表异业广播 0 */
		public final static int MSG_VIP = 0;
		/** 会员卡 1 */
		public final static int MSG_CARD = 1;
		/** 优惠券 2 */
		public final static int MSG_COUPON = 2;
		/** 代表异业广播 3 */
		public final static int MSG_SHOP = 3;
		/** 代表异业广播 3 */
		public final static int READ_SHOP = 0;
	}

	/**
	 * 保存值的key
	 * 
	 * @author ad
	 * 
	 */
	public interface Keys {
		/** 用户每次进来首页第一运行 */
		public final static String IS＿FIRST_RUN = "homefirstrun";
	}

	/** 抢红包 */
	public interface Redbag {
		/** 50714-今天的红包已经被领完 */
		public final static int TODAT_REDBAG_END = 50714;
		/** 50715-红包已经被领完了 */
		public final static int REDBAG_END = 50715;
		/** 50717-已经领取过红包 */
		public final static int REDBAG_GET = 50717;
		/** 50717-还未到抢红包时间 */
		public final static int REDBAG_NOTIME = 50722;
		/** 50723-抢红包活动已结束 */
		public final static int REDBAG_ACTEND = 50723;

	}

	/** Jpush */
	public interface JPush {
		public final static String JPUSH_REGID = "jpushregid";
	}

	/**
	 * 会员券
	 * 
	 * @author ad
	 * 
	 */
	public interface Coupon {
		/** 80221-抢完了 */
		public final static int GRAB_OVER = 80221;
		/** 80220-过期了 */
		public final static int GRAB_EXPIRED = 80220;
		/** 80235已经抢过该店铺的优惠了 */
		public final static int GRAB_SHOP_COUPON = 80235;
		/** 80222-领用/抢的数量达到上限 */
		public final static int GRAB_LIMIT = 80222;
		/** 80231优惠券不存在 */
		public final static int GRAB_NOEXIT = 80223;
		/** 优惠券类型 */
		// 1-N元购；3-抵扣券；4-折扣券；5-实物券；6-体验券
		/*
		 * public final static String N_BUY = "1"; public final static String
		 * DEDUCT = "3"; public final static String DISCOUNT = "4"; public final
		 * static String REAL_COUPON = "5"; public final static String
		 * EXPERIENCE = "6";
		 */

		/** N元购 */
		public final static String N_BUY = "1";
		/** 抵扣券 */
		public final static String DEDUCT = "3";
		/** 折扣券 */
		public final static String DISCOUNT = "4";
		/** 实物券 */
		public final static String REAL_COUPON = "5";
		/** 体验券 */
		public final static String EXPERIENCE = "6";
		/** 32-送新用户的抵扣券 */
		public final static String REG_DEDUCT = "32";
		/** 33-送邀请人的优惠券 */
		public final static String INVITE_DEDUCT = "33";
		/** 兑换券*/
		public final static String EXCHANGE_VOUCHER = "7";
		/** 代金券*/
		public final static String VOUCHER = "8";
		
		/** N元购 */
		public final static int INT_N_BUY = 1;
		/** 抵扣券 */
		public final static int INT_DEDUCT = 3;
		/** 折扣券 */
		public final static int INT_DISCOUNT = 4;
		/** 实物券 */
		public final static int INT_REAL_COUPON = 5;
		/** 体验券 */
		public final static int INT_EXPERIENCE = 6;
		/** 32-送新用户的抵扣券 */
		public final static int INT_REG_DEDUCT = 32;
		/** 33-送邀请人的优惠券 */
		public final static int INT_INVITE_DEDUCT = 33;
		/** 兑换券*/
		public final static int INT_EXCHANGE_VOUCHER = 7;
		/** 代金券*/
		public final static int INT_VOUCHER = 8;
	}

	/** 分类检索商户 */
	public interface Shop {
		/** 50501-请输入用户所在经度 */
		public final static int NO_LONGITUDE = 50501;
		/** 50502-请输入用户所在纬度 */
		public final static int NO_LATITUDE = 50502;
		/** 50316-商家不存在 */
		public final static int NO_SHOP = 50316;
		/** 所有类型 */
		public final static int ALL_TYPE = 0;
		/** 美食 */
		public final static int FOOD = 1;
		/** 咖啡 */
		public final static int COFFEE = 2;
		/** 健身 */
		public final static int FITNESS = 3;
		/** 娱乐 */
		public final static int ENTERTAINMENT = 4;
		/** 服装 */
		public final static int CLOTHING = 5;
		/** 其他 */
		public final static int OTHER = 6;
	}

	/** 会员卡 */
	public interface Card {
		/** 50004-您已关注过该商家 */
		public final static int BEEN_CONCEREND = 50004;
		/** 50005-已申请过了 */
		public final static int BEEN_APPLY = 50005;
		/** 50006-该种会员卡数量达上限 */
		public final static int OVER_COUNT = 50006;
		/** 50007-不符合申请的标准 */
		public final static int NO_APPLY_CRITERIA = 50007;
		/** 50500-请输入用户编码 */
		public final static int NO_USERCODE = 50500;
		/** 80302-请输入会员卡号 */
		public final static int NO_CARDCODE = 80302;
	}

	/**
	 * 报名
	 * 
	 * @author ad
	 * 
	 */
	public interface JOIN {
		public final static String JOIN_ACTIVITY = "enroll";
		public final static String JOIN_KEY = "enrollkey";
		public final static String USERACT_CODE = "userActCode";
		public final static String USER_ACT_CODE = "userActCode";
		public final static String ACTIVITY_CODE = "activityActCode";

	}

	/**
	 * 查看报名的详情
	 */
	public interface USERJOIN {
		public final static String ADULTM = "adultM";
		public final static String ADULTF = "adultF";
		public final static String KIDM = "kidM";
		public final static String KIDF = "kidF";
	}

	/** 报名异常 */
	public interface ENTROLL {
		/**
		 * 50016-您已报过名了
		 */
		public final static int BEEN_ENTRPLL = 50016;
	}

	/** 修改个人信息 */
	public interface InfoMation {
		/**
		 * 50001-请将信息填写完整；
		 */
		public final static int INFOMATION_NO_FULL = 50001;
		/**
		 * 50003-您没有修改任何信息
		 */
		public final static int INFOMATION_NO_UPPNOTHING = 50003;
	}

	/**
	 * 添加银行卡
	 * 
	 * @author ad
	 * 
	 */
	public interface AddBank {
		public final static String BANK_ZERO = "0";
		public final static String BANK_ONE = "1";
		public final static String BANK_TWO = "2";
		public final static String BANK_THREE = "3";
		public final static String BANK_FOUR = "4";
		public final static String BANK_FIVE = "5";
		public final static String BANK_SEX = "6";
		public final static String BANK_SEVEN = "7";
		public final static String BANK_NINE = "9";
		public final static String BANK_TWEELFTH = "12";
	}

	/**
	 * 二维码
	 */
	public interface TwoCode {
		public final static String ORDERNBR = "ordernbr";
		public final static String SCODE = "sCode";
		public final static String QTYPE = "qType";
		public final static String SSRC = "sSrc";
		public final static String PTYPE = "payType";
		public final static String SHOP_CODE = "shopCode";
		public final static String SHOP_PRICE = "sPrice";
		public final static String CTYPE = "type";
	}

	/**
	 * 修改密码
	 */
	public interface updatePwd {
		/**
		 * 60000-请输入手机号码；
		 */
		public final static int INPUT_NUM = 60000;
		/**
		 * 60001-手机号码不正确；
		 */
		public final static int ERROR_NUM = 60001;
		/**
		 * 60012原密码错误
		 */
		public final static int ERROR_STARTPWD = 60012;
		/**
		 * 60013新密码与原密码一致
		 */
		public final static int PWD_FIT = 60013;
		/**
		 * 60014请输入新密码
		 */
		public final static int INPUT_NEWPWD = 60014;
	}

	public interface ConsumeCode {
		public final static String CONSUME_CODE = "consumeCode";
	}

	/**
	 * 跳转到好h5界面的常量
	 */
	public interface HactTheme {
		/**  体验馆 */
		public final static String HOME_EXPERICES = "experices";
		/** 惠圈主题 */
		public final static String HOME_THEME = "theme";
		/**  首页的活动图片 */
		public final static String HOME_ACTIVITY = "home_activity";
		/** 惠币的介绍 */
		public final static String HUIBI_INTRO = "huibiIntro";
		/** 注册活动 */
		public final static String NEW_REGISTER = "new_register";
		/**  首页红包 */
		public final static String HOME_BOUNDS = "bounds";
		/**  注册协议 */
		public final static String HONE_SYSTERM = "systerm";
		/** 工银特惠 */
		public final static String HOME_ICBC = "icbc";
		/** 支付成功 */
		public final static String PAY_SUCCESS = "pay_success";
		/** 支付失败*/
		public final static String PAY_Fail = "pay_fail";
		/** 商家 */
		public final static String SHOP_DETAIL = "shopDetailType";
		/** 支付成功跳转到H5店铺详情*/
		public final static String H5SHOP_DETAIL = "h5shopdetal";
		/** 关于惠圈*/
		public final static String ABOUT_HUIQUAN = "aboutHuiquan";
		/** 支付成功后跳转到H5外卖订单*/
		public final static String H5TAKEOUT_DETAIL = "h5takeout_detail";
		/** 游戏 */
		public final static String GAME = "game";
		/** 进入订单详情界面 */
		public final static String ORDER_DETAIL = "orderDetail";
		/** 重新加菜 */
		public final static String ADD_MENU = "addMenu";
		/** 跳到店铺详情 */
		public final static String SCAN_ONT = "scanOne";
		/** 跳到点餐 */
		public final static String SCAN_TWO = "scanTwo";
		/**优惠券码使用成功*/
		public final static String PAY_COUPON_USE = "pay_coupon_use";
		
		/**商品/服务详情H5*/
		public final static String SHOP_PRODUCT_SERVICE = "shop_product_service";
		
	}
}
