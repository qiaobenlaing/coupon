//
//  BMSQ_QR.h
//  BMSQC
//
//  Created by liuqin on 15/9/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BMSQ_QR : NSObject

+(void )getZeroPay:(NSString *)userCouponCode shopCode:(NSString *)shopCode completed:(void(^)(NSDictionary *userInfo,NSString *errMsg))finished;

+(void )getPosPay:(NSString *)userCouponCode shopCode:(NSString *)shopCode platBonus:(NSString *)platBonus shopBonus:(NSString *)shopBonus price:(NSString *)price completed:(void(^)(NSDictionary *userInfo,NSString *errMsg))finished;


@end
