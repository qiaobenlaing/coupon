//
//  BMSQ_ScanViewController.m
//  BMSQC
//
//  Created by gh on 15/11/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ScanViewController.h"

@implementation BMSQ_ScanViewController

- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    [self setViewUp];
    
}

- (void)setViewUp {
    
    
    
    [self setNavHidden:YES];
    
    
    
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    imageView.image = [UIImage imageNamed:@"scan_meal"];
    [self.view addSubview:imageView];
    
    [self setNavBackItem];
    
    CGFloat y = APP_VIEW_CAN_USE_HEIGHT/3;
    UILabel *lb_top = [[UILabel alloc] initWithFrame:CGRectMake(0, APP_VIEW_CAN_USE_HEIGHT/3, APP_VIEW_WIDTH, 50)];
    lb_top.textColor = UICOLOR(255,255,255,1);
    lb_top.font = [UIFont systemFontOfSize:15];
    lb_top.textAlignment = NSTextAlignmentCenter;
    lb_top.text = @"扫描餐桌上的二维码";
    [self.view addSubview:lb_top];
    
    y = y+30;
    UILabel *lb_bottom = [[UILabel alloc] initWithFrame:CGRectMake(0, y, APP_VIEW_WIDTH, 50)];
    lb_bottom.textColor = UICOLOR(255,255,255,1);
    lb_bottom.font = [UIFont systemFontOfSize:12];
    lb_bottom.textAlignment = NSTextAlignmentCenter;
    lb_bottom.text = @"到店入座后，请扫描桌上的二维码下单";
    [self.view addSubview:lb_bottom];
    
    y = y+50;
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/4, y, APP_VIEW_WIDTH/2, 40)];
    [btn setBackgroundColor:[UIColor clearColor]];
    [btn setTitle:@"扫码" forState:UIControlStateNormal];
    [btn setTitleColor:UICOLOR(255,255,255,1) forState:UIControlStateNormal];
    [btn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [btn.layer setBorderWidth:2];//设置边界的宽度
    CGColorSpaceRef colorSpaceRef = CGColorSpaceCreateDeviceRGB();
    CGColorRef color = CGColorCreate(colorSpaceRef, (CGFloat[]){255,255,255,1});
    [btn.layer setBorderColor:color];
    
    [self.view addSubview:btn];
    
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


- (void)btnAction:(UIButton *)btn {
    
   
    if ([self.scDelegate respondsToSelector:@selector(btnClick)]) {
        [self dismissViewControllerAnimated:NO completion:^{
            [self.scDelegate btnClick];
        }];
        
    }

}


@end
