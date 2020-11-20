//
//  SDZX_cinemaObject.h
//  SDBooking
//
//  Created by djx on 14-3-4.
//  Copyright (c) 2014年 djx. All rights reserved.
//

//影院对象

#import <Foundation/Foundation.h>

@interface SDZX_cinemaObject : NSObject

@property(nonatomic,strong)NSString* cinema_code; //影院code
@property(nonatomic,strong)NSString* cinema_name; //影院名称
@property(nonatomic,strong)NSMutableArray* cinema_halls; //影院对应的影厅数组
@property(nonatomic,strong)NSString* cinema_address;
@property(nonatomic,strong)NSString* cinema_logo;
@property(nonatomic,strong)NSString* cinema_description;
@property(nonatomic,strong)NSString* cinema_bus;
@property(nonatomic,strong)NSString* cinema_phone;
@property(nonatomic,strong)NSString* cinema_openinghours;
@property(nonatomic,strong)NSString* cinema_mapx;
@property(nonatomic,strong)NSString* cinema_mapy;
@property(nonatomic,strong)NSMutableArray* cinema_moviesSession;//电影排期
@property(nonatomic,strong)NSMutableArray* cinema_movies; //播放的电影
@property(nonatomic,strong)NSString* cinema_lowPrice; //最低价
@property(nonatomic,strong)NSString* cinema_averagePrice; //平均价
@property(nonatomic,strong)NSMutableArray* cinema_pics;
@property(nonatomic,strong)NSString* cinema_notice; //公告
@property(nonatomic,strong)NSString* cinema_firmtip; //影院优惠信息
@end
