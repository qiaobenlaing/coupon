<?php
namespace Wechat\Controller;
use Common\Model\wxApiModel;
use Think\Controller;
use Common\Model\ActivityModel;
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-1
 * Time: 上午11:37
 */
class ActivityController extends WechatBaseController{
    /**
     * @var $wxApi wxApiModel
     */
    private $wxApi;

    public function _initialize(){
        parent::_initialize();
        $this->wxApi = new wxApiModel();
    }

    /**
     * 分享活动
     */
    public function share(){
        $activityMdl = new ActivityModel();
        $activityCode = I('get.activityCode');
//        $activityCode = '0a6878b4-c076-3ba2-49ef-42d2216750e5';
        $activityInfo = $activityMdl->getActInfo(array('activityCode' => $activityCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;
        $assign['title'] = '活动分享';
        $assign['actInfo'] = $activityInfo;
        $this->assign($assign);
        $this->display();
    }
}