//
//  BMSQ_JPushVCWebVIewController.m
//  BMSQC
//
//  Created by liuqin on 16/1/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_JPushVCWebVIewController.h"

@interface BMSQ_JPushVCWebVIewController ()<UIWebViewDelegate>

@property (nonatomic, strong)UIWebView *webView;

@end


@implementation BMSQ_JPushVCWebVIewController


-(void)viewDidLoad{
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:self.myTitle];
    
    self.view.backgroundColor = [UIColor whiteColor];
    self.webView = [[UIWebView alloc]initWithFrame:CGRectMake(0,20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-20)];
    [self.webView setBackgroundColor:[UIColor whiteColor]];
    self.webView.delegate = self;
    [self.view addSubview:self.webView];
//    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(loginSuccess) name:@"loginSuccess" object:nil];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:self.urlStr]]];
    
    
}

-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType{
    if (navigationType==UIWebViewNavigationTypeLinkClicked) {
        
        NSString *urlString=[[request URL] absoluteString];
        if ([urlString isEqualToString:@"hs://backup"]) {
            [self dismissViewControllerAnimated:YES completion:nil];
        }
        /*
        else if ([urlString isEqualToString:@"hs://login"]) {
            [self getLogin];
        }
        else if ([urlString hasPrefix:@"hs://getShopInfo"]) {
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 9)];
            BMSQ_ShopDetailController* shopDetailCtrl = [[BMSQ_ShopDetailController alloc]init];
            shopDetailCtrl.shopCode = mutableStr;
            //            shopDetailCtrl.shopName = [dic objectForKey:@"shopName"];
            //            shopDetailCtrl.shopImage = [dic objectForKey:@"logoUrl"];
            [self.navigationController pushViewController:shopDetailCtrl animated:YES];
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
         */
    }
    return YES;
}
/*
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
*/
-(void)webViewDidStartLoad:(UIWebView *)webView{
    [SVProgressHUD showWithStatus:@""];
}
-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [SVProgressHUD dismiss];
}
//-(void)loginSuccess{
//    NSString *url = [NSString stringWithFormat:@"%@&userCode=%@",self.urlStr,[gloabFunction getUserCode]];
//    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
//}
- (void)getLogin
{
//    UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
//    BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
//    BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
//    [self presentViewController:nav animated:YES completion:^{
//        
//    }];
}


-(void)goBack{
    
    [self dismissViewControllerAnimated:YES completion:nil];
    
}

@end
