<?php
namespace Common\Model;
use Think\Model;
/**
 * backgroundTemplate表
 * @author 
 */
class BackgroundTemplateModel extends BaseModel {
    protected $tableName = 'BackgroundTemplate';

    /**
     * 添加图片
     * @param string $url 图片地址
     * @param string $creatorCode 创建者编码
     * @param number $type 类型。1:优惠券	2：会员卡
     * @return string
     */
    public function addBackgroundTemplate($url, $creatorCode, $type) {
        $bgCode = $this->create_uuid();
        $backgroundTemplateInfo = array(
            'bgCode' => $bgCode,
            'type' => $type,
            'url' => $url,
            'creatorCode' => $creatorCode,
            'createTime' => date('Y-m-d H:i:s', time()),
        );
        return $this->add($backgroundTemplateInfo) !== false ? $bgCode : C('API_INTERNAL_EXCEPTION');
    }
    
    /**
    * 删除数据
    * @param number $backgroundTemplateCode 主键
    * @return boolean||string 删除成功返回true；删除失败返回错误信息
    */
    public function delBackgroundTemplate($backgroundTemplateCode) {
        return $this->where(array('backgroundTemplateCode' => $backgroundTemplateCode))->delete() !== false ? true : '数据库删除数据失败';
    }
    
    /**
    * 数据列表
    * @param array $condition 条件
    * @param $page 分页
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function listBackgroundTemplate($condition, $page) {
        return $this->where($condition)
                ->pager($page)
                ->select();
    }
    
    /**
    * 根据主键得到数据详情
    * @param number $backgroundTemplateCode 主键
    * @return array||boolean 查询成功返回关联数组；查询结果为空返回NULL；查询出错返回false
    */
    public function getBackgroundTemplate($backgroundTemplateCode) {
        return $this->where(array('backgroundTemplateCode' => $backgroundTemplateCode))->find();
    }
    
    /**
    * 更新数据
    * @param number $backgroundTemplateCode 主键
    * @param array $backgroundTemplateInfo 关联数组
    * @return boolean||string
    */
    public function updateBackgroundTemplate($backgroundTemplateCode, $backgroundTemplateInfo) {
        $rules = array(
            array('bgCode', 'require', '提醒'),
            array('type', 'require', '提醒'),
            array('url', 'require', '提醒'),
            array('remark', 'require', '提醒'),
            array('title', 'require', '提醒'),
            array('creatorCode', 'require', '提醒'),
            array('createTime', 'require', '提醒'),
        );
        if($this->validate($rules)->create() != false) {
            return $this->where(array('backgroundTemplateCode' => $backgroundTemplateCode))->save($backgroundTemplateInfo) !== false ? true : '数据库更新数据失败';
        } else {
            return $this->getValidErrorCode();
        }
    }
}