<?php
namespace Common\Model;
use Think\Model;
use Think\Page;
/**
 * Created by PhpStorm.
 * User: huafei.ji
 * Date: 15-8-31
 * Time: 下午3:12
 */
class ShopPhotoModel extends BaseModel {
    protected $tableName = 'ShopPhoto';

    /**
     * 删除子相册的所有图片
     * @param string $subAlbumCode 子相册编码
     * @return boolean 删除成功返回true，删除失败返回false
     */
    public function delSubAlbumPhoto($subAlbumCode) {
        return $this->where(array('subAlbumCode' => $subAlbumCode))->delete() !== false ? true : false;
    }

    /**
     * 获得最新的3张图片
     * @param string $shopCode 商家编码
     * @return array
     */
    public function getNewestPhoto($shopCode) {
        $newLimitTime = 7*24*60*60;// 7天
        $photoList = $this
            ->field(array('url', 'ShopPhoto.createTime'))
            ->join('SubAlbum ON SubAlbum.code = ShopPhoto.subAlbumCode')
            ->where(array('shopCode' => $shopCode))
            ->order('ShopPhoto.createTime desc')
            ->limit(3)
            ->select();
        foreach($photoList as &$photo) {
            if(time() - strtotime($photo['createTime']) <= $newLimitTime) {
                $photo['isNew'] = C('YES');
            } else {
                $photo['isNew'] = C('NO');
            }
        }
        return $photoList;
    }

    /**
     * 商家是否有上新
     * @param string $shopCode 商家编码
     * @return boolean 有返回true，没有返回false
     */
    public function isShopHasNew($shopCode) {
        $newLimitTime = 7*24*60*60;// 7天
        $subAlbumMdl = new SubAlbumModel();
        $subAlbumList = $subAlbumMdl->listSubAlbum($shopCode);
        foreach($subAlbumList as $v) {
            if(time() - strtotime($v['photoCreateTime']) <= $newLimitTime) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得第一张图片
     * @param string $subAlbumCode
     * @return array
     */
    public function getFirstPhoto($subAlbumCode) {
        return $this->field(array('url', 'createTime'))->where(array('subAlbumCode' => $subAlbumCode))->order('createTime desc')->find();
    }

    /**
     * 获得子相册图片
     * @param string $subAlbumCode 子相册编码
     * @return array $photoList
     */
    public function getSubAlbumPhoto($subAlbumCode) {
        $subAlbumMdl = new SubAlbumModel();
        $subAlbumInfo = $subAlbumMdl->getSubAlbumInfo($subAlbumCode);
        $photoList = $this->where(array('subAlbumCode' => $subAlbumCode))->order('createTime desc')->select();
        foreach($photoList as &$photo) {
            $photo['price'] = $photo['price'] / C('RATIO');
            $photo['subAlbumName'] = $subAlbumInfo['name'];
        }
        return $photoList;
    }

    /**
     * 删除商家图片
     * @param string $code 图片编码
     * @return array
     */
    public function delPhoto($code) {
        $code = $this->where(array('code' => $code))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 编辑子相册图片
     * @param array $data
     * @return array $ret
     */
    public function editSubAlbumPhoto($data) {
        if($data['code']) {
            $code = $this->where(array('code' => $data['code']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            return $this->getBusinessCode($code);
        } else {
            $code = $this->add($data);
            if($code === false) {
                return array('code' => C('API_INTERNAL_EXCEPTION'));
            } else {
                // 发商家广播
                $subAlbumMdl = new SubAlbumModel();
                $subAlbumInfo = $subAlbumMdl->getSubAlbumInfo($data['subAlbumCode']);
                $msgMdl = new MessageModel();
                $msgInfo = C('SHOP_BROADCASTING.NEW_PRODUCT');
                $msgMdl->shopBroadcasting($subAlbumInfo['shopCode'], $msgInfo['TITLE'], $msgInfo['CONTENT']);
                return array('code' => C('SUCCESS'), 'photoCode' => $code);
            }
        }
    }

    /**
     * 获得子相册的图片
     * @param string $subAlbumCode 子相册编码
     * @return array $photoList 图片列表
     */
    public function listPhoto($subAlbumCode) {
        $photoList = $this->where(array('subAlbumCode' => $subAlbumCode))->order('createTime desc')->select();
        foreach($photoList as &$v) {
            $v['price'] = $v['price'] / C('RATIO');
        }
        return $photoList;
    }

    /**
     * 获得商家详情页的10张产品图片
     * @param array $condition 条件
     * @return array $shopPhoto
     */
    public function getShopInfoPhoto($condition) {
        $shopPhoto = $this->field(array('url'))->where($condition)->limit(10)->select();
        return $shopPhoto;
    }

}

