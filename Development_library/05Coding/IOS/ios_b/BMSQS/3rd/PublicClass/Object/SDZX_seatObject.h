//
//  SDZX_seatObject.h
//  SDBooking
//
//  Created by djx on 14-2-17.
//  Copyright (c) 2014年 djx. All rights reserved.
//

/*
 座位对象
 */

#import <Foundation/Foundation.h>

/*
 座位详细信息对象
 */
@interface SDZX_seatDetailObject : NSObject

@property(nonatomic,strong)NSString* seat_code;
@property(nonatomic,strong)NSString* seat_colIndex; //列数的Index
@property(nonatomic,strong)NSString* seat_column; //列数
@property(nonatomic,strong)NSString* seat_row;
@property(nonatomic,strong)NSString* seat_status; //状态
@property(nonatomic,strong)NSString* seat_type; //类型
@property(nonatomic,strong)NSString* seat_loveSeat; //情侣座
@property(nonatomic,strong)NSString* seat_section; //普通区，vip区
@property(nonatomic,strong)NSString* seat_isSale; //座位是否已售 0表示未售，1表示已售
@property(nonatomic,strong)NSString* seat_weight; //座位的权值
@end

/*
 一行座位整体信息对象
 */
@interface SDZX_seatObject : NSObject


@property(nonatomic,strong)NSString* seat_seatCount;
@property(nonatomic,strong)NSString* seat_rowindex;
@property(nonatomic,strong)NSString* seat_seatrow;
@property(nonatomic,strong)NSMutableArray* seat_seatinfos; //具体座位信息,存储SDZX_seatDetailObject对象
@property(nonatomic,strong)NSString* seat_columncount;
@property(nonatomic,strong)NSString* seat_rowcount;

@end

/*
 已经选择的单个座位信息
 */
@interface SDZX_seatSelected : NSObject

@property(nonatomic,strong)NSString* seat_column; //座位列数
@property(nonatomic,strong)NSString* seat_status; //座位状态
@property(nonatomic,strong)NSString* seat_type; //座位类型，0表示普通座位，1表示情侣座
@property(nonatomic,strong)NSString* seat_row; //座位行数

@end
