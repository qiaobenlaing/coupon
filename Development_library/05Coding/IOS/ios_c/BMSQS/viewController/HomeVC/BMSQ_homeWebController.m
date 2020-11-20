//
//  BMSQ_homeWebController.m
//  BMSQC
//
//  Created by liuqin on 15/12/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_homeWebController.h"
#import "BMSQ_LoginViewController.h"
#import "BaseNavViewController.h"
#import "BMSQ_Share.h"
#import "BMSQ_ShopDetailController.h"
#import "BMSQ_NewShopDetailViewController.h"
#import "BMSQ_joinActivityViewController.h"

@interface BMSQ_homeWebController ()<UIWebViewDelegate>
@property (nonatomic,strong) UIWebView *webView;
@end

@implementation BMSQ_homeWebController

-(void)viewDidLoad{
    [super viewDidLoad];
    [self setNavigationBar];
    self.view.backgroundColor = [UIColor whiteColor];
   self.webView = [[UIWebView alloc]initWithFrame:CGRectMake(0,20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-20)];
    [self.webView setBackgroundColor:[UIColor whiteColor]];
    self.webView.delegate = self;
    [self.view addSubview:self.webView];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(loginSuccess) name:@"loginSuccess" object:nil];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:self.urlStr]]];

}
-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType{
    if (navigationType==UIWebViewNavigationTypeLinkClicked) {
        
        NSString *urlString=[[request URL] absoluteString];
        if ([urlString isEqualToString:@"hg://backup"] ||[urlString isEqualToString:@"hs://backup"] ) {
            [self.navigationController popViewControllerAnimated:YES];
        }
        else if ([urlString isEqualToString:@"hs://login"]) {
            [self getLogin];
        }
        else if ([urlString hasPrefix:@"hs://getShopInfo"]) {
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 9)];
            
            BMSQ_NewShopDetailViewController * detailCtrl = [[BMSQ_NewShopDetailViewController alloc] init];
            detailCtrl.shopCode = mutableStr;
            detailCtrl.userCode = [gloabFunction getUserCode];
            detailCtrl.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:detailCtrl animated:YES];
            
            
        }
        else if ([urlString hasPrefix:@"hg://share"]) {
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 7)];
            NSDictionary *dic = [self dictionaryWithJsonString:mutableStr];
            if (dic) {
                NSString *content = [dic objectForKey:@"content"];
                NSString *imagepath = [dic objectForKey:@"icon"];
                NSString *title = [dic objectForKey:@"title"];
                NSString *url = [dic objectForKey:@"link"];
                [BMSQ_Share shareClick:content imagePath:imagepath title:title url:url];
            }
        }
        //活动
        else if ([urlString hasPrefix:@"hs://orderAct?activityCode"]){ //参加报名
            //hs://orderAct?activityCode=9a3dcb3d-e219-d980-9a82-5feb5bf5b079
            NSArray *array = [urlString componentsSeparatedByString:@"="];
            NSString *activityCode = [array objectAtIndex:1];
            [self join:activityCode];
            
        }else if([urlString hasPrefix:@"hs://share"]){
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 7)];
            NSDictionary *dic = [self dictionaryWithJsonString:mutableStr];
            if (dic) {
                [BMSQ_Share shareClick:[dic objectForKey:@"content"] imagePath:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"icon"]] title:[dic objectForKey:@"title"] url:[dic objectForKey:@"link"]];
            }
            
        }
        else if ([urlString hasPrefix:@"hs://call"]){
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSArray *array = [str componentsSeparatedByString:@"="];
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",[array objectAtIndex:1]]]];
            
        }
        //活动

    }
    return YES;
}

#pragma mark --报名--
-(void)join:(NSString *)activityCode{
    BMSQ_joinActivityViewController *vc = [[BMSQ_joinActivityViewController alloc]init];
    vc.activityCode = activityCode;
    [self.navigationController pushViewController:vc animated:YES];
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
-(void)webViewDidStartLoad:(UIWebView *)webView{
    [SVProgressHUD showWithStatus:@""];
}
-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [SVProgressHUD dismiss];
}
-(void)loginSuccess{
    NSString *url = [NSString stringWithFormat:@"%@&userCode=%@",self.urlStr,[gloabFunction getUserCode]];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
}
- (void)getLogin
{
    UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
    BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
    BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
    [self presentViewController:nav animated:YES completion:^{
        
    }];
}
@end
