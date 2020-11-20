<?php
namespace Common\Model;
use Think\Model;

class SubModuleModel extends BaseModel{
    protected $tableName = 'SubModule';

    /**
     * 根据搜索条件获取子模块列表
     * @param $field
     * @param $where
     * @return mixed
     */
    public function getSubModuleList($field, $where){
        if(empty($field)){
            $field = array('SubModule.*');
        }
        $subModuleList = $this
            ->field($field)
            ->join('District ON District.id = SubModule.cityId', 'INNER')
            ->where($where)
            ->order('SubModule.orderNbr asc, SubModule.id asc')
            ->select();
        return $subModuleList;
    }

    public function getScrollList($field, $where){
        $subModuleList = $this
            ->field($field)
            ->join('District ON District.id = SubModule.cityId', 'INNER')
            ->join('Activity ON Activity.activityCode = SubModule.activityCode', 'INNER')
            ->where($where)
            ->order('SubModule.orderNbr asc, SubModule.id asc')
            ->select();
        return $subModuleList;
    }

    /**
     * 根据搜索条件获取子模块数量
     * @param $where
     * @return int
     */
    public function countSubModule($where){
        return $this
            ->join('District ON District.id = SubModule.cityId', 'INNER')
            ->where($where)
            ->count('SubModule.id');
    }

    /**
     * 根据主键获取子模块详情
     * @param $id
     * @return mixed
     */
    public function getSubModuleInfo($id){
        return $this
            ->where(array('id' => $id))
            ->find();
    }

    /**
     * 添加 OR 修改子模块
     * @param array $data
     * @return array
     */
    public function editSubModule($data){
        $rules = array(
//            array('imgPosition', 'require', C('SUB_MODULE.IMG_POSITION_EMPTY')),
            array('screenRate', 'require', C('SUB_MODULE.SCREEN_RATE_EMPTY')),
            array('imgRate', 'require', C('SUB_MODULE.IMG_RATE_EMPTY'))
        );
        if($data['parentModuleId'] == \IndexModule::HOME_TAB){
            $rules[] = array('focusedUrl', 'require', C('SUB_MODULE.NOT_FOCUSED_URL_EMPTY'));
            $rules[] = array('notFocusedUrl', 'require', C('SUB_MODULE.FOCUSED_URL_EMPTY'));
        }else{
            $rules[] = array('imgUrl', 'require', C('SUB_MODULE.IMG_URL_EMPTY'));
            $rules[] = array('imgSize', 'require', C('SUB_MODULE.IMG_SIZE_EMPTY'));
        }
        if(isset($data['imgPosition']) && $data['imgPosition'] != 4){
            $rules[] = array('title', 'require', C('INDEX_MODULE.TITLE_EMPTY'));
//            $rules[] = array('titleColor', 'require', C('INDEX_MODULE.TITLE_COLOR_EMPTY'));
        }
        if(isset($data['linkType']) && $data['linkType'] == 0){
            $rules[] = array('link', 'require', C('INDEX_MODULE.LINK_EMPTY'));
        }
        if($this->validate($rules)->create($data) != false) {
            if(isset($data['activityCode'])){
                M()->startTrans();
                $activityCodeArr = $data['activityCode'];
                unset($data['activityCode']);
                $this->where($data)->delete();
                $data['orderNbr'] = time();
                foreach($activityCodeArr as $actCode) {
                    $data['activityCode'] = $actCode;
                    $data['orderNbr'] = $data['orderNbr'] + 1;
                    $ret = $this->add($data) !== false ? true : false;
                    if($ret == false) {
                        $this->rollback();
                        return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
                    }
                }
                $this->commit();
                return $this->getBusinessCode(C('SUCCESS'));
            }else{
                if(isset($data['id']) && !empty($data['id'])){
                    $code = $this->where(array('id' => $data['id']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                }else{
                    $data['orderNbr'] = time();
                    $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                }
                return $this->getBusinessCode($code);
            }
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 查找所有商圈
     */
    public function getDistrict() {
        return $this->field(array('title'))->where(array('parentModuleId' => C('TITLE.DISTRICT')))->select();
    }
}