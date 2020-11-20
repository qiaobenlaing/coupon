<?php
/**
 * Shop Controller
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\ActivityModel;
use Common\Model\BankAccountModel;
use Common\Model\BonusModel;
use Common\Model\BonusStatisticsModel;
use Common\Model\BrandModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\DistrictModel;
use Common\Model\Pager;
use Common\Model\ShopApplyEntryModel;
use Common\Model\ShopBrandRelModel;
use Common\Model\ShopDecorationModel;
use Common\Model\ShopPhotoModel;
use Common\Model\ShopStaffRelModel;
use Common\Model\ShopTradingAreaRelModel;
use Common\Model\ShopTypeModel;
use Common\Model\ShopTypeRelModel;
use Common\Model\SubAlbumModel;
use Common\Model\SubModuleModel;
use Common\Model\UserConsumeModel;
use Common\Model\UserModel;
use Org\FirePHPCore\FP;
use Common\Model\ShopModel;
use Common\Model\ShopStaffModel;
use Common\Model\CardModel;
use Common\Model\BatchCouponModel;
use Common\Model\PreShopModel;
use Common\Model\BmStaffModel;
use Think\Model;


class ShopController extends AdminBaseController {
   

    /**
     * 获得该城市下的商户
     */
    public function getCityShop() {
        if(IS_AJAX) {
            $cityIdName = I('get.cityIdName');
            $cityInfo = explode('|', $cityIdName);
            $cityName = $cityInfo[1];
            // 兼容只有cityId
            $cityId = I('get.cityId');
            if(!empty($cityId)) {
                $districtMdl = new DistrictModel();
                $cityInfo = $districtMdl->getCityInfo(array('id' => $cityId), array('name'));
                $cityName = $cityInfo['name'];
            }

            $shopMdl = new ShopModel();
            $condition = (array('city' => $cityName, 'shopStatus' => \Consts::SHOP_ENTER_STATUS_YES));
            // 获得城市下所有已经入驻的商户
            $shopList = $shopMdl->getShopList($condition, array('shopCode' => 'id', 'shopName' => 'text'));
            $shopCodeList = $shopMdl->getShopFieldList('shopCode', $condition);
            $this->ajaxSucc('', array('shopList' => $shopList, 'shopCodeList' => $shopCodeList));
        }
    }

    /**
     * 获得大店长信息
     */
    public function getBigMngInfo() {
        if(IS_AJAX) {
            $shopCode = I('post.shopCode');
            $shopStaffRelMdl = new ShopStaffRelModel();
            $bigMngInfo = $shopStaffRelMdl->getStaffInfoByShopCode($shopCode, C('STAFF_LVL.BIG_MANAGER'));
            $this->ajaxSucc('', $bigMngInfo);
        }
    }

    /**
     * 编辑商店和店员的关系
     */
    public function editShopStaffRel() {
        if(IS_GET) {
            $shopCode = I('get.shopCode');
            $shopMdl = new ShopModel();
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.shopName', 'Shop.shopCode'));
            // 获得所有大店长
            $shopStaffMdl = new ShopStaffModel();
            $bigMngList = $shopStaffMdl->getShopStaffList(array('userLvl' => C('STAFF_LVL.BIG_MANAGER')), array('staffCode', 'realName', 'mobileNbr'));
            $shopStaffRelMdl = new ShopStaffRelModel();
            // 获得该商户大店长信息
            $bigMngInfo = $shopStaffRelMdl->getStaffInfoByShopCode($shopCode, C('STAFF_LVL.BIG_MANAGER'));

            $assign = array(
                'title' => '选择大店长',
                'shopInfo' => $shopInfo,
                'bigMngList' => $bigMngList,
                'bigMngInfo' => $bigMngInfo
            );
            $this->assign($assign);
            $this->display();
        } else {
            $data = I('post.');
            $shopStaffRelMdl = new ShopStaffRelModel();
            $ret = $shopStaffRelMdl->editShopStaffRel($data);
            if($ret['code'] == C('SUCCESS')) {
                $this->ajaxSucc();
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }

    /**
     * 修改是否支持工行卡支付
     */
    public function updateIsAcceptBankCard() {
        if(IS_AJAX) {
            $data = I('get.');
            $shopMdl = new ShopModel();
            if($data['isAcceptBankCard'] == true) {
                // 检查商户是否有入账账户和入帐账户户名
                $shopInfo = $shopMdl->getShopInfo($data['shopCode'], array('addCardNo', 'addCardUserName', 'hqIcbcShopNbr'));
                if(empty($shopInfo['hqIcbcShopNbr'])) {
                    $this->ajaxError('商户入账信息不完整，无法开启工行支付');
                }
            }
            $ret = $shopMdl->editShop($data);
            if ($ret['code'] === true) {
                if($data['isAcceptBankCard'] == true) {
                    $retData = array('newClass' => 'btn-warning', 'oldClass' => 'btn-success', 'isAcceptBankCard' =>  C('NO'), 'text' => '关闭工行支付');
                } else {
                    $retData = array('newClass' => 'btn-success', 'oldClass' => 'btn-warning', 'isAcceptBankCard' =>  C('YES'), 'text' => '开启工行支付');
                }
                $this->ajaxSucc('保存成功', $retData);
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }

    /**
     * 删除商家
     * @param string $shopCode 商家编码
     */
    public function deleteShop($shopCode) {
        $shopMdl = new ShopModel();
        $shopMdl->delShop($shopCode);
        $shopStaffMdl = new ShopStaffModel();
        $shopStaffMdl->delShopStaff($shopCode);
        $batchCouponMdl = new BatchCouponModel();
        $batchCouponMdl->delBatchCoupon(array('shopCode' => $shopCode));
        $bonusMdl = new BonusModel();
        $bonusMdl->delBonus($shopCode);

        $this->redirect('Admin/Shop/listShop');
    }

    /**
     * 获得商家，根据商家名蘑菇查询
     */
    public function getShopByNameLike($shopName) {
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->getShopByNameLike($shopName);
        $this->ajaxReturn($shopList);
    }

    /**
     * 商店列表
     */
    public function listShop() {
        $shopMdl = new ShopModel();
        $preShopMdl = new PreShopModel();
        $shopTypeRelMdl = new ShopTypeRelModel();
        $condition = I('get.');
        $shopList = $shopMdl->listShop($condition, $this->pager);
        $shopStaffRelMdl = new ShopStaffRelModel();
        foreach($shopList as $k => $shop) {
            // 获得大店长的信息
            $bigManagerInfo = $shopStaffRelMdl->getStaffInfoByShopCode($shop['shopCode'], C('STAFF_LVL.BIG_MANAGER'));
            $shopList[$k]['bigMngRealName'] = $bigManagerInfo['realName'];
            $shopList[$k]['bigMngMobileNbr'] = $bigManagerInfo['mobileNbr'];
            $shopList[$k]['applyCount'] = 0;
            if($shop['shopStatus'] == \Consts::SHOP_ENTER_STATUS_NO) {
                // 获得未入驻商户提交入驻申请的数量
                $shopApplyEntryMdl = new ShopApplyEntryModel();
                $shopList[$k]['applyCount'] = $shopApplyEntryMdl->countApply(array('shopCode' => $shop['shopCode'], 'applyStatus' => \Consts::SHOP_APPLY_ENTRY_STATUS_NO_DEAL));
            }
            // 获得商户的所属类型
            $shopList[$k]['shopType'] = $shopTypeRelMdl->getShopType($shop['shopCode']);
        }
        $shopCount = $shopMdl->countShop($condition);

        $shopTypeMdl = new ShopTypeModel();
        $shopTypeList = $shopTypeMdl->getAllShopTypeList();
        $this->pager->setItemCount($shopCount);

        $assign = array(
            'route' => array(array('url' => '/Admin/Index/index', 'title' => '首页')),
            'title' => '商户列表',
            'dataList' => $shopList,
            'get' => I('get.'),
            'pager' => $this->pager,
            'shopTypeList' => $shopTypeList,
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($shopList) ? '' : $this->fetch('Shop:listShopWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 商户入驻申请
     */
    public function listApply() {
        $shopApplyEntryMdl = new ShopApplyEntryModel();
        $applyList = $shopApplyEntryMdl->listApply(I('get.'), $this->pager);
        $applyCount = $shopApplyEntryMdl->countApply(I('get.'));

        $this->pager->setItemCount($applyCount);
        $assign = array(
            'title' => '商户入驻申请',
            'dataList' => $applyList,
            'get' => I('get.'),
            'pager' => $this->pager,
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($applyList) ? '' : $this->fetch('Shop:listApplyWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    public function getKeywords() {
        $shopMdl = new ShopModel();
        $keywords = $shopMdl->getKeywords(I('get.keywords'));
        if (!empty($keywords)) {
            $this->ajaxSucc('', $keywords);
        } else {
            $this->ajaxError($keywords);
        }
    }

    /**
     * 获得商店详情
     */
    public function shopInfo() {
        $shopCode = I('get.shopCode');
        $shopMdl = new ShopModel();
        // 获得商户的信息
        $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.*'));

        $shopStaffRelMdl = new ShopStaffRelModel();
        // 获得大店长信息
        $bigMngInfo = $shopStaffRelMdl->getStaffInfoByShopCode($shopInfo['shopCode'], \Consts::SHOP_STAFF_LVL_BIG_MANAGER);

        $shopTypeMdl = new ShopTypeModel();
        // 获得所有商户类型
        $shopTypeList = $shopTypeMdl->getAllShopTypeList();
        //获得商户身份证图片
        if(!empty($shopInfo['IDcardUrl'])){
            $data = explode('||', $shopInfo['IDcardUrl']);
            $idCardPhoto = array();
            foreach ($data as $d){
                $idCardPhoto[] = (array)json_decode($d);
            }
            $shopInfo['IDcardUrl'] = array_filter($idCardPhoto);
        }
        $assign = array(
            'shopCode' => $shopCode,
            'bigMngInfo' => $bigMngInfo,
            'shopInfo' => $shopInfo,
            'title' => '商户详细信息',
            'shopTypeList' => $shopTypeList,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 商户员工列表
     */
    public function shopStaff() {
        $ShopStaffMdl = new ShopStaffModel();
        $listShopStaff = $ShopStaffMdl->listShopStaff(I('get.'), $this->pager);
        $this->assign('dataList', $listShopStaff);
        $countShopStaff = $ShopStaffMdl->countShopStaff(I('get.'));
        $this->pager->setItemCount($countShopStaff);
        $this->assign('pager', $this->pager);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($listShopStaff) ? '' : $this->fetch('ShopStaff:listShopStaffWidget');
            $this->ajaxSucc('', null, $html);
        };
    }

    /**
     * 商户会员卡
     */
    public function shopCard() {
        $cardMdl = new CardModel();
        $listCard = $cardMdl->listCard(I('get.'), $this->pager);
        $this->assign('dataList', $listCard);
        $this->assign('get', I('get.'));
        $countCard = $cardMdl->countCard(I('get.'));
        $this->pager->setItemCount($countCard);
        $this->assign('pager', $this->pager);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($listCard) ? '' : $this->fetch('Shop:cardWidget');
            $this->ajaxSucc('', null, $html);
        };
    }

    /**
     * 商户优惠券
     */
    public function shopBatchCoupon() {
        $BatchCouponMdl = new BatchCouponModel();
        $type = C('Coupon_BELONG.SHOP');
        $batchCouponList = $BatchCouponMdl->listBatchCoupon(I('get.'), $type, $this->pager);
        $countBatchCoupon = $BatchCouponMdl->countBatchCoupon(I('get.'), $type);
        $this->pager->setItemCount($countBatchCoupon);
        $assign = array(
            'dataList' => $batchCouponList,
            'get' => I('get.'),
            'nowTime' => time(),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($batchCouponList) ? '' : $this->fetch('Shop:batchCouponWidget');
            $this->ajaxSucc('', null, $html);
        };
    }

    /**
     * 商户活动
     */
    public function shopActivity() {
        $activityMdl = new ActivityModel();
        $filterData = I('get.');
        $filterData['activityBelonging'] = C('ACTIVITY_BELONGING.SHOP');
        $page = $filterData['page'] ? $filterData['page'] : 1;
        unset($filterData['page']);
        $filterData['Activity.activityName'] = array('like', '%'.$filterData['activityName'].'%');
        unset($filterData['activityName']);
        $listSpActivity = $activityMdl->getActList($filterData, array('Activity.*'), array(), 'Activity.createTime desc', Pager::DEFUALT_PAGE_SIZE, $page);
        $countSpActivity = $activityMdl->countActList($filterData, array());
        $this->pager->setItemCount($countSpActivity);
        $this->assign('dataList', $listSpActivity);
        $this->assign('get', I('get.'));
        $this->assign('pager', $this->pager);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($listSpActivity) ? '' : $this->fetch('Shop:avtivityWidget');
            $this->ajaxSucc('', null, $html);
        };
    }

    /**
     * 编辑商家基本信息
     */
    public function editBasic() {
        if(IS_GET) {
            $shopCode = I('get.shopCode');
            // 获得商家基本信息
            $shopMdl = new ShopModel();
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.*'));

            // 获得所有商户类型
            $shopTypeMdl = new ShopTypeModel();
            $shopTypeList = $shopTypeMdl->getAllShopTypeList();
            // 获得总店列表
            $shopTypeRelMdl = new ShopTypeRelModel();
            $shopCodeArr = $shopTypeRelMdl->getFieldList(array('ShopType.typeValue' => array('IN', array(\Consts::SHOP_TYPE_PLAT, \Consts::SHOP_TYPE_ICBC))), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
            if(empty($shopCodeArr)){
                $shopCodeArr[] = '0';
            }
            if($shopCode && !in_array($shopCode, $shopCodeArr)){
                $shopCodeArr[] = $shopCode;
            }
            $shopCodeArr = array_unique($shopCodeArr);
            $parentList = $shopMdl->getShopList(array('shopCode' => array('NOTIN', $shopCodeArr), 'parentCode' => ''), array('shopCode', 'shopName'));
            $assign = array(
                'title' => '基本信息',
                'shopInfo' => $shopInfo,
                'shopTypeList' => $shopTypeList,
                'parentList' => $parentList,
            );
            $this->assign($assign);
            $this->display();
        } else {
            $this->ajaxError('还没开发好');
        }
    }

    /**
     * 新增或者编辑商家
     */
    public function editShop() {
        $starMdl = new ShopTradingAreaRelModel();
        $shopTypeRelMdl = new ShopTypeRelModel();
        if(IS_GET) {
            $shopCode = I('get.shopCode');
            $shopMdl = new ShopModel();
            $title  = $shopCode ? '修改商户信息' : '添加商户信息';
            // 获得商家信息
            $shopInfo = $shopMdl->getShopInfo($shopCode, array('Shop.*'));
            // 获得商户所属类型
            $shopType = $shopTypeRelMdl->getShopType($shopInfo['shopCode']);
            $strShopType = '';
            foreach($shopType as $v) {
                $strShopType .= "$v,";
            }

            // 设置商家的工行地区号
            $shopInfo['icbcCityNbr'] = substr($shopInfo['hqIcbcShopNbr'], 0, 4);

            $temp = array('deliveryFee', 'takeoutRequirePrice'); // （配送费用，外卖要求金额）
            foreach($temp as $v) {
                $shopInfo[$v] = $shopInfo[$v] / C('RATIO'); // 分转化为元
            }

            $districtMdl = new DistrictModel();
            $city = $shopInfo['city'];
            // 获得商户所在城市的信息
            $cityInfo = $districtMdl->getCityInfo(array('name' => array('like', "%$city%")), array('id'));

            $subModuleMdl = new SubModuleModel();
            $ccData = array('parentModuleId' => 3);
            if($shopCode) {
                $ccData['SubModule.cityId'] = $cityInfo['id'];
            }
            $tradingAreaList = $subModuleMdl->getSubModuleList(array('SubModule.id', 'SubModule.title', 'District.name'), $ccData); // 3 代表商圈(来自 IndexModule 表)
            $linkedTradingArea = $starMdl->listShopTradingAreaRel(array('subModuleId'), array('shopCode' => $shopCode));

            $shopDecorationMdl = new ShopDecorationModel();
            $subAlbumMdl = new SubAlbumModel();
            // 获得商户的背景图片
            $shopInfo['decoration'] = $shopDecorationMdl->getShopDecorationList($shopCode);
            // 获得商户的商品相册图片
            $shopInfo['product'] = $subAlbumMdl->listSubAlbum($shopCode);

            $shopStaffRelMdl = new ShopStaffRelModel();
            // 获得该商户大店长信息
            $bigMngInfo = $shopStaffRelMdl->getStaffInfoByShopCode($shopCode, C('STAFF_LVL.BIG_MANAGER'));

            $userMdl = new UserModel();
            // 统计使用商家邀请码注册的用户数
            $shopInfo['regNbr'] = $userMdl->countRegNbrUsedShopInviteCode($shopInfo['inviteCode']);
            // 统计使用商家邀请码注册并消费的用户数
            $shopInfo['regACNbr'] = $userMdl->countRegACNbrUsedShopInviteCode($shopInfo['inviteCode']);
            // 计算奖励值
            $shopInfo['rewardPoint'] = $shopInfo['regNbr'] * $shopInfo['regRewardCoefficient'] + $shopInfo['regACNbr'] * $shopInfo['rACRewardCcoefficient'];

            // 获得所有商户类型
            $shopTypeMdl = new ShopTypeModel();
            $shopTypeList = $shopTypeMdl->getAllShopTypeList();

            // 获得所有品牌
            $brandMdl = new BrandModel();
            $brandList = $brandMdl->getBrandList(array(), array('*'));

            // 获得商户所属的品牌
            $shopBrandRelMdl = new ShopBrandRelModel();
            $shopInfo['brandList'] = $shopBrandRelMdl->getRelList(array('ShopBrandRel.shopCode' => $shopInfo['shopCode']), array('Brand.brandId'));

            // 获得总店列表
            $shopCodeArr = $shopTypeRelMdl->getFieldList(array('ShopType.typeValue' => array('IN', array(\Consts::SHOP_TYPE_PLAT, \Consts::SHOP_TYPE_ICBC))), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
            if(empty($shopCodeArr)){
                $shopCodeArr[] = '0';
            }
            if($shopCode && !in_array($shopCode, $shopCodeArr)){
                $shopCodeArr[] = $shopCode;
            }
            $shopCodeArr = array_unique($shopCodeArr);
            $parentList = $shopMdl->getShopList(array('shopCode' => array('NOTIN', $shopCodeArr), 'parentCode' => ''), array('shopCode', 'shopName'));

            $districtMdl = new DistrictModel();
            // 获得浙江省的所有市级地及其地区号
            $areaNbrList = $districtMdl->getCityList('上海市');
        //   $yiwu = $districtMdl->getCityInfo(array('name' => '义乌市'), array('name', 'areaNbr'));
          //  $areaNbrList[] = $yiwu;

            // 判断用户是否有权限添加，修改商户的入账资料
            $HqStaffInfo = $this->user;
            $cmsba = ($HqStaffInfo['userLvl'] == \Consts::HQ_STAFF_TYPE_ADMIN) && (in_array($HqStaffInfo['mobileNbr'], array('13588305490', '13738129650', '13738157214', '18767174523')) && (in_array($HqStaffInfo['realName'], array('侯丹丹', '王贤君', '张彪', '汤杰')))) ? true : false;
            // IDcardUrl字段string转array
            $shopInfo['IDcardUrl'] = empty($shopInfo['IDcardUrl']) ? array() : explode('|', $shopInfo['IDcardUrl']);

//            if(!empty($shopInfo['IDcardUrl'])){
//                $shopInfo['orignIDcardUrl'] = $shopInfo['IDcardUrl'];
//                $data = explode('||', $shopInfo['IDcardUrl']);
//                $idCardPhoto = array();
//                foreach ($data as $d){
//                    $idCardPhoto[] = (array)json_decode(htmlspecialchars_decode(htmlspecialchars_decode($d)));
//                }
//                $shopInfo['IDcardUrl'] = array_filter($idCardPhoto);
//            }
            $assign = array(
                'shopInfo' => $shopInfo,
                'strShopType' => $strShopType,
                'bigMngInfo' => $bigMngInfo,
                'page' => I('get.page'),
                'title' => $title,
                'shopTypeList' => $shopTypeList,
                'brandList' => $brandList,
                'parentList' => $parentList,
                'tradingAreaList' => $tradingAreaList,
                'linkedTradingArea' => $linkedTradingArea,
                'areaNbrList' => $areaNbrList,
                'cmsba' => $cmsba,
            );

            $this->assign($assign);
            $this->display();
        } else {
			
            $data = I('post.');
            // 保存商户所属的类型
            if(empty($data['type'])) {
                $this->ajaxError(C('SHOP.TYPE_EMPTY'));
            }
            $shopTypeList = $data['type'];
            unset($data['type']);

            // 保存商户所属的品牌
            $brandIdList = $data['brandId'];
            unset($data['brandId']);

            // 商户所属的商圈
            $linkedShopTrading = $data['linkedShopTrading'];
            unset($data['linkedShopTrading']);

            if(empty($data['shopCode'])) { // 新添商店，获得输入的大店长信息
                $bigMngRealName = $data['bigMngRealName'];
                $bigMngMobileNbr = $data['bigMngMobileNbr'];
                unset($data['bigMngRealName']);
                unset($data['bigMngMobileNbr']);
            } else { // 修改商家信息，获得选择的大店长编码
                $bigMngCode = $data['bigMngCode'];
                unset($data['bigMngCode']);
            }

            $temp = array('onlinePaymentDiscountUpperLimit', 'takeoutRequirePrice', 'deliveryFee');
            foreach($temp as $v) {
                if(isset($data[$v])) {
                    $data[$v] = $data[$v] * \Consts::HUNDRED;
                }
            }
            $data['onlinePaymentDiscount'] = $data['onlinePaymentDiscount'] * C('DISCOUNT_RATIO');
            if($data['shopOpeningTime']){
                $businessHours = array();
                foreach($data['shopOpeningTime'] as $sk => $sv){
                    if($sv && $data['shopClosedTime'][$sk]){
                        $businessHours[] = array(
                            'open' => $sv,
                            'close' => $data['shopClosedTime'][$sk]
                        );
//                        if(empty($shopOpeningTime)){
//                            $shopOpeningTime = $sv;
//                            $shopClosedTime = $data['shopClosedTime'][$sk];
//                        }
                    }
                }
                unset($data['shopOpeningTime']);
                unset($data['shopClosedTime']);
                if($businessHours){
                    $data['businessHours'] = json_encode($businessHours);
                }
            }
//            $data['shopOpeningTime'] = isset($shopOpeningTime) ? $shopOpeningTime : '';
//            $data['shopClosedTime'] = isset($shopClosedTime) ? $shopClosedTime : '';
//            $this->ajaxError('', $data);

            // 提取出商家的背景图片的相关信息
            $decorationCodeArr = $data['decorationCode'];
            $imgUrlArr = $data['imgUrl'];
            $titleArr = $data['title'];
            $shortDesArr = $data['decoShortDes'];
            $temp = array('decorationCode', 'imgUrl', 'title', 'decoShortDes');
            foreach($temp as $v) {
                unset($data[$v]);
            }

            // IDcardUrl字段array转string
            if($data['IDcardUrl']) {
                $data['IDcardUrl'] = implode('|', $data['IDcardUrl']);
            }

            // 保存商户信息
            $shopMdl = new ShopModel();
            $ret = $shopMdl->editShop($data);

            if ($ret['code'] === C('SUCCESS')) {
                // 保存商户所属的类型
                $shopTypeRelMdl->saveShopTypeRel($shopTypeList, $ret['shopCode']);
                // 保存商户所属的品牌
                $this->saveShopBrand($ret['shopCode'], $brandIdList);
                // 保存商户所属的商圈
                $starMdl->addLinkedShopTradingAreaRel($ret['shopCode'], $linkedShopTrading);
                // 保存商户的环境图片
                $this->saveShopDecImg($ret['shopCode'], $decorationCodeArr, $imgUrlArr, $titleArr, $shortDesArr);

                if(empty($data['shopCode'])) { // 新建商户时
                    // 保存输入的大店长信息
                    $shopStaffMdl = new ShopStaffModel();
                    $shopStaffInfo = $shopStaffMdl->getShopStaffInfo(array('mobileNbr' => $bigMngMobileNbr), array('staffCode'));
                    $shopStaffInfo['realName'] = $bigMngRealName;
                    $shopStaffInfo['mobileNbr'] = $bigMngMobileNbr;
                    $shopStaffInfo['userLvl'] = C('STAFF_LVL.BIG_MANAGER');
                    $editShopStaffRet = $shopStaffMdl->editShopStaff($shopStaffInfo);
                    if($editShopStaffRet['code'] === C('SUCCESS')) {
                        $shopStaffRelMdl = new ShopStaffRelModel();
                        // 添加商店和大店长的关系
                        $shopStaffRelMdl->editShopStaffRel(array('staffCode' => $editShopStaffRet['staffCode'], 'shopCode' => $ret['shopCode']));
                       // $this->ajaxSucc('保存成功', array('shopCode' => $ret['shopCode']));
                    } else {
                        $this->ajaxError($editShopStaffRet['code']);
                    }
                } else {
                    // 获得商店原大店长信息
                    $shopStaffRelMdl = new ShopStaffRelModel();
                    $bigMngInfo = $shopStaffRelMdl->getStaffInfoByShopCode($data['shopCode'], C('STAFF_LVL.BIG_MANAGER'));
                    if(empty($bigMngInfo)) {
                        // 添加商店和大店长的关系
                        $shopStaffRelMdl->editShopStaffRel(array('staffCode' => $bigMngCode, 'shopCode' => $data['shopCode']));
                    } else {
                        if($bigMngInfo['staffCode'] != $bigMngCode) {
                            // 商店更换大店长
                            $shopStaffRelMdl->editShopStaffRel(array('id' => $bigMngInfo['id'], 'staffCode' => $bigMngCode, 'shopCode' => $bigMngInfo['shopCode']));
                        }
                    }
                }
             
				// $this->ajaxSucc('保存成功', array('shopCode' => $ret['shopCode'],$html='editShop'));
				
            } else {
                $this->ajaxError($ret['code']);
            }
			$this->redirect('Admin/Shop/listShop');
			
        }
    }
	

	

    /**
     * 保存商户所属的品牌
     * @param string $shopCode 商家明白
     * @param string $brandIdList 商圈ID一维数组
     */
    private function saveShopBrand($shopCode, $brandIdList) {
        if(!empty($brandIdList)) {
            $shopBrandRelMdl = new ShopBrandRelModel();
            // 删除未选择的品牌与商户的关系
            $shopBrandRelMdl->delRel(array('brandId' => array('NOTIN', $brandIdList), 'shopCode' => $shopCode));

            // 保存商户与品牌的关系
            foreach($brandIdList as $brandId) {
                $info = $shopBrandRelMdl->getRelInfo(array('ShopBrandRel.shopCode' => $shopCode, 'ShopBrandRel.brandId' => $brandId), array('ShopBrandRel.shopBrandRelId'));
                if(empty($info)) {
                    $shopBrandRelMdl->editRel(array('brandId' => $brandId, 'shopCode' => $shopCode));
                }
            }
        }
    }

    /**
     * 保存商户的背景图片
     * @param string $shopCode 商家编码
     * @param array $decorationCodeArr 编码一维数组
     * @param array $imgUrlArr 图片url一维数组
     * @param array $titleArr 图片标题一维数组
     * @param array $shortDesArr 图片简短描述一维数组
     */
    private function saveShopDecImg($shopCode, $decorationCodeArr, $imgUrlArr, $titleArr, $shortDesArr) {
        // 删除页面上已删除的图片
        $condition = array('shopCode' => $shopCode);
        if($decorationCodeArr) {
            $condition['decorationCode'] = array('NOTIN', $decorationCodeArr);
        }
        $shopDecorationMdl = new ShopDecorationModel();
        $shopDecorationMdl->delDecoration($condition);

        foreach($decorationCodeArr as $k => $v) {
            $shopDecorationData = array(
                'decorationCode' => $decorationCodeArr[$k],
                'imgUrl' => $imgUrlArr[$k],
                'title' => $titleArr[$k],
                'shortDes' => $shortDesArr[$k],
                'shopCode' => $shopCode,
            );
            // 修改或添加图片信息
            $shopDecorationMdl->editShopDecoration($shopDecorationData);
        }
    }

    public function do_shop_upload(){
        $config = array(
            'mimes'         =>  array(), //允许上传的文件MiMe类型
            'maxSize'       =>  5242880, //上传的文件大小限制(以字节为单位)(0-不做限制)5M
            'exts'          =>  array('jpg', 'gif', 'png', 'jpeg'), //允许上传的文件后缀
            'autoSub'       =>  true, //自动子目录保存文件
            'subName'       =>  array('date', 'Ymd'), //子目录创建方式，[0]-函数名，[1]-参数，多个参数使用数组
            'rootPath'      =>  '',
            'savePath'      =>  './Public/Uploads/', //保存路径
            'saveName'      =>  array('uniqid', ''), //上传文件命名规则，[0]-函数名，[1]-参数，多个参数使用数组
        );
        $upload = new \Think\Upload($config);// 实例化上传类

        $shopCode = I('post.shopCode'); // 商家编码
        $upload_type = I('post.upload_type'); // 上传的图片类型
        $shopType = I('post.shopType'); // 商家类型
        $info = $upload->upload(); // 上传图片

        if(!$info) {
            // 上传错误提示错误信息
            echo json_encode(array('code' => $upload->getError()));
            exit;
        } else {  // 上传成功，获取上传文件信息
            $shopMdl = new ShopModel();
            $preShopMdl = new PreShopModel();
            $url = substr($info['userfile']['savepath'], 1).$info['userfile']['savename'];
            if($upload_type == 'logo'){ // 图片类型为商家logo
//                $shopMdl = new ShopModel();
//                $ret = $shopMdl->setShopLogo($shopCode, $url);
//                $res['code'] = $ret;
                $res['code'] = true;
            }elseif($upload_type == 'photo'){ // 图片类型为商户背景图
                $res['code'] = true;
//                $shopDecorationMdl = new ShopDecorationModel();
//                $res = $shopDecorationMdl->addShopDecoration($shopCode, '', '', '', $url, '');
            }elseif($upload_type == 'bankcard'){ // 图片类型为银行卡图片
                if(!empty($shopCode)){
                    if($shopType == 'shop'){
                        $res = $shopMdl->setShopFeild(array('shopCode' => $shopCode), array('bankCardUrl' => $url));
                    }elseif($shopType == 'preShop'){
                        $res = $preShopMdl->setPreShopFeild(array('shopCode' => $shopCode), array('bankCardUrl' => $url));
                    }
                }else {
                    $res['code'] = true;
                }
            }elseif($upload_type == 'license'){ // 图片类型为商户营业执照图片
                if(!empty($shopCode)){
                    if($shopType == 'shop'){
                        $res = $shopMdl->setShopFeild(array('shopCode' => $shopCode), array('licenseUrl' => $url));
                    }elseif($shopType == 'preShop'){
                        $res = $preShopMdl->setPreShopFeild(array('shopCode' => $shopCode), array('licenseUrl' => $url));
                    }
                }else {
                    $res['code'] = true;
                }
            }elseif($upload_type == 'protocol'){ // 图片类型为惠圈协议图片
                if(!empty($shopCode)){
                    if($shopType == 'shop'){
                        $res = $shopMdl->setShopFeild(array('shopCode' => $shopCode), array('protocolUrl' => $url));
                    }elseif($shopType == 'preShop'){
                        $res = $preShopMdl->setPreShopFeild(array('shopCode' => $shopCode), array('protocolUrl' => $url));
                    }
                }else {
                    $res['code'] = true;
                }
            } elseif($upload_type == 'idcard') { // 图片类型为开户人身份证图片
                $data['name'] = $info['userfile']['name'];
                $data['url'] = $url;
                if($info['userfile']['ext'] == 'jpeg') {
                    $data['id'] = substr($info['userfile']['savename'], 0, -5);
                } else {
                    $data['id'] = substr($info['userfile']['savename'], 0, -4);
                }
                $urlMessage = '||'.json_encode($data);
                $res['code'] = true;
//                if(empty($shopCode)) {
//                    $res['code'] = true;
//                } else {
//                    if($shopType == 'shop') {
//                        $iDcardUrlInfo = $shopMdl->getShopFieldList('IDcardUrl', array('shopCode' => $shopCode));
//                        if(!empty($iDcardUrlInfo[0])){
//                            $urlMessage = $urlMessage.$iDcardUrlInfo[0];
//                        }
//                        $res = $shopMdl->setShopFeild(array('shopCode' => $shopCode), array('IDcardUrl' => $urlMessage));
//                    } elseif($shopType == 'preShop') {
//                        $iDcardUrlInfo = $preShopMdl->getPreShopInfo(array('shopCode' => $shopCode), 'IDcardUrl');
//                        if(!empty($iDcardUrlInfo['IDcardUrl'])) {
//                            $urlMessage = $urlMessage.$iDcardUrlInfo['IDcardUrl'];
//                        }
//                        $res = $preShopMdl->setPreShopFeild(array('shopCode' => $shopCode), array('IDcardUrl' => $urlMessage));
//                    }
//                }
            }else{
                $shopPhoto = new ShopPhotoModel();
                $res = $shopPhoto->editSubAlbumPhoto(array('subAlbumCode'=>I('post.code'),'url'=>$url,'createTime'=>date('Y-m-d H:i:s',time())));
            }
            if($res['code'] == true){
                $return_json = array(
                    'code' => 200,
                    'msg' => 'upload success',
                    'upload_type' => $upload_type,
                    'url'=> $url,
                    'shopCode' => $shopCode,
                    'shopType' => $shopType,
                    'urlMessage' => isset($urlMessage)?$urlMessage:'',
                    'data' => isset($data)?$data:'',
                    'decorationCode' => isset($res['decorationCode'])?$res['decorationCode']:'',
                    'photoCode' => isset($res['photoCode'])?$res['photoCode']:0
                );
            }else{
                $return_json = array('code'=> 400, 'msg'=>'update avatar error');
            }
            echo json_encode($return_json);
            exit;
        }
    }

    public function delDecoration(){
        $decorationCode = I('post.decorationCode');
        $shopDecorationMdl = new ShopDecorationModel();
        $ret = $shopDecorationMdl->delShopDecoration($decorationCode);
        $return_json = array(
            'code'=>200,
            'response'=>array(
                'result'=>$ret['code'],
                'decorationCode'=>$decorationCode,
            )
        );
        echo json_encode($return_json);
    }

    /**
     * 删除身份证图片
     */
    public function delIdCard() {
        $id = I('post.idCode');
        $shopCode = I('post.shopCode');
        $shopType = I('post.shopType');
        $shopMdl = new ShopModel();
        $preShopMdl = new PreShopModel();
        if($shopType == 'shop'){
            $iDcardUrlInfo = $shopMdl->getShopFieldList('IDcardUrl', array('shopCode' => $shopCode));
            $data = explode('||', $iDcardUrlInfo[0]);
        }elseif ($shopType == 'preShop'){
            $iDcardUrlInfo = $preShopMdl->getPreShopInfo(array('shopCode' => $shopCode), 'IDcardUrl');
            $data = explode('||', $iDcardUrlInfo['IDcardUrl']);
        }
        foreach ($data as $d){
            $data[json_decode(htmlspecialchars_decode(htmlspecialchars_decode($d)))->id] = $d;
            if($id == json_decode(htmlspecialchars_decode(htmlspecialchars_decode($d)))->id){
                unset($data[json_decode(htmlspecialchars_decode(htmlspecialchars_decode($d)))->id]);
            }
            if(!empty($data[json_decode(htmlspecialchars_decode(htmlspecialchars_decode($d)))->id])){
                $urlMessage .= '||'.$data[json_decode(htmlspecialchars_decode(htmlspecialchars_decode($d)))->id];
            }
        }
        if($shopType == 'shop'){
            $res = $shopMdl->setShopFeild(array('shopCode' => $shopCode), array('IDcardUrl' => $urlMessage));
        }elseif ($shopType == 'preShop'){
            $res = $preShopMdl->setPreShopFeild(array('shopCode' => $shopCode), array('IDcardUrl' => $urlMessage));
        }
        $return_json = array(
            'code'=>200,
            'response'=>array(
                'result'=>$res['code'],
                'urlMessage' => $urlMessage,
                'idCode'=>$id,
            )
        );
        echo json_encode($return_json);
    }
    /**
     * 修改商店状态
     */
    public function changeStatus() {
        $shopMdl = new ShopModel();
        $shopCode = I('get.shopCode');
        $status = I('get.status');
        $res = $shopMdl->changeShopStatus($shopCode, $status);
        if ($res === true) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($res);
        }
    }

    /**
     * 修改商店入驻状态
     */
    public function changeShopEnterStatus() {
        $data = I('get.');
        $shopMdl = new ShopModel();
        $res = $shopMdl->editShop($data);
        if ($res['code'] === C('SUCCESS')) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($res['code']);
        }
    }

    /**
     * 修改商户申请入驻的状态
     */
    public function editApplyStatus() {
        $shopApplyEntryMdl = new ShopApplyEntryModel();
        $id = I('get.id');
        $condition = array('id' => $id);
        $data = array('id' => $id, 'applyStatus' => I('get.applyStatus'));
        $res = $shopApplyEntryMdl->editApplyEntry($condition, $data);
        if ($res['code'] === C('SUCCESS')) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($res);
        }
    }

    /**
     * 设置分析商户的条件
     * @param string $startTime 开始时间
     * @param string $endTime 结束时间
     * @param int $type 商家类型
     * @param int $shopStatus 商家入驻类型
     * @param int $isAcceptBankCard 是否接受银行卡支付
     * @param string $city 城市
     * @param string $province 省级地区
     * @param string $district 县级地区
     * @return array {'totalCon', 'incCon'}
     */
    private function setAnalysisShopCon($startTime, $endTime, $type, $shopStatus, $isAcceptBankCard, $city, $province, $district) {
        if($startTime && !$endTime) { // 只有开始时间
            // 设置结束时间为开始时间的7天后
            $endTime = date('Y-m-d', strtotime($startTime) + 7 * 86400);
        } elseif(!$startTime && $endTime) { // 只有结束时间
            // 设置开始时间为结束时间的7天前
            $startTime = date('Y-m-d', strtotime($endTime) - 7 * 86400);
        } elseif(!$startTime && !$endTime) { // 开始时间和结束时间都没有
            // 设置开始时间为当前时间的7天前
            $startTime = date('Y-m-d', time() - 7 * 86400);
            // 设置结束时间为当前时间
            $endTime = date('Y-m-d');
        }
        $startTime .= ' 00:00:00';
        $endTime .= ' 23:59:59';
        $shopTypeRelMdl = new ShopTypeRelModel();
        if($type !== '') {
            $shopCodeArr = $shopTypeRelMdl->getFieldList(array('ShopType.typeValue' => array('EQ', $type)), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
            if(empty($shopCodeArr)){
                $shopCodeArr = array('0');
            }
            $shopCodeArr = array_unique($shopCodeArr);
            // 设置商家行业类型
            $conditionTotal['Shop.shopCode'] = array('in', $shopCodeArr);
            $conditionInc['Shop.shopCode'] = array('in', $shopCodeArr);
        }
        if($shopStatus !== '') {
            // 设置商家入驻状态
            $conditionTotal['shopStatus'] = $conditionInc['shopStatus'] = $shopStatus;
        }
        if($isAcceptBankCard !== '') {
            // 设置是否支持银行卡支付
            $conditionTotal['isAcceptBankCard'] = $conditionInc['isAcceptBankCard'] = $isAcceptBankCard;
        }
        // 设置商家区域
        $placeTem = array('province', 'city', 'district');
        foreach($placeTem as $v) {
            if(!empty($$v)) {
                $conditionTotal[$v] = $conditionInc[$v] = $$v;
            }
        }

        $conditionTotal['createDate'] = array('ELT', $endTime); // 设置创建时间为结束时间之前
        $conditionInc['createDate'] = array('between', array($startTime, $endTime)); // 设置创建时间为开始时间和结束时间之间
        return array(
            'totalCon' => $conditionTotal, // 分析总量的条件
            'incCon' => $conditionInc, // 分析增量的条件
        );
    }

    /**
     * 商店统计分析
     */
    public function analysisShop() {
        $data = I('get.');
        $type = $data['type']; // 商家行业类型
        $isAcceptBankCard = $data['isAcceptBankCard']; // 是否接受银行卡支付
        $con = $this->setAnalysisShopCon($data['startTime'], $data['endTime'], $data['type'], $data['shopStatus'], $isAcceptBankCard, $data['city'], $data['province'], $data['district']);
        $shopMdl = new ShopModel();
        $statisticsData = array(
            \Consts::SHOP_TYPE_FOOD => array('name' => '美食', 'totalAmount' => 0, 'incAmount' => 0),
            \Consts::SHOP_TYPE_BEAUTY => array('name' => '丽人', 'totalAmount' => 0, 'incAmount' => 0),
            \Consts::SHOP_TYPE_FITNESS => array('name' => '健身', 'totalAmount' => 0, 'incAmount' => 0),
            \Consts::SHOP_TYPE_ENTERTAINMENT => array('name' => '娱乐', 'totalAmount' => 0, 'incAmount' => 0),
            \Consts::SHOP_TYPE_OTHER => array('name' => '其他', 'totalAmount' => 0, 'incAmount' => 0),
            \Consts::SHOP_TYPE_UNKNOWN => array('name' => '未知', 'totalAmount' => 0, 'incAmount' => 0),
        );
        $total = 0;
        $inc = 0;
        if($type !== '') {
            if(isset($statisticsData[$type])){
                // 获得总数分析数据
                $typeTotalAnalysis = $shopMdl->analysisShopType($con['totalCon']);
                // 获得增加数量的分析数据
                $typeIncAnalysis = $shopMdl->analysisShopType($con['incCon']);

                $statisticsData[$type]['totalAmount'] = intval($typeTotalAnalysis,10);
                $total += intval($typeTotalAnalysis,10);

                $statisticsData[$type]['incAmount'] = intval($typeIncAnalysis,10);
                $inc += intval($typeIncAnalysis,10);
            }
        }else{
            $shopTypeRelMdl = new ShopTypeRelModel();
            foreach($statisticsData as $k => $v) {
                $shopCodeArr = $shopTypeRelMdl->getFieldList(array('ShopType.typeValue' => array('EQ', $k)), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
                if(empty($shopCodeArr)){
                    $shopCodeArr = array('0');
                }
                $shopCodeArr = array_unique($shopCodeArr);
                // 设置商家行业类型
                $con['totalCon']['Shop.shopCode'] = array('in', $shopCodeArr);
                $con['incCon']['Shop.shopCode'] = array('in', $shopCodeArr);
                // 获得总数分析数据
                $typeTotalAnalysis = $shopMdl->analysisShopType($con['totalCon']);
                // 获得增加数量的分析数据
                $typeIncAnalysis = $shopMdl->analysisShopType($con['incCon']);

                $statisticsData[$k]['totalAmount'] = intval($typeTotalAnalysis,10);
                $total += intval($typeTotalAnalysis,10);

                $statisticsData[$k]['incAmount'] = intval($typeIncAnalysis,10);
                $inc += intval($typeIncAnalysis,10);
            }
        }

        $shopTypeMdl = new ShopTypeModel();
        // 获得所有商家类型
        $shopTypeList = $shopTypeMdl->getAllShopTypeList();

        $assign = array(
            'route' => array(array('url' => '/Admin/Index/index', 'title' => '首页')),
            'title' => '商户统计分析',
            'get' => I('get.'),
            'sumData' => array('totalAmount' => $total, 'incAmount' => $inc),
            'data' => $statisticsData,
            'shopTypeList' => $shopTypeList,
        );

        $this->assign($assign);
        $this->display();
    }

    public function listAlbumPhoto(){
        if(IS_GET) {
            $code = I('get.code');
            $shopPhotoMdl = new ShopPhotoModel();
            $subAlbumMdl = new SubAlbumModel();
            $subAlbum = $subAlbumMdl->getSubAlbumInfo($code);
            $subAlbum['code'] = $code;
            $subAlbum['photo'] = $shopPhotoMdl->listPhoto($code);
            $assign = array(
                'title'=>'相册编辑',
                'data'=>$subAlbum
            );
            $this->assign($assign);
            $this->display();
        }else{
            $data = I('post.');
            $ret['code'] = true;
            $shopPhotoMdl = new ShopPhotoModel();
            foreach($data['title'] as $dk=>$dv){
                if($dv || $data['des'][$dk] || $data['price'][$dk]){
                    $da = array(
                        'code'=>$dk,
                        'title'=>$dv,
                        'price'=>$data['price'][$dk]*100,
                        'des'=>$data['des'][$dk],
                    );
                    $ret = $shopPhotoMdl->editSubAlbumPhoto($da);
                }
            }
            if ($ret['code'] == true) {
                $this->ajaxSucc('保存成功！');
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }

    public function addAlbum(){
        $name = I('post.name');
        $shopCode = I('post.shopCode');
        $subAlbumMdl = new SubAlbumModel();
        $ret = $subAlbumMdl->editSubAlbum(array('name'=>$name,'shopCode'=>$shopCode));
        $ret['name'] = $name;
        $this->ajaxReturn($ret);
    }

    public function delAlbum(){
        $code = I('post.code');
        $subAlbumMdl = new SubAlbumModel();
        $ret = $subAlbumMdl->delSubAlbum($code);
        $return_json = array(
            'code'=>200,
            'response'=>array(
                'result'=>$ret['code'],
                'code'=>$code,
            )
        );
        echo json_encode($return_json);
    }

    public function delProduct(){
        $code = I('post.code');
        $shopPhotoMdl = new ShopPhotoModel();
        $ret = $shopPhotoMdl->delPhoto($code);
        $return_json = array(
            'code'=>200,
            'response'=>array(
                'result'=>$ret['code'],
                'code'=>$code,
            )
        );
        echo json_encode($return_json);
    }

    //把 shopOpeningTime 和 shopClosedTime 同步到 businessHours
    public function updateBusinessHours(){
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->getShopList(array(), array('shopCode', 'shopOpeningTime', 'shopClosedTime'));
        foreach($shopList as $v){
            $updateInfo = array(
                'businessHours' => json_encode(array(array(
                    'open' => substr($v['shopOpeningTime'], 0, 5),
                    'close' => substr($v['shopClosedTime'], 0, 5)
                )))
            );
            $shopMdl->updateShop($v['shopCode'], $updateInfo);
        }
    }

    public function dealUserBonus($page = 1){
        $page = ($page - 1) * 50;
        $userMdl = new ThinkAuthUserModel();

        $userList = $userMdl->query("select userCode,mobileNbr,inviteCode from User where inviteCode <> '' AND registerTime < '2015-12-25 00:00:00' order by registerTime asc limit ".$page.", 50");
        $userMobileNbrArr = array();
        foreach($userList as $v){
            $bsMdl = new BonusStatisticsModel();
            $userConsumeMdl = new UserConsumeModel();
            $bankAccountMdl = new BankAccountModel();

            //用户已经得到的平台红包总额
            $platBonus = 0;
            $notUsePlatBonus = $bsMdl->getUserBonusStatistics($v['userCode'], C('HQ_CODE'));
            if($notUsePlatBonus){
                $platBonus += $notUsePlatBonus['totalValue'];
            }
            $usedPlatBonus = $userConsumeMdl->platBonusConsumeAmount(array('consumerCode' => $v['userCode'], 'location' => C('HQ_CODE'), 'status' => 3));
            if(!empty($usedPlatBonus)){
                $usedPlatBonus = $usedPlatBonus * C('RATIO');
                $platBonus += $usedPlatBonus;
            }
            $receivedCount = ($platBonus - $platBonus % 1000) / 1000;

            //用户应该要得到的红包总额
            $inviteList = $userMdl->listUser(array('userCode' => array('NEQ', $v['userCode']), 'recomNbr' => $v['inviteCode']), $this->getPager(0), array('userCode'));
            $shouldCount = 0;
            foreach($inviteList as $iv){
                //得到被邀请人第一次绑的卡号
                $bankCard = $bankAccountMdl->getFirstBankCard($iv['userCode']);
                //获取这个被邀请人的这个卡号在他绑之前的绑卡人数
                if($bankCard && $bankCard['bankCard']){
                    if($bankCard['createTime'] > '2015-12-25 00:00:00'){
                        $condition = array(
                            'BankAccount.userCode' => array('neq', $iv['userCode']),
                            'BankAccount.bankCard' => $bankCard['bankCard'],
                            'BankAccount.createTime' => array('between', array('2015-12-25 00:00:00', $bankCard['createTime'])),
                            'BankAccount.status' => array('in', array(\Consts::BANKACCOUNT_STATUS_SIGNED, \Consts::BANKACCOUNT_STATUS_TERMINATE))
                        );
                        $bankAccountCount = $bankAccountMdl->getBankAccountCount($condition, array(), 'DISTINCT(userCode)');
                        if($bankAccountCount <= 3){
                            $shouldCount += 1;
                        }
                    }
                }

                $bankAccountCount = $bankAccountMdl->getBankAccountCount(array('BankAccount.createTime' => array('between', array('2015-10-26 00:00:00', '2015-12-24 23:59:59')), 'userCode' => $iv['userCode'], 'status' => array('in', array(\Consts::BANKACCOUNT_STATUS_SIGNED, \Consts::BANKACCOUNT_STATUS_TERMINATE))), array(), 'DISTINCT(userCode)');
                if($bankAccountCount > 0){
                    $shouldCount += 1;
                }
            }
            if($shouldCount > $receivedCount){
                $userMobileNbrArr[] = array('mobileNbr' => $v['mobileNbr'], 'shouldCount' => $shouldCount, 'receivedCount' => $receivedCount);
            }
        }
        var_dump($userMobileNbrArr);
        return $userList;
    }

    /**
     * 导出符合条件的excel表格
     */
    public function exportShop() {
        $shopMdl = new ShopModel();
        $preShopMdl = new PreShopModel();
        $bmStaffMdl = new BmStaffModel();
        $shopStaffRelMdl = new ShopStaffRelModel();
        $shopList = $shopMdl->listShop(I('get.'), $this->pager);
        if(empty($shopList)){
            $url = '/Admin/Shop/listShop';
            header('content-type:text/html;charset=utf-8;');
            echo '<script>alert("没有符合条件的数据！");</script>';
            echo "<SCRIPT LANGUAGE=\"JavaScript\">location.href='$url'</SCRIPT>";
            exit;
        }

        $newShopList = array();
        foreach($shopList as $v){
            if($v['city']){
                $newShopList[$v['city']][] = $v;
            }else{
                $newShopList['未知'][] = $v;
            }
        }

        //导入类库
        Vendor("PHPExcel.PHPExcel");
        $excel = new \PHPExcel();
        $excel->getDefaultStyle()->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_LEFT); // 水平向左对齐
        $excel->getDefaultStyle()->getAlignment()->setVertical(\PHPExcel_Style_Alignment::VERTICAL_CENTER); // 上下居中对齐
        $excel->getDefaultStyle()->getAlignment()->setWrapText(1);
        $excel->setActiveSheetIndex(0);
        //列
        $letter = array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
        //表头
        $title = array('内部商户号', '商户名称', '工行折扣', '工行商户名称', '入账账户', '入账账户名', '营业执照编号', '行业', '成交金额', '成交笔数', '详细地址', '建档时间', '联系人', '联系方式', '状态', '是否开通支付', '签约人');
        $column = count($title);
        $currentSheet = 0;
        $shopTypeMdl = new ShopTypeModel();
        $shopTypeList = $shopTypeMdl->getAllShopTypeList();
        $shopTypeRelMdl = new ShopTypeRelModel();
        foreach($newShopList as $ck => $cv){
            //添加一个新的worksheet
            $excel->createSheet();//创建一个新的工作表
            $objActSheet = $excel->getSheet($currentSheet);
            $objActSheet->setTitle($ck);//设置当前工作表的标题
            for($i = 0;$i < $column;$i++){
                //设置表格宽度
                $objActSheet->getColumnDimension("$letter[$i]")->setWidth(20);

                //设置表头样式
                $head_row = 1;
//            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setSize(16);
                $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setBold(true);
                //填充表头
                $objActSheet->setCellValue("$letter[$i]$head_row", $title[$i]);
            }

            $row = 2; // 从第2行开始写入数据
            foreach($cv as $v){
                $objActSheet->setCellValueExplicit("$letter[0]$row", $v['hqIcbcShopNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
                $objActSheet->getStyle("$letter[0]$row")->getNumberFormat()->setFormatCode("@");
                $objActSheet->setCellValue("$letter[1]$row", $v['shopName']); // 商户名称
                $v['onlinePaymentDiscount'] = number_format($v['onlinePaymentDiscount'] / 10, 2);
                $objActSheet->setCellValue("$letter[2]$row", $v['onlinePaymentDiscount']); // 工行折扣
                $objActSheet->setCellValue("$letter[3]$row", $v['icbcShopName']); // 工行商户名称
                $objActSheet->setCellValueExplicit("$letter[4]$row", $v['addCardNo'], \PHPExcel_Cell_DataType::TYPE_STRING);
                $objActSheet->getStyle("$letter[4]$row")->getNumberFormat()->setFormatCode("@");
                $objActSheet->setCellValue("$letter[5]$row", $v['addCardUserName']);
                $objActSheet->setCellValueExplicit("$letter[6]$row", $v['licenseNbr'], \PHPExcel_Cell_DataType::TYPE_STRING); // 写入营业执照编号
                $objActSheet->getStyle("$letter[6]$row")->getNumberFormat()->setFormatCode("@"); // 设置单元格格式为文本格式

                // 获得商户的所属类型
                $shopType = $shopTypeRelMdl->getShopType($v['shopCode']);
                $typeString = '';
                foreach($shopType as $typeValue){
                    foreach($shopTypeList as $shopType){
                        if($typeValue == $shopType['typeValue']){
                            if($typeString){
                                $typeString .= ', '.$shopType['typeZh'];
                            }else{
                                $typeString .= $shopType['typeZh'];
                            }
                        }
                    }
                }
                $consumeOrderMdl = new ConsumeOrderModel();
                $shopStatistics = $consumeOrderMdl->sumConsumeStatistics(array('UserConsume.location' => $v['shopCode'], 'UserConsume.status' => \Consts::PAY_STATUS_PAYED, 'ConsumeOrder.status' => \Consts::PAY_STATUS_PAYED));
                $objActSheet->setCellValue("$letter[7]$row", $typeString); // 写入行业
                $objActSheet->setCellValue("$letter[8]$row", $shopStatistics['orderAmount']); // 写入成交金额
                $objActSheet->setCellValue("$letter[9]$row", $shopStatistics['consumeCount']); // 写入成交笔数
                $objActSheet->setCellValue("$letter[10]$row", $v['province'] . $v['city'] . $v['district'] . $v['street']); // 写入详细地址
                $objActSheet->setCellValue("$letter[11]$row", $v['createDate']); // 写入建档时间
                // 获得大店长的信息
                $bigManagerInfo = $shopStaffRelMdl->getStaffInfoByShopCode($v['shopCode'], C('STAFF_LVL.BIG_MANAGER'));
                $objActSheet->setCellValue("$letter[12]$row", $bigManagerInfo['realName']); // 写入联系人
                $objActSheet->setCellValue("$letter[13]$row", $v['mobileNbr']); // 写入联系方式
                if($bigManagerInfo['status'] == 1){
                    $status = '启用';
                }elseif($bigManagerInfo['status'] == -1){
                    $status = '未激活';
                }else{
                    $status = '禁用';
                }
                $objActSheet->setCellValue("$letter[14]$row", $status); // 写入状态
                if($v['isAcceptBankCard'] == 1){
                    $v['isAcceptBankCard'] = '已开通';
                }else{
                    $v['isAcceptBankCard'] = '未开通';
                }
                $objActSheet->setCellValue("$letter[15]$row", $v['isAcceptBankCard']); // 写入是否开通支付
                $developerInfo = $preShopMdl->getPreShopInfo(array('useShopCode' => $v['shopCode']), array('developerCode')); // 获得地推录入人员
                $bmStaffInfo = $bmStaffMdl->getBmStaff($developerInfo['developerCode']);
                $objActSheet->setCellValue("$letter[16]$row", $bmStaffInfo['realName']); // 写入签约人

                $row += 1;
            }
            $currentSheet += 1;
        }

        $write = new \PHPExcel_Writer_Excel2007($excel);
        ob_end_clean();
        header("Pragma: public");
        header("Expires: 0");
        header("Cache-Control:must-revalidate, post-check=0, pre-check=0");
        header("Content-Type:application/force-download");
        header("Content-Type:application/vnd.ms-excel");
        header("Content-Type:application/octet-stream");
        header("Content-Type:application/download");
        header('Content-Disposition:attachment;filename="商户列表-'.date('Y-m-d', time()).'.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }

    public function exportAllShop() {
        $shopMdl = new ShopModel();
        $shopList = $shopMdl->listShop(I('get.'), $this->pager);
        if(empty($shopList)){
            $url = '/Admin/Shop/listShop';
            header('content-type:text/html;charset=utf-8;');
            echo '<script>alert("没有符合条件的数据！");</script>';
            echo "<SCRIPT LANGUAGE=\"JavaScript\">location.href='$url'</SCRIPT>";
            exit;
        }

        //导入类库
        Vendor("PHPExcel.PHPExcel");
        $excel = new \PHPExcel();
        $excel->getDefaultStyle()->getAlignment()->setHorizontal(\PHPExcel_Style_Alignment::HORIZONTAL_LEFT); // 水平向左对齐
        $excel->getDefaultStyle()->getAlignment()->setVertical(\PHPExcel_Style_Alignment::VERTICAL_CENTER); // 上下居中对齐
        $excel->getDefaultStyle()->getAlignment()->setWrapText(1);
        $objActSheet = $excel->getActiveSheet();
        //列
        $letter = array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
        //表头
        $title = array('内部商户号', '商户名称', '入账账户', '入账账户名');
        $column = count($title);
        for($i = 0;$i < $column;$i++){
            //设置表格宽度
            $objActSheet->getColumnDimension("$letter[$i]")->setWidth(20);

            //设置表头样式
            $head_row = 1;
//            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setSize(16);
            $objActSheet->getStyle("$letter[$i]$head_row")->getFont()->setBold(true);
            //填充表头
            $objActSheet->setCellValue("$letter[$i]$head_row", $title[$i]);
        }

        $row = 2; // 从第2行开始写入数据
        foreach($shopList as $v){
            $objActSheet->setCellValueExplicit("$letter[0]$row", $v['hqIcbcShopNbr'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[0]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[1]$row", $v['shopName']); // 商户名称
            $objActSheet->setCellValueExplicit("$letter[2]$row", $v['addCardNo'], \PHPExcel_Cell_DataType::TYPE_STRING);
            $objActSheet->getStyle("$letter[2]$row")->getNumberFormat()->setFormatCode("@");
            $objActSheet->setCellValue("$letter[3]$row", $v['addCardUserName']);

            $row += 1;
        }

        $write = new \PHPExcel_Writer_Excel2007($excel);
        ob_end_clean();
        header("Pragma: public");
        header("Expires: 0");
        header("Cache-Control:must-revalidate, post-check=0, pre-check=0");
        header("Content-Type:application/force-download");
        header("Content-Type:application/vnd.ms-excel");
        header("Content-Type:application/octet-stream");
        header("Content-Type:application/download");
        header('Content-Disposition:attachment;filename="商户清算导出-'.date('Y-m-d', time()).'.xlsx"');
        header("Content-Transfer-Encoding:utf-8");
        $write->save('php://output');
    }

    /**
     * 获得页数对象
     * @param int $page 页数
     * @return Object Pager
     */
    public function getPager($page){
        if(! isset($page) || $page === '')
            $page = 1;
        return new Pager($page, C('PAGESIZE'));
    }
}
