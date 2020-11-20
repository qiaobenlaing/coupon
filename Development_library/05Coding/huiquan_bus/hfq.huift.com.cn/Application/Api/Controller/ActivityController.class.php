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

class ActivityController extends ApiBaseController
{

    //获得某会员会员卡卡列表
    public   function showCardList($userCode, $zoneId)
    {

        $userCardModel = new UserCardModel();
        $userCardList =  $userCardModel->join('user ON user.userCode=usercard.userCode','right')
            ->join('card ON card.cardCode=usercard.cardCode','right')
            ->join('shop ON shop.shopCode=card.shopCode')
            ->join('backgroundtemplate ON card.bgImgCode = backgroundtemplate.bgCode')
            ->where(array('shop.bank_id' => $zoneId,'user.userCode'=>$userCode ,'shop.status' => 1,'usercard.status'=>1))
            ->field(array('shop.shopName,card.cardName,usercard.point,shop.logoUrl,backgroundtemplate.url as bgImgCode'))->select();

            if($userCardList){
                    return $userCardList;
            }else{
                return array('status'=>'error','msg'=>'暂无会员卡');
            }

    }

    //获得某张会员卡详情
    function showCard($userCode, $cardCode)
    {
        $cardComment = M('card')->field('*')->where(array('userCode' => $userCode, 'cardCode' => $cardCode))->find();
        return $cardComment;
    }

    //获取某用户某商店的会员卡
    function getShopCard($userCode,$shopCode){
        $card = M('card')
            ->field('card.*')
            ->join('LEFT JOIN usercard on card.cardCode=usercard.cardCode')
            ->where(array('card.shopCode' => $shopCode,'usercard.userCode' => $userCode))
            ->find();
        if($card)
            return $card;
        else
            return '没有该商店的会员卡';
    }
    //获取会员某商圈的总圈值
    function getAllPoint($userCode, $zoneId)
    {
        $allPoint = M('user')->field('currpoint allPoint')->where(array('userCode' => $userCode))->select();
        if ($allPoint) {
            return $allPoint;
        } else {
            return array('status'=>'error','msg'=>'暂无圈值');
        }
    }

    //会员获取圈值记录
    function getEachPoint($userCode, $zoneId)
    {
        $eachPoint = M('pointearninglog')->field('point,earningTime,reason')->where(array('userCode' => $userCode, 'zone' => $zoneId))->select();
        if ($eachPoint) {
            return $eachPoint;
        } else {
            return array('status'=>'error','msg'=>'暂无圈值记录');
        }
    }

    //获得商家活动
    function getActivities($shopCode)
    {
        $shopActive = M('activity')
            ->where(array('shipCode' => array('eq',$shopCode), 'status' =>  array('eq',1),'endTime' => array('egt',date('Y-m-d H:i:s',time()))))->select();
        if ($shopActive) {
            return $shopActive;
        } else {
            return '暂无活动';
        }
    }

    /*获取某商圈内的未到期活动列表,包括平台，银行，商家三种活动；
     *$zoneId 商圈
     * $pageNow 偏移量
     * $length 条目数
     * */
     function activity($zoneId,$pageNow=1,$length=8)
    {
        if(!$zoneId)  return false;
        $offset = ($pageNow-1)*$length;
        $activityList = M('activity')
            ->alias('a')
            ->field('a.*,b.shopCode,b.shopName')
            ->join('LEFT JOIN __SHOP__ b ON a.shopCode=b.shopCode')
            ->where(array('a.status' => array('eq',1),'a.endTime' => array('egt',date('Y-m-d H:i:s',time()))))
            ->where(array('b.bank_id' => $zoneId,'b.status' => 1))
            ->limit($offset,$length)
            ->select();
        if ($activityList) {
            return array('active' => $activityList, 'code' => 200);
        } else {
            return array('msg' => '该商圈暂无活动', 'code' => 300);
        }
        return $shopCode;
    }
    //获取某商圈内的未到期活动总数,包括平台，银行，商家三种活动；(做翻页)
    function getActivityCount($zoneId){
        if(!$zoneId)  return false;
        $activityCount = M('activity')
            ->alias('a')
            ->field('a.*,b.shopCode,b.shopName')
            ->join('LEFT JOIN __SHOP__ b ON a.shopCode=b.shopCode')
            ->where(array('a.status' => array('eq',1),'a.endTime' => array('egt',date('Y-m-d H:i:s',time()))))
            ->where(array('b.bank_id' => $zoneId,'b.status' => 1))
            ->count();
        return $activityCount;
    }
//    //h获取活动列表
//    function getActList()
//    {
//        $activity = new ActivityModel();
//        $res = $activity->getActList();
//        return $res;
//    }

    //收藏活动/取消收藏
    function collectActivity($userCode, $activityCode)
    {
        //判断活动是否已被收藏
        $where = array('userCode' => $userCode, 'activityCode' => $activityCode);
        $status = M('collectactivity')->field('status')->where($where)->find();
        if ($status !== false) {
            //表中没有该数据，添加收藏
            if (empty($status)) {
                $res = M('collectactivity')->add(array('userCode' => $userCode, 'activityCode' => $activityCode, 'status' => 1));
                if ($res) {
                    return '收藏成功';
                } else {
                    return '收藏失败';
                }
            }
            if ($status['status'] == 1) {
                //已收藏，取消收藏
                M('collectactivity')->where($where)->setDec('status');
                return $status['status'] - 1;
            }
            if ($status['status'] == 0) {
                //未收藏，添加收藏
                M('collectactivity')->where($where)->setInc('status');
                return $status['status'] + 1;
            }
        } else {
            return '收藏出错';
        }
    }

    //判断活动是否被收藏
    function isCollectActivity($activityCode, $userCode)
    {
        $where = array('userCode' => $userCode, 'activityCode' => $activityCode);
        $status = M('collectactivity')->field('status')->where($where)->find();
        if ($status == false) {
            return '系统错误';
        }
        if ($status['status'] == 0) {
            return '未收藏';
        }
        if ($status['status'] == 1) {
            return '已收藏';
        }
    }

    //获取用户某商圈内收藏的活动
    function getCollectActivity($userCode, $zoneId)
    {
        //先获取所在商圈商家活动activityCode
        $asData = $this->activity($zoneId);
        if ($asData['code'] !== 200)
            return $asData['msg'];
        //在这些activityCode里面查找出user收藏的activity
        if ($asData['active']) {
            //获取用户收藏的活动，与商圈内的活动做对比，取交集
            $userActive = M('collectactivity')->field('activityCode')->where(array('userCode' => $userCode, 'status' => 1))->select();
            if ($userActive) {
                $collectActivity = array();
                foreach ($asData['active'] as $v) {
                    foreach ($userActive as $v1) {
                        if ($v['activityCode'] == $v1['activityCode']) {
                            $collectActivity[] = $v;
                        }
                    }
                }
                return $collectActivity;
            } else {
                return '暂未收藏任何活动';
            }
        } else {
            return '该商圈暂无活动';
        }

    }

    //获取用户参加的活动（包含过期与未过期）
    //用户没有商圈的之分，只有商铺有，所以用户可能参加多个商圈的活动
    function getUserActivityList($userCode, $zoneId)
    {
        //获取该商圈的活动
        if(!$zoneId)  return false;
        $activityList = M('activity')
            ->alias('a')
            ->field('a.*,b.shopCode,b.shopName')
            ->join('LEFT JOIN __SHOP__ b ON a.shopCode=b.shopCode')
            ->where(array('a.status' => array('eq',1)))
            ->where(array('b.bank_id' => $zoneId,'b.status' => 1))
            ->select();
        if ($activityList) {
            //获取参加的活动
            $userActivity = M('useractivity')->where(array('userCode' => $userCode))->select();
            if ($userActivity) {
                $onActivity = array();
                foreach ($activityList as $v) {
                    foreach ($userActivity as $v1) {
                        if ($v['activityCode'] == $v1['activityCode']) {
                            $onActivity[] = $v;
                        }
                    }
                }
                return $onActivity;
            } else {
                return '暂时没有参加活动';
            }
        } else {
            return '该商圈暂无活动';
        }
    }

    //获取用户已退出的活动列表
    function backOut($userCode)
    {

    }

    //查看活动是否可以退款
    function activityGetBack($activityCode, $userCode)
    {
        $res = M('activity')->field('refundRequired')->where(array('activityCode' => $activityCode))->find();
        switch ($res['refundRequred']) {
            case 1 :
                return true;
                break;
            case 2 :
                $orderCode = M('useractivity')->filed('b.orderCode')
                    ->alias('a')->join('__USERACTCODE__ b on a.userActivityCode=b.uerActCodeId')
                    ->where(array('a.activityCode' => array('eq', $activityCode), 'a.ueserCode' => array('eq', $userCode)))
                    ->find();
                $payTime = M('userconsume')->field('payedTime')
                    ->where(array('orderCode' => $orderCode['orderCode']))
                    ->find();
                return (strtotime(substr($payTime['payedTime'], 0, 10)) + 86400) < time() ? true : false;
                break;
            case 3 :
                $orderCode = M('useractivity')->filed('b.orderCode')
                    ->alias('a')->join('__USERACTCODE__ b on a.userActivityCode=b.uerActCodeId')
                    ->where(array('a.activityCode' => array('eq', $activityCode), 'a.ueserCode' => array('eq', $userCode)))
                    ->find();
                $payTime = M('userconsume')->field('payedTime')
                    ->where(array('orderCode' => $orderCode['orderCode']))
                    ->find();
                return (strtotime($payTime['payedTime']) + 86400) < time() ? true : false;
                break;
            case 4 :
                $orderCode = M('useractivity')->filed('b.orderCode')
                    ->alias('a')->join('__USERACTCODE__ b on a.userActivityCode=b.uerActCodeId')
                    ->where(array('a.activityCode' => array('eq', $activityCode), 'a.ueserCode' => array('eq', $userCode)))
                    ->find();
                $payTime = M('userconsume')->field('payedTime')
                    ->where(array('orderCode' => $orderCode['orderCode']))
                    ->find();
                return (strtotime($payTime['payedTime']) + 86400 * 2) < time() ? true : false;
                break;
        }
    }

    //获取用户参加的正在进行的活动
    function getOnActivity($userCode)
    {
        if(!$userCode)  return false;
        $activityCode = M('useractivity')
            ->alias('a')
            ->field('b.*')
            ->join('LEFT JOIN __ACTIVITY__ b ON a.activityCode=b.activityCode')
            ->where(array('a.userCode' => array('eq',$userCode),'b.endTime' => array('egt',date('Y-m-d H:i:s',time()),'b.status' => array('eq',1))))
            ->select();
        if ($activityCode) {
            return $activityCode;
        } else {
            return '暂未参加任何活动';
        }
    }

    //获取某项活动的基本情况
    function getActivityComment($activityCode)
    {
        if(!$activityCode) return false;
        $activityData = M('activity')
            ->where(array('activityCode' => $activityCode, 'status' => 1))
            ->where(array('endTime' => array('egt',date('Y-m-d H:i:s',time()))))
            ->find();
        if ($activityData) {
            return $activityData;
        } else {
            return '出现错误';
        }
    }

    //获取用户参加活动的详细情况（参看活动验证码）
    function getUserActivity($userCode, $activityCode)
    {
        $activityComment = M('useractivity')
            ->alias('a')
            ->field('a.*,GROUP_CONCAT(b.actCode)')
            ->where(array('userCode' => $userCode, 'activityCode' => $activityCode))
            ->join('LEFT JOIN __USERACTCODE__ b ON a.userActivityCode=b.userActCode and b.status=4')
            ->find();
        if ($activityComment) {
            return $activityComment;
        } else {
            return '沒有该活动的具体信息';
        }
    }
    /**
     * 提交免费活动报名订单
     * @param string $userCode 用户编码
     * @param string $shopCode 商户编码
     * @param string $actCode 活动编码
     * @param string $orderInfo 订单详情。Json格式字符串，例如：[{"id": "1","nbr":"2","price":"70"},{"id": "2","nbr":"1","price":"50"}]。其中id是票种id；nbr是数量；price是票的价格，单位：元。
     * 其中id是票种id；nbr是数量；price是票的价格，单位：元。
     * @param string $bookingName 联系人姓名
     * @param string $mobileNbr 联系人有效电话
     * @param float $orderAmount 订单价格，单位：元
     * @param float $platBonus 平台红包，单位：元
     * @param float $shopBonus 商户红包，单位：元
     * @return array 一维关联数组 {'code' => '结果码'}
     */
    function addUserActivity($userCode, $shopCode, $actCode, $orderInfo, $bookingName, $mobileNbr, $orderAmount, $platBonus, $shopBonus)
    {
        // $orderInfo = json_decode($orderInfo, true); // json格式字符串转数组
        $totalNbr = 0; // 购买的票的总数
        foreach ($orderInfo as $v) {
            $totalNbr += $v['nbr']; // 累加票的数量
        }
        $orderAmount = $orderAmount * \Consts::HUNDRED; // 订单金额，单位元转化为分

        // 判断用户是否可以报名该活动
        $actMdl = new ActivityModel();
        $isUserCanSignUpTheAct = $actMdl->isUserCanSignUpTheAct($userCode, $actCode, $totalNbr);
        if ($isUserCanSignUpTheAct['code'] !== true) {
            return $isUserCanSignUpTheAct;
        }

        M()->startTrans();
        // 添加活动订单
        $orderData = array(
            'shopCode' => $shopCode, // 商家编码
            'clientCode' => $userCode, // 用户编码
            'orderAmount' => $orderAmount, // 订单金额，单位：分
            'receiver' => $bookingName, // 联系人姓名
            'receiverMobileNbr' => $mobileNbr, // 联系人电话
            'actualOrderAmount' => $orderAmount, // 订单最初消费金额
        );

        $consumeOrderMdl = new ConsumeOrderModel();
        $editActOrderRet = $consumeOrderMdl->editActOrder(array(), $orderData);
        $orderCode = $editActOrderRet['orderCode']; // 订单编码

        /**如果用户没有报名过该活动，则新添加报名记录；如果用户已经报名过该活动，则更新购买总票数即可 */
        $userActMdl = new UserActivityModel();
        $userActInfo = $userActMdl->getUserActInfo(array('userCode' => $userCode, 'activityCode' => $actCode));
        if (empty($userActInfo)) {
            // 添加新的用户活动记录
            $userActData = array(
                'activityCode' => $actCode, // 活动编码
                'userCode' => $userCode, // 用户编码
                'totalNbr' => $totalNbr, // 票的总数
            );
            $editUserActRet = $userActMdl->editUserAct(array(), $userActData);
            $userActCode = $editUserActRet['userActCode']; // 用户活动编码
        } else {
            // 更新用户要购买的票总数
            $incTotalNbrRet = $userActMdl->incField(array('userCode' => $userCode, 'activityCode' => $actCode), 'totalNbr', $totalNbr);
            $editUserActRet['code'] = $incTotalNbrRet != true ? C('API_INTERNAL_EXCEPTION') : C('SUCCESS');
            $userActCode = $userActInfo['userActivityCode']; // 用户活动编码
        }
        // 保存用户订单中购买的票的详情
        $editUserActCodeRet['code'] = C('SUCCESS');
        $userActCodeMdl = new UserActCodeModel();
        foreach ($orderInfo as $v) {
            for ($i = 0; $i < $v['nbr']; $i++) {
                $userActCodeData = array(
                    'userActCode' => $userActCode, // 用户活动编码
                    'scaleId' => $v['id'], // 票种id
                    'price' => $v['price'] * \Consts::HUNDRED, // 票的价格，单位：分
                    'orderCode' => $orderCode, // 订单编码
                    'liquidationStatus' => \Consts::LIQUIDATION_STATUS_HAD_NOT, // 清算状态
                );
                $editUserActCodeRet = $userActCodeMdl->updateUserActCode(array(), $userActCodeData);
            }
        }

        // 生成在线支付记录
        $userConsumeMdl = new UserConsumeModel();
        $bankcardPayRet = $userConsumeMdl->bankcardPay($orderCode, '', $platBonus, $shopBonus);

        if ($editActOrderRet['code'] != C('SUCCESS') || $editUserActRet['code'] != C('SUCCESS') || $editUserActCodeRet['code'] != C('SUCCESS') || $bankcardPayRet['code'] != C('SUCCESS')) {
            M()->rollback();
            $code = C('API_INTERNAL_EXCEPTION');
            $orderNbr = '';
            $consumeCode = '';
            $realPay = 0;
        } else {
            M()->commit();
            $code = C('SUCCESS');
            $orderNbr = $editActOrderRet['orderNbr'];
            $consumeCode = $bankcardPayRet['consumeCode'];
            $realPay = $bankcardPayRet['realPay'];
        }

        //因为是免费活动，修改订单状态为已付款，发放活动码
        // 修改活动码的状态为未验证，可用。并添加活动验证码
        M()->startTrans;
        $userActCodeMdl = new UserActCodeModel();
        $ret = $userActCodeMdl->addActCode($orderCode);

        // 更新订单支付状态为已支付
        $upOrderStatusRet = $consumeOrderMdl->updateConsumeOrder(array('orderCode' => $orderCode), array('status' => \Consts::PAY_STATUS_PAYED));

        // 更新用户支付状态为已支付和支付完成时间
        $retUserConsume = $userConsumeMdl->updateConsumeStatus(array('consumeCode' => $consumeCode), array('status' => \Consts::PAY_STATUS_PAYED, 'payedTime' => date('Y-m-d H:i:s')));
        if ($ret == true && $upOrderStatusRet == true && $retUserConsume == true) {
            M()->commit();
        }
        // 判断用户是否有该商店的会员卡，没有则添加
        $userCardMdl = new UserCardModel();
        $userCardMdl->checkUserCard($userCode, $shopCode);
        $code = C('SUCCESS');
        return array('code' => $code, 'orderCode' => $orderCode);
    }

    //免费活动，直接退出；
    //$actCode活动验证码,$refundMsg退款原因；
    function cancelActivity($actCodes, $refundMsg)
    {

        if ($actCodes && is_array($actCodes)) {
            //判断是部分退还是全部退
            $userActData = M('useractcode')->field('userActCode,orderCode')->where(array('actCode' => $actCodes[0]))->find();
            $dataActCodes = M('useractcode')->field('actCode')
                ->where(array('userActCode' => array('eq', $userActData['userActCode']), 'status' => array('eq', 4)))->select();
            $orderInfo = M('consumeorder')->field('clientCode')->where(array('orderCode' => $userActData['orderCode']))->find();
            if (count($dataActCodes) == count($actCodes)) {
                //说明是全退
                //改已参加的活动表
                M('useractivity')->where(array('userActivityCode' => $userActData['userActCode']))->delete();
                //改验证码状态
                M('useractcode')->where(array('actCode' => array('in', $actCodes)))->setField('status', '2');
                //改用户支付记录状态
                M('userconsume')->where(array('orderCode' => $userActData['orderCode'], 'consumerCode' => $orderInfo['clientCode']))->setField('status', 7);
                //改订单
                M('consumeorder')->where(array('orderCode' => $userActData['orderCode'], 'consumerCode' => $orderInfo['clientCode']))->setField('status', 7);
                return "success1";
            } else {
                //说明是部分退
                //改已参加的活动表改参加详情（大人，小孩）
                // M('useractivity')->where(array('userActivityCode' => $userActCode))->delete();
                //改验证码状态
                M('useractcode')->where(array('actCode' => array('in', $actCodes)))->setField('status', '2');
                //改用户订单状态
                M('userconsume')->where(array('orderCode' => $userActData['orderCode'], 'consumerCode' => $orderInfo['clientCode']))->setField('status', 7);
                //改订单
                M('consumeorder')->where(array('orderCode' => $userActData['orderCode'], 'consumerCode' => $orderInfo['clientCode']))->setField('status', 8);
                return "success2";
            }
        } else {
            return '参数错误';
        }
    }
    //活动提醒
    //获取退款原因，退款验证码列表
    function getReason($actCode)
    {
        //根据一个验证码查到该订单下的所有验证码
        $userActCode = M('useractcode')->where(array('actCode' => $actCode))->getField('userActCode');
        $actData = M('useractcode')
            ->field('actCode')
            ->where(array('userActCode' => $userActCode['userActCode'], 'status' => 4))
            ->select();
        return $actData;
    }

    //判断用户还有多少活动验证码可领
    function getCodeNum($userCode, $activityCode)
    {
        $userActivity = M('useractivity')
            ->field('totalNbr')
            ->where(array('userCode' => $userCode, 'activityCode' => $activityCode))
            ->find();
        //未参加过该活动
        if ($userActivity) {

        }
        //已参加过该活动
    }


    /*取出某优惠券正在进行的活动
     * $batchCouponCode 优惠券code
     * */
    function getCouponActivity($batchCouponCode){
        if(!$batchCouponCode) return false;
        $activityInfo = M('couponactivity')
            ->alias('a')
            ->field('b.*')
            ->join('LEFT JOIN __ACTIVITY__ b ON a.activityCode=b.activityCode')
            ->where(array('a.batchCouponCode' => array('eq',$batchCouponCode),'b.status' => array('eq',1),'b.isShow' => array('eq',1),'b.startTime' => array('elt',date('Y-m-d H:i:s',time())),'b.endTime' => array('egt',date('Y-m-d H:i:s',time()))))
            ->select();
        if($activityInfo)
            return $activityInfo;
        else
            return C('ACTIVITY.ACT_NOT_EXIST');
    }

    /*参加活动（new）,一人只能参加一次，生成的活动码只有一个，活动码没用掉的话不能继续参加活动领码
     * $activityCode 活动code
     * $userCode 用户code
     * */
    function joinActivity($activityCode,$userCode){
        if(!$activityCode or !$userCode)  return   false;
        //判断用户是否已有某活动能正常使用的活动码
        $isTrue = M('useractivity')
            ->alias('a')
            ->field('b.actCode')
            ->join('LEFT JOIN __USERACTCODE__ b ON a.userActivityCode=b.userActCode')
            ->where(array('a.activityCode' => $activityCode,'a.userCode' => $userCode))
            ->where(array('b.status'=>array('eq','4'),'b.liquidationStatus'=>array('in','0,1')))
            ->find();
        if($isTrue) return '已参加活动领过活动码';
        //未参加活动,参加活动，发放活动码
        M()->startTrans();
        //判断该活动人数是否达到上限
        //活动名额总数量
        $actCodeTotal = M('activity')->field('limitedParticipators')->find($activityCode);
        if($actCodeTotal['limitedParticipators'] != 0){
            //取出所有的活动验证码（能用和用过）
            $actCodesNum = M('useractivity')
                ->alias('a')
                ->field('b.actCode')
                ->join('LEFT JOIN __USERACTCODE__ b ON a.userActivityCode=b.userActCode')
                ->where(array('a.activityCode' => array('eq',$activityCode),'b.status' => array('in','3,4') ))
                ->count();
            if($actCodesNum >= $actCodeTotal['limitedParticipators']){
                //已达到活动总人数
                return '已达到活动总人数';
            }
        }
        $userActMdl = new UserActivityModel();
        $userActCodeMdl = new UserActCodeModel();
        // 添加新的用户活动记录
        $userActData = array(
            'activityCode' => $activityCode, // 活动编码
            'userCode' => $userCode, // 用户编码
            'totalNbr' => 1, // 票的总数
        );
        $editUserActRet = $userActMdl->editUserAct(array(), $userActData);
        //添加活动验证码
        $actCode = $userActCodeMdl->setActCode();
//        return $editUserActRet;
        $userActCodeData = array(
            'userActCode' => $editUserActRet['userActCode'],
            'price' => 0,
            'actCode' => $actCode,
            'buyTime' => time(),
            'status' => 4,
        );
        $res = $userActCodeMdl->add($userActCodeData);
        if(!$editUserActRet && !$res){
            M()->rollbaci();
            return '领取失败';
        }
        else{
            M()->commit();
            return '领取成功';
        }
    }
    /*获取某用户参加的某商家某商品的活动
     *$userCode 用户code
     * $couponCode 优惠券code
     * */
    function getUserJoinCouponActivity($userCode,$couponCode){
        //获取优惠券参加的活动
        $activityCodes = M('couponactivity')->where(array('couponCode' => $couponCode))->getField('activityCode',true);
        $userActivityCodes = M('useractivity')
            ->alias('a')
            ->field('c.*')
            ->join('LEFT JOIN __USERACTCODE__ b ON  a.userActivityCode=b.userActCode')
            ->join('LEFT JOIN __ACTIVITY__ c ON  a.activityCode=c.activityCode')
            ->where(array('a.activityCode' => array('in',$activityCodes),array('a.userCode' => array('eq',$userCode),'b.status' => array('eq',4),'b.liquidationStatus' => array('eq',0)),'c.status' => array('eq',1),'c.endTime' => array('egt',date('Y-m-d H:i:s',time()))))
            ->select();
        if($userActivityCodes)
            return  $userActivityCodes;
        else
            return  '未参加该商品活动或该商品暂时没有活动';
 }
 //获取参加活动后的商品最新价格
 function getPrice($activityCode,$couponNum){
    $couponNum = $couponNum ? $couponNum : 1;
    $couponPrice = M('batchcoupon')
         ->alias('a')
         ->field('a.payPrice')
         ->join('LEFT JOIN __COUPONACTIVITY__ b ON b.batchCouponCode=a.batchCouponCode')
         ->where(array('b.activityCode' => $activityCode))
         ->find();
     //判断活动有效且正在进行
     $onAct = D('Activity')->isTrueActivity($activityCode);
     if(!$onAct) return $couponPrice['payPrice']/100;
     $function = M('couponactivity')->field('function')->where(array('activityCode' => $activityCode))->find();
     $activityModel = D('Activity');
     $newPrice = $activityModel->$function['function']($couponPrice['payPrice'],$couponNum,true);
     return $newPrice['newPrice'];
}

//店铺端获取活动相关数据
//获取商家某次活动完成的让利总额
function getShopTotalDiscount($activityCode,$shopCode){
    //确定该活动属于该商家
    $has = M('activity')->where(array('activityCode' => $activityCode,'shopCode' => $shopCode))->find();
    if(!$has)
        return '数据错误';
    $totalDiscount = M('amountdiscount')->field('SUM(discount) totalDiscount')->where(array('activityCode' => $activityCode,'status' => '2'))->group('activityCode')->find();
    if(!$totalDiscount['totalDiscount'])
        $totalDiscount['totalDiscount'] = 0;
    return $totalDiscount;
    }
function getTotalActivityDiscount($shopCode){
    //获取该商家的全部活动码
    $activityCodes = M('activity')->where(array('shopCode' => $shopCode))->getField('activityCode',true);
    $allDiscount = 0;
    $totalDiscount = M('amountdiscount')->field('SUM(discount) totalDiscount')->where(array('activityCode' => array('in',$activityCodes),'status' => '2'))->find();
    return $totalDiscount;
}
}
