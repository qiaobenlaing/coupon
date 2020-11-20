package cn.suanzi.baomi.shop;

public interface ShopConst {

	/** 跳转到主界面的ID **/
	public final static String FRAG_ID = "fragid";
	/** 分页显示的条数 */
	public final static int PAGE_NUM = 10;
	
	/** 获取验证码的时间 */
	public static final int INDENCODE_TIME = 9000;
	
	/**红包的比例*/
	public static final int MONEY_PERCEN = 100;
	
	/**保存红包的状态 */
	public static final String ACTMONEY_TARTUS = "actMoneyStatus";
	
	/**保存月份*/
	public static final String RECOMMONED_MONTH = "recommonedMonth";
	
	/**保存订单编码*/
	public static final String ORDER_CODE = "orderCode";
	
	/** 保存订单详情*/
	public static final String ORDER_INFO = "orderInfo";
	
	/** 保存传过去的类型*/
	public static final String ORDER_TYPE = "orderType";
	
	/** 保存员工管理的品牌ID*/
	public static final String STAFF_BTANDID = "brandId";
	
	/** 保存原先订单管理的菜谱*/
	public static final String ORDER_MENUE = "orderMenue";
	
	/** 保存新加订单管理的菜谱*/
	public static final String NEW_ORDER_MENUE = "neworderMenue";
	
	/** 保存订单管理的菜谱*/
	public static final String CLICK_MENUE = "clickMenue";
	
	/** 保存订单管理的菜谱*/
	public static final String 	HOME_TITLE = "homeTitle";
	
	/**
	 * 保存登录的用户名和密码
	 * 
	 * @author ad
	 * 
	 */
	public interface LoginSave {
		public final static String LOGIN_KEEP = "loginkeep";
		
		public final static String LOGIN_FLAG = "login";
	}
	/**
	 * 判断是否有数据
	 * @author ad
	 */
	public interface DATA {
		/** 有数据*/
		public final static int HAVE_DATA = 1;
		/** 没数据数据*/
		public final static int NO_DATA = 0;
		/** 正在加载*/
		public final static int LOADIMG = 2;
	}
	
	/**
	 * 订单状态
	 */
	public interface Order{
		/** 20-已下单*/
		public final static String  ORDERED = "20";
		/** 21-已接单*/
		public final static String  HAS_ORDER = "21";
		/** 22-已派送*/
		public final static String  DELIVERED = "22";
		/** 23-已送达*/
		public final static String  BEEN_SERVED = "23";
		/** 24-已撤销*/
		public final static String  REVOKED = "24";
		/** 25-待下单*/
		public final static String  PEND_ORDER = "25";
	}
	
	/**
	 * 记载每个页面下拉的时间
	 * @author ad
	 *  // B 端的  110001;  会员卡  110001  优惠券 120001 消息 130001 活动 140001  店铺 150001 其他 160001
	 *  // C 端的  210001;  会员卡  210001  优惠券 220001 消息 230001 活动 240001  店铺 250001 其他 260001 
	 * */  
	public interface PullRefresh {
	    /** 订单列表*/
	    public static final int SHOP_ORDER_FAIL_LIST_PULL = 150002;
	}
	
	/**
	 * 订单状态
	 */
	public interface OrderTitle{
		public final static String  DELIVEREDS = "配送中";
		public final static String  TRADING_CANCEL = "交易取消";
		public final static String  TRADING_SUCCESS = "交易成功";
		public final static String  PEND_ORDER = "待接单";
		public final static String  PEND_PAYMONEY = "待付款";
	}
	
	/**
	 * 商家首页的推送入驻商圈和添加商品展示
	 */
	public interface ShopMessage{
		/** 入驻惠圈和添加商品展示的action*/
		public final static String SETTLED_SHOP_ACTION = "settled_shop_action";
		/** 入驻惠圈的msg*/
		public final static String INVITE_SHOP_MSG = "shop_msg";
		/** 添加商品展示的msg*/
		public final static String ADD_SHOP_MSG = "shop_msg";
		/** 用户要求入驻惠圈标示*/
		public static final String INVITE_SHOP  = "INVITE_SHOP";
		/** 用户要求添加商品展示标示*/
		public static final String REMIND_SHOP  = "REMIND_SHOP";
	}
	
	/**
	 * 保存注册的用户名和密码
	 * 
	 * @author ad
	 * 
	 */
	public interface RegisterSave {
		public final static String REGISTER_KEEP = "registerkeep";
		public final static String JPUSH_REGID = "jpushregid";
	}

	/**
	 * 保存值的key
	 * 
	 * @author ad
	 * 
	 */
	public interface Key {
		/** 用户每次进来首页第一运行 */
		public final static String IS＿FIRST_RUN = "homefirstrun";
		/** 判断会员卡添加或者修改是否成功*/
		public final static String CARD_ADD = "cardAdd";
		/** 判断会员卡添加或者修改是否成功*/
		public final static String COUPON_ADD = "couponAdd";
		/** 判断消息是否发送成功*/
		public final static String MSG_SEND = "msgSend";
		/** 判断是否在首页*/
		public final static String IS_HOME_MAIN = "homemain";
		/** 保存首页对象*/
		public final static String HOME_MAIN = "homeIsMain";
		/** 保存首页对象*/
		public final static String HOME = "allFragment";
		/** 判断是否收到推送消息，后去更新会员卡里面的内容*/
		public final static String JPUSH_CARDACCEPT = "jPushCardAccept";
		/** 判断是否收到推送消息，然后去更新优惠券的内容*/
		public final static String JPUSH_COUPONACCEPT = "jPushCouponAccept";
		/** 跟新*/
		public final static String APP_UPP = "appUpp";
		/**是否修改了子相册里面数据*/
		public final static String UPP_DECORATION = "uppDecoration";
		/** 是否修改了子相册的数据*/
		public final static String UPP_ALBUM_PHOTO = "uppAlbumPhoto";
		/** 订单状态是否修改*/
		public final static String UPP_ORDERSTATUS = "upp_orderstatus";
		/** 是否阅读了订单数量*/
		public final static String HAVE_READ = "have_read";
		/** 商店是否有上架的商品*/
		public final static String IS_HAVE_PRODUCT = "isHaveProduct";
		/** 修改商家店铺信息*/
		public final static String UPP_SHOPINFO = "updateShopInfo";
		/** 修改或添加删除商家配送信息*/
		public final static String UPP_DEL_ADD_DELIVERY = "shopDelivery";
	}
	
	/**
	 * 营销活动的key
	 * @author ad
	 * 
	 */
	public interface ActAddKey {
		/**保存对象*/
		public final static String ACT_MAIN = "actMain";
		public final static String ACT = "allAct";
		/** 判断是否在活动页*/
		public final static String IS_ACT_MAIN = "isAct";
	}
	
	/**
	 * 登录的key
	 * @author ad
	 * 
	 */
	public interface LoginKey {
		/**保存对象*/
		public final static String LOGITN_MAIN = "loginMain";
		public final static String LOGIN = "allLogin";
		/** 判断是否在登录页*/
		public final static String IS_LOGIN_MAIN = "isLogin";
		/**登陆的时间*/
		public final static String LOGIN_TIME = "loginTime";
	}
	

	/**
	 * 优惠券
	 * 
	 * @author yanfang.li
	 * 
	 */
	public interface Card {
		/** 会员卡添加修改的判断 */
		public final static String CARD_FALG = "card";
		/** 会员卡的添加 */
		public final static String CARD_ADD = "cardAdd";
		/** 会员卡的修改 */
		public final static String CARD_UPP = "cardUpp";
		/** 会员卡等级的集合 */
		public final static String CARD_LIST = "cardList";
		/** 会员卡等级的集合 */
		public final static String CARD_ADDLIST = "cardAddList";
		/** 会员卡等级查询为空 */
		public final static String CARD_NULL = "cardlistnull";
		/** 会员卡等级查询有值 */
		public final static String CARD_VALUE = "cardlistvalue";
		/** 会员卡等级查询有值 */
		public final static String CARD_REUTRN = "cardReturn";

		public final static String LV_FIRST = "1";
		public final static String LV_SECOND = "2";
		public final static String LV_THIRD = "3";
	}

	/**
	 * 优惠券
	 * 
	 * @author yanfang.li
	 */
	public interface Coupon {
		/** 优惠券类型查询 */
		public final static String COUPON_LIST = "couponlist";
		/** 保存图片 */
		public final static String COUPON_IMAGE = "couponimage";
		/** 1-N元购 */
		public final static String N_BUY = "1";
		/** 3-抵扣券*/
		public final static String DEDUCT = "3";
		/** 4-折扣券*/
		public final static String DISCOUNT = "4";
		/** 5-实物券*/
		public final static String REAL_COUPON = "5";
		/**6-体验券 */
		public final static String EXPERIENCE = "6";
		/** 32-送新用户的抵扣券*/
		public final static String REG_DEDUCT = "32";
		/** 兑换券*/
		public final static String EXCHANGE_VOUCHER = "7";
		/** 代金券*/
		public final static String VOUCHER = "8";
	}

	public interface Month {
		/** 1-12月 */
		public final static int JAN = 1;
		public final static int FEB = 2;
		public final static int MAR = 3;
		public final static int APR = 4;
		public final static int MAY = 5;
		public final static int JUN = 6;
		public final static int JUL = 7;
		public final static int AUG = 8;
		public final static int SEP = 9;
		public final static int OCT = 10;
		public final static int NOV = 11;
		public final static int DEC = 12;
	}

	public interface Day {
		/** 30号 */
		public final static int THIRTY = 30;
		/** 31号 */
		public final static int THIRTY_ONE = 31;
		/** 28号 */
		public final static int TWENTY_EIGHT = 28;
		/** 29号 */
		public final static int TWENTY_NINE = 29;
		/** 21号 */
		public final static int TWENTY_ONE = 21;
		/** 22号 */
		public final static int TWENTY_TWO = 22;
		/** 23号 */
		public final static int TWENTY_THIRD = 23;
		/** 24号 */
		public final static int TWENTY_FOUR = 24;
	}

	/**
	 * 消息
	 * 
	 * @author yanfang.li
	 */
	public interface Massage {
		public final static int MSG_VIP = 0;
		public final static int MSG_CARD = 1;
		public final static int MSG_COUPON = 2;
		/** 代表异业广播 */
		public final static int MSG_SHOP = 3;
		public final static int READ_SHOP = 0;
		/** 对惠圈的建议*/
		public final static String FEED_BACK = "feedback";
		/** 邀请入驻*/
		public final static String CONTENT = "content";
		
	}

	public interface Shop {
		public final static String SHOP_LOGO = "shoplogo";
		public final static String SHOP_NAME = "shopName";
		public final static String SHOP_PROVICE = "shopPrivce";
		public final static String SHOP_CITY = "shopCity";
		public final static String SHOP_STREET = "shopStreet";
	}
	
	/**
	 * 添加员工
	 */
	public interface Staff {
		/** 60000 请输入手机号码 */ 
		public final static int NO_MOBILENBR = 60000;
		/**60001 手机号码不正确*/
		public final static int MOBILENBR_ERROR = 60001;
		/**60003-手机号已经被使用*/
		public final static int MOBILENBR_BEENUSED = 60003;
		/**80040-请输入员工姓名*/
		public final static int NO_STAFFNAME = 80040;
		/**80041-请输入员工类型*/
		public final static int NO_STAFFTYPE = 80041;
		/**该手机号码已被使用*/
		public final static int PHONE_USERED = 80043;
		public final static String CODE = "mRetCode";
	}

	/**
	 * POS服务
	 */
	public interface PosService {
		// 50314-商家编码不正确
		public final static int SHOPCODE_ERROR = 50314;
		// 50316-商家不存在
		public final static int SHOP_NOEXIT = 50316;
		// 50800-服务类型不正确
		public final static int SERVICETYPE_ERROR = 50800;
	}

	public interface SysSuggest {
		// 50100-反馈内容为空
		public final static int FEEDBACK_CONTENT = 50100;
	}

	/**
	 * 营销活动
	 * @author ad
	 * 
	 */
	public interface ActAdd {
		public final static String ACT_ADD = "actadd";
	}
	
	/**
	 * 红包列表
	 * @author ad
	 * 
	 */
	public interface ActMoneyDetail {
		public final static String ACTMONEY_DETAIL = "moneyDetail";
		public final static String ACTMONEY_NUMBER = "actMoneyNumber";
		public final static String ACTMONEY_REGION= "actMoneyRegion";
		public final static String ACTMONEY_AMOUNT= "actMoneyAmount";
		public final static String ACTMONEY_START= "actMoneystart";
		public final static String ACTMONEY_END= "actMoneyEnd";
		public final static String ACTMONEY_TERM= "actMoneyTerm";
	}
	

	/**
	 * 保存商家名字
	 * 
	 * @author ad
	 * 
	 */
	public interface ActListName {
		public final static String ACT_SHOPNAME = "actShopName";
	}
	
	/**
	 * 保存商户的总浏览量
	 */
	public interface Access{
		public final static String USER_ACCESS_COUNT = "userAccessCount";
	}

	/**
	 * 二维码
	 */
	public interface QrCode {
		public final static String Q_TYPE = "qType";
		public final static String SHOP_PROVICE = "Provice";
		public final static String SHOP_CITY = "City";
		public final static String SHOP_STREET = "Street";
		public final static String SHOP_SCODE = "sCode";
		public final static String USER_CODE = "userCode";
		public final static String MOBILENBR = "mobileNbr";
		public final static String COUPONCODE = "couponCode";
		public final static String GENERAL_BONUSUSED = "generalBonusUsed";
		public final static String SHOP_BONUSUSED = "shopBonusUsed";
		public final static String TOTAL_PAYMENT = "totalPayment";
		public final static String ACTUAL_PAYMENT = "actualPayment";
		public final static String PAY_TIME = "payTime";
		public final static String SHOP_CODE = "shopCode";
		public final static String SHOP_LONGITUDE = "Longitude";
		public final static String SHOP_LATITUDE = "Latitude";
		public final static String SCODE = "sCode";
		public final static String QTYPE = "qType";
		public final static String SSRC = "sSrc";
	}
	
	/**
	 * 相册
	 */
	public interface Photo{
		//80317-商家编码为空；
		//80500-子相册名字错误；
		public final static int SHOP_CODE = 80317;
		public final static int ALBUM_NAME = 80500;

	}
	
	/**
	 * h5跳转的页面
	 */
	public interface WebPage{
		/**活动详情*/
		public final static String ACT_DETAIL = "actDetail";
	}
	
	/**
	 * 对兑换券和代金券的验证
	 */
	public interface CouponVerification {
		//11-已退款，12-申请退款；13-过期；20-未验证，30-已验证
		/** 11-已退款，不可用*/
		public final static String REFUNDED = "11";
		/** 12-申请退款，不可用*/
		public final static String REQUEST_REFUNDED = "12";
		/** 13-过期，不可用*/
		public final static String EXPIRED = "13";
		/** 20-未验证*/
		public final static String AVAILABLE = "20";
		/** 30-已验证*/
		public final static String USERD = "30";
		/** 表示进的是代金券还是活动的 1.代金券   2.活动*/
		public static String IS_GO = "go";
		/** 51002 优惠券不可用*/
		public final static String COUPON_NOT_USE = "51002";
		/** 80220 优惠券已过期*/
		public final static String COUPON_HAS_EXPIRED = "80220";
		
	}
	
	/**
	 * 对兑换券和代金券的验证
	 */
	public interface ActivityVerification {
		//1-已申请退款；2-已退款；3-已验证；4-未验证，可用；
		/** 1-已申请退款，不可用*/
		public final static String APPLIED_REFUNDED = "1";
		/** 2-已退款，不可用*/
		public final static String REFUNDED = "2";
		/** 3-已验证，不可用*/
		public final static String VERIFIED = "3";
		/** 4-未验证 ，可用*/
		public final static String UNVERIFIED = "4";
	}
	
	/**
	 * 修改商家信息的参数
	 */
	public interface Params{
		/** 修改商家头像*/
		public final static String UPDATE_SHOP_LOGO = "logoUrl";
		/** 修改外卖点餐*/
		public final static String UPDATE_SHOP_ISOPENTAKEOUT= "isOpenTakeOut";
		/** 修改堂食点餐*/
		public final static String UPDATE_SHOP_ISOPENEAT= "isOpenEat";
		/** 修改桌号管理*/
		public final static String UPDATE_SHOP_TABLENBR_SWITCH = "TableNbrSwitch";
	}
	
	/**
	 * 保存扫码的输入金额
	 */
	public interface InputAmout{
		/**消费金额*/
		public final static String INPUT_CONSUMPTION = "inputConsumtion";
		/**不参与优惠金额*/
		public final static String INPUT_NODISCOUNT = "inputNodiscount";
	}
	
	/**
	 * 修改活动的状态
	 */
	public interface UppActStatus{
		// 50220   活动不存在; 50221 活动现在状态不可变更 ;50225, 活动状态没有变化
		/** 0     禁用或者未发布*/
		public final static String DISABLE_OR_RELEASE = "0";
		/** 1     启用或者发布*/
		public final static String ENABLE_OR_RELEASE = "1";
		/** 2     停止报名*/
		public final static String STOP_SIGN_UP = "2";
		/** 3    活动取消*/
		public final static String EVENTS_CANCELED = "3";
		/** 4   活动已结束*/
		public final static String ACTIVITY_HAS_ENDED = "4";
		/** 5   活动已满员*/
		public final static String ACTIVITY_FULL_MEMBER = "5";
		/** 50220   活动不存在*/
		public final static String ACTIVITY_NOT_EXIST = "50220";
		/** 50221 活动现在状态不可变更 */
		public final static String EVENTS_UNALTERABLE = "50221";
		/** 50225  活动状态没有变化*/
		public final static String ACTIVITY_NO_CHANGE = "50225";
		/** 修改活动信息是否成功*/
		public final static String UPP_ACTIVITY = "upp_activity";
		/** 编辑活动成功后的信息*/
		public final static String UPP_ACTIVITY_SUCCESS = "upp_activity_success";
		/** 获得code*/
		public final static String ACT_CODE = "actCode";
		
	}
}

