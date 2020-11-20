<?php
namespace Home\Controller;
use Common\Model\BatchCouponModel;
use Common\Model\BonusModel;
use Common\Model\JpushModel;
use Common\Model\ShopModel;
use Common\Model\SystemParamModel;
use Common\Model\UserBonusModel;
use Common\Model\UserCouponModel;
use Common\Model\UserModel;
use JPush\Exception\APIRequestException;
use Think\Controller;
use Org\FirePHPCore\FP;
class IndexController extends Controller {

    public function index() {
        // 根据域名判断进入的模块
        $domainName = $_SERVER['HTTP_HOST'];
        switch($domainName) {
            case 'web.huiquan.suanzi.cn':
                $url = '/Admin';
                break;
            case 'api.huiquan.suanzi.cn':
                $url = '/Admin';
                break;
            case 'www.huiquan.net.cn':
                $url = '/Client';
                break;
            default :
                $url = '/Admin';
                break;
        }
        redirect($url);
    }

    public function checkPhp() {
        echo phpinfo();
    }

    public function testEncoding() {
        $txt = file('./Public/huiquan.settle.1205.20151227.001.txt');
        $newTxt = '';
        foreach($txt as $v) {
            $v = iconv('UTF-8', 'GBK', $v);
            $newTxt .= $v;
        }
        $myfile = fopen('./Public/a.txt', 'w');
        fwrite($myfile, $newTxt);
        fclose($myfile);
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
            'addRegId' => array(
                'device' => 'a4.4_720*1280*2.0_100_XiaoMi_WIFI_XXXXXXXXXXXX',
                'regId' => '010bcd85d2b',
                'appType' => 2,
            ),
            'getValidateCode' => array(
                'mobileNbr' => '15868177459',
                'action' => 'mr',
                'appType' => 's',
                'extra' => '{\"shopCode\":\"gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC\"}'
            ),
            'login' => array(
                'mobileNbr' => '15827037207',
                'password' => md5('123456'),
                'loginType' => '0',
                'registrationId' => ''
            ),
            'logoff' => array(
                'tokenCode' => '08ae4ed820f13979ffdba07b53e24027'
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
        }elseif($_SERVER['HTTP_HOST'] == 'baomi.sz.cn') {
            $url = "http://baomi.sz.cn/Api/Shop";
        }elseif($_SERVER['HTTP_HOST'] == 'baomi.com'){
            $url = "http://baomi.com/Api/Shop";
        } else {
            $url = "http://baomi.suanzi.cn/Api/Shop";
        }
        $client = new \jsonRPCClient($url);
        $method = 'getShopHeaderInfo';    //方法名
        $params = $this->getShopParams($method);
        $params['tokenCode'] = '00000000000000000000000000000000';
        $ret = $client->$method($params);
        var_dump($ret);
    }

    /**
     * 获得商家端端测试参数
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
            'getShopHeaderInfo' => array("shopCode"=>"b3ba491e-d775-3dbe-1179-a0add7122cb0"),
            'delShopClass' => array('classCode' => '79fcaf76-7ca3-80f4-ca0b-ddd1d6f1ecdb'),
            'getShopClassList' => array(
                'shopCode' => 'b3ba491e-d775-3dbe-1179-a0add7122cb0',
                'page' => 1
            ),
            'getShopClassInfo' => array(
                'classCode' => '09183081-4d74-2b0a-84df-78a2260f5379',
            ),
            'getShopTeacherList' => array(
                'shopCode' => 'b3ba491e-d775-3dbe-1179-a0add7122cb0',
                'page' => 0
            ),
            'getShopTeacherInfo' => array(
                'teacherCode' => '43d1142a-a83e-586f-1abe-83f49bfd4f72',
            ),
            'editShopTeacher' => array(
                'teacherCode' => '',
                'updateData' => '{"teacherName":"aaa","teacherTitle":"一级教师","shopCode":"82b6cd96-a897-e7f1-717c-6d4f6bac4d92","teacherWork":[{"workUrl":"/Public/Img/avatar.jpg"}],"teacherInfo":"aaaaa"}',
            ),
            'addShopClass' => array(
                'className' => '跆拳道',
                'learnStartDate' => '2016-03-10',
                'learnEndDate' => '2016-06-10',
                'learnMemo' => '小学',
                'learnFee' => '3000',
                'learnNum' => '50',
                'teacherCode' => '761c4076-cb33-d330-c6a3-40f33d699ed3',
                'signStartDate' => '2016-03-01',
                'signEndDate' => '2016-03-09',
                'classInfo' => 'SD敢达返回给电话',
                'classUrl' => '/Public/Img/avatar.jpg',
                'classWeekInfo' => '[{"weekName":"2","startTime":"09:00","endTime":"11:00"},{"weekName":"4","startTime":"09:00","endTime":"11:00"}]'
            ),
            'getOrderListForB' => array(
                'shopCode' => 'gXjhagS3-xQ7Q-Nv4k-UjA1-Kn4J58WIqnYC',
                'keyword' => '',
                'date' => '2016-02-26',
                'page' => '1',
            ),
            'changeActivityStatus' => array(
                'activityCode' => '6f0e7541-c579-03fe-1705-082287bc42ed',
                'status' => '2'
            ),
            'editActivity' => array(
                'activityCode' => '9a3df152-8673-5082-3f7a-490c5d099abc',
                'updateData' => '{"limitedParticipators":"0","dayOfBooking":"0","activityImg":"\/Public\/Uploads\/20160125\/14537042973120.jpg","contactMobileNbr":"11111111","endTime":"2016-01-30 14:15:00","actType":"2","richTextContent":"lllllllllllllll","startTime":"2016-01-21 07:15:00","refundRequired":"0","contactName":"\u554a\u5723\u8bde\u8282\u554a","activityBelonging":"3","feeScale":"[]","registerNbrRequired":"5","activityLocation":"ccc","activityName":"cc"}'
            ),
            'listConsumeOrder' => array(
                'staffCode' => '910e90f4-aa0a-ca59-76dd-c1cb0e70ce02',
                'startTime' => '2016-01-25',
                'endTime' => '2016-01-25',
                'shopName' => '',
                'orderStatus' => '3',
                'orderNbr' => '',
                'mobileNbr' => '',
                'page' => 1
            ),
            'consumeOrderStatistics' => array(
                'staffCode' => '910e90f4-aa0a-ca59-76dd-c1cb0e70ce02',
                'startTime' => '',
                'endTime' => '',
                'shopName' => '',
                'orderStatus' => '3',
                'orderNbr' => '',
                'mobileNbr' => '',
            ),
            'getShopDes' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
            ),
            'isHasProduct' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
            ),
            'getShopDayBrowseQuantity' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
            ),
            'getShopAllBrowseQuantity' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
            ),
            'editOwner' => array(
                'staffCode' => '33c896c2-2d43-ad15-b5eb-d550737d7ba4',
                'shopCode' => 'b8a888c7-0543-feb7-ce48-103e941c63b5|e8364419-c627-2a05-de42-b86b39154a68|5c639493-29d8-215a-1733-9c62c420392c'
            ),
            'getStoreBelong' => array(
                'staffCode' => '33c896c2-2d43-ad15-b5eb-d550737d7ba4',
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
            'getClerkAdmin' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'page' => 1
            ),
            'getManAdmin' => array(
                'staffCode' => '33c896c2-2d43-ad15-b5eb-d550737d7ba4',
                'page' => 1
            ),
            'getStaffShopList' => array(
                'staffCode' => '8311808e-3aca-51ea-0eca-7e1d927e9b95',
                'page' => 1
            ),
            'getAccount' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'page' => 1
            ),
            'countOrderByType' => array(
                'shopCode' => ''
            ),
            'submitEnd' => array(
                'orderCode' => '370d0952-3329-8ca2-dbbf-fe738e7a7631',
                'orderProductList' => array(2081, 2082),
                'actualOrderAmount' => ""
            ),
            'getOrder' => array(
                'shopCode' => '5650d9e4-05e1-9ee1-2313-ec493b3cd3dd',
                'keyWord' => '',
                'unit' => 'D',
                'value' => '11-06',
                'status' => 0,
                'orderType' => '20'
            ),
            'addNewOrderProduct' => array(
                'orderCode' => '65045776-2dcb-4da8-c70f-eb8d69e1b1b8',
                'productList' => array(
                    array(
                        'productId' => 1,
                        'productNbr' => 50,
                        'productUnitPrice' => 10
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
                'orderCode' => 'd48b2493-5a83-3753-692f-c9cd24efd124',
                'orderStatus' => '21',
                'status' => ''
            ),
            'getProductOrderInfo' => array(
                'orderCode' => 'd48b2493-5a83-3753-692f-c9cd24efd124',
                'isNewlyAdd' => ''
            ),
            'listProductOrder' => array(
                'shopCode' => '5650d9e4-05e1-9ee1-2313-ec493b3cd3dd',
                'orderStatus' => '',
                'orderType' => ''
            ),
            'delProduct' => array(
                'productId' => '2'
            ),
            'listProductByCategory' => array(
                'categoryId' => '',
                'shopCode' => '5650d9e4-05e1-9ee1-2313-ec493b3cd3dd'
            ),
            'editProductStatus' => array(
                'productId' => '10',
                'productStatus' => '1'
            ),
            'editProduct' => array(
                'productName' => '扫把',
                'categoryId' => '2',
                'productImg' => '/Public/Img/avatar.jpg',
                'notTakeoutPrice' => 'aa',
                'takeoutPrice' => '10',
                'recommendLevel' => '0',
                'spicyLevel' => '0',
                'unit' => '个',
                'isTakenOut' => '1',
                'des' => '中国好拖把',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'sortNbr' => '0',
                'productId' => '10'
            ),
            'addProduct' => array(
                'productName' => '拖把',
                'categoryId' => '2',
                'productImg' => '/Public/Img/avatar.jpg',
                'notTakeoutPrice' => 'aa',
                'takeoutPrice' => 'aa',
                'recommendLevel' => '0',
                'spicyLevel' => '0',
                'unit' => '个',
                'isTakenOut' => '1',
                'des' => '中国好拖把',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'sortNbr' => '0'
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
                'categoryName' => '推荐菜',
                'shopCode' => '5650d9e4-05e1-9ee1-2313-ec493b3cd3dd'
            ),
            'getConsumeInfo' => array(
                'consumeCode' => 'f41416bd-bf9d-b4b3-a2d0-689f0f70e033'
            ),
            'getSystemParam' => array(),
            'getShopInviteCode' => array(
                'shopCode' => '9370e17b-f245-a127-71ea-eb065d612c66',
                'month' => 9
            ),
            'pcPay' => array(
                'userCouponNbr' => '1150800001',
                'shopCode' => '9370e17b-f245-a127-71ea-eb065d612c66',
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
                'batchCouponCode' => '2431fd8b-a010-829a-baf1-27913ec878a7',
                'page' => 1,
            ),
            'changeCouponStatus' => array(
                'batchCouponCode' => '2431fd8b-a010-829a-baf1-27913ec878a7',
                'isAvailable' => 1,
            ),
            'getCouponInfo' => array(
                'batchCouponCode' => '671cc0f9-aa50-1e43-a86e-02e6deab441c',
            ),
            'listShopCoupon' => array(
                'shopCode' => 'f508e052-93ff-d8a5-9ff0-732f71948c27',
                'time' => 2,
                'page' => 1
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
                'password' => md5('123456'),
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
                'shopCode' => '00b161f8-d57a-d949-429c-0b70da38b6e1'
//                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
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
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'couponType' => 3,
                'totalVolume' => 50,
                'startUsingTime' => '2015-8-30',
                'expireTime' => '2015-10-30',
                'dayStartUsingTime' => '00:00:00',
                'dayEndUsingTime' => '23:59:59',
                'startTakingTime' => '2015-8-30',
                'endTakingTime' => '2015-10-30',
                'isSend' => '0',
                'sendRequired' => '0',
                'remark' => '抵扣券',
                'creatorCode' => '9de92517-4b67-265a-ed79-4c29a67cdaa8',
                'discountPercent' => 0,
                'insteadPrice' => 50,
                'availablePrice' => 100,
                'function' => '年满18岁可用',

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
            'sGetShopBasicInfo' => array( 'shopCode' => '9370e17b-f245-a127-71ea-eb065d612c66'),
            'sGetShopInfo' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082'
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
                'shopCode' => 'cec32281-38ff-43bf-4db2-a5072390f716',
                'updateKey' => 'businessHours',
                'updateValue' => '05:00,09:00;14:00,20:00'
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
                'lastFourOfOrderNbr' => '0001',
                'shopCode' => 'f508e052-93ff-d8a5-9ff0-732f71948c27'
            ),
            'getShopStaffSetting' => array(
                'mobileNbr' => '15868179748'
            ),
            'updateShopStaffSetting' => array(
                'staffCode' => '9de92517-4b67-265a-ed79-4c29a67cdaa8',
                'setting' => 0,
            ),
            'sweepQrCode' => array(
                'validateString' => 'bc21f186-f595-0bcc-ff88-8587ce4ba26f97aaa1860N5I6961af456961JKb27'
                //91d6c1fc-f5e2-fe5f-13fb-db210c514f9b97aaa7c2568c9af2rg568c9af2rg
                //d21610a3-7d4b-98cd-ef9e-bc4eec7b3bd11e315231ci5694c7Rf0
            ),
            'getOptimalPay' => array(
                'userCode' => '91d6c1fc-f5e2-fe5f-13fb-db210c514f9b',
                'shopCode' => 'a51cde1c-0955-a042-4a4f-48f7c4107f22',
                'orderAmount' => '230',
                'noDiscountPrice' => '30'
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
        }elseif($_SERVER['HTTP_HOST'] == 'baomi.sz.cn'){
            $url = "http://baomi.sz.cn/Api/Client";
        }elseif($_SERVER['HTTP_HOST'] == 'baomi.com'){
            $url = "http://baomi.com/Api/Client";
        }else{
            $url = "http://baomi.suanzi.cn/Api/Client";
        }
        dump($url);
        $client = new \jsonRPCClient($url);
        $method = 'getUserActList';    //方法名
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
            'getUserActList' => array(
                'userCode' => 'd21610a3-7d4b-98cd-ef9e-bc4eec7b3bd1',
                'type' => 3,
                'page' => 1
            ),
            'getHomeInfo' => array(
                'city' => '湖州'
            ),
            'AddErrorInformation' => array(
                'userCode' => '0fd7e668-6dbf-ae2f-1b59-c8de5301eb1e',
                'errorInfo' => '1',
                'errorImg' => '1',
                'toShopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
            ),
            'errorInfo' => array(
            ),           
            'listSearchWords' => array(
                'cityName' => '杭州市',
            ),
            'listUserPayInfo' => array(
                'userCode' => '0fd7e668-6dbf-ae2f-1b59-c8de5301eb1e',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'consumeAmount' => 0,
                'batchCouponCode' => ''
            ),
            'scanCode' => array(
                'userCode' => '7c121767-98f6-5025-45af-35b19f4e3a7b',
                'shopCode' => '633680b7-cbd3-3f40-735a-8ee644f37e62',
            ),
            'getUserShopRecord' => array(
                'userCode' => '1641286db922fd9a42dd725158f5b62f',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
            ),
            'getAct' => array(
                'pos' => 3
            ),
            'getClientHomePage' => array(
                'city' => '杭州'
            ),
            'addNotTakeoutOrder' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'productList' => '[{"productId":1,"productNbr":2,"productUnitPrice":3},{"productId":2,"productNbr":2,"productUnitPrice":10}',
            ),
            'addNotTakeoutOrderOtherInfo' => array(
                'orderCode' => '860644fb-d72b-8a86-a395-8781a092fc2a',
                'remark' => '苦，我要变态苦'
            ),
            'addTakeoutOrder' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'productList' => '[{"productId":1,"productNbr":2,"productUnitPrice":3},{"productId":2,"productNbr":2,"productUnitPrice":10}]',
            ),
            'addTakeoutOrderOtherInfo' => array(
                'orderCode' => '860644fb-d72b-8a86-a395-8781a092fc2a',
                'userAddressId' => '16',
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
                'userCode' => 'e5abc323-7ad6-663b-f3ac-917c6d903f27',
                'accountName' => '吃个饭',
                'idType' => '0',
                'idNbr' => '344418199307076877',
                'accountNbrPre6' => '622202',
                'accountNbrLast4' => '7328',
                'mobileNbr' => 15757282649,
                'consumeCode' => '971c032d-ff0e-7b90-c98c-e0294fd58422'
            ),
            'getConsumeInfo' => array(
                'consumeCode' => '92e64984-1627-9472-4ad5-34306a920f97'
            ),
            'zeroPay' => array(
                'userCode' => 'd21610a3-7d4b-98cd-ef9e-bc4eec7b3bd1',
                'shopCode' => '8436a3e7-6008-4473-c40b-e5c59fa53e24',
                'userCouponCode' => '5fbc2448-9e37-2c07-4432-4d6602f01950'
            ),
            'getUserInviteCode' => array(
                'userCode' => '008bade1-aac2-c9eb-4f4e-f2200150560f'
            ),
            'getShopBankCardDiscount' => array(
                'shopCode' => $shopCode
            ),
            'getUserOrderList' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
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
                'recomNbr' => 'yha1p',
            ),
            'updateUserInfo' => array(
                'mobileNbr' => 15868179748,
                'updateInfo' => array(
                    'realName' => 'MMM',
                )
            ),
            'getUserInfo' => array(
                'userCode' => 'b2aef490-d70f-f809-f5b3-efc9facdfbde',
            ),
            'getUserSetting' => array('userCode' => $userCode),
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
                'longitude' => 119.990074,
                'latitude' => 30.275362,
                'userCode' => '2200d403-042d-28ac-f0bc-0f0708ad0267',
                'page' => 1,
                'city' => '杭州',
            ),
            'getShopInfo' => array(
                'shopCode' => '3a4e4a05-198e-0d03-ce7e-a7036ce70082',
                'userCode' => '6df56d79-9bde-f7ca-2141-9f16d1eb7aa1'
//                'userCode' => ''
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
            'listFootprint' => array(
                'userCode' => 'f47093de-348c-158a-48c0-1ec836804b6d',
                'longitude' => 120,
                'latitude' => 30,
                'page' => 1
            ),
            'listNearShop' => array(
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
                'couponType' => 4,
                'searchWord' => '',
                'longitude' => 119.990074,
                'latitude' => 30.275362,
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
                'batchCouponCode' => '1bcd80ad-33d0-b563-b5cd-2320a08b4882',
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'sharedLvl' => 2
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
                'userCode' => 'cfd22e8f-3e05-f2f2-d2cc-e03a520c3a59',
                'type' => 0,
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
                'orderNbr' => '15090800004'
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
                'userCode' => '6fcfa418-c568-47c8-a6fd-7fbe38c90bec',
                'shopCode' => 'ea3be81f-ada8-7fd6-965a-528e5bd6a480',
                'consumeAmount' => 199,
                'userCouponCode' => '14d5442b-676d-b847-639a-04a06910396b',
                'platBonus' => 0,
                'shopBonus' => 0,
                'payType' => '1'
            ),
            'getNewPriceForAndroid' => array(
                'userCode' => 'd21610a3-7d4b-98cd-ef9e-bc4eec7b3bd1',
                'shopCode' => 'cec32281-38ff-43bf-4db2-a5072390f716',
                'consumeAmount' => 24.6,
                'batchCouponCode' => 'ddaeb63c-e335-1eec-ce6d-d195c798d732',
                'nbrCoupon' => 1,
                'platBonus' => 18.6,
                'shopBonus' => 0,
                'payType' => '1'
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
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
                'shopCode' => 'bd8c73d8-2b53-105e-1f72-32d5b510f8be',
                'orderAmount' => 10,
                'userCouponCode' => '044cd23a-5fe2-3232-803d-11263c868057',
                'platBonus' => 0,
                'shopBonus' => 0,
            ),
            'cancelBankCardPay' => array(
                'consumeCode' => 'b14e22a0-eea4-36c7-eeb6-ca0a28c8b3a0',
            ),
            'pOBankcardPay' => array(
                'orderCode' => '7ffb9ec5-b65d-854b-baf9-3298d2cd18db',
                'userCouponCode' => '',
                'platBonus' => 0,
                'shopBonus' => 0,
            ),
            'listAllBankCard' => array(
                'userCode' => '09755b11-27b4-cea5-484c-997928181764',
            ),
            'getIcbcPayValCode' => array(
                'consumeCode' => '7b4aba47-394c-86e9-d95c-5b650ce1c841',
                'bankAccountCode' => '6b999145-6a45-13d3-a3ab-290cc1131c71',
                'mobileNbr' => '15757282649'
            ),
            'bankcardPayConfirm' => array(
                'consumeCode' => '5f5b216e-3096-f099-4b19-7456747b4c5e',
                'bankAccountCode' => 'fbb88ebe-cd3c-32b3-8d8b-98806ec5b032',
                'realPay' => 69.2,
                'valCode' => '123456',
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
                'userCode' => '9842509a-7b47-21b6-0d86-5692d5012bd5',
                'shopCode' => 'cec32281-38ff-43bf-4db2-a5072390f716',
                'userCouponCode' => '15003872-494d-e789-3b50-fb1365da2a47',
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
        dump($method);
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