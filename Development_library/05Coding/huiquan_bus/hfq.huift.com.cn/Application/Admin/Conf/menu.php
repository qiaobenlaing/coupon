<?php
return array (
//    'GROUND_PERSON_MENU' => array(
//        // 地推人员录入的商户管理
//        array('name'=>'商户管理', 'id'=>'shop', 'url'=>'', 'submenu' => array(
//            array('name'=>'商户', 	'url'=>'/Admin/PreShop/listPreShop'),
//            array('name'=>'编辑商户', 		'url'=>'/Admin/PreShop/editPreShop'),
//        )),
//    ),
    'MENU' => array(

        // 商户统计
        array('name'=>'统计', 'id'=>'brand', 'url'=>'', 'submenu' => array(
            array('name'=>'商户统计', 	'url'=>'/Admin/Total/listConsumeTotal'),
        )),

        // 商户相关管理
        array('name' => '商户相关管理', 'id' => 'shop', 'url' => '', 'submenu' => array(
            array('name' => '商户列表', 'url' => '/Admin/Shop/listShop'),
            array('name' => '编辑商户', 'url' => '/Admin/Shop/editShop'),
            array('name' => '商户员工列表', 'url' => '/Admin/ShopStaff/listShopStaff'),
            array('name' => '编辑商户员工', 'url' => '/Admin/ShopStaff/editShopStaff'),
            array('name' => '商户关联列表', 'url' => '/Admin/ShopLink/linkShopList'),
            array('name' => '编辑商户关联', 'url' => '/Admin/ShopLink/addShopLink'),
//            array('name' => '商户关联列表', 'url' => '/Admin/ShopLink/linkShopList'),
//            array('name' => '编辑商户关联', 'url' => '/Admin/ShopLink/addShopLink'),
//            array('name' => '商户员工列表', 'url' => '/Admin/ShopStaff/listShopStaff'),
//            array('name' => '编辑商户员工', 'url' => '/Admin/ShopStaff/editShopStaff'),
            array('name' => '商户优惠券列表', 'url' => '/Admin/BatchCoupon/listSpCoupon'),
            array('name' => '编辑优惠券', 'url' => '/Admin/BatchCoupon/editCoupon'),
            array('name' => '商户订单列表', 'url' => '/Admin/ConsumeOrder/listConsumeOrder'),
            array('name' => '账单列表', 'url' => '/Admin/UserConsume/listUserConsume'),
//            array('name' => '活动列表', 'url' => '/Admin/Activity/listSpActivity'),
//            array('name' => '编辑活动', 'url' => '/Admin/Activity/editActivity'),
//            array('name' => '商户会员卡列表', 'url' => '/Admin/Card/listCard'),
//            array('name' => '编辑会员卡', 'url' => '/Admin/Card/editCard'),
            array('name' => '商家类型列表', 'url' => '/Admin/ShopType/listShopType'),
            array('name' => '编辑商家类型', 'url' => '/Admin/ShopType/editShopType'),
//            array('name' => '商家付款码列表', 'url' => '/Admin/Shop/barCodeList')
        )),

        // 用户相关管理
        array('name' => '用户相关管理', 'id' => 'user', 'url' => '', 'submenu' => array(
            array('name' => '用户列表', 'url' => '/Admin/User/listUser'),
//            array('name' => '用户添加', 'url' => '/Admin/User/editUser'),
//            array('name' => '用户会员卡', 'url' => '/Admin/UserCard/listUserCard'),
            array('name' => '用户优惠券', 'url' => '/Admin/UserCoupon/listUsCoupon'),
            array('name' => '用户圈值', 'url' => '/Admin/User/listUserPoint'),

        )),
        // 平台相关管理
        array('name' => '惠圈平台管理', 'id' => 'plat', 'url' => '', 'submenu' => array(
            array('name' => '惠圈平台人员列表', 'url' => '/Admin/BmStaff/listBmStaff'),
            array('name' => '编辑人员', 'url' => '/Admin/BmStaff/editBmStaff'),
            array('name' => '惠圈平台优惠券', 'url' => '/Admin/BatchCoupon/listPfCoupon'),
            array('name' => '编辑优惠券', 'url' => '/Admin/BatchCoupon/editCoupon'),
        )),
       //商圈管理
        array('name'=>'商圈管理', 'id'=>'bank', 		'url'=>'', 'submenu' => array(
            array('name' => '商圈列表', 'url' => '/Admin/Bank/listBank'),
            array('name' => '新增商圈', 'url' => '/Admin/Bank/editBank'),
        )),

        // 轮播图添加
        array('name'=>'轮播|城市管理', 'id'=>'Swiper',    'url'=>'', 'submenu' => array(
            array('name' => '轮播图管理', 'url'=>'/Admin/Swiper/swiperRoll'),
            array('name'=>'城市管理', 	'url'=>'/Admin/District/listCity'),
        )),
//        array('name'=>'广告管理', 'id'=>'Notice', 		'url'=>'', 'submenu' => array(
//            array('name' => '广告列表', 'url' => '/Admin/QNotice/listAdv'),
//            array('name' => '编辑广告', 'url' => '/Admin/QNotice/editAdv'),
//            array('name' => '分类列表', 'url' => '/Admin/QNotice/listSort'),
//            array('name' => '编辑分类', 'url' => '/Admin/QNotice/editSort'),
//        )),
//        array('name'=>'广告管理', 'id'=>'Notice', 		'url'=>'', 'submenu' => array(
//            array('name' => '广告列表', 'url' => '/Admin/QNotice/listAdv'),
//            array('name' => '编辑广告', 'url' => '/Admin/QNotice/editAdv'),
//            array('name' => '分类列表', 'url' => '/Admin/QNotice/listSort'),
//            array('name' => '编辑分类', 'url' => '/Admin/QNotice/editSort'),
//        )),
        // APP升级记录管理
        array('name'=>'APP升级记录', 'id'=>'ShopAppLog', 	'url'=>'', 'submenu' => array(
            array('name'=>'商家端升级记录', 	'url'=>'/Admin/ShopAppLog/listShopAppLog'),
            array('name'=>'编辑商家端升级记录', 		'url'=>'/Admin/ShopAppLog/editShopAppLog'),
//            array('name'=>'顾客端升级记录', 	'url'=>'/Admin/ClientAppLog/listClientAppLog'),
//            array('name'=>'编辑顾客端升级记录', 		'url'=>'/Admin/ClientAppLog/editClientAppLog'),
//            array('name'=>'PC端升级记录', 	'url'=>'/Admin/PcAppLog/listPcAppLog'),
//            array('name'=>'编辑PC端升级记录', 		'url'=>'/Admin/PcAppLog/editPcAppLog'),
        )),
    )
);
