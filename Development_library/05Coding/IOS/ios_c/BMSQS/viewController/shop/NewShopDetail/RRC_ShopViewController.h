//
//  RRC_ShopViewController.h
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/22.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface RRC_ShopViewController : UIViewControllerEx<UIWebViewDelegate>

//webview跳转的url
@property(nonatomic,strong)NSString* requestUrl;
@property(nonatomic,strong)NSString* navtitle;
@property(nonatomic, assign)BOOL isHidenNav;




@end
