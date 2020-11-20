<?php
namespace Common\Model;
use Think\Model;
use Common\Model\JpushModel;
/**
 * userActivity表
 * @author
 */
class UserActivityModel extends BaseModel {
    protected $tableName = 'UserActivity';

    /**
     * 获得某个字段的值
     * @param array $con 条件，一维关联数组
     * @param string $field 字段名称
     * @return mixed
     */
    public function getUserActFieldInfo($con, $field) {
        return $this->where($con)->getField($field);
    }

    /**
     * 获得已经报名的人数
     * @param array $condition 条件，一维关联数组
     * @return int
     */
    public function countTotalNbr($condition) {
        return $this->where($condition)->sum('totalNbr');
    }

    /**
     * 获取用户活动详情
     * @param array $condition 条件，一维关联数组
     * @param array $field 字段，一维关联数组
     * @param array $joinTableArr join的表，二维数组
     * @param string $order 排序
     * @return array|mixed
     */
    public function getUserActInfo($condition, $field = array(), $joinTableArr = array(), $order = '') {
        if(empty($field)){
            $field = array('UserActivity.*');
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        if($order){
            $this->order($order);
        }
        $userActivityInfo = $this->find();
        if($userActivityInfo){
            if(isset($userActivityInfo['feeScale']) && $userActivityInfo['feeScale']){
                $feeScale = json_decode($userActivityInfo['feeScale'], true);
                $userActivityInfo['minPrice'] = $feeScale[0]['price'];
                foreach($feeScale as $fk => $fv){
                    if($fv['price'] < $userActivityInfo['minPrice']){
                        $userActivityInfo['minPrice'] = $fv['price'];
                    }
                    $feeScale[$fk]['price'] = $fv['price'] / \Consts::HUNDRED;
                }
                $userActivityInfo['feeScale'] = $feeScale;
            }elseif(isset($userActivityInfo['feeScale']) && empty($userActivityInfo['feeScale'])){
                $userActivityInfo['feeScale'] = array();
            }
            if(isset($userActivityInfo['activityCode']) && $userActivityInfo['activityCode']){
                $userActivityInfo['participators'] = $this->countPersons($userActivityInfo['activityCode']);
            }
            $userActivityInfo = $this->dividedByHundred($userActivityInfo, array('prePayment', 'totalPayment', 'minPrice'));
        }
        return $userActivityInfo;
    }

    /**
     * 获得用户活动列表
     * @param array $condition
     * @param array $field
     * @param array $joinTableArr
     * @param string $order
     * @param int $limit
     * @param int $page
     * @return mixed
     */
    public function getUserActList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0) {
        if(empty($field)){
            $field = array('UserActivity.*');
        }
        $this->field($field);
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        if($order){
            $this->order($order);
        }
        if($limit){
            $this->limit($limit);
        }
        if($page){
            $this->page($page);
        }
        $activityList = $this->select();
        if($activityList){
            foreach($activityList as $k => $v){
                if(isset($v['feeScale']) && $v['feeScale']){
                    $feeScale = json_decode($v['feeScale'], true);
                    $minPrice = $feeScale[0]['price'];
                    foreach($feeScale as $fk => $fv){
                        if($fv['price'] < $minPrice){
                            $minPrice = $fv['price'];
                        }
                        $feeScale[$fk]['price'] = $fv['price'] / \Consts::HUNDRED;
                    }
                    $activityList[$k]['feeScale'] = $feeScale;
                    $activityList[$k]['minPrice'] = $minPrice / \Consts::HUNDRED;
                }elseif(isset($v['feeScale']) && empty($v['feeScale'])){
                    $activityList[$k]['feeScale'] = array();
                }

                if(isset($v['prePayment']) && $v['prePayment']){
                    $activityList[$k]['prePayment'] = $v['prePayment'] / \Consts::HUNDRED;
                }
                if(isset($v['totalPayment']) && $v['totalPayment']){
                    $activityList[$k]['totalPayment'] = $v['totalPayment'] / \Consts::HUNDRED;
                }
                if(isset($v['activityCode']) && $v['activityCode']){
                    $activityList[$k]['participators'] = $this->countPersons($v['activityCode']);
                }
            }
        }
        return $activityList;
    }

    /**
     * 增加某个字段的值
     * @param array $condition 条件，一维索引数组
     * @param string $field 字段
     * @param int $nbr 数量
     * @return boolean 成功，返回true；失败，返回false
     */
    public function incField($condition, $field, $nbr) {
        return $this->where($condition)->setInc($field, $nbr) !== false ? true : false;
    }

    /**
     * 修改用户报名活动信息
     * @param array $condition 条件，一维索引数组
     * @param array $data 数据，一维索引数组
     * @return array $ret 一维索引数组 {'code' => '结果码', 'userActCode' => '用户活动编码'}
     */
    public function editUserAct($condition, $data) {
        $rules = array(
            array('adultM', 'require', C('USER_ACT.ADULT_M_ERROR')),
            array('adultF', 'require', C('USER_ACT.ADULT_F_ERROR')),
            array('kidM', 'require', C('USER_ACT.KID_M_ERROR')),
            array('kidF', 'require', C('USER_ACT.KID_F_ERROR')),
        );
        if($this->validate($rules)->create($data)) {
            if($condition) {
                $code = $this
                    ->where($condition)
                    ->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $data['userActivityCode'] = $this->create_uuid();
                $data['createTime'] = time();
                $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'userActCode' => $data['userActivityCode'] ? $data['userActivityCode'] : '');
        } else {
            return $this->getValidErrorCode();
        }
    }
     /**
     * 获得查询数量
     * @param array $condition
     * @param array $joinTableArr
     * @return mixed
     */
    public function countUserActList($condition = array(), $joinTableArr = array()) {
        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('distinct(UserActivity.userActivityCode)');
    }

    /**
     * 统计活动参与人数
     * @param string $actCode 活动编码
     * @return string $personCount
     */
    public function countPersons($actCode) {
        $personCount = $this->where(array('activityCode' => $actCode))->sum('totalNbr');
        return $personCount ? $personCount : '0';
    }

    /**
     * 退出活动
     * @param string $userActCode 用户活动编码
     * @return array $ret
     */
    public function exitAct($userActCode) {
        $userAct = $this->getUserActInfo(array('userActivityCode' => $userActCode));
        $code = $this->where(array('userActivityCode' => $userActCode))->delete() !== false ? C('SUCCESS') : C('API_INTER_EXCEPTION');
        if($code == C('SUCCESS')){
            $actMdl = new ActivityModel();
            $userMdl = new UserModel();
            $activityInfo = $actMdl->getActInfo(array('activityCode' => $userAct['activityCode']), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
            $userInfo = $userMdl->getUserInfo(array('userCode' => $userAct['userCode']), array('nickName','realName'));
            $userName = $userInfo['realName']?$userInfo['realName']:$userInfo['nickName'];
            $content = str_replace('{{userName}}', $userName, C('PUSH_MESSAGE.EXIT_ACT'));
            $content = str_replace('{{shopName}}', $activityInfo['shopName'], $content);
            $content = str_replace('{{activityName}}', $activityInfo['activityName'], $content);
            $receiver = explode('-', $activityInfo['shopCode']);
            $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
            $jpushMdl->jPushByAction($receiver, $content, array(), C('PUSH_ACTION.EXIT_ACTIVITY'));
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得用户报名的活动列表
     * @param string $userCode 用户编码
     * @param double $longitude 用户经度
     * @param double $latitude 用户纬度
     * @param object $page 页码对象
     * @return array $userActList
     */
    public function listUserAct($userCode, $longitude, $latitude, $page) {
        $userActList = $this
            ->field(array(
                'userActivityCode',
                'Activity.activityCode',
                'activityLogo',
                'activityName',
                'shopName',
                'sqrt(power(Shop.longitude-'.$longitude.',2) + power(Shop.latitude-'.$latitude.',2))' => 'distance',
                'signUpTime'
            ))
            ->join('Activity ON Activity.activityCode = UserActivity.activityCode')
            ->join('Shop ON Shop.shopCode = Activity.shopCode')
            ->where(array('userCode' => $userCode))
            ->pager($page)
            ->select();
        foreach($userActList as &$v) {
            $v['distance'] = intval($v['distance'] * C('PROPORTION'));
            $v['personCount'] = $this->where(array('activityCode' => $v['activityCode']))->sum('adultM + adultF + kidM + kidF');
        }
        return $userActList;
    }

    /**
     * 获得用户报名的活动列表
     * @param string $userCode 用户编码
     * @return array $userActList
     */
    public function countUserAct($userCode) {
        $userActCount = $this
            ->join('Activity ON Activity.activityCode = UserActivity.activityCode')
            ->join('Shop ON Shop.shopCode = Activity.shopCode')
            ->where(array('userCode' => $userCode))
            ->count('userActivityCode');
        return $userActCount;
    }

    /**
     * 判断用户是否报名该活动
     * @param string $userCode 用户编码
     * @param string $actCode 活动编码
     * @return int $isUserJoinAct 是返回true，否返回false
     */
    public function isUserJoinAct($userCode, $actCode) {
        $isUserJoinAct = $this->where(array('userCode' => $userCode, 'activityCode' => $actCode))->find() ? true : false;
        return $isUserJoinAct;
    }

    /**
     * 活动报名
     * @param string $userCode 用户编码
     * @param string $activityCode 活动编码
     *  @param int $adultM 大人男性参与人数
     * @param int $adultF 大人女性参与人数
     * @param int $kidM 小孩男性参与人数
     * @param int $kidF 小孩女性参与人数
     * @return array
     */
    public function joinActivity($userCode, $activityCode, $adultM, $adultF, $kidM, $kidF) {
        $rules = array(
            array('userCode', 'require', C('USER_ACT.USER_CODE_ERROR')),
            array('activityCode', 'require', C('USER_ACT.ACT_CODE_ERROR')),
            array('adultM', 'require', C('USER_ACT.ADULT_M_ERROR')),
            array('adultF', 'require', C('USER_ACT.ADULT_F_ERROR')),
            array('kidM', 'require', C('USER_ACT.KID_M_ERROR')),
            array('kidF', 'require', C('USER_ACT.KID_F_ERROR')),
            array('adultM', 'number', C('USER_ACT.ADULT_M_ERROR')),
            array('adultF', 'number', C('USER_ACT.ADULT_F_ERROR')),
            array('kidM', 'number', C('USER_ACT.KID_M_ERROR')),
            array('kidF', 'number', C('USER_ACT.KID_F_ERROR')),
        );
        $userActivity = $this->where(array('userCode' => $userCode, 'activityCode' => $activityCode))->find();
        if($userActivity){
            return $this->getBusinessCode(C('ACTIVITY.REPEAT'));
        }
        $data = array(
            'userCode' => $userCode,
            'activityCode' => $activityCode,
            'adultM' => $adultM,
            'adultF' => $adultF,
            'kidM' => $kidM,
            'kidF' => $kidF
        );
        if($this->validate($rules)->create($data)) {
            $data['userActivityCode'] = $this->create_uuid();
            $data['signUpTime'] = date('Y-m-d H:i:s', time());
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            if($code == C('SUCCESS')){
                $actMdl = new ActivityModel();
                $userMdl = new UserModel();
                $activityInfo = $actMdl->getActInfo(array('activityCode' => $activityCode), array('Activity.*', 'Shop.shopName'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
                $userInfo = $userMdl->getUserInfo(array('userCode' => $userCode), array('nickName','realName'));
                $userName = $userInfo['realName']?$userInfo['realName']:$userInfo['nickName'];
                $content = str_replace('{{userName}}', $userName, C('PUSH_MESSAGE.JOIN_ACT'));
                $content = str_replace('{{shopName}}', $activityInfo['shopName'], $content);
                $content = str_replace('{{activityName}}', $activityInfo['activityName'], $content);
                $receiver = explode('-', $activityInfo['shopCode']);
                $jpushMdl = new JpushModel(C('SHOP_APP_KEY'), C('SHOP_MASTER_SECRET'));
                $jpushMdl->jPushByAction($receiver, $content, array(), C('PUSH_ACTION.JOIN_ACTIVITY'));
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 修改用户活动
     * @param $where
     * @param $data
     * @return array
     */
    public function updateUserActivity($where, $data){
        if($where){
            $code = $this->where($where)->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }else{
            $data['createTime'] = time();
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 增加参与活动的人数
     * @param $userActivityCode
     * @param $value
     * @return bool
     */
    public function incActivityTotalNbr($userActivityCode, $value) {
        return $this
            ->where(array('userActivityCode' => $userActivityCode))
            ->setInc('totalNbr', $value) !== false ? true : false;
    }

    /**
     * 减少参与活动的人数
     * @param $userActivityCode
     * @param $value
     * @return bool
     */
    public function decActivityTotalNbr($userActivityCode, $value) {
        return $this
            ->where(array('userActivityCode' => $userActivityCode))
            ->setDec('totalNbr', $value) !== false ? true : false;
    }
}