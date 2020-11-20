//
//  BMSQ_listModel.m
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_circleModel.h"

@implementation BMSQ_circleModel

- (id)initWithInforDic:(NSDictionary *)inforDic{
    self = [super init];
    if (self) {
        [self setValuesForKeysWithDictionary:inforDic];
    }
    return self;
}
- (void)setValue:(id)value forUndefinedKey:(NSString *)key{
    
}

@end
