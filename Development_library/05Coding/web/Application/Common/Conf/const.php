<?php
// 这里可以使用define定义一些常量
define('CC', 'cc1');



/** 常量类 */
class Consts {
    /** session USER */
    const SESS_USER = 'USER';
    /** cookie user wechat open id */
    const COOKIE_OPEN_ID = 'OPEN_ID';
    /** cookie user role */
    const COOKIE_ROLE = 'ROLE';
    /**优惠券来源**/
    const COUPON_BELONG_SHOP = 1; //  商户优惠券
    const COUPON_BELONG_PLAT = 2; //  平台优惠券
    /**发送验证码的动作*/
    const MSG_VAL_ACTION_REG = 'r'; // 注册
    const MSG_VAL_ACTION_FIND_PWD = 'f'; // 找回密码
    const MSG_VAL_ACTION_ACTIVATE = 'a'; // 商家端激活
    const MSG_VAL_ACTION_SET_PAY_PWD = 'spp'; // 用户设置支付密码
    const MSG_VAL_ACTION_ADD_MR = 'mr'; // 添加短信接收人
    /** JPush推送应用信息*/
    const J_PUSH_APP_KEY_B = 'f25791f60bbb4c5f3676b3fe'; // 商家端AppKey
    const J_PUSH_MASTER_SECRET_B = '85d76db160a78a40a6bbf7e6'; // 商家端Master Secret
    const J_PUSH_APP_KEY_C = '3be895fd29dd0835857cf35b'; // 顾客端AppKey
    const J_PUSH_MASTER_SECRET_C = 'd78480d9f70b8dbe6ad50f7b'; // 顾客端Master Secret
    /** 生产环境地址*/
    const WEB_PRODUCTION = 'web.huiquan.suanzi.cn'; // 后台地址
    const WEB_NICK_NAME_PRODUCTION = 'huiquan.suanzi.cn'; // 后台地址昵称
    /**100 */
    const HUNDRED = 100;
    /** 1000*/
    const THOUSAND = 1000;
    /** 一页数据量 */
    const PAGESIZE = 10;
    /** 是否验证token是否过期*/
    const IS_VALIDATE_TOKEN_EXPIRE = '0'; // 是否验证token是否过期，0表示不验证，1表示验证
    /** 是*/
    const YES = '1';
    /** 否*/
    const NO = '0';
    /** 惠圈下载统计表相关*/
    const DOWNLOAD_LOG_OPERATION_AD = '1'; // android
    const DOWNLOAD_LOG_OPERATION_IOS = '2'; // IOS
    /** 惠圈员工相关*/
    // 惠圈员工角色
    const HQ_STAFF_TYPE_ADMIN = '1'; // 管理员
    const HQ_STAFF_TYPE_GROUND_PERSON = '10'; // 地推人员
    /** 惠圈员工状态*/
    const HQ_STAFF_STATUS_ACTIVE = '1'; // 启用
    const HQ_STAFF_STATUS_FROZEN = '0'; // 禁用
    const HQ_STAFF_STATUS_DELETE = '2'; // 已删除
    /** 支付状态*/
    const PAY_STATUS_CAN_NOT_PAY = '0'; // 不能支付
    const PAY_STATUS_UNPAYED = '1'; // 未付款
    const PAY_STATUS_PAYING = '2'; // 付款中
    const PAY_STATUS_PAYED = '3'; // 已付款
    const PAY_STATUS_CANCELED = '4'; // 已取消订单
    const PAY_STATUS_FAIL = '5'; // 付款失败
    const PAY_STATUS_REFUNDING = '6'; // 退款受理中
    const PAY_STATUS_REFUNDED = '7'; // 已退款
    const PAY_STATUS_PART_REFUNDED = '8'; // 已部分退款
    /** 工行卡状态*/
    const BANKACCOUNT_STATUS_DISABLE = '0'; // 禁用
    const BANKACCOUNT_STATUS_NO_SIGN = '1'; // 未签订协议
    const BANKACCOUNT_STATUS_SIGNED = '2'; // 已签订协议
    const BANKACCOUNT_STATUS_TERMINATE = '3'; // 已解除协议
    const BANKACCOUNT_STATUS_CANCELLED = '4'; // 已取消签订协议
    /** 餐饮订单状态*/
    const CATERING_ORDER_STATUS_ORDERED = '20'; // 已下单
    const CATERING_ORDER_STATUS_RECEIVED = '21'; // 已接单
    const CATERING_ORDER_STATUS_DELIVERED = '22'; // 已派送
    const CATERING_ORDER_STATUS_SERVED = '23'; // 已送达
    const CATERING_ORDER_STATUS_CANCELED = '24'; // 已撤销
    const CATERING_ORDER_STATUS_UNORDERED = '25'; // 待下单
    /** 阅读状态*/
    const READING_STATUS_READ = '1'; // 已读
    const READING_STATUS_UNREAD = '0'; // 未读
    /** 优惠券类型*/
    const COUPON_TYPE_N_PURCHASE = '1'; // N元购
    const COUPON_TYPE_REDUCTION = '3'; // 抵扣券
    const COUPON_TYPE_DISCOUNT = '4'; // 折扣券
    const COUPON_TYPE_PHYSICAL = '5'; // 实物券
    const COUPON_TYPE_EXPERIENCE = '6'; // 体验券
    const COUPON_TYPE_EXCHANGE = '7'; // 兑换券
    const COUPON_TYPE_VOUCHER = '8'; // 代金券
    const COUPON_TYPE_NEW_CLIENT_REDUCTION = '32'; // 送新用户的抵扣券
    const COUPON_TYPE_SEND_INVITER = '33'; // 送邀请人的抵扣券
    /** 订单购买的优惠券状态*/
    const ORDER_COUPON_STATUS_UNPAY_NOUSE = '10'; // 订单未付款，不可用
    const ORDER_COUPON_STATUS_REFUNDED_NOUSE = '11'; // 已退款，不可用
    const ORDER_COUPON_STATUS_REFUNDING_NOUSE = '12'; // 退款中，不可用
    const ORDER_COUPON_STATUS_USE = '20'; // 可用
    const ORDER_COUPON_STATUS_USED = '30'; // 已使用
    /** 用户活动码状态*/
    const USER_ACT_CODE_STATUS_UNPAY_NOUSE = '0'; // 未付款，不可用
    const USER_ACT_CODE_STATUS_REFUNDING_NOUSE = '1'; // 已申请退款，不可用
    const USER_ACT_CODE_STATUS_REFUNDED_NOUSE = '2'; // 已退款，不可用
    const USER_ACT_CODE_STATUS_USED = '3'; // 已验证，不可用
    const USER_ACT_CODE_STATUS_USE = '4'; // 未验证，可使用
    /** 清算状态*/
    const LIQUIDATION_STATUS_HAD_NOT = '0'; // 未清算
    const LIQUIDATION_STATUS_ING = '1'; // 清算中
    const LIQUIDATION_STATUS_HAD = '2'; // 已清算
    const LIQUIDATION_STATUS_NO_NEED = '3'; // 不需要清算
    /** 订单类型*/
    const ORDER_TYPE_OTHER = '10'; // 其他订单
    const ORDER_TYPE_NO_TAKE_OUT = '20'; // 堂食订单
    const ORDER_TYPE_TAKE_OUT = '21'; // 外卖订单
    const ORDER_TYPE_COUPON = '30'; // 买券订单
    const ORDER_TYPE_ACT = '40'; // 活动订单
    /** 订单状态*/
    const ORDER_STATUS_ORDERED = '20'; // 已下单
    const ORDER_STATUS_RECEIVED = '21'; // 已接单
    const ORDER_STATUS_DELIVERY = '22'; // 已派送
    const ORDER_STATUS_SERVED = '23'; // 已送达
    const ORDER_STATUS_CANCELED = '24'; // 已撤单
    const ORDER_STATUS_UNORDERED = '25'; // 待下单
    /** 预采用商户相关*/
    // 预采用商户的状态
    const PRE_SHOP_STATUS_NOT_USE = '1'; // 未采用
    const PRE_SHOP_STATUS_USED = '2'; // 已采用
    /** 商品类别相关*/
    // 掌柜类别
    const PRODUCT_CATEGORY_RECOM_ID = 'r'; // 掌柜推荐类别的ID
    /** 商品相关*/
    // 商品的类型
    const PRODUCT_TYPE_SINGLE = '1'; // 单品
    const PRODUCT_TYPE_PACKAGE = '2'; // 套餐
    // 商品的性别限制
    const PRODUCT_SEX_LIMIT_NO_LIMIT = '0'; // 无限制
    const PRODUCT_SEX_LIMIT_MAN = '1'; // 男
    const PRODUCT_SEX_LIMIT_WOMAN = '2'; // 女
    // 商品的年龄限制
    const PRODUCT_AGE_LIMIT_NO_LIMIT = '0'; // 无限制
    const PRODUCT_AGE_LIMIT_UPPER_18 = '1'; // 18岁以上
    // 商品的是否预约
    const PRODUCT_IS_BOOKING_NO = '0'; // 否
    const PRODUCT_IS_BOOKING_YES = '1'; // 是
    /** 商家申请入驻相关*/
    // 商家申请入驻的处理状态
    const SHOP_APPLY_ENTRY_STATUS_NO_DEAL = '1'; // 未处理
    const SHOP_APPLY_ENTRY_STATUS_DEALT = '2'; // 已处理
    /** 商户相关*/
    // 商户入驻状态
    const SHOP_ENTER_STATUS_NO = '1'; // 未入驻
    const SHOP_ENTER_STATUS_YES = '2'; // 已入驻
    // 商户类型
    const SHOP_TYPE_UNKNOWN = '0'; // 未知
    const SHOP_TYPE_FOOD = '1'; // 美食
    const SHOP_TYPE_BEAUTY = '2'; // 丽人
    const SHOP_TYPE_FITNESS = '3'; // 健身
    const SHOP_TYPE_ENTERTAINMENT = '4'; // 娱乐
    const SHOP_TYPE_CLOTHING = '5'; // 服装
    const SHOP_TYPE_OTHER = '6'; // 其他
    const SHOP_TYPE_PLAT = '11'; // 苞米平台
    const SHOP_TYPE_ICBC = '21'; // 中国工商银行
    /** 商家员工相关*/
    // 员工角色
    const SHOP_STAFF_LVL_BIG_MANAGER = '3'; // 大店长
    const SHOP_STAFF_LVL_MANAGER = '2'; // 店长
    const SHOP_STAFF_LVL_EMPLOYEE = '1'; // 员工
    // 员工状态
    const SHOP_STAFF_STATUS_NOT_ACTIVA = '-1'; // 未激活
    const SHOP_STAFF_STATUS_DISABLE = '0'; // 禁用
    const SHOP_STAFF_STATUS_ACTIVE = '1'; // 启用
    /** 工行交易码*/
    const TXCODE_SIGN_CONTRACT = '20100'; // 支付协议签订交易
    const TXCODE_TERMINATE_CONTRACT = '20110'; // 支付协议解除交易
    const TXCODE_VAL_CODE = '20260'; // 手机验证码获取交易
    const TXCODE_BANKCARD_CONSUME = '20270'; // 银行卡消费交易
    const TXCODE_CONSUME_REVOCATION = '20271'; // 银行卡消费撤销交易
    const TXCODE_QUERY_RET = '20250'; // 查询交易
    const TXCODE_CREDIT_CARD_RETURN_GOOD = '20240'; // 信用卡退货交易
    /** 用户性别*/
    const USER_SEX_M = 'M'; // 男
    const USER_SEX_F = 'F'; // 女
    const USER_SEX_U = 'U'; // 未知
    /** 用户优惠券状态*/
    const USER_COUPON_STATUS_DISABLE = '0'; // 不可用
    const USER_COUPON_STATUS_ACTIVE = '1'; // 可使用
    const USER_COUPON_STATUS_USED = '2'; // 已被使用
    const USER_COUPON_STATUS_FROZEN = '3'; // 冻结状态
    const USER_COUPON_STATUS_EXPIRED = '4'; // 已过期
    const USER_COUPON_STATUS_TOBE_ACTIVE = '5'; // 待使用，未到使用时间
    /** 用户状态*/
    const USER_STATUS_DISABLE = '0'; // 禁用
    const USER_STATUS_ACTIVE = '1'; // 启用
    const USER_STATUS_NOT_REG = '2'; // 未注册
    /** 不采用商户的理由记录表的状态*/
    const PSNAL_STATUS_UNREAD = '0'; // 未阅
    const PSNAL_STATUS_READ = '1'; // 已阅
    /** 支付通道 */
    const PAY_CHANEL_NULL = '0';
    const PAY_CHANEL_QR_CODE = '1';
    /** 用户参与活动的验证码的状态 */
    const ACT_CODE_STATUS_UNPAY = '0'; // 未付款
    const ACT_CODE_STATUS_REQUEST_REFUND = '1'; // 申请退款
    const ACT_CODE_STATUS_REFUND = '2'; // 已退款
    const ACT_CODE_STATUS_USED = '3'; // 已验证
    const ACT_CODE_STATUS_ACTIVE = '4'; // 未验证
    /** 品牌链接类型*/
    const BRAND_LINK_TYPE_H5 = '0'; // H5页面
    const BRAND_LINK_TYPE_SHOP_LIST = '1'; // 商家列表
    const BRAND_LINK_TYPE_COUPON_LIST = '2'; // 优惠券列表
    const BRAND_LINK_TYPE_ICBC_DISCOUNT = '3'; // 工银特惠
    /** 商家开班列表每页的数据量*/
    const SHOP_CLASS_PAGE_SIZE = 3;
    /** 商家教师列表每页的数据量*/
    const SHOP_TEACHER_PAGE_SIZE = 5;
    /** 商户行业*/
    const INDUSTRY_CATERING = '1'; // 餐饮类
    const INDUSTRY_EDUCATION = '2'; // 教育类
}

abstract class IndexModule{  // C 端首页模块值
    const DISTRICT = -2; // 区
    const DISTANCE = -1; // 距离
    const INITIAL = 0; // 距离
    const SCROLLING = 1; // 滚屏
    const SHOP_TYPE = 2; // 商家分类
    const SHOP_TRADING_AREA = 3; // 商圈
    const BRAND = 4; // 品牌
    const ACTIVITY = 5; // 活动
    const SHOP_LIST = 6; // 商家列表
    const FUNCTION_AREA = 7; // 功能区
    const HOME_TAB = 8; // 选项卡
}

abstract class SearchShopOrder{ // 搜索商家的排序值
    const DISTANCE = 1; // 距离（由近及远）
    const POPULARITY = 2; // 人气（由高到低）
}

abstract class SearchShopFilter{ // 搜索商家的筛选值
    const BANK_DISCOUNT = 3; // 工行折扣
    const COUPON = 4; // 优惠活动
    const ACTIVITY = 5; // 活动商户
}

return array (
    // 下订单渠道
    'ORDER_WAY' => array(
        'WX' => '1', // 微信
    ),

    // 活动归属的位置信息
    'ACT_POS' => array(
        'NO' => '0', // 没有归属位置
        'SCROLL' => '1', // 滚屏位置
        'CB' => '3', // 中央红包的位置
    ),

    // 活动归属的位置的值对应的中文
    'ACT_POS_VALUE' => array(
        '0' => '无',
        '1' => '滚屏位置',
        '3' => '红包活动区'
    ),

    // 惠圈员工类别数组
    'HQ_STAFF_TYPE_ARRAY' => array(
        'ADMIN' => array('value' => '1', 'zh' => '管理员'), // 管理员
        'GROUND_PERSON' => array('value' => '10', 'zh' => '地面推广人员'), // 地推人员
    ),

    // 申请退款受理结果
    'REFUND_APPLY_RET' => array(
        'REJECT'  => '3', // 拒绝
        'AGREE'   => '2', // 同意
        'NO_DEAL' => '1', // 未处理
    ),

    // 点餐支付类型
    'EAT_PAY_TYPE' => array(
        'BEFORE' => '1', // 餐前支付
        'AFTER'  => '2', // 餐后支付
    ),

    // 美食订单状态
    'FOOD_ORDER_STATUS' => array(
        'ORDERED'   => '20', // 已下单
        'RECEIVED'  => '21', // 已接单
        'DELIVERED' => '22', // 已派送
        'SERVED'    => '23', // 已送达
        'CANCELED'  => '24', // 已撤销
        'UNORDERED' => '25', // 待下单
    ),

    // 订单类型
    'ORDER_TYPE' => array(
        'OTHER'       => '10', // 其他订单
        'NO_TAKE_OUT' => '20', // 堂食订单
        'TAKE_OUT'    => '21', // 外卖订单
    ),

    // 产品状态
    'PRODUCT_STATUS' => array(
        'ON_SHELF'  => '1', // 上架
        'OFF_SHELF' => '2', // 下架
        'SOLD_OUT'  => '3', // 售罄
    ),

    // 商品/服务的类型
    'PRODUCT_TYPE' => array(
        'SINGLE'  => '1', // 单品
        'PACKAGE' => '2', // 套餐
    ),

    //模块标题
    'TITLE' => array(
        'ROLLING_SCREEN'    => '1', //滚屏
        'CLASSIFY'          => '2', //分类
        'DISTRICT'          => '3', //商圈
        'BRAND'             => '4', //品牌
        'ACTIVITY'          => '5', //活动
        'SHOP_LIST'         => '6', //商户列表
    ),

    // 平台会员等级定义
    'membershipLevel' => array(
        array(
            'name' => '普通平台会员', // 等级名字
            'pointRequired' => '0', // 达到该等级积分要求
        ),
        array(
            'name' => '第一级',
            'pointRequired' => '1000',
        ),
        array(
            'name' => '第二级',
            'pointRequired' => '5000',
        ),
        array(
            'name' => '第三级',
            'pointRequired' => '10000',
        ),
        array(
            'name' => '第四级',
            'pointRequired' => '20000',
        ),
        array(
            'name' => '第五级',
            'pointRequired' => '50000',
        ),
    ),

    // Jpush相关参数
    'SHOP_APP_KEY' => 'f25791f60bbb4c5f3676b3fe',
    'SHOP_MASTER_SECRET' => '85d76db160a78a40a6bbf7e6',
    'CLIENT_APP_KEY' => '3be895fd29dd0835857cf35b',
    'CLIENT_MASTER_SECRET' => 'd78480d9f70b8dbe6ad50f7b',

    /**发送验证码的动作*/
    'ACTION' => array(
        'REGISTER' => 'r', // 注册
        'FIND_PWD' => 'f', // 找回密码
        'ACTIVATE' => 'a', // 商家端激活
        'SET_PAY_PWD' => 'spp', // 用户设置支付密码
    ),

    /**百度地图ak*/
    'BAIDU_MAP_AK' => 'mG5hg7F9fN7u8gZWTLGNlBRg',

    /**优惠券默认图片*/
    'COUPON_DEFAULT_IMG' => '/Public/img/couponDefault/{{couponType}}.png',

    /**商家与顾客交流的发消息一方*/
    'COMMUNICATION_APP' => array(
        'SHOP' => 0, // 商家端
        'USER' => 1, // 顾客端
    ),

    /**活动类型*/
    'ACTIVITY_TYPE' => array(
        'PARTY' => 1, // 聚会
        'SPORT' => 2, // 运动
        'OUTSIDE' => 3, // 户外
        'PARENT' => 4, // 亲子
        'EXPERIENCE' => 5, // 体验课
        'MUSIC' => 6, // 音乐
        'OTHER' => 7, //其他
    ),
    'ACTIVITY_TYPE_NAME' => array(
        'PARTY' => '聚会', // 聚会
        'SPORT' => '运动', // 运动
        'OUTSIDE' => '户外', // 户外
        'PARENT' => '亲子', // 亲子
        'EXPERIENCE' => '体验课', // 体验课
        'MUSIC' => '音乐', // 音乐
        'OTHER' => '其他', //其他
    ),

    /**活动退款要求*/
    'ACTIVITY_REFUND_REQUIRED_VALUE' => array(
        'CASUAL' => 1, // 随意退、过期退
        'THE_DAY' => 2, // 当天不退
        'ONE_DAY' => 3, // 24小时内不退
        'TWO_DAY' => 4, // 48小时内不退
    ),

    /**活动退款要求*/
    'ACTIVITY_REFUND_REQUIRED_NAME' => array(
        'CASUAL' => '随意退、过期退',
        'THE_DAY' => '当天不退',
        'ONE_DAY' => '24小时内不退',
        'TWO_DAY' => '48小时内不退',
    ),

    // 活动归属
    'ACTIVITY_BELONGING' => array(
        'PLAT' => 1, // 平台活动
        'BANK' => 2, // 工行活动
        'SHOP' => 3, // 商家活动
    ),

    /**活动状态*/
    'ACTIVITY_STATUS' => array(
        'DELETE'   => -1, //删除
        'DISABLE'  => 0, // 禁用 or 未发布
        'ACTIVE'   => 1, //启用 or 已发布
        'STOP_TO_JOIN'  => 2, //停止报名
        'CANCEL'   => 3, //取消
        'EXPIRED'  => 4, //活动已结束
        'FULL'     => 5, //活动已满员
    ),

    /**操作日志类型*/
    'ACTION_TYPE' => array(
        'LOGIN' => '登录',
    ),

    /**红包归属*/
    'BONUS_BELONGING' => array(
        'SHOP' => 1, // 商家红包
        'PLAT' => 2, // 平台红包
    ),

    /**红包状态*/
    'BONUS_STATUS' => array(
        'DISABLE'  => 0, //禁用
        'ACTIVE'   => 1, //启用
        'USED'     => 2, //已被使用
    ),

    /**用户银行卡状态*/
    'BANKACCOUNT_STATUS' => array(
        'DISABLE'    => 0, //禁用
        'NO_SIGN'    => 1, //未签订协议
        'SIGNED'     => 2, //已签订协议
        'TERMINATE'  => 3, //已解除协议
        'CANCELLED'  => 4, //已取消签订协议
    ),

    /**用户银行卡证件类型*/
    'BANKACCOUNT_ID_TYPE' => array(
        'SHENFEN' => 0, // 身份证
        'HUZHAO' => 1, // 护照
        'JUNGUAN' => 2, // 军官证
        'SHIBING' => 3, // 士兵证
        'GANGAO' => 4, // 港澳通行证
        'LINSHISHENFEN' => 5, // 临时身份证
        'HUKOU' => 6, // 户口簿
        'OTHER' => 7, // 其他
        'JINGGUAN' => 9, // 警官证
        'WAIGUOREN' => 12, // 外国人居留证
    ),

    /**性别*/
    'SEX' => array(
        'MALE'     => 'M', //男
        'FEMALE'   => 'F', //女
        'UNKNOWN'  => 'U', //不确定
    ),

    /**角色类型*/
    //TODO
    'ROLE_TYPE' => array(

    ),

    /**商家店员 等级*/
    'STAFF_LVL' => array(
        'BIG_MANAGER' => 3, // 大店长
        'MANAGER'     => 2, // 店长
        'EMPLOYEE'    => 1, // 员工
    ),

    /**staff状态*/
    'STAFF_STATUS' => array(
        'DISABLE'  => 0, //禁用
        'ACTIVE'  => 1, //启用
    ),

    /**user 等级*/
    //TODO
    'USER_LVL' => array(

    ),

    /**user状态*/
    'USER_STATUS' => array(
        'DISABLE' => 0, //禁用
        'ACTIVE'  => 1, //启用
        'NOT_REG' => 2, //未注册
    ),

    /**背景图类型*/
    'BACKGROUND_TYPE' => array(
        'COUPON'  => 1, //优惠券背景
        'CARD'    => 2, //卡背景
        'BONUS'   => 3, //红包背景
    ),

    /**优惠券来源**/
    'COUPON_BELONG' => array(
        'SHOP' => 1, //商户优惠券
        'PLATFORM' => 2, //平台优惠券
    ),

    /**优惠券类型*/
    'COUPON_TYPE' => array(
        'N_PURCHASE'           => 1, // N元购
        'REDUCTION'            => 3, // 抵扣券
        'DISCOUNT'             => 4, // 折扣券
        'PHYSICAL'             => 5, // 实物券
        'EXPERIENCE'           => 6, // 体验券
        'EXCHANGE'             => 7, // 兑换券
        'VOUCHER'              => 8, // 代金券
        'NEW_CLIENT_REDUCTION' => 32, // 送新用户的抵扣券
        'SEND_INVITER'         => 33, // 送邀请人的抵扣券
    ),

    /**商家端正常类型的优惠券*/
    'SHOP_NORMAL_COUPON' => array(
        'N_PURCHASE'           => 1, // N元购
        'REDUCTION'            => 3, // 抵扣券
        'DISCOUNT'             => 4, // 折扣券
        'PHYSICAL'             => 5, // 实物券
        'EXPERIENCE'           => 6, // 体验券
        'EXCHANGE'             => 7, // 兑换券
        'VOUCHER'              => 8, // 代金券
    ),

    'COUPON_BG' => array(
        'N_PURCHASE'           => '/Public/img/couponDefault/n_purchase.png', // N元购
        'REDUCTION'            => '/Public/img/couponDefault/reduction.png', // 抵扣券
        'DISCOUNT'             => '/Public/img/couponDefault/discount.png', // 折扣券
        'PHYSICAL'             => '/Public/img/couponDefault/physical.png', // 实物券
        'EXPERIENCE'           => '/Public/img/couponDefault/experience.png', // 体验券
        'EXCHANGE'             => '/Public/img/couponDefault/exchange.png', // 兑换券
        'VOUCHER'              => '/Public/img/couponDefault/voucher.png', // 代金券
    ),

    /**红包类型*/
    'BONUS_TYPE' => array(
        'GRAB'   => 1, //供用户抢的红包
        'REWARD' => 2, //奖励邀请人的红包
    ),

    /**优惠券类型名字*/
    'COUPON_TYPE_NAME' => array(
        'N_PURCHASE'           => 'N元购',
        'REDUCTION'            => '抵扣券',
        'DISCOUNT'             => '折扣券',
        'PHYSICAL'             => '实物券',
        'EXPERIENCE'           => '体验券',
        'EXCHANGE'             => '兑换券',
        'VOUCHER'              => '代金券',
        'NEW_CLIENT_REDUCTION' => '送新用户的抵扣券',
        'SEND_INVITER'         => '送邀请人的抵扣券',
    ),

    /**优惠券交易状态*/
    'COUPON_SALE_STATUS' => array(
        'SUCCESS'  => 1, //交易成功
        'WAITING'  => 0, //未交易
        'FAIL'     => -1, //交易失败
    ),

    /**优惠券分享级别*/
    'COUPON_SHARED_LVL' => array(
        'ALL'     => 0, //所有人可见
        'FRIEND'  => 1, //朋友可见
        'OTHER'   => 2, //其他人不可见
    ),

    /**优惠券是否在客户端显示*/

    /**用户优惠券状态*/
    'USER_COUPON_STATUS' => array(
        'DISABLE'     => 0, //不可用
        'ACTIVE'      => 1, //可使用
        'USED'        => 2, //已被使用
        'FROZEN'      => 3, //冻结状态
        'EXPIRED'     => 4, //已过期
        'TOBE_ACTIVE' => 5, //待使用，未到使用时间
    ),


    /**卡类型*/
    'CARD_TYPE' => array(
        'VIP'  => 1000, //会员卡
        'SV'   => 2000, //储值卡
    ),

    /**卡等级*/
    'CARD_LVL' => array(
        'WORST'  => 1,
        'NORMAL' => 2,
        'BEST'   => 3,
    ),

    /**卡状态*/
    'CARD_STATUS' => array(
        'DISABLE'  => 0, //禁用
        'ACTIVE'   => 1, //启用
    ),


    /**消息类型*/
    'MESSAGE_TYPE' => array(
        'SHOP'    => 0, //商家消息
        'CARD'    => 1, //会员卡消息
        'COUPON'  => 2, //优惠券消息
    ),

    /**消息状态*/
    //TODO
    'MESSAGE_STATUS' => array(

    ),

    /**消息阅读状态*/
    'MESSAGE_READING_TYPE' => array(
        'UNREAD'  => 0, //未读
        'READ'    => 1, //已读
    ),

    /**商家装饰类型*/
    'SHOP_DECORATION_TYPE' => array(
        'SHOP' => '0', // 商家背景图片
        'PRE_SHOP' => '1', // 预采用商家的
    ),

    /**商家状态*/
    'SHOP_STATUS' => array(
        'DISABLE'  => 0, //禁用
        'ACTIVE'   => 1, //启用
    ),


    /**反馈状态*/
    //TODO
    'FEEDBACK_STATUS' => array(
        'UNREAD' => 0, //未读
        'READ'   => 1, //已读
    ),

    /**登录类型*/
    'LOGIN_TYPE' => array(
        'SHOP'  => 0, //商家端
        'USER'  => 1, //顾客端
        'ADMIN' => 2, //管理端
        'PC'    => 3, //PC端
    ),

    /**会员卡的会员排序类型*/
    'VIP_ORDER_TYPE' => array(
        'APPLY_TIME'     => 1,
        'ACTION_TIME'    => 2,
        'CONSUME_AMOUNT' => 3,
        'CONSUME_TIMES'  => 4
    ),

    /**用户红包状态*/
    'USER_BONUS_STATUS' => array(
        'VALID'   => 1,
        'USED'    => 2,
        'INVALID' => 3,
    ),

    /**POS服务类型*/
    'POS_SERVER_TYPE' => array(
        'APPLY_POS'     => 1, // 申请工行POS
        'SEND_MATERIAL' => 2, // 耗材配送
        'REPAIR'        => 3, // 故障报修
        'OTHER'         => 4, // 其他
    ),

    /**POS服务是否已处理*/
    'POS_SERVER_IS_DEAL' => array(
        'YES' => 1, // 已处理
        'NO'  => 0, // 未处理
    ),

    /**用户银行卡消费记录的消费状态*/
    'BALL_STATUS' => array(
        'CAN_NOT_PAY' => 0, // 不能支付
        'UNPAYED'   => 1, // 未付款
        'PAYING'    => 2, // 付款中
        'PAYED'     => 3, // 已付款
        'CANCELED'  => 4, // 已取消付款
        'FAIL'      => 5, // 付款失败
        'REFUNDING' => 6, // 退款受理中
        'REFUNDED'  => 7, // 已退款
    ),

    /**用户支付状态*/
    'UC_STATUS' => array(
        'CAN_NOT_PAY' => 0, // 不能支付
        'UNPAYED'   => 1, // 未付款
        'PAYING'    => 2, // 付款中
        'PAYED'     => 3, // 已付款
        'CANCELED'  => 4, // 已取消付款
        'FAIL'      => 5, // 付款失败
        'REFUNDING' => 6, // 退款受理中
        'REFUNDED'  => 7, // 已退款
    ),

    /**用户支付方式*/
    'UC_PAY_TYPE' => array(
        'BANKCARD' => 1, // 线上银行卡支付
        'POS'      => 2, // POS机刷卡支付
        'CASH'     => 3, // 现金支付
        'NULL'     => 4, // 未选择支付方式
        'COUPON'   => 5, // 实物券 or 体验券
    ),

    /**订单支付状态*/
    'ORDER_STATUS' => array(
        'CAN_NOT_PAY' => 0, // 不能支付
        'UNPAYED'   => 1, // 未付款
        'PAYING'    => 2, // 付款中
        'PAYED'     => 3, // 已付款
        'CANCELED'  => 4, // 已取消订单
        'FAIL'      => 5, // 付款失败
        'REFUNDING' => 6, // 退款受理中
        'REFUNDED'  => 7, // 已退款
    ),

    /** 丽萍类型 */
    'PRIZE_TYPE' => array(
        'COUPON' => 1, // 优惠券
    ),

    /**图片上传默认参数*/
    'IMG_UP_PARAMS' => array(
        'maxSize'  => 1000000,
        'savePath' => './Public/upload/',
        'saveName' => 'saveName',
        'exts'     => array('jpg', 'gif', 'png', 'jpeg'),
        'autoSub'  => true,
        'subName'  => array('date','Ymd')
    ),
    /**分化元，元化分*/
    'RATIO' => 100,
    'DISCOUNT_RATIO' => 10,
    'THOUSAND' => 1000,
    'HUNDRED' => 100,
    'TEN' => 10,

    /**经纬度距离比例*/
    'PROPORTION' => 111700,

    /**是或否*/
    'YES' => 1,
    'NO'  => 0,

    // 惠圈平台编码
    'HQ_CODE' => '00000000-0000-0000-0000-000000000000',
    // 工行编码
    'ICBC_CODE' => '3a1442dc-07ff-11e5-ac45-00163e021731',

    /**app类型*/
    'APP_TYPE' => array(
        'SHOP'   => 's', // 商家端
        'CLIENT' => 'c', // 顾客端
    ),

    'PUSH_MESSAGE' => array(
        'JOIN_ACT' => '{{userName}}报名参加{{shopName}}商家的{{activityName}}活动。huiquan.club',
        'EXIT_ACT' => '{{userName}}退出参加{{shopName}}商家的{{activityName}}活动。huiquan.club',
        'LOGIN_REPEAT' => '该账号已在其他设备上登录，已强制退出！您也可以重新登陆。如非本人操作，请修改密码！',
        'ACT_UPDATE' => '{{shopName}}商家的{{activityName}}活动{{status}}。huiquan.club',
        'INVITE_SHOP' => '温馨提示：{{userName}}邀请您入驻惠圈开店啦！(当前邀请您入驻惠圈的人数已达{{userCount}}人)。',
        'REMIND_SHOP' => '温馨提示：{{userName}}邀请您添加商品展示，提前预览店铺的服务内容！(当前邀请您添加商品展示的人数已达{{userCount}}人)。',
        'COUPON_TO_BE_EXPIRED' => '您有{{userCount}}张{{shopName}}的{{couponType}}在3天内到期。huiquan.club',
        'PAY_COUPON_USE' => '您成功使用了{{userCount}}张{{shopName}}的{{couponType}}。huiquan.club',
        'PAY_COUPON_REFUND' => '您有{{userCount}}张{{shopName}}的{{couponType}}已过期，购买款项已原路退回。huiquan.club',
        'ORDER_COUPON_REFUND' => '您有{{userCount}}张{{shopName}}的{{couponType}}已退款，购买款项将于24小时内原路退回。huiquan.club',
    ),

    'PUSH_ACTION' => array(
        'LOGIN' => 'LOGIN', //登陆
        'CONSUME' => 'CONSUME', //买单
        'JOIN_ACTIVITY' => 'JOIN_ACTIVITY', //参加活动
        'EXIT_ACTIVITY' => 'EXIT_ACTIVITY', //退出活动
        'ACT_UPDATE' => 'ACT_UPDATE', //活动内容修改
        'INVITE_SHOP' => 'INVITE_SHOP', //邀请商家入驻
        'REMIND_SHOP' => 'REMIND_SHOP', //提醒商家展示商品
        'COUPON_TO_BE_EXPIRED' => 'COUPON_TO_BE_EXPIRED', //买的券3天过期
        'PAY_COUPON_USE' => 'PAY_COUPON_USE', //买的券的使用
        'PAY_COUPON_REFUND' => 'PAY_COUPON_REFUND', //买的券退掉
    ),

    'SEND_MESSAGE' => array(
        'REGISTER' => '验证码：{{validateCode}}，欢迎您注册惠圈会员，如非本人注册，请忽略本短信。本验证码在15分钟之内有效，请勿泄露。huiquan.club',
        'FIND_PWD' => '验证码：{{validateCode}}，如非本人操作，请忽略此短信。本验证码在15分钟之内有效，请勿泄露。huiquan.club',
        'SET_PAY_PWD' => '验证码：{{validateCode}}。本验证码在15分钟之内有效，请勿泄露。huiquan.club',
        'USE_COUPON' => '您成功使用{{shopName}}商家的{{couponName}}券，订单编号(后5位)：{{orderNbr}}。',
        'SEND_COUPON' => '恭喜您获得{{shopName}}的{{couponType}}({{function}}:{{userCouponNbr}})，可登录惠圈APP查看，请尽快使用。huiquan.club',
        'SEND_COUPON_MORE_ONE' => '恭喜您获得{{shopName}}{{batchNbr}}批次的{{couponType}}({{function}}){{sendCount}}张，可登录惠圈APP查看，请尽快使用。huiquan.club',
        'C_PAYED' => '您成功支付{{realPay}}元，订单号(后5位)：{{orderNbr}}。huiquan.club',
        'S_PAYED' => '您收到{{userName}}对订单号(后5位)为{{orderNbr}}支付的{{realPay}}元，实际消费{{orderAmount}}元，优惠{{deduction}}元。huiquan.club',
        'PAY_COUPON' => '您在惠圈购买了{{shopName}}的{{userCount}}张{{function}}{{insteadPrice}}{{couponType}}，兑换码为{{couponCodeString}}，请您在{{expireTime}}之前使用。huiquan.club',
        'ADD_MR' => '验证码：{{validateCode}}，{{shopName}}设置订单短信接收号码。huiquan.club'

    ),

    // 商家广播
    'SHOP_BROADCASTING' => array(
        // 发优惠券
        'ISSUE_COUPON' => array(
            'TITLE'   => '发券',
            'CONTENT' => '亲们，本店新一批优惠券大派送，快来领券咯'
        ),
        // 新活动
        'NEW_ACT'      => array(
            'TITLE'   => '活动',
            'CONTENT' => '亲们，本店有新活动咯，欢迎大家参与'
        ),
        // 发红包
        'ISSUE_BONUS'  => array(
            'TITLE'   => '红包',
            'CONTENT' => '亲们，本店大派红包，快来领取福利'
        ),
        // 产品上新
        'NEW_PRODUCT'  => array(
            'TITLE'   => '上新',
            'CONTENT' => '亲们，本店有新品推出，快来体验'
        ),
    ),
    'IS_SEND_NEW_CLIENT_COUPON' => '0', // 是否送新注册用户优惠券，0表示不送，1表示送
    // 工行快捷支付错误编码
    'ICBC_ERROR_CODE_MSG' => array(
        '00000' => '交易成功',
        '00001' => '部分扣款成功',
        'FFFFF' => '中间业务平台初始错误',
        'B9999' => '系统错（通用错误，如62）',
        'B9998' => '系统错误以错误提示为准（如SS）',
        'B9997' => '主机疑账，请调用8624交易进行处理',
        'B0001' => '通讯报文错',
        'B0002' => '系统错',
        'B0003' => '交易不可用',
        'B0004' => '系统已关闭',
        'B0005' => '交易渠道未开放',
        'B0006' => '非工作时间',
        'B0007' => '交易网点未开通此业务',
        'B0008' => '系统参数设置错误，请与系统管理员联系',
        'B0009' => '业务参数设置错误，请与系统管理员联系',
        'B0010' => '该模式系统暂时不支持',
        'B0011' => '本业务不能执行本交易',
        'B0012' => '公司方暂停受理本业务',
        'B0013' => '该业务禁止公司方发起此交易',
        'B0014' => '该业务禁止从柜面发起此交易',
        'B0015' => '与公司方通信失败',
        'B0016' => '与主机通信失败',
        'B0017' => '与公司方通讯异常产生疑帐，后台自动补做，请稍后查询交易结果!',
        'B0018' => '与公司方通讯异常产生疑帐，后台自动冲正，请稍后查询交易结果!',
        'B0019' => '与主机通讯异常产生疑帐，后台自动补做，请稍后查询交易结果!',
        'B0020' => '与主机通讯异常产生疑帐，后台自动冲正，请稍后查询交易结果!',
        'B0021' => '发送文件失败',
        'B0022' => '接收通信包不完整',
        'B0023' => '接收文件出错',
        'B0024' => '该业务已暂停',
        'B0025' => '此地区未开通该业务',
        'B0026' => '该柜员有主机疑账，需先用8624交易处理',
        'B0027' => '文件打开出错',
        'B0028' => '数据库操作错误',
        'B0029' => '与公司方通讯异常，后台自动补发，请稍后查询交易结果!',
        'B0030' => '与公司方通讯异常，交易失败，需做疑帐处理（8624）交易',
        'B0031' => '主机疑帐，交易失败，需做疑帐处理（8624）交易',
        'B0032' => '该用户有疑账，暂无法交易',
        'B0033' => '原交易已成功',
        'B0034' => '公司方疑帐未自动处理完,请稍后做主机疑帐处理(8624)交易',
        'B0035' => '柜面交易柜员号为空，请重做交易或重新签到',
        'B0036' => '柜面交易地区号为空，请重做交易或重新签到',
        'B0037' => '柜面交易网点号为空，请重做交易或重新签到',
        'B0038' => '该业务已日终, 请稍后或次日重试',
        'B0039' => '业务未日初, 请稍后重试',
        'B0040' => '业务状态非正常',
        'B0041' => '柜员信息异常, 请重做交易或重新签到',
        'B0101' => '通讯错,联接后台服务器失败',
        'B0102' => '通讯错,数据接收失败',
        'B0103' => '通讯错,数据发送失败',
        'B0104' => '通讯错,文件接收失败',
        'B0105' => '通讯错,文件发送失败',
        'B0106' => '通讯MAC校验错',
        'B0110' => '通讯包文解析错，取信息要素失败',
        'B0111' => '通讯包文解析错，添加信息要素失败',
        'B0112' => '通讯包文解析错，更新信息要素失败',
        'B0121' => '系统错,打开存在文件错误',
        'B0122' => '系统错,创建文件错误',
        'B0123' => '系统错,读入文件时错误',
        'B0124' => '系统错,签名信息非法',
        'B0140' => '数据库错,查询失败',
        'B0141' => '数据库错,更新失败',
        'B0142' => '数据库错,新增失败',
        'B0143' => '数据库错,删除失败',
        'B0144' => '数据库连接错误',
        'B0201' => '帐号类型错',
        'B0202' => '非法帐号',
        'B0203' => '送主机交易要素中帐号开户地区非法',
        'B0204' => '帐户非结算帐户',
        'B0205' => '帐户状态不正常',
        'B0206' => '帐户不存在',
        'B0207' => '帐户未启用',
        'B0208' => '帐户已注销',
        'B0209' => '帐户已挂失',
        'B0210' => '帐户已冻结',
        'B0211' => '帐户为欠息户',
        'B0212' => '帐户为协定存款户',
        'B0213' => '帐户为止付卡',
        'B0214' => '卡过期',
        'B0215' => '卡已被收回',
        'B0216' => '贷记卡不支持透支转帐',
        'B0220' => '帐户不能通兑',
        'B0221' => '该卡为被伪冒卡',
        'B0222' => '账户为储蓄账户,不允许建档',
        'B0223' => '账户为非实名户,不允许建档',
        'B0240' => '原交易已冲正',
        'B0241' => '没有原交易',
        'B0242' => '没有交易明细',
        'B0243' => '没有历史明细',
        'B0244' => '只能在当天做冲正交易',
        'B0245' => '冲正金额与原交易金额不符',
        'B0246' => '原转帐交易不存在(仅查询交易流水的情况)',
        'B0247' => '打印次数超过允许最大打印次数',
        'B0248' => '支票号码错',
        'B0249' => '该订单号已失效，请重新提交', // '同公司流水号交易正在处理',
        'B0250' => '该凭证已支付',
        'B0251' => '该凭证正在支付',
        'B0280' => '余额不足',
        'B0281' => '金额为零',
        'B0282' => '金额为空',
        'B1001' => '用户状态不正常',
        'B1002' => '不支持的缴费业务类型',
        'B1003' => '解密文件失败',
        'B1004' => '该业务不支持加密文件上传',
        'B1005' => '后台业务参数未配置',
        'B1006' => '该协议不支持此种传输方式',
        'B1007' => '该协议不存在',
        'B1008' => '该协议已开通代理业务文件传输功能',
        'B1009' => '该协议未开通代理业务文件传输功能',
        'B1010' => '该协议设置为无证书，不能开通代理业务文件传输功能',
        'B1011' => '该协议不允许开通代理业务文件传输功能',
        'B1012' => '本行客户，主机客户号核对不通过',
        'B1013' => '该协议已终止',
        'B1014' => '该协议的内部清算过渡户未设置',
        'B1015' => '文件中的笔数/金额与实际笔数/金额不一致',
        'B1016' => '该批次不允许撤销',
        'B1017' => '指令已提交,请稍后查询结果',
        'B1018' => '上送金额与实际退款金额不一致',
        'B1019' => '非工行同城,暂不支持',
        'B1020' => '上送帐号与实际登记帐号不一致',
        'B2001' => '无此用户',
        'B2002' => '无效的用户号码',
        'B2003' => '该业务必须指定出帐日期扣款',
        'B2004' => '帐户余额不能还清该客户所有帐期的欠费',
        'B2005' => '没有公司方流水',
        'B2006' => '找不到指定的欠费记录',
        'B2007' => '已经部分扣款的用户不能做累加实时扣款',
        'B2008' => '出帐日期不符',
        'B2009' => '该笔欠费不允许扣款',
        'B2010' => '该帐户没有找到委托',
        'B2011' => '公司流水号不正确',
        'B2012' => '该客户欠费尚未还清',
        'B2013' => '卡号错误或卡类型不符',
        'B2014' => '用户号与帐号不符',
        'B2015' => '密码长度不够',
        'B2016' => '缴费金额至少10元',
        'B2017' => '指定帐期中,尚有欠费存在,发票不能打印',
        'B2018' => '找不到发票所对应的扣款记录',
        'B2019' => '找不到指定的发票或收据',
        'B2020' => '柜员未设置代理业务授权密码',
        'B2021' => '金额不符',
        'B2022' => '尚未办理委托',
        'B2023' => '该用户不在本行缴费',
        'B2024' => '扣款帐号与委托账号不一致',
        'B2025' => '没有该缴费记录',
        'B2026' => '无原交易流水',
        'B2027' => '没有找到原交易',
        'B2028' => '已办理委托',
        'B2029' => '不允许对多笔欠费合计进行部分缴款',
        'B2030' => '账号不正确',
        'B2031' => '证件号不正确',
        'B2032' => '客户帐号非本地帐号',
        'B2033' => '本业务不允许部分缴费',
        'B2034' => '有滞纳金不允许部分缴费',
        'B2035' => '缴费金额与单价不匹配',
        'B2036' => '需按欠费时间顺序缴款',
        'B2037' => '公司方处理不成功',
        'B2038' => 'BICE系统错',
        'B2039' => '原帐户正在进行缴费处理',
        'B2040' => '该用户委托已销户',
        'B2041' => '该业务无此币种',
        'B2042' => '欠费已缴',
        'B2043' => '核对户名不相符',
        'B2044' => '欠费数据错',
        'B2045' => '与参数值不匹配',
        'B2046' => '记录已存在',
        'B2047' => '费种不符',
        'B2048' => '日期格式错（应该为14位：yyyymmddhhmmss）',
        'B2049' => '用户编号（回单号）长度不对（应该为18位）',
        'B2050' => '输入的用户错',
        'B2051' => '年份不合规范',
        'B2052' => '有多条欠费记录',
        'B2053' => '已过缴费截止日',
        'B2054' => '公司方无我行委托',
        'B2055' => '行号必须是12位',
        'B2056' => '公司方已存在我行委托',
        'B2057' => '没有该用户的余额信息',
        'B2058' => '没有该用户的明细信息',
        'B2059' => '未找到该生欠费信息,可通过8699交易查询学费业务帮助信息',
        'B2060' => '不允许使用外地卡号在本地做该类交易',
        'B2061' => '本业务必须全额处理',
        'B2062' => '无打印数据',
        'B2063' => '累计扣款超限额',
        'B2065' => '无退款原因',
        'B2066' => '清算日期不符',
        'B2067' => '制卡信息不存在',
        'B2100' => '无效交易码',
        'B2101' => '密码不符',
        'B2102' => '服务停止',
        'B2103' => '参数设置错误',
        'B2104' => '数据超出有效期',
        'B2105' => '业务停止',
        'B2106' => '该批次数据不存在',
        'B2107' => '批次数据记录异常',
        'B2108' => '该批次数据不允许退款',
        'B2109' => '该批次数据已存在',
        'B2110' => '选择的业务编号、子编号与文件不一致',
        'B2111' => '该批次数据不允许进行多余款入帐',
        'B2112' => '该批次数据不允许进行划款',
        'B2113' => '请调整机器时间为正确的时间',
        'B2114' => '批量开户复核不通过',
        'B2115' => '入库柜员或补录柜员和复核柜员不能为同一人',
        'B2116' => '日累计交易笔数超限',
        'B2200' => '财政托收未划拨',
        'B2201' => '财政托收划拨通讯异常',
        'B2202' => '财政托收划拨成功',
        'B2203' => '财政托收划拨失败',
        'B2300' => '特约商户编号不符',
        'B2301' => '日累计消费超限额',
        'B2302' => '浙江省外(含宁波)卡不支持',
        'B2303' => '手机号码与卡片预留手机不一致',
        'B2304' => '预留手机号不对，为了您的安全，请到工行网银或柜台处理', // '手机号码与卡片预留手机不一致',
        'B2320' => '无对应的驾档信息',
        'B2321' => '不支持非交通卡缴费',
        'B2322' => '该违法记录交警尚未入库, 请稍后再缴',
        'B2323' => '证件类型不符',
        'B2324' => '处理日期不能为空',
        'B2325' => '处理日期格式为YYYYMMDD',
        'B2326' => '处罚决定书类别不能为空',
        'B2327' => '处罚地行政区划码不能为空',
        'B2328' => '该笔缴费为离线，请输全缴费要素',
        'B2329' => '非本地区的处罚决定书',
        'B2330' => '处罚决定书编号有误, 请核对',
        'B2331' => '发票代码不能为空',
        'B2332' => '发票号码不能为空',
        'B2333' => '发票代码非法',
        'B2334' => '发票号码有误',
        'B2340' => '从交警系统获取驾档数据失败',
        'B2341' => '驾档信息入库失败',
        'B2342' => '驾档信息核查失败,证件类型不符',
        'B2343' => '驾档信息核查失败,户名不符',
        'B2344' => '驾档信息核查失败,性别不符',
        'B2345' => '已存在相同罚单的成功缴罚记录',
        'B2401' => '重复的流水号',
        'B2402' => '取公积金专户失败，请联系银行人员',
        'B2403' => '贷方账户与公积金指定账户不一致',
        'B2404' => '交易已存在',
        'B2410' => '未找到该收费站所属的柜员',
        'B2411' => '未找到相应的班次信息',
        'B2412' => '该收费站柜员该班次封包已经录入过，如重录请先做反交易',
        'B2413' => '封包日期不正确',
        'B2414' => '封包日期与当前系统日期相隔不能超过十天',
        'B2420' => '未使用指定电话号码缴款',
        'B2421' => '该用户当日累计金额已经超限',
        'B2430' => '未取到相应的省行标准汇率',
        'B2431' => '取套餐信息失败',
        'B2432' => '借贷方帐号为同城帐号',
        'B2433' => '未取到网点账户对照表信息',
        'B2434' => '根据CIS客户号取客户信息失败',
        'B2435' => '未办理该协议',
        'B2436' => '下载文件失败',
        'B2437' => '该帐号已签订结算伴侣',
        'B2438' => '结算伴侣必须使用借记卡',
        'B2439' => '未找到发起行行号',
        'B2440' => 'A套餐只能跨行同城汇款',
        'B2441' => 'B套餐只能跨行异地汇款',
        'B2442' => '协议已销户',
        'B2443' => '未取到相应的结算伴侣手续费汇率',
        'B2444' => '未找到网点对应的核算网点',
        'B2445' => '查询物理核算网点对照表失败',
        'B2446' => '收款人帐号非法',
        'B2447' => '收款人帐号暂时不支持对公户',
        'B2448' => '预约汇款金额不能超过5万元',
        'B2450' => '无汇划用户信息',
        'B2451' => '汇划用户信息冲突',
        'B2460' => '查询套餐信息失败或缺套餐信息',
        'B2461' => '收款人1户名不一致',
        'B2462' => '收款人2户名不一致',
        'B2463' => '收款人帐号状态正常，不允许变更',
        'B2464' => '该帐号已签该协议',
        'B2465' => '新帐号已签该协议',
        'B2466' => '预约汇款金额要少于5万元',
        'B2467' => '汇款人户名不符',
        'B2468' => '每月预约日期只能为1日至28日间',
        'B2469' => '不符合签约1年以上规定，不允许变更',
        'B2470' => '汇款金额不能为零',
        'B2471' => '收款人户名不能为空',
        'B2472' => '收款人帐号不能为空',
        'B2473' => '交易为疑账，请查看账户余额',
        'B2474' => '参数信息不全',
        'B2475' => '已签订协议',
        'B2476' => '收款人户名不符',
        'B2477' => '异地借记扣款成功，入账失败',
        'B2550' => '信用额度过低',
        'B2551' => '非浙江省信用卡',
        'B2560' => '该加油卡卡已注销',
        'B2561' => '该加油卡卡已挂失',
        'B2562' => '该加油卡卡为无效卡',
        'B2563' => '该加油卡卡号不存在',
        'B2564' => '公司方连接异常',
        'B2565' => '金额超过最大',
        'B2566' => '该加油卡为副卡不能进行充值',
        'B2567' => '该畅通卡已绑定一张加油卡',
        'B2580' => '质押资金冻结异常, 请稍候查询',
        'B2581' => '质押资金冻结失败',
        'B2582' => '交易日期不符',
        'B2583' => '质押资金已解冻',
        'B2584' => '质押资金解冻失败',
        'B2585' => '质押资金解冻异常, 请稍候查询',
        'B2586' => '该卡号质押笔数超限',
        'B2587' => '账户户名与原委托不符',
        'B2588' => '账户证件信息与原委托不符',
        'B2589' => '账户与原质押不符',
        'B2590' => '质押冻结尚未解冻, 不允许进行抵扣',
        'B2591' => '抵扣金额不能大于解冻总金额',
        'B2600' => '证件号码不符',
        'B2601' => '出生日期有误, 格式YYYY-MM-DD',
        'B2602' => '存在有效挂号未门诊信息',
        'B2603' => '存在门诊信息, 请延后处理',
        'B2604' => '医保类型未知',
        'B2605' => '该病历号已办理委托',
        'B2610' => '手机号核对次数超过上限',
        'B2611' => '验证码已失效',
        'B2612' => '已存在不同金额的同一订单号',
        'B2613' => '该订单已支付成功',
        'B2614' => '本业务不支持非签约客户',
        'B2615' => '验证码核对次数超过上限',
        'B2616' => '日累计支付金额超限',
        'B2617' => '月累计支付金额超限',
        'B2618' => '年累计支付金额超限',
        'B2619' => '支付金额超过最高累计限额',
        'B2620' => '未找到支付订单号',
        'B2621' => '验证码核对不通过',
        'B2622' => '无验证码消费累计金额已超限',
        'B2623' => '卡片有效期不符',
        'B2624' => '支付卡号与已签约卡号不符合',
        'B2625' => '客户户名与已签约户名不符合',
        'B2626' => '手机号码与已签约手机号码不符合',
        'B2627' => '证件号码与已签约证件号码不一致',
        'B2628' => '客户未在银行预留手机号',
        'B2629' => '积分兑换超过该业务上限',
        'B2630' => '卡片有效期不能为空',
        'B2631' => 'CVV2不能为空',
        'B2632' => '该客户是我行黑名单客户,拒绝交易',
        'B2633' => '该商户是我行黑名单商户,拒绝交易',
        'B2634' => '积分兑换额超过商品价格',
        'B2635' => '退货金额不能大于原交易金额',
        'B2636' => '超单笔交易限额',
        'B2637' => '月累计交易笔数超限',
        'B2638' => '上送卡号与原交易卡号不一致',
        'B2639' => '当日不允许退货',
        'B2640' => '短信回复超时',
        'B2641' => '协议未激活',
        'B2642' => '客户类型不符',
        'B2643' => '客户协议尚未激活',
        'B2644' => '客户协议状态异常',
        'B2645' => '客户类型错',
        'B2646' => '客户类型与原客户类型一致',
        'B2647' => '日累计支付笔数超限',
        'B2648' => '月累计支付笔数超限',
        'B2649' => '年累计支付笔数超限',
        'B2650' => '支付笔数超过最高累计笔数',
        'B2651' => '通讯错误',
        'B2652' => '短信格式错误',
        'B2653' => '已存在不同支付卡号的同一订单号',
        'B2654' => '已存在不同用户号的同一订单号',
        'B2655' => '该订单号已撤销',
        'B2656' => '该订单号已退货',
        'B2657' => '该订单号已存在',
        'B2658' => '该用户已存在不同卡号的协议',
        'B2659' => '客户无对应尾号的工行卡',
        'B2660' => '客户尚未持有工行卡',
        'B3001' => '发票终止号码小于起始号码或号码段超过1000',
        'B3002' => '该发票号码段中存在过期,或者不符合领用条件的发票',
        'B3003' => '发票终止号码小于起始号码',
        'B3004' => '该发票号码段中存在过期,或者不符合往出条件的发票',
        'B3005' => '发票终止号码小于起始号码或发票张数不符',
        'B3006' => '该发票号码段不存在或部分发票不存在',
        'B3007' => '发票终止号码小于起始号码或现发票张数与原发票张数不符',
        'B3008' => '该发票号码段中的发票非已使用或不存在',
        'B3009' => '该发票号码非已使用或不存在',
        'B3010' => '发票打印证明文件建立失败',
        'B3011' => '发票打印证明文件关闭失败',
        'B3012' => '发票终止号码小于起始号码',
        'B3013' => '该发票号码段不存在或部分发票不存在',
        'B3014' => '发票打印记录文件建立失败',
        'B3015' => '发票打印记录文件关闭失败',
        'B3016' => '柜员发票使用情况统计文件建立失败',
        'B3017' => '柜员发票使用情况统计文件关闭失败',
        'B3018' => '该发票号码不存在',
        'B3019' => '该发票号码未领用',
        'B3020' => '该发票号码已使用',
        'B3021' => '该发票号码已过期',
        'B3022' => '该发票号码已作废',
        'B3023' => '该发票号码状态未知',
        'B3024' => '该发票号码领用柜员和使用柜员不一致',
        '4102'  => '借方账户余额不足',
        '2251'  => '客户的证件号不符',
    ),

    'ICBC_PAY_SUCCESS' => '00000', // 在线支付成功
);
