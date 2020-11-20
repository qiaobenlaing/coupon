<?php
namespace Common\Model;
use Think\Model;
use Common\Model\ShopModel;
/**
 * shopStaff表
 * @author
 */
class ShopStaffModel extends BaseModel {
    protected $tableName = 'ShopStaff';
    const DEFAULT_PWD = '123456';

    /**
     * 获得商家店员列表
     * @param array $condition 条件
     * @param array $field 查询的字段
     * @return array
     */
    public function getShopStaffList($condition, $field) {
        $condition = $this->filterWhere($condition);
        return $this->field($field)->where($condition)->select();
    }

    /**
     * 更新用户最后一次登录时间
     * @param string $userCode 用户编码
     * @return boolean 成功返回true，失败返回false
     */
    public function updateLastLoginTime($userCode) {
        return $this->where(array('staffCode' => $userCode))->save(array('lastLoginTime' => date('Y-m-d H:i:s'))) !== false ? true : false;
    }

    /**
     * 删除商家员工
     * @param string $shopCode 商家编码
     * @return boolean 成功返回true，失败返回false
     */
    public function delShopStaff($shopCode) {
        return $this->where(array('shopCode' => $shopCode))->delete() !== false ? true : false;
    }

    /**
     * 判断员工是否有添加，修改，删除员工的权限
     * @param string $staffCode 员工编码
     * @return array
     */
    public function isStaffHasPerms($staffCode) {
        $ret = $this->where(array('staffCode' => $staffCode, 'userLvl' => C('STAFF_LVL.MANAGER')))->getField('staffCode');
        $code = $ret ? C('YES') : C('NO');
        return $this->getBusinessCode($code);
    }

    /**
     * 二次过滤条件
     * @param array $where
     * @return array $where
     */
    public function secondFilter(&$where) {
        if($where['shopCode']) {
            $subWhere['_logic'] = 'OR';
            $subWhere['ShopStaffRel.shopCode'] = $where['shopCode'];
            $subWhere['ShopStaff.shopCode'] = $where['shopCode'];
            $where['_complex'] = $subWhere;
            unset($where['shopCode']);
        }
        if ($where['status'] || $where['status'] == '0') {
            $where['ShopStaff.status'] = $where['status'];
            unset($where['status']);
        }
        if($where['mobileNbr']){
            $where['ShopStaff.mobileNbr'] = $where['mobileNbr'];
            unset($where['mobileNbr']);
        }
        if($where['brandId']){
            $where['ShopStaff.brandId'] = $where['brandId'];
            unset($where['brandId']);
        }
        return $where;
    }

    /**
     * 获得商户员工列表
     * @param array $filterData
     * @param object $page 页码
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listShopStaff($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'realName' => 'like', 'mobileNbr' => 'like'),
            $page);
        $where = $this->secondFilter($where);
        return $this
                ->field(array('DISTINCT(ShopStaff.staffCode)', 'ShopStaff.mobileNbr' => 'mobileNbr', 'realName', 'wechatId', 'userLvl', 'ShopStaff.status' => 'status', 'avatarUrl', 'ShopStaff.brandId', 'ShopStaff.parentCode', 'isSendPayedMsg', 'ShopStaff.access'))
                ->join('ShopStaffRel ON ShopStaffRel.staffCode = ShopStaff.staffCode', 'left')
                ->join('Shop ON Shop.shopCode = ShopStaff.shopCode', 'LEFT')
                ->where($where)
                ->order('registerTime desc, shopName')
                ->pager($page)
                ->select();
    }

    /**
     * 管理端获得商户员工总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countShopStaff($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('shopName' => 'like', 'realName' => 'like', 'mobileNbr' => 'like'));
        $where = $this->secondFilter($where);
        return $this
                ->join('ShopStaffRel ON ShopStaffRel.staffCode = ShopStaff.staffCode', 'left')
                ->join('Shop ON Shop.shopCode = ShopStaff.shopCode', 'LEFT')
                ->where($where)
                ->count('DISTINCT(ShopStaff.staffCode)');
    }

    /**
     * 删除员工
     * @param string $staffCode 员工编码
     * @return array
     */
    public function delStaff($staffCode) {
        $code = $this->where(array('staffCode' => $staffCode))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 添加员工
     * @param array $staffInfo 员工信息
     * @return array
     */
    public function addStaff($staffInfo) {
        $staff = $this->where(array('mobileNbr' => $staffInfo['mobileNbr']))->find();
        if($staff){
            return $this->getBusinessCode(C('MOBILE_NBR.REPEAT'));
        }
        $rules = array(
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('realName', 'require', C('SHOP_STAFF.NAME_EMPTY')),
            array('userLvl', 'require', C('SHOP_STAFF.TYPE_EMPTY')),
            array('cfPassword', 'password', C('PWD.NOT_SAME'), 0, 'confirm'),
        );
        if((int)$staffInfo['mobileNbr'] == 0) {
            return $this->getBusinessCode(C('MOBILE_NBR.ERROR'));
        }
        if($this->validate($rules)->create($staffInfo) != false) {
            $staffCode = $this->create_uuid();
            $password = isset($staffInfo['password']) && $staffInfo['password'] ? $staffInfo['password'] : md5('123456');
            $shopStaffInfo = array(
                'staffCode' => $staffCode,
                'password' => md5(substr($staffCode, 0, 6).$password),
                'mobileNbr' => isset($staffInfo['mobileNbr']) ? $staffInfo['mobileNbr'] : '',
                'realName' => isset($staffInfo['realName']) ? $staffInfo['realName'] : '',
                'shopCode' => isset($staffInfo['shopCode']) ? $staffInfo['shopCode'] : '',
                'userLvl' => isset($staffInfo['userLvl']) ? $staffInfo['userLvl'] : C('STAFF_LVL.EMPLOYEE'),
                'status' => isset($staffInfo['status']) ? $staffInfo['status'] : C('STAFF_STATUS.ACTIVE'),
                'registerTime' => date('Y-m-d H:i:s', time())
            );
            $ret = $this->add($shopStaffInfo) !== false ? array('code' => C('SUCCESS'), 'staffCode' => $staffCode) : array('code' => C('API_INTERNAL_EXCEPTION'), 'staffCode' => '');
            return $ret;
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 修改商家员工
     * @param array $updateInfo 用户信息
     * @return array
     */
    public function updateStaff($updateInfo) {
        $rules = array(
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('realName', 'require', C('SHOP_STAFF.NAME_EMPTY')),
            array('userLvl', 'require', C('SHOP_STAFF.TYPE_EMPTY')),
            array('cfPassword', 'password', C('PWD.NOT_SAME'), 0, 'confirm'),
        );
        if($this->validate($rules)->create($updateInfo) != false) {
            $staffCode = $updateInfo['staffCode'];
            unset($updateInfo['staffCode']);
            // 根据员工编码获得员工的信息（手机号码）
            $staffInfo = $this->field(array('mobileNbr'))->where(array('staffCode' => $staffCode))->find();
            if($staffInfo) { // 判断要修改的员工对象是否存在
                if(isset($updateInfo['mobileNbr']) && $staffInfo['mobileNbr'] != $updateInfo['mobileNbr']) { // 如果要修改员工的手机号码，判断手机号码是否已经存在
                    $anotherStaff = $this->field(array('mobileNbr'))->where(array('mobileNbr' => $updateInfo['mobileNbr']))->find();
                    if($anotherStaff) {
                        return $this->getBusinessCode(C('MOBILE_NBR.REPEAT'));
                    }
                }
                // 保存修改信息
                $code = $this->where(array('staffCode' => $staffCode))->save($updateInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $code = C('SHOP_STAFF.NOT_EXIST'); // 要修改的员工对象不存在
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 管理端新增或者编辑商家员工信息
     * @param array $data 商家员工信息
     * @return array
     */
    public function editShopStaff($data) {
        $rules = array(
            array('brandId', 'require', C('SHOP_STAFF.BRAND_EMPTY')),
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
//            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
//            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('realName', 'require', C('SHOP_STAFF.NAME_EMPTY')),
            array('userLvl', 'require', C('SHOP_STAFF.TYPE_EMPTY')),
            array('cfPassword', 'password', C('PWD.NOT_SAME'), 0, 'confirm'),
        );
        if(empty($data['staffCode'])) {
            $rules[] = array('mobileNbr', '' , C('MOBILE_NBR.REPEAT'), 0, 'unique');
        }
        if($this->validate($rules)->create($data)) {
            unset($data['cfPassword']);
            $staffInfo = $this->field('staffCode')->where(array('staffCode' => $data['staffCode']))->find();
            if($data['staffCode'] && $staffInfo) {
                $shopStaffInfo  = $this->getShopStaffInfo(array('mobileNbr' => $data['mobileNbr']));
                if(! empty($shopStaffInfo) && $shopStaffInfo['staffCode'] != $data['staffCode']) {
                    return $this->getBusinessCode(C('MOBILE_NBR.REPEAT'));
                }
                if($data['password']) {
                    $data['password'] = md5(substr($data['staffCode'], 0, 6) . md5($data['password']));
                } else {
                    unset($data['password']);
                }
                $code = $this->where(array('staffCode' => $data['staffCode']))->save($data) !== false ? C('SUCCESS') : C('FAIL');
            } else {
                $data['staffCode'] = $this->create_uuid();
                $data['staffId'] = $data['mobileNbr'];
                $password = $data['password'] ? $data['password'] : self::DEFAULT_PWD;
                $data['password'] = md5(substr($data['staffCode'], 0, 6).md5($password));
                $data['registerTime'] = date('Y-m-d H:i:s', time());
                $code = $this->add($data) ? C('SUCCESS') : C('FAIL');
            }
            return array('code' => $code, 'staffCode' => $data['staffCode']);
        } else{
            return $this->getValidErrorCode();
        }
    }

    /**
     * 获取商家员工信息
     * @param array $condition 条件
     * @param array $field 要查询的字段
     * @return array
     */
    public function getShopStaffInfo($condition, $field = array()) {
        $field = empty($field) ? array('*') : $field;
        $shopStaff = $this->field($field)->where($condition)->find();
        return $shopStaff ? $shopStaff : array();
    }


    /**
     * 管理端修改商店员工状态
     * @param string $staffCode 员工编码
     * @param int $status 状态
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function changeShopStaffStatus($staffCode, $status) {
        return $this
            ->where(array('staffCode' => $staffCode))
            ->data(array('status' => $status))
            ->save() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
     * 更新商家员工密码
     * @param string $staffCode 员工编码
     * @param $newPwd
     * @return mixed
     */
    public function updateShopStaffPwd($staffCode, $newPwd) {
        return $this->where(array('staffCode' => $staffCode))->data(array('password' => $newPwd))->save() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }
}
