<?php
namespace Common\Model;
/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-10-15
 * Time: 下午1:36
 */
class OrderProductModel extends BaseModel {
    protected $tableName = 'OrderProduct';

    /**
     * 标记该订单产品全部未上
     * @param int $orderProductId 产品订单ID
     * @return boolean 成功返回true，失败返回false
     */
    public function allNoServed($orderProductId) {
        $orderProductInfo = $this->field(array('productNbr'))->where(array('orderProductId' => $orderProductId))->find();
        return $this->where(array('orderProductId' => $orderProductId))->save(array('unavailableNbr' => $orderProductInfo['productNbr'], 'availableNbr' => 0)) !== false ? true : false;
    }

    /**
     * 获得订单产品清单
     * @param array $condition 条件，一维关联数组
     * @param array $field 要查询的字段，一维索引数组
     * @param array $joinTable 二维数组，格式：{{'table' => 'tableName', 'con' => 'tableName.filedName = tableName.filedName', 'type' => 'inner'}, {...}}
     * @return array $where 二维数组
     */
    public function getOrderProductList($condition, $field = array('*'), $joinTable = array()) {
        $this->field($field);
        if($joinTable) {
            foreach($joinTable as $v) {
                $this->join($v['table'] . ' ON ' . $v['con'], $v['type']);
            }
        }
        $orderProductList = $this->where($condition)->select();
        return $orderProductList;
    }

    /**
     * 标记该订单产品全部已上
     * @param int $orderProductId 产品订单ID
     * @return boolean 成功返回true，失败返回false
     */
    public function allServed($orderProductId) {
        $orderProductInfo = $this
            ->field(array('productNbr'))
            ->where(array('orderProductId' => $orderProductId))
            ->find();
        return $this
            ->where(array('orderProductId' => $orderProductId))
            ->save(array('availableNbr' => $orderProductInfo['productNbr'], 'unavailableNbr' => 0)) !== false ? true : false;
    }

    /**
     * 计算餐后之后堂食订单的总金额
     * @param string $orderCode 订单编码
     * @return int $actualOrderAmount 单位：分
     */
    public function calActualOrderAmount($orderCode) {
        $orderProductList = $this
            ->field(array('productUnitPrice', 'availableNbr'))
            ->where(array('orderCode' => $orderCode))
            ->select();
        $actualOrderAmount = 0;
        foreach($orderProductList as $orderProduct) {
            $actualOrderAmount += $orderProduct['productUnitPrice'] * $orderProduct['availableNbr'];
        }
        return $actualOrderAmount;
    }

    /**
     * 获得详情
     * @param array $condition 条件
     * @param array $field 字段
     * @return array
     */
    public function getOrderProductInfo($condition, $field) {
        return $this->field($field)->where($condition)->find();
    }

    /**
     * 修改订单产品信息
     * @param array $data 数据
     * @return array
     */
    public function editOrderProduct($data) {
        $code = $this->where(array('orderProductId' => $data['orderProductId']))->save($data) !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
        return $this->getBusinessCode($code);
    }

    /**
     * 获得订单已点的产品总数
     * @param string $orderCode 订单编码
     * @param string $isNewlyAdd 是否新增的。1-是,0-否，''或null-全部
     * @return int $nbr
     */
    public function sumOrderProductNbr($orderCode, $isNewlyAdd = '') {
        $condition = array('orderCode' => $orderCode);
        if(!empty($isNewlyAdd) || $isNewlyAdd == 0) {
            $condition['isNewlyAdd'] = $isNewlyAdd;
        }
        $nbr = $this->where($condition)->sum('productNbr');
        return $nbr ? $nbr : 0;
    }

    /**
     * 修改订单的产品清单
     * @param string $orderCode 订单编码
     * @param array $productList 新的订单列表。二维数组
     * @return boolean 成功返回true，失败返回false
     */
    public function modifyOrderProduct($orderCode, $productList) {
        $productCodeList = array();
        foreach($productList as $product) {
            $productCodeList[] = $product['productId'];
        }
        $where['orderCode'] = $orderCode;
        if(! empty($productCodeList)) {
            $where['productId'] = array('NOTIN', $productCodeList);
        }
        $delRet = $this->where($where)->delete();
        if($delRet !== false) {
            foreach($productList as $product) {
                $condition = array('orderCode' => $orderCode, 'productId' => $product['productId']);
                $orderProductInfo = $this->where($condition)->find();
                if($orderProductInfo) {
                    $ret = $this->where($condition)->save($product);
                } else {
                    $product['orderCode'] = $orderCode;
                    $ret = $this->add($product);
                }
                if($ret === false ) return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得订单的产品总数
     * @param $orderCode
     * @return int $orderProductCount
     */
    public function countProductByOrder($orderCode) {
        $orderProductCount = $this
            ->where(array('orderCode' => $orderCode))
            ->count('orderProductId');
        return $orderProductCount;
    }

    /**
     * 获得订单的产品清单
     * @param string $orderCode 订单编码
     * @param string $isNewlyAdd 是否查询新增的产品。0-查询所有的，1-只查询新增，''或null查询所有的
     * @return array $orderProductList 二维数组
     */
    public function getProductListByOrder($orderCode, $isNewlyAdd = '') {
        $condition = array('orderCode' => $orderCode);
        if(!empty($isNewlyAdd)) {
            $condition['isNewLyAdd'] = C('YES');
        }
        $orderProductList = $this
            ->field(array('orderProductId', 'Product.productId', 'productName', 'productUnitPrice', 'productNbr', 'sortNbr', 'unit', 'categoryId', 'availableNbr', 'unavailableNbr', 'isNewLyAdd'))
            ->join('Product ON Product.productId = OrderProduct.productId')
            ->where($condition)
            ->select();
        foreach($orderProductList as &$product) {
            $product['productUnitPrice'] = $product['productUnitPrice'] / C('RATIO');
            $product['productPrice'] = $product['productUnitPrice'] * $product['productNbr'];
            $product['actualOrderAmount'] = $product['productUnitPrice'] * $product['availableNbr'];
        }
        return $orderProductList;
    }

    /**
     * 产品是否被下过单
     * @param int $productId 产品ID
     * @return boolean 是返回true，否返回false
     */
    public function isProductHadOrdered($productId) {
        $orderInfo = $this->field(array('orderProductId'))->where(array('productId' => $productId))->find();
        return empty($orderInfo) ? false : true;
    }

    /**
     * 添加订单产品
     * @param array $data {'orderCode' => '1', 'productId' => '2', 'productUnitPrice' => '3', 'productNbr' => '4'}
     * @return array
     */
    public function addOrderProduct($data) {
        $rules = array(
            array('orderCode', 'require', C('ORDER.CODE_EMPTY')),
            array('productId', 'require', C('PRODUCT.PRODUCT_ID_EMPTY')),
            array('productUnitPrice', 'require', C('ORDER_PRODUCT.UNIT_PRICE_EMPTY')),
            array('productNbr', 'require', C('ORDER_PRODUCT.NBR_EMPTY')),
        );
        if($this->validate($rules)->create($data)) {
            $data['availableNbr'] = $data['productNbr'];
            $ret = $this->add($data);
            $code = $ret !== false ? C('SUCCESS') : C('API_INTERNAL_EXCEPTION');
            return array('code' => $code, 'orderProductId' => $ret);
        } else {
            return $this->getValidErrorCode();
        }
    }
}