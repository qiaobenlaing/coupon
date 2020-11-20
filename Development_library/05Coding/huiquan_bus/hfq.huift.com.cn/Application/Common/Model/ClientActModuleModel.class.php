<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-8-22
 * Time: 下午1:31
 */
class ClientActModuleModel extends BaseModel {
    protected $tableName = 'ClientActModule';

    /**
     * 编辑模块
     * @param array $data
     * @return boolean||string $ret 成功返回true，失败返回false或者错误信息
     */
    public function editModule($data) {
        $rules = array(
            array('moduleName', 'require', C('CLIENT_ACT_MODULE.MODULE_NAME_EMPTY')),
            array('webAddress', 'require', C('CLIENT_ACT_MODULE.WEBADDRESS_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['moduleCode']) && $data['moduleCode']) {
                $ret = $this->where(array('moduleCode' => $data['moduleCode']))->save($data) !== false ? true : false;
            }else{
                $data['moduleCode'] = $this->create_uuid();
                $ret = $this->add($data) !== false ? true : false;
            }
            return $ret;
        } else {
            return $this->getError();
        }
    }

    /**
     * 获得模块详情
     * @param string $moduleCode 模块编码
     * @return array $moduleInfo
     */
    public function getClientActModuleInfo($moduleCode) {
        $moduleInfo = $this->where(array('moduleCode' => $moduleCode))->find();
        return $moduleInfo;
    }

    /**
     * 活动模块列表
     * @param array $filterData
     * @param object $page
     * @return array $moduleList 如果为空，返回空数组；
     */
    public function listClientActModule($filterData, $page) {
        $where = $this->filterWhere(
            $filterData,
            array('moduleName' => 'like'),
            $page);
        $where = $this->secondFilterWhere($where);
        $moduleList = $this
            ->field(array('moduleCode', 'moduleName', 'imgUrl', 'webAddress', 'sortNbr', 'sign'))
            ->where($where)
            ->order('sortNbr asc')
            ->pager($page)
            ->select();
        return $moduleList;
    }

    /**
     * 活动模块列表
     * @param array $filterData
     * @return array $moduleCount 如果为空，返回空数组；
     */
    public function countClientActModule($filterData) {
        $where = $this->filterWhere(
            $filterData,
            array('moduleName' => 'like'),
            $page);
        $where = $this->secondFilterWhere($where);
        $moduleCount = $this
            ->where($where)
            ->count('moduleCode');
        return $moduleCount;
    }

    /**
     * 第二次过滤条件
     * @param array $where 条件
     * @return array $where
     */
    public function secondFilterWhere(&$where) {
        return $where;
    }

}