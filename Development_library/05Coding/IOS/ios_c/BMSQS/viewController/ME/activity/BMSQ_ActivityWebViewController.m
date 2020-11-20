//
//  BMSQ_ActivityWebViewController.m
//  BMSQC
//
//  Created by liuqin on 16/1/22.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_ActivityWebViewController.h"
#import "BMSQ_joinActivityViewController.h"
#import "MobClick.h"
#import "BMSQ_Share.h"

@interface BMSQ_ActivityWebViewController ()<UIWebViewDelegate>
@property (nonatomic,strong) UIWebView *webView;


@end

@implementation BMSQ_ActivityWebViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"ActivityWeb"];// 
}



-(void)viewDidLoad{
    [super viewDidLoad];
    [self setNavigationBar];
    self.view.backgroundColor = [UIColor whiteColor];
    self.webView = [[UIWebView alloc]initWithFrame:CGRectMake(0,20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-20)];
    [self.webView setBackgroundColor:[UIColor whiteColor]];
    self.webView.delegate = self;
    [self.view addSubview:self.webView];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:self.urlStr]]];
    
}
-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType{
    if (navigationType==UIWebViewNavigationTypeLinkClicked) {
        
        NSString *urlString=[[request URL] absoluteString];
        NSLog(@"%@",urlString);
        if ([urlString isEqualToString:@"hs://backup"] ) {
            [self.navigationController popViewControllerAnimated:YES];
        }else if ([urlString hasPrefix:@"hs://orderAct?activityCode"]){ //参加报名
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
-(void)webViewDidStartLoad:(UIWebView *)webView{
    [SVProgressHUD showWithStatus:@""];
}
-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [SVProgressHUD dismiss];
}
-(void)viewWillDisappear:(BOOL)animated{
    [SVProgressHUD dismiss];
    [MobClick endLogPageView:@"ActivityWeb"];

}
-(void)loginSuccess{
    NSString *url = [NSString stringWithFormat:@"%@&userCode=%@",self.urlStr,[gloabFunction getUserCode]];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
}
- (void)getLogin{
    //    UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
    //    BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
    //    BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
    //    [self presentViewController:nav animated:YES completion:^{
    //
    //    }];
}

#pragma mark --报名--
-(void)join:(NSString *)activityCode{
    BMSQ_joinActivityViewController *vc = [[BMSQ_joinActivityViewController alloc]init];
    vc.activityCode = activityCode;
    [self.navigationController pushViewController:vc animated:YES];
}
@end
