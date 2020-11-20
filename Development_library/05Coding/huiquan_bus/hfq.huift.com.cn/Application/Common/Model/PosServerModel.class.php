<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-9
 * Time: 下午4:08
 */
namespace Common\Model;
use Think\Model;
class PosServerModel extends BaseModel {
    protected $tableName = 'PosServer';
    private $posType = array(1, 2, 3, 4); //1-申请POS机，2-耗材配送，3-故障报修，4-其他

    /**
     * 商家申请POS服务
     * @param array $serverInfo array('shopCode','type','remark')
     * @return array
     */
    public function applyPosServer($serverInfo) {
        $rules = array(
            array('shopCode', 'require', C('SHOP.SHOP_CODE_ERROR')),
            array('type', 'require', C('POS.TYPE_ERROR')),
            array('type', $this->posType, C('POS.TYPE_ERROR'), 0, 'IN'),
        );
        if ($this->validate($rules)->create($serverInfo) != false) {
            $shopMdl = new ShopModel();
            // 获得商家信息（商家名称）
            $shopInfo = $shopMdl->getShopInfo($serverInfo['shopCode'], array('shopName'));
            if(!$shopInfo) {
                return $this->getBusinessCode(C('SHOP.NOT_EXIST'));
            }
            $serverInfo['posServerCode'] = $this->create_uuid();
            $serverInfo['createTime'] = date('Y-m-d H:i:s', time());
            $serverInfo['isDeal'] = C('POS_SERVER_IS_DEAL.NO');
            $code = $this->add($serverInfo) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            return $this->getBusinessCode($code);
        } else {
            return $this->getValidErrorCode();
        }
    }
}