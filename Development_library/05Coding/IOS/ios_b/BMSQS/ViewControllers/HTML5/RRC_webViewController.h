//
//  SDZX_webViewController.h
//  SDBooking
//
//  Created by djx on 14-3-28.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "UIWebviewEx.h"

@interface RRC_webViewController : UIViewControllerEx<UIWebViewDelegate>

//webview跳转的url
@property(nonatomic,strong)NSString* requestUrl;
@property(nonatomic,strong)NSString* navtitle;
@property(nonatomic, assign)BOOL isHidenNav;

@property (nonatomic, assign)BOOL isPayResult;  //是不是支付页面
@property (nonatomic, assign)BOOL isTakeOut;  //是不是外卖

@property (nonatomic, assign)BOOL isBuy;

@property (nonatomic, assign)BOOL isClick;

@property (nonatomic, assign)int fromVC;


@property (nonatomic, strong)NSString *actCode;
@property (nonatomic, assign)BOOL isShare;
@property (nonatomic, assign)BOOL isJoin;


@end
