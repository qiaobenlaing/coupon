<?php
/**
 * Created by PhpStorm.
 * User: jihuafei
 * Date: 15-12-31
 * Time: 下午7:02
 */
namespace Api\Controller;
use Common\Model\ActivityModel;
use Common\Model\BatchCouponModel;
use Common\Model\BrandModel;
use Common\Model\CityBrandRelModel;
use Common\Model\Pager;
use Common\Model\ShopModel;
use Common\Model\SubModuleModel;
use Common\Model\wxApiModel;
use Think\Controller;
use Wechat\Controller\WechatBaseController;

class ActModuleController extends WechatBaseController {

    /**
     * @var $wxApi wxApiModel
     */
    private $wxApi;

    public function _initialize(){
        parent::_initialize();
        $this->wxApi = new wxApiModel();
    }

    /**
     * 丽水市活动h5
     */
    public function lishui() {
        $actId = I('get.actId');
        $isShared = I('get.isShared');
        $subModuleMdl = new SubModuleModel();
        $subModuleInfo = $subModuleMdl->getSubModuleInfo($actId);
        $subModuleInfo['exButtonList'] = json_decode($subModuleInfo['exButtonList'], true);
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;
        $assign['title'] = $subModuleInfo['title'];
        $assign['subModuleInfo'] = $subModuleInfo;
        $assign['rank'] = 1;
        $assign['isShared'] = $isShared;
        $this->assign($assign);
        $this->display();
    }

    /**
     * 衢州市活动h5
     */
    public function quzhou() {
        $actId = I('get.actId');
        $isShared = I('get.isShared');
        $subModuleMdl = new SubModuleModel();
        $subModuleInfo = $subModuleMdl->getSubModuleInfo($actId);
        $subModuleInfo['exButtonList'] = json_decode($subModuleInfo['exButtonList'], true);
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;
        $assign['title'] = $subModuleInfo['title'];
        $assign['subModuleInfo'] = $subModuleInfo;
        $assign['isShared'] = $isShared;
        $this->assign($assign);
        $this->display();
    }

    /**
     * 嘉兴市活动h5
     */
    public function jiaxing() {
        $actId = I('get.actId');
        $isShared = I('get.isShared');
        $subModuleMdl = new SubModuleModel();
        $subModuleInfo = $subModuleMdl->getSubModuleInfo($actId);
        $subModuleInfo['exButtonList'] = json_decode($subModuleInfo['exButtonList'], true);
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;
        $assign['title'] = $subModuleInfo['title'];
        $assign['subModuleInfo'] = $subModuleInfo;
        $assign['isShared'] = $isShared;
        $this->assign($assign);
        if($actId == 190){ //190 是观影惠
            $this->display('ActModule/jiaxing_copy');
        }else{
            $this->display();
        }
    }

    /**
     * 湖州市活动h5
     */
    public function huzhou() {
        $actId = I('get.actId');
        $isShared = I('get.isShared');
        $subModuleMdl = new SubModuleModel();
        $subModuleInfo = $subModuleMdl->getSubModuleInfo($actId);
        $subModuleInfo['exButtonList'] = json_decode($subModuleInfo['exButtonList'], true);
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;
        $assign['title'] = $subModuleInfo['title'];
        $assign['subModuleInfo'] = $subModuleInfo;
        $assign['isShared'] = $isShared;
        $this->assign($assign);
        $this->display();
    }

    /**
     * 在页面中加载外部链接的内容
     */
    public function cGetExternalLink(){
        $key = I('get.key');
        $isShared = I('get.isShared');
        $externalLinkArr = array(
            1 => array(
                'externalLink' => 'http://mp.weixin.qq.com/s?__biz=MzA4ODA1NjM1OA==&mid=409720724&idx=1&sn=e3c7209cbde5a9303ded52926476be28#rd',
                'activityCode' => '4e9670c8-450a-e6b5-bf39-5888530bffb1'
            ),
            2 => array(
                'externalLink' => 'http://mp.weixin.qq.com/s?__biz=MzA4ODA1NjM1OA==&mid=409808792&idx=1&sn=80c72d138c79ae992f1ddd4d80f73eef#rd',
                'activityCode' => 'a947f288-2b7c-58c7-b776-ebe4bea57a46'
            )
        );
        $html = file_get_contents($externalLinkArr[$key]['externalLink']);
//            ob_start(); //打开输出缓冲区
//            $ch = curl_init(); //初始化会话
//            curl_setopt( $ch, CURLOPT_URL, $externalLink ); //设定目标URL
//            curl_exec( $ch ); //发送请求
//            $html = ob_get_contents(); //返回内部缓冲区的内容
//            ob_end_clean(); //删除内部缓冲区的内容并关闭内部缓冲区
//            curl_close( $ch ); //会话结束
        $actMdl = new ActivityModel();
        $activityInfo = $actMdl->getActInfo(array('activityCode' => $externalLinkArr[$key]['activityCode']));
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;
        $assign['title'] = $activityInfo['activityName'];
        $assign['externalLinkHtml'] = $html;
        $assign['activityInfo'] = $activityInfo;
        $assign['isShared'] = $isShared;
        $this->assign($assign);
        $this->display();
    }

    /**
     * 滚屏活动 H5
     */
    public function cGetScrollInfo() {
        $activityCode = I('get.activityCode');
        $isShared = I('get.isShared');
        //通过活动编码查到的活动详情，前期设置平台主题活动功能无，所以先默认以下数据（简）
        $actMdl = new ActivityModel();
        $activityInfo = $actMdl->getActInfo(array('activityCode' => $activityCode));
//        $activityInfo['batchCouponCode'] = '';
//        $bcMdl = new BatchCouponModel();
//        $batchCouponInfo = $bcMdl->cGetScrollInfo($activityInfo['batchCouponCode']);
        if($activityInfo['exButtonList']){
            $activityInfo['exButtonList'] = json_decode($activityInfo['exButtonList'], true);
        }
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;
        $assign['title'] = $activityInfo['activityName'];
        $assign['activityInfo'] = $activityInfo;
        $assign['isShared'] = $isShared;
        if(in_array($activityCode, array('1147ebaf-8649-9698-a219-5904a15d715f', '7c0392e3-0c24-8cc1-8000-3580a61ee3e1', '0db43a39-c2d7-1bdf-499b-2a85b6a4072b'))){
            if($activityCode == '1147ebaf-8649-9698-a219-5904a15d715f'){ //丽水
                $shopCodeArr = array('ed04073c-3990-f41b-5193-9250fad2c24b','130dd9b6-587b-dd62-4c71-4837e4f58d0f','884d051b-63b5-fa21-b606-56e47a9c20ab','383b7eff-91cd-762e-633d-14c366c51ee2','9e3e5aa8-ed5d-26ed-af72-831def63ea6e','e0b9fb15-a0a7-ebd5-7bce-ac1c85170ec4','1d7048fc-63c0-5d29-59fa-3cc8816595a5','6a05ae7e-549b-640e-36a3-29150c3e384b ');
            }elseif($activityCode == '7c0392e3-0c24-8cc1-8000-3580a61ee3e1'){ //衢州
                $shopCodeArr = array('3b13c700-c5bf-2136-d3b7-095792217e1d','90390b2d-1c96-ca19-a91b-d8ca00d8bab6','8eb2b1ee-66b2-0d06-8e71-3788d66ab05d','347b4cff-ee25-65af-f6c1-73eabb75e985','e8ad299d-c492-38a7-7667-94865c5ca617','9f36c872-17fd-15e7-82a4-c8dcee781dcb','d8f7f696-2e0b-6f66-040f-3b1ff479e744','71398e26-ffcb-9a17-3580-c4849d943a98');
            }else{ //嘉兴
                $shopCodeArr = array('5fcc4c1c-b8b9-7102-bf11-e883f8e79fd6','99bfd845-ae0b-eee8-7a27-243057388881','83b1a3d8-0735-0d4c-8b97-9f9aef6f588f','66703024-ec60-802a-b9d9-4c71c671ad54','0481838d-6555-5bc5-2057-de2aaa450e42','c6fcc5a9-3654-71ed-c236-6cdfa0df9584');
            }
            $shopMdl = new ShopModel();
            $joinTableArr = array(
                array('joinTable' => '(select * from (select shopCode,availablePrice,insteadPrice from BatchCoupon where couponType=3 AND isSend <> 1 AND isAvailable = 1 order by createTime desc) b group by shopCode) a', 'joinCondition' => 'a.shopCode = Shop.shopCode', 'joinType' => 'left'),
            );
            $shopList = $shopMdl->getShopList(array('Shop.shopCode' => array('IN', $shopCodeArr)), array('Shop.shopCode','Shop.shopName','Shop.logoUrl', 'a.availablePrice', 'a.insteadPrice'), $joinTableArr);
            $assign['shopList'] = $shopList;
            $this->assign($assign);
            $this->display('ActModule/getShopList');
        }else{
            $this->assign($assign);
            $this->display();
        }
    }

    /**
     * 品牌 H5
     */
    public function cGetBrandInfo() {
        $relId = I('get.relId');
        $isShared = I('get.isShared');
        $cityBrandRelMdl = new CityBrandRelModel();
        $relInfo = $cityBrandRelMdl->listRel(array('CityBrandRel.brandId', 'brandName', 'brandLogo', 'brandDes', 'CityBrandRel.linkType', 'CityBrandRel.content', 'CityBrandRel.cityBrandRelId', 'CityBrandRel.bgUrl', 'CityBrandRel.exButtonList'), array('cityBrandRelId' => $relId));
        $relInfo = $relInfo[0];
        if($relInfo['exButtonList']){
            $relInfo['exButtonList'] = json_decode($relInfo['exButtonList'], true);
        }
        $wxJsPackage = $this->wxApi->getSignPackage();
        $assign = $wxJsPackage;
        $assign['title'] = $relInfo['brandName'];
        $assign['relInfo'] = $relInfo;
        $assign['isShared'] = $isShared;
        $this->assign($assign);
        $this->display();
    }




}