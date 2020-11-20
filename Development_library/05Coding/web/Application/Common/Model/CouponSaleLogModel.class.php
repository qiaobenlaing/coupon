<?php
namespace Common\Model;
use Think\Model;
use Common\Model\UserCouponModel;
/**
 * couponSaleLog表
 * @author
 */
class CouponSaleLogModel extends BaseModel {
    protected $tableName = 'CouponSaleLog';

    /**
     * 转让/分享优惠券
     * @param string $sellerCode 出让/被索取者编码
     * @param string $buyerCode 受让/被索取者编码
     * @param string $batchCouponCode 出让的优惠券编码
     * @param int $price 价格
     * @return array
     */
    public function transferCoupon($sellerCode, $buyerCode, $batchCouponCode, $price=0){
        $batchCouponMdl = new \Common\Model\BatchCouponModel();
        $ret = $batchCouponMdl->getCouponInfo($batchCouponCode);
        if(! $ret) {
            return $this->getBusinessCode(C('COUPON.NOT_EXIST'));
        }
        if($buyerCode){
            $userCouponMdl = new \Common\Model\UserCouponModel();
            $ret = $userCouponMdl->isUserCouponCanBeSent($sellerCode, $batchCouponCode, $buyerCode);
            if($ret !== true) {
                return $this->getBusinessCode($ret);
            }
            $status = C('COUPON_SALE_STATUS.SUCCESS');
            // TODO 添加转让优惠券信息
        } else {
            $status = C('COUPON_SALE_STATUS.WAITING');
        }
        $transferInfo = array(
            'logCode' => $this->create_uuid(),
            'sellerCode' => $sellerCode,
            'buyerCode' => $buyerCode,
            'couponCode' => $batchCouponCode,
            'price' => $price,
            'status' => $status
        );
        $code = $this->add($transferInfo) ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);

    }

    /**
     * 索取优惠券请求
     * @param string $sellerCode 出让/被索取者编码
     * @param string $buyerCode 受让/索取者编码
     * @param string $batchCouponCode 出让的优惠券编码
     * @param int $price 价格
     * @return array
     */
    public function extortCouponRequest($sellerCode, $buyerCode, $batchCouponCode, $price=0) {
        $userCouponMdl = new \Common\Model\UserCouponModel();
        // 判断索取者是否可以索取被索取者的优惠券
        $ret = $userCouponMdl->isUserCouponCanBeExtort($sellerCode, $buyerCode, $batchCouponCode);
        if($ret !== true) {
            return $this->getBusinessCode($ret);
        }
        // 是否已经请求、索要过了
        $couponSaleLogInfo = $this->field('logCode')->where(array('sellerCode' => $sellerCode, 'buyerCode' => $buyerCode, 'couponCode' => $batchCouponCode, 'status' => C('COUPON_SALE_STATUS.WAITING')))->find();
        if($couponSaleLogInfo) {
            return $this->getBusinessCode(C('COUPON.HAS_BEEN_EXTORT_REQUEST'));
        }
        $ret = $this->add(array( 'logCode' => $this->create_uuid(), 'sellerCode' => $sellerCode, 'buyerCode' => $buyerCode, 'couponCode' => $batchCouponCode, 'price' => $price, 'status' => C('COUPON_SALE_STATUS.WAITING')));
        if($ret) {
            $userMdl = new UserModel();
            $sellerInfo = $userMdl->getUserInfo(array('userCode' => $sellerCode), array('nickName'));
            $shopMdl = new ShopModel();
            $shopName = $shopMdl->getShopNameBybatchCouponCode($batchCouponCode);
            $batchCouponMdl = new BatchCouponModel();
            $batchCouponInfo = $batchCouponMdl->getCouponInfo($batchCouponCode);

            // 添加索取者索取优惠券消息
            $requestContent = str_replace(array('{{seller}}', '{{shopName}}', '{{couponName}}', '{{nbr}}'), array($sellerInfo['nickName'], $shopName, $batchCouponInfo['couponName'], 1), C('COUPON_MSG_TDL.REQUEST'));
            $msgInfo = array(
                'msgCode' => $this->create_uuid(),
                'title' => C('MSG_TITLE_TDL.REQUEST_COUPON'),
                'content' => $requestContent,
                'createTime' => date('Y-m-d H:i:s'),
                'senderCode' => $buyerCode,
                'type' => C('MESSAGE_TYPE.COUPON'),
            );
            $msgMdl = new MessageModel();
            if($msgMdl->addMsg($msgInfo, $buyerCode)) {
                // 添加被索取者被索取优惠券消息
                $buyerInfo = $userMdl->getUserInfo(array('userCode' => $buyerCode), array('nickName'));
                $beenRequestContent = str_replace(array('{{buyer}}', '{{shopName}}', '{{couponName}}', '{{nbr}}'), array($buyerInfo['nickName'], $shopName, $batchCouponInfo['couponName'], 1), C('COUPON_MSG_TDL.BEEN_REQUEST'));
                $msgInfo = array(
                    'msgCode' => $this->create_uuid(),
                    'title' => C('MSG_TITLE_TDL.BEEN_REQUEST_COUPON'),
                    'content' => $beenRequestContent,
                    'createTime' => date('Y-m-d H:i:s'),
                    'senderCode' => $buyerCode,
                    'type' => C('MESSAGE_TYPE.COUPON'),
                );
                $code = $msgMdl->addMsg($msgInfo, $buyerCode) != false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                return $this->getBusinessCode($code);
            }
            return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
        }
        return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
    }

    /**
     * 索取优惠券应答
     * @param string $logCode 交易记录编码
     * @param number $result，1表示同意，-1表示不同意
     * @return array
     */
    public function extortCouponReply($logCode, $result) {
        $logInfo = $this->where(array('logCode' => $logCode, 'status' => C('COUPON_SALE_STATUS.WAITING')))->find();
        // 判断是否已经处理过请求
        if(! $logInfo) {
            return $this->getBusinessCode(C('COUPON.HAS_REPLY'));
        }
        if($result == C('COUPON_SALE_STATUS.SUCCESS')) {
            // 判断用户是否可以赠送他的优惠券，是否更新优惠券的拥有者
            $userCouponMdl =new \Common\Model\UserCouponModel();
            $ret = $userCouponMdl->isUserCouponCanBeSent($logInfo['sellerCode'], $logInfo['couponCode'], $logInfo['buyerCode']);
            if($ret !== true) {
                return $this->getBusinessCode($ret);
            }

            // 添加同意赠送优惠券消息
            $msgMdl = new MessageModel();
            $content = C('COUPON_MSG_TDL.SEND');
            $msgInfo = array(
                'msgCode' => $this->create_uuid(),
                'title' => C('MSG_TITLE_TDL.SEND_COUPON'),
                'content' => $content,
                'createTime' => date('Y-m-d H:i:s'),
                'senderCode' => $logInfo['sellerCode'],
                'type' => C('MESSAGE_TYPE.COUPON'),
            );
            $ret = $msgMdl->addMsg($msgInfo, $logInfo['buyerCode']);
            if($ret === false) {
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }
        }
        $code = $this->where(array('logCode' => $logCode))->save(array('status' => $result)) ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 领取别人分享的优惠券
     * @param string $logCode 优惠券交易记录编码
     * @param string $buyerCode 用户编码
     * @return array
     */
    public function receiveCoupon($logCode, $buyerCode){
        $logInfo = $this->where(array('logCode' => $logCode))->find();
        if($logInfo['buyerCode']) {
            return $this->getBusinessCode(C('COUPON.BEEN_TOKEN_AWAY'));
        }
        $userCouponMdl = new \Common\Model\UserCouponModel();
        $ret = $userCouponMdl->isUserCouponCanBeSent($logInfo['sellerCode'], $logInfo['couponCode'], $buyerCode);
        if( $ret !== true) {
            return $this->getBusinessCode($ret);
        }
        $ret = $this->where(array('logCode' => $logCode))->save(array('buyerCode' => $buyerCode, 'status' => C('COUPON_SALE_STATUS.SUCCESS')));
        if($ret != false) {
            // 添加领取优惠券的消息
            $msgMdl = new MessageModel();
            $content = C('COUPON_MSG_TDL.GRAB');
            $msgInfo = array(
                'msgCode' => $this->create_uuid(),
                'title' => C('MSG_TITLE_TDL.GRAB_COUPON'),
                'content' => $content,
                'createTime' => date('Y-m-d H:i:s'),
                'senderCode' => $logInfo['buyerCode'],
                'type' => C('MESSAGE_TYPE.COUPON'),
            );
            $code = $msgMdl->addMsg($msgInfo, $logInfo['buyerCode']) ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            return $this->getBusinessCode($code);
        }
        return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
    }

    /**
     * 删除数据
     * @param number $logCode 主键
     * @return array 删除成功返回true；删除失败返回错误信息
     */
    public function delCouponSaleLog($logCode) {
        $code = $this->where(array('logCode' => $logCode))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 数据列表
     * @param array $condition 条件
     * @param $page 分页
     * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
     */
    public function listCouponSaleLog($condition, $page) {
        return $this->where($condition)
            ->pager($page)
            ->select();
    }

    /**
     * 根据主键得到数据详情
     * @param number $logCode 记录编码
     * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
     */
    public function getCouponSaleLog($logCode) {
        return $this->where(array('logCode' => $logCode))->find();
    }
}