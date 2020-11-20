<?php
    class Weixin{
		
		
		
        private $appID = "wxdacebf540333a98f";
       private $appsecret  = "d37c8606678f5faa16ce322b8ead4d03";

     /**   function getAccessToken(){
			
           $url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={$this->appID}&secret={$this->appsecret}";

           /**  return $this->httpGet($url);
            //json字符串
            $json = $this->httpGet($url);
            //解析json
            $obj = json_decode($json);
	
			
            return $obj->access_token;
			
		return $this->httpGet($url);
            //json字符串
            $json = $this->httpGet($url);
            //解析json
            $obj = json_decode($json);
	
			
            return $obj->access_token;
			
			
			
       }**/
		 function getWxAccessToken(){
			//将在access_token 存在session/cookie中
			if($_SESSION['access_token']&&$_SESSION['expire_time']>time()){
				return $_SESSION['access_token'];
			}else{
			//如果access_token 不存在或者已经过期，重新取access_token
			//$appID = "wx93901597a1aceff2";
			//$appsecret  = "ade332458b339e04b4c3c4636be9f66f";
			$url= "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={$this->appID}&secret={$this->appsecret}";
			$res=$this->http_curl($url,'get','json');
			$access_token=$res['access_token'];
			//将重新获取到的access_token存到session
			$_SESSION['access_token']=$access_token;
			$_SESSION['expire_time']=time()+7000;
			return $access_token;
			
		}
		 }
		
		
		
		

    /**  function httpGet($url){
            //1.初始化
            $curl = curl_init();
            //配置curl
            curl_setopt($curl, CURLOPT_URL, $url);
            curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
            //执行curl
            $res = curl_exec($curl);
            //关闭curl
            curl_close($curl);
            return $res;
        }**/
		
		function http_curl($url,$type='get',$res='json',$arr=''){
			//1.初始化curl
			$ch=curl_init();
			
			//2.设置curl的参数
			curl_setopt($ch, CURLOPT_URL, $url);
			curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
			if($type=='post'){
				curl_setopt($ch, CURLOPT_POST, 1);
				curl_setopt($ch,CURLOPT_POSTFIELDS,$arr);
			}
			//3.采集
			$output=curl_exec($ch);
			//4.关闭curl
			curl_close($ch);
			if($res=='json'){
				return json_decode($output,true);
			}
			
		}
	
		
		
		
		
		
		
		
		 function definedItem(){
	 
			header('content-type:text/html;charset=utf-8');
			 $access_token=$this->getWxAccessToken();
			//创建微信菜单
			echo '<hr />';
			echo  $access_token=$this->getWxAccessToken();
			$url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=".$access_token;
			$postArr=array(
			'button'=>array(
				array(
					'name'=>urlencode('菜单一'),
					'type'=>'click',
					'key'=>'item1',
				),
				array(
				'name'=>urlencode('菜单二'),
				'sub_button'=>array(
					array(
						'name'=>urlencode('lcy'),
						'type'=>'click',
						'key'=>'songs',
					),
					array(
						'name'=>urlencode('新浪'),
						'type'=>'view',
						'url'=>'http://www.baidu.com',
					),//第二个二级菜单
				),
				),
				array(
					'name'=>urlencode('菜单三'),
					'type'=>'view',
					'url'=>'http://www.qq.com'
				)		//第三个一级菜单
			),
			);
			echo '<hr />';
			echo  $postJson=urldecode(json_encode($postArr));
		//	echo '<hr />';
			$res=$this->http_curl($url,'post','json',$postJson);
			echo '<hr />';
			var_dump($res);
			
		}
		
		
		
		
		  //获取用户基本信息
	/*function  getUserInfo(){
		
        header('content-type:text/html;charset=utf-8');
        $access_token=$this->getWxAccessToken();
        $url="https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=".$access_token;
        $postArr2=array(
            'user_list'=>array(
                array(
                    'openid'=>"oQJGr0X_0Pby7DgG1ThKt-uvu33s",
                    'lang'=>"zh_CN"
                )
            )
        );
        echo  $postJson=urldecode(json_encode($postArr2));
        $res2=$this->http_curl($url,'post','json',$postJson);
        echo '<hr />';
        var_dump($res2);


    }*/
	
	//创建卡券
    function getUserCard(){
        header('content-type:text/html;charset=utf-8');
        $access_token=$this->getWxAccessToken();
		$url="https://api.weixin.qq.com/card/create?access_token=".$access_token;
        $postArr=array(
		'card'=>array(
		'card_type'=>'GROUPON',
            'groupon'=>array(
                'base_info'=>array(
                    'logo_url'=>'http://mmbiz.qpic.cn/mmbiz_jpg/SCDZXZ95v5x8k74cxzcnMO6GBxcCibcPtaNdTovdvUmtnP1aXhorgrY1jiavyToic4SgXHVpkBNPADwJ1lB3YWovw/0',
                    'brand_name'=>urlencode('微信餐厅'),
                    'code_type'=>'CODE_TYPE_TEXT',
                    'title'=>urlencode('132元双人火锅套餐'),
                    'sub_title'=>urlencode('周末狂欢必备'),
                    'color'=>'Color010',
                    'notice'=>urlencode('使用时向服务员出示此券'),
                    'service_phone'=>'020-88888888',
                    'description'=>urlencode('不可与其他优惠同享\n如需团购券发票，请在消费时向商户提出\n店内均可使用，仅限堂食'),
                    'date_info'=>array(
                        'type'=>'DATE_TYPE_FIX_TERM',
                        'fixed_term'=>15,
                        'fixed_begin_term'=>0
                    ),
                    'sku'=>array(
                        'quantity'=>50000
                    ),
                    'get_limit'=>3,
                    'use_custom_code'=>false,
                    'bind_openid'=>false,
                    'can_share'=>true,
                    'can_give_friend'=>true,
                     
                    'custom_url_name'=>urlencode('立即使用'),
                    'custom_url'=>'http://www.qq.com',
                    'custom_url_sub_title'=>urlencode('6个汉字tips'),
                    'promotion_url_name'=>urlencode('更多优惠'),
                    'promotion_url'=>'http://www.qq.com'
                ),
                'deal_detail'=>urlencode('以下锅底2选1（有菌王锅、麻辣锅、大骨锅、番茄锅、清补凉锅、酸 菜鱼锅可选）：\n大锅1份 12元\n小锅2份 16元 ')
            )
		)
          
        );
		// echo  $postJson=urldecode(json_encode($postArr));
		echo  $postJson=urldecode(json_encode($postArr));
        $res=$this->http_curl($url,'post','json',$postJson);
		echo '<hr />';
        var_dump($res);
    }
	
	
	 //创建二维码投放
    function createQR(){
        header('content-type:text/html;charset=utf-8');
        $access_token=$this->getWxAccessToken();
        $url="https://api.weixin.qq.com/card/qrcode/create?access_token=".$access_token;
        $postArr=array(
            'action_name'=>'QR_CARD',                
                'action_info'=>array(
                    'card'=>array(
                        'card_id'=>'pQJGr0S69Yo_gV3W2QEiJ7I_hMSw',
                    )
                )
            
        );
        echo  $postJson=urldecode(json_encode($postArr));
        $res=$this->http_curl($url,'post','json',$postJson);
        echo '<hr />';
        var_dump($res);
    }
	
	
	
	
	//创建会员卡
    function createVip(){
		
        header('content-type:text/html;charset=utf-8');
        $access_token=$this->getWxAccessToken();
        $url="https://api.weixin.qq.com/card/create?access_token=".$access_token;
        $postArr=array(
            'card'=>array(
                'card_type'=>'MEMBER_CARD',
                'member_card'=>array(
                    'background_pic_url'=>'http://mmbiz.qpic.cn/mmbiz_jpg/SCDZXZ95v5x8k74cxzcnMO6GBxcCibcPtNugmuITHqNWG4PeF4QIDtsmptGOl4QUf6Zz30qGuDGMic0IaPt1fRibQ/0',
                    'base_info'=>array(
                        'logo_url'=>'http://mmbiz.qpic.cn/mmbiz_jpg/SCDZXZ95v5x8k74cxzcnMO6GBxcCibcPtaNdTovdvUmtnP1aXhorgrY1jiavyToic4SgXHVpkBNPADwJ1lB3YWovw/0',
                        'brand_name'=>urlencode('海底捞'),
                        'code_type'=>'CODE_TYPE_QRCODE',
                        'title'=>urlencode('海底捞会员卡'),
                        'color'=>'Color010',
                        'notice'=>urlencode('使用时向服务员出示此券'),
                        'service_phone'=>'020-88888888',
                        'description'=>urlencode('不可与其他优惠同享'),
                        'date_info'=>array(
                            'type'=>'DATE_TYPE_PERMANENT'
                        ),
                        'sku'=>array(
                            'quantity'=>5000000
                        ),
                        'get_limit'=>3,
                        'use_custom_code'=>false,
                        'can_give_friend'=>true,
                        'location_id_list'=>array(
                            123,12321
                        ),
                        'custom_url_name'=>urlencode('立即使用'),
                        'custom_url'=>'http://weixin.qq.com',
                        'custom_url_sub_title'=>urlencode('6个汉字tips'),
                        'promotion_url_name'=>urlencode('营销入口1'),
                        'promotion_url'=>'http://www.qq.com',
                        'need_push_on_view'=>true
                    ),
                    'advanced_info'=>array(
                      'use_condition'=>array(
                          'accept_category'=>urlencode('鞋类'),
                          'reject_category'=>urlencode('阿迪达斯'),
                          'can_use_with_other_discount'=>true
                      ),
                        'abstract'=>array(
                            'abstract'=>urlencode('微信餐厅推出多种新季菜品，期待您的光临'),
                            'icon_url_list'=>array(
                                'http://mmbiz.qpic.cn/mmbiz/p98FjXy8LacgHxp3sJ3vn97bGLz0ib0Sfz1bjiaoOYA027iasqSG0sj
  piby4vce3AtaPu6cIhBHkt6IjlkY9YnDsfw/0'
                            )
                        ),
                        'text_image_list'=>array(
                            array(
                                'image_url'=>'http://mmbiz.qpic.cn/mmbiz/p98FjXy8LacgHxp3sJ3vn97bGLz0ib0Sfz1bjiaoOYA027iasqSG0sjpiby4vce3AtaPu6cIhBHkt6IjlkY9YnDsfw/0',
                                'text'=>urlencode('此菜品精选食材，以独特的烹饪方法，最大程度地刺激食 客的味蕾')
                            ),
                            array(
                                'image_url'=>'http://mmbiz.qpic.cn/mmbiz/p98FjXy8LacgHxp3sJ3vn97bGLz0ib0Sfz1bjiaoOYA027iasqSG0sj piby4vce3AtaPu6cIhBHkt6IjlkY9YnDsfw/0',
                                'text'=>urlencode('此菜品迎合大众口味，老少皆宜，营养均衡')
                            )
                        ),
                        'time_limit'=>array(
                            array(
                                'type'=>'MONDAY',
                                'begin_hour'=>0,
                                'end_hour'=>10,
                                'begin_minute'=>10,
                                'end_minute'=>59
                            ),
                            array(
                                'type'=>'HOLIDAY'
                            )
                        ),
                        'business_service'=>array(
                            'BIZ_SERVICE_FREE_WIFI','BIZ_SERVICE_WITH_PET','BIZ_SERVICE_FREE_PARK','BIZ_SERVICE_DELIVER'
                        )

                    ),
                    'supply_bonus'=>true,
                    'supply_balance'=>false,
                    'prerogative'=>'test_prerogative',
                    'auto_activate'=>true,
                    'custom_field1'=>array(
                        'name_type'=>'FIELD_NAME_TYPE_LEVEL',
                        'url'=>'http://www.qq.com'
                    ),
					'custom_field2'=>array(
                        'name_type'=>'FIELD_NAME_TYPE_COUPON',
                        'url'=>'http://www.qq.com'
                    ),
                    'activate_url'=>'http://www.qq.com',
                    'custom_cell1'=>array(
                        'name'=>urlencode('使用入口2'),
                        'tips'=>urlencode('激活后显示'),
                        'url'=>'http://www.xxx.com'
                    ),
                    'bonus_rule'=>array(
                        'cost_money_unit'=>100,
                        'increase_bonus'=>1,
                        'max_increase_bonus'=>200,
                        'init_increase_bonus'=>10,
                        'cost_bonus_unit'=>5,
                        'reduce_money'=>100,
                        'least_money_to_use_bonus'=>1000,
                        'max_reduce_bonus'=>50
                    ),
                    'discount'=>10
                )
            )
        );
		  echo  $postJson=urldecode(json_encode($postArr));
        $res=$this->http_curl($url,'post','json',$postJson);
        echo '<hr />';
        var_dump($res);
    }
	
	
	//获取用户OpenId
    function  getUserOpenId(){
        $appid=$this->appID;
        $secret=$this->appsecret;
        $code=$_GET['code'];
        //2.获取网页授权的access_token
        $url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=".$appid."&secret=".$secret."&code=".$code."&grant_type=authorization_code";
        //3.拉取用户的openid
       $res= $this->http_curl($url,'get');
	  // header('location:'."https://hfq.huift.com.cn/huiquan");
       return $res['openid'];
	   //$openId=$res['openid'];
	
		//var_dump($res['openid']);
		
		
    }
	

	
		
		
    }
    $wx = new Weixin();
	
	//echo $wx->getAccessToken();
   // echo $wx->getWxAccessToken();
	
	
	//echo '<hr />';
	// echo $wx->getUserCard();
	 
	// echo '<hr />';
	// echo $wx->createQR();
	
	//echo $wx->createVip();
	echo $wx->getUserOpenId();
    /*
        获取access_token方法 get方法
    */
?>



