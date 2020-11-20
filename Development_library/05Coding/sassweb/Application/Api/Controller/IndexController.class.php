<?php
/**
 * Created by PhpStorm.
 * User: Huafei Ji
 * Date: 15-7-7
 * Time: 下午4:47
 */
namespace Api\Controller;
use Common\Model\BankAccountModel;
use Common\Model\BatchCouponModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\ProductCategoryModel;
use Common\Model\ProductModel;
use Common\Model\ShopModel;
use Common\Model\ShopPhotoModel;
use Common\Model\UserBonusModel;
use Common\Model\UserModel;
use Think\Controller;

class IndexController extends Controller {

    public function index() {
        $this->assign('time', time());
        $this->display();
    }

    /**
     * 把ShopPhoto里的照片转移到Product里
     */
    public function shopPhotoToProduct() {
        $shopPhotoMdl = new ShopPhotoModel();
        $shopPhotoList = $shopPhotoMdl
            ->field(array('ShopPhoto.url', 'ShopPhoto.title', 'ShopPhoto.price', 'ShopPhoto.des', 'ShopPhoto.createTime', 'SubAlbum.name' => 'subAlbumName', 'SubAlbum.shopCode'))
            ->join('SubAlbum ON SubAlbum.code = ShopPhoto.subAlbumCode')
            ->where(array())
            ->select();
        $productCategoryMdl = new ProductCategoryModel();
        $productMdl = new ProductModel();
        foreach($shopPhotoList as $photo) {
            $categoryInfo = $productCategoryMdl->getProductCategoryInfo(array('categoryName' => $photo['subAlbumName'], 'shopCode' => $photo['shopCode']), array('categoryId'));
            if(!$categoryInfo) {
                $categoryInfo = $productCategoryMdl->editProductCategory(array('categoryName' => $photo['subAlbumName'], 'shopCode' => $photo['shopCode']));
            }
            $productInfo = $productMdl->getProductInfo(array('shopCode' => $photo['shopCode'], 'productName' => $photo['title'], 'categoryId' => $categoryInfo['categoryId']));
            if(empty($productInfo['productId'])) {
                $productMdl->editProduct(array(
                    'shopCode' => $photo['shopCode'],
                    'createTime' => $photo['createTime'],
                    'productImg' => $photo['url'],
                    'productName' => $photo['title'],
                    'originalPrice' => $photo['price'],
                    'des' => $photo['des'],
                    'categoryId' => $categoryInfo['categoryId']
                ));
            }
        }
    }
}