<?php
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-7-8
 * Time: 下午8:00
 */
namespace Admin\Controller;
use Common\Model\BrandModel;
use Common\Model\CityBrandRelModel;
use Common\Model\CityIndexModuleRelModel;
use Common\Model\DistrictBankIdModel;
use Common\Model\DistrictModel;
use Common\Model\indexModule;
use Common\Model\IndexModuleModel;
use Common\Model\SubAlbumModel;
use Common\Model\SubModuleModel;
use Common\Model\ShopQueryModel;
use Common\Model\CityShopTypeModel;

class DistrictController extends AdminBaseController {

    /**
     * 获得城市列表
     */
    public function listCity() {

        $district_bankModel = new DistrictBankIdModel();
        $this->pager->setPageSize(30);

        // 判断是否为惠圈人员
        $condition = array();
        if($_SESSION['USER']['bank_id']!=-1){
            $condition['id'] = $_SESSION['USER']['bank_id'];
        }

        $list =     D("Bank")->field("id,name")->where($condition)->select();
        //剔除惠圈
        foreach ($list as $item => &$value){
            if($value['id']==1){
                unset($list[$item]);
            }
        }
        $where = empty(I('get.')) ? array('isOpen' => 1) : I('get.');
//        $cityList = $district_bankModel->SaaslistCity(I('get.'), $this->pager);
//        $cityCount = $district_bankModel->SaascountCity(I('get.'));
        $cityListRes = $district_bankModel->getCityList($where, $this->pager);
        $cityList = $cityListRes['cityList'];
        $cityCount = $cityListRes['cityCount'];

        $this->pager->setItemCount($cityCount);

        $this->assign(array(
            'title' => '城市列表',
            'dataList' => $cityList,
            'pager' => $this->pager,
            'get' =>  I('get.'),
            "list" => $list,
        ));
        if (!IS_AJAX) {
            $this->display();
        } else {
            $html = empty($cityList) ? '' : $this->fetch('District:listCityWidget');
            $this->ajaxSucc('', null, $html);
        }
    }

    /**
     * 修改城市是否开通的状态
     */
    public function changeStatus() {
        $districtMdl = new DistrictModel();
        $id = I('get.id');
        $isOpen = I('get.status');

        // 判断是否为惠圈人员
        if($_SESSION['USER']['bank_id']!=-1){
            $bank_id = $_SESSION['USER']['bank_id'];
        }else{
            $bank_id = I("get.bank_id");
        }
        if($isOpen == C('YES')) {
            $resp = array('txt' => '关闭', 'status' => C('NO'));
        } else {
            $resp = array('txt' => '开启', 'status' => C('YES'));
        }
            //判断是否开通/关闭
            $dist_bank = D("District_bank_id");
            if($isOpen==C('YES')){
                $result   =  $dist_bank->add(array("dist_id"=>$id,"bank_id"=>$bank_id,"isOpen"=>"1"));
            }else{
                $result   = $dist_bank->where(array("dist_id"=>$id))->delete();
            }
         if ($result) {
            $this->success("操作成功");
        } else {
            $this->error("操作失败");
        }
    }


    public function listCityIndexModule(){
        $cityId = I('get.id');
        $cimrMdl = new CityIndexModuleRelModel();
        $subModuleMdl = new SubModuleModel();
        $indexModuleMdl = new IndexModuleModel();

        $dMdl = new DistrictModel();
        $cityInfo = $dMdl->getCityInfo(array('id' => $cityId), array('name'));
        $indexModuleList = $cimrMdl->listCityIndexModuleRel('', array('cityId' => $cityId));

        foreach($indexModuleList as $k => $v){
            if($v['moduleValue'] == \IndexModule::BRAND){
                $cityBrandRelMdl = new CityBrandRelModel();
//                $indexModuleList[$k]['subModuleList'] = $cityBrandRelMdl->listRel(array('CityBrandRel.*', 'brandName', 'brandLogo'), array('cityId' => $cityId));
                $indexModuleList[$k]['subModuleCount'] = $cityBrandRelMdl->countRel(array('cityId' => $cityId));
            }elseif($v['moduleValue'] == \IndexModule::SHOP_TYPE){
                $cityShopTypeMdl = new CityShopTypeModel();
                $shopTypeList = $cityShopTypeMdl->listCityShopType($cityId);
                $indexModuleList[$k]['subModuleCount'] = count($shopTypeList);
            }else{
                $indexModuleList[$k]['subModuleList'] = $subModuleMdl->getSubModuleList('', array('SubModule.cityId' => $cityId, 'parentModuleId' => $v['indexModuleId']));
                $indexModuleList[$k]['subModuleCount'] = $subModuleMdl->countSubModule(array('SubModule.cityId' => $cityId, 'parentModuleId' => $v['indexModuleId'], 'hide' => C('NO')));
            }
        }
        $linkedModuleIdArr = $cimrMdl->getIndexModuleArr($cityId);
        $count = $indexModuleMdl->CountIndexModule();
        $canAddModule = 1;
        if(count($linkedModuleIdArr) >= $count){
            $canAddModule = 0;
        }
        $assign = array(
            'title' => $cityInfo['name'].' - 首页模块管理',
            'cityId' => $cityId,
            'canAddModule' => $canAddModule,
            'dataList' => $indexModuleList,
            'scrolling' => \IndexModule::SCROLLING,
            'shopType' => \IndexModule::SHOP_TYPE,
            'trading' => \IndexModule::SHOP_TRADING_AREA,
            'brand' => \IndexModule::BRAND,
            'activity' => \IndexModule::ACTIVITY,
            'shopList' => \IndexModule::SHOP_LIST,
            'function' => \IndexModule::FUNCTION_AREA,
            'tab' => \IndexModule::HOME_TAB,
        );
        $this->assign($assign);
        $this->display();
    }

    public function addLinkedCity(){
        $cityId = I('get.cityId');
        $dMdl = new DistrictModel();
        $cimrMdl = new CityIndexModuleRelModel();
        $indexModuleMdl = new IndexModuleModel();
        $cityInfo = $dMdl->getCityInfo(array('id' => $cityId), array('name'));
        $linkedList = $cimrMdl->listCityIndexModuleRel(array('indexModuleId'), array('cityId' => $cityId));
        $linkedModuleIdArr = $cimrMdl->getIndexModuleArr($cityId);
        if(empty($linkedModuleIdArr)){
            $linkedModuleIdArr = array(0);
        }
        $indexModuleList = $indexModuleMdl->getIndexModuleList(array('id' => array('NOTIN', $linkedModuleIdArr)));

        $assign = array(
            'title' => $cityInfo['name'].' - 添加模块',
            'linkedList' => $linkedList,
            'cityId' => $cityId,
            'indexModuleList' => $indexModuleList
        );
        $this->assign($assign);
        if (IS_GET) {
            $this->display();
        } else {
            $cityId = I('post.cityId');
            $linkedModule = I('post.linkedModule');
            $ret = $cimrMdl->addLinkedCity($cityId, $linkedModule);
            if ($ret['code'] === true) {
                $this->ajaxSucc('编辑成功');
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }

    public function editCityModule() {
        $relId = I('get.relId');
        $cimrMdl = new CityIndexModuleRelModel();
        $subModuleMdl = new SubModuleModel();
        $relInfo = $cimrMdl->getRelInfo($relId);
        $subModuleCount = $subModuleMdl->countSubModule(array('SubModule.cityId' => $relInfo['cityId'], 'parentModuleId' => $relInfo['indexModuleId'], 'hide' => C('NO')));
        $subModuleList = $subModuleMdl->getSubModuleList('', array('SubModule.cityId' => $relInfo['cityId'], 'parentModuleId' => $relInfo['indexModuleId'], 'hide' => C('NO')));
        if($subModuleCount > 0){
            foreach($subModuleList as $k => $v){
                $size = explode('|', $v['imgSize']);
                $imgWidth = $size[0] ? $size[0] : 80;
                $imgHeight = $size[1] ? $size[1] : 80;
                $subModuleList[$k]['imgWidth'] = $imgWidth.'px';
                $subModuleList[$k]['imgHeight'] = $imgHeight.'px';
//                $subModuleList[$k]['imgRate'] = number_format($imgWidth / $imgHeight, 2);
            }
        }else{
            $cityBrandRelMdl = new CityBrandRelModel();
            $subModuleCount = $cityBrandRelMdl->countRel(array('cityId' => $relInfo['cityId']));
            $subModuleList = $cityBrandRelMdl->listRel(array('CityBrandRel.*'), array('cityId' => $relInfo['cityId']));
            foreach($subModuleList as $k => $v){
                $subModuleList[$k]['imgWidth'] = '80px';
                $subModuleList[$k]['imgHeight'] = '80px';
//                $subModuleList[$k]['imgRate'] = number_format($imgWidth / $imgHeight, 2);
            }
        }
        $assign = array(
            'title' => '显示模板',
            'dataInfo' => $relInfo,
            'subModuleList' => $subModuleList,
            'subModuleCount' => $subModuleCount
        );
        $this->assign($assign);
        if (IS_GET) {
            $this->display('District/editCityModule_new');
        } else {
            $data = I('post.');
            $ret = $cimrMdl->editRel($data);
            if ($ret['code'] === true) {
                $this->ajaxSucc('编辑成功');
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }

    public function editCityBrandRel(){
        $cityId = I('get.cityId');
        $cityBrandRelMdl = new CityBrandRelModel();
        $subCount1 = $cityBrandRelMdl->countRel(array('cityId' => $cityId));

        $cityBrandRelList = $cityBrandRelMdl->listRel(array('CityBrandRel.*', 'brandName'), array('cityId' => $cityId));
        $brandMdl = new BrandModel();
        $brandList = $brandMdl->getBrandList(array(), array('brandId', 'brandName'));

        // 设置h5里的按钮
        if($cityBrandRelList){
            foreach($cityBrandRelList as $k => $v){
                $cityBrandRelList[$k]['exButtonList'] = json_decode($v['exButtonList'], true);
            }
        }
        $assign = array(
            'title' => '添加品牌',
            'cityId' => $cityId,
            'cityBrandRelList' => $cityBrandRelList,
            'brandList' => $brandList
        );
        $this->assign($assign);
        if (IS_GET) {
//            $this->display();
            $this->display('District/editCityBrandRel_new');
        } else {
            $data = I('post.');
            $linkedBrand = array();
            if($data['brandId']){
                foreach($data['brandId'] as $v){
                    if($data['linkType'][$v] != 0){
                        $linkedBrand[] = array(
                            'brandId' => $v,
                            'linkType' => $data['linkType'][$v],
                            'content' => 0,
                            'imgUrl' => isset($data['imgUrl'.$v]) ? $data['imgUrl'.$v] : '',
                            'imgRate' => $data['imgRate'][$v],
                            'screenRate' => $data['screenRate'][$v]
                        );
                    }else{
                        $exButtonList = array();
                        foreach($data['exButtonList'][$v]['ebLink'] as $key => $value){
                            if($value){
                                $exButtonList[] = array(
                                    'ebLink' => $data['exButtonList'][$v]['ebLink'][$key],
                                    'ebWidth' => $data['exButtonList'][$v]['ebWidth'][$key],
                                    'ebHeight' => $data['exButtonList'][$v]['ebHeight'][$key],
                                    'ebLeft' => $data['exButtonList'][$v]['ebLeft'][$key],
                                    'ebTop' => $data['exButtonList'][$v]['ebTop'][$key],
                                );
                            }
                        }
                        $linkedBrand[] = array(
                            'brandId' => $v,
                            'linkType' => $data['linkType'][$v],
                            'content' => $data['content'][$v],
                            'imgUrl' => isset($data['imgUrl'.$v]) ? $data['imgUrl'.$v] : '',
                            'imgRate' => $data['imgRate'][$v],
                            'screenRate' => $data['screenRate'][$v],
                            'bgUrl' => isset($data['bgUrl'.$v]) ? $data['bgUrl'.$v] : '',
                            'exButtonList' => empty($exButtonList) ? NULL : json_encode($exButtonList),
                        );
                    }
                }
            }

            $ret = $cityBrandRelMdl->addLinkedRel($data['cityId'], $linkedBrand);
            if ($ret['code'] === true) {
                $subCount2 = $cityBrandRelMdl->countRel(array('cityId' => $data['cityId']));
                if($subCount2 != $subCount1 && $subCount2 > 0){
                    $cimrMdl = new CityIndexModuleRelModel();
                    $relList = $cimrMdl->listCityIndexModuleRel(array('relId'), array('cityId' => $data['cityId'], 'indexModuleId' => \IndexModule::BRAND));
                    if($subCount2 == 5){
                        $cimrMdl->editRel(array('relId' => $relList[0]['relId'], 'template' => $subCount2.'06'));
                    }else{
                        $cimrMdl->editRel(array('relId' => $relList[0]['relId'], 'template' => $subCount2.'01'));
                    }
                }
                $this->ajaxSucc('编辑成功');
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }
}