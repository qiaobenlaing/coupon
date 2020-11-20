//
//  CouponTypeModel.m
//  BMSQS
//
//  Created by gh on 15/12/15.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "CouponTypeModel.h"

@implementation CouponTypeModel


+ (id)createCoupon:(int)i {
    
    NSArray * coponbackColorS =  @[@"",@"253,118,75",@"",@"247, 101, 98", @"247, 151, 0", @"32,198,132", @"111,201,39", @"53,151,216", @"253,118,75"];
    NSArray * coponImageS =  @[@"",@"coupon_1",@"",@"coupon_3",@"coupon_4",@"coupon_5",@"coupon_6", @"coupon_7", @"coupon_1"];
    NSArray * coponBottomColorS= @[@"",@"251,56,21",@"",@"228,40,41",@"227,93,3",@"21,152,61",@"53,155,12", @"9,88,183", @"251,56,21"];
    NSArray * status = @[@"",@"N元购",@"",@"抵扣券",@"折扣券",@"实物券",@"体验券",@"兑换券", @"代金券"];
    
    NSMutableDictionary *mutableDic = [[NSMutableDictionary alloc] init];
    
    
    [mutableDic setObject:coponbackColorS[i] forKey:@"backColor"];
    [mutableDic setObject:coponImageS[i] forKey:@"image"];
    [mutableDic setObject:coponBottomColorS[i] forKey:@"bottomColor"];
    [mutableDic setObject:status[i] forKey:@"status"];
    
    return mutableDic;
    
    


}


@end
