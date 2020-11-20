//
//  SDZX_cityObject.h
//  SDBooking
//
//  Created by djx on 14-3-3.
//  Copyright (c) 2014年 djx. All rights reserved.
//

/**********************************
 影院城市信息对象
 *********************************/
#import <Foundation/Foundation.h>


@interface SDZX_cityObject : NSObject

@property(nonatomic,strong)NSString* cityName;
@property(nonatomic,strong)NSString* defaultCity;
@property(nonatomic,strong)NSString* cityId;
@property(nonatomic,strong)NSString* ifshow;
@property(nonatomic,strong)NSString* city_firstWord;
@end
