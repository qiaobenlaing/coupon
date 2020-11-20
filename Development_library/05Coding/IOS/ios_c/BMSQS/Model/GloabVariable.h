//
//  GloabVariable.h
//  SDBooking
//
//  Created by icarddjx on 14-1-22.
//  Copyright (c) 2014年 djx. All rights reserved.
//

//判断是否Iphone5
#define iPhone4 ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 960), [[UIScreen mainScreen] currentMode].size) : NO)
#define iPhone5 ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 1136), [[UIScreen mainScreen] currentMode].size) : NO)

//屏蔽//NSLog
#ifndef __OPTIMIZE__

# define debug 1 //debug环境

#else

# define debug 0

#endif

#ifdef debug
#define DLog( s, ... ) NSLog( @"<行号:%d 方法名:%s> %@", __LINE__,__func__,[NSString stringWithFormat:(s), ##__VA_ARGS__] )
#else
#define DLog( s, ... )
#endif

/*******************************
 第三方key和secret
 *******************************/
#define APP_MAP_KEY   @"AOBPj4EdTx5znnI8v6pugLRn" //@"B8CjOCyKyZhoEuyRCTIOUS4p" //地图key,百度地图
#define APP_YM_KEY  @"54ddac71fd98c54226000bd8" //友盟key
#define APP_WX_ID  @"wx458808a03d4cfe4f" //微信key
#define APP_WX_SECRET @"3171e4f236f26aeb0211d5e2c782532c" //微信Secret
#define APP_WX_PAY_KEY @"NusuW3oTTfShdqCFQIHZQi9EDp2ns7fXwaRLKtRONAp65aTcspVZhlPiR0YWdToloJvZ8YKBmklXb9hSihwvMIJeCCd93mHgQmQWhfgIzKaGok769IFaRrh4KNCI4BlW" //商户支付秘钥
#define APP_WX_MERCHANT_ID @"1219391201" //微信支付商户号
#define APP_WX_PARTNERKEY @"7f97de1a1c6b0d745f0c0c49edbe0961" //微信支付初始密钥(PartnerKey)

#define APP_QQ_KEY  @"1104357230"

#define APP_ALIPAY_PID @"2088311758792711" //支付宝PID，partner id
#define APP_ALIPAY_SELLER @"2088311758792711" //支付宝卖家id，可以是pid也可以是申请的邮箱
#define APP_ALIPAY_KEY @"gpfkvtbbw128mfpdccjj81ur1xsiv5lw" //支付宝key
#define APP_ALIPAY_PRIVATEKEY @"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMZ6mOviLj8FA1VMExh1+ZyTP+3DlcMK+oQzPwUiyw8CD4HbaidtO1kVym7I0/8s2VyEexOdovAPe+BPOKPGMoGs2SfjsTCpKKc1A9d/lSR+1v67qRKL2jVlzOxOd+sOyyKuH3671RT5e++l5EHhrprPJwpY7Mz+wjsFB0YIGNaDAgMBAAECgYBLS2sD4VQl+CLhkvCTkP3Wlk3kRxRjX6DV0hMQQbjCwsdbPf+xYVDoZMnc6TbzUPJMlL2UHXMYmuclmygjiSbTOp7sxo92cdjGhCo0YqGTQrnpd733YkXZNW59XMIC8n4JrGhbBlWmPEO1qVQFjDh+q6oMxsywx0QCrdaeMx0gsQJBAP31LavFvjGyDrJVQgtjQ0A+5Y6T/zOa/vb8G232RIRn9PDrk+zMgRjOr1S316rSi2thOfoBZAPaxbWEQB8u/w8CQQDIEzR2cI3nkAu48Fq2TQtF6dUoo9HAWxoP7FImt3xG9B2BtRYsAcvksW65e9Xv9IQnfNNOktySppv8sPeZl/FNAkAbAE5yzPuD3SKi4126SDuGQSm3FxUzL2+cYwGnl1+BlGv+kY2Qx82SDaemokVT7D7Wk+fOJQe1QTV0LzOCHUURAkEAoLoM4zj4RpYJVMCBnwG1lKyMeOFhl02YWkJWnJO6WqWxLonzDddDnKyNbqR08RdVMwOsHOsOFyGRHiZsQg814QJAcDCc5m9ugy+qM+nLCfOUcJHozarGTNPqUgjtxi+60f5imqWdKxE6ityv5OMqkDGo4d9quDNFlJBaCIIYzfVLfA==" //支付宝私钥




//URL
#define BASE_URL @"http://baomi.suanzi.cn/Wechat" //
#define APP_SERVERCE_COMM_URL @"http://baomi.suanzi.cn/Api/Comm" //通用服务器地址
#define APP_SERVERCE_SHOP_URL @"http://baomi.suanzi.cn/Api/Shop" //商家服务器地址
#define APP_SERVERCE_CLIENT_URL @"http://baomi.suanzi.cn/Api/Client" //顾客服务器地址
#define H5_URL   @"http://baomi.suanzi.cn/Api/"   //开发版HTML
#define IMAGE_URL @"http://baomi.suanzi.cn"

//#define BASE_URL @"http://huiquan.suanzi.cn/Wechat" //
//#define APP_SERVERCE_COMM_URL @"http://api.huiquan.suanzi.cn/Api/Comm" //正式通用服务器地址
//#define APP_SERVERCE_SHOP_URL @"http://api.huiquan.suanzi.cn/Api/Shop"   //正式商家服务器地址
//#define APP_SERVERCE_CLIENT_URL @"http://api.huiquan.suanzi.cn/Api/Client" //正式顾客服务器地址
//#define H5_URL   @"http://api.huiquan.suanzi.cn/Api/"  //正式HTML
//#define IMAGE_URL @"http://api.huiquan.suanzi.cn"  //正式图片请求地址


#define H5_URLL   @"http://api.test.huiquan.suanzi.cn:1080/Api/"
#define APP_SERVERCE_REDBACK @"http://baomi.suanzi.cn/Api/Browser/grabBonus/bonusCode//" //红包请求
#define APP_COUPON @"00000000-0000-0000-0000-000000000000"

#define APP_PRODUCT_ID @"965746332" //产品appstoreid
#define APP_DownloadURL @"https://itunes.apple.com/us/app/piao-shi-dai/id853467968?ls=1&mt=8"
#define APP_PAYWAY_ALIPAY @"000044" //PAYWAY 支付宝
#define APP__PAYWAY_UNIPAY @"000045" //PAYWAY 银联
#define APP_PAYWAY_WX @"000046" //PAYWAY 微信支付
#define APP_PAYWAY_UNIPAY_MODE (debug == 0)?@"00":@"00"   //01测试环境 00正式环境
#define APP_SAVE_USERDEFAULTS(key,value) [[NSUserDefaults standardUserDefaults] setObject:value forKey:key]
//sina微博key
#define APP_SINA_WB_KEY @"512618886"
#define APP_SINA_WB_SECRET @"f5ca97c60e9406628291c23923e7b877"
#define APP_SINA_WB_kRedirectURI    @"http://www.sina.com"

#define appDelegate				        ((AppDelegate *)[[UIApplication sharedApplication] delegate])
#define appDefalutEngine				 ([(AppDelegate *)[[UIApplication sharedApplication] delegate] defalutEngine])

#define RMB_SYMBOL  @"¥"
//是否为ios7系统
#define IOS7 ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0 ? YES:NO) //IOS7.0

#define APP_FONT_NAME @"TrebuchetMS-Bold"

//app view尺寸
#define APP_VIEW_WIDTH    ([[UIScreen mainScreen] applicationFrame].size.width)//([[UIScreen mainScreen] currentMode].size.width/2)   //app view的基本宽度
#define APP_VIEW_HEIGHT   ([[UIScreen mainScreen] applicationFrame].size.height+20)//([[UIScreen mainScreen] currentMode].size.height/2)   //app view的基本高度
//#define APP_IS_SHOW_STATUSBAR  1 //O表示隐藏，1表示显示
#define APP_STATUSBAR_HEIGHT 20 //如果显示则高度为20 否则为0
#define APP_NAVGATION_HEIGHT 44 //导航条高度
#define APP_NAV_LEFT_ITEM_HEIGHT 44 //导航条左边按钮高度
#define APP_NAV_LEFT_ITEM_WIDTH  30 //导航条左边按钮宽度
#define APP_NAV_RIGHT_ITEM_HEIGHT 44 //导航条右边按钮高度
#define APP_NAV_RIGHT_ITEM_WIDTH  30 //导航条右边按钮宽度
#define APP_NAV_RIGHT_X_POSITION  (APP_VIEW_WIDTH - APP_NAV_RIGHT_ITEM_WIDTH - 10)
#define APP_TABBAR_HEIGHT 49 //底部导航条的高度
#define APP_VIEW_CAN_USE_HEIGHT  (APP_VIEW_HEIGHT - APP_STATUSBAR_HEIGHT - APP_NAVGATION_HEIGHT)// - APP_TABBAR_HEIGHT) //app view可使用高度
#define UICOLOR(R,G,B,A) [UIColor colorWithRed:R/255.0 green:G/255.0 blue:B/255.0 alpha:A]
#define APP_VIEW_ORIGIN_Y (APP_STATUSBAR_HEIGHT + APP_NAVGATION_HEIGHT) //APPY轴起点

#define APP_USER_CHANEL_ID @"000101" //用户信息chanel 
#define APP_PHONE_REG @"^((13[0-9])|(147)|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$" //APP手机号正则表达式判断
#define APP_CELL_LINE_HEIGHT 0.5 //线的高度
#define APP_FOTERVIEW_HEIGHT 13 //分组页脚高度
#define APP_CELL_LINE_COLOR UICOLOR(207, 207, 207, 1.0) //cell 分割线颜色

#define HOME_MODULE_HEIGHT APP_VIEW_WIDTH/5


#define APP_TEXTCOLOR UICOLOR(84, 84, 84, 1.0)
#define APP_NAVCOLOR UICOLOR(197, 0, 11, 1.0)
#define APP_VIEWCOLOR UICOLOR(240, 239, 245, 1.0)
#define APP_VIEW_BACKCOLOR UICOLOR(232, 232, 232, 1.0) //app view背景色

//APP 服务器连接地址


#define APP_HQ_CODE @"00000000-0000-0000-0000-000000000000"


//APP 保存文件的路径
#define APP_SAVEFILE_PATH @"SDZX"
#define APP_SAVEFILE_CITY_NAME @"city.plist" //保存开放影院的城市信息（plist格式，文件名和key值一样）,
#define APP_SAVEFILE_CITY_KEY @"CITY"
#define APP_USER_INFO_PLIST @"user.plist" //用户信息plist
#define APP_USER_INFO_KEY @"USERINFO" //用户信息key
#define APP_USER_AOCNUM_KEY @"USERAOCNUM"
#define APP_USER_CANCEL_VERSION_KEY @"CANCEL_VERSION_TIME" //用户取消更新版本时间
#define APP_WX_ACCESS_TOKEN @"WXPAY_TOKEN" //微信支付token
#define APP_WX_EXPIRES_TIME @"WXPAY_EXPIRES" //微信申请token时间
#define APP_WX_LOGIN_USERINFO @"WX_USER_INFO" //微信登陆用户信息
#define APP_USER_INFO @"userinfo"
#define APP_USER_FREEVALCODEPAY @"freeValCodePay"
#define APP_USER_MOBILENBR @"app_user_mobileNBR"
#define APP_USER_HEADIMG @"userHeadImg"


#define LONGITUDE @"longitude"
#define LATITUDE @"latitude"
#define CITY @"city"
#define SELECITY @"selecity"

#define RegistratcionID @"registratctionid"


#define CSAlert(_S_)     [[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"提示",nil) message:_S_ delegate:nil cancelButtonTitle:NSLocalizedString(@"知道了",nil) otherButtonTitles:nil] show]

#define IsNOTNullOrEmptyOfNSString(_STRING___) (_STRING___ && [_STRING___ isKindOfClass:[NSString class]] && [_STRING___ length])
#define IsNOTNullOrEmptyOfNSNumber(_NUMBER___) (_NUMBER___ && [_NUMBER___ isKindOfClass:[NSNumber class]] )


#define IsNOTNullOrEmptyOfDictionary(_DICTIONARY___) (_DICTIONARY___ && [_DICTIONARY___ isKindOfClass:[NSDictionary class]] && [_DICTIONARY___ count])
#define IsNullObj(____object____) ((____object____ && [____object____ isKindOfClass: [NSNull class]]) || (____object____ == nil))

#define ProgressHudStr @"正在加载"

#define GRAYCOLOR [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1]

#define TABLEVIEWCELLH 44
