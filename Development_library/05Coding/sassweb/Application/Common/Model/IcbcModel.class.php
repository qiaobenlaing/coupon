<?php
namespace Common\Model;
use Common\Model\BaseModel;
use SimpleXMLElement;
use Think\Exception;

/**
 * Created by PhpStorm.
 * User: huafeiji
 * Date: 7/13/2015
 * Time: 11:10 PM
 */
class IcbcModel extends BaseModel {

    /** @var string 请求xml的头部 */
    const REQ_PACK_HEADER = '<?xml version="1.0"?>';

    /** @var array API及其参数 */
    static $API_METHODS =  array(
        20100 => array(
            'name'     => '2.2	支付协议签订交易',
            'params'   => array(
                'cardno'   => array('cardno', '卡号', 'Char(10),银行卡号'),
                'username' => array('username', '持卡人户名', 'Char(100)'),
                'idtype'   => array('idtype', '证件类型',    'Char(1),0 - 身份证，
                                                                          1 - 护照，
                                                                          2 - 军官证，
                                                                          3 - 士兵证，
                                                                          4 - 港澳通行证，
                                                                          5 - 临时身份证，
                                                                          6 - 户口簿，
                                                                          7 - 其他，
                                                                          9 - 警官证，
                                                                          12 - 外国人居留证
                                                                          '),
                'idno'        => array('idno', '证件号码', 'Char(30)'),
                'mobilephone' => array('mobilephone', '手机', 'Char(20)'),
                'userno'      => array('userno', '用户号', 'Char(40),客户在合作商户的账户'),
                'validcode'   => array('validcode', '验证码', 'Char(10),20260交易获取的动态验证码'),
            )
        ),
        20110 => array(
            'name'     => '2.3	支付协议解除交易',
            'params'   => array(
                'cardno'   => array('cardno', '卡号', 'Char(10),银行卡号'),
                'username' => array('username', '持卡人户名', '[可选],Char(100),选输，如有值则核对'),
                'idtype'   => array('idtype', '证件类型',    '[可选],Char(1),0 - 身份证，
                                                                                 1 - 护照，
                                                                                 2 - 军官证，
                                                                                 3 - 士兵证，
                                                                                 4 - 港澳通行证，
                                                                                 5 - 临时身份证，
                                                                                 6 - 户口簿，
                                                                                 7 - 其他，
                                                                                 9 - 警官证，
                                                                                 12 - 外国人居留证,
                                                                                 选输，如有值则核对'),
                'idno'        => array('idno', '证件号码', '[可选],Char(30),选输，如有值则核对'),
                'mobilephone' => array('mobilephone', '手机', '[可选],Char(20),选输，如有值则核对'),
                'userno'      => array('userno', '用户号', 'Char(40),必须与签约交易相同'),
            )
        ),
        20260 => array(
            'name'     => '2.4	手机验证码获取交易',
            'params'   => array(
                'cardno'   => array('cardno', '卡号', 'Char(10),银行卡号'),
                'username' => array('username', '持卡人户名', 'char(100)'),
                'amount'   => array('amount', '金额', 'Char(16),单位:元，小数点2位；签约交易送0，消费交易与实际支付金额相同'),
                'idtype'   => array('idtype', '证件类型','Char(2),0 - 身份证，
                                                                      1 - 护照，
                                                                      2 - 军官证，
                                                                      3 - 士兵证，
                                                                      4 - 港澳通行证，
                                                                      5 - 临时身份证，
                                                                      6 - 户口簿，
                                                                      7 - 其他，
                                                                      9 - 警官证，
                                                                      12 - 外国人居留证
                                                                      '),
                'idno'        => array('idno', '证件号码', 'Char(30)'),
                'mobilephone' => array('mobilephone', '手机号码', 'Char(20)'),
                'orderno'     => array('orderno', '订单号', 'Char(30),要求订单号唯一，建议前面加8位日期；同消费或签约交易的流水。'),
                'userno'      => array('userno', '用户号', 'Char(40),同签约、消费交易'),
            )
        ),
        20270 => array(
            'name'     => '2.5	银行卡消费交易',
            'params'   => array(
                'cardno'   => array('cardno', '卡号', 'Char(10),银行卡号'),
                'amount'   => array('amount', '金额', 'Char(16)'),
                'username' => array('username', '帐户户名', 'Char(100)'),
                'idtype'   => array('idtype', '证件类型',    'Char(1),0 - 身份证，
                                                                          1 - 护照，
                                                                          2 - 军官证，
                                                                          3 - 士兵证，
                                                                          4 - 港澳通行证，
                                                                          5 - 临时身份证，
                                                                          6 - 户口簿，
                                                                          7 - 其他，
                                                                          9 - 警官证，
                                                                          12 - 外国人居留证
                                                                          '),
                'idno'       => array('idno', '证件号码', 'Char(30)'),
                'trxnote'    => array('trxnote', '交易描述', 'Char(20),体现在客户卡明细中'),
                'userno'     => array('userno', '用户号', 'Char(40),同签约交易'),
                'validcode'  => array('validcode', '验证码', 'Char(10),20260交易获取的动态验证码'),
                'goodsname'  => array('goodsname', '商品名称', 'Char(200)'),
                'goodsid'    => array('goodsid', '商品代码', 'Char(60)'),
                'sellertype' => array('sellertype', '商户类型', 'Char(10),1、虚拟高风险类（无物流、非实名登记、易变现如：游戏点卡、游戏装备、手机充值、礼品卡、虚拟账户充值）
										<br/>2、虚拟低风险类（无物流、非实名登记、不易变现如：电影票、信息咨询）
										<br/>3、虚拟实名类（无物流、实名登记、不易变现如：航空售票、酒店预订、旅游产品、学费、行政费用（税费、车船使用费）、汽车、房产）
										<br/>4、实物高风险类（有物流、易变现如：数码家电、黄金、珠宝首饰等）
										<br/>5、实物低风险类（有物流、不易变现如：服饰、食品、日用品等）
										<br/>6、金融类产品（同花顺、基金等）
										<br/>7、支付账户/钱包充值类（仅限借记卡）'),
                'sellername'    => array('sellername', '一级商户名称', 'Char(200)'),
                'sellerid'      => array('sellerid', '一级商户代码', 'Char(20)'),
                'subsellername' => array('subsellername', '二级商户名称', '[可选],Char(200),如无则送空'),
                'subsellerid'   => array('subsellerid', '二级商户代码', '[可选],Char(20),如无则送空'),
            )
        ),
        20271 => array(
            'name'     => '2.7	银行卡消费撤销交易',
            'params'   => array(
                'cardno'   => array('cardno', '部分卡号', 'Char(10),银行卡号，卡号前6位+末4位'),
                'userno'   => array('userno', '用户号', 'Char(40)'),
                'amount' => array('amount', '金额', 'Char(16)'),
                'username' => array('username', '帐户户名', 'Char(100)'),
                'idtype'   => array('idtype', '证件类型',    'Char(1),0 - 身份证，
                                                                              1 - 护照，
                                                                              2 - 军官证，
                                                                              3 - 士兵证，
                                                                              4 - 港澳通行证，
                                                                              5 - 临时身份证，
                                                                              6 - 户口簿，
                                                                              7 - 其他，
                                                                              9 - 警官证，
                                                                              12 - 外国人居留证
                                                                              '),
                'idno'       => array('idno', '证件号码', 'Char(30)'),
                'cmptxosno'  => array('cmptxosno', '原交易流水号', '不超过60位'),
            )
        ),
        20240 => array(
            'name'     => '2.6	信用卡退货交易',
            'params'   => array(
                'cardno'   => array('cardno', '卡号', 'Char(10),银行卡号'),
                'amount'   => array('amount', '金额', 'Char(16),金额必须小于等于原消费交易金额'),
                'username' => array('username', '帐户户名', '[可选],Char(100),选输，如有值则核对'),
                'idtype'   => array('idtype', '证件类型',    '[可选],Char(1),0 - 身份证，
                                                                                 1 - 护照，
                                                                                 2 - 军官证，
                                                                                 3 - 士兵证，
                                                                                 4 - 港澳通行证，
                                                                                 5 - 临时身份证，
                                                                                 6 - 户口簿，
                                                                                 7 - 其他，
                                                                                 9 - 警官证，
                                                                                 12 - 外国人居留证,
                                                                                 选输，如有值则核对'),
                'idno'      => array('idno', '证件号码', '[可选],Char(30),选输，如有值则核对'),
                'cmptxosno' => array('cmptxosno', '原交易流水号', '不超过60位,公司方的原交易流水号'),
                'otxdate'   => array('otxdate', '原交易日期', 'Char(8),yyyymmdd'),
                'trxnote'   => array('trxnote', '退货附加信息', 'Char(20),可上送退货交易说明等'),
            )
        ),
        20250 => array(
            'name'     => '2.7	查询交易结果',
            'params'   => array(
                'cmptxosno' => array('cmptxosno', '公司方交易流水号', 'Char(60),待查询交易的流水号
                                                                              格式：日期（yyyymmdd，8位）＋公司方流水号'),
                'cardno'    => array('cardno', '卡号', 'Char(10),完整银行卡号'),
            )
        ),

    );

    /**
     * @var array 参数默认值
     */
    static $DEFAULT_PARAMS = array(
        'cardno' => '6222081410000552460',
        'username' => '邱宪喂',
        'idtype' => '0',
        'idno' => '36120819811227178X',
        'mobilephone' => '18906088104',
        'userno' => 'Anacleto',
        'validcode' => '000000',
    );

    /** 公共参数列表 */
    static $PUBLIC_PARAMS = array(
        'txcode'  => array('txcode','交易码','Char(5)'),
        'cityno'  => array('cityno','城市号','Char(4),交易发生地信息，一般为商户所在地，合作商户默认上送1202；'),
        'cmpdate' => array('cmpdate','公司交易日期','Char(8),YYYYMMDD'),
        'cmptime' => array('cmptime','公司交易时间','char(6),hhmmss'),
        'cmptxsno'=> array('cmptxsno','公司方流水号','Char(20)不超过20位.格式：公司方流水号（总长不超过20位）..
                                                            说明：公司方必须保证流水号的唯一性。建议在流水前增加日期以确保唯一。'),
        'paytype' => array('paytype','快捷支付类型','Char(1),1 – 借记卡，2 – 贷记卡'),
        'sellerid' => array('sellerid', '商户号', 'Char(20)'),
    );

    /** 通用返回码 */
    static $RET_CODES = array(
        'B0001' =>	'通讯报文错',
        'B0002' =>	'系统错',
        'B0033' =>	'原交易已成功',
        'B0241' =>	'原交易不正确',
        'B0245' =>	'金额不符',
        'B0249' =>	'相同公司流水号交易正在处理（重复支付流水）',
        'B2022' =>	'尚未办理委托',
        'B2024' =>	'扣款帐号与委托账号不一致',
        'B2027' =>	'原交易不存在',
        'B2040' =>	'该用户委托已销户',
        'B2043' =>	'核对户名不相符',
        'B2301' =>	'日累计消费超限额',
        'B2024' =>	'扣款帐号与委托账号不一致',
        'B2028' =>	'已办理委托',
        'B9998'	=>  '（特殊错误，具体错误内容不定）',
        'B9997' =>	'疑账',
        '99999' =>	'未知',
        '2037'  =>	'证件号码输入错误',
        '2252'	=>	'客户的证件类型不符',
        '4102'	=>	'借方帐户余额不足',
        '4101'	=>	'帐户余额不足',
        '5919'	=>	'卡片信用额度超限',
        'B0110' =>  '通讯包文解析错，取信息要素失败',
        'B2303' =>  '手机号码不符',
        'B2043' =>  '核对户名不相符',
        'B2323' =>  '证件类型不符',
        'B2031' =>  '证件号不正确',
        'B2658' =>  '该用户已存在不同卡号的协议',
        'B2653' =>  '已存在不同支付卡号的同一订单号',
        'B2654' =>  '已存在不同用户号的同一订单号',
        'B2655' =>  '该订单号已撤销',
        'B2656' =>  '该订单号已退货',
        'B2657' =>  '该订单号已存在',
        'B2636' =>  '超单笔交易限额',
        'B2616' =>  '日累计支付金额超限',
        'B2617' =>  '月累计支付金额超限',
        'B2618' =>  '年累计支付金额超限',
        'B2610' =>  '手机号核对次数超过上限'

    );

    /**
     * 支付协议签约交易 20100
     * @param string $cpmtxsno 公司方流水号
     * @param string $sellerId 商户代码
     * @param string $cardno 银行卡号
     * @param string $username 账户户名
     * @param string $idtype 证件类型
     * @param string $idno 证件号
     * @param string $mobileNbr 手机号码
     * @param string $userno 用户号
     * @param string $valCode 验证码
     * @return array 结果
     */
    public function paymentAgreement($cpmtxsno, $sellerId, $cardno, $username, $idtype, $idno, $mobileNbr, $userno, $valCode) {
        $reqData = array(
            // 公共
            'txcode' => \Consts::TXCODE_SIGN_CONTRACT, // 交易码：支付协议签约交易
            'cmptxsno' => $cpmtxsno, // 公司方流水号(总长不超过20位)，保证流水号的唯一性
            'sellerid' => $sellerId,//工行提供的一级商户代码（12位）
            // 私有
            'cardno' => $cardno, // 银行卡号（完整卡号或者卡号前6位，后4位拼接而成）
            'username' => $username, // 账户户名
            'idtype' => $idtype, // 证件类型
            'idno' => $idno, // 证件号
            'mobilephone' => $mobileNbr, // 手机号码
            'userno' => $userno, // 用户号
            'validcode' => $valCode, // 验证码
        );
        // 调用银行卡消费撤销交易API
        $ret = $this->processPay($reqData);
        return get_object_vars($ret->pub);
    }

    /**
     * 信用卡退货交易 20240
     * @param string $cpmtxsno 公司方流水号
     * @param string $sellerid 商户代码
     * @param string $cardno 银行卡号
     * @param string $userno 用户号
     * @param float $amount 金额，单位：元
     * @param string $username 账户户名
     * @param string $idtype 证件类型
     * @param string $idno 证件号
     * @param string $cmptxosno 原交易流水号
     * @param string $otxdate 原交易日期:yyyymmdd
     * @param string $trxnote 退货附加信息,可上送退货交易说明等
     * @return array 结果
     */
    public function creditCardReturnGoods($cpmtxsno, $sellerid, $cardno, $userno, $amount, $username, $idtype, $idno, $cmptxosno, $otxdate, $trxnote) {
        $reqData = array(
            // 公共
            'txcode' => \Consts::TXCODE_CREDIT_CARD_RETURN_GOOD, // 交易码：信用卡退货交易
            'cmptxsno' => $cpmtxsno, // 公司方流水号
            'sellerid' => $sellerid,//工行提供的一级商户代码
            // 私有
            'cardno' => $cardno, // 银行卡号（完整卡号或者卡号前6位，后4位拼接而成）
            'userno' => $userno, // 用户号
            'amount' => $amount, // 金额，单位：元
            'username' => $username, // 账户户名
            'idtype' => $idtype, // 证件类型
            'idno' => $idno, // 证件号
            'cmptxosno' => $cmptxosno, // 原交易流水号
            'otxdate'   => $otxdate, // 原交易日期:yyyymmdd
            'trxnote'   => $trxnote, // 退货附加信息,可上送退货交易说明等
        );
        // 调用银行卡消费撤销交易API
        $ret = $this->processPay($reqData);
        return get_object_vars($ret->pub);
    }

    /**
     * 手机验证码获取交易 20260
     * @param string $cpmtxsNo 公司方流水号
     * @param string $sellerId 商户代码
     * @param string $cardNo 银行卡号
     * @param string $username 账户户名
     * @param float $amount 金额，单位：元
     * @param string $idType 证件类型
     * @param string $idNo 证件号
     * @param string $mobileNbr 手机号
     * @param string $orderNbr 订单号
     * @param string $userNo 用户号
     * @return array 结果
     */
    public function getMsgValCode($cpmtxsNo, $sellerId, $cardNo, $username, $amount, $idType, $idNo, $mobileNbr, $orderNbr, $userNo) {
        $reqData = array(
            // 公共
            'txcode' => \Consts::TXCODE_VAL_CODE, // 交易码：手机验证码获取交易
            'cmptxsno' => $cpmtxsNo, // 公司方流水号
            'sellerid' => $sellerId, //一级商户代码
            // 私有
            'cardno' => $cardNo, // 银行卡号（完整卡号或者卡号前6位，后4位拼接而成）
            'username' => $username, // 账户户名
            'amount' => $amount, // 金额，单位：元
            'idtype' => $idType, // 证件类型
            'idno' => $idNo, // 证件号
            'mobilephone' => $mobileNbr, // 手机号码
            'orderno' => $orderNbr, // 订单号
            'userno' => $userNo, // 用户号
        );
        // 调用银行卡消费撤销交易API
        $ret = $this->processPay($reqData);
        return get_object_vars($ret->pub);
    }

    /**
     * 银行卡消费交易 20270
     * @param string $orderNbr 公司方流水号，订单编码
     * @param string $sellerid 商户代码
     * @param string $cardno 银行卡号
     * @param float $amount 金额，单位：元
     * @param string $username 账户户名
     * @param string $idType 证件类型
     * @param string $idno 证件号码
     * @param string $trxnote 交易描述
     * @param string $userno 用户号
     * @param string $valCode 验证码
     * @param string $goodsname 商品名称
     * @param string $goodsid 商品代码
     * @param string $sellertype 商品类型
     * @param string $sellerName 一级商户名字
     * @param string $subsellerName 二级商户名称
     * @param string $subsellerId 二级商户代码
     * @return array 结果 {'retcode' => ''}
     */
    public function consumptionTransaction($orderNbr, $sellerid, $cardno, $amount, $username, $idType, $idno, $trxnote = '', $userno, $valCode, $goodsname = '', $goodsid = '', $sellertype = '', $sellerName, $subsellerName = '', $subsellerId = '') {
        $reqData = array(
            // 公共
            'txcode' => \Consts::TXCODE_BANKCARD_CONSUME, // 交易码：银行卡消费交易
            'cmptxsno' => $orderNbr, // 公司方流水号
            'sellerid' => $sellerid,//一级商户代码
            // 私有
            'cardno' => $cardno, // 银行卡号（完整卡号或者卡号前6位，后4位拼接而成）
            'amount' => $amount, // 金额，单位元
            'username' => $username, // 账户户名
            'idtype' => $idType, // 证件类型
            'idno' => $idno, // 证件号码
            'trxnote' => $trxnote, //交易描述
            'userno' => $userno, // 用户号
            'validcode' => $valCode, // 验证码
            'goodsname' => $goodsname, // 商品名称
            'goodsid' => $goodsid, // 商品代码
            'sellertype' => $sellertype, // 商品类型
            'sellername' => $sellerName, // 一级商户名字
            'subsellername' => $subsellerName, // 二级商户名称
            'subsellerid' => $subsellerId, // 二级商户代码
        );
        // 调用银行卡消费撤销交易API
        $ret = $this->processPay($reqData);
        return get_object_vars($ret->pub);
    }

    /**
     * 银行卡消费撤销交易20271
     * @param string $cpmtxsno 公司方流水号
     * @param string $sellerid 商户代码
     * @param string $cardno 银行卡号
     * @param string $userno 用户号
     * @param float $amount 金额，单位：元
     * @param string $username 账户户名
     * @param string $idtype 证件类型
     * @param string $idno 证件号
     * @param string $cmptxosno 原交易流水号
     * @return array 结果 {'retcode' => ''}
     */
    public function cancelPay($cpmtxsno, $sellerid, $cardno, $userno, $amount, $username, $idtype, $idno, $cmptxosno) {
        $reqData = array(
            // 公共
            'txcode' => \Consts::TXCODE_CONSUME_REVOCATION, // 交易码：银行卡消费撤销交易
            'cmptxsno' => $cpmtxsno, // 公司方流水号
            'sellerid' => $sellerid,//工行提供的一级商户代码
            // 私有
            'cardno' => $cardno, // 银行卡号（完整卡号或者卡号前6位，后4位拼接而成）
            'userno' => $userno, // 用户号
            'amount' => $amount, // 金额，单位：元
            'username' => $username, // 账户户名
            'idtype' => $idtype, // 证件类型
            'idno' => $idno, // 证件号
            'cmptxosno' => $cmptxosno, // 原交易流水号
        );
        // 调用银行卡消费撤销交易API
        $ret = $this->processPay($reqData);
        return get_object_vars($ret->pub);
    }

    /**
     * 处理参数，只接受post
     */
    public function processPay($reqData) {
        // 公共部分的参数
        $reqData['G_TRANSCODE'] = 'pub'; // 前置系统G_TRANSCODE
        $reqData['cityno'] = '1202'; // 城市号，交易发生地信息，一般为商户所在地，合作商户默认上送1202；
        $reqData['cmpdate'] = date('Ymd', time()); // 公司交易日期，YYYYMMDD
        $reqData['cmptime'] = date('His', time()); // 公司交易时间，hhmmss
        $reqData['paytype'] = '1'; // 快捷支付类型。1 – 借记卡，2 – 贷记卡

        \Think\Log::write('-------------------icbc post start-------------------', 'DEBUG');
        \Think\Log::write('reqData：' . json_encode($reqData), 'DEBUG');
        if(! in_array($_SERVER['HTTP_HOST'], array('api.huiquan.suanzi.cn', 'web.huiquan.suanzi.cn'))) {
            // 开发和测试环境均不向银行请求，直接返回结果码
            $retmsg = '';
            $retcode = '00000';

            // 模拟核对验证码
            if(isset($reqData['validcode']) && !empty($reqData['validcode'])) {
                // 设置一个默认的正确验证码
                $valCode = '111111';
                if($reqData['validcode'] == $valCode) {
                    $retcode = '00000';
                    $retmsg = '交易成功';
                } else {
                    $retcode = 'B2621'; // 验证码核对不通过
                }
            }
            $ret = '<package><pub><retcode>' . $retcode . '</retcode><retmsg>'.$retmsg.'</retmsg></pub></package>';
        } else {
            $sliceTransCode = '';
            $priReqData = new SimpleXMLElement(self::REQ_PACK_HEADER . '<req />');
            $pubReqData = new SimpleXMLElement(self::REQ_PACK_HEADER . '<pub />');

            $apiId = $reqData['txcode'];
            foreach ($reqData as $param => $val) {
                if ($param == 'G_TRANSCODE') {
                    $sliceTransCode = $val;
                } elseif (array_key_exists($param, self::$PUBLIC_PARAMS)) {
                    $pubReqData->addChild($param, $val);
                } elseif (array_key_exists($param, self::$API_METHODS[$apiId]['params'])) {
                    $priReqData->addChild($param, $val);
                }
            }
            if(in_array($_SERVER['HTTP_HOST'], array('api.huiquan.suanzi.cn', 'web.huiquan.suanzi.cn', 'huiquan.suanzi.cn'))) {
                $url = 'https://211.155.225.106:8443/icbcpay/FrontServer'; // 生产环境地址
            } else {
                $url = 'http://211.155.225.102/icbcpay/FrontServer'; // 测试环境地址
            }
            $params = array(
                'G_TRANSCODE' => $sliceTransCode,
                'pub' => $this->removeXmlRoot($pubReqData->asXml()),
                'req' => $this->removeXmlRoot($priReqData->asXml()),
            );
            $ret = $this->post($url, $params);
        }
        \Think\Log::write('icbc post ret:' . $ret, 'DEBUG');
        \Think\Log::write('--------------------icbc post end--------------------', 'DEBUG');
        return simplexml_load_string($ret);
    }

    /**
     * 去处xml头部及换行符 <?xml version="1.0"?>
     * @param string $xml
     * @return string
     */
    protected function removeXmlRoot($xml) {
        return trim(substr($xml, strlen(self::REQ_PACK_HEADER)));
    }

    /**
     * 发送HTTP POST请求
     * @param string $url 目标URL
     * @param array(k=>v) $params 请求参数
     * @return string
     */
    protected function post($url, $params) {
        $data = http_build_query($params);
        $opts = array (
            'http' => array (
                'method' => 'POST',
                'header'=> "Content-type: application/x-www-form-urlencoded\r\n" .
                    "Content-Length: " . strlen($data) . "\r\n",
                'content' => $data
            )
        );
        $context = stream_context_create($opts);
        $html = file_get_contents($url, false, $context);
        return $html;
    }

    /**
     * 查询交易 20250
     * @param string $sellerId 商户号
     * @param string $cmptxosno 原交易流水号
     * @param string $cardno 银行卡号
     * @return array
     */
    public function getTransactionRet($sellerId, $cmptxosno, $cardno) {
        $cpmtxsnoLogMdl = new CmptxsnoLogModel();
        $cpmtxsno = $cpmtxsnoLogMdl->getCmptxsno(); // 获得公司方流水号
        $reqData = array(
            // 公共
            'txcode' => \Consts::TXCODE_QUERY_RET, // 交易码：查询交易
            'cmptxsno' => $cpmtxsno, // 公司方流水号(总长不超过20位)，保证流水号的唯一性
            'sellerid' => $sellerId, // 商户号
            // 私有
            'cmptxosno' => $cmptxosno, // 待查询交易的流水号
            'cardno' => $cardno, // 银行卡号（完整卡号或者卡号前6位，后4位拼接而成）

        );
        $ret = $this->processPay($reqData);
        $ansArr = get_object_vars($ret->ans);
        return $ansArr;
    }
}