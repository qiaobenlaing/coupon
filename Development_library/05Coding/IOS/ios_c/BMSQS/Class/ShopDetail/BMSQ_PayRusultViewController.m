//
//  BMSQ_PayRusultViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/9.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_PayRusultViewController.h"

@interface BMSQ_PayRusultViewController ()

@end

@implementation BMSQ_PayRusultViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"支付结果"];

    
    
    UIView *resultView = [[UIView alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y+20, APP_VIEW_WIDTH-20, 80)];
    resultView.layer.borderWidth =1;
    resultView.layer.borderColor = [[UIColor colorWithRed:247/255.0 green:202/255.0 blue:204/255.0 alpha:1]CGColor];
    resultView.backgroundColor = [UIColor colorWithRed:254/255.0 green:232/255.0 blue:228/255.0 alpha:1];
    resultView.layer.cornerRadius = 5;
    resultView.layer.masksToBounds = YES;
    
    [self.view addSubview:resultView];
    UILabel *resultLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, resultView.frame.size.width-15, 40)];
    resultLabel.backgroundColor = [UIColor clearColor];
    resultLabel.font = [UIFont systemFontOfSize:13.f];
    resultLabel.textColor = [UIColor blackColor];
    [resultView addSubview:resultLabel];
    
    
    if ([self.code isEqualToString:@"50000"]) {
        
        resultLabel.text = @"谢谢你的配合，支付成功";
        
    }else{
        resultLabel.text = @"抱歉，支付失败";
        UILabel *contentLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 40, resultView.frame.size.width, 40)];
        contentLabel.numberOfLines = 0;
        contentLabel.backgroundColor = [UIColor clearColor];
        contentLabel.textColor = [UIColor grayColor];
        contentLabel.font = [UIFont systemFontOfSize:12.f];
        [resultView addSubview:contentLabel];
        
        if ([self.code isEqualToString:@"20000"]) {
            contentLabel.text = @"2000失败  请检查您的网络是否顺畅。如有疑问，请联系客服热线：400-04-95588";

        }else if ([self.code isEqualToString:@"50056"]) {
            contentLabel.text = @"银行账户为空";

        }else if ([self.code isEqualToString:@"50057"]) {
            contentLabel.text = @"银行账户编码错误";

        }else if ([self.code isEqualToString:@"50900"]) {
            contentLabel.text = @"消费订单不存在";

        }else{
            contentLabel.text =  [NSString stringWithFormat:@"%@[%@]",self.retmsg,self.code];

        }
    }

}



@end
