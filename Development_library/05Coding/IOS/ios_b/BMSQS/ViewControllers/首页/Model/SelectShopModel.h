//
//  SelectShopModel.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SelectShopModel : NSObject

@property (nonatomic, strong) NSNumber * count;
@property (nonatomic, strong) NSArray  * shopList;
@property (nonatomic, strong) NSNumber * totalCount;

- (id)initWithInforDic:(NSDictionary *)inforDic;

@end
