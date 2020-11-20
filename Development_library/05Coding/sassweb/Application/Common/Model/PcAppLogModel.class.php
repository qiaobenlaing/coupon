<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-10-19
 * Time: 下午4:02
 */ 
class PcAppLogModel extends BaseModel {
    protected $tableName = 'PcAppLog';

    /**
     * @return array
     */
    public function getNewestPcAppVersion(){
        return $this->field(array('versionCode', 'updateUrl', 'isMustUpdate'))->order('createTime desc')->find();
    }

    /**
     * 编辑PC端APP更新记录
     * @param array $data 更新记录信息
     * @return boolean 编辑成功返回true,编辑失败返回false
     */
    public function editUpdateLog($data) {
        $rules = array(
            array('versionName', 'require', C('CLIENT_APP_UPDATE.VERSION_NAME_EMPTY')),
            array('versionCode', 'require', C('CLIENT_APP_UPDATE.VERSION_CODE_EMPTY')),
            array('updateUrl', 'require', C('CLIENT_APP_UPDATE.UPDATE_URL_EMPTY')),
            array('updateContent', 'require', C('CLIENT_APP_UPDATE.UPDATE_CONTENT_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            if($data['logCode']) {
                return $this->where(array('logCode' => $data['logCode']))->save($data) !== false ? true : false;
            }
            return $this->add($data) !== false ? true : false;
        } else{
            return $this->getError();
        }
    }

    /**
     * 获取更新记录信息
     * @param string $logCode 记录编码
     * @return array
     */
    public function getUpdateLogInfo($logCode) {
        return $this
            ->where(array('logCode' => $logCode))
            ->find();
    }

    /**
     * PC端APP更新列表
     * @param array $filterData
     * @param object $page
     * @return null|array 如果为空，返回null；如果不为空，返回二维数组;
     */
    public function listUpdateLog($filterData, $page) {
        $where = $this->filterWhere($filterData, array(
            'versionName' => 'like'
        ), $page);
        return $this
            ->where($where)
            ->order('createTime desc')
            ->pager($page)
            ->select();
    }

    /**
     * 统计更新记录数量
     * @param array $filterData
     * @return null|int 如果为空，返回null；如果不为空，返回number;
     */
    public function countUpdatelog($filterData) {
        $where = $this->filterWhere($filterData, array(
            'versionName' => 'like'
        ));
        return $this
            ->where($where)
            ->count('logCode');
    }

    /**
     * 删除更新记录
     * @param array $condition 条件
     * @return boolean
     */
    public function delLog($condition) {
        return $this->where($condition)->delete() !== false ? true : false;
    }
}