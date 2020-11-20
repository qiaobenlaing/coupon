//
//  NotifiCationViewController.m
//  BMSQC
//
//  Created by liuqin on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "NotifiCationViewController.h"

@interface NotifiCationViewController ()<UIWebViewDelegate>

@property(nonatomic, strong)UIWebView *webView;

@end


@implementation NotifiCationViewController

-(void)viewDidLoad{
    [super viewDidLoad];
    
    self.webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-20)];
    NSString *str = [NSString stringWithFormat:@"%@%@",H5_URL,[self.dic objectForKey:@"webUrl"]];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:str]]];
    self.webView.delegate = self;
    [self.view addSubview:self.webView];
    [self.view bringSubviewToFront:self.webView];
    self.webView.scrollView.bounces = NO;
    
    UIButton *disButton = [[UIButton alloc]initWithFrame:CGRectMake(10, 20, 40, 40)];
    disButton.backgroundColor = [UIColor redColor];
    [self.view addSubview:disButton];
    
    [disButton addTarget:self action:@selector(dismissVC) forControlEvents:UIControlEventTouchUpInside];

}
-(void)dismissVC{
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end
