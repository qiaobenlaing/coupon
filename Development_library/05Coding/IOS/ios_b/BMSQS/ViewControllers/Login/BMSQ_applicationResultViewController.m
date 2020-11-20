//
//  BMSQ_applicationResultViewController.m
//  BMSQS
//
//  Created by liuqin on 15/11/20.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_applicationResultViewController.h"

@interface BMSQ_applicationResultViewController ()

@end

@implementation BMSQ_applicationResultViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self.navigationItem setTitle:@"申请成功"];


    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(30, 5, APP_VIEW_WIDTH-60, APP_VIEW_HEIGHT/2)];
    label.text = @"开店申请成功后，将会有惠圈工作人员上门注册签约";
    label.numberOfLines = 0;
    label.textColor = UICOLOR(1, 1, 1, 1);
    
    [self.view addSubview:label];
    
    
    
    
    
     UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(15, APP_VIEW_HEIGHT/2,APP_VIEW_WIDTH-30, 45)];
    button.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [button setTitle:@"返回登录页" forState:UIControlStateNormal];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    button.titleLabel.font = [UIFont boldSystemFontOfSize:17];
    button.layer.cornerRadius = 5;
    button.layer.masksToBounds = YES;
    button.center = CGPointMake(APP_VIEW_WIDTH/2, APP_VIEW_HEIGHT/2-20);
    [self.view addSubview:button];
    
    [button addTarget:self action:@selector(clickSubmit) forControlEvents:UIControlEventTouchUpInside];
    
    
}
-(void)clickSubmit{
    
    if (self.isLogin) {
        [self.navigationController popToRootViewControllerAnimated:YES];
    }else{
        [self dismissViewControllerAnimated:YES completion:nil];
    }
    

}






@end
