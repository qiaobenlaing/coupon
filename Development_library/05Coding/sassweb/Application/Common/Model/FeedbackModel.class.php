<?php
namespace Common\Model;
use Think\Model;
/**
 * feedback表
 * @author
 */
class FeedbackModel extends BaseModel {
    protected $tableName = 'Feedback';

    /**
     * 添加反馈
     * @param string $creatorCode 创建者编码
     * @param string $content 内容
     * @param string $targetCode 目标指向编码
     * @return array
     */
    public function addFeedback($creatorCode, $content, $targetCode) {
        $rules = array(
            array('creatorCode', 'require', C('FEEDBACK.CREATOR_CODE_ERROR')),
            array('content', 'require', C('FEEDBACK.CONTENT_EMPTY')),
        );
        $data = array(
            'creatorCode' => $creatorCode,
            'content' => $content,
        );
        if($this->validate($rules)->create($data) != false) {
            $feedback = array(
                'feedbackCode' => $this->create_uuid(),
                'creatorCode' => $creatorCode,
                'createTime' => date('Y-m-d H:i:s', time()),
                'content' => $content,
                'status' => C('FEEDBACK_STATUS.UNREAD'),
                'targetCode' => $targetCode ? $targetCode : C('HQ_CODE'),
            );
            $code = $this->add($feedback) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 删除数据
     * @param number $feedbackCode 主键
     * @return boolean||string 删除成功返回true；删除失败返回错误信息
     */
    public function delFeedback($feedbackCode) {
        return $this->where(array('feedbackCode' => $feedbackCode))->delete() !== false ? true : '数据库删除数据失败';
    }

    /**
     * 获得商家反馈列表
     * @param $filterData
     * @param $page
     * @return array
     */
    public function listShopFeedback($filterData, $page) {
        $where = $this->filterWhere(
            $filterData
        );
        $where['targetCode'] = C('HQ_CODE');

        $subQuery = $this
            ->field(array(
                'Feedback.createTime',
                'left(content, 20)' => 'content',
                'shopCode',
                'shopName',
                'Shop.tel' => 'shopTel',
                'Shop.mobileNbr' => 'shopMobileNbr'))
            ->table('Feedback')
            ->join('Shop ON Shop.shopCode = Feedback.creatorCode')
            ->where($where)
            ->order('createTime desc')
            ->buildSql();
        $fdList = $this
            ->table($subQuery.' a')
            ->group('shopCode')
            ->order('createTime desc')
            ->pager($page)
            ->select();
        foreach($fdList as &$v){
            $v['unreadCount'] = $this->countUnreadFeedback($v['shopCode'], C('HQ_CODE'));
        }

        $fdNum = $this
            ->table($subQuery.' a')
            ->group('shopCode')
            ->select();

        return array(
            'fdList'=>$fdList,
            'totalNum'=>count($fdNum)?count($fdNum):0
        );
    }

    /**
     * 获得平台和某一商家的反馈详情
     * @param $shopCode
     * @return array
     */
    public function getOneShopFeedbackInfo($shopCode, $order = 'asc'){
        return $this
            ->field(array(
                    'createTime',
                    'creatorCode',
                    'targetCode',
                    'content'
                )
            )
            ->where('creatorCode is not null AND creatorCode <> "" AND targetCode is not null AND targetCode <> "" AND (creatorCode = "'.$shopCode.'" or targetCode = "'.$shopCode.'")')
            ->order('createTime '.$order)
            ->select();
    }

    /**
     * 获得顾客反馈列表
     * @param $filterData
     * @param $page
     * @return array
     */
    public function listUserFeedback($filterData, $page) {
        $where = $this->filterWhere(
            $filterData
        );
        $where['targetCode'] = C('HQ_CODE');

        $subQuery = $this
            ->field(array(
                'Feedback.createTime',
                'left(content, 20)' => 'content',
                'userCode',
                'mobileNbr',
                'realName'))
            ->table('Feedback')
            ->join('User ON User.userCode = Feedback.creatorCode')
            ->where($where)
            ->order('createTime desc')
            ->buildSql();
        $fdList = $this
            ->table($subQuery.' a')
            ->group('userCode')
            ->order('createTime desc')
            ->pager($page)
            ->select();
        foreach($fdList as &$v){
            $v['unreadCount'] = $this->countUnreadFeedback($v['userCode'], C('HQ_CODE'));
        }

        $fdNum = $this
            ->table($subQuery.' a')
            ->group('userCode')
            ->select();

        return array(
            'fdList'=>$fdList,
            'totalNum'=>count($fdNum)?count($fdNum):0
        );
    }

    /**
     * 获得平台和某一顾客的反馈详情
     * @param $userCode
     * @param string $order
     * @return array
     */
    public function getOneUserFeedbackInfo($userCode, $order = 'asc'){
        return $this
            ->field(array(
                    'createTime',
                    'creatorCode',
                    'targetCode',
                    'content'
                )
            )
            ->where('creatorCode is not null AND creatorCode <> "" AND targetCode is not null AND targetCode <> "" AND (creatorCode = "'.$userCode.'" or targetCode = "'.$userCode.'")')
            ->order('createTime '.$order)
            ->select();
    }

    /**
     * @param $creatorCode
     * @param $targetCode
     * @return bool
     */
    public function updateFeedbackStatus($creatorCode, $targetCode) {
        return $this->where(array('creatorCode'=>$creatorCode, 'targetCode'=>$targetCode, 'status'=>C('FEEDBACK_STATUS.UNREAD')))->save(array('status'=>C('FEEDBACK_STATUS.READ'))) != false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 获取未读反馈数量
     * @param $creatorCode
     * @param $targetCode
     * @return mixed
     */
    public function countUnreadFeedback($creatorCode, $targetCode) {
        $where['status'] = C('FEEDBACK_STATUS.UNREAD');
        $where['targetCode'] = $targetCode;
        if($creatorCode){
            $where['creatorCode'] = $creatorCode;
        }
        $ret = $this->where($where)->count('feedbackCode');
        return $ret?$ret:0;
    }
}