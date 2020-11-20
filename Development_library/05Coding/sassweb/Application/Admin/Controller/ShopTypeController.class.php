<?php
namespace Admin\Controller;
use Common\Model\CityShopTypeModel;
use Common\Model\DistrictBankIdModel;
use Common\Model\DistrictModel;
use Common\Model\ShopTypeModel;

/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-10-12
 * Time: 下午5:23
 */
class ShopTypeController extends AdminBaseController {
    /**
     * 商家类型列表
     */
    public function listShopType() {
        $shopTypeMdl = new ShopTypeModel();
        $shopTypeList = $shopTypeMdl->listShopType(I('get.'), $this->pager);
        $cityShopTypeMdl = new CityShopTypeModel();
        foreach($shopTypeList as &$v) {
            $cityList = $cityShopTypeMdl->listShopTypeCity($v['shopTypeId']);
            $v['city'] = '';
            foreach($cityList as $index => $city) {
                $v['city'] .= $index > 0 ? ',' . $city['name'] : $city['name'];
            }
            $v['name'] = getName("Bank","name","id='".$v['bank_id']."'");
        }

        $shopTypeCount = $shopTypeMdl->countShopType(I('get.'));
        $this->pager->setItemCount($shopTypeCount);

        // 图片OSS处理
        foreach ($shopTypeList as $item => &$value){
            if(!empty($value['shopTypeImg'])){
                $value['shopTypeImg'] = C("urlOSS").$value['shopTypeImg'];
            }
            if(!empty($value['focusedUrl'])){
                $value['focusedUrl'] = C("urlOSS").$value['focusedUrl'];
            }
            if(!empty($value['notFocusedUrl'])){
                $value['notFocusedUrl'] = C("urlOSS").$value['notFocusedUrl'];
            }
        }

        $assign = array(
            'title' => '会员卡列表',
            'dataList' => $shopTypeList,
            'get' => I('get.'),
            'pager' => $this->pager
        );
        $this->assign($assign);
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($shopTypeList) ? '' : $this->fetch('ShopType:listShopTypeWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 编辑商家类型
     */
    public function editShopType() {
        $shopTypeMdl = new ShopTypeModel();
        if(IS_GET){
            $shopTypeId = I('get.shopTypeId');
            $shopTypeInfo = $shopTypeMdl->getShopTypeInfo(array('shopTypeId' => $shopTypeId), array('*'));

            //        判断是否为惠圈人员
            if($_SESSION['USER']['bank_id']!=-1){
                $zone_id=   $_SESSION['USER']['bank_id'];
            }

            $districtBankMdl = new DistrictBankIdModel();
            $cityList = $districtBankMdl->zonelistOpenCity($zone_id);

            $cityShopTypeMdl = new CityShopTypeModel();

            $linkedCityList = $cityShopTypeMdl->listLinkedCity(array('shopTypeId' => $shopTypeInfo['shopTypeId']));

            //所属商圈信息
            $bankList =array();
            if($_SESSION['USER']['bank_id']!=-1){
                $bankList['id'] = $_SESSION['USER']['bank_id'];
            }
            $bankList=   D("Bank")->field("id,name")->where($bankList)->select();

            //图片OSS处理
            if(!empty($shopTypeInfo['shopTypeImg'])){
                    $shopTypeInfo['shopTypeImg']= C("urlOSS").$shopTypeInfo['shopTypeImg'];
            }
            if(!empty($shopTypeInfo['focusedUrl'] )){
                $shopTypeInfo['focusedUrl']= C("urlOSS").$shopTypeInfo['focusedUrl'];
            }

            if(!empty($shopTypeInfo['notFocusedUrl'] )){
                $shopTypeInfo['notFocusedUrl']= C("urlOSS").$shopTypeInfo['notFocusedUrl'];
            }

            $assign = array(
                'title' => '编辑商家类型',
                'shopTypeInfo' => $shopTypeInfo,
                'bankList'=>$bankList,
                'cityList' => $cityList,
                'linkedCityList' => $linkedCityList,
            );
            $this->assign($assign);
            $this->display();
        } else {
            $data = I('post.');
            $linkedCity = $data['linkedCity'];
            unset($data['linkedCity']);
            $ret = $shopTypeMdl->editShopType($data);
            if($ret === true) {
                $cityShopTypeMdl = new CityShopTypeModel();
                $cityShopTypeMdl->addLinkedCity($data['shopTypeId'], $linkedCity);
                $this->ajaxSucc();
            } else {
                $this->ajaxError($ret);
            }
        }
    }
}