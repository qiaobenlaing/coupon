//
//  SDZX_webViewController.m
//  SDBooking
//
//  Created by djx on 14-3-28.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "RRC_webViewController.h"
#import "SVProgressHUD.h"
#import "BMSQ_RegistViewController.h"

#import "BMSQ_LoginViewController.h"
#import "BaseNavViewController.h"

#import "BMSQ_CouponDetailViewController.h"
#import <ShareSDK/ShareSDK.h>
#import "HQShareUtils.h"


//#import <ShareSDK/ShareSDK.h>
@interface RRC_webViewController ()
{
    UIWebviewEx*  webView; //webView，加载网页
}

@property (nonatomic, assign)BOOL isFinsh;


@property (nonatomic,strong)NSString *urlStr;

@end

@implementation RRC_webViewController
@synthesize requestUrl;
@synthesize navtitle;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    
    [self setViewUp];
    
    
    

}





-(void)viewWillDisappear:(BOOL)animated{
    [SVProgressHUD dismiss];
}

- (void)setViewUp
{
    if (!self.isHidenNav) {
        [self setNavigationBar];
        [self setNavBackItem];
        [self setNavTitle:navtitle];
        self.view.backgroundColor = [UIColor whiteColor];
        webView = [[UIWebviewEx alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-self.navigationView.frame.size.height)];
        [webView setBackgroundColor:[UIColor whiteColor]];
        webView.delegate = self;
        [self.view addSubview:webView];
    }else{
        [self setNavHidden:YES];
        
        if (IOS7) {
            UIView *statusView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 20)];
            [statusView setBackgroundColor:UICOLOR(182, 0, 12, 1.0)];
            [self.view addSubview:statusView];
            
        }
        
        
        webView = [[UIWebviewEx alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 20)];
        [webView setBackgroundColor:[UIColor whiteColor]];
        [self.view addSubview:webView];
        
    
        
        
    }
    
    webView.delegate = self;
    [webView stopLoading];
    
    if (requestUrl != nil && requestUrl.length > 0)
    {
        [webView loadRequestWithString:requestUrl];
    }
    
    
    
}

-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if (navigationType==UIWebViewNavigationTypeLinkClicked) {
        
        NSString *urlString=[[request URL] absoluteString];
        if ([urlString hasPrefix:@"hs://share?params="]) {
            
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 7)];
            NSDictionary *shareParams = [self dictionaryWithJsonString:mutableStr];
            if (shareParams) {
                [self clickShare:shareParams];
                
            }

            
        }else if ([urlString hasPrefix:@"hs://call?tel="]){
            
                UIWebView *callWebView = [[UIWebView alloc] init];
                NSString * phoneNumber =  [urlString  substringFromIndex:14];
                NSURL *telURL = [NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",phoneNumber]];
                [callWebView loadRequest:[NSURLRequest requestWithURL:telURL]];
                [self.view addSubview:callWebView];
            
            
        }
        else if ([urlString hasPrefix:@"hs://backup"]){
            [self.navigationController popViewControllerAnimated:YES];
        }
    }
    return YES;
}

- (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString {
    if (jsonString == nil) {
        return nil;
    }
    
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                        options:NSJSONReadingMutableContainers
                                                          error:&err];
    if(err) {
        NSLog(@"json解析失败：%@",err);
        return nil;
    }
    return dic;
}


-(void)clickShare:(NSDictionary *)dicShare
{
    [HQShareUtils shareCouponWithTitle:dicShare[@"title"] content:dicShare[@"content"] url:dicShare[@"link"]];
}

-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [SVProgressHUD dismiss];
    self.isFinsh = YES;
}





-(void)webViewDidStartLoad:(UIWebView *)webView{
    [SVProgressHUD showWithStatus:@""];
}


@end
