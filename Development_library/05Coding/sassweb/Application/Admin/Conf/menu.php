<?php
return array (
    'GROUND_PERSON_MENU' => array(
        // 地推人员录入的商户管理
        array('name'=>'商户管理', 'id'=>'shop', 'url'=>'', 'submenu' => array(
            array('name'=>'商户', 	'url'=>'/Admin/PreShop/listPreShop'),
            array('name'=>'编辑商户', 		'url'=>'/Admin/PreShop/editPreShop'),
        )),
    ),
    'MENU' => array(
        // 地推人员录入的商户管理
        array('name'=>'地推商户', 'id'=>'preShop', 'url'=>'', 'submenu' => array(
            array('name'=>'商户列表', 	'url'=>'/Admin/PreShop/listPreShop'),
            array('name'=>'数据导出', 	'url'=>'/Admin/PreShop/dataExport'),
        )),
        // 品牌管理
        array('name'=>'品牌管理', 'id'=>'brand', 'url'=>'', 'submenu' => array(
            array('name'=>'品牌列表', 	'url'=>'/Admin/Brand/listBrand'),
            array('name'=>'编辑品牌', 		'url'=>'/Admin/Brand/editBrand'),
        )),
        // 商户相关管理
        array('name' => '商户相关管理', 'id' => 'shop', 'url' => '', 'submenu' => array(
            array('name' => '商户列表', 'url' => '/Admin/Shop/listShop'),
            array('name' => '编辑商户', 'url' => '/Admin/Shop/editShop'),
            array('name' => '商户入驻申请', 'url' => '/Admin/Shop/listApply'),
            array('name' => '商户员工列表', 'url' => '/Admin/ShopStaff/listShopStaff'),
            array('name' => '编辑商户员工', 'url' => '/Admin/ShopStaff/editShopStaff'),
            array('name' => '商户优惠券列表', 'url' => '/Admin/BatchCoupon/listSpCoupon'),
            array('name' => '编辑优惠券', 'url' => '/Admin/BatchCoupon/editCoupon'),
            array('name' => '商户订单列表', 'url' => '/Admin/ConsumeOrder/listConsumeOrder'),
            array('name' => '账单列表', 'url' => '/Admin/UserConsume/listUserConsume'),
//            array('name' => '商户红包列表', 'url' => '/Admin/Bonus/listSpBonus'),
//            array('name' => '编辑红包', 	'url' => '/Admin/Bonus/editBonus'),
//            array('name' => '商户活动列表', 'url' => '/Admin/Activity/listSpActivity'),
//            array('name' => '编辑活动', 'url' => '/Admin/Activity/editActivity'),
            array('name' => '商户会员卡列表', 'url' => '/Admin/Card/listCard'),
            array('name' => '编辑会员卡', 'url' => '/Admin/Card/editCard'),
            array('name' => '商家类型列表', 'url' => '/Admin/ShopType/listShopType'),
            array('name' => '编辑商家类型', 'url' => '/Admin/ShopType/editShopType'),
        )),
        // 用户相关管理
        array('name' => '用户相关管理', 'id' => 'user', 'url' => '', 'submenu' => array(
            array('name' => '用户列表', 'url' => '/Admin/User/listUser'),
            array('name' => '用户添加', 'url' => '/Admin/User/editUser'),
            array('name' => '用户会员卡', 'url' => '/Admin/UserCard/listUserCard'),
            array('name' => '用户优惠券', 'url' => '/Admin/UserCoupon/listUsCoupon'),
//            array('name' => '用户红包', 'url' => '/Admin/UserBonus/listUserBonus'),
            array('name' => '用户圈值', 'url' => '/Admin/User/listUserPoint'),
//            array('name' => '用户银行卡', 'url' => '/Admin/BankAccount/listBankAccount'),
//            array('name' => '用户绑卡数据导出', 'url' => '/Admin/User/exportExcelRegister')s
        )),
        // 平台相关管理
        array('name' => '智能POS惠圈平台管理', 'id' => 'plat', 'url' => '', 'submenu' => array(
            array('name' => '惠圈平台人员列表', 'url' => '/Admin/BmStaff/listBmStaff'),
            array('name' => '惠圈管理员日志', 'url' => '/Admin/StaffActionLog/listActionLog'),
            array('name' => '编辑人员', 'url' => '/Admin/BmStaff/editBmStaff'),
//            array('name' => '惠圈平台优惠券', 'url' => '/Admin/BatchCoupon/listPfCoupon'),
//            array('name' => '编辑优惠券', 'url' => '/Admin/BatchCoupon/editCoupon'),
//            array('name' => '平台红包列表', 	'url' => '/Admin/Bonus/listPfBonus'),
//            array('name' => '编辑红包', 	'url' => '/Admin/Bonus/editBonus'),
//            array('name' => '平台活动列表', 'url' => '/Admin/Activity/listPfActivity'),
//            array('name' => '编辑活动', 'url' => '/Admin/Activity/editActivity'),
        )),
       //机构管理
        array('name'=>'平台管理', 'id'=>'bank', 		'url'=>'', 'submenu' => array(
            array('name' => '所属平台', 'url' => '/Admin/Bank/listBank'),
            array('name' => '新增平台', 'url' => '/Admin/Bank/editBank'),
            array('name' => '微信卡券操作日志', 'url' => '/Admin/WeixinLog/listWeixinLog'),
        )),

        // 活动管理
//        array('name'=>'活动管理', 'id'=>'activity', 		'url'=>'', 'submenu' => array(
//            array('name'=>'工行活动列表', 'url'=>'/Admin/Activity/listIcbcActivity'),
//        )),
        // 轮播图添加
        array('name'=>'轮播图添加', 'id'=>'Swiper',    'url'=>'', 'submenu' => array(
            array('name' => '轮播图添加', 'url'=>'/Admin/Swiper/swiperRoll'),
        )),
        // 消息管理
//        array('name'=>'消息管理', 'id'=>'message', 		'url'=>'', 'submenu' => array(
//            array('name'=>'平台消息', 		'url'=>'/Admin/Message/listMessage'),
//            array('name'=>'消息查询统计', 		'url'=>'/Admin/Message/analysisMessage'),
//        )),
        // 投诉建议
        array('name'=>'投诉反馈', 'id'=>'feedback', 	'url'=>'', 'submenu' => array(
            array('name'=>'商家反馈', 'url'=>'/Admin/Feedback/listShopFeedback'),
            array('name'=>'用户反馈', 'url'=>'/Admin/Feedback/listUserFeedback'),
        )),
        // 城市开通管理
        array('name'=>'城市开通管理', 'id'=>'city', 	'url'=>'', 'submenu' => array(
            array('name'=>'城市列表', 	'url'=>'/Admin/District/listCity'),
        )),
        // 顾客端APP活动模块管理
//        array('name'=>'顾客端APP活动模块', 'id'=>'ClientActModule', 	'url'=>'', 'submenu' => array(
//            array('name'=>'活动模块', 	'url'=>'/Admin/ClientActModule/listClientActModule'),
//            array('name'=>'编辑活动模块', 		'url'=>'/Admin/ClientActModule/editClientActModule'),
//        )),
        // APP升级记录管理
//        array('name'=>'APP升级记录', 'id'=>'ShopAppLog', 	'url'=>'', 'submenu' => array(
//            array('name'=>'商家端升级记录', 	'url'=>'/Admin/ShopAppLog/listShopAppLog'),
//            array('name'=>'编辑商家端升级记录', 		'url'=>'/Admin/ShopAppLog/editShopAppLog'),
//            array('name'=>'顾客端升级记录', 	'url'=>'/Admin/ClientAppLog/listClientAppLog'),
//            array('name'=>'编辑顾客端升级记录', 		'url'=>'/Admin/ClientAppLog/editClientAppLog'),
//            array('name'=>'PC端升级记录', 	'url'=>'/Admin/PcAppLog/listPcAppLog'),
//            array('name'=>'编辑PC端升级记录', 		'url'=>'/Admin/PcAppLog/editPcAppLog'),
//        )),
        // APP奔溃日志
//        array('name'=>'APP崩溃日志', 'id'=>'CrashLog', 	'url'=>'', 'submenu' => array(
//            array('name'=>'顾客端APP崩溃日志', 	'url'=>'/Admin/CrashLog/listClientCrashLog'),
//            array('name'=>'商家端APP崩溃日志',   'url'=>'/Admin/CrashLog/listShopCrashLog'),
//        )),
        // APP配置管理
//        array('name'=>'APP配置管理', 'id'=>'Module', 	'url'=>'', 'submenu' => array(
//            array('name'=>'首页模块列表', 	'url'=>'/Admin/Module/listIndexModule'),
//            array('name'=>'筛选条件管理', 	'url'=>'/Admin/Module/listQuery'),
//        )),
        // 系统参数
//        array('name'=>'系统参数', 'id'=>'SystemParam', 	'url'=>'', 'submenu' => array(
//            array('name'=>'顾客端', 	'url'=>'/Admin/SystemParam/listClientSystemParam'),
//            array('name'=>'商家端',   'url'=>'/Admin/SystemParam/listShopSystemParam'),
//            array('name'=>'编辑系统参数',   'url'=>'/Admin/SystemParam/editSystemParam'),
//        )),
        // 统计分析
//        array('name'=>'统计分析', 'id'=>'Statistics', 	'url'=>'', 'submenu' => array(
//            array('name' => '商户统计分析', 'url'=>'/Admin/Shop/analysisShop'),
//            array('name' => '商户订单统计', 'url' => '/Admin/ConsumeOrder/listShopConsumeStatistics'),
//            array('name' => '按日订单成交统计', 'url' => '/Admin/ConsumeOrder/listOrderStatisticsByDay'),
//            array('name' => '商户会员卡统计分析', 'url'=>'/Admin/Card/analysisCard'),
//            array('name' => '商户优惠券统计', 'url'=>'/Admin/BatchCoupon/analysisCoupon'),
//            array('name' => '活动统计分析', 'url'=>'/Admin/Activity/analysisActivity'),
//            array('name' => '红包统计分析', 'url'=>'/Admin/Bonus/analysisBonus'),
//            array('name' => '用户统计分析', 'url'=>'/Admin/User/userAnalysis'),
//            array('name' => '用户消费统计', 'url'=>'/Admin/User/consumptionStatistics'),
//            array('name' => '惠圈下载记录', 'url'=>'/Admin/QrCode/listLog'),
//        )),
//        // 导出二维码
//        array('name'=>'二维码生成', 'id'=>'QrCode', 	'url'=>'', 'submenu' => array(
//            array('name' => '生成下载惠圈二维码', 'url'=>'/Admin/QrCode/cDownloadQC'),
//        )),
//        // 商户资金清算
//        array('name'=>'商户资金清算', 'id'=>'Liquidation', 	'url'=>'', 'submenu' => array(
//            array('name' => '文件列表', 'url'=>'/Admin/Liquidation/getLiquidationFileList'),
//            array('name' => '生成请求文件', 'url'=>'/Admin/Liquidation/getLiquidationRecord'),
//        )),
    )
);