package com.huift.hfq.shop;

public interface ShopConst {

	/** 跳转到主界面的ID **/
	String FRAG_ID = "fragid";
	/** 分页显示的条数 */
	int PAGE_NUM = 10;

	/** 获取验证码的时间 */
	int INDENCODE_TIME = 9000;

	/**红包的比例*/
	int MONEY_PERCEN = 100;

	/**保存红包的状态 */
	String ACTMONEY_TARTUS = "actMoneyStatus";

	/**保存月份*/
	String RECOMMONED_MONTH = "recommonedMonth";

	/**保存订单编码*/
	String ORDER_CODE = "orderCode";

	/** 保存订单详情*/
	String ORDER_INFO = "orderInfo";

	/** 保存传过去的类型*/
	String ORDER_TYPE = "orderType";

	/** 保存员工管理的品牌ID*/
	String STAFF_BTANDID = "brandId";

	/** 保存原先订单管理的菜谱*/
	String ORDER_MENUE = "orderMenue";

	/** 保存新加订单管理的菜谱*/
	String NEW_ORDER_MENUE = "neworderMenue";

	/** 保存订单管理的菜谱*/
	String CLICK_MENUE = "clickMenue";

	/** 保存订单管理的菜谱*/
	String HOME_TITLE = "homeTitle";

	/**
	 * 保存登录的用户名和密码
	 *
	 * @author ad
	 *
	 */
	interface LoginSave {
		String LOGIN_KEEP = "loginkeep";

		String LOGIN_FLAG = "login";
	}
	/**
	 * 判断是否有数据
	 * @author ad
	 */
	interface DATA {
		/** 有数据*/
		int HAVE_DATA = 1;
		/** 没数据数据*/
		int NO_DATA = 0;
		/** 正在加载*/
		int LOADIMG = 2;
	}

	/**
	 * 订单状态
	 */
	interface Order{
		/** 20-已下单*/
		String  ORDERED = "20";
		/** 21-已接单*/
		String  HAS_ORDER = "21";
		/** 22-已派送*/
		String  DELIVERED = "22";
		/** 23-已送达*/
		String  BEEN_SERVED = "23";
		/** 24-已撤销*/
		String  REVOKED = "24";
		/** 25-待下单*/
		String  PEND_ORDER = "25";
	}

	/**
	 * 记载每个页面下拉的时间
	 * @author ad
	 *  // B 端的  110001;  会员卡  110001  优惠券 120001 消息 130001 活动 140001  店铺 150001 其他 160001
	 *  // C 端的  210001;  会员卡  210001  优惠券 220001 消息 230001 活动 240001  店铺 250001 其他 260001
	 * */
	interface PullRefresh {
		/** 订单列表*/
		int SHOP_ORDER_FAIL_LIST_PULL = 150002;
	}

	/**
	 * 订单状态
	 */
	interface OrderTitle{
		String  DELIVEREDS = "配送中";
		String  TRADING_CANCEL = "交易取消";
		String  TRADING_SUCCESS = "交易成功";
		String  PEND_ORDER = "待接单";
		String  PEND_PAYMONEY = "待付款";
	}

	/**
	 * 商家首页的推送入驻商圈和添加商品展示
	 */
	interface ShopMessage{
		/** 入驻惠圈和添加商品展示的action*/
		String SETTLED_SHOP_ACTION = "settled_shop_action";
		/** 入驻惠圈的msg*/
		String INVITE_SHOP_MSG = "shop_msg";
		/** 添加商品展示的msg*/
		String ADD_SHOP_MSG = "shop_msg";
		/** 用户要求入驻惠圈标示*/
		String INVITE_SHOP  = "INVITE_SHOP";
		/** 用户要求添加商品展示标示*/
		String REMIND_SHOP  = "REMIND_SHOP";
	}

	/**
	 * 保存注册的用户名和密码
	 *
	 * @author ad
	 *
	 */
	interface RegisterSave {
		String REGISTER_KEEP = "registerkeep";
		String JPUSH_REGID = "jpushregid";
	}

	/**
	 * 保存值的key
	 *
	 * @author ad
	 *
	 */
	interface Key {
		/** 用户每次进来首页第一运行 */
		String IS＿FIRST_RUN = "homefirstrun";
		/** 判断会员卡添加或者修改是否成功*/
		String CARD_ADD = "cardAdd";
		/** 判断会员卡添加或者修改是否成功*/
		String COUPON_ADD = "couponAdd";
		/** 判断消息是否发送成功*/
		String MSG_SEND = "msgSend";
		/** 判断是否在首页*/
		String IS_HOME_MAIN = "homemain";
		/** 保存首页对象*/
		String HOME_MAIN = "homeIsMain";
		/** 保存首页对象*/
		String HOME = "allFragment";
		/** 判断是否收到推送消息，后去更新会员卡里面的内容*/
		String JPUSH_CARDACCEPT = "jPushCardAccept";
		/** 判断是否收到推送消息，然后去更新优惠券的内容*/
		String JPUSH_COUPONACCEPT = "jPushCouponAccept";
		/** 跟新*/
		String APP_UPP = "appUpp";
		/**是否修改了子相册里面数据*/
		String UPP_DECORATION = "uppDecoration";
		/** 是否修改了子相册的数据*/
		String UPP_ALBUM_PHOTO = "uppAlbumPhoto";
		/** 订单状态是否修改*/
		String UPP_ORDERSTATUS = "upp_orderstatus";
		/** 是否阅读了订单数量*/
		String HAVE_READ = "have_read";
		/** 商店是否有上架的商品*/
		String IS_HAVE_PRODUCT = "isHaveProduct";
		/** 修改商家店铺信息*/
		String UPP_SHOPINFO = "updateShopInfo";
		/** 修改或添加删除商家配送信息*/
		String UPP_DEL_ADD_DELIVERY = "shopDelivery";
	}

	/**
	 * 营销活动的key
	 * @author ad
	 *
	 */
	interface ActAddKey {
		/**保存对象*/
		String ACT_MAIN = "actMain";
		String ACT = "allAct";
		/** 判断是否在活动页*/
		String IS_ACT_MAIN = "isAct";
	}

	/**
	 * 登录的key
	 * @author ad
	 *
	 */
	interface LoginKey {
		/**保存对象*/
		String LOGITN_MAIN = "loginMain";
		String LOGIN = "allLogin";
		/** 判断是否在登录页*/
		String IS_LOGIN_MAIN = "isLogin";
		/**登陆的时间*/
		String LOGIN_TIME = "loginTime";
	}


	/**
	 * 优惠券
	 *
	 * @author yanfang.li
	 *
	 */
	interface Card {
		/** 会员卡添加修改的判断 */
		String CARD_FALG = "card";
		/** 会员卡的添加 */
		String CARD_ADD = "cardAdd";
		/** 会员卡的修改 */
		String CARD_UPP = "cardUpp";
		/** 会员卡等级的集合 */
		String CARD_LIST = "cardList";
		/** 会员卡等级的集合 */
		String CARD_ADDLIST = "cardAddList";
		/** 会员卡等级查询为空 */
		String CARD_NULL = "cardlistnull";
		/** 会员卡等级查询有值 */
		String CARD_VALUE = "cardlistvalue";
		/** 会员卡等级查询有值 */
		String CARD_REUTRN = "cardReturn";

		String LV_FIRST = "1";
		String LV_SECOND = "2";
		String LV_THIRD = "3";
	}

	/**
	 * 优惠券
	 *
	 * @author yanfang.li
	 */
	interface Coupon {
		/** 优惠券类型查询 */
		String COUPON_LIST = "couponlist";
		/** 保存图片 */
		String COUPON_IMAGE = "couponimage";
		/** 1-N元购 */
		String N_BUY = "1";
		/** 3-抵扣券*/
		String DEDUCT = "3";
		/** 4-折扣券*/
		String DISCOUNT = "4";
		/** 5-实物券*/
		String REAL_COUPON = "5";
		/**6-体验券 */
		String EXPERIENCE = "6";
		/** 32-送新用户的抵扣券*/
		String REG_DEDUCT = "32";
		/** 兑换券*/
		String EXCHANGE_VOUCHER = "7";
		/** 代金券*/
		String VOUCHER = "8";
	}

	interface Month {
		/** 1-12月 */
		int JAN = 1;
		int FEB = 2;
		int MAR = 3;
		int APR = 4;
		int MAY = 5;
		int JUN = 6;
		int JUL = 7;
		int AUG = 8;
		int SEP = 9;
		int OCT = 10;
		int NOV = 11;
		int DEC = 12;
	}

	interface Day {
		/** 30号 */
		int THIRTY = 30;
		/** 31号 */
		int THIRTY_ONE = 31;
		/** 28号 */
		int TWENTY_EIGHT = 28;
		/** 29号 */
		int TWENTY_NINE = 29;
		/** 21号 */
		int TWENTY_ONE = 21;
		/** 22号 */
		int TWENTY_TWO = 22;
		/** 23号 */
		int TWENTY_THIRD = 23;
		/** 24号 */
		int TWENTY_FOUR = 24;
	}

	/**
	 * 消息
	 *
	 * @author yanfang.li
	 */
	interface Massage {
		int MSG_VIP = 0;
		int MSG_CARD = 1;
		int MSG_COUPON = 2;
		/** 代表异业广播 */
		int MSG_SHOP = 3;
		int READ_SHOP = 0;
		/** 对惠圈的建议*/
		String FEED_BACK = "feedback";
		/** 邀请入驻*/
		String CONTENT = "content";

	}

	interface Shop {
		String SHOP_LOGO = "shoplogo";
		String SHOP_NAME = "shopName";
		String SHOP_PROVICE = "shopPrivce";
		String SHOP_CITY = "shopCity";
		String SHOP_STREET = "shopStreet";
	}

	/**
	 * 添加员工
	 */
	interface Staff {
		/** 60000 请输入手机号码 */
		int NO_MOBILENBR = 60000;
		/**60001 手机号码不正确*/
		int MOBILENBR_ERROR = 60001;
		/**60003-手机号已经被使用*/
		int MOBILENBR_BEENUSED = 60003;
		/**80040-请输入员工姓名*/
		int NO_STAFFNAME = 80040;
		/**80041-请输入员工类型*/
		int NO_STAFFTYPE = 80041;
		/**该手机号码已被使用*/
		int PHONE_USERED = 80043;
		String CODE = "mRetCode";
	}

	/**
	 * POS服务
	 */
	interface PosService {
		// 50314-商家编码不正确
		int SHOPCODE_ERROR = 50314;
		// 50316-商家不存在
		int SHOP_NOEXIT = 50316;
		// 50800-服务类型不正确
		int SERVICETYPE_ERROR = 50800;
	}

	interface SysSuggest {
		// 50100-反馈内容为空
		int FEEDBACK_CONTENT = 50100;
	}

	/**
	 * 营销活动
	 * @author ad
	 *
	 */
	interface ActAdd {
		String ACT_ADD = "actadd";
	}

	/**
	 * 红包列表
	 * @author ad
	 *
	 */
	interface ActMoneyDetail {
		String ACTMONEY_DETAIL = "moneyDetail";
		String ACTMONEY_NUMBER = "actMoneyNumber";
		String ACTMONEY_REGION= "actMoneyRegion";
		String ACTMONEY_AMOUNT= "actMoneyAmount";
		String ACTMONEY_START= "actMoneystart";
		String ACTMONEY_END= "actMoneyEnd";
		String ACTMONEY_TERM= "actMoneyTerm";
	}


	/**
	 * 保存商家名字
	 *
	 * @author ad
	 *
	 */
	interface ActListName {
		String ACT_SHOPNAME = "actShopName";
	}

	/**
	 * 保存商户的总浏览量
	 */
	interface Access{
		String USER_ACCESS_COUNT = "userAccessCount";
	}

	/**
	 * 二维码
	 */
	interface QrCode {
		String Q_TYPE = "qType";
		String SHOP_PROVICE = "Provice";
		String SHOP_CITY = "City";
		String SHOP_STREET = "Street";
		String SHOP_SCODE = "sCode";
		String USER_CODE = "userCode";
		String MOBILENBR = "mobileNbr";
		String COUPONCODE = "couponCode";
		String GENERAL_BONUSUSED = "generalBonusUsed";
		String SHOP_BONUSUSED = "shopBonusUsed";
		String TOTAL_PAYMENT = "totalPayment";
		String ACTUAL_PAYMENT = "actualPayment";
		String PAY_TIME = "payTime";
		String SHOP_CODE = "shopCode";
		String SHOP_LONGITUDE = "Longitude";
		String SHOP_LATITUDE = "Latitude";
		String SCODE = "sCode";
		String QTYPE = "qType";
		String SSRC = "sSrc";
	}

	/**
	 * 相册
	 */
	interface Photo{
		//80317-商家编码为空；
		//80500-子相册名字错误；
		int SHOP_CODE = 80317;
		int ALBUM_NAME = 80500;

	}

	/**
	 * h5跳转的页面
	 */
	interface WebPage{
		/**活动详情*/
		String ACT_DETAIL = "actDetail";
	}

	/**
	 * 对兑换券和代金券的验证
	 */
	interface CouponVerification {
		//11-已退款，12-申请退款；13-过期；20-未验证，30-已验证
		/** 11-已退款，不可用*/
		String REFUNDED = "11";
		/** 12-申请退款，不可用*/
		String REQUEST_REFUNDED = "12";
		/** 13-过期，不可用*/
		String EXPIRED = "13";
		/** 20-未验证*/
		String AVAILABLE = "20";
		/** 30-已验证*/
		String USERD = "30";
		/** 表示进的是代金券还是活动的 1.代金券   2.活动*/
		public static String IS_GO = "go";
		/** 51002 优惠券不可用*/
		String COUPON_NOT_USE = "51002";
		/** 80220 优惠券已过期*/
		String COUPON_HAS_EXPIRED = "80220";

	}

	/**
	 * 对兑换券和代金券的验证
	 */
	interface ActivityVerification {
		//1-已申请退款；2-已退款；3-已验证；4-未验证，可用；
		/** 1-已申请退款，不可用*/
		String APPLIED_REFUNDED = "1";
		/** 2-已退款，不可用*/
		String REFUNDED = "2";
		/** 3-已验证，不可用*/
		String VERIFIED = "3";
		/** 4-未验证 ，可用*/
		String UNVERIFIED = "4";
	}

	/**
	 * 修改商家信息的参数
	 */
	interface Params{
		/** 修改商家头像*/
		String UPDATE_SHOP_LOGO = "logoUrl";
		/** 修改外卖点餐*/
		String UPDATE_SHOP_ISOPENTAKEOUT= "isOpenTakeOut";
		/** 修改堂食点餐*/
		String UPDATE_SHOP_ISOPENEAT= "isOpenEat";
		/** 修改桌号管理*/
		String UPDATE_SHOP_TABLENBR_SWITCH = "TableNbrSwitch";
	}

	/**
	 * 保存扫码的输入金额
	 */
	interface InputAmout{
		/**消费金额*/
		String INPUT_CONSUMPTION = "inputConsumtion";
		/**不参与优惠金额*/
		String INPUT_NODISCOUNT = "inputNodiscount";
	}

	/**
	 * 修改活动的状态
	 */
	interface UppActStatus{
		// 50220   活动不存在; 50221 活动现在状态不可变更 ;50225, 活动状态没有变化
		/** 0     禁用或者未发布*/
		String DISABLE_OR_RELEASE = "0";
		/** 1     启用或者发布*/
		String ENABLE_OR_RELEASE = "1";
		/** 2     停止报名*/
		String STOP_SIGN_UP = "2";
		/** 3    活动取消*/
		String EVENTS_CANCELED = "3";
		/** 4   活动已结束*/
		String ACTIVITY_HAS_ENDED = "4";
		/** 5   活动已满员*/
		String ACTIVITY_FULL_MEMBER = "5";
		/** 50220   活动不存在*/
		String ACTIVITY_NOT_EXIST = "50220";
		/** 50221 活动现在状态不可变更 */
		String EVENTS_UNALTERABLE = "50221";
		/** 50225  活动状态没有变化*/
		String ACTIVITY_NO_CHANGE = "50225";
		/** 修改活动信息是否成功*/
		String UPP_ACTIVITY = "upp_activity";
		/** 编辑活动成功后的信息*/
		String UPP_ACTIVITY_SUCCESS = "upp_activity_success";
		/** 获得code*/
		String ACT_CODE = "actCode";

	}
}

