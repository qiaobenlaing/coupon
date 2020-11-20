<?php
namespace Common\Model;
use Think\Model;
/**
 * shopDecoration表
 * @author
 */
class ShopDecorationModel extends BaseModel {
    protected $tableName = 'ShopDecoration';
    private $imgLimitNbr = 6;

    /**
     * 更新预商户背景图片被商户图片
     * @param string $preShopCode 预商户编码
     * @param string $shopCode 商家编码
     * @return array
     */
    public function usePreShopDecoration($preShopCode, $shopCode) {
        $code = $this->where(array('shopCode' => $preShopCode))->save(array('shopCode' => $shopCode)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 删除图片
     * @param array $where 条件
     * @return array
     */
    public function delDecoration($where) {
        $where = $this->filterWhere($where);
        $code = $this->where($where)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 编辑商家背景图片
     * @param array $data
     * @return array
     */
    public function editShopDecoration($data) {
        if(empty($data['decorationCode'])) {
            $data['decorationCode'] = $this->create_uuid();
            $data['createTime'] = date('Y-m-d H:i:s');
            $code = $this->add($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $code = $this->where(array('decorationCode' => $data['decorationCode']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 获得商家环境图片列表
     * @param string $shopCode 商家编码
     * @param object $page 页码。从1开始
     * @return array $shopDecoList
     */
    public function listShopDecoration($shopCode, $page) {
        $shopDecorationList = $this
            ->where(array('shopCode' => $shopCode))
            ->order('createTime desc')
            ->pager($page)
            ->select();
        return $shopDecorationList;
    }

    /**
     * 获得商家环境图片列表
     * @param string $shopCode 商家编码
     * @return int $shopDecorationCount
     */
    public function countShopDecoration($shopCode) {
        $shopDecorationCount = $this
            ->where(array('shopCode' => $shopCode))
            ->count();
        return $shopDecorationCount;
    }

    /**
     * 添加商店装饰信息
     * @param string $shopCode 商家编码
     * @param number $type 装饰类型
     * @param string $shortDes 装饰主要描述
     * @param string $detailDes 装详细描述
     * @param string $imgUrl 装饰图片
     * @param string $audioUrl 装饰的音频信息
     * @return boolean
     */
    public function addShopDecoration($shopCode, $type, $shortDes, $detailDes, $imgUrl, $audioUrl) {
        $shopDecorationInfo = array(
            'decorationCode' => $this->create_uuid(),
            'shopCode' => $shopCode,
            'type' => $type,
            'shortDes' => $shortDes,
            'detailDes' => $detailDes,
            'imgUrl' => $imgUrl,
            'audioUrl' => $audioUrl,
            'createTime' => date('Y-m-d H:i:s', time()),
        );
        $ret = $this->add($shopDecorationInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return array('code'=>$ret, 'decorationCode'=>$shopDecorationInfo['decorationCode']);
    }

    /**
     * 删除数据
     * @param string $decorationCode 装饰编码
     * @return array
     */
    public function delShopDecoration($decorationCode) {
        $decoration = $this->getOneDecoration($decorationCode);
        unlink('.'.$decoration['imgUrl']);  //删除文件
        $code = $this->where(array('decorationCode' => $decorationCode))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 获得商家所有的环境图片
     * @param string $shopCode 商家编码
     * @return array
     */
    public function getShopDecorationList($shopCode) {
        return $this
            ->field(array('decorationCode', 'imgUrl', 'shortDes', 'title'))
            ->where(array('shopCode' => $shopCode))
            ->order('createTime asc')
            ->select();
    }

    /**
     * 获取某家商家的装饰信息
     * @param $shopCode
     * @return array
     */
    public function getShopDecoration($shopCode) {
        $shopDecoration = $this
            ->field(array('decorationCode', 'imgUrl', 'type', 'createTime', 'title'))
            ->where(array('shopCode' => $shopCode))
            ->order('createTime asc')
            ->select();
        return $shopDecoration;
    }

    /**
     * 添加商家装饰图片信息
     * @param string $shopCode 商铺编码
     * @param string $imgUrl 图片url,多个元素以竖线“|”分割
     *  @param string $title 照片标题，多个元素以竖线“|”分割
     * @return array
     */
    public function addShopDecImg($shopCode, $imgUrl, $title) {
        if(! isset($imgUrl) || empty($imgUrl))
            return $this->getBusinessCode(C('SHOP_DEC.IMG_ERROR'));
        $imgData = explode('|', $imgUrl);
        $titleData = explode('|', $title);
        $shopDecCount = $this->where(array('shopCode' => $shopCode))->count('decorationCode');
//        if(count($imgData) > $this->imgLimitNbr || $shopDecCount >= $this->imgLimitNbr) {
//            return $this->getBusinessCode(C('SHOP_DEC.TOO_MANY_IMG'));
//        }
        foreach ($imgData as $k => $v) {
            $data = array(
                'decorationCode' => $this->create_uuid(),
                'shopCode' => $shopCode,
                'imgUrl' => $v,
                'createTime' => date('Y-m-d h:i:s', time()),
                'title' => $titleData[$k],
            );
            $ret = $this->add($data);
            if($ret == false) {
                return $this->getBusinessCode(C('API_INTERNAL_EXCEPTION'));
            }
        }
        return $this->getBusinessCode(C('SUCCESS'));
    }

    /**
     * 将商家装饰图片其中一张设置为主图
     * @param string $shopCode 商家编码
     * @param string $decorationCode 装修编码
     * @return array
     */
    public function setMainShopDecImg($shopCode, $decorationCode){
        $shopDecoration = $this
            ->field(array('decorationCode'))
            ->where(array('shopCode' => $shopCode, 'type' => 1))
            ->find();
        if($shopDecoration){
            $code = $this->where(array('decorationCode' => $shopDecoration['decorationCode']))->save(array('type'=>0)) !== false ?  C('SUCCESS'): C('API_INTERNAL_EXCEPTION');
        }
        $code = $this->where(array('decorationCode' => $decorationCode))->save(array('type'=>1)) !== false ?  C('SUCCESS'): C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 更新数据
     * @param string $decorationCode 主键
     * @param array $decorationInfo 关联数组
     * @return array
     */
    public function updateShopDecoration($decorationCode, $decorationInfo) {
        $rules = array(
            array('imgUrl', 'require', C('SHOP_DEC.IMG_ERROR')),
        );
        if($this->validate($rules)->create($decorationInfo) != false) {
            $code = $this->where(array('decorationCode' => $decorationCode))->save($decorationInfo) !== false ?  C('SUCCESS'): C('API_INTERNAL_EXCEPTION');
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    public function getOneDecoration($decorationCode) {
        return $this
            ->where(array('decorationCode' => $decorationCode))
            ->find();
    }
}