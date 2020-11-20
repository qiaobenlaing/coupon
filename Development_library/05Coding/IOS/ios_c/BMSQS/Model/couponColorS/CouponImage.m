//
//  CouponImage.m
//  BMSQC
//
//  Created by liuqin on 15/12/24.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "CouponImage.h"

#import "ZXingObjC.h"
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>
#import "GTMDefines.h"
#import "GTMBase64.h"
#import "JSONKit.h"




@implementation CouponImage

+(NSString *)couponTypeName:(int)type{
    
    if(type == 1)
        return @"N元购";
    else if (type ==3)
        return @"抵扣券";
    else if (type ==4)
        return @"折扣券";
    else if (type ==5)
        return @"实物券";
    else if (type ==6)
        return @"体验券";
    else if (type ==7)
        return @"兑换券";
    else if (type ==8)
        return @"代金券";
    else
        return @"优惠券";
}
+(NSString *)couponType:(int)type{
    if(type == 1)
        return @"detail_nbuy";
    else if (type ==3)
        return @"detail_deduct";
    else if (type ==4)
        return @"detail_discount";
    else if (type ==5)
        return @"detail_real";
    else if (type ==6)
        return @"detail_experience";
    else if (type ==7)
        return @"detail_ex_voucher";
    else if (type ==8)
        return @"detail_voucher";
    else
    return @"detail_deduct";  ///
}

+(UIColor *)couponBgColor:(int)type{
    if(type == 1)
        return UICOLOR(238, 142, 41, 1);
    else if (type ==3)
        return UICOLOR(241, 101, 101, 1);
    else if (type ==4)
        return UICOLOR(241, 154, 26, 1);
    else if (type ==5)
        return UICOLOR(24, 204, 141, 1);
    else if (type ==6)
        return UICOLOR(127, 207, 51, 1);
    else if (type ==7)
        return UICOLOR(42, 129, 231, 1);
    else if (type ==8)
        return UICOLOR(225, 117, 79, 1);
    else
     return UICOLOR(241, 102, 98, 1);
}
+(UIColor *)couponBottomBgColor:(int)type{
    if(type == 1)
        return UICOLOR(216, 107, 18, 1);
     else if (type ==3)
        return UICOLOR(228, 40, 40, 1);
    else if (type ==4)
        return UICOLOR(288, 93, 4, 1);
    else if (type ==5)
        return UICOLOR(4, 163, 78, 1);
    else if (type ==6)
        return UICOLOR(63, 168, 10, 1);
    else if (type ==7)
        return UICOLOR(9, 88, 183, 1);
    else if (type ==8)
        return UICOLOR(228, 40, 40, 1);
    else
        return UICOLOR(228, 40, 40, 1);
    
    
}
+(UIImage *)couponBgImage:(int)type{
    if(type == 1)
        return [UIImage imageNamed:@"copon_1"];
    else if (type ==3)
        return [UIImage imageNamed:@"copon_3"];
    else if (type ==4)
        return [UIImage imageNamed:@"copon_4"];
    else if (type ==5)
        return [UIImage imageNamed:@"copon_5"];
    else if (type ==6)
        return [UIImage imageNamed:@"copon_6"];
    else if (type ==7)
        return [UIImage imageNamed:@"copon_7"];
    else if (type ==8)
        return [UIImage imageNamed:@"copon_8"];
    else
        return [UIImage imageNamed:@"copon_3"];
}
+(UIImage *)couponCodeQR:(NSString *)userCouponNbr{
    NSError *error = nil;
    ZXMultiFormatWriter *writer = [ZXMultiFormatWriter writer];
    ZXBitMatrix* result = [writer encode:userCouponNbr
                                  format:kBarcodeFormatCode128
                                   width:200
                                  height:25
                                   error:&error];
    
    if (result) {
        CGImageRef imageT = [[ZXImage imageWithMatrix:result] cgimage];
        UIImage* imageF = [UIImage imageWithCGImage: imageT];
        return imageF;
    }
    return [UIImage imageNamed:@""];

}
/*
+(void)UserCoupon:(NSDictionary *)dic viewController:(UIViewController *)vc{
    
    int type =(int)[[dic objectForKey:@"couponType"]integerValue];
    
    switch (type) {
        case 1:  //N元购
        {
            
            [self payCard:dic viewController:(UIViewController *)vc];
        }
            break;
        case 2:
        {
        }
            break;
        case 3:   //抵扣券 折扣券 一样
        {
            [self buyClick:dic viewController:(UIViewController *)vc];
            
        }
            break;
        case 4:   //折扣券 抵扣券 一样
        {
            [self buyClick:dic viewController:(UIViewController *)vc];
        }
            break;
        case 5:    //实物券 体验券 一样
        {
            [self gotoPay_secVC:dic viewController:(UIViewController *)vc];
        }
            break;
        case 6:
        {
            [self gotoPay_secVC:dic viewController:(UIViewController *)vc];
        }
            break;
        case 32:
        {
            [self buyClick:dic viewController:(UIViewController *)vc];
        }
            break;
        case 33:
        {
            [self buyClick:dic viewController:(UIViewController *)vc];
        }
            break;
        default:
            break;
    }
}
*/
+(NSString *)getOrderCouponStatus:(NSString *)type{
    int i = [type intValue];
    if (i == 11) {
        return @"已退款";
    }else if (i == 12) {
        return @"申请退款";
    }else if (i == 20) {
        return @"退款";
    }else if (i == 30) {
        return @"不可退款";
    }
    return @"";
    
}

@end
