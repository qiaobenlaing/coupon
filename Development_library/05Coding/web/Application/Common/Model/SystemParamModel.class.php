<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: huafei.ji
 * Date: 15-9-25
 * Time: 下午4:08
 */
class SystemParamModel extends BaseModel {
    protected $tableName = 'SystemParam';

    /**
     * 获得参数的值
     * @param string $param 参数的英文描述
     * @return array
     */
    public function getParamValue($param) {
        return $this->field(array('value'))->where(array('param' => $param))->find();
    }

    /**
     * 删除系统参数
     * @param int $id 主键ID
     * @return boolean 成功返回true，失败返回false
     */
    public function delSystemParam($id) {
        return $this->where(array('id' => $id))->delete() !== false ? true : false;
    }

    /**
     * 获得顾客端所有系统参数列表
     * @return array $systemParamList 二维数组，{{'id', 'param', 'value', 'createTime', 'zhParam'},...}
     */
    public function listAllShopSystemParam() {
        $systemParamList = $this
            ->field(array('param', 'value'))
            ->where(array('appType' => C('APP_TYPE.SHOP')))
            ->order('createTime desc')
            ->select();
        return $systemParamList;
    }

    /**
     * 获得顾客端所有系统参数列表
     * @return array $systemParamList 二维数组，{{'id', 'param', 'value', 'createTime', 'zhParam'},...}
     */
    public function listAllClientSystemParam() {
        $systemParamList = $this
            ->field(array('param', 'value'))
            ->where(array('appType' => C('APP_TYPE.CLIENT')))
            ->order('createTime desc')
            ->select();
        return $systemParamList;
    }

    /**
     * 编辑系统参数
     * @param array $data 数据
     * @return boolean || string 成功返回true，失败返回错误信息或者false
     */
    public function editSystemParam($data) {
        $rules = array(
            array('appType', 'require', C('SYSTEM_PARAM.APP_TYPE_EMPTY')),
            array('param', 'require', C('SYSTEM_PARAM.PARAM_EMPTY')),
            array('param', '', C('SYSTEM_PARAM.PARAM_REPEAT'), 0, 'unique'),
            array('zhParam', 'require', C('SYSTEM_PARAM.ZH_PARAM_EMPTY')),
            array('value', 'require', C('SYSTEM_PARAM.VALUE_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            if(!empty($data['id'])) {
                return $this->where(array('id' => $data['id']))->save($data) !== false ? true : C('API_INTERNAL_EXCEPTION');
            } else {
                $data['createTime'] = date('Y-m-d H:i:s');
                return $this->add($data) !== false ? true : C('API_INTERNAL_EXCEPTION');
            }
        } else {
            return $this->getError();
        }
    }

    /**
     * 获得系统参数详情
     * @param int $id 主键ID
     * @return array
     */
    public function getSystemParamInfo($id) {
        return $this->where(array('id' => $id))->find();
    }

    /**
     * 获得顾客端系统参数列表
     * @param array $filterData
     * @param object $page
     * @return array $systemParamList 二维数组，{{'id', 'param', 'value', 'createTime', 'zhParam'},...}
     */
    public function listClientSystemParam($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array(),
            $page);
        $where = $this->secondClientFilterWhere($where);
        $systemParamList = $this
            ->field(array('id', 'param', 'value', 'createTime', 'zhParam', 'paramType'))
            ->where($where)
            ->order('createTime desc')
            ->pager($page)
            ->select();
        return $systemParamList;
    }

    /**
     * 获得顾客端系统参数总数
     * @param array $filterData
     * @return int $systemParamCount
     */
    public function CountClientSystemParam($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array(),
            $page);
        $where = $this->secondClientFilterWhere($where);
        $systemParamCount = $this
            ->where($where)
            ->count('id');
        return $systemParamCount;
    }

    public function secondClientFilterWhere(&$where) {
        $where['appType'] = 'c';//顾客端
//        if ($where['startTime'] && $where['endTime']) {
//            $where['createTime'] = array('BETWEEN', array($where['startTime'], $where['endTime']));
//        } elseif ($where['startTime'] && !$where['endTime']) {
//            $where['createTime'] = array('EGT', $where['startTime']);
//        } elseif (!$where['startTime'] && $where['endTime']) {
//            $where['createTime'] = array('ELT', $where['endTime']);
//        }
//        unset($where['startTime']);
//        unset($where['endTime']);
        return $where;
    }

    /**
     * 获得商家端系统参数列表
     * @param array $filterData
     * @param object $page
     * @return array $crashLogList 二维数组，{{'id', 'param', 'value', 'createTime', 'zhParam'},...}
     */
    public function listShopSystemParam($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array(),
            $page);
        $where = $this->secondShopFilterWhere($where);
        $systemParamList = $this
            ->field(array('id', 'param', 'value', 'createTime', 'zhParam', 'paramType'))
            ->where($where)
            ->order('createTime desc')
            ->pager($page)
            ->select();
        return $systemParamList;
    }

    /**
     * 获得商家端系统参数总数
     * @param array $filterData
     * @return int $crashLogCount
     */
    public function CountShopSystemParam($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array(),
            $page);
        $where = $this->secondShopFilterWhere($where);
        $crashLogCount = $this
            ->where($where)
            ->count('id');
        return $crashLogCount;
    }

    public function secondShopFilterWhere(&$where) {
        $where['appType'] = 's';//商家端
//        if ($where['startTime'] && $where['endTime']) {
//            $where['createTime'] = array('BETWEEN', array($where['startTime'], $where['endTime']));
//        } elseif ($where['startTime'] && !$where['endTime']) {
//            $where['createTime'] = array('EGT', $where['startTime']);
//        } elseif (!$where['startTime'] && $where['endTime']) {
//            $where['createTime'] = array('ELT', $where['endTime']);
//        }
//        unset($where['startTime']);
//        unset($where['endTime']);
        return $where;
    }

    public function getSysParamInfo($condition){
        return $this->field(array('value'))->where($condition)->find();
    }
}