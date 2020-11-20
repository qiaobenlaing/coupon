//
//  ActiVityModel.m
//  BMSQC
//
//  Created by liuqin on 16/1/28.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ActiVityModel.h"

@implementation ActiVityModel

+(NSString *)getStatusStr:(NSString *)type{
    
    int i = [type intValue];
    switch (i) {
        case 0:
        {
            ;//return @"未付款，不可用";
        }
            break;
        case 1:
        {
            return @"退款中";

        }
            break;
        case 2:
        {
            return @"已退款";

        }
            break;
        case 3:
        {
            //return @"已验证";

        }
            break;
        case 4:
        {
            return @"退款";

        }
            break;
        default:
            break;
    }
    
    
    return @"";
}

@end
