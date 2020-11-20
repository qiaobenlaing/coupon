<?php
/**
 * Created by PhpStorm.
 * User: 98041
 * Date: 2019/6/12
 * Time: 11:44
 */

namespace Common\Model;


class QAdvListModel extends BaseModel
{
    protected $trueTableName  = 'q_advlist';

    //定义自动验证
    protected $_validate = array(
        array('title','require','标题不能为空'),
        array('usedid','require','所属分类不能为空'),
        array('state','require','状态必须选择！'), //默认情况下用正则进行验证
    );
    //定义自动完成
    protected $_auto = array(
        array('readtime','time',1,'function'),
    );

    //查看广告详情
    public function getAdvListInfo($field,$condition){
        return $this->field($field)->where($condition)->find();
    }

}
