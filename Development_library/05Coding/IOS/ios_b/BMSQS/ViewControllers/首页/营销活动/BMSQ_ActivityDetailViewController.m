//
//  BMSQ_ActivityDetailViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/8/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ActivityDetailViewController.h"
#import "UIImageView+WebCache.h"
#import "SVProgressHUD.h"

@interface BMSQ_ActivityDetailViewController ()<UIWebViewDelegate>

@property (nonatomic ,strong) UITableView* tableView;
@property (nonatomic ,strong) UILabel *titleLabel;
@property (nonatomic ,strong) UIImageView *activityImageView;;
@property (nonatomic ,strong) UILabel *desLabel;
@property (nonatomic ,strong) NSDictionary *dataDic;

@end

@implementation BMSQ_ActivityDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"活动详情"];
    [self setNavBackItem];
    
    UIWebView *webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@Browser/sGetActInfo/actCode/%@",APP_SERVERCE_H5_URL,self.activityCode]]]];
    webView.delegate = self;
    [self.view addSubview:webView];
    
}
-(void)webViewDidStartLoad:(UIWebView *)webView{
    [SVProgressHUD showWithStatus:@""];
}
-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [SVProgressHUD dismiss];
}
-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    [SVProgressHUD dismiss];

}

@end
