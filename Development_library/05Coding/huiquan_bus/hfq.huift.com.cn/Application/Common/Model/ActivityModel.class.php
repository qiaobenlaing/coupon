<?php
namespace Common\Model;
use Think\Model;
/**
 * activity表
 * @author
 */
class ActivityModel extends BaseModel {
    protected $tableName = 'Activity'; // 活动表
    private $maxRegisterNbrRequired = 5; // 活动单人最大报名人数

    /**
     * 判断用户是否可以报名活动
     * @param string $userCode 用户编码
     * @param string $actCode 活动编码
     * @param int $totalNbr 报名购买的票数量
     * @return array 格式：['code' => '结果', 'remaining' => '用户可购的数量']
     */
    public function isUserCanSignUpTheAct($userCode, $actCode, $totalNbr) {
        // 判断活动是否过期
        $actInfo = $this->field(array('endTime', 'registerNbrRequired', 'limitedParticipators'))->where(array('activityCode' => $actCode))->find();
        $limitedParticipators = $actInfo['limitedParticipators']; // 活动限制报名人数
        if(time() > strtotime($actInfo['endTime'])) {
            return array('code' => C('ACTIVITY.ACT_EXPIRED'), 'remaining' => 0);
        }

        // 判断用户报名人数是否超过活动单人最大报名人数
        $userActMdl = new UserActivityModel();
        $userSignedCount = $userActMdl->countTotalNbr(array('userCode' => $userCode, 'activityCode' => $actCode)); // 用户已经报名的人数
        if($totalNbr + $userSignedCount > $actInfo['registerNbrRequired']) {
            return array('code' => C('ACTIVITY.REG_NBR_REQUIRED_OVER'), 'remaining' => $actInfo['registerNbrRequired'] - $userSignedCount);
        }

        // 判断活动报名人数是否超限
        $participators = $userActMdl->countPersons($actCode); // 活动已经报名的人数
        if($totalNbr + $participators > $limitedParticipators && $limitedParticipators != 0) {
            $remaining = $limitedParticipators - $participators; // 用户可购数量
            return array('code' => C('ACTIVITY.LIMIT_NBR_OVER'), 'remaining' => $remaining);
        }
        return array('code' => true);
    }

    /**
     * 商家是否有活动
     * @param string $shopCode 商家编码
     * @return boolean 有返回true，没有返回false
     */
    public function isShopHasAct($shopCode) {
        $actInfo = $this
            ->field(array('activityCode'))
            ->where(array('shopCode' => $shopCode, 'endTime' => array('EGT', date('Y-m-d h:i:s', time())), 'pos' => array('NEQ', C('ACT_POS.SCROLL')), 'status' => array('IN', array(C('ACTIVITY_STATUS.ACTIVE')))))
            ->find();
        return $actInfo ? true : false;
    }

    /**
     * 获取首页滚动信息
     * @return array
     */
    public function getScrollInfo(){
//        $condition['unix_timestamp(endTime)'] = array('GT', time());
        $condition['endTime'] = array('GT',date('Y-m-d H:i:s',time()));
        $condition['status'] = C('ACTIVITY_STATUS.ACTIVE');
        $condition['pos'] = C('ACT_POS.SCROLL');
        $bankAct = $this->getActivityByBelonging($condition, C('ACTIVITY_BELONGING.BANK'));
        $platActList = $this->getActivityByBelonging($condition, C('ACTIVITY_BELONGING.PLAT'));
        $shopAct = $this->getActivityByBelonging($condition, C('ACTIVITY_BELONGING.SHOP'));
        return $platActList;
    }

    /**
     * 根据活动归属获得最新的一个活动信息
     * @param array $condition 条件
     * @param int $activityBelonging 活动归属。平台活动为1，银行活动为2，商户活动为3
     * @param int $limit 默认值为3
     * @return array 一维数组
     */
    public function getActivityByBelonging($condition, $activityBelonging, $limit = 3) {
        $this->field(array('activityCode', 'activityName', 'activityImg', 'createTime', 'shopCode', 'endTime', 'status'));
        if(!empty($condition)){
            $this->where($condition);
        }
        $this->where(array('activityBelonging' => $activityBelonging));
        $this->order('createTime desc');
        if(!empty($limit)){
            $this->limit($limit);
        }
        return $this->select();
    }

    /**
     * 获取活动列表
     * @param array $filterData
     * @param object $page 页码
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function getActivityList($filterData, $page){
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'activityName' => 'like', 'city' => 'like'),
            $page);
        $where = $this->secondFilterWhere($where);
        $field = array(
            'activityCode',
            'activityName',
            'LEFT(txtContent,20)' => 'txtContent',
            'activityLogo',
            'startTime',
            'endTime',
            'Activity.createTime',
            'Activity.status' => 'status',
            'shopName',
            'Shop.logoUrl' => 'shopLogo',
            'webUrl',
            'pos'
        );
        if($where['longitude'] && $where['latitude']) {
            $field['format(sqrt(power(Shop.longitude-' . $where['longitude'] . ',2) + power(Shop.latitude-' . $where['latitude'] . ', 2)), 2) *'.C('PROPORTION')] = 'distance';
        }
        unset($where['longitude']);
        unset($where['latitude']);

        $activityList = $this
            ->field($field)
            ->join('Shop ON Shop.shopCode = Activity.shopCode', 'LEFT')
            ->where($where)
            ->order('Activity.createTime desc, shopName')
            ->pager($page)
            ->select();

        $userActMdl = new UserActivityModel();
        foreach($activityList as &$v) {
            $actCode = $v['activityCode'];
            $v['participators'] = $userActMdl->countPersons($actCode);
        }
        return $activityList;
    }

    /**
     * 管理端活动总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countActivity($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'activityName' => 'like', 'city' => 'like'));
        $where = $this->secondFilterWhere($where);
        unset($where['longitude']);
        unset($where['latitude']);
        return $this
            ->join('Shop ON Shop.shopCode = Activity.shopCode', 'LEFT')
            ->where($where)
            ->count('distinct(activityCode)');
    }

    /**
     * 第二次过滤
     * @param array $where 过滤条件
     * @return array $where
     */
    public function secondFilterWhere(&$where) {
        if($where['shopCode']){
            $where['Shop.shopCode'] = $where['shopCode'];
            unset($where['shopCode']);
        }
        if ($where['status'] || $where['status'] == '0') {
            $where['Activity.status'] = $where['status'];
            unset($where['status']);
        }
        return $where;
    }

    /**
     * 增加一个活动
     * @param array $activityInfo 活动信息
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function editActivity($activityInfo) {
        $rules = array(
            array('activityName', 'require', C('ACTIVITY.NAME_ERROR')),
            array('startTime', 'require', C('ACTIVITY.START_TIME_ERROR')),
//            array('endTime', 'require', C('ACTIVITY.END_TIME_ERROR')),
            array('activityLocation', 'require', C('ACTIVITY.LOCATION_ERROR')),
//            array('txtContent', 'require', C('ACTIVITY.TXT_CONTENT_ERROR')),
            array('limitedParticipators', 'require', C('ACTIVITY.LIMITED_PARTICIPATORS_ERROR')),
            array('limitedParticipators', 'number', C('ACTIVITY.LIMITED_PARTICIPATORS_ERROR')),
//            array('isPrepayRequired', 'require', C('ACTIVITY.IS_PREPAY_REQUIRED_ERROR')),
//            array('prePayment', 'number', C('ACTIVITY.PREPAYMENT_ERROR')),
            array('isRegisterRequired', 'require', C('ACTIVITY.IS_REGISTER_REQUIRED_ERROR')),
//             array('activityLogo', 'require', C('ACTIVITY.ACTIVITY_LOGO_ERROR')),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_ERROR')),
            array('actType', 'require', C('ACTIVITY.ACT_TYPE_ERROR')),
            array('contactName', 'require', C('ACTIVITY.CONTACT_NAME_ERROR')),
            array('contactMobileNbr', 'require', C('ACTIVITY.CONTACT_MOBILE_ERROR')),
            array('creatorCode', 'require', C('ACTIVITY.CREATOR_CODE_ERROR')),
            array('activityBelonging', 'require', C('ACTIVITY.ACT_BELONGING_ERROR')),
            array('function', '', C('ACTIVITY.ACT_BELONGING_ERROR'),0,'unique',3),
        );
        if($activityInfo['isPrepayRequired'] == 1) {
            $rules[] = array('prePayment', 'require', C('ACTIVITY.PREPAYMENT_ERROR'));
        }
        if($activityInfo['totalPayment'] > \Consts::NO){
            $rules[] = array('refundRequired', 'require', C('ACTIVITY.REFUND_REQUIRED_ERROR'));
            $rules[] = array('registerNbrRequired', 'require', C('ACTIVITY.REGISTER_NBR_REQUIRED_ERROR'));
        }
        if($activityInfo['pos'] == C('ACT_POS.SCROLL')){
            $rules[] = array('activityImg', 'require', C('ACTIVITY.ACTIVITY_IMG_ERROR'));
            $rules[] = array('imgRate', 'require', C('ACTIVITY.ACTIVITY_IMG_RATE_ERROR'));
        }
        if(strtotime($activityInfo['endTime']) < strtotime($activityInfo['startTime'])) {
            return $this->getBusinessCode(C('ACTIVITY.START_GT_END'));
        }
        if($activityInfo['actType'] == 7){
            $rules[] = array('batchCouponCode', 'require', '优惠券类型不正确');
        }

        if($this->validate($rules)->create($activityInfo) != false) {
            if(isset($activityInfo['activityCode']) && !empty($activityInfo['activityCode'])){
                //c赋值优惠券code
                if(!empty($activityInfo['batchCouponCode']))
                    $batchCouponCode = $activityInfo['batchCouponCode'];
                unset($activityInfo['batchCouponCode']);
                //c方法名
                if(!empty($activityInfo['function']))
                    $function = $activityInfo['function'];
                unset($activityInfo['function']);


                $code = $this->where(array('activityCode' => $activityInfo['activityCode']))->save($activityInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');

                //c活动码未变,修改成功后couponactivity表的优惠券进行更改
                if(!empty($batchCouponCode)){
                    M('couponactivity')->where(array('activityCode' => $activityInfo['activityCode']))->save(array('batchCouponCode' => $batchCouponCode,'function' => $function));
                }
            }else{
                $activityInfo['activityCode'] = $this->create_uuid();
                $activityInfo['createTime'] = date('Y-m-d H:i:s', time());
                if(!isset($activityInfo['status'])){
                    $activityInfo['status'] = C('ACTIVITY_STATUS.ACTIVE');
                }
                //c赋值优惠券code
                if(!empty($activityInfo['batchCouponCode']))
                    $batchCouponCode = $activityInfo['batchCouponCode'];
                unset($activityInfo['batchCouponCode']);
                //c方法名
                if(!empty($activityInfo['function']))
                    $function = $activityInfo['function'];
                unset($activityInfo['function']);

                $code = $this->add($activityInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                if($code == C('SUCCESS') && $activityInfo['status'] == C('ACTIVITY_STATUS.ACTIVE')) { //新增成功且发布活动，则发商家广播
                    // 发商家广播
                    $msgMdl = new MessageModel();
                    $msgInfo = C('SPHO_BROADCASTING.NEW_TAC');
                    $msgMdl->shopBroadcasting($activityInfo['shopCode'], $msgInfo['TITLE'], $msgInfo['CONTENT']);
                    //c成功后把活动码与优惠券码放入couponactivity表
                    if(!empty($batchCouponCode))
                        $funcName = $this->getOnlyFunctionName();
                    M('couponactivity')->add(array('activityCode' => $activityInfo['activityCode'],'batchCouponCode' => $batchCouponCode,'function' => $funcName));
                }
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 修改活动状态
     * @param string $activityCode
     * @param int $status
     * @param int $isAdmin
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function changeActivityStatus($activityCode, $status, $isAdmin = 0) {
        $activityInfo = $this->getActInfo(array('activityCode' => $activityCode), array('Activity.activityCode', 'Activity.activityName', 'Activity.status',  'Shop.shopCode', 'Shop.shopName', 'Activity.endTime', 'limitedParticipators'), array(array('joinTable' => 'Shop', 'joinCondition' => 'Shop.shopCode = Activity.shopCode', 'joinType' => 'INNER')));
        if(empty($activityInfo)){
            return C('ACTIVITY.ACT_NOT_EXIST');
        }
        if($status == $activityInfo['status']){
            return C('ACTIVITY.ACT_STATUS_SAME');
        }
        $userActMdl = new UserActivityModel();
        if($isAdmin){ // 拥有修改所有状态的权限
            $code = $this->where(array('activityCode' => $activityCode))->save(array('status' => $status)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }else{
            if($status == C('ACTIVITY_STATUS.ACTIVE')){
                if($activityInfo['status'] == C('ACTIVITY_STATUS.DISABLE') || $activityInfo['status'] == C('ACTIVITY_STATUS.STOP_TO_JOIN')){
                    $code = $this->where(array('activityCode' => $activityCode))->save(array('status' => $status)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                }else{
                    $code = C('ACTIVITY.ACT_STATUS_ERROR');
                }
            }elseif($status == C('ACTIVITY_STATUS.STOP_TO_JOIN')){
                if($activityInfo['status'] == C('ACTIVITY_STATUS.ACTIVE')){
                    $code = $this->where(array('activityCode' => $activityCode))->save(array('status' => $status)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                }else{
                    $code = C('ACTIVITY.ACT_STATUS_ERROR');
                }
            }elseif($status == C('ACTIVITY_STATUS.CANCEL')){
                if($activityInfo['status'] == C('ACTIVITY_STATUS.ACTIVE') || $activityInfo['status'] == C('ACTIVITY_STATUS.FULL')){
                    //如果活动有已验证的，则不可取消
                    $info = $userActMdl->getUserActInfo(
                        array(
                            'UserActivity.activityCode' => $activityCode,
                            'UserActCode.status' => \Consts::USER_ACT_CODE_STATUS_USED
                        ),
                        array('userActCodeId'),
                        array(
                            array(
                                'joinTable' => 'UserActCode',
                                'joinCondition' => 'UserActCode.userActCode = UserActivity.userActivityCode',
                                'joinType' => 'inner',
                            )
                        )
                    );
                    if($info){
                        $code = C('ACTIVITY.ACT_STATUS_ERROR');
                    }else{
                        $code = $this->where(array('activityCode' => $activityCode))->save(array('status' => $status)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                    }
                }else{
                    $code = C('ACTIVITY.ACT_STATUS_ERROR');
                }
            }else{
                $code = C('ACTIVITY.ACT_STATUS_ERROR');
            }
        }
        if($code == C('SUCCESS')){
            if($status == C('ACTIVITY_STATUS.ACTIVE')){
                if($activityInfo['status'] == C('ACTIVITY_STATUS.DISABLE')){
                    $staString = '已发布';
                    //发布活动，发商家广播
                    $msgMdl = new MessageModel();
                    $msgInfo = C('SHOP_BROADCASTING.NEW_ACT');
                    $msgMdl->shopBroadcasting($activityInfo['shopCode'], $msgInfo['TITLE'], $msgInfo['CONTENT']);
                }elseif($activityInfo['status'] == C('ACTIVITY_STATUS.STOP_TO_JOIN')){
                    $staString = '重新开启报名';
                }
            }elseif($status == C('ACTIVITY_STATUS.STOP_TO_JOIN')){
                if($activityInfo['status'] == C('ACTIVITY_STATUS.ACTIVE')){
                     $staString = '现暂停报名中';
                }
            }elseif($status == C('ACTIVITY_STATUS.CANCEL')){ //TODO 退款部分
                if($activityInfo['status'] == C('ACTIVITY_STATUS.ACTIVE') || $activityInfo['status'] == C('ACTIVITY_STATUS.FULL')){
                    $staString = '被取消了';
                }
            }
            $content = str_replace('{{shopName}}', $activityInfo['shopName'], C('PUSH_MESSAGE.ACT_UPDATE'));
            $content = str_replace('{{activityName}}', $activityInfo['activityName'], $content);
            $content = str_replace('{{status}}', $staString, $content);

            $mobileNbrArr = $userActMdl->getUserActList(array('activityCode' => $activityCode, 'totalNbr' => array('gt', 0)), array('User.mobileNbr'), array(array('joinTable' => 'User', 'joinCondition' => 'User.userCode = UserActivity.userCode', 'joinType' => 'inner')));
            $receiver = array();
            if($mobileNbrArr){
                foreach($mobileNbrArr as $v){
                    if(!in_array($v['mobileNbr'], $receiver)){
                        array_push($receiver, $v['mobileNbr']);
                    }
                }
            }
            if($receiver){
                $jpushMdl = new JpushModel(C('CLIENT_APP_KEY'), C('CLIENT_MASTER_SECRET'));
                $jpushMdl->jPushByAction($receiver, $content, array('activityCode'=>$activityCode, 'activityName'=>$activityInfo['activityName']), C('PUSH_ACTION.ACT_UPDATE'));
            }
        }
        return $code;
    }

    /**
     * 获得活动列表
     * @param array $condition
     * @param array $field
     * @param array $joinTableArr
     * @param string $order
     * @param int $limit
     * @param int $page
     * @return mixed
     */
    public function getActList($condition = array(), $field = array(), $joinTableArr = array(), $order = '', $limit = 0, $page = 0) {

//        判断是否是惠圈管理人员
//        if($_SESSION['USER']['bank_id']!=-1){
//            $condition['shop.bank_id'] = "'".$_SESSION['USER']['bank_id']."'";
//        }

        if(empty($field)){
            $field = array('Activity.*');
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
            $userActMdl = new UserActivityModel();
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
                $participators = $userActMdl->countPersons($v['activityCode']);
                $activityList[$k]['participators'] = $participators;
                if(isset($v['status']) && $v['status'] == C('ACTIVITY_STACTIVEATUS.')){
                    if(strtotime($v['endTime']) < time()){
                        $activityList[$k]['status'] = C('ACTIVITY_STATUS.EXPIRED');
                    }elseif($v['limitedParticipators'] > \Consts::NO){
                        if($participators >= $v['limitedParticipators']){
                            $activityList[$k]['status'] = C('ACTIVITY_STATUS.FULL');
                        }
                    }
                }
            }
        }
        return $activityList;
    }

    /**
     * 获得查询数量
     * @param array $condition
     * @param array $joinTableArr
     * @return mixed
     */
    public function countActList($condition = array(), $joinTableArr = array()) {

        //        判断是否是惠圈管理人员
//        if($_SESSION['USER']['bank_id']!=-1){
//            $condition['shop.bank_id'] = "'".$_SESSION['USER']['bank_id']."'";
//        }

        if($joinTableArr){
            foreach($joinTableArr as $v){
                $this->join($v['joinTable'].' on '.$v['joinCondition'], $v['joinType']);
            }
        }
        if($condition){
            $this->where($condition);
        }
        return $this->count('distinct(Activity.activityCode)');
    }

    /**
     * 获取活动详情
     * @param $condition
     * @param array $field
     * @param array $joinTableArr
     * @param string $order
     * @return array|mixed
     */
    public function getActInfo($condition, $field = array(), $joinTableArr = array(), $order = '') {
        if(empty($field)){
            $field = array('Activity.*');
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
        $activityInfo = $this->find();
        if($activityInfo){
            $userActMdl = new UserActivityModel();
            if(isset($activityInfo['feeScale']) && $activityInfo['feeScale']){
                $feeScale = json_decode($activityInfo['feeScale'], true);
                $activityInfo['minPrice'] = $feeScale[0]['price'];
                foreach($feeScale as $fk => $fv){
                    if($fv['price'] < $activityInfo['minPrice']){
                        $activityInfo['minPrice'] = $fv['price'];
                    }
                    $feeScale[$fk]['price'] = $fv['price'] / \Consts::HUNDRED;
                }
                $activityInfo['feeScale'] = $feeScale;
            }elseif(isset($activityInfo['feeScale']) && empty($activityInfo['feeScale'])){
                $activityInfo['feeScale'] = array();
            }
            $activityInfo['participators'] = $userActMdl->countPersons($activityInfo['activityCode']);
            $activityInfo = $this->dividedByHundred($activityInfo, array('prePayment', 'totalPayment', 'minPrice'));
            if(isset($activityInfo['status']) && $activityInfo['status'] == C('ACTIVITY_STATUS.ACTIVE')){
                if(strtotime($activityInfo['endTime']) < time()){
                    $activityInfo['status'] = C('ACTIVITY_STATUS.EXPIRED');
                }elseif($activityInfo['limitedParticipators'] > \Consts::NO){
                    if($activityInfo['participators'] >= $activityInfo['limitedParticipators']){
                        $activityInfo['status'] = C('ACTIVITY_STATUS.FULL');
                    }
                }
            }
        }
        return $activityInfo;
    }

    /**
     * 增加浏览量
     * @param $activityCode
     * @return bool
     */
    public function incPageViews($activityCode){
        return $this->where(array('activityCode' => $activityCode))->setInc('pageviews', 1);
    }

    /**
     * 删除活动
     * @param $activityCode
     * @return array
     */
    public function delActivity($activityCode){
        $activityInfo = $this->getActInfo(array('activityCode' => $activityCode), array('status', 'endTime', 'limitedParticipators'));
        if(empty($activityInfo)){
            return $this->getBusinessCode(C('ACTIVITY.ACT_NOT_EXIST'));
        }
        if(!in_array($activityInfo['status'], array(C('ACTIVITY_STATUS.DISABLE'),C('ACTIVITY_STATUS.CANCEL')))){
            return $this->getBusinessCode(C('ACTIVITY.ACT_STATUS_ERROR'));
        }
        return $this->where(array('activityCode' => $activityCode))->save(array('status' => C('ACTIVITY_STATUS.DELETE'))) !== false ? $this->getBusinessCode(C('SUCCESS')) : $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
//        return $this->where(array('activityCode' => $activityCode))->delete() !== false ? $this->getBusinessCode(C('SUCCESS')) : $this->getBusinessCode(C('API_INTER_EXCEPTION'));
    }

    //判断活动有效且正在进行
    public function isTrueActivity($activityCode){
        if($activityCode){
            $onAct = M('activity')
                ->where(array('activityCode' => array('eq',$activityCode),'status' => array('eq',1),'isShow' => array('eq',1),'startTime' => array('elt',date('Y-m-d H:i:s',time())),'endTime' => array('egt',date('Y-m-d H:i:s',time()))))->count();
            return $onAct;
        }
    }
    /*c
     * 获取参加活动后优惠券订单最新价格
     * $userCode 用户code
     * $activityCode 活动code
     * $couponCode 优惠券code
     * $couponCodePrice 优惠券的价格
     * $couponNum 购买的优惠券数量
     * */
    public function getTruePrice($activityCode,$couponCode,$couponPrice,$couponNum){
       //确定提交上来的activitycode 是该优惠券的活动并且正在进行中；
        $function = M('couponactivity')->field('function')->where(array('activityCode' => $activityCode,'counpCode' => $couponCode))->find();
        $onAct = $this->isTrueActivity($activityCode);
        if($function['function'] && $onAct){ //活动存在且正在进行
            //根据活动码取出不同的活动的计算优惠价格的方法名；
            $funcName = $function['function'];
            $priceData = $this->$funcName($couponPrice,$couponNum);
            //统计商家/平台/银行让利；
            $truePrice = $priceData['newPrice'];
            $eachPrice = $priceData['eachPrice'];
            $data['activityCode'] = $activityCode;
            $data['discount'] = $priceData['discountAmount'];
            $data['status'] = 1;
            $amountDiscountCode = M('amountdiscount')->add($data);
            return array('orderAmount' => $truePrice,'amountDiscountId' => $amountDiscountCode,'eachPrice' => $eachPrice);
        }else{
            //活动不存在或过期返回原来总价
            return array('orderAmount' => $couponPrice*$couponNum,'amountDiscountId' => '','eachPrice' => $couponPrice);
        }
    }
    function getPrice_KYUFNS($couponPrice,$couponNum=1,$type=false){
        $totalPrice = $couponPrice*$couponNum;
        if($totalPrice >= 2000 && $totalPrice <5000){
            $newPrice = $totalPrice-500;
            $discountAmount = 500;
        }elseif($totalPrice >= 5000){
            $newPrice = $totalPrice*70/100 ;
            $discountAmount = $totalPrice-$newPrice;
        }else{
            $newPrice = $totalPrice;
            $discountAmount = 0;
        }
        if($type)
            //返回价格,让分变成元  /100
            return array('newPrice'=> $newPrice/100);
        else
            return array('newPrice'=> $newPrice,'discountAmount' => $discountAmount);
    }
    function getPrice_PSSELR($couponPrice,$couponNum=1,$type=false){
        $eachPrice = $couponPrice*80/100;
        $newPrice = $couponPrice*$couponNum*80/100;
        $discountAmount = $couponPrice*$couponNum-$newPrice;
        if($type)
            //返回价格,让分变成元  /100
            return array('newPrice'=> $newPrice/100);
        else
            return array('newPrice'=> $newPrice,'discountAmount' => $discountAmount,'eachPrice' => $eachPrice);
    }
    function getPrice_KOLFFM($couponPrice,$couponNum=1,$type=false){
        $eachPrice = $couponPrice-100;
        $newPrice = ($couponPrice-100)*$couponNum;
        $discountAmount = $couponPrice*$couponNum-$newPrice;
        if($type)
            //返回价格,让分变成元  /100
            return array('newPrice'=> $newPrice/100);
        else
            return array('newPrice'=> $newPrice,'discountAmount' => $discountAmount,'eachPrice' => $eachPrice);
    }
    function getPrice_FHDWTS($couponPrice,$couponNum=1,$type=false){
        $eachPrice = $couponPrice-100;
        $newPrice = ($couponPrice-100)*$couponNum;
        $discountAmount = $couponPrice*$couponNum-$newPrice;
        if($type)
            //返回价格,让分变成元  /100
            return array('newPrice'=> $newPrice/100);
        else
            return array('newPrice'=> $newPrice,'discountAmount' => $discountAmount,'eachPrice' => $eachPrice);
    }
    function getPrice_BBRGXR($couponPrice,$couponNum=1,$type=false)
    {
        $eachPrice = $couponPrice* 50 / 100;
        $newPrice = $couponPrice * $couponNum * 50 / 100;
        $discountAmount = $couponPrice * $couponNum - $newPrice;
        if ($type)
            //返回价格,让分变成元  /100
            return array('newPrice' => $newPrice / 100);
        else
            return array('newPrice' => $newPrice, 'discountAmount' => $discountAmount,'eachPrice' => $eachPrice);
    }
    function getPrice_MQIXQX($couponPrice,$couponNum=1,$type=false){
        $eachPrice = $couponPrice* 50 / 100;
        $newPrice = $couponPrice * $couponNum * 50 / 100;
        $discountAmount = $couponPrice * $couponNum - $newPrice;
        if ($type)
            //返回价格,让分变成元  /100
            return array('newPrice' => $newPrice / 100);
        else
            return array('newPrice' => $newPrice, 'discountAmount' => $discountAmount,'eachPrice' => $eachPrice);
    }
    function getPrice_CEMXIL($couponPrice,$couponNum=1,$type=false){
        $eachPrice = $couponPrice* 50 / 100;
        $newPrice = $couponPrice * $couponNum * 50 / 100;
        $discountAmount = $couponPrice * $couponNum - $newPrice;
        if ($type)
            //返回价格,让分变成元  /100
            return array('newPrice' => $newPrice / 100);
        else
            return array('newPrice' => $newPrice, 'discountAmount' => $discountAmount,'eachPrice' => $eachPrice);
    }
        /*c
         *参加活动时的退款
         *$activityCode 活动码
         * $couponCodePrice 优惠券价格
         * $couponNum 优惠券数量
         * $orderCode 订单号
         * */
    public function refund($activityCode,$couponPrice,$couponNum,$orderCode){
        //查询该笔订单中是否有优惠券被使用
        $isUsed = M('ordercoupon')->where(array('orderCode' => $orderCode,'status' => '30'))->select();
        $function = M('couponactivity')->field('function')->where(array('activityCode' => $activityCode))->find();

        $newPrice = $this->$function['function']($couponPrice,$couponNum,true);
        //化为分单位
        return $newPrice['newPrice']*100;
    }
    /*c
      * 改活动码状态
      *$userCode
      * $activityCode
      *$whichStatus 那个字段改状态status（验证码状态）/liquidationStatus（清算状态）
      * $statusValue 改成的值 0/1/2/3/4
      * */
    public function changeActCodeStatus($userCode,$activityCode,$whichStatus='status',$statusValue= 0){
        if(!$userCode || !$activityCode)  return false;
        $actCode = M('useractivity')
            ->alias('a')
            ->field('b.userActCodeId')
            ->join('LEFT JOIN __USERACTCODE__ b ON a.userActivityCode=b.userActCode')
            ->where(array('a.userCode' => $userCode,'a.activityCode' => $activityCode))
            ->find();
        $actCode[$whichStatus] = $statusValue;
        return  M('useractcode')->save($actCode) ? true : false ;
    }
    //生成唯一函数名
    protected function getOnlyFunctionName(){
        $prefix = 'getPrice_';
        $charArr = array('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
        $nameCode = '';
        for($i = 0;$i<6;$i++){
            $nameCode .= $charArr[array_rand($charArr,1)];
        }
        return $prefix.$nameCode;
    }

}