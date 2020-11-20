<?php
namespace Home\Controller;
use Common\Model\BankAccountModel;
use Common\Model\BatchCouponModel;
use Common\Model\ConsumeOrderModel;
use Common\Model\OrderCouponModel;
use Common\Model\UserConsumeModel;
use Common\Model\UserModel;
use Think\Controller;

/**
 * Created by PhpStorm.
 * User: Jihuafei
 * Date: 15-11-9
 * Time: 上午10:01
 */
class JhfController extends Controller {
    public function index() {
        $bankAccountMdl = new BankAccountModel();
        // 根据订单号，获得未绑定状态下的账户信息
        $bankAccountInfo = $bankAccountMdl->getBankAccountInfo(array('orderNbr' => '15081500011511169215', 'status' => \Consts::BANKACCOUNT_STATUS_NO_SIGN), array());
        dump($bankAccountInfo);
    }

    public function checkPhp() {
        echo phpinfo();
    }

    /**
     * 测试公共端API
     */
    public function commTest() {
        //导入类库
        Vendor("jsonRPC.jsonRPCClient");
        // 公共API
        if($_SERVER['HTTP_HOST'] == 'baomiserver.com') {
            $url = "http://baomiserver.com/Api/Comm";
        } else {
            $url = "http://baomi.suanzi.cn/Api/Comm";
        }
        $client = new \jsonRPCClient($url);
        $method = 'getValidateCode';    //方法名
        $params = $this->getCommParams($method);
        $ret = $client->$method($params);
        var_dump($ret);
    }

    /**
     * 获得公共端测试参数
     * @param number $method 键
     * @return array 一维数组
     */
    public function getCommParams($method){
        // 登录成功后返回的token值
        $token = '5e7206c236a64ce8b4a6edebdd5fbcd2';
        // 登录成功后返回的用户编码值
        $userCode = 'c63ee722-f950-6b5b-1ff3-37c693fcd81b';
        $sKey = MD5(substr($userCode, 0, 6));
        $commParams = array(
            'getServerTime' =>array(),
            'editIosToken' => array(
                'mobileNbr' => '',
                'token' => '012345678901234567890123456789012345678901234567890123456789012345678901',
                'appType' => '0'
            ),
            'addRegId' => array(
                'device' => 'a4.4_720*1280*2.0_100_XiaoMi_WIFI_XXXXXXXXXXXX',
                'regId' => '010bcd85d2b',
                'appType' => 2,
            ),
            'getValidateCode' => array(
                'mobileNbr' => '15811111111',
                'action' => 'r',
                'appType' => 'c'
            ),
            'login' => array(
                'mobileNbr' => '15868179748',
                'password' => md5('123456'),
                'loginType' => '0',
                'registrationId' => ''
            ),
            'logoff' => array(
                'tokenCode' => '1f7001741f570730728732415b049175',
                'appType' => '0',
                'registrationId' => '',
            ),
            'updatePwd' => array(
                'mobileNbr' => 15868179748,
                'originalPwd' => md5('123456'),
                'newPwd' => md5('1234568'),
                'type' => 1
            ),
            'findPwd' => array(
                'mobileNbr' => '18627885265',
                'validateCode' => '975027',
                'password' => md5('123456'),
                'type' => 0
            ),
            'addFeedback' => array(
                'creatorCode' => '',
                'content' => '1234465645645',
                'targetCode' => ''
            )
        );
        $commParams[$method]['reqtime'] = time();
        $request_key1 = array_keys($commParams[$method]);
        $param1 = $commParams[$method][$request_key1[0]];
        $commParams[$method]['vcode'] = $token.MD5($method.$param1.$sKey);
        return $commParams[$method];
    }

    /**
     * 测试商家端API
     */
    public function shopTest() {
        //导入类库
        Vendor("jsonRPC.jsonRPCClient");

        // 商家端API
        if($_SERVER['HTTP_HOST'] == 'baomiserver.com') {
            $url = "http://baomiserver.com/Api/Shop";
        } else {
            $url = "http://baomi.suanzi.cn/Api/Shop";
        }
        $client = new \jsonRPCClient($url);
        $method = 'addProductCategory';    //方法名
        $params = $this->getShopParams($method);
//        $params['tokenCode'] = '00000000000000000000000000000000';
        $ret = $client->$method($params);
        var_dump($ret['result']);
    }

    /**
     * 获得商家端测试参数
     * @param number $method 方法名
     * @return array 一维数组
     */
    public function getShopParams($method) {
        $shopCode = '3a4e4a05-198e-0d03-ce7e-a7036ce70082';
        $creatorCode = '2ebc2cc6-8d6c-135b-0769-02e66e40ac77';
        // 登录成功后返回的token值
        $token = '00000000000000000000000000000000';
        // 登录成功后返回的用户编码值
        $userCode = '00000000-0000-0000-0000-000000000000';
        $sKey = MD5(substr($userCode, 0, 6));
        $shopParams = array(
            'getShopTeacherList' => array('shopCode' => 'b3ba491e-d775-3dbe-1179-a0add7122cb0', 'page' => 1),
            'getHqBook' => array(
                'shopCode' => '',
            ),
            'getBillList' => array(
                'shopCode' => 'gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC',
                'page' => 1,
                'timeLimit' => 3,
                'billType' => 6,
            ),
            'getBillStatistics' => array(
                'shopCode' => 'gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC',
                'timeLimit' => 3,
                'billType' => 6
            ),
            'sweepQrCode' => array(
                'validateString' => 'd5accbaf-047f-efca-c17a-ef04e267a4e997aaaa82v56cU6Lca2256cu6caY27'
            ),
            'sGetActList' => array(
                'shopCode' => 'a51cde1c-0955-a042-4a4f-48f7c4107f22',
                'page' => 1
            ),
            'valUserActCode' => array(
                'actCode' => '0001645813',
                'userActCodeId' => 10,
            ),
            'getInfoByActCode' => array(
                'actCode' => '0001062400',
            ),
            'getActRefundChoice' => array(),
            'getOptimalPay' => array(
                "userCode" => "d5accbaf-047f-efca-c17a-ef04e267a4e9","shopCode" => "68392e3b-e5db-8e28-ff25-bd0e656b4ba5","orderAmount" => "1","noDiscountPrice" => ""
            ),
            //"userCode":"d5accbaf-047f-efca-c17a-ef04e267a4e9","shopCode":"68392e3b-e5db-8e28-ff25-bd0e656b4ba5","orderAmount":"1","noDiscountPrice":""
            'getCouponInfoByCode' => array(
                'couponCode' => '00101835707',
            ),
            'useCoupon' => array(
                'userCode' => 'd21610a3-7d4b-98cd-ef9e-bc4eec7b3bd1',
                'shopCode' => 'a51cde1c-0955-a042-4a4f-48f7c4107f22',
                'userCouponCode' => '61868d93-0855-8083-c0b1-a163d6db7057',
            ),
            'valPwd' => array(
                'staffCode' => "db98f87a-b1b5-0e88-48db-6dbbe4f2bb7c",
                'pwd' => "e10adc3949ba59abbe56e057f20f883e",
            ),
            'editShopDeliveryBatchAd' => array(
                'deliveryId' => '||',
                'shopCode' => 'cec32281-38ff-43bf-4db2-a5072390f716|cec32281-38ff-43bf-4db2-a5072390f716|',
                'deliveryDistance' => '5000|5000|',
                'requireMoney' => '50|50|',
                'deliveryFee' => '0|2|',
            ),
            'delShopDelivery' => array(
                'deliveryId' => 15
            ),
            'listShopDelivery' => array(
                'shopCode' => '8eee60a4-2c93-3860-8d11-70b76be69d99'
            ),
            'editShopDelivery' => array(
                'deliveryId' => '',
                'shopCode' => 'cec32281-38ff-43bf-4db2-a5072390f716',
                'deliveryDistance' => 5000,
                'requireMoney' => 50,
                'deliveryFee' => 0,
            ),
            'editShopDeliveryBatch' => array(
                'deliveryList' => array(
                    array('id' => ''),
                    array('id' => ''),
                ),
            ),
            'getShopOrderList' => array(
                'shopCode' => 'cec32281-38ff-43bf-4db2-a5072390f716',
                'isFinish' => '1',
                'page' => '1'
            ),
            'orderConfirm' => array(
                'consumeCode' => '0d901c43-b680-2960-1d4b-68ba45bdff84',
                'mobileNbr' => '15957206699',
            ),
            'dealRefund' => array(
                'orderCode' => '42a1bb57-454a-894e-b8bc-91105b553eb7',
                'isAgree' => '1'
            ),
            'applyEntry' => array(
                'shopCode' => '',
                'shopName' => '',
                'tel' => '',
                'startTime' => '',
                'endTime' => '',
                'street' => '',
                'mobileNbr' => '',
            ),
            'getOrderB' => array(
                'shopCode' => 'gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC',
                'keyWard' => '',
                'unit' => 'D',
                'value' => '2-24',
                'status' => 0,
                'orderType' => '21'
            ),
            'editOwner' => array(
                'staffCode' => '33c896c2-2d43-ad15-b5eb-d550737d7ba4',
                'shopCode' => 'b8a888c7-0543-feb7-ce48-103e941c63b5|e8364419-c627-2a05-de42-b86b39154a68|5c639493-29d8-215a-1733-9c62c420392c'
            ),
            'getStoreBelong' => array(
                'staffCode' => '0945c696-04f5-aa8e-7677-b6461666aa9b',
                'page' => 1
            ),
            'editStaff' => array(
                'staffCode' => '13deca6a-5b79-939c-afa8-bd7523a41428',
                'mobileNbr' => '13972751580',
                'realName' => '枣胡',
                'userLvl' => '2',
                'shopCode' => '',
                'brandCode' => ''
            ),
            'editStaffB' => array(
                'staffCode' => '',
                'mobileNbr' => '18698745693',
                'realName' => '胡',
                'userLvl' => '1',
                'shopCode' => '',
                'parentCode' => '082a8b1b-f72e-515a-3494-37c04295a3f3'
            ),
            'getClerkAdmin' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'page' => 1
            ),
            'getManAdmin' => array(
                'staffCode' => '0945c696-04f5-aa8e-7677-b6461666aa9b',
                'page' => 1
            ),
            'getStaffShopList' => array(
                'staffCode' => '8311808e-3aca-51ea-0eca-7e1d927e9b95',
                'page' => 1
            ),
            'getAccount' => array(
                'shopCode' => 'gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC',
                'page' => 1
            ),
            'countOrderByType' => array(
                'shopCode' => '8eee60a4-2c93-3860-8d11-70b76be69d99'
            ),
            'submitEnd' => array(
                'orderCode' => 'dbd92a61-60aa-f0c5-4e8b-7ce60bb77db9',
                'orderProductList' => array(98,80,78),
                'actualOrderAmount' => "0.55"
            ),
            'getOrder' => array(
                'shopCode' => '7c121767-98f6-5025-45af-35b19f4e3a7b',
                'keyWord' => '',
                'unit' => 'D',
                'value' => '11-23',
                'status' => 0,
                'orderType' => '21'
            ),
            'addNewOrderProduct' => array(
                'orderCode' => '43fee112-0f0d-47ed-2d1f-fa7bcc287a81',
                'productList' => array(
                    array(
                        'productId' => 92,
                        'productNbr' => 3,
                    )
                )
            ),
            'updateOrderProductInfo' => array(
                'orderProductId' => 880,
                'availableNbr' => 1,
            ),
            'getNewestPcAppVersion' => array(
            ),
            'updateProductOrderStatus' => array(
                'orderCode' => '0175b748-0f44-fcd8-0dfa-0f17d95f8c97',
                'orderStatus' => '23',
                'status' => null,
                'tableNbr' => '2',
            ),
            'getProductOrderInfo' => array(
                'orderCode' => '93cdbb36-7d2f-22c5-3f16-70aa1eb87ea0',
                'isNewlyAdd' => ''
            ),
            'listProductOrder' => array(
                'shopCode' => '7c121767-98f6-5025-45af-35b19f4e3a7b',
                'orderStatus' => '',
                'orderType' => ''
            ),
            'delProduct' => array(
                'productId' => '2'
            ),
            'listProductByCategory' => array(
                'categoryId' => '',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
            ),
            'editProductStatus' => array(
                'productId' => '10',
                'productStatus' => '1'
            ),
            'editProduct' => array(
                'productName' => '扫把套餐',
                'categoryId' => '30',
                'productImg' => '/Public/Img/avatar.jpg',
                'notTakeoutPrice' => '30',
                'takeoutPrice' => '30',
                'recommendLevel' => '0',
                'spicyLevel' => '0',
                'unit' => '个',
                'isTakenOut' => '1',
                'des' => '中国好拖把',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'sortNbr' => '0',
                'productId' => '102',
                'productType' => '2',
                'originalPrice' => '15000',
                'dropPrice' => '0',
                'discount' => '10',
                'sexLimit' => '1',
                'ageLimit' => '1',
                'isBooking' => '0',
                'pId' => '4|5',
                'pNbr' => '1|2',
            ),
            'addProduct' => array(
                'productName' => '套餐801',
                'categoryId' => '26',
                'productImg' => '/Public/img/avatar.jpg',
                'notTakeoutPrice' => '0',
                'takeoutPrice' => '0',
                'recommendLevel' => '0',
                'spicyLevel' => '0',
                'unit' => '个',
                'isTakenOut' => '1',
                'des' => '1011010101010101010101010101',
                'shopCode' => 'gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC',
                'sortNbr' => '0',
                'productType' => '2',
                'originalPrice' => '15000',
                'dropPrice' => '0',
                'discount' => '10',
                'sexLimit' => '1',
                'ageLimit' => '1',
                'isBooking' => '0',
                'pId' => '129|130|131',
                'pNbr' => '1|2|1',
            ),
            'delProductCategory' => array(
                'categoryId' => 2,
            ),
            'listProductCategory' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'page' => 1
            ),
            'editProductCategory' => array(
                'categoryName' => '精品套餐',
                'categoryId' => '1',
            ),
            'addProductCategory' => array(
                'categoryName' => '1',
                'shopCode' => '27c78b6f-f7f2-3f72-b11e-a3ca6b1dce86'
            ),
            'getConsumeInfo' => array(
                'consumeCode' => '45a54e9d-93fd-f489-d9d8-5feadc13dd88'
            ),
            'getSystemParam' => array(),
            'getShopInviteCode' => array(
                'shopCode' => '9370e17b-f245-a127-71ea-eb065d612c66',
                'month' => 9
            ),
            'pcPay' => array(
                'userCouponNbr' => '1150172177',
                'shopCode' => 'c264432e-6751-9fe1-25a1-fd3968d117f3',
            ),
            'getSubAlbumPhoto' => array('code' => 24),
            'addSubAlbumPhoto' => array(
                'subAlbumCode' => '110',
                'url' => '/Public/img/avatar.jpg',
                'title' => '浪里白斩鸡',
                'price' => '808080805',
                'des' => '浪里白斩鸡，吃了可以更浪呦',
            ),
            'addSubAlbum' => array(
                'shopCode' => 'bfe60db6-39c6-2cf7-4d6c-2ee8a407d8a9',
                'name' => '牛',
            ),
            'updateSubAlbum' => array(
                'code' => '1',
                'name' => '子丑寅某辰巳午未申酉戌亥',
            ),
            'listSubAlbum' => array('shopCode' => '036aab74-680e-1e1f-be4c-47ecec4096fc'),
            'getSubAlbumInfo' => array('code' => 27),
            'getShopProductAlbum' => array(
                'shopCode' => '036aab74-680e-1e1f-be4c-47ecec4096fc'
            ),
            'listGrabCoupon' => array(
                'batchCouponCode' => 'cbe43cf5-9895-49ca-8a67-ae28f8675410',
                'page' => 1,
            ),
            'changeCouponStatus' => array(
                'batchCouponCode' => '2431fd8b-a010-829a-baf1-27913ec878a7',
                'isAvailable' => 1,
            ),
            'getCouponInfo' => array(
                'batchCouponCode' => '01639a95-c2a7-7e8a-e139-bc60c2de26f9',
            ),
            'listShopCoupon' => array(
                'shopCode' => 'a51cde1c-0955-a042-4a4f-48f7c4107f221',
                'time' => 1,
                'page' => 0,
            ),
            'setPayResult' => array(
                'consumeCode' => '5f96b3e1-ca16-971b-283e-5129a47bae08',
                'serialNbr' => '2',
                'result' => 'SUCCES'),
            'getCouponBill' => array(
                'shopCode' => '84300bec-899a-c331-02bb-b77d484f8942',
                'startDate' => '',
                'endDate' => ''
            ),
//            'register' => array(
//                'mobileNbr' => '12345678902',
//                'validateCode' => S('r12345678902validateCode'),
//                'password' => md5('123456'),
//                'licenseNbr' => '12345678901'
//            ),
            'activate' => array(
                'mobileNbr' => '18627885265',
                'validateCode' => '',
                'password' => 123456,
            ),
            'getAroundShopInfo' => array(
                'shopCode' => $shopCode,
            ),
            'listConsumingMsg' => array(
                'shopCode' => $shopCode,
                'page' => 1,
            ),
            'getGeneralCardStastics' => array(
                'shopCode' => '6de2a08b-3d3b-01fd-1aff-e10a18b72f54',
                'page' => 1
            ),
            'countCard' => array(
                'shopCode' => '6de2a08b-3d3b-01fd-1aff-e10a18b72f54',
            ),
            'listCardVip' => array(
                'cardCode' => '952715b4-3929-db91-c1b9-1db832b0392a',
                'userName' => '',
                'orderType' => 2,
                'page' => 1
            ),
            'getVipInfo' => array(
                'userCardCode' => '24c07aac-e4a3-7ddb-01aa-77c46848d748',
            ),
            'editCard' => array(
                'cardName' => '银卡|金卡|白金卡',
                'cardType' => '1000',
                'cardLvl' => '1|2|3',
                'creatorCode' => 'e10adc3949ba59abbe56e057f20f883e',
                'shopCode' => '10558e01-2973-e821-c2ce-622d9ab78c8f',
                'url' => '/Public/act.jpg',
                'discountRequire' => '100|200|300',
                'discount' => '80|70|60',
                'isRealNameRequired' => 0,
                'isSharable' => 1,
                'pointLifetime' => '600|900|0',
                'pointsPerCash' => '1|2|3',
                'outPointsPerCash' => '500|200|100',
                'remark' => '测试数据而已'
            ),
            'listIncreasingTrend' => array(
                'shopCode' => '15dc1dec-801b-58db-9f80-9a7c6393d2a2'
            ),
            'listConsumeTrend' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
            ),
            'getCouponHomePage' => array(
                'shopCode' => '5c639493-29d8-215a-1733-9c62c420392c'
            ),
            'getAllCoupon' => array(
                'shopCode' => 'd16ae444-569b-ea71-5b8c-2044980f864b'
            ),
            'getCouponInfoByType' => array(
                'shopCode' => '15dc1dec-801b-58db-9f80-9a7c6393d2a2',
            ),
            'getShopCouponList' => array(
                'shopCode' => '84300bec-899a-c331-02bb-b77d484f8942',
                'couponType' => '3',
                'time' => 2,
                'page' => 1
            ),
            'getCouponConsumeList' => array(
                'batchCouponCode' => '4980e79a-a640-9189-f6f4-5d76ed2233fe',
                'page' => 1
            ),
            'listCouponConsumeTrend' => array(
                'shopCode' => '061823f3-a182-91c7-63f0-46889ea35bc1',
            ),
            'listCouponPersonTrend' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
            ),
            'addBatchCoupon' => array(
                "shopCode"=>"60ca4900-204f-bec7-9f67-b3d9883da2c5",
                "couponType"=>"8",
                "totalVolume"=>"10",
                "startUsingTime"=>"2015-12-23",
                "expireTime"=>"2015-12-30",
                "dayStartUsingTime"=>"09:00",
                "dayEndUsingTime"=>"18:00",
                "startTakingTime"=>"2015-12-23",
                "endTakingTime"=>"2015-12-30",
                "isSend"=>"0",
                "sendRequired"=>"0",
                "remark"=>"      adfafadsf",
                "creatorCode"=>"ac561a18-601a-7f8a-5126-27719f563aed",
                "discountPercent"=>"0",
                "insteadPrice"=>"0",
                "availablePrice"=>"0",
                "function"=>"",
                "limitedNbr"=>"1",
                "nbrPerPerson"=>"2",
                "limitedSendNbr"=>"0",
                "payPrice"=>"0"
            ),
            'listBankCardCountBill' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'page' => 1,
                'time' => 1
            ),
            'getBankCardCountBill' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'actionTime' => '2015-07-14'
            ),
            'listBankCardBill' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'page' => 1,
                'datetime' => '2015-07-14'
            ),
            'listStaff' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'page' => 1
            ),
            'isStaffHasPerms' => array(
                'staffCode' => '09a05fb1-9be0-c64c-ce6b-488fc2efcb2e',
            ),
            'addStaff' => array(
                'mobileNbr' => '12345678902',
                'realName' => '石值金',
                'userLvl' => '1',
                'shopCode' => $shopCode
            ),
            'updateStaff' => array(
                'mobileNbr' => '15868179748',
                'realName' => '季华飞',
                'type' => '2',
                'staffCode' => 'c9274bba-3e52-3e92-6c55-2574feaf2aa3'
            ),
            'delStaff' => array(
                'staffCode' => '571d89c4-f6d8-01e1-2858-c2e7c0af2c96'
            ),
            'sGetShopBasicInfo' => array( 'shopCode' => '1908f731-91b7-6d5e-585d-57c4fa6370e8'),
            'sGetShopInfo' => array(
                'shopCode' => 'a51cde1c-0955-a042-4a4f-48f7c4107f22'
            ),
            'getShopDecoration' => array(
                'shopCode' => '6de2a08b-3d3b-01fd-1aff-e10a18b72f54',
            ),
            'addShopDecImg' => array(
                'shopCode' => $shopCode,
                'imgUrl' => '/Public/Uploads/default98.jpg',
            ),
            'updateShopDecoration' => array(
                'decorationCode' => '71d756fa-0e70-11e5-ac45-00163e021731',
                'imgUrl' => '/Public/Uploads/default9.jpg',
            ),
            'delShopDec' => array(
                'decorationCode' => '440c3a3c-b07f-446a-7acb-c050d96bba04',
            ),
            'updateShop' => array(
                'shopCode' => '330aa582-709f-7957-d180-936df635bf19',
                'updateKey' => "isOpenTakeout",
                'updateValue' => "0",
            ),
            'updateShopShortDes' => array(
                'shopCode' => $shopCode,
                'shortDes' => '这是一家神奇的商店'
            ),
            'sGetActivityList' => array(
                'activityBelonging' => '3',
                'shopCode' => $shopCode,
                'page' => 1
            ),
            'sGetActivityInfo' => array(
                'activityCode' => '2e82e098-8585-23e7-c8db-a420953673d0',
            ),
            'listActParticipant' => array(
                'actCode' => '2e82e098-8585-23e7-c8db-a420953673d0',
                'page' => 1
            ),
            'addActivity' => array(
                'activityName' => '挑战吧，余文思',
                'startTime' => '2015-10-01 00:00:00',
                'endTime' => '2015-10-10 11:59:59',
                'activityLocation' => '西城广场',
                'txtContent' => '连续10天，当日首单，立减90元。900元疯狂派送，就是这么任性！！',
                'limitedParticipators' => '20000',
                'isPrepayRequired' => 0,
                'prePayment' => 20,
                'isRegisterRequired' => 1,
                'activityImg' => '/Public/Uploads/20150703/5596220279959.jpg',
                'activityLogo' => '000',
                'shopCode' => $shopCode,
                'creatorCode' => $creatorCode,
                'activityBelonging' => 3,
            ),
            'listBonus' => array('shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082', 'page' => 1),
            'getBonusInfo' => array('bonusCode' => '094df101-2079-02be-d037-0c1824875a08'),
            'addBonus' => array(
                'bonusBelonging' => 2,
                'shopCode' => '00000000-0000-0000-0000-000000000000',
                'creatorCode' => 'c9274bba-3e52-3e92-6c55-2574feaf2aa3',
                'upperPrice' => 20,
                'lowerPrice' => 1,
                'totalValue' => 5000,
                'nbrPerDay' => 0,
                'totalVolume' => 2000,
                'validityPeriod' => 30,
                'startTime' => '2015-02-02',
                'endTime' => '2018-02-02'
            ),
            'changeBonusStatus' => array(
                'bonusCode' => '94d678e1-5437-6d40-530a-d605943dfbc1',
                'status' => 0,
            ),
            'listGrabBonus' => array(
                'bonusCode' => '094df101-2079-02be-d037-0c1824875a08',
                'page' => 1,
            ),
            'listMsg' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'page' => 1
            ),
            'sendMsg' => array(
                'shopCode' => $shopCode,
                'userCode'=> '09755b11-27b4-cea5-484c-997928181764',
                'staffCode'=> '0e06395c-231f-00f1-3b12-d8921f69f4d6',
                'message'=> '来我们家吃饭吧！最近推出超多新品，还有更多免费小食。'
            ),
            'readMsg' => array(
                'userCode'=> '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => $shopCode
            ),
            'countUnreadMsg' => array(
                'userCode'=> '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => $shopCode
            ),
            'getMsg' => array(
                'userCode'=> '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => $shopCode,
                'all'=> 0,
                'staffCode'=>''
            ),
            'getMsgGroup' => array(
                'shopCode' => $shopCode,
                'page' => 1
            ),
            'applyPosServer' => array(
                'shopCode' => $shopCode,
                'type' => 1,
                'remark' => ''
            ),
            'getOrderInfoByPC' => array(
                'lastFourOfOrderNbr' => '0293',
                'shopCode' => 'a51cde1c-0955-a042-4a4f-48f7c4107f22'
            ),
            'getShopStaffSetting' => array(
                'mobileNbr' => '15868179748'
            ),
            'updateShopStaffSetting' => array(
                'staffCode' => '7a15f9b6-838e-8fbd-d0c6-484e0d4600b1',
                'setting' => 0,
            ),
        );

        $shopParams[$method]['reqtime'] = time();
        $request_key1 = array_keys($shopParams[$method]);
        $param1 = $shopParams[$method][$request_key1[0]];
        $shopParams[$method]['vcode'] = $token.MD5($method.$param1.$sKey);

        return $shopParams[$method];
    }

    /**
     * 测试客户端API
     */
    public function clientTest() {
        //导入类库
        Vendor("jsonRPC.jsonRPCClient");
        //顾客端API
        if($_SERVER['HTTP_HOST'] == 'baomiserver.com'){
            $url = "http://baomiserver.com/Api/Client";
        }else{
            $url = "http://baomi.suanzi.cn/Api/Client";
        }
        $client = new \jsonRPCClient($url);
        $method = 'submitActOrder';    //方法名
        $params = $this->getClientParams($method);
        $ret = $client->$method($params);
        dump($ret);
    }

    /**
     * 获得顾客端测试参数
     * @param number $method 键
     * @return array 一维数组
     */
    public function getClientParams($method) {
        $testValidateMobileNbr = '18627885267';
        $shopCode = '3a4e4a05-198e-0d03-ce7e-a7036ce70082';
        // 登录成功后返回的token值
        $token = '00000000000000000000000000000000';
        // 登录成功后返回的用户编码值
        $userCode = '00000000-0000-0000-0000-000000000000';
        $sKey = MD5(substr($userCode, 0, 6));
        $clientParams = array(
            'addClassRemark' => array('classCode' => 'ec6f7d0e-af50-dc2b-a840-2a0b9bd32246', 'effectLvl' => 4, 'envLvl' => 4, 'remark' => '我注册了一个惠圈账号，为了不让爸妈送我来补习，我必须要做点什么', 'remarkImg' => '', 'teacherLvl' => 4, 'userCode' => '91d6c1fc-f5e2-fe5f-13fb-db210c514f9b', 'wholeLvl' => 4),
            'listShopClass' => array('shopCode' => 'b3ba491e-d775-3dbe-1179-a0add7122cb0'),
            'getClassInfo' => array('classCode' => 'ec6f7d0e-af50-dc2b-a840-2a0b9bd32246', 'userCode' => '929fe58f-cfab-45d8-c3bc-9a85717f1863'),
            'getShopTeacherInfo' => array('teacherCode' => '43d1142a-a83e-586f-1abe-83f49bfd4f72'),
            'listShopTeacher' => array('shopCode' => 'b3ba491e-d775-3dbe-1179-a0add7122cb0', 'page' => 1),
            'getUserActList' => array(
                'userCode' => '929fe58f-cfab-45d8-c3bc-9a85717f1863',
                'type' => '1',
                'page' => 1,
            ),
            'getInfoWhenCouponPay' => array(
                'batchCouponCode' => '7531dc9c-7d09-cc79-7b7b-abf70af31501',
                'userCode' => '929fe58f-cfab-45d8-c3bc-9a85717f1863'
            ),
            'actCodeApplyRefund' => array(
                'orderCode' => 'e5236f0c-ff96-6153-8b3f-14cf43ff7fd6',
                'actCode' => '0003032283',
                'bankcardRefund' => '45.0',
                'platBonusRefund' => '0.0',
                'shopBonusRefund' => '0.0',
                'refundReason' => '1',
                'refundRemark   ' => '',
            ),
            'getUserActCodeInfo' => array(
                'actCode' => '0001825383'
            ),
            'getBatchCouponInfo' => array(
                'batchCouponCode' => 'bca9e38d-75d9-d6d6-c436-e8fb488fb76c',
                'longitude' => 120,
                'latitude' => 30
            ),
            'getBatchCouponInfoHasUser' => array(
                'batchCouponCode' => '0a52c270-1b28-8529-9608-0f5f2ee219e9',
                'userCode' => '5d4b7ad2-1d88-c1d1-1cf1-d8c7ad46165d',
                'longitude' => 120,
                'latitude' => 30
            ),
            'getInfoPreActInfo' => array(
                'userCode' => 'b897417b-a3e5-3528-f139-e17aeb79b93c',
                'actCode' => '7b08ed8d-0471-e30d-89c1-3ad50dc479e6',
            ),
            'submitActOrder' => array(
                'userCode' => '9842509a-7b47-21b6-0d86-5692d5012bd5',
                'shopCode' => 'a51cde1c-0955-a042-4a4f-48f7c4107f22',
                'actCode' => '2780b842-5b3d-f52c-5387-ad3b9afaa7d6',
                'orderInfo' => '[{"id":"1","nbr":"5","price":"12.5"}]',
                'bookingName' => '季华飞',
                'mobileNbr' => '15868179748',
                'orderAmount' => 62.5,
                'platBonus' => 0,
                'shopBonus' => 0,
            ),
            'addCouponOrder' => array(
                'userCode' => '91d6c1fc-f5e2-fe5f-13fb-db210c514f9b',
                'shopCode' => '445bfe41-373e-20b6-3e26-c86445376517',
                'batchCouponCode' => '8b8ef5a4-836a-1926-3995-b0741f33e9ea',
                'couponNbr' => 1
            ),
            'editFreeVal' => array(
                'userCode' => '9842509a-7b47-21b6-0d86-5692d5012bd5',
                'freeValCodePay' => '1',
            ),
            'listUserCouponWhenPay' => array(
                'userCode' => 'd5accbaf-047f-efca-c17a-ef04e267a4e9',
                'shopCode' => 'cec32281-38ff-43bf-4db2-a5072390f716',
                'consumeAmount' => '100',
                'couponType' => 3
            ),
            'listSearchWords' => array(
                'city' => '湖州',
            ),
            'getHomeShopList' => array(
                'userCode' => '',
                'longitude' => '120',
                'latitude' => '30',
                'page' => '1',
                'city' => '丽水市',
            ),
            'getHomeInfo' => array(
                'city' => '湖州市'
            ),
            'readMessage' => array(
                'userCode' => '8af6ad90-711e-8ffe-12be-63581a41c154',
                'type' => '0',
            ),
            'listUserPayInfo' => array(
                'userCode' => '91d6c1fc-f5e2-fe5f-13fb-db210c514f9b',
                'shopCode' => 'gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC',
                'consumeAmount' => '',
                'batchCouponCode' => '7446824a-f362-b58a-b01b-b3cf366d7e16'
            ),
            'getUserShopRecord' => array(
                'userCode' => '',
                'shopCode' => '633680b7-cbd3-3f40-735a-8ee644f37e62',
            ),
            'getAct' => array(
                'pos' => 3
            ),
            'getClientHomePage' => array(
                'city' => '杭州'
            ),
            'addNotTakeoutOrder' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => 'cec32281-38ff-43bf-4db2-a5072390f716',
                'productList' => '[{"productId":90,"productNbr":2,"productUnitPrice":9},{"productId":92,"productNbr":2,"productUnitPrice":9}',
            ),
            'addNotTakeoutOrderOtherInfo' => array(
                'orderCode' => 'd5be237d-b92e-466b-c42d-f6e03f1521ea',
                'remark' => '苦，我要变态苦'
            ),
            'addTakeoutOrder' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'productList' => '[{"productId":1,"productNbr":2,"productUnitPrice":3},{"productId":2,"productNbr":2,"productUnitPrice":10}]',
            ),
            'addTakeoutOrderOtherInfo' => array(
                'orderCode' => 'e4f2a0bd-5ba9-cabb-a607-0be755d17f8f',
                'userAddressId' => '9',
                'remark' => ''
            ),
            'getProductInfo' => array(
                'productId' => '1'
            ),
            'getProductList' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
            ),
            'getUserAddressInfo' => array(
                'userAddressId' => '1'
            ),
            'getUserAddressList' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764'
            ),
            'delUserAddress' => array(
                'userAddressId' => '3'
            ),
            'editUserAddress' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'contactName' => '季华飞',
                'mobileNbr' => '15868179748',
                'province' => '浙江省',
                'city' => '杭州市',
                'district' => '余杭区',
                'street' => '仓益绿源',
                'userAddressId' => '1'
            ),

            'addUserAddress' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'contactName' => '季华飞',
                'mobileNbr' => '15868179748',
                'province' => '浙江省',
                'city' => '杭州市',
                'district' => '西湖区',
                'street' => '文一西路222号'
            ),
            'getSystemParam' => array(),
            'getPayValCodeQuickly' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'accountName' => '哈哈哈',
                'idType' => '0',
                'idNbr' => '344418199307076877',
                'accountNbrPre6' => '622222',
                'accountNbrLast4' => '7398',
                'mobileNbr' => '18659859685',
                'consumeCode' => '0005ff6d-e451-d611-478e-6809b0c675b6'
            ),
            'getConsumeInfo' => array(
                'consumeCode' => 'ff006a06-70ca-92a1-2fd1-73a96d385701'
            ),
            'zeroPay' => array(
                'userCode' => '91d6c1fc-f5e2-fe5f-13fb-db210c514f9b',
                'shopCode' => 'gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC',
                'userCouponCode' => '4076a174-f04b-d6f4-bc5f-51ff29c72784'
            ),
            'getUserInviteCode' => array(
                'userCode' => '10502072-dc79-d21c-421d-bc8f429449db'
            ),
            'getShopBankCardDiscount' => array(
                'shopCode' => $shopCode
            ),
            'getUserOrderList' => array(
                'userCode' => '929fe58f-cfab-45d8-c3bc-9a85717f1863',
                'isFinish' => 0,
                'page' => 1,
            ),
            'validatePayPwd' => array(
                'userCode' => '605dc410-4c60-8683-26f4-5aa0cb6308aa',
                'payPwd' => md5('123456')
            ),
            'isUserSetPayPwd' => array('userCode' => '605dc410-4c60-8683-26f4-5aa0cb6308aa'),
            'setPayPwd' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'payPwd' => md5('123456'),
                'confirmPayPwd' => md5('123456')
            ),
            'getUserActInfo' => array('userActCode' => '01927e66-e5d5-4879-5544-5460bb686c50'),
            'listActModule' => array(
                'city' => '杭州'
            ),
            'editUserActInfo' => array(
                'userActCode' => 'c36dc09b-bfff-e745-dd46-dbb6faebfe7c',
                'adultM' => 1,
                'adultF' => 20,
                'kidM' => 20,
                'kidF' => 20
            ),
            'exitAct' => array('userActCode' => '10c35296-56fd-302d-99e7-2d5152bb4bc8'),
            'listUserAct' => array(
                'userCode' => '605dc410-4c60-8683-26f4-5aa0cb6308aa',
                'longitude' => 119.990074,
                'latitude' => 30.275362,
                'page' => 1
            ),
            'guessYouLikeShop' => array(
                'userCode' => $userCode,
                'shopCode' => $shopCode,
                'longitude' => 120,
                'latitude' => 30,
            ),
            'getMyBonus' => array(
                'userCode' => $userCode,
            ),
            'getIcbcValCode' =>array(
                'accountNbr' => '8888888888',
                'accountName' => '季华飞',
                'amount' => 0,
                'idType' => '0',
                'idNbr' => '554955699848551238',
                'mobileNbr' => '18858585858',
                'orderNbr' => '11589856484c-997928181764',
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
            ),
            'getPlateBonus' => array(),
            'listOpenCity' => array(),
            'register' => array(
                'mobileNbr' => '11122233324',
                'validateCode' => '875964',
                'password' => md5('123456'),
                'recomNbr' => '',
            ),
            'updateUserInfo' => array(
                'mobileNbr' => 15868179748,
                'updateInfo' => array(
                    'realName' => 'MMM',
                )
            ),
            'getUserInfo' => array(
                'userCode' => 'd21610a3-7d4b-98cd-ef9e-bc4eec7b3bd1',
            ),
            'getUserSetting' => array('userCode' => 'bf08b2b9-38f1-5d98-4a3a-3dd1b9db71ef'),
            'updateUserSetting' => array(
                'userCode' => $userCode,
                'settingInfo' => array(
                    'isBroadcastOn' => '1',
                    'isMsgBingOn' => '1',
                    'isCouponMsgOn' => '1',
                )
            ),
            'searchShop' => array(
                'searchWord' => '',
                'type' => 0,
                'longitude' => 119.985983,
                'latitude' => 30.277930,
                'userCode' => '',
                'page' => 1,
                'city' => '义乌市',
                'moduleValue' => '0',
                'content' => '',
                'order' => '0',
                'filter' => '0'
            ),
            'getShopInfo' => array(
                'shopCode' => 'b3ba491e-d775-3dbe-1179-a0add7122cb0',
                'userCode' => '9842509a-7b47-21b6-0d86-5692d5012bd5'
            ),
            'cGetShopProductAlbum' => array(
                'shopCode' => '036aab74-680e-1e1f-be4c-47ecec4096fc',
                'page' => 1,
            ),
            'cGetSubAlbumPhoto' => array(
                'code' => 21
            ),
            'cGetShopDecoration' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'page' => 1
            ),
            'followShop' => array(
                'userCode' => '',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
            ),
            'cancelFollowShop' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => '15dc1dec-801b-58db-9f80-9a7c6393d2a2',
            ),
            'listFollowedShop' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'longitude' => 120,
                'latitude' => 30,
                'page' => 1
            ),
            'listFollowedShopB' => array(
                'userCode' => 'b897417b-a3e5-3528-f139-e17aeb79b93c',
                'longitude' => 119.990712,
                'latitude' => 30.275237,
                'page' => 1
            ),
            'listFootprint' => array(
                'userCode' => 'f47093de-348c-158a-48c0-1ec836804b6d',
                'longitude' => 120,
                'latitude' => 30,
                'page' => 1
            ),
            'listFootprintB' => array(
                'userCode' => '91d6c1fc-f5e2-fe5f-13fb-db210c514f9b',
                'longitude' => 120,
                'latitude' => 30,
                'page' => 2
            ),
            'listNearShop' => array(
                'city' => '杭州',
                'longitude' => 120,
                'latitude' => 30,
                'page' => 1,
                'userCode' => '4d40a5b6-bded-b837-3998-17f0a93c1178'
            ),
            'listNearShopB' => array(
                'city' => '杭州',
                'longitude' => 120,
                'latitude' => 30,
                'page' => 1,
                'userCode' => '4d40a5b6-bded-b837-3998-17f0a93c1178'
            ),
            'getCardList' => array(
                'userCode' => '4d40a5b6-bded-b837-3998-17f0a93c1178',
                'longitude' => 120,
                'latitude' => 120,
                'isFollowed' => 4,
                'page' => 1
            ),
            'getBestUserCard' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
            ),
            'getUserCardInfo' => array(
                'userCardCode' => '05346035-fff0-1ec5-30bb-28a70401fae3'
            ),
            'applyCard' => array(
                'cardCode' => '8a9daedf-e6a1-f369-4c58-43b624ce6eb2',
                'userCode' => '09755b11-27b4-cea5-484c-997928181764'
            ),
            'searchCard' => array(
                'searchWord' => '',
                'city' => '杭州',
                'longitude' => 120.098016,
                'latitude' => 30.897775,
                'userCode' => '2200d403-042d-28ac-f0bc-0f0708ad0267',
                'page' => 1,
                'isFollowed' => 1
            ),
            'countUserNotUsedCoupon' => array(
                'userCode' => '410bf330-bdcc-b82a-21f4-a8b03790a707'
            ),
            'listCoupon' => array(
                'couponType' => '4',
                'searchWord' => '好',
                'longitude' => 119.99675,
                'latitude' => 30.281819,
                'page' => 1,
                'city' => '杭州',
            ),
            'getMyAvailableCoupon' => array(
                'userCode' => '0fd7e668-6dbf-ae2f-1b59-c8de5301eb1e',
                'shopCode' => '',
                'status' => 1,
                'page' => 1,
                'longitude' => 120,
                'latitude' => 30
            ),
            'getUserCouponInfo' => array(
                'userCouponCode' => '9dcb3817-b479-6f12-acce-c036bdc977be'
            ),
            'countReceivedCoupon' => array(
                'batchCouponCode' => '1696c4d8-4612-39b3-7897-28209e5e1424'
            ),
            'updateCouponSharedStatus' => array(
                'userCouponCode' => 'f2aac8d0-9c89-1640-89f8-d95f9b6139ed',
                'userCode' => $userCode,
                'sharedLvl' => '2'
            ),
            'grabCoupon' => array(
                'batchCouponCode' => '7446824a-f362-b58a-b01b-b3cf366d7e16',
                'userCode' => '91d6c1fc-f5e2-fe5f-13fb-db210c514f9b',
                'sharedLvl' => 0
            ),
            'extortCouponRequest' => array(
                'sellerCode' => $userCode,
                'buyerCode' => '6478b2ab-f558-bf83-4a8a-00a11244a225',
                'batchCouponCode' => 'fa8ee867-082c-5502-3aca-dd787a1df569',
                'price' => 0
            ),
            'extortCouponReply' => array(
                'logCode' => 'bd16bc5b-5478-e33f-1348-ea35e4f1934e',
                'result' => 1
            ),
            'transferCoupon' => array(
                'sellerCode' => $userCode,
//                'buyerCode' => '6478b2ab-f558-bf83-4a8a-00a11244a225',
                'buyerCode' => '',
                'couponCode' => '68446281-00b6-5a40-234c-0d9d5da787cf',
                'price' => 0,
            ),
            'receiveCoupon' => array(
                'logCode' => 'f9dc5ee3-72bd-8116-a393-46f27ad4f0e8',
                'buyerCode' => '6478b2ab-f558-bf83-4a8a-00a11244a225'
            ),
            'listUserShopBonus' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
            ),
            'grabBonus' => array(
                'userCode' => '22096fcb-301e-e91d-711d-ebf171072aab',
                'bonusCode' => 'dac7a270-1efc-7f31-1a0e-a1810e6664d7',
            ),
            'getScrollInfo' => array(
//                'activityNbr' => 7,
            ),
            'getActivityList' => array(
                'type' => '1',
                'shopCode' => '',
                'longitude' => 120,
                'latitude' => 30,
                'page' => 1,
                'city' => ''
            ),
            'getActivityInfo' => array(
                'activityCode' => '1f35c426-79f3-21ef-ffd5-a996de1d9825'
            ),
            'joinActivity' => array(
                'userCode' => $userCode,
                'activityCode' => '2e82e098-8585-23e7-c8db-a420953673d0',
                'adultM' => 1,
                'adultF' => 1,
                'kidM' => 1,
                'kidF' => 1,
            ),
            'getMessageList' => array(
                'userCode' => '91d6c1fc-f5e2-fe5f-13fb-db210c514f9b',
                'type' => 2,
                'page' => 1,
            ),
            'countAllTypeMsg' => array(
                'userCode' => 'cfd22e8f-3e05-f2f2-d2cc-e03a520c3a59',
            ),
            'getBankAccountList' => array(
                'userCode' => '2342375e-5121-6dc1-4f0a-bced4c6adb7c',
                'page' => 1
            ),
            'addBankAccount' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'accountName' => '射簧',
                'idType' => '0',
                'idNbr' => '345517196803190587',
                'accountNbrPre6' => '524048',
                'accountNbrLast4' => '6305',
                'mobileNbr' => 15757282649
            ),
            'getSignCardValCode' => array(
                'orderNbr' => '15102710445300003'
            ),
            'cancelBankAccount' => array(
                'orderNbr' => '1508280001',
            ),
            'signBankAccount' => array(
                'orderNbr' => '15102519500700001',
                'valCode' => '123456',
            ),
            'terminateBankAccount' => array(
                'bankAccountCode' => '406b96ef-51d5-12e9-3570-d8b5aaf2bd4d'
            ),
            'getBankAccountInfo' => array(
                'bankAccountCode' => '11d7b4d8-dc93-d949-77fe-f707a5970965'
            ),
            'listUserCoupon' => array(
                'userCode' => '0fd7e668-6dbf-ae2f-1b59-c8de5301eb1e',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'consumeAmount' => 0
            ),
            'listUserBonus' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'consumeAmount' => 30
            ),
            'getNewPrice' => array(
                'userCode' => '6fcfa418-c568-47c8-a6fd-7fbe38c90000',
                'shopCode' => 'cec32281-38ff-43bf-4db2-a5072390f716',
                'consumeAmount' => 100,
                'userCouponCode' => '',
                'platBonus' => 0,
                'shopBonus' => 0,
                'payType' => '1'
            ),
            'getNewPriceForAndroid' => array(
                'userCode' => '929fe58f-cfab-45d8-c3bc-9a85717f1863',
                'shopCode' => 'gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC',
                'consumeAmount' => 2,
                'batchCouponCode' => '',
                'nbrCoupon' => 1,
                'platBonus' => 2.1,
                'shopBonus' => 0,
                'payType' => '1',
                'noDiscountPrice' => '0'
            ),
            'addOrder' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'price' => 150,
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
            ),
            'cancelOrder' => array(
                'orderCode' => '7582b0cf-da5d-7629-e05d-051cadbf9b1c',
            ),
            'bankcardPay' => array(
                'userCode' => '91d6c1fc-f5e2-fe5f-13fb-db210c514f9b',
                'shopCode' => 'gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC',
                'orderAmount' => 1,
                'userCouponCode' => '',
                'platBonus' => 0,
                'shopBonus' => 0,
            ),
            'bankcardPayForAndroid' => array(
                'userCode' => 'e5abc323-7ad6-663b-f3ac-917c6d903f27',
                'shopCode' => 'a51cde1c-0955-a042-4a4f-48f7c4107f22',
                'orderAmount' => 200,
                'batchCouponCode' => '6e0f7d1c-42b0-432c-ec32-5b0d5dd2c4f7',
                'nbrCoupon' => 1,
                'platBonus' => 0,
                'shopBonus' => 0,
            ),
            'cancelBankCardPay' => array(
                'consumeCode' => 'fcf9db9f-6ca4-5502-56e7-ce48f5ff3832',
            ),
            'pOBankcardPay' => array(
                'orderCode' => 'e4f2a0bd-5ba9-cabb-a607-0be755d17f8f',
                'userCouponCode' => '',
                'platBonus' => 0,
                'shopBonus' => 0,
            ),
            'listAllBankCard' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
            ),
            'getIcbcPayValCode' => array(
                'consumeCode' => '1f3abdde-5e98-8cae-9101-8d0431c6e7bd',
                'bankAccountCode' => '0ca6407a-b1e1-840c-18a6-9135744ca512',
                'mobileNbr' => '13738157214'
            ),
            'bankcardPayConfirm' => array(
                'consumeCode' => '9d67346f-b2a4-cb9d-ef26-ad4de928e79b',
                'bankAccountCode' => 'fe7111ec-3dbb-2dbe-8ef7-c3419532fc3c',
                'valCode' => '111111',
            ),
            'posPay' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'userCouponCode' => '',
                'platBonus' => 0,
                'shopBonus' => 0,
                'price' => 500,
            ),
            'cashPay' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => 'f9532d03-84b5-8c2d-a59e-ed5832804baa',
                'userCouponCode' => '',
                'platBonus' => 0,
                'shopBonus' => 0,
                'price' => 100,
            ),
            'sendMsg' => array(
                'shopCode' => '00000000-0000-0000-0000-000000000000',
                'userCode'=> '09755b11-27b4-cea5-484c-997928181764',
                'message'=> '我好饿啊！！！'
            ),
            'readMsg' => array(
                'userCode'=> $userCode,
                'shopCode' => $shopCode
            ),
            'countUnreadMsg' => array(
                'userCode'=> $userCode,
                'shopCode' => $shopCode
            ),
            'getMsg' => array(
                'userCode'=> $userCode,
                'shopCode' => $shopCode,
                'all'=> 0
            ),
            'getMsgGroup' => array(
                'userCode' => $userCode,
                'page' => 1
            )
        );
        $clientParams[$method]['reqtime'] = time();
        $request_key1 = array_keys($clientParams[$method]);
        $param1 = $clientParams[$method][$request_key1[0]];
        $clientParams[$method]['vcode'] = $token.MD5($method.$param1.$sKey);
        return $clientParams[$method];
    }

    public function upload(){
        $this->display();
    }

    public function testXml() {
        $str = '<?xml version="1.0"?><ans><result>1</result><amount>2</amount><cmptxsno>3</cmptxsno></ans>';
        $xml = simplexml_load_string($str);
        var_dump($xml);
    }

    public function testDateTimePicker() {
        $this->display();
    }
}