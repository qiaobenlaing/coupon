<?php
namespace Admin\Controller;
use Common\Model\CityShopTypeModel;
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

        }
        $shopTypeCount = $shopTypeMdl->countShopType(I('get.'));
        $this->pager->setItemCount($shopTypeCount);
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

            $districtMdl = new DistrictModel();
            $cityList = $districtMdl->listOpenCity();

            $cityShopTypeMdl = new CityShopTypeModel();
            $linkedCityList = $cityShopTypeMdl->listLinkedCity(array('shopTypeId' => $shopTypeInfo['shopTypeId']));

            $assign = array(
                'title' => '编辑商家类型',
                'shopTypeInfo' => $shopTypeInfo,
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