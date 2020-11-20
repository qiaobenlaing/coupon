//
//  BMSQ_NewShopDetailViewController.h
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/17.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "SVProgressHUD.h"
#import <ShareSDK/ShareSDK.h>
#import "BMSQ_Share.h"
#import "BMSQ_CouponDetailViewController.h"
#import "BMSQ_BuyCouponViewController.h"
#import "BMSQ_LoginViewController.h"
#import "BaseNavViewController.h"
#import "Pay_SecViewController.h"
#import "BMSQ_PayDetailViewController.h"
#import "BMSQ_ActivityWebViewController.h"
@interface BMSQ_NewShopDetailViewController : UIViewControllerEx

@property (nonatomic, strong) NSString * shopCode;
@property (nonatomic, strong) NSString * userCode;


@end
