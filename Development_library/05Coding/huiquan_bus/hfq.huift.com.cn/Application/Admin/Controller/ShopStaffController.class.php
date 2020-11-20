<?php
/**
 * Activity Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\BrandModel;
use Common\Model\ShopModel;
use Common\Model\ShopStaffModel;
use Common\Model\ShopStaffRelModel;
use Common\Model\ShopTypeRelModel;
use Common\Model\SmsModel;
use JPush\Exception\APIRequestException;

class ShopStaffController extends AdminBaseController{

    /**
     * 获得店员所属的商家
     */
    public function listStaffShop() {

        //判断用户所属商圈
        $zoneId = null;
        if($_SESSION['USER']['bank_id']!=-1){
            $zoneId =  $_SESSION['USER']['bank_id'];
        }

        $staffCode = I('get.staffCode');
        // 获得店员的信息
        $shopStaffMdl = new ShopStaffModel();
        $staffInfo = $shopStaffMdl->getShopStaffInfo(array('staffCode' => $staffCode), array('realName', 'mobileNbr', 'userLvl', 'staffCode'));
        // 获得店员所属的商家
        $shopStaffRelMdl = new ShopStaffRelModel();
        $shopList = $shopStaffRelMdl->getShopListByStaffCode($staffCode, array('Shop.shopName', 'id', 'staffCode', 'Shop.shopCode', 'province', 'city'));
        $selectShopList = array();
        switch($staffInfo['userLvl']) {
            case C('STAFF_LVL.MANAGER'):
                $staffInfo['staffRole'] = '店长';
                // 获得没有店长的商户
                $selectShopList = $shopStaffRelMdl->getNoMngShop2($zoneId);
                break;
            case C('STAFF_LVL.BIG_MANAGER'):
                $staffInfo['staffRole'] = '大店长';
                // 获得没有大店长的商户
                $selectShopList = $shopStaffRelMdl->getNoBigMngShop2($zoneId);
                break;
            case C('STAFF_LVL.EMPLOYEE'):
                $staffInfo['staffRole'] = '普通店员';

                $condition = array();

                if(isset($zoneId) && !empty($zoneId)){
                    $condition= array("Shop.bank_id"=>$zoneId);
                }

                $shopMdl = new ShopModel();
                $selectShopList = $shopMdl->getShopList($condition, array('shopCode', 'shopName', 'province', 'city'));

                break;
            default :
                $staffInfo['staffRole'] = '店员';
                $condition = array();

                if(isset($zoneId) && !empty($zoneId)){
                    $condition= array("Shop.bank_id"=>$zoneId);
                }

                $shopMdl = new ShopModel();
                $selectShopList = $shopMdl->getShopList($condition, array('shopCode', 'shopName', 'province', 'city'));

                break;
        }
        // 路径导航
        $route = array(
            array('title' => '商户员工', 'url' => '/Admin/ShopStaff/listShopStaff'),
        );

        $assign = array(
            'title' => '店员所属商家',
            'staffInfo' => $staffInfo,
            'shopList' => $shopList,
            'selectShopList' => $selectShopList,
            'route' => $route,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 删除商家员工
     */
    public function delShopStaff() {
        $staffCode = I('get.staffCode');
        $shopStaffMdl = new ShopStaffModel();
        $ret = $shopStaffMdl->delStaff($staffCode);
        if(IS_AJAX) {
            $userLvl = I('get.userLvl');
            if($userLvl == C('STAFF_LVL.MANAGER')) {
                $shopCode = I('get.shopCode');
                $shopMdl = new ShopModel();
                $ret = $shopMdl->editShop(array('shopCode' => $shopCode, 'ownerCode' => ''));
                if($ret['code'] == C('SUCCESS')) {
                    $this->ajaxSucc();
                } else {
                    $this->ajaxError($ret['code']);
                }
            }
        } else {
            $page = I('get.page');
            if($ret['code'] == C('SUCCESS')) {
                redirect('/Admin/ShopStaff/listShopStaff?page=' . $page);
            }
        }
    }

    /**
     * 商户员工列表
     */
    public function listShopStaff() {
        $ShopStaffMdl = new ShopStaffModel();
        $filterData = I('get.');
        $shopMdl = new ShopModel();
        $shopName = $filterData['shopName'];
        unset($filterData['shopName']);
        if(!empty($shopName)) {
            $shopCodeList = $shopMdl->getShopFieldList('shopCode', array('shopName' => array('LIKE', "%$shopName%")));
            if(empty($shopCodeList)) {
                $shopCodeList = array('0');
            }
            $filterData['shopCode'] = array('IN', $shopCodeList);
        }
        $listShopStaff = $ShopStaffMdl->listShopStaff($filterData, $this->pager);

//        $smsMdl = new SmsModel();
        $shopStaffRelMdl = new ShopStaffRelModel();
        foreach ($listShopStaff as &$v) {
//            $rValidateCode = $smsMdl->getCode('r'.$v['mobileNbr']);
//            $v['rValidateCode'] = $rValidateCode ? $rValidateCode : '无';
//            $fValidateCode = $smsMdl->getCode('f'.$v['mobileNbr']);
//            $v['fValidateCode'] = $fValidateCode ? $fValidateCode : '无';
            // 获得店员所属商家
            $v['shopList'] = $shopStaffRelMdl->getShopListByStaffCode($v['staffCode'], array('Shop.shopName', 'id', 'staffCode', 'Shop.shopCode', 'province', 'city'));
            $v['parentInfo'] = $ShopStaffMdl->getShopStaffInfo(array('staffCode' => $v['parentCode']), array('realName', 'mobileNbr', 'userLvl'));
            switch($v['parentInfo']['userLvl']) {
                case C('STAFF_LVL.MANAGER'):
                    $v['parentInfo']['staffRole'] = '店长';
                    break;
                case C('STAFF_LVL.BIG_MANAGER'):
                    $v['parentInfo']['staffRole'] = '大店长';
                    break;
                case C('STAFF_LVL.EMPLOYEE'):
                    $v['parentInfo']['staffRole'] = '普通店员';
                    break;
                default :
                    $v['parentInfo']['staffRole'] = '店员';
                    break;
            }
        }

        // 获得所有品牌
        $brandMdl = new BrandModel();
        $brandList = $brandMdl->getBrandList(array(), array('*'));

        $countShopStaff = $ShopStaffMdl->countShopStaff($filterData);
        $this->pager->setItemCount($countShopStaff);

        $assign = array(
            'title' => '商户员工列表',
            'dataList' => $listShopStaff,
            'get' => I('get.'),
            'brandList' => $brandList,
            'pager' => $this->pager,
        );
        $this->assign($assign);
        if (! IS_AJAX) {
            $this->display();
        } else {
            $html = empty($listShopStaff) ? '' : $this->fetch('ShopStaff:listShopStaffWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 编辑商户员工
     */
    public function editShopStaff() {
        $shopMdl = new ShopModel();
        $shopStaffMdl = new ShopStaffModel();

        //判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']==-1){
            $bankList = D("bank")->where("status=1")->select();
        }else{
            $bankList = D("bank")->where("status=1 and id = '".$_SESSION['USER']['bank_id']."'")->select();
        }

        if (IS_GET) {
            $staffCode = I('get.staffCode');
            $shopStaffInfo = $shopStaffMdl->getShopStaffInfo(array('staffCode' => $staffCode));
            $shopTypeRelMdl = new ShopTypeRelModel();
            $shopCodeArr = $shopTypeRelMdl->getFieldList(array('ShopType.typeValue' => array('IN', array(\Consts::SHOP_TYPE_PLAT, \Consts::SHOP_TYPE_ICBC))), 'shopCode', array(array('joinTable' => 'ShopType', 'joinCondition' => 'ShopType.shopTypeId = ShopTypeRel.typeId', 'joinType' => 'inner')));
            if(empty($shopCodeArr)){
                $shopCodeArr[] = '0';
            }
            $shopCodeArr = array_unique($shopCodeArr);
            $shopList = $shopMdl->getShopList(array('shopCode' => array('NOTIN', $shopCodeArr)), array('shopCode', 'shopName'));
            // 获得所有大店长
            $bigMngList = $shopStaffMdl->getShopStaffList(array('userLvl' => C('STAFF_LVL.BIG_MANAGER')), array('staffCode', 'realName', 'mobileNbr'));
            // 获得所有店长
            $mngList = $shopStaffMdl->getShopStaffList(array('userLvl' => C('STAFF_LVL.MANAGER')), array('staffCode', 'realName', 'mobileNbr'));
            // 拼接url参数
            $urlParam = $this->setBackUrlParam(I('get.'), array('staffCode'));
            $assign = array(
                'title' => '编辑商户员工',
                'shopNameList' => $shopList,
                'shopStaffInfo' => $shopStaffInfo,
                'bankList'=>$bankList,
                'bigMngList' => $bigMngList,
                'mngList' => $mngList,
                'urlParam' => $urlParam
            );
            $this->assign($assign);
            $this->display();
        } else {
            $postData = I('post.');
            if($postData['userLvl'] != C('STAFF_LVL.EMPLOYEE')) {
                $postData['shopCode'] = '';
            }
            if($postData['userLvl'] == C('STAFF_LVL.BIG_MANAGER')) {
                $postData['parentCode'] = '';
            }
            $ret = $shopStaffMdl->editShopStaff($postData);
            if($ret['code'] !== C('SUCCESS')) {
                $this->ajaxError($ret['code']);
            } else {
                $this->ajaxSucc('保存成功', array('staffCode' => $ret['staffCode']));
            }
        }
    }

    /**
     * 修改商店员工状态
     */
    public function changeStatus() {
        $ShopStaffMdl = new ShopStaffModel();
        $staffCode = I('get.staffCode');
        $status = I('get.status');
        $res = $ShopStaffMdl->changeShopStaffStatus($staffCode, $status);
        if ($res === true) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($res);
        }
    }

}
