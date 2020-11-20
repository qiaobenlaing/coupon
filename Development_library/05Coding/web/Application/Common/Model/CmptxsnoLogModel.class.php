<?php
namespace Common\Model;
use Think\Model;
/**
 * Created by PhpStorm.
 * User: huafei.ji
 * Date: 15-9-8
 * Time: 下午7:27
 */
class CmptxsnoLogModel extends BaseModel {
    protected $tableName = 'CmptxsnoLog';

    /**
     * 获得当前公司流水号并更新
     */
    public function getCmptxsno() {
        // 生成新的公司方流水号
        $count = $this->where(array('cmptxsno' => array('like', date('YmdHis') . '%')))->count('id');
        $serialNbr = sprintf('%06d', $count + 1);
        $newCmptxsno = date('YmdHis') . $serialNbr;
        // 检查新的公司方流水号是否已经存在
        $info = $this->where(array('cmptxsno' => $newCmptxsno))->find();
        if($info) {
            $this->getCmptxsno();
        } else {
            // 保存公司方流水号
            $this->add(array('cmptxsno' => $newCmptxsno));
            return $newCmptxsno;
        }
    }
}
