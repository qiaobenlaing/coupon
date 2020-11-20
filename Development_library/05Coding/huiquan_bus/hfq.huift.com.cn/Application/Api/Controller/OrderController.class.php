<?php
/**
 * 收藏商家列表
 */
/**
 * 商店端API Controller
 * User: Weiping
 * Date: 2015-04-20
 * Time: 23:49
 */
namespace Api\Controller;
use Common\Model\BankAccountModel;
use Common\Model\BaseModel;
use Common\Model\ClassRemarkImgModel;
use Common\Model\ClassRemarkModel;
use Common\Model\ClassWeekInfoModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\CouponRuleModel;
use Common\Model\MessageRecipientModel;
use Common\Model\OrderCouponModel;
use Common\Model\OrderProductModel;
use Common\Model\PcAppLogModel;
use Common\Model\PPRelModel;
use Common\Model\ProductCategoryModel;
use Common\Model\ProductModel;
use Common\Model\RefundLogModel;
use Common\Model\ShopAppLogModel;
use Common\Model\ShopApplyEntryModel;
use Common\Model\ShopClassModel;
use Common\Model\ShopDecorationModel;
use Common\Model\BatchCouponModel;
use Common\Model\ShopDeliveryModel;
use Common\Model\ShopHeaderModel;
use Common\Model\ShopHonorModel;
use Common\Model\ShopPhotoModel;
use Common\Model\ShopRecruitModel;
use Common\Model\ShopStaffRelModel;
use Common\Model\ShopTeacherModel;
use Common\Model\StudentStarModel;
use Common\Model\StuStarWorkModel;
use Common\Model\SubAlbumModel;
use Common\Model\SystemParamModel;
use Common\Model\TeacherWorkModel;
use Common\Model\UserActCodeModel;
use Common\Model\UserActivityModel;
use Common\Model\FeedbackModel;
use Common\Model\UserBonusModel;
use Common\Model\UserCardModel;
use Common\Model\ShopStaffModel;
use Common\Model\ShopModel;
use Common\Model\BankAccountLocalLogModel;
use Common\Model\CardModel;
use Common\Model\UserCouponModel;
use Common\Model\BonusModel;
use Common\Model\ActivityModel;
use Common\Model\UserConsumeModel;
use Common\Model\CommunicationModel;
use Common\Model\UserMessageModel;
use Common\Model\PosServerModel;
use Common\Model\JpushModel;
use Common\Model\MessageModel;
use Common\Model\BonusStatisticsModel;
use Common\Model\UserModel;
use Common\Model\UtilsModel;
use JPush\Exception\APIRequestException;
use Common\Model\UserEnterShopInfoRecordModel;
use \Think\Cache\Driver\Memcache;
use  Org\aliyun\api_demo\WxPayData;

class OrderController extends ApiBaseController
{
    /*获取某用户的订单列表
     *
     * */
    public function getOrderList($userCode){
        $orderList = M('consumeorder')->where(array('clientCode' => $userCode))->select();
        return $orderList;
    }
    /*根据券码跳转到退款订单
     *
     * */
    public function getOrderRefund($userCouponNbr){

        //查订单
        $orderCode = M('usercoupon')
            ->alias('a')
            ->field('b.orderCode')
            ->join('LEFT JOIN __ORDERCOUPON__ b ON a.orderCouponCode = b.orderCouponCode')
            ->where(array('a.userCouponNbr' => array('eq',$userCouponNbr),'a.status' => array('eq',1)))
            ->find();
        //查该订单下的券
        $orderData = M('ordercoupon')
            ->alias('a')
            ->field('a.orderCode,a.couponCode,b.status status')
            ->join('LEFT JOIN __USERCOUPON__ b ON a.couponCode = b.userCouponNbr')
            ->where(array('a.orderCode' => array('eq',$orderCode['orderCode'])))
            ->select();

        return $orderData;
    }

}
