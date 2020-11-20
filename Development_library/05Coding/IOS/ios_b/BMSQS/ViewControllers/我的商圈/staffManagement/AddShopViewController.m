//
//  AddShopViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/31.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "AddShopViewController.h"

@interface AddShopViewController ()<UITableViewDataSource,UITableViewDelegate>

@end

@implementation AddShopViewController

- (void)viewDidLoad {
    [super viewDidLoad];

   self.title = @"门面设置";
    
    [self setNavTitle:@"门面设置"];
    [self setNavBackItem];
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT - APP_STATUSBAR_HEIGHT), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    [btnback setTitle:@"保存" forState:UIControlStateNormal];
    [btnback setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [btnback addTarget:self action:@selector(clickSaveMess) forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem *backButtonItem = [[UIBarButtonItem alloc] initWithCustomView:btnback];
    self.navigationItem.rightBarButtonItem = backButtonItem;

}






-(void)clickSaveMess{
    
    NSArray *array = self.navigationController.viewControllers;
    [self.navigationController popToViewController:[array objectAtIndex:array.count-3] animated:YES];
    
}

@end
