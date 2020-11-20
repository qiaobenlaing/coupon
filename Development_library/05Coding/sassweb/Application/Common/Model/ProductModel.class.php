<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-10-13
 * Time: 下午4:28
 */
class ProductModel extends BaseModel {
    protected $tableName = 'Product';

    /**
     * 制定商品的最终价格
     * @param int $originalPrice 原价，单位：分
     * @param int $discount 商品折扣，单位：百分数
     * @param int $dropPrice 商品下调价格，单位：分
     * @return int $finalPrice 商品最终价格，单位：分
     */
    public function calProductFinalPrice($originalPrice, $discount, $dropPrice) {
        if(! empty($dropPrice)) {
            $finalPrice = $originalPrice - $dropPrice;
        } elseif($discount != \Consts::HUNDRED) {
            $finalPrice = $originalPrice * $discount / \Consts::HUNDRED;
        } else {
            $finalPrice = $originalPrice;
        }
        return $finalPrice;
    }

    /**
     * 获得掌柜推荐的产品
     * @param string $shopCode 商家编码
     * @return array $productList
     */
    public function getRecommProductList($shopCode) {
        $productList = $this->where(array('shopCode' => $shopCode, 'recommendLevel' => array('gt', 0)))->order('recommendLevel desc')->select();
        foreach($productList as &$product) {
            $product['takeoutPrice'] = $product['takeoutPrice'] / C('RATIO');
            $product['notTakeoutPrice'] = $product['notTakeoutPrice'] / C('RATIO');
            $product['monthlySalesVolume'] = $this->calMonthlySalesVolume($product['wSalesVolume'], $product['createTime']);
        }
        return $productList;
    }

    /**
     * 增加产品外卖销售总量
     * @param int $productId 产品编码
     * @param int $number 数字
     * @return boolean 成功返回true，失败返回false
     */
    public function incWSalesVolumes($productId, $number) {
        return $this->where(array('productId' => $productId))->setInc('wSalesVolume', $number) !== false ? true : false;
    }

    /**
     * 增加产品堂食销售总量
     * @param int $productId 产品编码
     * @param int $number 数字
     * @return boolean 成功返回true，失败返回false
     */
    public function incTSalesVolumes($productId, $number) {
        return $this->where(array('productId' => $productId))->setInc('tSalesVolume', $number) !== false ? true : false;
    }

    /**
     * 增加数量
     * @param string $field
     * @param int $productId 产品编码
     * @param int $number 数字
     * @return boolean 成功返回true，失败返回false
     */
    public function incFiled($field, $productId, $number) {
        return $this->where(array('productId' => $productId))->setInc($field, $number) !== false ? true : false;
    }

    /**
     * 减少数量
     * @param string $field
     * @param int $productId 产品编码
     * @param int $number 数字
     * @return boolean 成功返回true，失败返回false
     */
    public function decFiled($field, $productId, $number) {
        return $this->where(array('productId' => $productId))->setDec($field, $number) !== false ? true : false;
    }

    /**
     * 减少产品外卖销售总量
     * @param int $productId 产品编码
     * @param int $number 数字
     * @return boolean 成功返回true，失败返回false
     */
    public function decWSalesVolumes($productId, $number) {
        return $this->where(array('productId' => $productId))->setDec('wSalesVolume', $number) !== false ? true : false;
    }

    /**
     * 减少产品堂食销售总量
     * @param int $productId 产品编码
     * @param int $number 数字
     * @return boolean 成功返回true，失败返回false
     */
    public function decTSalesVolumes($productId, $number) {
        return $this->where(array('productId' => $productId))->setDec('wSalesVolume', $number) !== false ? true : false;
    }

    /**
     * 添加产品,外卖或者堂食的销售总量
     * @param string $orderCode 订单编码
     * @param string $orderType 订单类型
     * @return boolean 成功返回true,失败返回false
     */
    public function addSalesVolumes($orderCode, $orderType) {
        $orderProductMdl = new OrderProductModel();
        $orderProductList = $orderProductMdl->getProductListByOrder($orderCode);
        $ret = true;
        if($orderType == C('ORDER_TYPE.TAKE_OUT')) {
            foreach($orderProductList as $orderProduct) {
                $ret = $this->incWSalesVolumes($orderProduct['productId'], $orderProduct['availableNbr']);
            }
        } else {
            foreach($orderProductList as $orderProduct) {
                $ret = $this->incTSalesVolumes($orderProduct['productId'], $orderProduct['availableNbr']);
            }
        }
        return $ret;
    }

    /**
     * 减少产品外卖或者堂食的销售总量
     * @param string $orderCode 订单编码
     * @param string $orderType 订单类型
     * @return boolean 成功返回true,失败返回false
     */
    public function decSalesVolumes($orderCode, $orderType) {
        $orderProductMdl = new OrderProductModel();
        $orderProductList = $orderProductMdl->getProductListByOrder($orderCode);
        $ret = true;
        if($orderType == C('ORDER_TYPE.TAKE_OUT')) {
            foreach($orderProductList as $orderProduct) {
                $ret = $this->decWSalesVolumes($orderProduct['productId'], $orderProduct['availableNbr']);
            }
        } else {
            foreach($orderProductList as $orderProduct) {
                $ret = $this->decTSalesVolumes($orderProduct['productId'], $orderProduct['availableNbr']);
            }
        }
        return $ret;
    }

    /**
     * 删除产品
     * @param int $condition 条件
     * @return array
     */
    public function delProduct($condition) {
        $code = $this->where($condition)->delete() !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 计算产品的月销售量
     * @param int $salesVolume 产品销售总量
     * @param string $createTime 产品添加时间
     * @return int $monthlySalesVolume 产品月销售量
     */
    public function calMonthlySalesVolume($salesVolume, $createTime) {
        return floor($salesVolume / ceil((time() - strtotime($createTime)) / 86400 / 30));
    }

    /**
     * 获得商户菜单详情
     * @param array $condition 条件
     * @return array $productInfo
     */
    public function getProductInfo($condition) {
        $productInfo = $this
            ->field(array('*'))
            ->where($condition)
            ->find();
        $productInfo['monthlySalesVolume'] = $this->calMonthlySalesVolume($productInfo['wSalesVolume'], $productInfo['createTime']);
        return $productInfo;
    }

    /**
     * 根据条件获得商品列表
     * @param array $condition 条件
     * @param array $field 查询的字段
     * @param string $order 排序
     * @param int $limit 数量
     * @return array $productList 二维数组
     */
    public function getProductList($condition, $field = array('*'), $order = '', $limit = 0) {
        $condition = $this->filterWhere($condition);
        $productList = $this
            ->field($field)
            ->where($condition)
            ->order($order)
            ->limit($limit)
            ->select();
        $tem = array('takeoutPrice', 'notTakeoutPrice', 'originalPrice', 'dropPrice');
        foreach($productList as &$product) {
            foreach($tem as $v) {
                if($product[$v]) {
                    $product[$v] = $product[$v] / C('HUNDRED');
                }
            }
            $product['monthlySalesVolume'] = $this->calMonthlySalesVolume($product['wSalesVolume'], $product['createTime']);
        }
        return $productList;
    }

    public function getNewProduct($field, $where, $limit){
        if(empty($field)){
            $field = array('*');
        }
        return $this
            ->field($field)
            ->where($where)
            ->order('createTime desc')
            ->limit($limit)
            ->select();
    }

    /**
     * 编辑产品
     * @param array $data
     * @return array
     */
    public function editProduct($data) {
        $rules = array(
//            array('productName', 'require', C('PRODUCT.PRODUCT_NAME_EMPTY')),
            array('categoryId', 'require', C('PRODUCT_CATEGORY.CATEGORY_ID_EMPTY')),
//            array('productImg', 'require', C('PRODUCT.PRODUCT_IMG_EMPTY')),
            array('notTakeoutPrice', 'require', C('PRODUCT.NOT_TAKEOUT_PRICE_EMPTY')),
            array('notTakeoutPrice', 'is_numeric', C('PRODUCT.NOT_TAKEOUT_PRICE_ERROR'), '0', 'function'),
            array('takeoutPrice', 'require', C('PRODUCT.TAKEOUT_PRICE_EMPTY')),
            array('takeoutPrice', 'is_numeric', C('PRODUCT.TAKEOUT_PRICE_ERROR'), '0', 'function'),
            array('recommendLevel', 'require', C('PRODUCT.RECOMMEND_LEVEL_EMPTY')),
            array('spicyLevel', 'require', C('PRODUCT.SPICY_LEVEL_EMPTY')),
            array('unit', 'require', C('PRODUCT.UNIT_EMPTY')),
            array('isTakenOut', 'require', C('PRODUCT.IS_TAKEN_OUT_EMPTY')),
            array('shopCode', 'require', C('SHOP.SHOP_CODE_EMPTY')),
        );
        if($this->validate($rules)->create($data) != false) {
            $data['notTakeoutPrice'] = $data['notTakeoutPrice'] * C('RATIO');
            $data['takeoutPrice'] = $data['takeoutPrice'] * C('RATIO');

            if(! empty($data['productId'])) {
                $code = $this->where(array('productId' => $data['productId']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
                $productId = $data['productId'];
            }else{
                $data['createTime'] = empty($data['createTime']) ? date('Y-m-d H:i:s') : $data['createTime']; // 设置添加时间
                $productId = $this->add($data);
                $code = $productId !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            }
            return array('code' => $code, 'productId' => $productId !== false ? $productId : 0);
        } else {
            return $this->getValidErrorCode();
        }
    }

    /**
     * 修改产品的状态
     * @param array $condition 条件
     * @param int $status 状态。1-上架，2-下架，3-售罄
     * @return array
     */
    public function changeProductStatus($condition, $status) {
        $code = $this->where($condition)->save(array('productStatus' => $status)) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }
    
    /**
     * 查找商店产品
     * @param $where 查询条件
     */
    public function getShopProduct($where) {
        $ret = $this->where($where)->select();
        return $ret;
    }
}