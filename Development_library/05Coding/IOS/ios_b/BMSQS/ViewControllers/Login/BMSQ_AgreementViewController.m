//
//  BMSQ_AgreementViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/25.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_AgreementViewController.h"

@interface BMSQ_AgreementViewController ()

@end

@implementation BMSQ_AgreementViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.navigationController setNavigationBarHidden:NO animated:YES];
//    [self.navigationItem setTitle:self.navTitle];
    [self setNavBackItem];
    [self setNavTitle:self.navViewTitle];
    if (self.url)
    {
        NSURLRequest *request =[NSURLRequest requestWithURL:[NSURL URLWithString:self.url]];
        [_webView loadRequest:request];
    }
    
    if ([self.navViewTitle isEqual:@"关于惠圈"]) {
        CGRect frame = _webView.frame;
        frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
        
        _webView.frame = frame;
    }
    
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void)webViewDidStartLoad:(UIWebView *)webView;
{
    
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType;
{
    NSLog(@"request=%@",request);    
    if (self.url)
    {
        NSString *theTitle=[webView stringByEvaluatingJavaScriptFromString:@"document.title"];
        if(theTitle&&theTitle.length>0)
            self.title = theTitle;
    }
    return YES;
}

- (void)webViewDidFinishLoad:(UIWebView *)webView;
{
    NSLog(@"webview=%@",webView);
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error;
{
    NSLog(@"errot%@",error);
    if (![error.localizedDescription isEqualToString:@"Plug-in handled load"]){
    }
    if ( [error code] == 204 ) {
        
    }
}

@end
