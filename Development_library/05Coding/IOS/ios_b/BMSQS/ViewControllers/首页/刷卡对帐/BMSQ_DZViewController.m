//
//  BMSQ_DZViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/8/24.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_DZViewController.h"
#import "BMSQ_CouponDZViewController.h"

#import "BMSQ_CarFeedViewController.h"
#import "BMSQ_CouponFeedViewController.h"
@interface BMSQ_DZViewController ()

@end

@implementation BMSQ_DZViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"惠圈对帐"];
    [self setNavBackItem];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (IBAction)didClickYXButton:(id)sender {

//    BMSQ_CouponDZViewController *pushVC = [[BMSQ_CouponDZViewController alloc] init];
//    [self.navigationController pushViewController:pushVC animated:YES];

}

#pragma mark -- 银行卡付费--
- (IBAction)didClickFFButton:(id)sender {
    BMSQ_CarFeedViewController *pushVC = [[BMSQ_CarFeedViewController alloc] init];
     [self.navigationController pushViewController:pushVC animated:YES];

}

#pragma mark -- 优惠券--
- (IBAction)didClickCouponButton:(id)sender {
    BMSQ_CouponFeedViewController *pushVC = [[BMSQ_CouponFeedViewController alloc] init];
    [self.navigationController pushViewController:pushVC animated:YES];


}



@end
