<?php
return array(
	//'配置项'=>'配置值'
    'SUCCESS' => 50000,   // 成功

    // 用户修改个人信息相关错误
    'UPDATE_INFO' => array(
        'EMPTY'           => 50001,  // 请将信息填写完整
        'USER_ROLE_EMPTY' => 50002,  // 请输入用户角色类型
        'NO_UPDATE'       => 50003,  // 您没有修改任何信息
        'NOT_EXIST'       => 50008,  // 用户不存在
    ),

    // 其他相关错误
    'FOLLOW_SHOP_REPEAT' => 50004,	// 您已关注过该商家
    'MONEY_LOW'          => 50015,	// 余额不足
    'REGISTRATION_Id_EMPTY' => '50017', // registrationId不能为空

    // 银行卡相关
    'BANK_ACCOUNT' => array(
        'ACCOUNT_NAME_WRONG' => 50050, // 持卡人姓名不正确
        'ID_NBR_WRONG'       => 50051, // 证件号码不正确
        'ACCOUNT_NBR_WRONG'  => 50052, // 银行卡号不正确
        'REQ_BANK_API_ERROR' => 50053, // 请求工行获取验证短信API失败，可能是填写信息错误
        'ID_TYPE_WRONG'      => 50054, // 证件类型不正确
        'REPEAT'             => 50055, // 此银行卡已经签订了支付协议
        'CODE_EMPTY'         => 50056, // 银行账户编码为空
        'CODE_ERROR'         => 50057, // 银行账户编码错误
        'ORDER_NBR'          => 50058, // 订单编码错误
        'TERMINATED'         => 50059, // 银行卡已经解除协议了
        'OVER_LIMIT'         => '50060', // 银行卡归属的用户数量超限
    ),
    // 提交反馈相关
    'FEEDBACK' => array(
        'CONTENT_EMPTY' => 50100,  // 反馈内容为空
        'CREATOR_CODE_ERROR' => 50101, // 反馈者编码为空
    ),

    // 活动相关
    'ACTIVITY' => array(
        'REPEAT'                      => 50016, // 您已经报过名了
        'NAME_ERROR'                  => 50200, // 活动主题不正确
        'START_TIME_ERROR'            => 50201, // 活动开始时间不正确
        'END_TIME_ERROR'              => 50202, // 活动结束时间不正确
        'LOCATION_ERROR'              => 50203, // 活动地点不正确
        'TXT_CONTENT_ERROR'           => 50204, // 活动说明不正确
        'LIMITED_PARTICIPATORS_ERROR' => 50205, // 活动参与人数上限不正确
        'IS_PREPAY_REQUIRED_ERROR'    => 50206, // 是否需要预付费不正确
        'PREPAYMENT_ERROR'            => 50207, // 预付金额不正确
        'IS_REGISTER_REQUIRED_ERROR'  => 50208, // 是否需要报名不正确
        'ACTIVITY_IMG_ERROR'          => 50209, // 活动图片不正确
        'ACTIVITY_LOGO_ERROR'         => 50210, // 活动方形图片不正确
        'CREATOR_CODE_ERROR'          => 50212, // 活动发起人编码不正确
        'ACT_BELONGING_ERROR'         => 50213, // 活动归属不正确
        'START_GT_END'                => 50214, // 活动开始时间不能大于活动结束日期
        'REFUND_REQUIRED_ERROR'       => 50215, // 请选择退款要求
        'REGISTER_NBR_REQUIRED_ERROR' => 50216, // 请填写单人报名上限
        'CONTACT_NAME_ERROR'          => 50217, // 请填写联系人姓名
        'CONTACT_MOBILE_ERROR'        => 50218, // 请填写联系方式
        'ACT_TYPE_ERROR'              => 50219, // 请选择活动类型
        'ACT_NOT_EXIST'               => 50220, // 活动不存在
        'ACT_STATUS_ERROR'            => 50221, // 活动现在状态不可变更
        'ACT_EXPIRED'                 => 50222, // 活动已经过期
        'REG_NBR_REQUIRED_OVER'       => 50223, // 活动单人报名人数超限
        'LIMIT_NBR_OVER'              => 50224, // 活动报名人数超限
        'ACT_STATUS_SAME'             => 50225, // 活动状态没有变化
    ),

    // 商家信息相关
    'SHOP' => array(
        'NAME_ERROR'                  => 50300, // 商店名称不正确
        'COUNTRY_ERROR'               => 50301, // 所在国家不正确
        'PROVINCE_ERROR'              => 50302, // 省份不正确
        'CITY_ERROR'                  => 50303, // 城市不正确
        'STREET_ERROR'                => 50304, // 具体街道地址不正确
        'TEL_ERROR'                   => 50305, // 电话不正确
        'MOBILE_NBR_ERROR'            => 50306, // 移动电话不正确
        'IS_OUTTAKE_ERROR'            => 50307, // 是否支持送外卖不正确
        'OPENING_TIME_ERROR'          => 50308, // 店铺开门时间不正确
        'CLOSED_TIME_ERROR'           => 50309, // 店铺关门时间不正确
        'IS_ORDER_ON_ERROR'           => 50310, // 是否可预订不正确
        'LOGO_ERROR'                  => 50311, // 商店头像不正确
        'TYPE_ERROR'                  => 50312, // 商店类型不正确
        'IMG_ERROR'                   => 50313, // 装饰图片不正确
        'SHOP_CODE_ERROR'             => 50314, // 商店编码不正确
        'SHORT_DES_ERROR'             => 50315, // 商店简介不正确
        'NOT_EXIST'                   => 50316, // 商家不存在
        'SHOP_CODE_EMPTY'             => 50317, // 商家编码为空
        'LICENSE_EXPIRE_TIME_ERROR'   => 50318, // 营业执照到期日不正确
        'CAN_NOT_TAKE_OUT'            => 50319, // 商家不能够送外卖
        'SHOP_CLOSED'                 => 50320, // 商家已经打烊
        'IS_CATERING_EMPTY'           => '50321', // 是否为餐饮类商铺为空
        'IS_OUTTAKE_EMPTY'            => '50322', // 是否外卖为空
        'IS_OPEN_TAKEOUT_EMPTY'       => '50323', // 是否开启外卖为空
        'EAT_PAY_TYPE_EMPTY'          => '50324', // 堂食支付类型为空
        'TAKEOUT_REQUIRE_PRICE_EMPTY' => '50325', // 起送价为空
        'DELIVERY_FEE_EMPTY'          => '50326', // 配送费为空
        'DELIVERY_DISTANCE_EMPTY'     => '50327', // 最远配送距离为空
        'DELIVERY_START_TIME_EMPTY'   => '50328', // 外卖配送开始时间为空
        'DELIVERY_END_TIME_EMPTY'     => '50329', // 外卖配送结束时间为空
        'IS_OPEN_EAT_EMPTY'           => '50330', // 是否开启堂食为空
        'BUSINESS_HOURS'              => '50331', //营业时间不正确
    ),

    // 买单相关
    'PAY' => array(
        'PRICE_EMPTY'         => 50400, // 请输入消费金额
        'PRICE_ERROR'         => 50401, // 消费金额不正确
        'COUPON_BONUS_REPEAT' => 50402, // 优惠券与红包不能同时使用
        'CONSUME_CANCELED'    => 50403, // 支付已经取消
    ),

    // 用户信息相关
    'USER' => array(
        'USER_CODE_EMPTY'       => 50500, // 请输入用户编码
        'LONGITUDE_EMPTY'       => 50501, // 请输入用户所在经度
        'LATITUDE_EMPTY'        => 50502, // 请输入用户所在纬度
        'USER_CODE_ERROR'       => 50503, // 用户编码错误
        'PAY_PWD_EMPTY'         => 50504, // 请输入支付密码
        'CONFIRM_PAY_PWD_EMPTY' => 50505, // 请再次输入支付密码
        'PAY_PWD_NOT_SAME'      => 50506, // 两次输入的支付密码不一致
        'PAY_PWD_ERROR'         => 50507, // 支付密码错误
        'INVITE_CODE_NOT_EXIST' => 50508, // 该邀请码不存在
        'REGISTER_LIMIT'        => 50509, // 一个设备能注册的手机号数量超出
    ),

    // 商店装饰信息相关
    'SHOP_DEC' => array(
        'IMG_ERROR'    => 50600,  // 装饰图片不正确
        'TOO_MANY_IMG' => 50601, // 装饰图片不得超过6张
    ),

    // 红包相关
    'BONUS' => array(
        'NAME_ERROR'            => 50700, // 红包名不正确
        'TYPE_ERROR'            => 50701, // 红包所属类型不正确
        'CREATOR_CODE_ERROR'    => 50702, // 创建者编码不正确
        'UPPERPRICE_ERROR'      => 50704, // 红包金额上限不正确
        'LOWERPRICE_ERROR'      => 50705, // 红包金额下限不正确
        'TOTAL_VALUE_ERROR'     => 50706, // 发行总额不正确
        'NBRPERDAY_ERROR'       => 50707, // 每天限发数量不正确
        'TOTAL_VOLUME_ERROR'    => 50708, // 发行总量不正确
        'START_TIME_ERROR'      => 50709, // 开抢时间不正确
        'END_TIME_ERROR'        => 50710, // 结束时间不正确
        'START_USE_TIME_ERROR'  => 50711, // 开始使用时间不正确
        'END_USE_TIME_ERROR'    => 50712, // 截止使用时间不正确
        'VALIDITY_PERIOD_ERROR' => 50713, // 红包有效期不正确
        'REACH_DAY_LIMIT'       => 50714, // 今天的红包已经被领完了，明天再来吧
        'EMPTY'                 => 50715, // 红包已经被领完了
        'UPPER_LT_LOWER'        => 50716, // 红包最大金额小于最小金额
        'REPEAT'                => 50717, // 您已经领取过该红包了
        'START_GT_END'          => 50718, // 抢红包开始时间不能大于结束时间
        'TOTAL_VALUE_TOO_LOW'   => 50719, // 红包发行总额过低
        'NOT_AVAILABLE'         => 50720, // 红包不可用
        'USED'                  => 50721, // 红包已被使用
        'NO_BEGINNING'          => 50722, // 还没有到达抢红包开始时间
        'IS_OVER'               => 50723, // 抢红包活动已经结束
        'EXPIRED'               => 50724, // 红包已经过期
        'PLAT_UPPER_ERROR'      => 50725, // 平台红包不够
        'SHOP_UPPER_ERROR'      => 50726, // 商家红包不够
        'TOTAL_VALUE_TOO_HIGH'  => 50727, // 红包发行总额过高
    ),

    // POS相关错误
    'POS' => array(
        'TYPE_ERROR' => 50800, // 服务类型不正确
    ),

    // 用户支付相关
    'USER_CONSUME' => array(
        'CONSUME_NOT_EXIST' => 50900, // 用户支付记录不存在
    ),

    // 用户优惠券相关
    'USER_COUPON' => array(
        'USER_COUPON_CODE_EMPTY' => 51000, // 请选择一张优惠券
        'NOT_REACH_USE_TIME' => 51001, // 优惠券未到使用时间
        'CAN_NOT_USE' => 51002, // 优惠券不可用
    ),

    // 产品类别相关
    'PRODUCT_CATEGORY' => array(
        'CATEGORY_NAME_EMPTY' => '52100', // 产品类别名称不能为空
        'CATEGORY_NAME_ERROR' => '52101', // 产品类别名称错误
        'CATEGORY_ID_EMPTY'   => '52102', // 产品类别ID不能为空
        'CAN_NOT_DELETE'      => '52103', // 类别下仍有产品，不能删除
    ),

    // 用户活动码相关
    'USER_ACT_CODE' => array(
        'CAN_NOT_REFUND' => '52200', // 不能退款
        'CAN_NOT_REFUND_SAME_DAY' => '52201' // 购买当日不能退款
    ),

    // 产品相关
    'PRODUCT' => array(
        'PRODUCT_NAME_EMPTY'      => '53100', // 产品名字不能为空
        'PRODUCT_NAME_ERROR'      => '53101', // 产品名字错误
        'PRODUCT_IMG_EMPTY'       => '53102', // 产品图片不能为空
        'NOT_TAKEOUT_PRICE_EMPTY' => '53103', // 产品堂食单价不能为空
        'NOT_TAKEOUT_PRICE_ERROR' => '53104', // 产品堂食单价错误
        'TAKEOUT_PRICE_EMPTY'     => '53105', // 产品外卖单价不能为空
        'TAKEOUT_PRICE_ERROR'     => '53106', // 产品外卖单价错误
        'RECOMMEND_LEVEL_EMPTY'   => '53107', // 产品推荐度不能为空
        'RECOMMEND_LEVEL_ERROR'   => '53108', // 产品推荐度错误
        'SPICY_LEVEL_EMPTY'       => '53109', // 产品辣度不能为空
        'SPICY_LEVEL_ERROR'       => '53110', // 产品辣度错误
        'UNIT_EMPTY'              => '53111', // 产品单位不能为空
        'UNIT_ERROR'              => '53112', // 产品单位错误
        'IS_TAKEN_OUT_EMPTY'      => '53113', // 是否可外送不能为空
        'IS_TAKEN_OUT_ERROR'      => '53114', // 是否可外送错误
        'SORT_NBR_EMPTY'          => '53115', // 排序号不能为空
        'SORT_NBR_ERROR'          => '53116', // 排序号错误
        'CAN_NOT_DELETE'          => '53117', // 产品被下单过，不可删除
        'PRODUCT_ID_EMPTY'        => '53118', // 产品编码为空
    ),

    // 用户地址相关
    'USER_ADDRESS' => array(
        'CONTACT_NAME_EMPTY'   => '54100', // 联系人姓名不能为空
        'CONTACT_MOBILE_EMPTY' => '54101', // 联系人手机号不能为空
        'PROVINCE_EMPTY'       => '54102', // 省份不能为空
        'CITY_EMPTY'           => '54103', // 城市不能为空
        'DISTRICT_EMPTY'       => '54104', // 区或县不能为空
        'STREET_EMPTY'         => '54105', // 具体地址不能为空
        'ID_ERROR'             => '54106', // 用户地址ID错误
    ),

    // 订单产品相关
    'ORDER_PRODUCT' => array(
        'UNIT_PRICE_EMPTY' => '54200', // 单价为空
        'NBR_EMPTY'        => '54201', // 数量为空
        'AVAILABLE_OVER'   => '54202', // 已上数量大于总数量
        'CAN_NOT_ADD_NEW_PRODUCT' => '54203', // 该订单不能添加新的菜品
    ),

    // 退款相关
    'REFUND' => array(
        'CAN_NOT_REFUND_APART' => '55000', // 当日订单不能部分退款
    ),

    // 手机号码相关
    'MOBILE_NBR' => array(
        'LENGTH'            => 11,     //手机号码长度为11位
        'NO_REGISTER'       => 20207,  // 用户不存在
        'DISABLE'           => 20208,  // 该帐号已经被禁用
        'EMPTY'             => 60000,  // 请输入手机号码
        'ERROR'             => 60001,  // 手机号码不正确
        'RESERVED_ERROR'    => 60002,  // 银行预留手机号码不正确
        'REPEAT'            => 60003,  // 手机号码已经被使用
        'RECOM_NBR_ERROR'   => 60004,  // 推荐人手机号码错误
        'RESERVED_NOT_SAME' => 60005,  // 手机号码与卡片预留手机不一致
        'LOGIN_REPEAT'      => 60009,  // 不可多设备登陆
    ),

    // 密码相关
    'PWD' => array(
        'EMPTY'              => 60010,  // 请输入密码
        'WRONG'              => 60011,  // 用户名密码不匹配
        'ORIGINAL_PWD_ERROR' => 60012,  // 原密码错误
        'PWD_SAME'           => 60013,  // 新密码与原密码一致
        'NEW_PWD_EMPTY'      => 60014,  // 请输入新密码
        'ERROR'              => 60015,  // 密码格式不正确
    ),

    // 营业执照编号
    'LICENSE_NBR' => array(
        'EMPTY' => 60020,  // 请输入营业执照编号
        'ERROR' => 60021,  // 营业执照编号不正确
    ),

    // 报名参加活动相关
    'USER_ACT' => array(
        'USER_CODE_ERROR' => 60100, // 用户编码为空
        'ACT_CODE_ERROR'  => 60101, // 活动编码为空
        'ADULT_M_ERROR'   => 60102, // 男性大人人数错误
        'ADULT_F_ERROR'   => 60103, // 女性大人人数错误
        'KID_M_ERROR'     => 60104, // 男性小孩人数错误
        'KID_F_ERROR'     => 60105, // 女性小孩人数错误
    ),

    // 商家端激活相关
    'ACTIVATE' => array(
        'NOT_COMMIT' => 60200, // 手机号未提交审核
        'NOT_PASSED' => 60201, // 手机号审核还未通过，请耐心等待
        'PASSED'     => 60202, // 手机号审核已经通过，请直接登录
        'ACTIVATED' => 60203, // 商家已经审核过，不能重复审核。
    ),

    // 订单相关错误
    'ORDER' => array(
        'CODE_ERROR'                => '60500', // 订单编码错误
        'NOT_EXIST'                 => '60501', // 订单不存在
        'STATUS_EMPTY'              => '60502', // 订单状态为空
        'AMOUNT_EMPTY'              => '60503', // 订单金额为空
        'AMOUNT_ERROR'              => '60504', // 订单金额不正确
        'ORDER_TYPE_EMPTY'          => '60505', // 订单类型为空
        'ORDER_STATUS_EMPTY'        => '60506', // 外卖订单状态为空
        'RECEIVER_EMPTY'            => '60507', // 接收人姓名为空
        'RECEIVER_MOBILE_NBR_EMPTY' => '60508', // 接收人联系电话为空
        'DELIVERY_ADDRESS_EMPTY'    => '60509', // 配送地址为空
        'REFUND_REASON_EMPTY'       => '60510', // 退款原因为空
        'NOT_EQ_UNRECEIVED'         => '60511', // 当前订单不再未接单状态，用户不可以取消订单
        'CODE_EMPTY'                => '60512', // 订单编码为空
        'MIN_PAY_ERROR'             => '60513', // 实际支付金额小于最小支付金额
        'CAN_NOT_SETTLEMENT'        => '60514', // 订单不能提交结算
        'MIN_DISCOUNT_PAY_ERROR'    => '60515', // 参与优惠的实际支付金额小于0
    ),

    // 课程报名相关
    'SIGN_CLASS' => array(
        'CLASS_EMPTY' => '60600', // 没有选择报名课程
    ),

    // 报名信息相关错误
    'SHOP_SIGN_INFO' => array(
        'STU_NAME_EMPTY' => '60700', // 学员姓名为空
        'STU_SEX_EMPTY' => '60701', // 学员性别为空
        'STU_BIRTHDAY_EMPTY' => '60702', // 学员出生日期为空
        'STU_SCHOOL_EMPTY' => '60703', // 学员学校为空
        'STU_GRADE_EMPTY' => '60704', // 学员年级为空
        'STU_TEL_EMPTY' => '60705', // 学员联系电话为空
    ),

    // 学员报名课程相关错误
    'STUDENT_CLASS' => array(
        'SHOP_SIGN_CODE_EMPTY' => '60800', // 没有报名信息
        'CLASS_EMPTY' => '60801', // 没有选择课程
    ),

    // 验证码相关错误
    'VALIDATE_CODE' => array(
        'SEND_FAIL'      => 80010, // 验证码请求失败
        'ERROR'          => 80011, // 验证码错误
        'EMPTY'          => 80012, // 请输入验证码
        'ACTION_ERROR'   => 80013, // 动作不正确
        'APP_TYPE_ERROR' => 80014, // app类型不正确
    ),

    // 商家修改、添加员工相关
    'SHOP_STAFF' => array(
        'NAME_EMPTY'         => '80040', // 请输入员工姓名
        'TYPE_EMPTY'         => '80041', // 请选择员工类型
        'NOT_EXIST'          => '80042', // 员工不存在
        'MOBILE_NBR_REPEAT'  => '80043', // 该手机号码已存在
        'BIG_MNG_NAME_ERROR' => '80044', // 员工姓名与联系号码不符
        'BRAND_EMPTY'        => '80045', // 请选择品牌
        'STAFF_CODE_EMPTY'   => '80046', // 店员编码为空
        'STAFF_NOT_ACTIVATE' => '80047', // 员工未激活
    ),

    // 优惠券相关
    'COUPON' => array(
        'USED'                           => '50014', // 选择的优惠券已被使用了
        'NAME_ERROR'                     => '80200', // 优惠券名字不正确
        'TYPE_ERROR'                     => '80201', // 优惠券类型不正确
        'CREATOR_CODE_ERROR'             => '80202', // 创建者编码不正确
        'BATCH_NBR_ERROR'                => '80204', // 批次号不正确
        'TOTAL_VOLUME_ERROR'             => '80205', // 总发行量不正确
        'REMARK_ERROR'                   => '80206', // 备注不正确
        'EXPIRE_TIME_ERROR'              => '80207', // 优惠券失效时间不正确
        'URL_ERROR'                      => '80208', // 券样不正确
        'END_TAKING_DATE_ERROR'          => '80209', // 最后可领用日期不正确
        'DISCOUNT_PERCENT_ERROR'         => '80210', // 打折数额不正确
        'INDUSTRY_CATEGORY_ERROR'        => '80211', // 所属行业不正确
        'INSTEAD_PRICE_ERROR'            => '80212', // 抵用金额不正确
        'AVAILABLE_PRICE_ERROR'          => '80213', // 达到多少金额可用不正确
        'LIMITED_NBR_ERROR'              => '80214', // 可用上限不正确
        'NBR_PER_PERSON_ERROR'           => '80215', // 每人可领用数量不正确
        'IS_CONSUME_REQUIRED_ERROR'      => '80217', // 是否消费后才可以领取
        'START_USING_TIME_ERROR'         => '80218', // 优惠券开始使用时间不正确
        'BEEN_TOKEN_AWAY'                => '80219', // 优惠券已被领走了
        'EXPIRED'                        => '80220', // 优惠券已过期
        'BEEN_TOKEN_OVER'                => '80221', // 优惠券已被领完了
        'LIMIT'                          => '80222', // 您领用的数量已达上限
        'NOT_EXIST'                      => '80223', // 优惠券不存在
        'OTHER_SIDE_NO_THE_COUPONS'      => '80224', // 对方已经没有该优惠券了
        'HAS_BEEN_EXTORT_REQUEST'        => '80225', // 你已经向对方请求/索要过了
        'HAS_REPLY'                      => '80226', // 你已经处理过对方的请求
        'NOT_AVAILABLE'                  => '80227', // 优惠券不可用
        'BELONGING_EMPTY'                => '80228', // 请输入优惠券归属
        'NBR_PER_PERSON_GT_TOTAL_VOLUME' => '80229', // 每人可领数量不能大于发行总量
        'START_TAKING_DATE_ERROR'        => '80230', // 优惠券开始领用时间错误
        'NO_SEND_COUPON'                 => '80231', // 没有用户可以领取的可送的优惠券
        'NO_REG_SEND_COUPON'             => '80232', // 没有送新注册用户的优惠券
        'REG_COUPON_CAN_NOT_BE_SENT'     => '80234', // 新注册用户优惠券不可送
        'PRIVILEGE_REPEAT'               => '80235', // 您已经享受过该特权
        'USE_LIMIT_NBR'                  => '80236', // 使用数量超过上限了
        'PAY_PRICE_ERROR'                => '80237', // 购买金额不正确
        'OVER_NBR_PER_PERSON'            => '80238', // 个人购买数量超上限
        'FUNCTION_ERROR'                 => '80239', // 每张可做什么错误
        'REST_ERROR'                     => '80240', // 剩余数量不足
    ),

    // 会员卡相关
    'CARD' => array(
        'REPEAT'                      => 50005,	// 您已申请过该种会员卡
        'UPPER_LIMIT'                 => 50006,	// 该种会员卡的会员数量已达上限
        'NOT_QUALIFIED'               => 50007,	// 您不符合该种会员卡的申请标准
        'CANCEL'                      => 50013,	// 此卡已被注销
        'NAME_ERROR'                  => 80300,	// 卡名不正确
        'TYPE_ERROR'                  => 80301,	// 卡的类型不正确
        'CARD_CODE_EMPTY'             => 80302, // 请输入会员卡号
        'CARD_LVL_ERROR'              => 80303,	// 卡的等级不正确
        'CREATOR_CODE_ERROR'          => 80305,	// 创建者编码不正确
        'URL_ERROR'                   => 80308,	// 卡样不正确
        'DISCOUNT_REQUIRE_ERROR'      => 80309,	// 享受折扣的积分要求不正确
        'DISCOUNT_ERROR'              => 80310,	// 可享受折扣不正确
        'IS_REAL_NAME_REQUIRED_ERROR' => 80311,	// 是否实名制
        'IS_SHARABLE_ERROR'           => 80312,	// 是否多人使用
        'POINT_LIFE_TIME_ERROR'       => 80313,	// 积分有效期不正确
        'POINTS_PER_CASH_ERROR'       => 80314,	// 每消费1元积多少积分不正确
        'REMARK_ERROR'                => 80315,	// 备注不正确
        'TIME_LIMIT'                  => 80316, // 距离上次编辑还不到1小时，暂时不能进行编辑
        'OUT_POINTS_PER_CASH_ERROR'   => 80317, // 每多少积分价值1元不正确
        'ADD_USER_CARD_FAIL'          => 80318, // 用户添加会员卡失败
    ),

    // 用户会员卡相关
    'USER_CARD' => array(
        'DISABLE' => 80400, // 用户会员卡不可用
    ),

    // 子相册相关
    'SUB_ALBUM' => array(
        'NAME_ERROR'  => 80500 , // 子相册名字错误
        'NAME_REPEAT' => 80501 , // 子相册名字重复
    ),

    'COMMUNICATION' => array(
        'USER' => 88001, //会员不能为空
        'SHOP' => 88002, //商家不能为空
        'MESSAGE' => 88003, //消息不能为空
        'ERRORINFO' => 88004, //错误信息不能为空
        'ERRORIMG' => 88005, //错误截图不能为空
        'TOSHOP' => 88006, //被反馈商家不能为空
    ),

    'OPEN_GIFT' => array(
        'DISABLE' => 89001, // 自己不能给自己拆
        'ALREADY' => 89002, // 已经帮TA拆过了
        'LIMIT' => 89003, // 今天帮朋友拆的机会已用完
        'LIMIT_TIME' => 89004, // 拆礼盒的时间已过
    ),

    'M_RECIPIENT' => array(
        'LIMIT_NBR' => 87001, // 短信接受人添加数量超过限制
    ),

    'REMIND_TO_SHOP' => array(
        'REPEAT' => 78001, // 你已经提醒过了
    ),

    // 系统异常
    'API_INTERNAL_EXCEPTION'    => 20000,  //服务端（API）内部异常
    'CLIENT_INTERNAL_EXCEPTION' => 20001,  // 客户端内部异常。
    'CLIENT_OFFLINE'            => 20102,  // 客户端未联网
    'INTERNET_EXCEPTION'        => 20103,  // 网络异常
    'TOKEN_OVERTIME'            => 20204,  // 会话令牌超时失效 （Session Out）
    'INVALID_TOKEN'             => 20205,  // 会话验证错误（令牌token非法，或者vcode非法）
    'USER_NO_AUTHORITY'         => 20206,  // 用户无权限访问资源
    'COMPULSORY_LOGOFF'         => 20209,  // 用户被强制退出

    // JSON-RPC异常
    'INVALID_REQUEST' => -32600,  // Invalid Request, The JSON sent is not a valid Request object.
    'INVALID_METHOD'  => -32601,  // Method not found, The method does not exist / is not available.
    'INVALID_PARAMS'  => -32602,  // Invalid params, Invalid method parameter(s).
    'INTERNAL_ERROR'  => -32603,  // Internal error
    'INVALID_JSON'    => -32700,  // Parse error, Invalid Json.

    //会员卡消息模板
    'CARD_MSG_TDL' => array(
        'GET' => '成功获得会员卡，成为{{cardName}}会员，记得关注商家消息哦',
        'SHOP_SEND' => '{{nickName}}成为了{{cardName}}会员',
        'POINT_ARRIVAL' => '到账积分{{points}}',
        'EXPIRED_POINT' => '即将到期积分{{points}}',
    ),

    //优惠券消息模板
    'COUPON_MSG_TDL' => array(
        'REQUEST' => '你向{{seller}}索要{{shopName}}的{{couponName}}优惠券{{nbr}}张',
        'BEEN_REQUEST' => '{{buyer}}向你要{{shopName}}的{{couponName}}优惠券{{nbr}}张',
        'USED' => '您使用了 1 张优惠券',
        'EXPIRE' => '您有优惠券即将过期，请尽快使用',
        'GRAB' => '亲，领到了优惠券，可以分享哦',
        'SEND' => '我同意送你优惠券',
        'BEEN_TOKEN_AWAY' => '{{buyer}}刚从你这领走了优惠券',
    ),

    // 消息标题模板
    'MSG_TITLE_TDL' => array(
        'REQUEST_COUPON'      => '索取优惠券',
        'BEEN_REQUEST_COUPON' => '有人向你索要优惠券',
        'GRAB_COUPON'         => '领取优惠券',
        'SEND_COUPON'         => '得到优惠券',
        'USE_COUPON'          => '{{shopName}}-优惠券',
        'APPLY_CARD'          => '{{shopName}}-会员卡',
        'SHOP_SEND_CARD'          => '会员卡发行',
    ),

    // 向商户推送消息的模板
    'SHOP_PUSH_MSG_TDL' => array(
        'PAYED' => '顾客付款成功，付款{{realPay}}元',
    ),

    // 默认头像
    'DEFAULT_AVATAR' => '/Public/img/avatar.jpg',
    // 默认活动图片
    'DEFAULT_ACT' => '/Public/img/act.jpg',

    // 工行API交易码
    'TXCODE' => array(
        'SIGN_CONTRACT'      => '20100', // 支付协议签订交易
        'TERMINATE_CONTRACT' => '20110', // 支付协议解除交易
        'VAL_CODE'           => '20260', // 手机验证码获取交易
        'BANKCARD_CONSUME'   => '20270', // 银行卡消费交易
        'CONSUME_REVOCATION' => '20271', // 银行卡消费撤销交易
    ),

    // 扫码支付相关错误
    'QR_PAY_ERROR_CODE' => array(
        'SWEEP_FAIL'           => '85001', // 扫描失败
        'QR_SAFE'              => '85002', // 请重新扫描二维码
        'QR_EXPIRED'           => '85003', // 二维码过期
        'USER_NOT_EXIST'       => '85004', // 用户不存在
        'BANK_CARD_NOT_EXIT'   => '85005', // 用户未绑定该银行卡
        'BANK_CARD_NOT_USE'    => '85006', // 该银行卡解绑或未成功绑定
        'CONSUME_AMOUNT_LIMIT' => '85007', // 扫码支付仅限300或300以下
    ),

    //教育类商家相关错误
    'EDUCATION_SHOP_ERROR_CODE' => array(
        'CLASS_WEEK_INFO_EMPTY'         => '77001', //上课时间为空
        'TEACHER_CODE_EMPTY'            => '77002', //教师编码为空
        'CLASS_NAME_EMPTY'              => '77003', //班级名字为空
        'LEARN_START_DATE_EMPTY'        => '77004', //学习开始时间为空
        'LEARN_END_DATE_EMPTY'          => '77005', //学习结束时间为空
        'LEARN_MEMO_EMPTY'              => '77006', //适合描述为空
        'LEARN_FEE_EMPTY'               => '77007', //报名费用为空
        'LEARN_NUM_EMPTY'               => '77008', //所学课时为空
        'SIGN_START_DATE_EMPTY'         => '77009', //报名开始时间为空
        'SIGN_END_DATE_EMPTY'           => '77010', //报名结束时间为空
        'CLASS_CODE_EMPTY'              => '77011', //班级编码为空
        'CW_WEEK_NAME_EMPTY'            => '77012', //周几为空
        'CW_START_TIME_EMPTY'           => '77013', //上课开始时间为空
        'CW_END_TIME_EMPTY'             => '77014', //上课结束时间为空
        'SHOP_CLASS_DELETE_ERROR'       => '77015', //有人报名，不能删除
        'SHOP_TEACHER_DELETE_ERROR'     => '77016', //该老师有开课，不能删除
        'TEACHER_NAME_EMPTY'            => '77017', //教师名字为空
        'TEACHER_TITLE_EMPTY'           => '77018', //教师职称为空
        'SHOP_SIGN_CODE_EMPTY'          => '77019', //报名编码为空
        'STAR_CODE_EMPTY'               => '77020', //星编码为空
        'STAR_NAME_EMPTY'               => '77021', //星姓名为空
        'ADV_IMG_EMPTY'                 => '77022', //广告图为空
        'RECRUIT_IMG_EMPTY'             => '77023', //招生图为空
        'SHOP_HONOR_IMG_EMPTY'          => '77024', //荣誉图为空
        'HEADER_EXP_MODEL'              => '77025', //表述模式为空
        'HEADER_TXT_MEMO_EMPTY'         => '77026', //校长之语为空
        'HEADER_IMG_EMPTY'              => '77027', //图片为空
        'REMARK_WHOLE_LVL_EMPTY'        => '77028', //总体星评为空
        'REMARK_EFFECT_LVL_EMPTY'       => '77029', //效果星评为空
        'REMARK_TEACHER_LVL_EMPTY'      => '77030', //师资星评为空
        'REMARK_ENV_LVL_EMPTY'          => '77031', //环境星评为空
        'SHOP_REMARK_EMPTY'             => '77032', //商家回复为空
    ),

    // 评价课程相关错误
    'CLASS_REMARK' => array(
        'NO_PERMISSION' => '78000', // 没有权限评价
    ),
);