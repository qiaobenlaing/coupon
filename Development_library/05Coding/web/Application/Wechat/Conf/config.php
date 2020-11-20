<?php
return array(
    //'配置项'=>'配置值'

    // 不需要登录即可访问的页面
    'PUBLIC_ACTONS' => array(
        'BmStaff:login',
    ),
    'SUCCESS' => true,   // 成功
    // 操作失败
    'FAIL'    => '操作失败',

    // 顾客端APP更新记录相关
    'CLIENT_APP_UPDATE' => array(
        'VERSION_NAME_EMPTY' => '请输入版本名称',
        'VERSION_CODE_EMPTY' => '请输入版本号',
        'UPDATE_URL_EMPTY' => '请输入更新URL',
        'UPDATE_CONTENT_EMPTY' => '请输入更新内容',
    ),

    // 商家端APP更新记录相关
    'SHOP_APP_UPDATE' => array(
        'VERSION_NAME_EMPTY' => '请输入版本名称',
        'VERSION_CODE_EMPTY' => '请输入版本号',
        'UPDATE_URL_EMPTY' => '请输入更新URL',
        'UPDATE_CONTENT_EMPTY' => '请输入更新内容',
    ),

    // 用户修改个人信息相关错误
    'UPDATE_INFO' => array(
        'EMPTY'           => "请将信息填写完整",
        'USER_ROLE_EMPTY' => "请输入用户角色类型",
        'NO_UPDATE'       => "您没有修改任何信息",
    ),

    // 其他相关错误
    'FOLLOW_SHOP_REPEAT' => "您已关注过该商家",
    'MONEY_LOW'          => "余额不足",

    // 银行卡相关
    'BANK_ACCOUNT' => array(
        'ACCOUNT_NAME_WRONG'    => "持卡人姓名不正确",
        'CERTIFICATE_NBR_WRONG' => "证件号码不正确",
        'ACCOUNT_NBR_WRONG'     => "银行卡号不正确",
    ),
    // 提交反馈相关
    'FEEDBACK' => array(
        'CONTENT_EMPTY' => "反馈内容为空",
    ),

    // 活动相关
    'ACTIVITY' => array(
        'REPEAT'                      => "您已经报过名了",
        'NAME_ERROR'                  => "活动主题不正确",
        'START_TIME_ERROR'            => "活动开始时间不正确",
        'END_TIME_ERROR'              => "活动结束时间不正确",
        'LOCATION_ERROR'              => "活动地点不正确",
        'TXT_CONTENT_ERROR'           => "活动说明不正确",
        'LIMITED_PARTICIPATORS_ERROR' => "活动参与人数上限不正确",
        'IS_PREPAY_REQUIRED_ERROR'    => "是否需要预付费不正确",
        'PREPAYMENT_ERROR'            => "预付金额不正确",
        'IS_REGISTER_REQUIRED_ERROR'  => "是否需要报名不正确",
        'ACTIVITY_IMG_ERROR'          => "活动图片不正确",
        'ACTIVITY_LOGO_ERROR'         => "活动方形图片不正确",
        'CREATOR_CODE_ERROR'          => "活动发起人编码不正确",
        'START_GT_END'                => "开始日期不能大于结束日期",
    ),

    // 商家信息相关
    'SHOP' => array(
        'NAME_ERROR'                => "商店名称不正确",
        'COUNTRY_ERROR'             => "所在国家不正确",
        'PROVINCE_ERROR'            => "省份不正确",
        'CITY_ERROR'                => "城市不正确",
        'STREET_ERROR'              => "具体街道地址不正确",
        'TEL_ERROR'                 => "商家对外联系电话不正确",
        'MOBILE_NBR_ERROR'          => "商户预留手机号",
        'IS_OUTTAKE_ERROR'          => "是否支持送外卖不正确",
        'OPENING_TIME_ERROR'        => "店铺开门时间不正确",
        'CLOSED_TIME_ERROR'         => "店铺关门时间不正确",
        'IS_ORDER_ON_ERROR'         => "是否可预订不正确",
        'LOGO_ERROR'                => "商店头像不正确",
        'TYPE_ERROR'                => "商店类型不正确",
        'IMG_ERROR'                 => "装饰图片不正确",
        'SHOP_CODE_ERROR'           => "商店编码不正确",
        'LICENSE_EXPIRE_TIME_ERROR' => '营业执照到期日不正确',
        'SHOP_OWNER_ERROR'          => '商户联系人不正确',

    ),

    // 买单相关
    'PAY' => array(
        'PRICE_EMPTY' => "请输入消费金额",
        'PRICE_ERROR' => "消费金额不正确",
    ),

    // 用户信息相关
    'USER' => array(
        'USER_CODE_EMPTY' => "请输入用户编码",
        'REAL_NAME_EMPTY' => '请输入用户姓名',
        'NICK_NAME_EMPTY' => '请输入昵称',
    ),
    // 手机号码相关
    'MOBILE_NBR' => array(
        'LENGTH'         => 11,     //手机号码长度为11位
        'NO_REGISTER'    => "用户不存在",
        'DISABLE'        => "该帐号已经被禁用",
        'EMPTY'          => "请输入手机号码",
        'ERROR'          => "手机号码不正确",
        'RESERVED_ERROR' => "银行预留手机号码不正确",
        'REPEAT'         => "手机号码已经被使用",
    ),

    // 密码相关
    'PWD' => array(
        'EMPTY'              => "请输入密码",
        'WRONG'              => "用户名密码不匹配",
        'ORIGINAL_PWD_ERROR' => "原密码错误",
        'PWD_SAME'           => "新密码与原密码一致",
        'NEW_PWD_EMPTY'      => "请输入新密码",
        'ERROR'              => "密码格式不正确",
        'NOT_SAME'           => "两次输入的密码不一致",
    ),

    // 营业执照编号
    'LICENSE_NBR' => array(
        'EMPTY' => "请输入营业执照编号",
        'ERROR' => "营业执照编号不正确",
    ),

    // 验证码相关错误
    'VALIDATE_CODE' => array(
        'SEND_FAIL' => "验证码请求失败",
        'ERROR'     => "验证码错误",
        'EMPTY'     => "请输入验证码",
    ),



    // 商家修改、添加员工相关
    'SHOP_STAFF' => array(
        'NAME_EMPTY' => "请输入员工姓名",
        'TYPE_EMPTY' => "请选择员工类型",
    ),

    // 优惠券相关
    'COUPON' => array(
        'USED'                           => "选择的优惠券已被使用了",
        'NAME_ERROR'                     => "优惠券名字不正确",
        'TYPE_ERROR'                     => "优惠券类型不正确",
        'CREATOR_CODE_ERROR'             => "创建者编码不正确",
        'SHOP_CODE_ERROR'                => "商店编码不正确",
        'BATCH_NBR_ERROR'                => "批次号不正确",
        'TOTAL_VOLUME_ERROR'             => "总发行量不正确",
        'REMARK_ERROR'                   => "备注不正确",
        'EXPIRE_TIME_ERROR'              => "优惠券失效时间不正确",
        'URL_ERROR'                      => "券样不正确",
        'END_TAKING_DATE_ERROR'          => "最后可领用日期不正确",
        'DISCOUNT_PERCENT_ERROR'         => "打折数额不正确",
        'INDUSTRY_CATEGORY_ERROR'        => "所属行业不正确",
        'INSTEAD_PRICE_ERROR'            => "面值不正确",
        'AVAILABLE_PRICE_ERROR'          => "达到多少金额可用不正确",
        'LIMITED_NBR_ERROR'              => "可用上限不正确",
        'NBR_PER_PERSON_ERROR'           => "每人可领用数量不正确",
        'IS_CONSUME_REQUIRED_ERROR'      => "是否消费后才可以领取",
        'START_USING_TIME_ERROR'         => "优惠券开始使用时间不正确",
        'BEEN_TOKEN_AWAY'                => "优惠券已被领走了",
        'EXPIRED'                        => "优惠券已过期",
        'BEEN_TOKEN_OVER'                => "优惠券已被领完了",
        'LIMIT'                          => "您领用的数量已达上限",
        'NOT_EXIST'                      => "优惠券不存在",
        'OTHER_SIDE_NO_THE_COUPONS'      => "对方已经没有该优惠券了",
        'HAS_BEEN_EXTORT_REQUEST'        => "你已经向对方请求/索要过了",
        'HAS_REPLY'                      => "你已经处理过对方的请求",
        'NBR_PER_PERSON_GT_TOTAL_VOLUME' => '每人可领数量不能大于发行总量',
        'START_TAKING_DATE_ERROR'        => '优惠券开始领用时间错误',
        'FUNCTION_ERROR'                 => '每张可做什么错误',
        'PRIVILEGE_REPEAT'               => '您已经享受过该特权'
    ),

    // 会员卡相关
    'CARD' => array(
        'REPEAT'                      => "您已申请过该种会员卡",
        'UPPER_LIMIT'                 => "该种会员卡的会员数量已达上限",
        'NOT_QUALIFIED'               => "您不符合该种会员卡的申请标准",
        'CANCEL'                      => "此卡已被注销",
        'NAME_ERROR'                  => "卡名不正确",
        'TYPE_ERROR'                  => "卡的类型不正确",
        'CARD_CODE_EMPTY'             => "请输入会员卡号",
        'CARD_LVL_ERROR'              => "卡的等级不正确",
        'CREATOR_CODE_ERROR'          => "创建者编码不正确",
        'URL_ERROR'                   => "卡样不正确",
        'DISCOUNT_REQUIRE_ERROR'      => "享受折扣的积分要求不正确",
        'DISCOUNT_ERROR'              => "可享受折扣不正确",
        'IS_REAL_NAME_REQUIRED_ERROR' => "是否实名制",
        'IS_SHARABLE_ERROR'           => "是否多人使用",
        'POINT_LIFE_TIME_ERROR'       => "积分有效期不正确",
        'POINTS_PER_CASH_ERROR'       => "每消费1元积多少积分不正确",
        'REMARK_ERROR'                => "备注不正确",
    ),

    // 红包相关
    'BONUS' => array(
        'NAME_ERROR'                  => "红包名不正确",
        'TYPE_ERROR'                  => "红包所属类型不正确",
        'CREATOR_CODE_ERROR'          => "创建者编码不正确",
        'SHOP_CODE_ERROR'             => "商家不正确",
        'UPPERPRICE_ERROR'            => "红包金额上限不正确",
        'LOWERPRICE_ERROR'            => "红包金额下限不正确",
        'TOTAL_VALUE_ERROR'           => "发行总额",
        'NBRPERDAY_ERROR'             => "每天限发数量",
        'TOTAL_VOLUME_ERROR'          => "发行总量不正确",
        'START_TIME_ERROR'            => "开抢时间不正确",
        'END_TIME_ERROR'              => "结束时间不正确",
        'START_USE_TIME_ERROR'        => "开始使用时间不正确",
        'END_USE_TIME_ERROR'          => "截止使用时间不正确",
        'VALIDITY_PERIOD_ERROR'       => "红包有效期不正确",
        'REACH_DAY_LIMIT'             => "今天的红包已经被领完了，明天再来吧",
        'EMPTY'                       => "红包已经被领完了",
        'UPPER_LT_LOWER'              => "红包最大金额小于最小金额",
        'REPEAT'                      => "您已经领取过该红包了",
        'START_GT_END'                => "抢红包开始时间不能大于结束时间",
        'TOTAL_VALUE_TOO_LOW'         => '红包发行总额过低',
        'TOTAL_VALUE_TOO_HIGH'        => '红包发行总金额过高',
    ),

    'API_INTERNAL_EXCEPTION' => false,

    // GET请求的操作
    'ACTION_GET' => array(
        '/Admin/BmStaff/homepage'             => '首页',
        '/Admin/BmStaff/login'                => '登录页面',
        '/Admin/BmStaff/logout'               => '退出登录',
        '/Admin/Shop/listShop'                => '商户列表',
        '/Admin/Shop/editShop'                => '商户编辑',
        '/Admin/Shop/analysisShop'            => '商户统计分析',
        '/Admin/Shop/shopInfo'                => '商户详情',
        '/Admin/Card/listCard'                => '会员卡列表',
        '/Admin/Card/analysisCard'            => '会员卡统计分析',
        '/Admin/ShopStaff/listShopStaff'      => '商户员工列表',
        '/Admin/ShopStaff/editShopStaff'      => '商户员工编辑',
        '/Admin/ShopStaff/analysisShopStaff'  => '商户员工分析',
        '/Admin/User/listUser'                => '用户列表',
        '/Admin/User/editUser'                => '用户添加',
        '/Admin/User/userAnalysis'            => '用户统计分析',
        '/Admin/User/getUserInfo'             => '查看用户详情',
        '/Admin/UserCard/listUserCard'        => '用户会员卡列表',
        '/Admin/UserCoupon/listUsCoupon'      => '用户优惠券',
        '/Admin/UserBonus/listUserBonus'      => '用户红包',
        '/Admin/BatchCoupon/listSpCoupon'     => '商户优惠券',
        '/Admin/BatchCoupon/listPfCoupon'     => '平台优惠券',
        '/Admin/BatchCoupon/editCoupon'       => '优惠券编辑',
        '/Admin/BatchCoupon/analysisCoupon'   => '优惠券统计分析',
        '/Admin/Activity/listSpActivity'      => '商户活动维护',
        '/Admin/Activity/listPfActivity'      => '平台活动维护',
        '/Admin/Activity/listIcbcActivity'    => '工行活动',
        '/Admin/Activity/editActivity'        => '活动编辑',
        '/Admin/Activity/analysisActivity'    => '活动统计分析',
        '/Admin/Bonus/listSpBonus'            => '商户红包维护',
        '/Admin/Bonus/listPfBonus'            => '平台红包维护',
        '/Admin/Bonus/editBonus'              => '红包编辑',
        '/Admin/Bonus/analysisBonus'          => '红包统计分析',
        '/Admin/Message/listMessage'          => '平台消息维护',
        '/Admin/Message/analysisMessage'      => '消息查询统计',
        '/Admin/Feedback/listShopFeedback'    => '商家反馈',
        '/Admin/Feedback/listUserFeedback'    => '用户反馈',
        '/Admin/Feedback/analysisFeedback'    => '建议分析',
        '/Admin/District/listCity'            => '城市列表',
        '/Admin/BmStaff/listBmStaff'          => '平台操作员',
        '/Admin/ShopAppLog/listShopAppLog'    => '商家端APP升级记录',
        '/Admin/ClientAppLog/listClientAppLog' => '顾客端APP升级记录',
        '/Admin/BmStaff/addBmStaff'           => '打开添加操作员'
    ),

    // POST请求的操作
    'ACTION_POST' => array(
        '/Admin/BmStaff/login'                 => '登录',
        '/Admin/BmStaff/logout'                => '退出登录',
        '/Admin/Shop/editShop'                 => '修改商户信息',
        '/Admin/ShopStaff/editShopStaff'       => '修改商户员工信息',
        '/Admin/User/editUser'                 => '修改用户信息',
        '/Admin/BatchCoupon/editCoupon'        => '修改优惠券信息' ,
        '/Admin/Activity/editActivity'         => '修改活动信息',
        '/Admin/Bonus/editBonus'               => '修改红包信息',
        '/Admin/ShopAppLog/editShopAppLog'     => '编辑商家端APP更新记录',
        '/Admin/ClientAppLog/editClientAppLog' => '编辑顾客端APP更新记录',
    ),

    'NO_LOG_ACTION' => array(
        '/Admin/StaffActionLog/listActionLog',
        '/Admin/Util/dropdown',
        '/Admin/Shop/shopCard',
        '/Admin/Shop/shopBatchCoupon',
        '/Admin/Shop/shopActivity',
        '/Admin/User/listUserCoupon',
        '/Admin/User/listUserBonus',
        '/Admin/User/listUserCard',
    ),
);