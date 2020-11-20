<?php
namespace Common\Model;
use Think\Model;

class UserGiftModel extends BaseModel{
    protected $tableName = 'UserGift';


    public function getUserGiftInfo($id){
        return $this->where(array('id' => $id))->find();
    }

    public function listUserGift($field, $where, $page){
        if(empty($field)){
            $field = array('UserGift.*');
        }
        $userGiftList = $this
            ->field($field)
            ->where($where)
            ->order('grabTime desc')
            ->pager($page)
            ->select();
        return $userGiftList;
    }

    /**
     * 获取抢礼盒记录数
     */
    public function countUserGift($where) {
        return $this->where($where)->count('distinct(userCode)');
    }

    public function maxUserGift($activityCode) {
        return $this->where(array('activityCode' => $activityCode))->max('grabNbr');
    }

    /**
     * 添加和修改用户抢礼盒记录
     * @param array $data
     * @return array $ret
     */
    public function editUserGift($data) {
        $info = $this->listUserGift('', array('userCode' => $data['userCode'], 'activityCode' => $data['activityCode'], 'currentDay' => date('Y-m-d', time())), $this->getPager(0));
        $spMdl = new SystemParamModel();
        $sp = $spMdl->getParamValue('demandGiftNbr');
        if(!empty($info)){
            $code = C('SUCCESS');
            if($data['grabNbr'] > $info[0]['grabNbr']){
                $data['grabTime'] = time();
                if($info[0]['isGivenGift'] == 1){
                    unset($data['prizeRuleId']);
                }
                if($data['grabNbr'] >= $sp['value'] && $info[0]['isGivenGift'] != 1){
                    $data['isGivenGift'] = 1;
                }
                $code = $this->where(array('id' => $info[0]['id']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'userGiftId' => $info[0]['id']);
        }else{
            $data['grabTime'] = time();
            $data['currentDay'] = date('Y-m-d', time());
            if($data['grabNbr'] >= $sp['value']){
                $data['isGivenGift'] = 1;
            }else{
                unset($data['prizeRuleId']);
            }
            $code = $this->add($data);
            if($code === false) {
                return array('code' => C('API_INTERNAL_EXCEPTION'));
            } else {
                return array('code' => C('SUCCESS'), 'userGiftId' => $code);
            }
        }
    }

    /**
     * 增加拆的数量(+1)
     */
    public function incOpenNbr($id){
        return $this->where(array('id' => $id))->setInc('openNbr', 1);
    }

    public function giveGift($id, $data){
        return $this->where(array('id' => $id))->save($data);
    }

    public function getPager($page){
        if(! isset($page) || $page === '')
            $page = 1;
        return new Pager($page, C('PAGESIZE'));
    }
}