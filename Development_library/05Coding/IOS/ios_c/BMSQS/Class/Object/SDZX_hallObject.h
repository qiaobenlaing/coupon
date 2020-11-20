//
//  SDZX_hallObject.h
//  SDBooking
//
//  Created by djx on 14-3-4.
//  Copyright (c) 2014年 djx. All rights reserved.
//

//影厅对象

#import <Foundation/Foundation.h>

@interface SDZX_hallObject : NSObject

@property(nonatomic,strong)NSString* hall_code;
@property(nonatomic,strong)NSString* hall_name;
@property(nonatomic,strong)NSString* hall_state; 
@property(nonatomic,strong)NSString* hall_rowcount;  //行数
@property(nonatomic,strong)NSString* hall_columncount; //列数
@property(nonatomic,strong)NSMutableArray* hall_seats; //座位数组

@end
