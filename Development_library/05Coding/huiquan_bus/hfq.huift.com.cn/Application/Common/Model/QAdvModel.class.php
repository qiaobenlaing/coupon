<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2019/6/12
 * Time: 11:43
 */

namespace Common\Model;


class QAdvModel extends BaseModel
{

    protected $trueTableName  = 'q_adv';

    //定义自动验证
    protected $_validate = array(
        array('title','require','标题不能为空'),
        array('state','require','状态必须选择！'), //默认情况下用正则进行验证
    );
    //定义自动完成
    protected $_auto = array(
        array('readtime','time',1,'function'),
    );

    //分页分类
    public function listSort($filterData,$page){
        $where = $this->filterWhere($filterData, array(
            'title' => 'like'
        ), $page);
        return   $this->alias('adv')
            ->join('bank on bank.id=adv.bank_id')
            ->where($where)
            ->field('adv.*,bank.name')
            ->order('adv.sort,adv.readtime')->page($page)->select();
    }
    //分类数量
    public function countSort($filterData){
        $where = $this->filterWhere($filterData, array(
            'title' => 'like'
        ), $page);
        return   $this->alias('adv')
            ->join('bank on bank.id=adv.bank_id')
            ->where($where)->count();
    }


    //分类列表
    public function adSort($field,$condition){
        return $this->field($field)->where($condition)->select();
    }

    //获取广告详细信息
    public function getAdvInfo($field,$condition){
        return  $this->alias('adv')->join("right join q_advlist on q_advlist.usedid=adv.id")
            ->join('bank on bank.id=adv.bank_id')
            ->where($condition)
            ->field($field)->select();
    }


    //广告列表
    public function advList($filterData, $page){
        $where = $this->filterWhere($filterData, array(
            'title' => 'like'
        ), $page);
        $this->secondFilterWhere($where);
        return   $this->alias('adv')->join("right join q_advlist on q_advlist.usedid=adv.id")
            ->join('bank on bank.id=adv.bank_id')
            ->where($where)
            ->field('adv.title as advtitle,adv.*,q_advlist.*,bank.name')
            ->order('adv.sort,q_advlist.sort,adv.readtime,q_advlist.readtime')->page($page)->select();
    }

    //广告数量
    public function advCount($filterData){
        $where = $this->filterWhere($filterData, array(
            'title' => 'like'
        ), $page);
        $this->secondFilterWhere($where);
        return   $this->alias('adv')->join("right join q_advlist on q_advlist.usedid=adv.id")
            ->join('bank on bank.id=adv.bank_id')
            ->where($where)
            ->field('adv.id')
            ->order('adv.sort,q_advlist.sort,adv.readtime,q_advlist.readtime')->count();
    }


    /**
     * 第二次过滤$where中的条件
     * @param array $where
     * @return array
     */
    public function secondFilterWhere(&$where) {
        if($where['title']){
            $where['q_advlist.title'] = $where['title'];
            unset($where['title']);
        }
        if ($where['state'] || $where['state'] == '0') {
            $where['q_advlist.state'] = $where['state'];
            unset($where['state']);
        }
        return $where;
    }

}
