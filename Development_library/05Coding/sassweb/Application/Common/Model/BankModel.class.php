<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2018/9/20
 * Time: 10:42
 */

namespace Common\Model;


class BankModel extends BaseModel
{
    protected $tableName = 'Bank';

    /**
     * 编辑品牌
     * @param array $data
     * @return array
     */
    public function editBank($data) {

        $rules = array(
            array('name','require','名称必须填写！'), //默认情况下用正则进行验证
            array('status','require','状态必须选择！'), //默认情况下用正则进行验证
        );

        if (!$this->validate($rules)->create($data)){
            // 对data数据进行验证
             return  $this->getError();
        }else{
            // 验证通过 可以进行其他数据操作
            if(empty($data['id'])) {
                $data['read_time'] = date('Y-m-d H:i:s',time());
                $ret = $this->add($data);
                $data['id'] = $ret;
            } else {
                $ret = $this->where(array('id' => $data['id']))->save($data);
            }
            $code = $ret === false ? C('API_INTERNAL_EXCEPTION') : C('SUCCESS');
            return array('code' => $code, 'id' => $data['id']);
        }
    }

    /**
     * 获得品牌列表
     * @param array $where 条件
     * @param array $field 查询字段
     * @return array
     */
    public function getBankList($where, $field) {
        $where = $this->filterWhere($where);
        $bankList = $this->field($field)->where($where)->select();
        return $bankList;
    }

    /**
     * 获得品牌信息
     * @param array $where 条件
     * @param array $field 查询字段
     * @return array
     */
    public function getBankInfo($where, $field) {
        $where = $this->filterWhere($where);
        $bankInfo = $this->field($field)->where($where)->find();
        return $bankInfo;
    }

    /**
     * 第二次过滤条件
     * @param array $where 条件
     * @return array $where
     */
    private function secondFilter(&$where) {
        return $where;
    }

    /**
     * 管理端用户列表
     * @param array $filterData
     * @param number $page 偏移值
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组array(array());
     */
    public function listBank($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('name' => 'like'),
            $page);

        //判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1) {
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $where = $this->secondFilter($where);
        return $this
            ->field(array('*'))
            ->where($where)
            ->pager($page)
            ->select();
    }

    /**
     * 管理端用户总数
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countBank($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('name' => 'like')
        );

        //判断是否为惠圈管理人员
        if($_SESSION['USER']['bank_id']!=-1) {
            $where['bank_id'] = $_SESSION['USER']['bank_id'];
        }

        $where = $this->secondFilter($where);
        return $this
            ->where($where)
            ->count('id');
    }
}