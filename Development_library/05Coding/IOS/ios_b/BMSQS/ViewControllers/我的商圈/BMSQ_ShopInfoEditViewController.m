//
//  BMSQ_ShopInfoEditViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/7/31.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopInfoEditViewController.h"

@interface BMSQ_ShopInfoEditViewController ()

@end

@implementation BMSQ_ShopInfoEditViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
//    [self setNavTitle:self.shopDataDic[@"title"]];
//    [self setNavBackItem];
    [self.navigationItem setTitle:self.shopDataDic[@"title"]];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    [self setNavBackItem];
    [self customRightBtn];

//    UIView *view = [self.view viewWithTag:100];
//    [view setBackgroundColor:[UIColor colorWithHexString:@"0xffffff"]];
    self.titleLabel.text = self.shopDataDic[@"title"];
//    self.textField.placeholder = [@"请输入" stringByAppendingString:self.shopDataDic[@"title"]];

}

- (void)customRightBtn
{
    UIButton *item = [UIButton buttonWithType:UIButtonTypeCustom];
    [item setTitle:@"完成" forState:UIControlStateNormal];
    
    item.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    [item addTarget:self action:@selector(didClickRightButton:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
//                      initWithTitle:@"完成" style:UIBarButtonItemStylePlain target:self action:@selector(didClickRightButton:) ];
//    [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.f],NSForegroundColorAttributeName: [UIColor whiteColor],} forState:UIControlStateNormal];
//    self.navigationItem.rightBarButtonItem = item;
    
}

- (void)didClickRightButton:(UIButton *)sender {
    
//    if (_delegate && [_delegate respondsToSelector:@selector(controller:DidSuccessEditInfo:)]) {
//        NSDictionary *info = @{@"key":self.shopDataDic[@"key"],@"value":self.textField.text};
//        [_delegate controller:self DidSuccessEditInfo:info];
//        [[NSNotificationCenter defaultCenter] postNotificationName:@"NotificationDidSuccessEditShopInfo" object:info];
//        [self.navigationController popViewControllerAnimated:YES];
//    }
    
}


@end
