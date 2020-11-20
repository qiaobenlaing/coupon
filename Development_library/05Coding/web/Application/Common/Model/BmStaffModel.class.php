<?php
namespace Common\Model;
use Think\Model;
/**
 * bmStaff表
 * @author 
 */
class BmStaffModel extends BaseModel {
    protected $tableName = 'BMStaff';

    /**
     * 管理端修改操作员状态
     * @param string $staffCode 操作员编码
     * @param int $status 状态。1-启用；0-禁用
     * @return true|string 成功放回true;失败返回错误信息
     */
    public function changeBmStaffStatus($staffCode, $status) {
        return $this
            ->where(array('staffCode' => $staffCode))
            ->data(array('status' => $status))
            ->save() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

    /**
    * 删除数据
    * @param number $bmStaffCode 主键
    * @return boolean||string 删除成功返回true；删除失败返回错误信息
    */
    public function delBmStaff($bmStaffCode) {
        return $this->where(array('bmStaffCode' => $bmStaffCode))->delete() !== false ? true : '数据库删除数据失败';
    }

    /**
     * 管理端用户操作日志列表
     * @param array $filterData
     * @param number $page 偏移值
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listBmStaff($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('realName' => 'like', 'mobileNbr' => 'like'),
            $page);
        return $this
            ->where($where)
            ->pager($page)
            ->select();
    }

    /**
     * 管理端用户操作日志总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countBmStaff($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('realName' => 'like', 'mobileNbr' => 'like')
        );
        return $this
            ->where($where)
            ->count('staffCode');
    }

    /**
    * 根据主键得到数据详情
    * @param number $bmStaffCode 主键
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function getBmStaff($bmStaffCode) {
        return $this->where(array('staffCode' => $bmStaffCode))->find();
    }
    
    /**
    * 修改苞米员工信息
    * @param array $data 关联数组
    * @return boolean||string
    */
    public function editBmStaff($data) {
        $rules = array(
            array('mobileNbr', 'require', C('MOBILE_NBR.EMPTY')),
            array('mobileNbr', 'number', C('MOBILE_NBR.ERROR')),
            array('mobileNbr', C('MOBILE_NBR.LENGTH'), C('MOBILE_NBR.ERROR'), 0, 'length'),
            array('realName', 'require', C('SHOP_STAFF.NAME_EMPTY')),
            array('cfPassword', 'password', C('PWD.NOT_SAME'), 0, 'confirm'),
        );
        if(! $data['staffCode']) {
            $rules[] = array('mobileNbr', '' , C('MOBILE_NBR.REPEAT'), 0, 'unique');
        }
        if($this->validate($rules)->create()) {
            unset($data['cfPassword']);
            if($data['staffCode']) {
                if($data['password']) {
                    $data['password'] = md5(substr($data['staffCode'], 0, 6).md5($data['password']));
                } else {
                    unset($data['password']);
                }
                return $this->where(array('staffCode' => $data['staffCode']))->save($data) !== false ? true : C('FAIL');
            } else {
                $data['staffCode'] = $this->create_uuid();
                $data['password'] = $data['password'] ? $data['password'] : '123456';
                $data['password'] = md5(substr($data['staffCode'], 0, 6).md5($data['password']));
                return $this->add($data) !== false ? true : C('FAIL');
            }
        } else{
            return $this->getError();
        }
    }

    /**
     * 根据条件获取BM员工信息
     * @param array $condition 条件
     * @return array
     */
    public function getBMStaffInfo($condition) {
        $bmStaff = $this->where($condition)->find();
        return $bmStaff ? $bmStaff : array();
    }

    public function updateBMStaffPwd($staffCode, $newPwd){
        return $this->where(array('staffCode' => $staffCode))->data(array('password' => $newPwd))->save() ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
    }

}