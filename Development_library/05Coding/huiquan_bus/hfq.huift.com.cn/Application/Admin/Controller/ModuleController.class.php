<?php
/**
 * Feedback Controller
 *
 * User: jiangyufeng
 * Date: 2015-05-19
 */
namespace Admin\Controller;
use Common\Model\ActivityModel;
use Common\Model\CityIndexModuleRelModel;
use Common\Model\indexModule;
use Common\Model\IndexModuleModel;
use Common\Model\ShopTradingAreaRelModel;
use Common\Model\ShopTypeModel;
use Common\Model\SubAlbumModel;
use Common\Model\SubModuleModel;
use Common\Model\UserModel;
use Common\Model\ShopModel;
use Org\FirePHPCore\FP;
use Common\Model\ShopQueryModel;


class ModuleController extends AdminBaseController {

    /**
     * 获得首页模块列表
     */
    public function listIndexModule() {
        $indexModuleMdl = new IndexModuleModel();
        $indexModuleList = $indexModuleMdl->getIndexModuleList();
        $assign = array(
            'title' => '首页模块管理',
            'dataList' => $indexModuleList,
        );
        $this->assign($assign);
        $this->display();
    }

    public function editIndexModule() {
        $id = I('get.id');
        $indexModuleMdl = new IndexModuleModel();
        $indexModuleInfo = $indexModuleMdl->getIndexModuleInfo($id);
        $assign = array(
            'title' => '编辑模块',
            'dataInfo' => $indexModuleInfo,
        );
        $this->assign($assign);
        if (IS_GET) {
            $this->display();
        } else {
            $data = I('post.');
            $ret = $indexModuleMdl->editIndexModule($data);
            if ($ret['code'] === true) {
                $this->ajaxSucc('编辑成功');
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }

    public function changeStatus() {
        $cimrMdl = new CityIndexModuleRelModel();
        $subModuleMdl = new SubModuleModel();
        $indexModuleMdl = new IndexModuleModel();
        $data = I('get.');
        $type = $data['type'];
        unset($data['type']);
        if($type == 'index'){
            $ret = $cimrMdl->editRel($data);
        }else{
            $subModuleInfo = $subModuleMdl->getSubModuleInfo($data['id']);
            $indexModuleInfo = $indexModuleMdl->getIndexModuleInfo($subModuleInfo['parentModuleId']);
            if(in_array($indexModuleInfo['moduleValue'], array(\IndexModule::ACTIVITY, \IndexModule::FUNCTION_AREA))){ // 5代表活动模块（来自 indexModule 表）
                $subModuleCount1 = $subModuleMdl->countSubModule(array('SubModule.cityId' => $subModuleInfo['cityId'], 'parentModuleId' => $subModuleInfo['parentModuleId']));
            }

            $ret = $subModuleMdl->editSubModule($data);

            if(in_array($indexModuleInfo['moduleValue'], array(\IndexModule::ACTIVITY, \IndexModule::FUNCTION_AREA))){ // 功能、活动模块（来自 indexModule 表）
                $subModuleCount2 = $subModuleMdl->countSubModule(array('SubModule.cityId' => $subModuleInfo['cityId'], 'parentModuleId' => $subModuleInfo['parentModuleId']));
                if($subModuleCount2 != $subModuleCount1){
                    $relList = $cimrMdl->listCityIndexModuleRel(array('relId'), array('cityId' => $subModuleInfo['cityId'], 'indexModuleId' => $subModuleInfo['parentModuleId']));
                    if($subModuleCount2 == 5){
                        $cimrMdl->editRel(array('relId' => $relList[0]['relId'], 'template' => $subModuleCount2.'06'));
                    }else{
                        $cimrMdl->editRel(array('relId' => $relList[0]['relId'], 'template' => $subModuleCount2.'01'));
                    }
                }
            }
        }
        if ($ret['code'] === true) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($ret['code']);
        }
    }

    public function changeOrder() {
        $cimrMdl = new CityIndexModuleRelModel();
        $subModuleMdl = new SubModuleModel();
        $data = I('get.');
        $type = $data['type'];
        unset($data['type']);
        if($type == 'index'){
            $relInfo = $cimrMdl->getRelInfo($data['relId']);
            $linkedRel = $cimrMdl->listCityIndexModuleRel(array('relId', 'orderNbr'), array('cityId' => $relInfo['cityId']));
            foreach($linkedRel as $k => $v){
                if($v['relId'] == $data['relId']){
                    $arr1 = $v;
                    if($data['order'] == 'up'){
                        if($k == 0){
                            $arr2 = $linkedRel[count($linkedRel) - 1]['orderNbr'] + 1;
                        }else{
                            $arr2 = $linkedRel[$k - 1];
                        }
                    }else{
                        if($k == count($linkedRel) - 1){
                            $arr2 = $linkedRel[0]['orderNbr'] - 1;
                        }else{
                            $arr2 = $linkedRel[$k + 1];
                        }
                    }
                    break;
                }else{
                    continue;
                }
            }
            if(is_array($arr2)){
                $cimrMdl->editRel(array('relId' => $arr2['relId'], 'orderNbr' => $arr1['orderNbr']));
                $ret = $cimrMdl->editRel(array('relId' => $arr1['relId'], 'orderNbr' => $arr2['orderNbr']));
            }else{
                $ret = $cimrMdl->editRel(array('relId' => $arr1['relId'], 'orderNbr' => $arr2));
            }
        }else{
            $subModuleInfo = $subModuleMdl->getSubModuleInfo($data['id']);
            $subModuleList = $subModuleMdl->getSubModuleList(array('SubModule.id', 'SubModule.orderNbr'), array('SubModule.cityId' => $subModuleInfo['cityId'], 'parentModuleId' => $subModuleInfo['parentModuleId']));
            foreach($subModuleList as $k => $v){
                if($v['id'] == $data['id']){
                    $arr1 = $v;
                    if($data['order'] == 'up'){
                        if($k == 0){
                            $arr2 = $subModuleList[count($subModuleList) - 1]['orderNbr'] + 1;
                        }else{
                            $arr2 = $subModuleList[$k - 1];
                        }
                    }else{
                        if($k == count($subModuleList) - 1){
                            $arr2 = $subModuleList[0]['orderNbr'] - 1;
                        }else{
                            $arr2 = $subModuleList[$k + 1];
                        }
                    }
                    break;
                }else{
                    continue;
                }
            }
            if(is_array($arr2)){
                $subModuleMdl->editSubModule(array('id' => $arr2['id'], 'orderNbr' => $arr1['orderNbr']));
                $ret = $subModuleMdl->editSubModule(array('id' => $arr1['id'], 'orderNbr' => $arr2['orderNbr']));
            }else{
                $ret = $subModuleMdl->editSubModule(array('id' => $arr1['id'], 'orderNbr' => $arr2));
            }
        }
        if ($ret['code'] === true) {
            $this->ajaxSucc('操作成功!');
        } else {
            $this->ajaxError($ret['code']);
        }
    }

    public function editSubModule() {
        $id = I('get.id');
        $parentModuleId = I('get.parentModuleId');
        $cityId = I('get.cityId');
        $cimrMdl = new CityIndexModuleRelModel();
        $subModuleMdl = new SubModuleModel();
        $indexModuleMdl = new IndexModuleModel();
        $subModuleInfo = $subModuleMdl->getSubModuleInfo($id);

        if(isset($subModuleInfo['titleColor']) && $subModuleInfo['titleColor']){$subModuleInfo['titleColor'] = str_replace("#", "", $subModuleInfo['titleColor']);}
        if(isset($subModuleInfo['subTitleColor']) && $subModuleInfo['subTitleColor']){$subModuleInfo['subTitleColor'] = str_replace("#", "", $subModuleInfo['subTitleColor']);}
        if(isset($subModuleInfo['bgColor']) && $subModuleInfo['bgColor']){$subModuleInfo['bgColor'] = str_replace("#", "", $subModuleInfo['bgColor']);}

        if(empty($subModuleInfo)){
            $subModuleInfo['parentModuleId'] = $parentModuleId;
            $subModuleInfo['cityId'] = $cityId;
        }

        foreach(C('SHOP_NORMAL_COUPON') as $k => $v){
            $couponTypeList[] = array(
                'name'  => C('COUPON_TYPE_NAME.'.$k),
                'value' => $v
            );
        }

//        if(empty($subModuleInfo['imgSize'])){$subModuleInfo['imgSize'] = '80|80';}

        // 设置h5里的按钮
        $subModuleInfo['exButtonList'] = json_decode($subModuleInfo['exButtonList'], true);
        $subModuleInfo['exButtonList'] = $subModuleInfo['exButtonList'] ? $subModuleInfo['exButtonList'] : array();

        $shopTypeMdl = new ShopTypeModel();
        $shopTypeList = $shopTypeMdl->getAllShopTypeList();
        $assign = array(
            'title' => '编辑子模块',
            'dataInfo' => $subModuleInfo,
            'couponTypeList' => $couponTypeList,
            'shopTypeList' => $shopTypeList
        );
        $this->assign($assign);
        if (IS_GET) {
            if($parentModuleId == \IndexModule::HOME_TAB || $subModuleInfo['parentModuleId'] == \IndexModule::HOME_TAB){
                $this->display('Module/editSubModuleForTab');
            }else{
                $this->display();
            }
        } else {
            $data = I('post.');
            if($data['titleColor']){$data['titleColor'] = '#'.$data['titleColor'];}
            if($data['subTitleColor']){$data['subTitleColor'] = '#'.$data['subTitleColor'];}
            if($data['bgColor']){$data['bgColor'] = '#'.$data['bgColor'];}

            if($data['linkType'] == 1 || $data['linkType'] == 3){
                $data['content'] = $data['shopType'];
                unset($data['exButtonList']);
            }elseif($data['linkType'] == 2){
                $data['content'] = $data['couponType'];
                unset($data['exButtonList']);
            }else{
                if(empty($data['exButtonList']['ebLink'])){
                    unset($data['exButtonList']);
                }else{
                    $exButtonList = array();
                    foreach($data['exButtonList']['ebLink'] as $key => $value){
                        if($value){
                            $exButtonList[] = array(
                                'ebLink' => $data['exButtonList']['ebLink'][$key],
                                'ebWidth' => $data['exButtonList']['ebWidth'][$key],
                                'ebHeight' => $data['exButtonList']['ebHeight'][$key],
                                'ebLeft' => $data['exButtonList']['ebLeft'][$key],
                                'ebTop' => $data['exButtonList']['ebTop'][$key],
                            );
                        }
                    }
                    if($exButtonList){
                        $data['exButtonList'] = json_encode($exButtonList);
                    }else{
                        unset($data['exButtonList']);
                    }
                }
            }
            unset($data['shopType']);
            unset($data['couponType']);

            $indexModuleInfo = $indexModuleMdl->getIndexModuleInfo($data['parentModuleId']);
            if(in_array($indexModuleInfo['moduleValue'], array(\IndexModule::ACTIVITY, \IndexModule::FUNCTION_AREA))){ // 5代表活动模块（来自 indexModule 表）
                $subModuleCount1 = $subModuleMdl->countSubModule(array('SubModule.cityId' => $data['cityId'], 'parentModuleId' => $data['parentModuleId']));
            }
            $ret = $subModuleMdl->editSubModule($data);

            if(in_array($indexModuleInfo['moduleValue'], array(\IndexModule::ACTIVITY, \IndexModule::FUNCTION_AREA))){ // 功能、活动模块（来自 indexModule 表）
                $subModuleCount2 = $subModuleMdl->countSubModule(array('SubModule.cityId' => $data['cityId'], 'parentModuleId' => $data['parentModuleId']));
                if($subModuleCount2 != $subModuleCount1){
                    $relList = $cimrMdl->listCityIndexModuleRel(array('relId'), array('cityId' => $data['cityId'], 'indexModuleId' => $data['parentModuleId']));
                    if($subModuleCount2 == 5){
                        $cimrMdl->editRel(array('relId' => $relList[0]['relId'], 'template' => $subModuleCount2.'06'));
                    }else{
                        $cimrMdl->editRel(array('relId' => $relList[0]['relId'], 'template' => $subModuleCount2.'01'));
                    }
                }
            }
            if ($ret['code'] === true) {
                $this->ajaxSucc('编辑成功');
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }

    /**
     * 查询条件列表
     */
    public function listQuery(){
        $shopQueryMdl = new ShopQueryModel();
        $listShopQuery = $shopQueryMdl->getSubField(array('*'), $condition = null);
        $field = ShopQueryModel::$fieldArr;
        $assign = array(
            'title' => '首页模块管理',
            'field' => $field,
            'dataList' => $listShopQuery,
        );
        $this->assign($assign);
        $this->display();
    }

    /**
     * 编辑查询条件
     */
    public function editShopQuery(){
        $shopQueryMdl = new ShopQueryModel();
        if (IS_GET) {
            $id = I('get.id');
            $shopQueryInfo = $shopQueryMdl->getShopQueryInfo(array('*'), array('id' => $id));
            $this->ajaxReturn($shopQueryInfo);
        }else{
            $data = I('post.');
            unset($data['upfile']);
            $ret = $shopQueryMdl->editShopQuery($data);
            if ($ret['code'] === true) {
                $this->ajaxSucc('编辑成功');
            } else {
                $this->ajaxError($ret['code']);
            }
        }
    }

    public function editScroll(){
        $cityId = I('get.cityId');
        $parentModuleId = I('get.parentModuleId');
        $subModuleMdl = new SubModuleModel();
        $subModuleList = $subModuleMdl->getSubModuleList(array('SubModule.activityCode'), array('cityId' => $cityId, 'parentModuleId' => $parentModuleId));
        $activityMdl = new ActivityModel();
//        $condition['unix_timestamp(endTime)'] = array('GT', time());
//        $condition['status'] = C('ACTIVITY_STATUS.ACTIVE');
        $condition['pos'] = C('ACT_POS.SCROLL');
        $bankAct = $activityMdl->getActivityByBelonging($condition, C('ACTIVITY_BELONGING.BANK'), 0);
        $platAct = $activityMdl->getActivityByBelonging($condition, C('ACTIVITY_BELONGING.PLAT'), 0);
        $shopAct = $activityMdl->getActivityByBelonging($condition, C('ACTIVITY_BELONGING.SHOP'), 0);
        $shopMdl = new ShopModel();
        foreach($shopAct as $k => $v){
            $shopInfo = $shopMdl->getShopInfo($v['shopCode'], array('shopName'));
            $shopAct[$k]['shopName'] = $shopInfo['shopName'];
        }
        $assign = array(
            'title' => '添加滚屏',
            'cityId' => $cityId,
            'parentModuleId' => $parentModuleId,
            'bankAct' => $bankAct,
            'platAct' => $platAct,
            'shopAct' => $shopAct,
            'subModuleList' => $subModuleList
        );
        $this->assign($assign);
        if (IS_GET) {
            $this->display();
        } else {
            $data = I('post.');
            if(!isset($data['platActivityCode'])){$data['platActivityCode'] = array();}
            if(!isset($data['bankActivityCode'])){$data['bankActivityCode'] = array();}
            if(!isset($data['shopActivityCode'])){$data['shopActivityCode'] = array();}
            $data['activityCode'] = array_unique(array_merge($data['platActivityCode'], $data['bankActivityCode'], $data['shopActivityCode']));
            unset($data['platActivityCode']);
            unset($data['bankActivityCode']);
            unset($data['shopActivityCode']);
            $ret = $subModuleMdl->editSubModule($data);
            if ($ret['code'] === true) {
                $this->ajaxSucc('编辑成功');
            } else {
                $this->ajaxError('', $ret['code']);
            }
        }
    }
}