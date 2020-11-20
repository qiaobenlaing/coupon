<?php
/**
 * Wechat相关Model.
 * User: Weiping
 * Date: 2014-12-19
 * Time: 10:49
 */
namespace Common\Model;

//use Com\Wechat\Wechat;
//use Com\Wechat\WechatAuth;
//use Com\Wechat\Button\WechatButton;
//use Com\Wechat\Button\WechatTopButton;
//use Com\Wechat\Button\WechatClickButton;
//use Com\Wechat\Button\WechatViewButton;
use Org\FirePHPCore\FP;

class WechatModel {
	/** 缓存微信access token的键 */
	const CACHE_KEY_WECHAT_ACCESS_TOKEN = 'WECHAT_ACCESS_TOKEN';
	/** 缓存微信access token的时间，微信默认为2小时，7200s，我们提前失效，7000s */
	const CACHE_SECONDS_WECHAT_ACCESS_TOKEN = 7000;
	/** @var string 二维码URL前缀 */
	const QRCODE_IMG_URL_PREFIX = 'https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=';
	/** 300个文件夹存储二维码*/
	const DIRNUM = 300;
	/** 二维码存入本地的路径前缀*/
	const QRIMG_PREFIX = './qrimg/';
	/** 二维码存入本地的图片格式*/
	const QRIMG_TYPE = '.jpg';

	protected $appId;
	protected $secret;
	protected $appToken;
	protected $appUrlPrefix;

	/**
	 * 构造函数，初始化APP参数。
	 */
	public function __construct() {
		$params = C('WECHAT');
		$this->appId = $params['APPID'];
		$this->secret = $params['SECRET'];
		$this->appToken = $params['TOKEN'];
		$this->appUrlPrefix = $params['APP_URL_PREFIX'];
	}

	/**
	 * 监听请求
	 */
	public function request() {
		$wechat = new Wechat($this->appToken);
		return $wechat->request();
	}

	/**
	 * WEB Api 通过code换取网页授权access_token，取得其中openid。
	 * @param $code
	 */
	public function oauthOpenId($code) {
		$wechatAuth = new WechatAuth($this->appId, $this->secret, $this->getAccessToken());
		$accessToken = $wechatAuth->getAccessToken('code', $code);
		return $accessToken['openid'];
	}

	/**
	 * 创建只有2个菜单项的微信菜单
	 * @return array
	 */
	public function createSimpleMenu() {
		// https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirec
		$callbackUrlAgent = urlencode($this->appUrlPrefix . 'Wechat/oauthAgent');
		$callbackUrlStaff = urlencode($this->appUrlPrefix . 'Wechat/oauthStaff');
		$menuZkt = new WechatViewButton('智客通', "https://open.weixin.qq.com/connect/oauth2/authorize?appid={$this->appId}&redirect_uri={$callbackUrlAgent}&response_type=code&scope=snsapi_base&state=WECHATMENU#wechat_redirec");
		$menuStaff = new WechatViewButton('内部人员', "https://open.weixin.qq.com/connect/oauth2/authorize?appid={$this->appId}&redirect_uri={$callbackUrlStaff}&response_type=code&scope=snsapi_base&state=WECHATMENU#wechat_redirec");

		// 创建菜单
		$wechatAuth = new WechatAuth($this->appId, $this->secret, $this->getAccessToken());
		$ret = $wechatAuth->menuCreate(array(
			$menuZkt->getData(),
			$menuStaff->getData()
		));
		return $ret;
	}

	/**
	 * 创建菜单
	 * @param $menu
	 * @return array
	 */
	public function createMenu() {
		$subMenuBuildingList = new WechatViewButton('楼盘列表', $this->appUrlPrefix . 'Building/listBuilding');
		$subMenuRcmmClient = new WechatViewButton('推荐客户', $this->appUrlPrefix . 'Client/rcmm');
		$subMenuAskForHelpv = new WechatViewButton('请求帮助', $this->appUrlPrefix . 'Support/request');
		$subMenuBuildingActivities = new WechatViewButton('楼盘活动', $this->appUrlPrefix . 'Building/activity');
		$menuBuilding = new WechatTopButton('我的楼盘', array(
				$subMenuBuildingList->getData(),
				$subMenuRcmmClient->getData(),
				$subMenuAskForHelpv->getData(),
				$subMenuBuildingActivities->getData(),
		));
		$subMenuMyHome = new WechatViewButton('我的主页', $this->appUrlPrefix . 'Agent/homepage');
		$subMenuMyClients = new WechatViewButton('已推荐客户', $this->appUrlPrefix . 'Client/rcmmList');
		$subMenuMyMoney = new WechatViewButton('我的佣金', $this->appUrlPrefix . 'Agent/Bonus/count');
		$subMenuMyRank = new WechatViewButton('我的排名', $this->appUrlPrefix . 'Agent/Agent/rank');
		$menuMe = new WechatTopButton('我的', array(
				$subMenuMyHome->getData(),
				$subMenuMyClients->getData(),
				$subMenuMyMoney->getData(),
				$subMenuMyRank->getData(),
		));
		$subMenuMoreAct = new WechatViewButton('福利', $this->appUrlPrefix . 'Benefit/listBenefit');
		$subMenuLeaveMsg = new WechatViewButton('留言·申述', $this->appUrlPrefix . 'Message/create');
		$subMenuEmployeeLogin = new WechatViewButton('内部人员入口', $this->appUrlPrefix . 'Staff/login');
		$subMenuHelp = new WechatViewButton('关于·帮助', $this->appUrlPrefix . 'Help/listHelp');
		$menuMore = new WechatTopButton('更多', array(
				$subMenuMoreAct->getData(),
				$subMenuLeaveMsg->getData(),
				$subMenuEmployeeLogin->getData(),
				$subMenuHelp->getData(),
		));
		// 创建菜单
		$wechatAuth = new WechatAuth($this->appId, $this->secret, $this->getAccessToken());
		$ret = $wechatAuth->menuCreate(array(
				$menuBuilding->getData(),
				$menuMe->getData(),
				$menuMore->getData()
		));
		return $ret;
	}

	/**
	 * 获取本应用的access token，例："rkFo98qHV7_OnJHUTQqueTWXvXQkz-YEpBDz0tLeNjvRj2ubntAlGfOtf-0q5nXZFZAdAt1rApFy_iTugQCRy5b9xxRUS9KJ5BNQX1ZE15Q" 。
	 * @return string 字符串access token，没有成功获取到则返回null。
	 */
	protected function getAccessToken() {
		S(self::CACHE_KEY_WECHAT_ACCESS_TOKEN, null);
		$accessToken = S(self::CACHE_KEY_WECHAT_ACCESS_TOKEN);
		if (!$accessToken) {
			$wechatAuth = new WechatAuth($this->appId, $this->secret);
			// ret example: array(2) { ["access_token"]=> string(107) "rkFo98qHV7_OnJHUTQqueTWXvXQkz-YEpBDz0tLeNjvRj2ubntAlGfOtf-0q5nXZFZAdAt1rApFy_iTugQCRy5b9xxRUS9KJ5BNQX1ZE15Q" ["expires_in"]=> int(7200) }
			$accessToken = $wechatAuth->getAccessToken();
			if (isset($accessToken['access_token'])) {
				S(self::CACHE_KEY_WECHAT_ACCESS_TOKEN, $accessToken['access_token'], self::CACHE_SECONDS_WECHAT_ACCESS_TOKEN);
				return $accessToken['access_token'];
			} else {
				return null;
			}
		} else {
			return $accessToken;
		}
	}

	/**
	 * 获取给定自由经纪人的二维码图片URL。目前全部使用永久二维码图片。
	 * 如果数据库中已存在，直接返回，否则调用API生成一张，并保存到数据库中。
	 * @param number $agentId 自由经纪人ID
	 * @return string 返回二维码图片的全URL。例：https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQEt8ToAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xLzhrakJ1TnJtT18xQ2ZiNURabWJYAAIEhOSTVAMEPAAAAA==
	 */
	public function getQRCodeImgUrl($agentId) {
		// 首先从数据库查询，如果已经失效，重新获取临时二维码
		$qrcodeMdl = M('wechatqrcode');
		$existentQrcodeRecord = $qrcodeMdl->where(array('agentId' => $agentId))->find();
		if ($existentQrcodeRecord && $existentQrcodeRecord['ticket']) {
			//return self::QRCODE_IMG_URL_PREFIX . $existentQrcodeRecord['ticket'];
			return $existentQrcodeRecord['url'];
		}

		// 请求API，重新获取二维码图片。
		$data = array(
			'agentId' => $agentId,
		);
		// 如果已经存在，直接使用以前的secneId，否则通过数据库自增一条记录的主键作为sceneId。
		$sceneId = ($existentQrcodeRecord && $existentQrcodeRecord['sceneId'])
					? $existentQrcodeRecord['sceneId'] : ($qrcodeMdl->data($data)->add());
		$data['sceneId'] = $sceneId;
		// 调用API
		$wechatAuth = new WechatAuth($this->appId, $this->secret, $this->getAccessToken());
		$qrcodeResp = $wechatAuth->qrcodeCreate($sceneId, 0); //
		$data['ticket'] = $qrcodeResp['ticket'];
		$data['expires'] = time() + $qrcodeResp['expire_seconds'];
		
		$qrimg = file_get_contents(self::QRCODE_IMG_URL_PREFIX . $qrcodeResp['ticket']);
		$dir = $data['sceneId'] % self::DIRNUM;
		$pathname = self::QRIMG_PREFIX.$dir;
		if (!file_exists($pathname)) {
			mkdir( $pathname , 0777 , true );
		}
		$filename = $pathname.'/'.$data['sceneId'].self::QRIMG_TYPE;
		file_put_contents($filename, $qrimg);
		$data['url'] = substr($filename, 1);
		
		//$data['url'] = $qrcodeResp['url'];
		$qrcodeMdl->data($data)->save();
		//return self::QRCODE_IMG_URL_PREFIX . $qrcodeResp['ticket'];
		return $existentQrcodeRecord['url'];
	}

	public function sendWelcomeMsg() {

	}


	/**
	 * 处理关注事件
	 * @param $req
	 * <xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>
	<FromUserName><![CDATA[FromUser]]></FromUserName>
	<CreateTime>123456789</CreateTime>
	<MsgType><![CDATA[event]]></MsgType>
	<Event><![CDATA[subscribe]]></Event>
	</xml>
	 *
	 * ToUserName	开发者微信号
	FromUserName	发送方帐号（一个OpenID）
	CreateTime	消息创建时间 （整型）
	MsgType	消息类型，event
	Event	事件类型，subscribe(订阅)、unsubscribe(取消订阅)
	 */
	public function handleSubscribe($req) {
		/* 加载微信SDK */
		$wechat = new Wechat($this->appToken);

		// 如果用户没注册，注册。
		// 获取用户基本信息并保存至DB。参考：http://mp.weixin.qq.com/wiki/14/bb5031008f1494a59c6f71fa0f319c66.html
		$agent = $this->register($req);
		session(\Consts::SESS_USER, $agent);

		// 发送消息
		$title = '欢迎使用智客通';
		$description = '欢迎使用智客通';
		$url = $this->appUrlPrefix . 'Wechat/welcome';
		$picurl = $this->appUrlPrefix . '../img/wechcat-welcome.png';
		$wechat->replyNewsOnce($title, $description, $url, $picurl); //回复单条图文消息
	}

	/**
	 * 处理消息事件
	 * @param $req
	 * <xml>
	 <ToUserName><![CDATA[toUser]]></ToUserName>
	 <FromUserName><![CDATA[FromUser]]></FromUserName>
	 <CreateTime>123456789</CreateTime>
	 <MsgType><![CDATA[event]]></MsgType>
	 <Event><![CDATA[subscribe]]></Event>
	 </xml>
	 *
	 * ToUserName	开发者微信号
	 FromUserName	发送方帐号（一个OpenID）
	 CreateTime	消息创建时间 （整型）
	 MsgType	消息类型，event
	 Event	事件类型，subscribe(订阅)、unsubscribe(取消订阅)
	 */
	public function handleText($req) {
		/* 加载微信SDK */
		$wechat = new Wechat($this->appToken);
		$agentMdl = new AgentModel();
		$openId = $req['FromUserName'];
		$agentInfo = $agentMdl->getUserByWechat($openId);
		$messageMdl = new MessageModel();
		// 如果用户没注册，注册。
		// 获取用户基本信息并保存至DB。参考：http://mp.weixin.qq.com/wiki/14/bb5031008f1494a59c6f71fa0f319c66.html
		$agent = $this->register($req);
		session(\Consts::SESS_USER, $agent);

		//得到发送消息的wechatId和内容
		$userId = $agentInfo['userId'];
		$content = $req['Content'];
		$msgType = C('MSGTYPE_MESSAGE');
		$time = date('Y-m-d H:i:s');
		$source = C('MSGSOURCE_WX');
		$data = array(
				'userId' => $userId,
				'msgText' => $content,
				'msgType' => $msgType,
				'createTime' => $time,
				'source' => $source
		);
		$res = $messageMdl->addMessage($data);
		\Think\Log::record('保存用户信息至DB返回：' . json_encode($res), 'DEBUG');
		// 发送消息
		$text = '您的留言已收到，我们将尽快跟您联系。';
		$wechat->replyText($text); //回复单条消息
	}
	
	/**
	 * 处理扫描关注事件
	 * @param $req
	 * <xml><ToUserName><![CDATA[toUser]]></ToUserName>
	<FromUserName><![CDATA[FromUser]]></FromUserName>
	<CreateTime>123456789</CreateTime>
	<MsgType><![CDATA[event]]></MsgType>
	<Event><![CDATA[subscribe]]></Event>
	<EventKey><![CDATA[qrscene_123123]]></EventKey>
	<Ticket><![CDATA[TICKET]]></Ticket>
	</xml>
	 *
	 * ToUserName	开发者微信号
	FromUserName	发送方帐号（一个OpenID）
	CreateTime	消息创建时间 （整型）
	MsgType	消息类型，event
	Event	事件类型，subscribe
	EventKey	事件KEY值，qrscene_为前缀，后面为二维码的参数值
	Ticket	二维码的ticket，可用来换取二维码图片
	 */
	public function handleScan($req) {

	}

	/**
	 * 处理菜单点击事件
	 * @param $req
	 * 点击菜单跳转链接时的事件推送

	推送XML数据包示例：

	<xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>
	<FromUserName><![CDATA[FromUser]]></FromUserName>
	<CreateTime>123456789</CreateTime>
	<MsgType><![CDATA[event]]></MsgType>
	<Event><![CDATA[VIEW]]></Event>
	<EventKey><![CDATA[www.qq.com]]></EventKey>
	</xml>
	 *
	 * ToUserName	开发者微信号
	FromUserName	发送方帐号（一个OpenID）
	CreateTime	消息创建时间 （整型）
	MsgType	消息类型，event
	Event	事件类型，VIEW
	EventKey	事件KEY值，设置的跳转URL
	 */
	public function handleMenuView($req) {
//		\Think\Log::record('handleMenuView()开始处session值：' . json_encode($_SESSION), 'DEBUG');
//		$openId = $req['FromUserName'];
//		$userMdl = null;
//		if (strpos($req['EventKey'], 'Staff/login') === false) { // 非“内部登录”按钮
//			$userMdl = new AgentModel();
//			$agent = $agentMdl->getUserByWechat($openId);
//			$agent['role'] = 'AGENT';
//			session(\Consts::SESS_USER, $agent);
//			$userMdl->autoLoginByWechatId($openId);
//		} else { // “内部登录”按钮
//			$userMdl = new StaffModel();
//			$staff = $staffMdl->getUserByWechat($openId);
//			session(\Consts::SESS_USER, $staff);
			// 员工登录，根据之前cookie之前的登录类型来登录。cokkie在用户手动登录时设定的。
//			if (cookie('?' . \Consts::COOKIE_ROLE)) {
//				$userMdl->autoLoginByWechatId($openId, cookie(\Consts::COOKIE_ROLE));
//			}
//		}
//		\Think\Log::record('handleMenuView()结尾处session值：' . json_encode($_SESSION), 'DEBUG');
	}
	/**
	 * 后台管理员回复经纪人留言
	 * @param 回复的内容 $data
	 */
	public function replyTextMsg($data) {
		$responseMdl = new ResponseModel();
		$agentMdl = new AgentModel();
		$data['userId'] = 51;
		$data['createTime'] = date('Y-m-d H:i:s');
		$res = $responseMdl->replyMessage($data);
// 		\Think\Log::record('保存用户信息至DB返回：' . json_encode($res), 'DEBUG');
		/* 加载微信SDK */
		$wechatAuth = new WechatAuth($this->appId, $this->secret, $this->getAccessToken());
		$agentInfo = $agentMdl->getAgentInfo($data['agentId']);
// 		\Think\Log::record('保存用户信息至DB返回：' . json_encode($agentInfo), 'DEBUG');
		$openid = $agentInfo['wechatId'];
// 		\Think\Log::record('保存用户信息至DB返回：' . json_encode($openid), 'DEBUG');
		$msgText = $data['msgText'];
		$wechatAuth->sendText($openid, $msgText); //回复单条消息
		
	}
	/**
	 * 关注注册。
	 * @param array $req 请求参数。例扫描带scene的二维码时返回：
	 *  {"ToUserName":"gh_63c4912ba0ae","FromUserName":"oE4xqs8E1n29Wszbd9WzfVK86IXE","CreateTime":"1419752055",
	 *        "MsgType":"event","Event":"subscribe","EventKey":"qrscene_2",
	 * 			"Ticket":"gQHz8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xLzJVanktZVhtR18xaUxwY1ZWV1RYAAIEJrKfVAMEAAAAAA=="}
	 *
	 * @return array 用户数据。
	 */
	private function register($req) {
		$agentMdl = new AgentModel();
		$openId = $req['FromUserName'];
		$agent = $agentMdl->getUserByWechat($openId);

		// TODO 如果是被推荐的，设置推荐时间
		// rcmmTime
		// rcmmByUserId

		if (!$agent) {
			$agent = array(
				'wechatId' => $openId,
				'registerTime' => date('Y-m-d H:i:s'),
				'lastLoginTime' => date('Y-m-d H:i:s'),
			);
			// 从EventKey中剥离推荐人的ID
			$rcmmUserId = substr($req['EventKey'], strlen('qrscene_'));
			if ($rcmmUserId > 0) {
				$agent['rcmmByUserId'] = $rcmmUserId;
				$agent['rcmmTime'] = $agent['registerTime'];
			}
			// 通过API获取更多用户信息
			$userInfo = $this->retrieveUserInfoApi($openId);
			if ($userInfo && !$userInfo['errcode']) {
				// TODO 保存更多用户数据
				$agent['wechatUserName'] = $userInfo['nickname'];
				$agent['sex'] = $userInfo['sex'] == 1 ? 'M' : ($userInfo['sex'] == 2 ? 'F' : '0');
				$agent['province'] = $userInfo['province'];
				$agent['city'] = $userInfo['city'];
				$agent['headImgUrl'] = $userInfo['headimgurl'];
			} else {
				\Think\Log::record('获取用户信息出错，返回：' . json_encode($userInfo), 'WARN');
			}
			// 保存至DB
			$res = $agentMdl->addAgent($agent);
			\Think\Log::record('保存用户信息至DB返回：' . json_encode($res), 'DEBUG');
		} else {
			// 取消过并再次关注，可以更新一下关注时间，以及城市信息等。
		}
		return $agent;
	}

	private function retrieveUserInfoApi($wechatOpenId) {
		$wechatAuth = new WechatAuth($this->appId, $this->secret, $this->getAccessToken());
		return $wechatAuth->userInfo($wechatOpenId);
	}
} 