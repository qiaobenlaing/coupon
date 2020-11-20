//
//  HQAccountBookViewController.m
//  BMSQS
//
//  Created by gh on 16/2/22.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "HQAccountBookViewController.h"
#import "AccountBookScrollView.h"

@interface HQAccountBookViewController () {
    AccountBookScrollView *scrollView;
}





@end

@implementation HQAccountBookViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [self setViewUp];
    
    
}

- (void)setViewUp {
    [self setNavBackItem];
    [self setNavTitle:@"惠圈账本"];
    
    
    scrollView = [[AccountBookScrollView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    scrollView.backgroundColor = UICOLOR(239, 239, 246, 1);
    [self.view addSubview:scrollView];
    
    
    [self getHqBook];
    
}

- (void)getHqBook {
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"]; //shopCode
    NSString* vcode = [gloabFunction getSign:@"getHqBook" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"getHqBook" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        //        NSLog(@"%@",responseObject);
        
        [scrollView setViewLabel:responseObject];
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        NSLog(@"%@",error);
        
    }];
    
    
}






@end
