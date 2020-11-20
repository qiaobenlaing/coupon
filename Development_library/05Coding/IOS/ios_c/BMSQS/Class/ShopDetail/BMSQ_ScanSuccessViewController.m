//
//  BMSQ_ScanSuccessViewController.m
//  BMSQC
//
//  Created by gh on 15/11/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ScanSuccessViewController.h"

@implementation BMSQ_ScanSuccessViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
    
    
}

- (void)setViewUp {
    
    CGColorSpaceRef colorSpaceRef = CGColorSpaceCreateDeviceRGB();
    CGColorRef color = CGColorCreate(colorSpaceRef, (CGFloat[]){255,255,255,1});
    
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    imageView.image = [UIImage imageNamed:@"scan_meal"];
    [self.view addSubview:imageView];
    
    [self setNavBackItem];
    
    CGFloat y = APP_VIEW_CAN_USE_HEIGHT/3;
    UILabel *lb_top = [[UILabel alloc] initWithFrame:CGRectMake(0, y, APP_VIEW_WIDTH, 50)];
    lb_top.font = [UIFont systemFontOfSize:15];
    lb_top.textAlignment = NSTextAlignmentCenter;
    lb_top.textColor = UICOLOR(255, 255, 255, 1);
    lb_top.text = @"恭喜您扫码成功";
    [self.view addSubview:lb_top];
    
    y = y+50;
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/4, y, APP_VIEW_WIDTH/2, 30)];
    [btn setBackgroundColor:[UIColor clearColor]];
    btn.tag = 90001;
    [btn setTitle:@"返回加菜" forState:UIControlStateNormal];
    [btn setTitleColor:UICOLOR(255, 255, 255, 1) forState:UIControlStateNormal];
    [btn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [btn.layer setBorderWidth:2];
    [btn.layer setBorderColor:color];
    [self.view addSubview:btn];
    
    y = y+50;
    UIButton *btn_bottom = [[UIButton alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/4, y, APP_VIEW_WIDTH/2, 30)];
    [btn_bottom setBackgroundColor:[UIColor clearColor]];
    btn_bottom.tag = 90002;
    [btn_bottom setTitle:@"确认下单" forState:UIControlStateNormal];
    [btn_bottom addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [btn_bottom.layer setBorderWidth:2];
    [btn_bottom.layer setBorderColor:color];
    
    [self.view addSubview:btn_bottom];
    
    
}

- (void)setNavBackItem
{
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT ), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    [btnback addTarget:self action:@selector(goBack) forControlEvents:UIControlEventTouchUpInside];
    btnback.tag = 99;
    [self.view addSubview:btnback];
}

- (void)goBack {
    
    [self dismissViewControllerAnimated:NO completion:^{
        
    }];
    
}



- (void)btnAction:(UIButton *)sender {
    if (sender.tag == 90001) {
        NSLog(@"返回加菜");
        if (self.scansDelegate != nil) {
            [self dismissViewControllerAnimated:NO completion:^{
                [self.scansDelegate btnScanSuccessReturn];
                
            }];
            
        }
        
    }else if (sender.tag == 90002) {
        NSLog(@"确认下单");
        if (self.scansDelegate != nil) {
            [self dismissViewControllerAnimated:NO completion:^{
                [self.scansDelegate btnScanSuccessCon];
                
            }];
            
        }
    }
    
    
    
    
}

@end
