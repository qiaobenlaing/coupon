//
//  BMSQ_JoinOtherSuccessViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/18.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_JoinOtherSuccessViewController.h"
#import "SVProgressHUD.h"
#import "BMSQ_MyBusinessController.h"
@interface BMSQ_JoinOtherSuccessViewController ()<UIWebViewDelegate>

@end

@implementation BMSQ_JoinOtherSuccessViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    UIView *navView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_ORIGIN_Y)];
    navView.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [self.view addSubview:navView];
    UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, APP_VIEW_ORIGIN_Y-20)];
    titleLabel.backgroundColor = [UIColor clearColor];
    titleLabel.text = @"注册成功";
    titleLabel.textAlignment = NSTextAlignmentCenter;
    titleLabel.font = [UIFont boldSystemFontOfSize:18];
    titleLabel.textColor = [UIColor whiteColor];
    [navView addSubview:titleLabel];
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y-APP_NAV_LEFT_ITEM_HEIGHT, 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    [btnback addTarget:self action:@selector(goBackclick) forControlEvents:UIControlEventTouchUpInside];
    [navView addSubview:btnback];
    
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    UIWebView *webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    NSURL* url = [NSURL URLWithString:self.requestStr];//创建URL
    NSURLRequest* request = [NSURLRequest requestWithURL:url];//创建NSURLRequest
    webView.delegate = self;
    [webView loadRequest:request];//加载
    
    [self.view addSubview:webView];
    

}

-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [SVProgressHUD dismiss];
}
-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if (navigationType==UIWebViewNavigationTypeLinkClicked) {
        
        NSString *urlString=[[request URL] absoluteString];
        if ([urlString isEqualToString:@"hq://getUserCouponInfo?userCouponCode="]) { //去看看
            //表示点击了注册按钮，填写要执行的代码
            
            UIApplication *app = [UIApplication sharedApplication];
            UIWindow *window = app.keyWindow;
            UITabBarController *tabbarVC = (UITabBarController *)[window rootViewController];
            tabbarVC.selectedIndex = 2;
            [self dismissViewControllerAnimated:YES completion:nil];
            
            
            
            
            return NO;
        }else if([urlString isEqualToString:@"hq://getUserInviteCode"]){  //推荐好友赢更多福利 我的推荐码:
            
            [self dismissViewControllerAnimated:YES completion:nil];

            UIApplication *app = [UIApplication sharedApplication];
            UIWindow *window = app.keyWindow;
            UITabBarController *tabbarVC = (UITabBarController *)[window rootViewController];
            tabbarVC.selectedIndex = 3;
            [self dismissViewControllerAnimated:YES completion:nil];

            UINavigationController *nc = (UINavigationController *)tabbarVC.selectedViewController;
            BMSQ_MyBusinessController *rootvc = (BMSQ_MyBusinessController *)[nc.viewControllers objectAtIndex:0 ];
            [rootvc gotoSec];
            
            
            return NO;
        }
 
    }
    
    return YES;
}
-(void)goBackclick{
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end
