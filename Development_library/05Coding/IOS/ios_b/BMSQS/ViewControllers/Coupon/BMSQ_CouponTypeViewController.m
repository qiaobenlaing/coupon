//
//  BMSQ_CouponTypeViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/16.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponTypeViewController.h"

#import "BMSQ_couponSetViewController.h"


#import "SVProgressHUD.h"
#import "UIImageView+WebCache.h"
#import "UIButtonEx.h"
#import "UIButton+WebCache.h"
@interface BMSQ_CouponTypeViewController ()

@end

@implementation BMSQ_CouponTypeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavTitle:@"优惠券设置"];

    [self getCouponInfoByType];
    
    
}
-(void)setSelfView:(NSArray *)array{
    UIScrollView *sc = [[UIScrollView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y+5, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    sc.backgroundColor = [UIColor clearColor];
    [self.view addSubview:sc];
    
    int couponNum=0;
    CGFloat btnHeight = (APP_VIEW_WIDTH-10)*(180/692.0);
    for (int i=0;i<array.count ;i++) {
        NSDictionary *dic = [array objectAtIndex:i];
        int couponType = [[dic objectForKey:@"couponType"]intValue];
        
        if (couponType == 1 || couponType == 5 || couponType == 6) {
            continue;
        }
        
        
        
        UIButtonEx *button = [[UIButtonEx alloc]initWithFrame:CGRectMake(5, (btnHeight+10) * couponNum , APP_VIEW_WIDTH-10, btnHeight)];
        button.object = dic;
        button.tag = couponType;
        button.backgroundColor = [UIColor clearColor];
        [button addTarget:self action:@selector(gotoSetVC:) forControlEvents:UIControlEventTouchUpInside];
        [sc addSubview:button];
        
        [button sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,[dic objectForKey:@"couponImg"]]] forState:UIControlStateNormal placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
        couponNum++;

    }
        
    sc.contentSize = CGSizeMake(APP_VIEW_WIDTH, couponNum *(btnHeight+10));

    
//    if (array.count>0) {
//
//
//        int couponNum=0;
    
        
//            if (couponType == 7 || couponType == 8) {
//
//            }else {
//                [button setImage:[UIImage imageNamed:[imageNameS objectAtIndex:couponType]] forState:UIControlStateNormal];
//                couponNum++;
//            }


}


-(void)gotoSetVC:(UIButton *)button{
    
    NSArray *status = @[@"",@"N元购",@"",@"抵扣券",@"折扣券",@"实物券",@"体验券", @"兑换券", @"代金券"];

    BMSQ_couponSetViewController *vc = [[BMSQ_couponSetViewController alloc] init];
    vc.myTitle = [status objectAtIndex:button.tag];
    vc.type = (int)button.tag;
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
}
#pragma  mark --请求数据--
-(void)getCouponInfoByType{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"getCouponInfoByType" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"getCouponInfoByType" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [weakSelf setSelfView:responseObject];
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"err");
        [SVProgressHUD dismiss];
        
    }];
    
}


@end
