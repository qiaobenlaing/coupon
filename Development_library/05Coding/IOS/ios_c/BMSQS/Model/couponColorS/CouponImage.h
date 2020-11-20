//
//  CouponImage.h
//  BMSQC
//
//  Created by liuqin on 15/12/24.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CouponImage : NSObject

+(NSString *)couponTypeName:(int)type;
+(NSString *)couponType:(int)type;
+(UIColor *)couponBgColor:(int)type;
+(UIColor *)couponBottomBgColor:(int)type;
+(UIImage *)couponBgImage:(int)type;
+(UIImage *)couponCodeQR:(NSString *)userCouponNbr;
//+(void)UserCoupon:(NSDictionary *)dic;
+(NSString *)getOrderCouponStatus:(NSString *)type;
@end
