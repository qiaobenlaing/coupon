//
//  SDZX_orderObejct.h
//  SDBooking
//
//  Created by djx on 14-4-17.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SDZX_orderObejct : NSObject

@property(nonatomic,strong)NSString* order_movieName;
@property(nonatomic,strong)NSString* order_orderCode;
@property(nonatomic,strong)NSString* order_cinemaName;
@property(nonatomic,strong)NSString* order_filmPlanCode; //排期
@property(nonatomic,strong)NSString* order_orderTime;//下单时间
@property(nonatomic,strong)NSString* order_filmPicUrl; // 影片图片
@property(nonatomic,strong)NSString* order_customerId; // 用户id
@property(nonatomic,strong)NSString* order_filmTypeName; //影片类型 2d 3d
@property(nonatomic,strong)NSString* order_qty; //购票张数
@property(nonatomic,strong)NSString* order_saleAmountPrice; //订单总价
@property(nonatomic,strong)NSString* order_discountAmountPrice; //订单支付价
@property(nonatomic,strong)NSString* order_filmCode;
@property(nonatomic,strong)NSString* order_hallName;
@property(nonatomic,strong)NSString* order_seatStr; //座位
@property(nonatomic,strong)NSString* order_playTime; //播放时间
@property(nonatomic,strong)NSString* order_cinemaCode;
@property(nonatomic,strong)NSString* order_voucherCode; //取票串码

@end
