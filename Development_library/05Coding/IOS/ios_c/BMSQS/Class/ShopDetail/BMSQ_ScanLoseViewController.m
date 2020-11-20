//
//  BMSQ_ScanLoseViewController.m
//  BMSQC
//
//  Created by gh on 15/11/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ScanLoseViewController.h"

@implementation BMSQ_ScanLoseViewController

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
    
    CGFloat y = APP_VIEW_CAN_USE_HEIGHT/3;
    CGFloat width = APP_VIEW_WIDTH/2;
    CGFloat x = APP_VIEW_WIDTH/4;
    
    UILabel *lb_top = [[UILabel alloc] initWithFrame:CGRectMake(0, y, APP_VIEW_WIDTH, 50)];
    lb_top.font = [UIFont systemFontOfSize:15];
    lb_top.textAlignment = NSTextAlignmentCenter;
    lb_top.textColor =UICOLOR(255, 255, 255, 1);
    lb_top.text = @"抱歉，商户信息读取错误";
    [self.view addSubview:lb_top];
    
    y=y+30;
    UILabel *lb_buttom =[[UILabel alloc] initWithFrame:CGRectMake(x, y, APP_VIEW_WIDTH, 50)];
    lb_buttom.font = [UIFont systemFontOfSize:11];
    lb_buttom.textAlignment = NSTextAlignmentLeft;
    lb_buttom.textColor =UICOLOR(255, 255, 255, 1);
    lb_buttom.lineBreakMode = UILineBreakModeWordWrap;
    lb_buttom.numberOfLines = 0;
    lb_buttom.text = @"请仔细核实您所在的店铺和您正在点餐的\r商户是否一致";
    [self.view addSubview:lb_buttom];
    
    
    y = y+50;
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(x, y, width, 30)];
    [btn setBackgroundColor:[UIColor clearColor]];
    btn.tag = 90003;
    [btn setTitle:@"返回" forState:UIControlStateNormal];
    [btn setTitleColor:UICOLOR(255, 255, 255, 1) forState:UIControlStateNormal];
    [btn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [btn.layer setBorderWidth:2];
    [btn.layer setBorderColor:color];
    [self.view addSubview:btn];
    
    y = y+50;
    UIButton *btn_bottom = [[UIButton alloc] initWithFrame:CGRectMake(x, y, width, 30)];
    [btn_bottom setBackgroundColor:[UIColor clearColor]];
    btn_bottom.tag = 90004;
    [btn_bottom setTitle:@"重新扫描" forState:UIControlStateNormal];
    [btn_bottom setTitleColor:UICOLOR(255, 255, 255, 1) forState:UIControlStateNormal];
    [btn_bottom addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [btn_bottom.layer setBorderWidth:2];
    [btn_bottom.layer setBorderColor:color];
    [self.view addSubview:btn_bottom];
    
    
    
}


- (void)btnAction:(UIButton *)sender {
    if (sender.tag == 90003) {
        if (self.ScanLdelegate != nil) {
            [self dismissViewControllerAnimated:NO completion:^{
                [self.ScanLdelegate btnScanLReturn];
                
            }];
            
        }
        
    }else if (sender.tag == 90004) {
        if (self.ScanLdelegate != nil) {
            [self dismissViewControllerAnimated:NO completion:^{
                [self.ScanLdelegate btnScanLAgain];
                
            }];
        }
    }
    
    
    
}



@end
