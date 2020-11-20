<?php
namespace Common\Model;
use Think\Model;
/**
 * Created by PhpStorm.
 * User: huafei.ji
 * Date: 15-8-31
 * Time: 下午2:56
 */
class SubAlbumModel extends BaseModel{
    protected $tableName = 'SubAlbum';

    /**
     * 删除子相册
     * @param int $code 子相册编码
     * @return array
     */
    public function delSubAlbum($code) {
        $shopPhotoMdl = new ShopPhotoModel();
        $ret = $shopPhotoMdl->delSubAlbumPhoto($code);
        if($ret) {
            $code = $this->where(array('code' => $code))->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        } else {
            $code = C('API_INTERNAL_EXCEPTION');
        }
        return $this->getBusinessCode($code);
    }

    /**
     * 顾客端获得子相册列表
     * @param string $shopCode 商家编码
     * @param object $page 页码
     * @return array $subAlbumList
     */
    public function cListSubAlbum($shopCode, $page) {
        $subAlbumList = $this
            ->where(array('shopCode' => $shopCode))
            ->order('createTime desc')
            ->pager($page)
            ->select();
        $shopPhotoMdl = new ShopPhotoModel();
        foreach($subAlbumList as &$album) {
            $firstPhotoInfo = $shopPhotoMdl->getFirstPhoto($album['code']);
            $album['url'] = $firstPhotoInfo['url'] ? $firstPhotoInfo['url'] : '';
            $album['photoCreateTime'] = $firstPhotoInfo['createTime'] ? $firstPhotoInfo['createTime'] : '';
        }
        return $subAlbumList;
    }

    /**
     * 顾客端统计子相册数量
     * @param string $shopCode 商家编码
     * @return array $subAlbumCount
     */
    public function cCountSubAlbum($shopCode) {
        $subAlbumCount = $this->where(array('shopCode' => $shopCode))->count('code');
        return $subAlbumCount;
    }

    /**
     * 获得某子相册的信息
     * @param string $code 子相册编码
     * @return array $albumList
     */
    public function getSubAlbumInfo($code) {
        $subAlbumInfo = $this->field(array('name', 'shopCode'))->where(array('code' => $code))->find();
        return $subAlbumInfo;
    }

    /**
     * 添加子相册
     * @param array $data 商家信息
     * @return array $ret
     */
    public function editSubAlbum($data) {
        $rules = array(
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
            array('name', 'require', C('SUB_ALBUM.NAME_ERROR')),
        );
        if($this->validate($rules)->create($data)) {
            $album = $this->where(array('shopCode' => $data['shopCode'],'name'=>$data['name']))->getField('code', true);
            if($album){
                return $this->getBusinessCode(C('SUB_ALBUM.NAME_REPEAT'));
            }
            if($data['code']) {
                $code = $this->where(array('code' => $data['code']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            } else {
                $data['createTime'] = date('Y-m-d H:i:s',time());
                $data['belonging'] = 1;
                $code = $this->add($data);
                if($code === false) {
                    return array('code' => C('API_INTERNAL_EXCEPTION'));
                } else {
                    return array('code' => C('SUCCESS'), 'subAlbumCode' => $code);
                }
            }
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 获得子相册列表
     * @param string $shopCode 商家编码
     * @return array $subAlbumList
     */
    public function listSubAlbum($shopCode) {
        $subAlbumList = $this->where(array('shopCode' => $shopCode))->order('createTime desc')->select();
        $shopPhotoMdl = new ShopPhotoModel();
        foreach($subAlbumList as &$album) {
            $firstPhotoInfo = $shopPhotoMdl->getFirstPhoto($album['code']);
            $album['url'] = $firstPhotoInfo['url'] ? $firstPhotoInfo['url'] : '';
            $album['photoCreateTime'] = $firstPhotoInfo['createTime'] ? $firstPhotoInfo['createTime'] : '';
        }
        return $subAlbumList;
    }

    /**
     * 获得子相册编码
     * @param string $shopCode 商家编码
     * @return array $subAlbumCodeList
     */
    public function getSubAlbumCodeList($shopCode) {
        $subAlbumCodeList = $this->where(array('shopCode' => $shopCode))->getField('code', true);
        return $subAlbumCodeList;
    }
}