//
//  BMSQ_CouponOPViewController.m
//  BMSQC
//
//  Created by liuqin on 16/1/8.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_CouponOPViewController.h"

#import "BMSQ_ShopDetailController.h"
#import "MobClick.h"
@interface BMSQ_CouponOPViewController ()<UIWebViewDelegate>

@property (nonatomic, strong)UIWebView *webView;

@end


@implementation BMSQ_CouponOPViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"CouponOP"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"CouponOP"];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [self setNavigationBar];
    
    
    self.webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-20)];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:self.urlStr]]];
    self.webView.delegate = self;
    [self.view addSubview:self.webView];
    [self.view bringSubviewToFront:self.webView];
    
    self.webView.scrollView.bounces = NO;
}
-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType{
    if (navigationType==UIWebViewNavigationTypeLinkClicked) {
        
        NSString *urlString=[[request URL] absoluteString];
        NSLog(@"%@",urlString);
        if ([urlString hasPrefix:@"hs://call"]){
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 4)];
            
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",mutableStr]]];
            
        }else if ([urlString hasPrefix:@"hs://getShopInfo"]){
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 9)];

            BMSQ_ShopDetailController *vc = [[BMSQ_ShopDetailController alloc]init];
            vc.shopCode =mutableStr;
            [self.navigationController pushViewController:vc animated:YES];
            

        }else if ([urlString isEqualToString:@"hs://backup"]){
       
            [SVProgressHUD dismiss];
            [self.navigationController popViewControllerAnimated:YES];;
            
            
        }
       
    }
    return YES;
}



-(void)webViewDidStartLoad:(UIWebView *)webView{
    [SVProgressHUD showWithStatus:@""];
}
-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [SVProgressHUD dismiss];
}

@end
