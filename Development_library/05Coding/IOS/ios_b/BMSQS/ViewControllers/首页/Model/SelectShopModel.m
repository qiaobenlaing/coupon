//
//  SelectShopModel.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "SelectShopModel.h"

@implementation SelectShopModel

- (id)initWithInforDic:(NSDictionary *)inforDic
{
    self = [super init];
    if (self) {
        [self setValuesForKeysWithDictionary:inforDic];
    }
    return self;
}

- (void)setValue:(id)value forUndefinedKey:(NSString *)key
{
    
}


@end
